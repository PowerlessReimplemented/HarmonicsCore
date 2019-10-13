package powerlessri.harmonics.gui.widget.button;

import com.mojang.blaze3d.platform.GlStateManager;
import powerlessri.harmonics.gui.ITexture;
import powerlessri.harmonics.gui.Texture;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractWidget;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;

import java.util.function.IntConsumer;

public abstract class AbstractIconButton extends AbstractWidget implements IButton, LeafWidgetMixin {

    private boolean hovered = false;
    private boolean clicked = false;

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        preRenderEvent(mouseX, mouseY);
        GlStateManager.color3f(1F, 1F, 1F);
        ITexture tex = isDisabled() ? getTextureDisabled()
                : isClicked() ? getTextureClicked()
                : isHovered() ? getTextureHovered()
                : getTextureNormal();
        tex.render(getAbsoluteX(), getAbsoluteY(), getAbsoluteXRight(), getAbsoluteYBottom(), getZLevel());
        postRenderEvent(mouseX, mouseY);
    }

    protected void preRenderEvent(int mx, int my) {
        if (isEnabled()) {
            RenderEventDispatcher.onPreRender(this, mx, my);
        }
    }

    protected void postRenderEvent(int mx, int my) {
        if (isEnabled()) {
            RenderEventDispatcher.onPostRender(this, mx, my);
        }
    }

    public abstract ITexture getTextureNormal();

    public abstract ITexture getTextureHovered();

    // Optional
    public ITexture getTextureClicked() {
        return getTextureHovered();
    }

    public ITexture getTextureDisabled() {
        return Texture.NONE;
    }

    @Override
    public boolean hasClickAction() {
        return false;
    }

    @Override
    public IntConsumer getClickAction() {
        return DUMMY;
    }

    @Override
    public void setClickAction(IntConsumer action) {
    }

    @Override
    public boolean isClicked() {
        return clicked;
    }

    @Override
    public boolean isHovered() {
        return hovered;
    }

    public boolean isDisabled() {
        return !isEnabled();
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        clicked = true;
        return true;
    }

    @Override
    public boolean onMouseReleased(double mouseX, double mouseY, int button) {
        clicked = false;
        return true;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        hovered = isInside(mouseX, mouseY);
    }

    @Override
    public void provideInformation(ITextReceiver receiver) {
        super.provideInformation(receiver);
        receiver.line("Hovered=" + hovered);
        receiver.line("Clicked=" + clicked);
        receiver.line("NormalTexture=" + getTextureNormal());
        receiver.line("HoveredTexture=" + getTextureHovered());
    }
}
