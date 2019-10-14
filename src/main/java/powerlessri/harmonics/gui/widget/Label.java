package powerlessri.harmonics.gui.widget;

import net.minecraft.client.resources.I18n;
import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.TextRenderer;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.layout.properties.BoxSizing;
import powerlessri.harmonics.gui.layout.properties.Side;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;

import static powerlessri.harmonics.gui.Render2D.fontRenderer;

public class Label extends AbstractWidget implements LeafWidgetMixin {

    private IWidget target;
    private Side side = Side.RIGHT;
    private Alignment alignment = Alignment.CENTER;

    private String text = "";
    private int color;

    public Label(IWidget target) {
        this.target = target;
        IWidget parent = target.getParent();
        if (parent != null) {
            this.attach(parent);
        } else {
            this.attachWindow(target.getWindow());
        }
        setHeight(fontRenderer().FONT_HEIGHT);
        setBorders(1);
        updatePosition();
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

    @SuppressWarnings("UnusedReturnValue")
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
        updatePosition();
        return this;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Label setAlignment(Alignment alignment) {
        this.alignment = alignment;
        updatePosition();
        return this;
    }

    public void updatePosition() {
        alignTo(target, side, alignment);
    }

    @Override
    public BoxSizing getBoxSizing() {
        return BoxSizing.PHANTOM;
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        TextRenderer.vanilla().setTextColor(color);
        TextRenderer.vanilla().renderText(text, getAbsoluteX(), getAbsoluteY(), getZLevel());
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
