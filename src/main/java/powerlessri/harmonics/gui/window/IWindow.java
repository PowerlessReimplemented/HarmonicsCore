package powerlessri.harmonics.gui.window;

import net.minecraft.client.gui.IRenderable;
import powerlessri.harmonics.gui.widget.IWidget;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public interface IWindow extends IRenderable {

    Dimension getBorder();

    int getWidth();

    int getHeight();

    int getBorderSize();

    int getContentWidth();

    int getContentHeight();

    List<? extends IWidget> getChildren();

    Point getPosition();

    float getZLevel();

    void setPosition(int x, int y);

    int getX();

    int getY();

    int getXRight();

    int getYBottom();

    int getContentX();

    int getContentY();

    int getContentXRight();

    int getContentYBottom();

    @Override
    void render(int mouseX, int mouseY, float particleTicks);

    @Nullable
    IWidget getFocusedWidget();

    /**
     * Change which widget is focused.
     * <p>
     * When possible, use {@link #changeFocus(IWidget, boolean)} instead.
     *
     * @implSpec This method should invoke {@link IWidget#onFocusChanged(boolean)} on both the parameter and the focused element as long as
     * they are nonnull.
     */
    void setFocusedWidget(@Nullable IWidget widget);

    /**
     * Helper method to set focus of a specific element. Notice this would cancel the originally focus element. Implementations should not
     * override this method unless it is using a special focus handler that is not compatible with the default implementation of this
     * method.
     */
    default boolean changeFocus(IWidget widget, boolean focus) {
        if (focus && widget.isEnabled()) {
            setFocusedWidget(widget);
            return true;
        } else {
            setFocusedWidget(null);
            return false;
        }
    }

    void onRemoved();

    default boolean isInside(double x, double y) {
        int selfX = getX();
        int selfY = getY();
        int selfXBR = selfX + getWidth();
        int selfYBR = selfY + getHeight();
        return x >= selfX &&
                x < selfXBR &&
                y >= selfY &&
                y < selfYBR;
    }

    /**
     * @implSpec Propagate the event and pass it on to the children.
     * @implNote This method will be invoked regardless of state (e.g. whether the cursor is inside or not). Event capture method. Return
     * {@code true} to stop propagation of the event to other widgets, otherwise the process would continue. Note that this is valid only
     * when the GUI is handling more than one windows.
     */
    boolean mouseClicked(double mouseX, double mouseY, int button);

    /**
     * @implSpec Propagate the event and pass it on to the children.
     * @implNote This method will be invoked regardless of state (e.g. whether the cursor is inside or not). Event capture method. Return
     * {@code true} to stop propagation of the event to other widgets, otherwise the process would continue. Note that this is valid only
     * when the GUI is handling more than one windows.
     */
    boolean mouseReleased(double mouseX, double mouseY, int button);

    /**
     * @implSpec Propagate the event and pass it on to the children.
     * @implNote This method will be invoked regardless of state (e.g. whether the cursor is inside or not). Event capture method. Return
     * {@code true} to stop propagation of the event to other widgets, otherwise the process would continue. Note that this is valid only
     * when the GUI is handling more than one windows.
     */
    boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY);

    /**
     * @implSpec Propagate the event and pass it on to the children.
     * @implNote This method will be invoked regardless of state (e.g. whether this window is focused or not). Event capture method. Return
     * {@code true} to stop propagation of the event to other widgets, otherwise the process would continue. Note that this is valid only
     * when the GUI is handling more than one windows.
     */
    boolean mouseScrolled(double mouseX, double mouseY, double scroll);

    /**
     * @implSpec Propagate the event and pass it on to the children.
     * @implNote This method will be invoked regardless of state (e.g. whether this window is focused or not). Event capture method. Return
     * {@code true} to stop propagation of the event to other widgets, otherwise the process would continue. Note that this is valid only
     * when the GUI is handling more than one windows.
     */
    boolean keyPressed(int keyCode, int scanCode, int modifiers);

    /**
     * @implSpec Propagate the event and pass it on to the children.
     * @implNote This method will be invoked regardless of state (e.g. whether this window is focused or not). Event capture method. Return
     * {@code true} to stop propagation of the event to other widgets, otherwise the process would continue. Note that this is valid only
     * when the GUI is handling more than one windows.
     */
    boolean keyReleased(int keyCode, int scanCode, int modifiers);

    /**
     * @implSpec Propagate the event and pass it on to the children.
     * @implNote This method will be invoked regardless of state (e.g. whether this window is focused or not). Event capture method. Return
     * {@code true} to stop propagation of the event to other widgets, otherwise the process would continue. Note that this is valid only
     * when the GUI is handling more than one windows.
     */
    boolean charTyped(char charTyped, int keyCode);

    void mouseMoved(double mouseX, double mouseY);

    void update(float particleTicks);
}
