package powerlessri.harmonics.gui;

import net.minecraft.util.ResourceLocation;

public abstract class Texture implements ITexture {

    Texture() {
    }

    public static ITexture portion(ResourceLocation texture, int texWidth, int texHeight, int portionX, int portionY, int portionWidth, int portionHeight) {
        if (texWidth == portionWidth && texHeight == portionHeight && portionX == 0 && portionY == 0) {
            return complete(texture, texWidth, texHeight);
        }
        return new PartialTexture(texture, texWidth, texHeight, portionX, portionY, portionWidth, portionHeight);
    }

    public static ITexture portion256x256(ResourceLocation texture, int portionX, int portionY, int portionWidth, int portionHeight) {
        return new PartialTexture(texture, 256, 256, portionX, portionY, portionWidth, portionHeight);
    }

    public static ITexture complete(ResourceLocation texture, int width, int height) {
        return new CompleteTexture(texture, width, height);
    }

    public static ITexture complete256x256(ResourceLocation texture) {
        return new CompleteTexture(texture, 256, 256);
    }
}
