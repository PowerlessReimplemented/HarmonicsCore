package powerlessri.harmonics.gui.widget.button;

import com.google.common.base.Preconditions;
import powerlessri.harmonics.gui.ITexture;
import powerlessri.harmonics.gui.widget.mixin.ResizableWidgetMixin;

import java.util.function.IntConsumer;

/**
 * A ready-to-use icon button implementation that stores each mouse state texture.
 */
public class SimpleIconButton extends AbstractIconButton implements ResizableWidgetMixin {

    private ITexture textureNormal;
    private ITexture textureHovering;
    private IntConsumer onClick;

    public SimpleIconButton(int x, int y, ITexture textureNormal, ITexture textureHovering) {
        super(x, y, 0, 0);
        this.setTextures(textureNormal, textureHovering);
    }

    @Override
    public ITexture getTextureNormal() {
        return textureNormal;
    }

    @Override
    public ITexture getTextureHovered() {
        return textureHovering;
    }

    public void setTextureNormal(ITexture textureNormal) {
        checkArguments(textureNormal, textureHovering);
        this.textureNormal = textureNormal;
        this.setDimensions(textureNormal.getPortionWidth(), textureNormal.getPortionHeight());
    }

    public void setTextureHovering(ITexture textureHovering) {
        checkArguments(textureNormal, textureHovering);
        this.textureHovering = textureHovering;
        this.setDimensions(textureHovering.getPortionWidth(), textureHovering.getPortionHeight());
    }

    public void setTextures(ITexture textureNormal, ITexture textureHovering) {
        checkArguments(textureNormal, textureHovering);
        this.textureNormal = textureNormal;
        this.textureHovering = textureHovering;
        // Either one is fine, since we checked that they are the same size
        this.setDimensions(textureNormal.getPortionWidth(), textureNormal.getPortionHeight());
    }

    private static void checkArguments(ITexture textureNormal, ITexture textureHovering) {
        Preconditions.checkArgument(textureNormal.getPortionWidth() == textureHovering.getPortionWidth());
        Preconditions.checkArgument(textureNormal.getPortionHeight() == textureHovering.getPortionHeight());
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        onClick.accept(button);
        return true;
    }

    public void onClick(IntConsumer onClick) {
        this.onClick = onClick;
    }
}
