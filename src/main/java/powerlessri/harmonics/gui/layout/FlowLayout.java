package powerlessri.harmonics.gui.layout;

import powerlessri.harmonics.gui.IWidget;
import powerlessri.harmonics.gui.layout.properties.BoxSizing;
import powerlessri.harmonics.gui.layout.properties.HorizontalAlignment;
import powerlessri.harmonics.utils.RenderUtils;
import powerlessri.harmonics.utils.Utils;

import java.awt.*;
import java.util.List;

public class FlowLayout {

    private FlowLayout() {
    }

    public static <T extends IWidget> List<T> reflow(List<T> widgets) {
        return reflow(widgets, 0);
    }

    public static <T extends IWidget> List<T> reflow(List<T> widgets, int x) {
        int y = 0;
        for (T widget : widgets) {
            if (BoxSizing.shouldIncludeWidget(widget)) {
                widget.setLocation(x, y);
                y += widget.getFullHeight();
            }
        }
        return widgets;
    }

    public static <T extends IWidget> List<T> reflow(Dimension bounds, HorizontalAlignment alignment, List<T> widgets) {
        int y = 0;
        for (T widget : widgets) {
            if (BoxSizing.shouldIncludeWidget(widget)) {
                switch (alignment) {
                    case LEFT: {
                        widget.setLocation(0, y);
                        break;
                    }
                    case CENTER: {
                        int x = RenderUtils.computeCenterX(0, bounds.width, widget.getFullWidth());
                        widget.setLocation(x, y);
                        break;
                    }
                    case RIGHT: {
                        int x = RenderUtils.computeRightX(bounds.width, widget.getFullWidth());
                        widget.setLocation(Utils.lowerBound(x, 0), y);
                        break;
                    }
                }
                y += widget.getFullHeight();
            }
        }
        return widgets;
    }
}
