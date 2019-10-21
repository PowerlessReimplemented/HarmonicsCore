package powerlessri.harmonics.gui.screen;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.util.GlAllocationUtils;
import powerlessri.harmonics.HarmonicsCore;

import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.opengl.GL11.GL_COMPILE;

public final class DisplayListCaches {

    private DisplayListCaches() {
    }

    private static final Cache<Rectangle, Integer> VANILLA_BACKGROUND_CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .removalListener(removal -> {
                HarmonicsCore.logger.info("Removed background display list with size {}", removal.getKey());
                GlAllocationUtils.deleteLists((int) removal.getValue(), 1);
            })
            .build();

    public static int createVanillaStyleBackground(Rectangle rectangle) {
        return createVanillaStyleBackground(rectangle, 0F);
    }

    public static int createVanillaStyleBackground(Rectangle rectangle, float z) {
        try {
            return VANILLA_BACKGROUND_CACHE.get(rectangle, () -> {
                HarmonicsCore.logger.info("Created background display list with size {}", rectangle);

                int id = GlAllocationUtils.genLists(1);
                GlStateManager.newList(id, GL_COMPILE);
                {
                    BackgroundRenderers.drawVanillaStyle4x4(rectangle.x, rectangle.y, rectangle.width, rectangle.height, z);
                }
                GlStateManager.endList();
                return id;
            });
        } catch (ExecutionException e) {
            HarmonicsCore.logger.error("Exception when creating OpenGL display list for {} for vanilla-style background", rectangle, e);
            return -1;
        }
    }

    public static int createVanillaStyleBackground(int x, int y, int width, int height) {
        return createVanillaStyleBackground(x, y, width, height, 0F);
    }

    public static int createVanillaStyleBackground(int x, int y, int width, int height, float z) {
        return createVanillaStyleBackground(new Rectangle(x, y, width, height), z);
    }
}
