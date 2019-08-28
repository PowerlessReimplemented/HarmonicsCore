package powerlessri.harmonics.gui.widget.mixin;

import powerlessri.harmonics.gui.IWidget;

import java.awt.*;

public interface ResizableWidgetMixin extends IWidget {

    default void setDimensions(Dimension dimensions) {
        setDimensions(dimensions.width, dimensions.height);
    }

    default void setDimensions(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    default void setWidth(int width) {
        getDimensions().width = width;
    }

    default void setHeight(int height) {
        getDimensions().height = height;
    }

    default int getBorderTop() {
        return getBorders().top;
    }

    default int getBorderRight() {
        return getBorders().right;
    }

    default int getBorderBottom() {
        return getBorders().bottom;
    }

    default int getBorderLeft() {
        return getBorders().left;
    }

    default void setBorderTop(int top) {
        getBorders().top = top;
    }

    default void setBorderRight(int right) {
        getBorders().right = right;
    }

    default void setBorderBottom(int bottom) {
        getBorders().bottom = bottom;
    }

    default void setBorderLeft(int left) {
        getBorders().left = left;
    }

    default void setBorders(int top, int right, int bottom, int left) {
        getBorders().top = top;
        getBorders().right = right;
        getBorders().bottom = bottom;
        getBorders().left = left;
    }

    default void setBorders(int borders) {
        setBorders(borders, borders, borders, borders);
    }
}
