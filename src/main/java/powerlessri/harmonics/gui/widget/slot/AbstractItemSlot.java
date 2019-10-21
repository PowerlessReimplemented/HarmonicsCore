package powerlessri.harmonics.gui.widget.slot;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import powerlessri.harmonics.gui.*;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractWidget;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;

import static powerlessri.harmonics.gui.Render2D.*;

public abstract class AbstractItemSlot extends AbstractWidget implements LeafWidgetMixin {

    public static final ITexture BASE = Texture.complete(Render2D.ITEM_SLOT, 18, 18);

    public AbstractItemSlot() {
        this.setDimensions(18, 18);
    }

    @Override
    public void render(int mouseX, int mouseY, float tickDelta) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        renderBase();
        if (isInside(mouseX, mouseY)) {
            renderHoveredOverlay();
        }
        renderStack();
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    public void renderStack() {
        ItemStack stack = getRenderedStack();
        ItemRenderer ir = minecraft().getItemRenderer();
        TextRenderer fr = minecraft().textRenderer;
        int x = getAbsoluteX() + 2;
        int y = getAbsoluteY() + 2;
        ir.renderGuiItem(stack, x, y);
        ir.renderGuiItemOverlay(fr, stack, x, y, null);
    }

    public void renderBase() {
        BASE.render(getAbsoluteX(), getAbsoluteY(), getAbsoluteXRight(), getAbsoluteYBottom());
    }

    public void renderHoveredOverlay() {
        useBlendingGLStates();
        beginColoredQuad();
        coloredRect(getAbsoluteX(), getAbsoluteY(), getAbsoluteXRight(), getAbsoluteYBottom(), 0xaac4c4c4);
        draw();
        useTextureGLStates();
    }

    public abstract ItemStack getRenderedStack();
}
