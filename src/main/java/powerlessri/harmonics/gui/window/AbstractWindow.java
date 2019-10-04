package powerlessri.harmonics.gui.window;

import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.screen.BackgroundRenderers;
import powerlessri.harmonics.gui.screen.DisplayListCaches;
import powerlessri.harmonics.gui.widget.AbstractWidget;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.window.mixin.NestedEventHandlerMixin;
import powerlessri.harmonics.gui.window.mixin.WindowOverlayInfoMixin;

import javax.annotation.Nullable;
import java.awt.*;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static powerlessri.harmonics.gui.Render2D.scaledHeight;
import static powerlessri.harmonics.gui.Render2D.scaledWidth;

public abstract class AbstractWindow implements IWindow, NestedEventHandlerMixin, WindowOverlayInfoMixin {

    private final Point position;
    private final Dimension contents;
    private final Dimension border;
    private final float z = Render2D.REGULAR_WINDOW_Z;

    private IWidget focusedWidget;

    public AbstractWindow() {
        this.position = new Point();
        this.contents = new Dimension();
        this.border = new Dimension();
    }

    @Override
    public float getZLevel() {
        return z;
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
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW_MOUSE_BUTTON_RIGHT) {
            for (IWidget child : getChildren()) {
                if (child.isInside(mouseX, mouseY) && child instanceof AbstractWidget) {
                    ((AbstractWidget) child).createContextMenu(mouseX, mouseY);
                }
            }
        }
        return NestedEventHandlerMixin.super.mouseClicked(mouseX, mouseY, button);
    }

    public void setContents(int width, int height) {
        contents.width = width;
        contents.height = height;
        int borderSize = getBorderSize();
        border.width = borderSize + width + borderSize;
        border.height = borderSize + height + borderSize;
    }

    public void setBorder(int width, int height) {
        border.width = width;
        border.height = height;
        int borderSize = getBorderSize();
        contents.width = width - borderSize * 2;
        contents.height = height - borderSize * 2;
    }

    public void centralize() {
        position.x = scaledWidth() / 2 - getWidth() / 2;
        position.y = scaledHeight() / 2 - getHeight() / 2;
        updatePosition();
    }

    @Override
    public void setPosition(int x, int y) {
        NestedEventHandlerMixin.super.setPosition(x, y);
        updatePosition();
    }

    protected final void updatePosition() {
        if (getChildren() != null) {
            for (IWidget child : getChildren()) {
                child.onParentPositionChanged();
            }
        }
    }

    protected final void renderChildren(int mouseX, int mouseY, float particleTicks) {
        for (IWidget child : getChildren()) {
            child.render(mouseX, mouseY, particleTicks);
        }
    }

    public void drawVanillaStyleBackground() {
        BackgroundRenderers.drawVanillaStyle(getX(), getY(), getWidth(), getHeight(), getZLevel());
    }

    public int createVanillaStyleDL() {
        return DisplayListCaches.createVanillaStyleBackground(getX(), getY(), getWidth(), getHeight(), getZLevel());
    }

    public void drawFlatStyleBackground() {
        BackgroundRenderers.drawFlatStyle(getX(), getY(), getWidth(), getHeight(), getZLevel());
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Nullable
    @Override
    public IWidget getFocusedWidget() {
        return focusedWidget;
    }

    @Override
    public void setFocusedWidget(@Nullable IWidget widget) {
        focusedWidget = widget;
    }
}
