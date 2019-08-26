package powerlessri.harmonics.gui.defined;

import powerlessri.harmonics.gui.IStyledElement;
import powerlessri.harmonics.gui.UIElement;
import powerlessri.harmonics.gui.style.*;

import javax.annotation.Nullable;

public class Label extends UIElement implements IStyledElement {

    private IBoxStyle boxStyle = IBoxStyle.defaultBeveled();
    private ITextStyle textStyle = ITextStyle.defaultVanilla();

    @Nullable
    @Override
    public <S extends IStyle> S getStyle(Class<S> typeClass, int identifier) {
        if (typeClass == IBoxStyle.class) {
            return boxStyle.cast();
        }
        if (typeClass == ITextStyle.class) {
            return textStyle.cast();
        }
        return null;
    }

    // TODO label
}
