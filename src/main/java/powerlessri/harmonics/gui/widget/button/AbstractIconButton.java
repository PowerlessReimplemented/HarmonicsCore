package powerlessri.harmonics.gui.widget.button;

import powerlessri.harmonics.gui.TextureWrapper;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractWidget;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;

import java.awt.*;

public abstract class AbstractIconButton extends AbstractWidget implements IButton, LeafWidgetMixin {

    private boolean hovered = false;
    private boolean clicked = false;

    public AbstractIconButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        preRenderEvent(mouseX, mouseY);
        TextureWrapper tex = isDisabled() ? getTextureDisabled()
                : isClicked() ? getTextureClicked()
                : isHovered() ? getTextureHovered()
                : getTextureNormal();
        tex.draw(getAbsoluteX(), getAbsoluteY());
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

    public abstract TextureWrapper getTextureNormal();

    public abstract TextureWrapper getTextureHovered();

    // Optional
    public TextureWrapper getTextureClicked() {
        return getTextureHovered();
    }

    public TextureWrapper getTextureDisabled() {
        return TextureWrapper.NONE;
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
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        clicked = true;
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
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
        receiver.line("NormalTexture=" + getTextureNormal());
        receiver.line("HoveredTexture=" + getTextureHovered());
    }
}
