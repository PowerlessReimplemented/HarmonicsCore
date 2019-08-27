package powerlessri.harmonics.gui.window;

import powerlessri.harmonics.gui.IWindow;

public interface IPopupWindow extends IWindow {

    /**
     * Number of ticks this popup should live. Return {@code -1} to remove the lifespan limit.
     */
    int getLifespan();

    DiscardCondition getDiscardCondition();

    default void move(int xOffset, int yOffset) {
        setPosition(getX() + xOffset, getY() + yOffset);
    }
}
