package powerlessri.harmonics.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import powerlessri.harmonics.HarmonicsCore;

// TODO complete switch to this render utils
public final class Render2D {

    public static final ResourceLocation COMPONENTS = new ResourceLocation(HarmonicsCore.MODID, "textures/gui/default_components.png");
    public static final ResourceLocation DELETE = new ResourceLocation(HarmonicsCore.MODID, "textures/gui/icons/delete.png");
    public static final ResourceLocation CUT = new ResourceLocation(HarmonicsCore.MODID, "textures/gui/icons/cut.png");
    public static final ResourceLocation COPY = new ResourceLocation(HarmonicsCore.MODID, "textures/gui/icons/copy.png");
    public static final ResourceLocation PASTE = new ResourceLocation(HarmonicsCore.MODID, "textures/gui/icons/paste.png");
    public static final ResourceLocation BACK = new ResourceLocation(HarmonicsCore.MODID, "textures/gui/icons/back.png");
    public static final ResourceLocation CLOSE = new ResourceLocation(HarmonicsCore.MODID, "textures/gui/icons/close.png");
    public static final ResourceLocation ITEM_SLOT = new ResourceLocation(HarmonicsCore.MODID, "textures/gui/icons/item_slot.png");

    private Render2D() {
    }

    public static BufferBuilder getBuffer() {
        return Tessellator.getInstance().getBuffer();
    }

    public static void coloredRect(int x1, int y1, int x2, int y2, float z, int color) {
        int alpha = (color >> 24) & 255;
        int red = (color >> 16) & 255;
        int green = (color >> 8) & 255;
        int blue = color & 255;
        BufferBuilder renderer = getBuffer();
        renderer.pos(x1, y1, z).color(red, green, blue, alpha).endVertex();
        renderer.pos(x1, y2, z).color(red, green, blue, alpha).endVertex();
        renderer.pos(x2, y2, z).color(red, green, blue, alpha).endVertex();
        renderer.pos(x2, y1, z).color(red, green, blue, alpha).endVertex();
    }

    public static void coloredRect(int x1, int y1, int x2, int y2, int color) {
        coloredRect(x1, y1, x2, y2, 0F, color);
    }

    public static int computeCenterY(int top, int bottom, int height) {
        return top + (bottom - top) / 2 - height / 2;
    }

    public static int computeBottomY(int bottom, int height) {
        return bottom - height;
    }

    public static int computeCenterX(int left, int right, int width) {
        return left + (right - left) / 2 - width / 2;
    }

    public static int computeRightX(int right, int width) {
        return right - width;
    }

    public static int getXForHorizontallyCenteredText(String text, int left, int right) {
        int textWidth = RenderingHelper.fontRenderer().getStringWidth(text);
        return computeCenterX(left, right, textWidth);
    }

    public static int getYForVerticallyCenteredText(int top, int bottom) {
        return computeCenterY(top, bottom, RenderingHelper.fontHeight());
    }

    public static void renderVerticallyCenteredText(String text, int leftX, int top, int bottom, int color) {
        int y = getYForVerticallyCenteredText(top, bottom);
        GlStateManager.enableTexture();
        RenderingHelper.fontRenderer().drawString(text, leftX, y, color);
    }

    public static void renderHorizontallyCenteredText(String text, int left, int right, int topY, int color) {
        int x = getXForHorizontallyCenteredText(text, left, right);
        GlStateManager.enableTexture();
        RenderingHelper.fontRenderer().drawString(text, x, topY, color);
    }

    public static void renderCenteredText(String text, int top, int bottom, int left, int right, int color) {
        int x = getXForHorizontallyCenteredText(text, left, right);
        int y = getYForVerticallyCenteredText(top, bottom);
        GlStateManager.enableTexture();
        RenderingHelper.fontRenderer().drawString(text, x, y, color);
    }
}
