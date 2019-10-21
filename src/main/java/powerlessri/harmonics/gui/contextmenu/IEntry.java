package powerlessri.harmonics.gui.contextmenu;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.mixin.ResizableWidgetMixin;

import java.awt.*;

public interface IEntry extends IWidget, ResizableWidgetMixin {

    /**
     * This icon must have a size of 16*16, and action menus will assume so to function. Failure to do so might create undefined behaviors.
     */
    @Nullable
    Identifier getIcon();

    default String getText() {
        return I18n.translate(getTranslationKey());
    }

    String getTranslationKey();

    @Override
    Dimension getDimensions();

    void attach(ContextMenu contextMenu);
}
