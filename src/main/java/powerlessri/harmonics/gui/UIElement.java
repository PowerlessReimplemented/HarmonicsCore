package powerlessri.harmonics.gui;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;

import java.awt.*;

/**
 * Base class for all UI elements.
 *
 * <pre>
 *     +------------------------------+
 *     | outer element                |
 *     |                              |
 *     |   [1]''''''''''''''''''''    |
 *     |   | element, border     |    |
 *     |   |                     |    |
 *     |   | [2]--------------+  |    |
 *     |   | | content        |  |    |
 *     |   | |               [6][8]   |
 *     |   | |                |  |    |
 *     |   | +------[5]-----[3]  |    |
 *     |   |                     |    |
 *     |   ^^^^^^^^^[7]^^^^^^^^[4]    |
 *     +------------------------------+
 * </pre>
 * <p>
 * <h3>Relative coordinates:</h3>
 * <ul>
 *     <li>[1]: (x,y), {@code position} object
 *     <li>[2]: inner (x,y)
 *     <li>[3]: inner right/bottom (x,y)
 *     <li>[4]: right/bottom (x,y)
 * </ul>
 * <h3>Absolute coordinates:</h3>
 * <ul>
 *     <li>[1]: outer (x,y)
 *     <li>[2]: (x,y)
 *     <li>[3]: right/bottom (x,y)
 *     <li>[4]: outer right/bottom (x,y)
 *     </ul>
 * <h3>Dimensions:</h3>
 * <ul>
 *     <li>[5]: width
 *     <li>[6]: height
 *     <li>[7]: full width
 *     <li>[8]: full height
 * </ul>
 *
 * @param <E> The inherited type itself.
 */
public abstract class UIElement<E extends UIElement<E>> {

    private static final UIElement DUMMY = new UIElement() {};

    // Alias to Minecraft properties

    public static Minecraft minecraft() {
        return Minecraft.getInstance();
    }

    public static FontRenderer fontRenderer() {
        return Minecraft.getInstance().fontRenderer;
    }

    public static int fontHeight() {
        return fontRenderer().FONT_HEIGHT;
    }

    public static TextureManager textureManager() {
        return Minecraft.getInstance().getTextureManager();
    }

    public static ClientWorld world() {
        return Minecraft.getInstance().world;
    }

    public static ClientPlayerEntity player() {
        return Minecraft.getInstance().player;
    }

    private Point position = new Point();
    private Dimension dimensions = new Dimension();
    private Insets borders = new Insets(0, 0, 0, 0);

    private int absX;
    private int absY;

    private UIElement parent;

    private boolean enabled;

    public UIElement() {
    }

    public UIElement(int x, int y, int width, int height) {
        this.position.x = x;
        this.position.y = y;
        this.dimensions.width = width;
        this.dimensions.height = height;
    }

    public UIElement(int width, int height) {
        this(0, 0, width, height);
    }

    @CanIgnoreReturnValue
    @SuppressWarnings("unchecked")
    public final E attach(UIElement parent) {
        boolean firstLoad = this.parent == null;
        if (!firstLoad) {
            detach();
        }

        this.parent = parent;
        if (firstLoad) {
            onAttach();
            populateChildren();
        } else {
            onReattach();
        }

        updateAbsPos();
        return (E) this;
    }

    protected void onAttach() {
    }

    protected void onReattach() {
    }

    /**
     * Create default child elements that may exist for some implementation. This is guaranteed to be called only once and called in a ready
     * state (attached to tree).
     */
    protected void populateChildren() {
    }

    @CanIgnoreReturnValue
    @SuppressWarnings("unchecked")
    public final E detach() {
        this.parent = DUMMY;
        onDetach();
        return (E) this;
    }

    protected void onDetach() {
    }

    public boolean isValid() {
        return parent != null && parent != DUMMY;
    }

    public Point getPosition() {
        return position;
    }

    public Dimension getDimensions() {
        return dimensions;
    }

    public Insets getBorders() {
        return borders;
    }

    public int getInnerRelativeX() {
        return position.x + borders.left;
    }

    public int getInnerRelativeY() {
        return position.y + borders.top;
    }

    public int getInnerRelativeXRight() {
        return getInnerRelativeX() + getWidth();
    }

    public int getInnerRelativeYBottom() {
        return getInnerRelativeY() + getHeight();
    }

    public int getRelativeX() {
        return position.x;
    }

    public int getRelativeY() {
        return position.y;
    }

    public int getRelativeXRight() {
        return position.x + getFullWidth();
    }

    public int getRelativeYBottom() {
        return position.y + getFullHeight();
    }

    public void setRelativeX(int x) {
        position.x = x;
        updateAbsPos();
    }

    public void setRelativeY(int y) {
        position.y = y;
        updateAbsPos();
    }

    private void updateAbsPos() {
        absX = position.x + parent.getAbsoluteX();
        absY = position.y + parent.getAbsoluteY();
    }

    public int getAbsoluteX() {
        return absX;
    }

    public int getAbsoluteY() {
        return absY;
    }

    public int getAbsoluteXRight() {
        return absX + getWidth();
    }

    public int getAbsoluteYBottom() {
        return absY + getHeight();
    }

    public int getOuterAbsoluteX() {
        return absX - borders.left;
    }

    public int getOuterAbsoluteY() {
        return absY - borders.top;
    }

    public int getOuterAbsoluteXRight() {
        return absX + getFullWidth();
    }

    public int getOuterAbsoluteYBottom() {
        return absY + getFullHeight();
    }

    public int getWidth() {
        return dimensions.width;
    }

    public int getHeight() {
        return dimensions.height;
    }

    public void setWidth(int width) {
        dimensions.width = width;
    }

    public void setHeight(int height) {
        dimensions.height = height;
    }

    public int getFullWidth() {
        return borders.left + dimensions.width + borders.right;
    }

    public int getFullHeight() {
        return borders.top + dimensions.height + borders.bottom;
    }

    public int getBorderLeft() {
        return borders.left;
    }

    public int getBorderRight() {
        return borders.right;
    }

    public int getBorderTop() {
        return borders.top;
    }

    public int getBorderBottom() {
        return borders.bottom;
    }

    public void setBorderLeft(int left) {
        borders.left = left;
    }

    public void setBorderRight(int right) {
        borders.right = right;
    }

    public void setBorderTop(int top) {
        borders.top = top;
    }

    public void setBorderBottom(int bottom) {
        borders.bottom = bottom;
    }

    public boolean isInside(double x, double y) {
        return getAbsoluteX() <= x &&
                getAbsoluteXRight() > x &&
                getAbsoluteY() <= y &&
                getAbsoluteYBottom() > y;
    }
}
