package powerlessri.harmonics.gui.widget;

public interface IRadioInput extends IWidget {

    boolean isChecked();

    void setChecked(boolean checked);

    void check(boolean checked);

    int getIndex();

    RadioController getRadioController();
}
