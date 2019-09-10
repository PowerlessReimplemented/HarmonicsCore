package powerlessri.harmonics.gui.widget;

import powerlessri.harmonics.gui.IWidget;

public interface IRadioButton extends IWidget {

    boolean isChecked();

    void setChecked(boolean checked);

    void check(boolean checked);

    int getIndex();

    RadioController getRadioController();
}
