package powerlessri.harmonics.gui.layout;

import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.layout.properties.*;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.utils.Utils;

import java.awt.*;
import java.util.Collection;
import java.util.List;

public class FlowLayout {

    private FlowLayout() {
    }

    public static <T extends IWidget> Collection<T> vertical(Collection<T> widgets, int x, int y, int gap) {
        for (T widget : widgets) {
            if (BoxSizing.shouldIncludeWidget(widget)) {
                widget.setLocation(x, y);
                y += widget.getFullHeight() + gap;
            }
        }
        return widgets;
    }

    public static <T extends IWidget> Collection<T> vertical(Dimension bounds, HorizontalAlignment alignment, Collection<T> widgets, int y, int gap) {
        for (T widget : widgets) {
            if (BoxSizing.shouldIncludeWidget(widget)) {
                switch (alignment) {
                    case LEFT: {
                        widget.setLocation(0, y);
                        break;
                    }
                    case CENTER: {
                        int x = Render2D.computeCenterX(0, bounds.width, widget.getFullWidth());
                        widget.setLocation(x, y);
                        break;
                    }
                    case RIGHT: {
                        int x = Render2D.computeRightX(bounds.width, widget.getFullWidth());
                        widget.setLocation(Utils.lowerBound(x, 0), y);
                        break;
                    }
                }
                y += widget.getFullHeight() + gap;
            }
        }
        return widgets;
    }

    public static <T extends IWidget> Collection<T> horizontal(Collection<T> widgets, int x, int y, int gap) {
        for (T widget : widgets) {
            if (BoxSizing.shouldIncludeWidget(widget)) {
                widget.setLocation(x, y);
                x += widget.getFullWidth() + gap;
            }
        }
        return widgets;
    }

    public static <T extends IWidget> Collection<T> horizontal(Dimension bounds, VerticalAlignment alignment, Collection<T> widgets, int x, int gap) {
        for (T widget : widgets) {
            if (BoxSizing.shouldIncludeWidget(widget)) {
                switch (alignment) {
                    case TOP: {
                        widget.setLocation(x, 0);
                        break;
                    }
                    case CENTER: {
                        int y = Render2D.computeCenterY(0, bounds.height, widget.getFullHeight());
                        widget.setLocation(x, y);
                        break;
                    }
                    case BOTTOM: {
                        int y = Render2D.computeBottomY(bounds.height, widget.getFullHeight());
                        widget.setLocation(Utils.lowerBound(x, 0), y);
                        break;
                    }
                }
                x += widget.getFullWidth() + gap;
            }
        }
        return widgets;
    }

    public static <T extends IWidget> List<T> reverseHorizontal(List<T> widgets, int x, int y, int gap) {
        for (T widget : widgets) {
            if (BoxSizing.shouldIncludeWidget(widget)) {
                x -= widget.getFullWidth() + gap;
                widget.setLocation(x, y);
            }
        }
        return widgets;
    }
}
