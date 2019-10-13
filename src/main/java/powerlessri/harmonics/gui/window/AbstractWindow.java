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

    private IWidget focusedWidget;

    public AbstractWindow() {
        this.position = new Point();
        this.contents = new Dimension();
        this.border = new Dimension();
    }

    @Override
    public float getZLevel() {
        return Render2D.REGULAR_WINDOW_Z;
    }

    @Override
    public Dimension getBorder() {
        return border;
    }

    @Override
    public Dimension getContents() {
        return contents;
    }

    protected final boolean mouseClickSubtree(double mouseX, double mouseY, int button) {
        if (button == GLFW_MOUSE_BUTTON_RIGHT) {
            for (IWidget child : getChildren()) {
                if (child.isInside(mouseX, mouseY) && child instanceof AbstractWidget) {
                    ((AbstractWidget) child).createContextMenu(mouseX, mouseY);
                }
            }
        }
        return NestedEventHandlerMixin.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return mouseClickSubtree(mouseX, mouseY, button) || isInside(mouseX, mouseY);
    }

    protected final boolean mouseReleasedSubtree(double mouseX, double mouseY, int button) {
        return NestedEventHandlerMixin.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return mouseReleasedSubtree(mouseX, mouseY, button) || isInside(mouseX, mouseY);
    }

    protected final boolean mouseDraggedSubtree(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return NestedEventHandlerMixin.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return mouseDraggedSubtree(mouseX, mouseY, button, deltaX, deltaY) || isInside(mouseX, mouseY);
    }

    protected final boolean mouseScrolledSubtree(double mouseX, double mouseY, double scroll) {
        return NestedEventHandlerMixin.super.mouseScrolled(mouseX, mouseY, scroll);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        return mouseScrolledSubtree(mouseX, mouseY, scroll) || isInside(mouseX, mouseY);
    }

    public void setContents(int width, int height) {
        contents.width = width;
        contents.height = height;
        int borderSize = getBorderSize();
        border.width = borderSize + width + borderSize;
        border.height = borderSize + height + borderSize;
        onResize();
    }

    public void setBorder(int width, int height) {
        border.width = width;
        border.height = height;
        int borderSize = getBorderSize();
        contents.width = width - borderSize * 2;
        contents.height = height - borderSize * 2;
        onResize();
    }

    public void centralize() {
        position.x = scaledWidth() / 2 - getWidth() / 2;
        position.y = scaledHeight() / 2 - getHeight() / 2;
        updatePosition();
    }

    protected void onResize() {
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

    public void renderVanillaStyleBackground() {
        BackgroundRenderers.drawVanillaStyle(getX(), getY(), getWidth(), getHeight(), getZLevel());
    }

    public int createVanillaStyleDL() {
        return DisplayListCaches.createVanillaStyleBackground(getX(), getY(), getWidth(), getHeight(), getZLevel());
    }

    public void renderFlatStyleBackground() {
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
