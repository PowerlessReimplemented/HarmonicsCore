package powerlessri.harmonics.gui.widget;

import powerlessri.harmonics.gui.TextureWrapper;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;

public class Icon extends AbstractWidget implements INamedElement, LeafWidgetMixin {

    private TextureWrapper texture;

    public Icon(int x, int y, TextureWrapper texture) {
        super(x, y, texture.getPortionWidth(), texture.getPortionHeight());
        this.texture = texture;
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        if (isEnabled()) {
            texture.draw(getAbsoluteX(), getAbsoluteY());
        }
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    public TextureWrapper getTexture() {
        return texture;
    }

    public void setTexture(TextureWrapper texture) {
        this.texture = texture;
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
