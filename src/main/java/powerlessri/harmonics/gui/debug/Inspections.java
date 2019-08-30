package powerlessri.harmonics.gui.debug;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import powerlessri.harmonics.gui.IWidget;
import powerlessri.harmonics.gui.IWindow;

import java.awt.*;

import static powerlessri.harmonics.gui.RenderingHelper.*;

public abstract class Inspections implements IRenderEventListener {

    public interface IInfoProvider {

        void provideInformation(ITextReceiver receiver);
    }

    public static final Inspections INSTANCE = new Inspections() {
        @Override
        public void onPreRender(IWidget widget, int mx, int my) {
        }

        @Override
        public void onPreRender(IWindow window, int mx, int my) {
        }

        @Override
        public void onPostRender(IWidget widget, int mx, int my) {
            tryRender(widget, mx, my);
        }

        @Override
        public void onPostRender(IWindow window, int mx, int my) {
            tryRender(window, mx, my);
        }
    };

    private static final ITextReceiver DEFAULT_INFO_RENDERER = new ITextReceiver() {
        private static final int STARTING_X = 1;
        private static final int STARTING_Y = 1;
        private int x;
        private int y;

        {
            reset();
        }

        @Override
        public void reset() {
            x = STARTING_X;
            y = STARTING_Y;
        }

        @Override
        public void string(String text) {
            fontRenderer().drawString(text, x, y, 0xffffff);
            x += fontRenderer().getStringWidth(text);
        }

        @Override
        public void line(String line) {
            fontRenderer().drawString(line, STARTING_X, y, 0xffffff);
            nextLine();
        }

        @Override
        public void nextLine() {
            x = STARTING_X;
            y += fontHeight() + 2;
        }
    };

    /**
     * Master switch for enabling/disabling inspection. Used for SFM setting 'InspectionBoxHighlighting'.
     */
    public static boolean enabled;

    public static final int CONTENTS = 0x662696ff;
    public static final int BORDER = 0x88e38a42;
    public static final int BORDER_A = BORDER >> 24 & 255;
    public static final int BORDER_R = BORDER >> 16 & 255;
    public static final int BORDER_G = BORDER >> 8 & 255;
    public static final int BORDER_B = BORDER & 255;

    // Mark these final to enforce the master switch on subclasses

    @CanIgnoreReturnValue
    public final boolean tryRender(IWidget widget, int mx, int my) {
        if (!enabled) {
            return false;
        }
        if (widget.isInside(mx, my) && shouldRender(widget, mx, my)) {
            renderBox(widget);
            if (Screen.hasControlDown()) {
                renderOverlayInfo(widget);
            }
            return true;
        }
        return false;
    }

    @CanIgnoreReturnValue
    public final boolean tryRender(IWindow window, int mx, int my) {
        if (!enabled) {
            return false;
        }
        if (window.isInside(mx, my) && shouldRender(window, mx, my)) {
            renderBox(window);
            if (Screen.hasControlDown()) {
                renderOverlayInfo(window);
            }
            return true;
        }
        return false;
    }

    public boolean shouldRender(IWidget widget, int mx, int my) {
        return true;
    }

    public boolean shouldRender(IWindow window, int mx, int my) {
        return true;
    }

    public void renderBox(IWidget widget) {
        renderBorderedHighlight(widget);
    }

    public void renderBox(IWindow window) {
        renderBorderedHighlight(window);
    }

    public void renderOverlayInfo(IWidget widget) {
        GlStateManager.pushMatrix();
        GlStateManager.scalef(0.5F, 0.5F, 1F);
        DEFAULT_INFO_RENDERER.reset();
        if (widget instanceof IInfoProvider) {
            ((IInfoProvider) widget).provideInformation(DEFAULT_INFO_RENDERER);
        } else {
            DEFAULT_INFO_RENDERER.line("(Widget does not support overlay info)");
        }
        GlStateManager.popMatrix();
    }

    public void renderOverlayInfo(IWindow window) {
        GlStateManager.pushMatrix();
        GlStateManager.scalef(0.5F, 0.5F, 1.0F);
        DEFAULT_INFO_RENDERER.reset();
        if (window instanceof IInfoProvider) {
            ((IInfoProvider) window).provideInformation(DEFAULT_INFO_RENDERER);
        } else {
            DEFAULT_INFO_RENDERER.line("(Window does not support overlay info)");
        }
        GlStateManager.popMatrix();
    }

    public static void renderHighlight(int x, int y, int width, int height) {
        useBlendingGLStates();
        drawRect(x, y, x + width, y + height, CONTENTS);
        useTextureGLStates();
    }

    public static void renderBorderedHighlight(IWidget widget) {
        renderBorderedHighlight(
                widget.getOuterAbsoluteX(), widget.getOuterAbsoluteY(),
                widget.getAbsoluteX(), widget.getAbsoluteY(),
                widget.getWidth(), widget.getHeight(),
                widget.getFullWidth(), widget.getFullHeight());
    }

    public static void renderBorderedHighlight(IWindow window) {
        renderBorderedHighlight(
                window.getX(), window.getY(),
                window.getContentX(), window.getContentY(),
                window.getContentWidth(), window.getContentHeight(),
                window.getWidth(), window.getHeight());
    }

    public static void renderBorderedHighlight(int x1, int y1, int ix1, int iy1, int width, int height, int fullWidth, int fullHeight) {
        int x2 = x1 + fullWidth;
        int y2 = y1 + fullHeight;
        int ix2 = ix1 + width;
        int iy2 = iy1 + height;

        useBlendingGLStates();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        // Can't just do two rectangles because they are transparent
        rectVertices(ix1, iy1, x2, y1, BORDER_R, BORDER_G, BORDER_B, BORDER_A); // Top border
        rectVertices(ix2, iy1, x2, y2, BORDER_R, BORDER_G, BORDER_B, BORDER_A); // Right border
        rectVertices(x1, y2, ix2, iy2, BORDER_R, BORDER_G, BORDER_B, BORDER_A); // Bottom border
        rectVertices(x1, y1, ix1, iy2, BORDER_R, BORDER_G, BORDER_B, BORDER_A); // Left border
        rectVertices(ix1, iy1, ix2, iy2, CONTENTS);

        tessellator.draw();
        useTextureGLStates();
    }
}
