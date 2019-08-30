package powerlessri.harmonics.gui.actionmenu;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import powerlessri.harmonics.gui.IWidget;
import powerlessri.harmonics.gui.RenderingHelper;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.window.IPopupWindow;
import powerlessri.harmonics.gui.window.mixin.NestedEventHandlerMixin;
import powerlessri.harmonics.gui.window.mixin.WindowOverlayInfoMixin;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class ActionMenu implements IPopupWindow, NestedEventHandlerMixin, WindowOverlayInfoMixin {

    public static ActionMenu atCursor(double mouseX, double mouseY, List<? extends IEntry> entries) {
        return new ActionMenu((int) mouseX, (int) mouseY, entries);
    }

    private final Point position;
    private final List<? extends IEntry> entries;
    private final List<? extends Section> sections;
    private IEntry focusedEntry;

    private final Dimension contents;
    private final Dimension border;

    private boolean alive = true;

    public ActionMenu(int x, int y, List<? extends IEntry> entries) {
        this(new Point(x, y), entries);
    }

    public ActionMenu(Point position, List<? extends IEntry> entries) {
        Preconditions.checkArgument(!entries.isEmpty());

        this.position = position;
        this.entries = entries;
        this.contents = new Dimension();
        this.contents.width = entries.stream()
                .max(Comparator.comparingInt(IEntry::getFullWidth))
                .orElseThrow(IllegalArgumentException::new)
                .getFullWidth();
        this.contents.height = entries.stream()
                .mapToInt(IEntry::getFullHeight)
                .sum();
        this.border = RenderingHelper.toBorder(contents, getBorderSize());

        Section section = new Section();
        // Safe downwards erasure cast
        @SuppressWarnings("unchecked") List<IEntry> c = (List<IEntry>) entries;
        section.addChildren(c);
        this.sections = ImmutableList.of(section);

        int y = 0;
        for (IEntry e : entries) {
            e.attach(this);
            e.setLocation(0, y);
            y += e.getFullHeight();
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        RenderingHelper.drawRect(position, border, 75, 75, 75, 255);
        RenderingHelper.drawRect(getContentX(), getContentY(), contents, 61, 61, 61, 255);
        for (IEntry entry : entries) {
            entry.render(mouseX, mouseY, particleTicks);
        }
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    @Nullable
    @Override
    public IWidget getFocusedWidget() {
        return focusedEntry;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isInside(mouseX, mouseY)) {
            alive = false;
        }
        return false;
    }

    @Override
    public void setFocusedWidget(@Nullable IWidget widget) {
        if (widget instanceof IEntry || widget == null) {
            if (focusedEntry != null) {
                focusedEntry.onFocusChanged(false);
            }
            focusedEntry = (IEntry) widget;
            if (widget != null) {
                widget.onFocusChanged(true);
            }
        }
    }

    public boolean isLastSection(Section section) {
        return Iterables.getLast(sections) == section;
    }

    @Override
    public Dimension getBorder() {
        return border;
    }

    @Override
    public Dimension getContents() {
        return contents;
    }

    @Override
    public List<? extends IEntry> getChildren() {
        return entries;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public int getBorderSize() {
        return 1;
    }

    @Override
    public boolean shouldDiscard() {
        return !alive;
    }

}
