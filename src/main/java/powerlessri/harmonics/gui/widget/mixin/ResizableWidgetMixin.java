package powerlessri.harmonics.gui.widget.mixin;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import powerlessri.harmonics.gui.IWidget;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
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
}
