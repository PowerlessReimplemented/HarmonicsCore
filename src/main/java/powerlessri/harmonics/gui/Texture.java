package powerlessri.harmonics.gui;

import net.minecraft.util.Identifier;

public abstract class Texture implements ITexture {

    Texture() {
    }

    public static final ITexture NONE = new ITexture() {
        @Override
        public Identifier getIdentifier() {
            return Render2D.INVALID_TEXTURE;
        }

        @Override
        public int getTextureWidth() {
            return 0;
        }

        @Override
        public int getTextureHeight() {
            return 0;
        }

        @Override
        public int getPortionX() {
            return 0;
        }

        @Override
        public int getPortionY() {
            return 0;
        }

        @Override
        public int getPortionWidth() {
            return 0;
        }

        @Override
        public int getPortionHeight() {
            return 0;
        }

        @Override
        public void render(int x1, int y1, int x2, int y2, float z) {
        }

        @Override
        public void vertices(int x1, int y1, int x2, int y2, float z) {
        }

        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public ITexture offset(int x, int y) {
            return this;
        }

        @Override
        public ITexture up(int y) {
            return this;
        }

        @Override
        public ITexture down(int y) {
            return this;
        }

        @Override
        public ITexture left(int x) {
            return this;
        }

        @Override
        public ITexture right(int x) {
            return this;
        }
    };

    public static ITexture portion(Identifier texture, int texWidth, int texHeight, int portionX, int portionY, int portionWidth, int portionHeight) {
        if (texWidth == portionWidth && texHeight == portionHeight && portionX == 0 && portionY == 0) {
            return complete(texture, texWidth, texHeight);
        }
        return new PartialTexture(texture, texWidth, texHeight, portionX, portionY, portionWidth, portionHeight);
    }

    public static ITexture portion256x256(Identifier texture, int portionX, int portionY, int portionWidth, int portionHeight) {
        return new PartialTexture(texture, 256, 256, portionX, portionY, portionWidth, portionHeight);
    }

    public static ITexture complete(Identifier texture, int width, int height) {
        return new CompleteTexture(texture, width, height);
    }

    public static ITexture complete256x256(Identifier texture) {
        return new CompleteTexture(texture, 256, 256);
    }
}
