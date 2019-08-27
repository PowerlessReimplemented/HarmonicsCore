package powerlessri.harmonics.gui.layout.properties;

import powerlessri.harmonics.gui.IWidget;
import powerlessri.harmonics.gui.layout.ILayoutDataProvider;

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
        if (widget instanceof ILayoutDataProvider) {
            return ((ILayoutDataProvider) widget).getBoxSizing().flow;
        }
        return false;
    }
}
