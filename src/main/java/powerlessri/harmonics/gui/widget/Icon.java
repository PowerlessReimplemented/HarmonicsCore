package powerlessri.harmonics.gui.widget;

import powerlessri.harmonics.gui.ITexture;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;

public class Icon extends AbstractWidget implements INamedElement, LeafWidgetMixin {

    private ITexture texture;

    public Icon(int x, int y, ITexture texture) {
        super(x, y, texture.getPortionWidth(), texture.getPortionHeight());
        this.texture = texture;
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        if (isEnabled()) {
            texture.render(getAbsoluteX(), getAbsoluteY());
        }
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    public ITexture getTexture() {
        return texture;
    }

    public void setTexture(ITexture texture) {
        this.texture = texture;
        this.setDimensions(texture.getPortionWidth(), texture.getPortionHeight());
    }

    @Override
    public void provideInformation(ITextReceiver receiver) {
        super.provideInformation(receiver);
        receiver.line("Texture=" + texture);
    }

    @Override
    public String getName() {
        return texture.toString();
    }
}
