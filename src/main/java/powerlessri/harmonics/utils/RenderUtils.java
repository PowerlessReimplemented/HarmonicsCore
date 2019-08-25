package powerlessri.harmonics.utils;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public final class RenderUtils {

    private RenderUtils() {
    }

    public static BufferBuilder getBuffer() {
        return Tessellator.getInstance().getBuffer();
    }

    public static void rectColor(int x1, int y1, int x2, int y2, float z, int color) {
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

    public static void rectColor(int x1, int y1, int x2, int y2, int color) {
        rectColor(x1, y1, x2, y2, 0F, color);
    }
}
