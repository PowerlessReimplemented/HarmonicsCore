package powerlessri.harmonics.gui.widget.button;

import powerlessri.harmonics.gui.widget.IWidget;

import java.util.function.IntConsumer;

public interface IButton extends IWidget {

    IntConsumer DUMMY = i -> {};

    boolean isHovered();

    boolean isClicked();

    boolean hasClickAction();

    IntConsumer getClickAction();

    void setClickAction(IntConsumer action);
}
