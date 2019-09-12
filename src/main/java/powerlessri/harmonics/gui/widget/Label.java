package powerlessri.harmonics.gui.widget;

import net.minecraft.client.resources.I18n;
import powerlessri.harmonics.gui.IWidget;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.layout.properties.HorizontalAlignment;
import powerlessri.harmonics.gui.layout.properties.Side;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;

public class Label extends AbstractWidget implements LeafWidgetMixin {

    private IWidget target;
    private Side side = Side.RIGHT;
    private HorizontalAlignment alignment = HorizontalAlignment.CENTER;

    private String text = "";
    private int color;

    public Label(IWidget target) {
        this.target = target;
    }

    public String getText() {
        return text;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Label text(String text) {
        this.text = text;
        int width = fontRenderer().getStringWidth(text);
        int height = fontRenderer().FONT_HEIGHT;
        setDimensions(width, height);
        updatePosition();
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Label translate(String translationKey) {
        return text(I18n.format(translationKey));
    }

    @SuppressWarnings("UnusedReturnValue")
    public Label translate(String translationKey, Object... args) {
        return text(I18n.format(translationKey, args));
    }

    public int getColor() {
        return color;
    }

    public Label setColor(int color) {
        this.color = color;
        return this;
    }

    public Side getSide() {
        return side;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Label setSide(Side side) {
        this.side = side;
        return this;
    }

    public HorizontalAlignment getAlignment() {
        return alignment;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Label setAlignment(HorizontalAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    private void updatePosition() {
        alignTo(target, side, alignment);
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        fontRenderer().drawString(text, getAbsoluteX(), getAbsoluteY(), color);
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    @Override
    public void provideInformation(ITextReceiver receiver) {
        super.provideInformation(receiver);
        receiver.line("Text=" + text);
        receiver.line("Color=" + color);
        receiver.line("Side=" + side);
        receiver.line("Alignment=" + alignment);
    }
}
