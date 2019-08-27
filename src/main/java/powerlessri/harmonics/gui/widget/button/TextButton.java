package powerlessri.harmonics.gui.widget.button;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractWidget;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;

import java.util.function.IntConsumer;

import static powerlessri.harmonics.gui.RenderingHelper.*;

public class TextButton extends AbstractWidget implements IButton, LeafWidgetMixin {

    public static final IntConsumer DUMMY = i -> {
    };

    public static TextButton of(String key) {
        return ofText(I18n.format(key));
    }

    public static TextButton of(String key, IntConsumer action) {
        return ofText(I18n.format(key), action);
    }

    public static TextButton of(String key, Object... args) {
        return ofText(I18n.format(key, args));
    }

    public static TextButton of(String key, IntConsumer action, Object... args) {
        return ofText(I18n.format(key, args), action);
    }

    public static TextButton ofText(String text) {
        TextButton button = new TextButton();
        button.setText(text);
        button.expandToTextWidth();
        return button;
    }

    public static TextButton ofText(String text, IntConsumer action) {
        TextButton button = new TextButton();
        button.setText(text);
        button.expandToTextWidth();
        button.onClick = action;
        return button;
    }

    private static final int BACKGROUND_COLOR = 0x8c8c8c;
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
        rectVertices(x1, y1, x2, y2, isInside(mouseX, mouseY) ? HOVERED_BORDER_COLOR : NORMAL_BORDER_COLOR);
        rectVertices(x1 + 1, y1 + 1, x2 - 1, y2 - 1, BACKGROUND_COLOR);
        Tessellator.getInstance().draw();
        GlStateManager.enableAlphaTest();

        drawTextCentered(text, y1, y2, x1, x2, 0xffffff);

        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
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
    }

    public void expandToTextWidth() {
        setWidth(Math.max(getWidth(), 4 + fontRenderer().getStringWidth(text) + 4));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        setDimensions(fontRenderer().getStringWidth(text), 3 + fontHeight() + 2);
    }

    public void translate(String translationKey) {
        setText(I18n.format(translationKey));
    }

    public void translate(String translationKey, Object... args) {
        setText(I18n.format(translationKey, args));
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
}