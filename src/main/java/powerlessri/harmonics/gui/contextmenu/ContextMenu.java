package powerlessri.harmonics.gui.contextmenu;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import powerlessri.harmonics.gui.IWidget;
import powerlessri.harmonics.gui.RenderingHelper;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.window.IPopupWindow;
import powerlessri.harmonics.gui.window.mixin.NestedEventHandlerMixin;
import powerlessri.harmonics.gui.window.mixin.WindowOverlayInfoMixin;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class ContextMenu implements IPopupWindow, NestedEventHandlerMixin, WindowOverlayInfoMixin {

    public static ContextMenu withEntries(List<? extends IEntry> entries) {
        return withSections(toSections(entries));
    }

    public static ContextMenu withSections(List<? extends Section> sections) {
        MouseHelper m = Minecraft.getInstance().mouseHelper;
        double scale = Minecraft.getInstance().mainWindow.getGuiScaleFactor();
        double mouseX = m.getMouseX() / scale;
        double mouseY = m.getMouseY() / scale;
        return withSections(mouseX, mouseY, sections);
    }

    public static ContextMenu withSections(double mouseX, double mouseY, List<? extends Section> sections) {
        return new ContextMenu((int) mouseX, (int) mouseY, sections);
    }

    public static ContextMenu withEntries(double mouseX, double mouseY, List<? extends IEntry> entries) {
        return new ContextMenu((int) mouseX, (int) mouseY, toSections(entries));
    }

    private static List<? extends Section> toSections(List<? extends IEntry> entries) {
        Section section = new Section();
        // Safe downwards erasure cast
        @SuppressWarnings("unchecked") List<IEntry> c = (List<IEntry>) entries;
        section.addChildren(c);
        return ImmutableList.of(section);
    }

    private final Point position;
    private final List<? extends Section> sections;
    private IEntry focusedEntry;

    private final Dimension contents;
    private final Dimension border;

    private boolean alive = true;

    public ContextMenu(int x, int y, List<? extends Section> sections) {
        this(new Point(x, y), sections);
    }

    public ContextMenu(Point position, List<? extends Section> sections) {
        Preconditions.checkArgument(!sections.isEmpty());

        this.position = position;
        this.sections = sections;
        this.contents = new Dimension();
        this.border = new Dimension();
        for (Section section : sections) {
            section.attach(this);
        }
        reflow();
    }

    public void reflow() {
        contents.width = 0;
        contents.height = 0;
        int y = 0;
        for (Section section : sections) {
            section.setLocation(0, y);
            section.reflow();
            y += section.getFullHeight();

            contents.width = Math.max(contents.width, section.getFullWidth());
            contents.height += section.getFullHeight();
        }
        int border = getBorderSize();
        this.border.width = border + contents.width + border;
        this.border.height = border + contents.height + border;

        for (Section section : sections) {
            section.setWidth(contents.width);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        RenderingHelper.drawRect(position, border, 75, 75, 75, 255);
        RenderingHelper.drawRect(getContentX(), getContentY(), contents, 61, 61, 61, 255);
        for (Section section : sections) {
            section.render(mouseX, mouseY, particleTicks);
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
        NestedEventHandlerMixin.super.mouseClicked(mouseX, mouseY, button);
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
    public List<? extends Section> getChildren() {
        return sections;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public int getBorderSize() {
        return 1;
    }

    public void discard() {
        alive = false;
    }

    @Override
    public boolean shouldDiscard() {
        return !alive;
    }
}
