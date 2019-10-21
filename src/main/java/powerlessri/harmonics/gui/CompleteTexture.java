package powerlessri.harmonics.gui;

import com.google.common.base.MoreObjects;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.Identifier;

import java.util.Objects;

import static powerlessri.harmonics.gui.Render2D.*;

class CompleteTexture implements ITexture {

    private Identifier texture;
    private int width;
    private int height;

    public CompleteTexture(Identifier texture, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    @Override
    public Identifier getIdentifier() {
        return texture;
    }

    @Override
    public int getTextureWidth() {
        return width;
    }

    @Override
    public int getTextureHeight() {
        return height;
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
        return width;
    }

    @Override
    public int getPortionHeight() {
        return height;
    }

    @Override
    public void render(int x1, int y1, int x2, int y2, float z) {
        bindTexture(texture);
        beginTexturedQuad();
        vertices(x1, y1, x2, y2, z);
        draw();
    }

    @Override
    public void vertices(int x1, int y1, int x2, int y2, float z) {
        BufferBuilder buffer = Tessellator.getInstance().getBufferBuilder();
        buffer.vertex(x1, y1, z).texture(0D, 0D).end();
        buffer.vertex(x1, y2, z).texture(0D, 1D).end();
        buffer.vertex(x2, y2, z).texture(1D, 1D).end();
        buffer.vertex(x2, y1, z).texture(1D, 0D).end();
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public CompleteTexture offset(int x, int y) {
        return this;
    }

    @Override
    public CompleteTexture up(int y) {
        return this;
    }

    @Override
    public CompleteTexture down(int y) {
        return this;
    }

    @Override
    public CompleteTexture left(int x) {
        return this;
    }

    @Override
    public CompleteTexture right(int x) {
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("texture", texture)
                .add("width", width)
                .add("height", height)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompleteTexture that = (CompleteTexture) o;
        return width == that.width &&
                height == that.height &&
                texture.equals(that.texture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texture, width, height);
    }
}
