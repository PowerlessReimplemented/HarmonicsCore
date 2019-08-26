package powerlessri.harmonics.gui.style;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.resources.I18n;
import powerlessri.harmonics.gui.RenderingContext;

import static powerlessri.harmonics.gui.UIElement.*;
import static powerlessri.harmonics.utils.RenderUtils.getCenterX;
import static powerlessri.harmonics.utils.RenderUtils.getCenterY;

public interface ITextStyle extends IStyle {

    void render(RenderingContext context, String text, int x, int y);

    default void renderTranslation(RenderingContext context, String translationKey, int x, int y) {
        render(context, I18n.format(translationKey), x, y);
    }

    default void renderTranslation(RenderingContext context, String translationKey, int x, int y, Object... args) {
        render(context, I18n.format(translationKey, args), x, y);
    }

    default void renderRight(RenderingContext context, String text, int xRight, int y) {
        int width = textWidth(text);
        render(context, text, xRight - width, y);
    }

    default void renderBottom(RenderingContext context, String text, int x, int yBottom) {
        render(context, text, x, yBottom - fontHeight());
    }

    default void renderHorizontalCenter(RenderingContext context, String text, int leftX, int rightX, int y) {
        render(context, text, getCenterX(leftX, rightX, textWidth(text)), y);
    }

    default void renderVerticalCenter(RenderingContext context, String text, int x, int topY, int bottomY) {
        render(context, text, x, getCenterY(topY, bottomY, fontHeight()));
    }

    default void renderCenter(RenderingContext context, String text, int leftX, int topY, int rightX, int bottomY) {
        render(context, text, getCenterX(leftX, rightX, textWidth(text)), getCenterY(topY, bottomY, fontHeight()));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Implementations
    ///////////////////////////////////////////////////////////////////////////

    static Vanilla defaultVanilla() {
        return Vanilla.DEFAULT_INSTANCE;
    }

    static Vanilla vanilla(int color) {
        return new Vanilla().setColor(color);
    }

    final class Vanilla implements ITextStyle {

        private static final Vanilla DEFAULT_INSTANCE = new Vanilla();

        public boolean hasShadow = false;
        public int color = 0x000000;

        public boolean isHasShadow() {
            return hasShadow;
        }

        public int getColor() {
            return color;
        }

        public Vanilla setColor(int color) {
            Preconditions.checkState(this != DEFAULT_INSTANCE, "Cannot mutate a default instance of a style implementation!");
            this.color = color;
            return this;
        }

        public Vanilla setHasShadow(boolean hasShadow) {
            Preconditions.checkState(this != DEFAULT_INSTANCE, "Cannot mutate a default instance of a style implementation!");
            this.hasShadow = hasShadow;
            return this;
        }

        @Override
        public void render(RenderingContext context, String text, int x, int y) {
            GlStateManager.enableTexture();
            if (hasShadow) {
                fontRenderer().drawStringWithShadow(text, x, y, color);
            } else {
                fontRenderer().drawString(text, x, y, color);
            }
        }
    }
}
