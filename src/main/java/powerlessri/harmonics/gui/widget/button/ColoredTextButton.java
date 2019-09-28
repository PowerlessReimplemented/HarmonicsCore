package powerlessri.harmonics.gui.widget.button;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractWidget;
import powerlessri.harmonics.gui.widget.INamedElement;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;
import powerlessri.harmonics.gui.Render2D;

import java.util.function.IntConsumer;

import static powerlessri.harmonics.gui.RenderingHelper.*;

public class ColoredTextButton extends AbstractWidget implements IButton, INamedElement, LeafWidgetMixin {

    public static final IntConsumer DUMMY = i -> {
    };

    public static ColoredTextButton of(String key) {
        return ofText(I18n.format(key));
    }

    public static ColoredTextButton of(String key, IntConsumer action) {
        return ofText(I18n.format(key), action);
    }

    public static ColoredTextButton of(String key, Object... args) {
        return ofText(I18n.format(key, args));
    }

    public static ColoredTextButton of(String key, IntConsumer action, Object... args) {
        return ofText(I18n.format(key, args), action);
    }

    public static ColoredTextButton ofText(String text) {
        ColoredTextButton button = new ColoredTextButton();
        button.setText(text);
        button.expandToTextWidth();
        return button;
    }

    public static ColoredTextButton ofText(String text, IntConsumer action) {
        ColoredTextButton button = new ColoredTextButton();
        button.setText(text);
        button.expandToTextWidth();
        button.onClick = action;
        return button;
    }

    private static final int NORMAL_BACKGROUND_COLOR = 0x8c8c8c;
    private static final int HOVERED_BACKGROUND_COLOR = 0x8c8c8c;
    private static final int NORMAL_BORDER_COLOR = 0x737373;
    private static final int HOVERED_BORDER_COLOR = 0xc9c9c9;

    public IntConsumer onClick = DUMMY;

    private String text;

    private boolean hovered = false;
    private boolean clicked = false;

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);

        GlStateManager.disableAlphaTest();
        usePlainColorGLStates();
        Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        int x1 = getAbsoluteX();
        int y1 = getAbsoluteY();
        int x2 = getAbsoluteXRight();
        int y2 = getAbsoluteYBottom();
        boolean hovered = isInside(mouseX, mouseY);
        rectVertices(x1, y1, x2, y2, hovered ? getHoveredBorderColor() : getNormalBorderColor());
        rectVertices(x1 + 1, y1 + 1, x2 - 1, y2 - 1, hovered ? getHoveredBackgroundColor() : getNormalBackgroundColor());
        Tessellator.getInstance().draw();
        GlStateManager.enableAlphaTest();

        renderText();

        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    public int getNormalBorderColor() {
        return NORMAL_BORDER_COLOR;
    }

    public int getHoveredBorderColor() {
        return HOVERED_BORDER_COLOR;
    }

    public int getNormalBackgroundColor() {
        return NORMAL_BACKGROUND_COLOR;
    }

    public int getHoveredBackgroundColor() {
        return HOVERED_BACKGROUND_COLOR;
    }

    protected void renderText() {
        Render2D.renderCenteredText(getText(), getAbsoluteY(), getAbsoluteYBottom(), getAbsoluteX(), getAbsoluteXRight(), 0xffffff);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        clicked = true;
        onClick.accept(button);
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
        if (!hovered) {
            clicked = false;
        }
    }

    public void expandToTextWidth() {
        setWidth(Math.max(getFullWidth(), 4 + fontRenderer().getStringWidth(text) + 4));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        setDimensions(fontRenderer().getStringWidth(text), 3 + fontHeight() + 2);
    }

    public void setTextRaw(String text) {
        this.text = text;
    }

    public void translate(String translationKey) {
        setText(I18n.format(translationKey));
    }

    public void translate(String translationKey, Object... args) {
        setText(I18n.format(translationKey, args));
    }

    public void translateRaw(String translationKey) {
        setTextRaw(I18n.format(translationKey));
    }

    public void translateRaw(String translationKey, Object... args) {
        setTextRaw(I18n.format(translationKey, args));
    }

    public boolean hasClickAction() {
        return onClick != DUMMY;
    }

    @Override
    public boolean isHovered() {
        return hovered;
    }

    @Override
    public boolean isClicked() {
        return clicked;
    }

    @Override
    public String getName() {
        return text;
    }

    @Override
    public void provideInformation(ITextReceiver receiver) {
        super.provideInformation(receiver);
        receiver.line("Hovered=" + hovered);
        receiver.line("Clicked=" + clicked);
    }
}
