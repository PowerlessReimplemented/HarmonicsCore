package powerlessri.harmonics.gui.widget;

import powerlessri.harmonics.gui.window.IWindow;

import java.awt.*;

public interface IWidget {

    /**
     * Local coordinate relative to the parent component
     */
    Point getPosition();

    Insets getBorders();

    int getBorderTop();

    int getBorderRight();

    int getBorderBottom();

    int getBorderLeft();

    void setBorderTop(int top);

    void setBorderRight(int right);

    void setBorderBottom(int bottom);

    void setBorderLeft(int left);

    void setBorders(int top, int right, int bottom, int left);

    default void setBorders(int borders) {
        setBorders(borders, borders, borders, borders);
    }

    int getX();

    int getY();

    int getInnerX();

    int getInnerY();

    int getAbsoluteX();

    int getAbsoluteY();

    int getOuterAbsoluteX();

    int getOuterAbsoluteY();

    default void setLocation(Point point) {
        setLocation(point.x, point.y);
    }

    default void setLocation(int x, int y) {
        getPosition().x = x;
        getPosition().y = y;
        onRelativePositionChanged();
    }

    default void setX(int x) {
        getPosition().x = x;
        onRelativePositionChanged();
    }

    default void setY(int y) {
        getPosition().y = y;
        onRelativePositionChanged();
    }

    Dimension getDimensions();

    int getWidth();

    int getHeight();

    int getFullWidth();

    int getFullHeight();

    /**
     * Render the widget. Implementations may assume tessellator finished drawing, and the following GL states are in the given mode.
     * <ul>
     * <li>depthTest: enabled
     * <li>alphaTest: enabled
     * <li>texture: enabled
     * </ul>
     */
    void render(int mouseX, int mouseY, float particleTicks);

    float getZLevel();

    IWidget getParent();

    IWindow getWindow();

    /**
     * Get whether this widget is enabled or not.
     * <p>
     * An enabled widgets means it will render and interact with user inputs. On the other hand, a disabled widgets always means it does not
     * interact with user inputs (i.e. all event methods return {@code false}) but whether to render or not is up to the implementation.
     */
    boolean isEnabled();

    void setEnabled(boolean enabled);

    boolean isFocused();

    default void onFocusChanged(boolean focus) {
    }

    default void onRemoved() {
    }

    /**
     * Attach and validate this widget.
     *
     * @implSpec Calling this method should update the value returned by {@link #getParent()} and trigger {@link
     * #onParentPositionChanged()}.
     */
    void attach(IWidget newParent);

    boolean isValid();

    void onParentPositionChanged();

    void onRelativePositionChanged();

    /**
     * @implNote Event capture method. Return {@code true} to stop propagation of the event to other widgets, otherwise the process would
     * continue.
     */
    boolean isInside(double x, double y);

    /**
     * @implNote Event capture method. Return {@code true} to stop propagation of the event to other widgets, otherwise the process would
     * continue.
     */
    boolean mouseClicked(double mouseX, double mouseY, int button);

    /**
     * @implNote Event capture method. Return {@code true} to stop propagation of the event to other widgets, otherwise the process would
     * continue.
     */
    boolean mouseReleased(double mouseX, double mouseY, int button);

    /**
     * @implNote Event capture method. Return {@code true} to stop propagation of the event to other widgets, otherwise the process would
     * continue.
     */
    boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY);

    /**
     * @implNote Event capture method. Return {@code true} to stop propagation of the event to other widgets, otherwise the process would
     * continue.
     */
    boolean mouseScrolled(double mouseX, double mouseY, double scroll);

    /**
     * @implNote Event capture method. Return {@code true} to stop propagation of the event to other widgets, otherwise the process would
     * continue.
     */
    boolean keyPressed(int keyCode, int scanCode, int modifiers);

    /**
     * @implNote Event capture method. Return {@code true} to stop propagation of the event to other widgets, otherwise the process would
     * continue.
     */
    boolean keyReleased(int keyCode, int scanCode, int modifiers);

    /**
     * @implNote Event capture method. Return {@code true} to stop propagation of the event to other widgets, otherwise the process would
     * continue.
     */
    boolean charTyped(char charTyped, int keyCode);

    void mouseMoved(double mouseX, double mouseY);

    void update(float particleTicks);
}
