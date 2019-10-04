package powerlessri.harmonics.gui;

import net.minecraft.util.ResourceLocation;

public interface ITexture {

    ResourceLocation getResourceLocation();

    int getTextureWidth();

    int getTextureHeight();

    int getPortionX();

    int getPortionY();

    int getPortionWidth();

    int getPortionHeight();

    void render(int x1, int y1, int x2, int y2, float z);

    default void render(int x1, int y1, int x2, int y2) {
        render(x1, y1, x2, y2, 0F);
    }

    default void render(int x, int y, float z) {
        render(x, y, x + getPortionWidth(), y + getPortionHeight(), z);
    }

    default void render(int x, int y) {
        render(x, y, 0F);
    }

    void vertices(int x1, int y1, int x2, int y2, float z);

    default void vertices(int x1, int y1, int x2, int y2) {
        vertices(x1, y1, x2, y2, 0F);
    }

    default void vertices(int x, int y, float z) {
        vertices(x, y, x + getPortionWidth(), y + getPortionHeight(), z);
    }

    default void vertices(int x, int y) {
        vertices(x, y, 0F);
    }

    boolean isComplete();

    ITexture offset(int x, int y);

    ITexture up(int y);

    ITexture down(int y);

    ITexture left(int x);

    ITexture right(int x);

    default ITexture moveUp(int times) {
        return up(getPortionHeight() * times);
    }

    default ITexture moveDown(int times) {
        return down(getPortionHeight() * times);
    }

    default ITexture moveLeft(int times) {
        return left(getPortionWidth() * times);
    }

    default ITexture moveRight(int times) {
        return right(getPortionWidth() * times);
    }
}
