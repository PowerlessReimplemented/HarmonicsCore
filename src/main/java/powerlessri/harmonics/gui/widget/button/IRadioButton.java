package powerlessri.harmonics.gui.widget.button;

import powerlessri.harmonics.gui.IWidget;
import powerlessri.harmonics.gui.widget.button.RadioController;

public interface IRadioButton extends IButton, IWidget {

    boolean isChecked();

    void setChecked(boolean checked);

    void check(boolean checked);

    int getIndex();

    RadioController getRadioController();
}
