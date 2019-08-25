package powerlessri.harmonics.gui;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;

import javax.annotation.Nullable;
import java.awt.*;

/**
 * Base class for all UI elements.
 *
 * <pre>
 *     +------------------------------+
 *     | outer element                |
 *     |                              |
 *     |   [1]''''''''''''''''''''    |
 *     |   | element  border     |    |
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
 */
public abstract class UIElement {

    private static final UIElement DUMMY = new UIElement() {};

    // Alias to Minecraft properties

    public static Minecraft minecraft() {
        return Minecraft.getInstance();
    }

    public static FontRenderer fontRenderer() {
        return Minecraft.getInstance().fontRenderer;
    }

    public static int textWidth(String text) {
        return Minecraft.getInstance().fontRenderer.getStringWidth(text);
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

    /**
     * Attache this element to a valid parent element, and populate the default child element by firing {@link #populateChildren()} if
     * necessary. If this is the first time attaching to some element tree, {@link #onAttach()} and {@link #populateChildren()} will be
     * fired. Otherwise {@link #onDetach()} will be fired along with {@link #onReattach()} if the parameter is nonnull.
     * <p>
     * <b>Notice</b> that the return value is intended to be used for chaining directly after element construct, with no following method
     * invocations. Additionally the return value will be automatically casted to anything that inherits from {@link UIElement}, therefore
     * please be careful with the source type and the expected type.
     * <p>
     * Good example:
     * <pre>{@code
     *     Label label = new Label("hello, world").attach(parent);
     * }</pre>
     * <p>
     * Bad example:
     * <pre>{@code
     *     element.attach(parent).doSomething();
     * }</pre>
     * In above example, the generic argument {@code THIS} will be inferred to {@link UIElement}, which is usually not very helpful.
     * <p>
     * Incorrect usage:
     * <pre>{@code
     *     TextField field = new Label("something").attach(parent);
     * }</pre>
     * The above example <b>will compile</b>, but will also throw a {@link ClassCastException} on execution. This is because the generic
     * argument can be anything that's a subtype of {@link UIElement} regardless of source type. The mistake in the example is obvious and
     * probably will not happen, however element creation sometimes go through a complicated series of method call and the original type
     * might be lost in the process.
     *
     * @param parent A valid parent element to be attached onto; if {@code null}, detaches from the currently attached tree.
     */
    @CanIgnoreReturnValue
    @SuppressWarnings("unchecked")
    public final <THIS extends UIElement> THIS attach(@Nullable UIElement parent) {
        Preconditions.checkArgument(parent == null || parent.isValid());

        boolean firstLoad = isFirstLoad();
        if (!firstLoad) {
            onDetach();
        }

        this.parent = parent;
        if (parent != null) {
            if (firstLoad) {
                onAttach();
                populateChildren();
            } else {
                onReattach();
            }

            updateAbsPos();
        } else {
            invalidateAbsPos();
        }
        return (THIS) this;
    }

    protected void onAttach() {
    }

    protected void onReattach() {
    }

    protected void onDetach() {
    }

    /**
     * Create default child elements that may exist for some implementation. This is guaranteed to be called only once and called in a ready
     * state (attached to tree).
     */
    protected void populateChildren() {
    }

    public boolean isValid() {
        return !isFirstLoad() && parent != DUMMY;
    }

    private boolean isFirstLoad() {
        return parent == null;
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

    private void invalidateAbsPos() {
        absX = 0;
        absY = 0;
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

    public void render(RenderingContext context) {
    }
}
