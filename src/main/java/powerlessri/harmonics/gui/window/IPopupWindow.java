package powerlessri.harmonics.gui.window;

import powerlessri.harmonics.gui.IOrdered;
import powerlessri.harmonics.gui.screen.WidgetScreen;

public interface IPopupWindow extends IWindow, IOrdered {

    boolean shouldDiscard();

    default void onAdded(WidgetScreen screen) {
    }
}
