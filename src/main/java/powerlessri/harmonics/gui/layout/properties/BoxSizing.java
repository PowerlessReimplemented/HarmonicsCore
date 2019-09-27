package powerlessri.harmonics.gui.layout.properties;

import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.layout.ISizedBox;

public enum BoxSizing {

    BORDER_BOX(true),
    CONTENT_BOX(true),
    PHANTOM(false),
    ;

    public final boolean flow;

    BoxSizing(boolean flow) {
        this.flow = flow;
    }

    public static boolean shouldIncludeWidget(IWidget widget) {
        if (widget instanceof ISizedBox) {
            return ((ISizedBox) widget).getBoxSizing().flow;
        }
        return false;
    }
}
