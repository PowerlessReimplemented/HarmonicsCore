package powerlessri.harmonics.gui.defined;

import net.minecraft.client.Minecraft;
import powerlessri.harmonics.gui.IStyledElement;
import powerlessri.harmonics.gui.UIElement;
import powerlessri.harmonics.gui.style.*;

import javax.annotation.Nullable;

public class TextField extends UIElement implements IStyledElement {

    public static final int BACKGROUND_STYLE = 0;
    public static final int SELECTION_BOX_STYLE = 1;
    public static final int CURSOR_STYLE = 2;
    public static final int NORMAL_TEXT_STYLE = 3;
    public static final int DISABLED_TEXT_STYLE = 4;

    private IBoxStyle backgroundStyle = IBoxStyle.defaultBeveled();
    private IBoxStyle selectionBoxStyle = IBoxStyle.defaultBeveled();
    private IBoxStyle cursorStyle = IBoxStyle.defaultBeveled();
    private ITextStyle normalTextStyle = ITextStyle.defaultVanilla();
    private ITextStyle disabledTextStyle = ITextStyle.defaultVanilla();

    @Nullable
    @Override
    public <S extends IStyle> S getStyle(Class<S> typeClass, int identifier) {
        if (typeClass == IBoxStyle.class) {
            switch (identifier) {
                case BACKGROUND_STYLE:
                default:
                    return backgroundStyle.cast();
                case SELECTION_BOX_STYLE: return selectionBoxStyle.cast();
                case CURSOR_STYLE: return cursorStyle.cast();
            }
        }
        if (typeClass == ITextStyle.class) {
            switch (identifier) {
                case NORMAL_TEXT_STYLE:
                default:
                    return normalTextStyle.cast();
                case DISABLED_TEXT_STYLE: return disabledTextStyle.cast();
            }
        }
        return null;
    }

    // TODO text field
}
