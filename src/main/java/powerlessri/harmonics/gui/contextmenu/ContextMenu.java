package powerlessri.harmonics.gui.contextmenu;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.window.IPopupWindow;
import powerlessri.harmonics.gui.window.mixin.*;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

import static powerlessri.harmonics.gui.Render2D.*;

public class ContextMenu implements IPopupWindow, WindowEventHandlerMixin, WindowOverlayInfoMixin, WindowPropertiesMixin {

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
    private final Dimension border;
    private final List<? extends Section> sections;
    private IEntry focusedEntry;

    private boolean alive = true;

    public ContextMenu(int x, int y, List<? extends Section> sections) {
        this(new Point(x, y), sections);
    }

    public ContextMenu(Point position, List<? extends Section> sections) {
        this.position = position;
        this.sections = sections;
        this.border = new Dimension();
        for (Section section : sections) {
            section.attach(this);
        }
        reflow();
    }

    public void reflow() {
        int width = 0;
        int height = 0;
        int y = 0;
        for (Section section : sections) {
            section.setLocation(0, y);
            section.reflow();
            y += section.getFullHeight();

            width = Math.max(width, section.getFullWidth());
            height += section.getFullHeight();
        }
        int border = getBorderSize();
        this.border.width = border + width + border;
        this.border.height = border + height + border;

        for (Section section : sections) {
            section.setWidth(width);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);

        GlStateManager.disableTexture();
        beginColoredQuad();
        int x = getX();
        int y = getY();
        coloredRect(x, y, x + getWidth(), y + getHeight(), getZLevel(), 0xff4b4b4b);
        int cx = getContentX();
        int cy = getContentY();
        coloredRect(cx, cy, cx + getContentWidth(), cy + getContentHeight(), getZLevel(), 0xff3d3d3d);
        draw();
        GlStateManager.enableTexture();

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
        WindowEventHandlerMixin.super.mouseClicked(mouseX, mouseY, button);
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
    public List<? extends Section> getChildren() {
        return sections;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public float getZLevel() {
        return Render2D.CONTEXT_MENU_Z;
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
