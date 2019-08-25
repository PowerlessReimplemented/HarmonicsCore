package powerlessri.harmonics.gui.style;

import com.google.common.base.Preconditions;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import powerlessri.harmonics.gui.RenderingContext;
import powerlessri.harmonics.utils.RenderUtils;

import static org.lwjgl.opengl.GL11.GL_QUADS;

public interface IBoxStyle {

    void render(RenderingContext context, int x, int y, int width, int height);

    ///////////////////////////////////////////////////////////////////////////
    // Implementations
    ///////////////////////////////////////////////////////////////////////////

    static None none() {
        return None.INSTANCE;
    }

    final class None implements IBoxStyle {

        private static final None INSTANCE = new None();

        private None() {
        }

        @Override
        public void render(RenderingContext context, int x, int y, int width, int height) {
        }
    }

    static PlainColor defaultPlainColor() {
        return PlainColor.DEFAULT_INSTANCE;
    }

    static PlainColor plainColor(int color) {
        return new PlainColor().setColor(color);
    }

    final class PlainColor implements IBoxStyle {

        private static final PlainColor DEFAULT_INSTANCE = new PlainColor();

        public int color = 0xffffff;

        public int getColor() {
            return color;
        }

        public PlainColor setColor(int color) {
            Preconditions.checkState(this != DEFAULT_INSTANCE, "Cannot mutate a default instance of a style implementation!");
            this.color = color;
            return this;
        }

        @Override
        public void render(RenderingContext context, int x, int y, int width, int height) {

        }
    }

    static Beveled defaultBeveled() {
        return Beveled.DEFAULT_INSTANCE;
    }

    static Beveled beveled(int backgroundColor, int lightColor, int darkColor) {
        return new Beveled().setColors(backgroundColor, lightColor, darkColor);
    }

    final class Beveled implements IBoxStyle {

        private static final Beveled DEFAULT_INSTANCE = new Beveled();

        public int backgroundColor = 0xc6c6c6;
        public int lightColor = 0xffffff;
        public int darkColor = 0x606060;

        public int getBackgroundColor() {
            return backgroundColor;
        }

        public int getLightColor() {
            return lightColor;
        }

        public int getDarkColor() {
            return darkColor;
        }

        public Beveled setBackgroundColor(int backgroundColor) {
            Preconditions.checkState(this != DEFAULT_INSTANCE, "Cannot mutate a default instance of a style implementation!");
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Beveled setLightColor(int lightColor) {
            Preconditions.checkState(this != DEFAULT_INSTANCE, "Cannot mutate a default instance of a style implementation!");
            this.lightColor = lightColor;
            return this;
        }

        public Beveled setDarkColor(int darkColor) {
            Preconditions.checkState(this != DEFAULT_INSTANCE, "Cannot mutate a default instance of a style implementation!");
            this.darkColor = darkColor;
            return this;
        }

        public Beveled setColors(int backgroundColor, int lightColor, int darkColor) {
            Preconditions.checkState(this != DEFAULT_INSTANCE, "Cannot mutate a default instance of a style implementation!");
            this.backgroundColor = backgroundColor;
            this.lightColor = lightColor;
            this.darkColor = darkColor;
            return this;
        }

        @Override
        public void render(RenderingContext context, int x, int y, int width, int height) {
            Tessellator.getInstance().getBuffer().begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            RenderUtils.rectColor(x, y, x + width, y + height, darkColor);
            RenderUtils.rectColor(x, y, x + width - 1, y + height - 1, lightColor);
            RenderUtils.rectColor(x + 1, y + 1, x + width - 1, y + height - 1, backgroundColor);
            Tessellator.getInstance().draw();
        }
    }
}
