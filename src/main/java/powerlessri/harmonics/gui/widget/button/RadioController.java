package powerlessri.harmonics.gui.widget.button;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class RadioController {

    private final List<IRadioButton> radioButtons = new ArrayList<>();
    private int checkedIndex = -1;

    public List<IRadioButton> getRadioButtons() {
        return radioButtons;
    }

    public int add(RadioButton button) {
        radioButtons.add(button);
        return radioButtons.size() - 1;
    }

    public void checkRadioButton(int index) {
        Preconditions.checkArgument(index < radioButtons.size());
        IRadioButton checkedButton = getCurrentCheckedButton();
        if (checkedButton != null) {
            checkedButton.setChecked(false);
        }
        checkedIndex = index;
    }

    @Nullable
    public IRadioButton getCurrentCheckedButton() {
        if (checkedIndex == -1) {
            return null;
        }
        return radioButtons.get(checkedIndex);
    }
}
