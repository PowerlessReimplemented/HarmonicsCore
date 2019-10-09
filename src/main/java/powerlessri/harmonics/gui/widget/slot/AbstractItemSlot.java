package powerlessri.harmonics.gui.widget.slot;

import com.google.common.base.MoreObjects;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
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
    public void render(int mouseX, int mouseY, float particleTicks) {
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
        FontRenderer fr = MoreObjects.firstNonNull(stack.getItem().getFontRenderer(stack), minecraft().fontRenderer);
        int x = getAbsoluteX() + 2;
        int y = getAbsoluteY() + 2;
        ir.renderItemAndEffectIntoGUI(stack, x, y);
        ir.renderItemOverlayIntoGUI(fr, stack, x, y, null);
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
