package powerlessri.harmonics.gui.widget.button;

import com.mojang.blaze3d.platform.GlStateManager;
import powerlessri.harmonics.gui.*;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractWidget;
import powerlessri.harmonics.gui.widget.Label;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;

public class RadioButton extends AbstractWidget implements IButton, IRadioButton, LeafWidgetMixin {

    private static final ITexture UNCHECKED = Texture.portion(Render2D.COMPONENTS, 256, 256, 0, 12, 8, 8);
    private static final ITexture CHECKED = UNCHECKED.moveRight(1);
    private static final ITexture HOVERED_UNCHECKED = UNCHECKED.moveDown(1);
    private static final ITexture HOVERED_CHECKED = CHECKED.moveDown(1);

    private final RadioController controller;
    private final int index;

    private boolean hovered;
    private boolean checked;

    public RadioButton(RadioController controller) {
        this.controller = controller;
        this.index = controller.add(this);
        this.setDimensions(8, 8);
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        GlStateManager.color3f(1F, 1F, 1F);
        ITexture texture = hovered
                ? (checked ? HOVERED_CHECKED : HOVERED_UNCHECKED)
                : (checked ? CHECKED : UNCHECKED);
        texture.render(getAbsoluteX(), getAbsoluteY(), getAbsoluteXRight(), getAbsoluteYBottom());
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        if (!checked) {
            check(true);
        }
        return true;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        hovered = isInside(mouseX, mouseY);
    }

    protected void onStateUpdate(boolean oldValue) {
    }

    protected void onCheck() {
    }

    protected void onUncheck() {
    }

    @Override
    public boolean isHovered() {
        return hovered;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void setChecked(boolean checked) {
        boolean oldValue = this.checked;
        this.checked = checked;
        onStateUpdate(oldValue);
        if (checked) {
            onCheck();
        } else {
            onUncheck();
        }
    }

    @Override
    public void check(boolean checked) {
        setChecked(checked);
        if (checked) {
            controller.checkRadioButton(index);
        }
    }

    public Label label(String translationKey) {
        return new Label(this).translate(translationKey);
    }

    public Label textLabel(String text) {
        return new Label(this).text(text);
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public RadioController getRadioController() {
        return controller;
    }

    /**
     * Same as {@link #isChecked()} because it a radio button doesn't really need a clicked style. Prefer to use the mentioned method for
     * its semantic advantages.
     */
    @Override
    public boolean isClicked() {
        return checked;
    }

    @Override
    public void provideInformation(ITextReceiver receiver) {
        super.provideInformation(receiver);
        receiver.line("Hovered=" + hovered);
        receiver.line("Checked=" + checked);
        receiver.line("Index=" + index);
    }
}
