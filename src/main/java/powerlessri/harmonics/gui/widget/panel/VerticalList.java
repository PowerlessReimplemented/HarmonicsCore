/* Code adapted from net.minecraftforge.client.gui.ScrollPanel
 */

package powerlessri.harmonics.gui.widget.panel;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.GuiUtils;
import powerlessri.harmonics.Config;
import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.ScissorTest;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractContainer;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.mixin.ResizableWidgetMixin;
import powerlessri.harmonics.utils.Utils;

import javax.annotation.Nonnegative;
import java.util.*;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static powerlessri.harmonics.gui.Render2D.*;

public class VerticalList<T extends IWidget> extends AbstractContainer<T> implements ResizableWidgetMixin {

    public static final int MIN_BAR_HEIGHT = 16;

    private boolean scrolling;
    protected float scrollDistance;

    private final List<T> elements;
    private int marginMiddle = 0;

    public VerticalList(int width, int height) {
        this.setDimensions(width, height);
        this.elements = new ArrayList<>();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isInside(mouseX, mouseY)) {
            return false;
        }
        scrolling = button == GLFW_MOUSE_BUTTON_LEFT && isInsideBar(mouseX, mouseY);
        if (scrolling) {
            setFocused(true);
            return true;
        }
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        setFocused(true);
        return false;
    }

    private boolean isInsideBar(double mouseX, double mouseY) {
        int barLeftX = getAbsBarLeft();
        return mouseX >= barLeftX && mouseX < barLeftX + getBarWidth()
                && mouseY >= getAbsoluteY() && mouseY < getAbsoluteYBottom();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!isInside(mouseX, mouseY)) {
            return false;
        }
        if (isInside(mouseX, mouseY)) {
            boolean ret = scrolling;
            scrolling = false;
            return ret;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }
        if (scrolling) {
            int maxScroll = getHeight() - getBarHeight();
            double moved = deltaY / maxScroll;
            scrollDistance += getMaxScroll() * moved;
            applyScrollLimits();
            reflow();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        if (super.mouseScrolled(mouseX, mouseY, scroll)) {
            return true;
        }
        if (isInside(mouseX, mouseY) && scroll != 0) {
            scrollDistance += -scroll * getScrollAmount();
            applyScrollLimits();
            reflow();
            return true;
        }
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        drawBackground();

        int left = getAbsoluteX();
        int top = getAbsoluteY();
        int bottom = getAbsoluteYBottom();
        int width = getWidth();
        int height = getHeight();

        ScissorTest test = ScissorTest.scaled(left, top, width, height);
        for (T child : getChildren()) {
            child.render(mouseX, mouseY, partialTicks);
        }
        drawOverlay();
        test.destroy();

        int extraHeight = getBarExtraHeight();
        if (extraHeight > 0) {
            int barWidth = getBarWidth();
            int barHeight = getBarHeight();
            int barTop = getAbsBarTop();
            int barBottom = barTop + barHeight;
            int barLeft = getAbsBarLeft();
            int barRight = barLeft + barWidth;

            GlStateManager.disableTexture();
            beginColoredQuad();
            coloredRect(barLeft, top, barRight, bottom, getZLevel(), getShadowColor());
            coloredRect(barLeft, barTop, barRight, barBottom, getZLevel(), getBarBorderColor());
            coloredRect(barLeft, barTop, barRight - 1, barBottom - 1, getZLevel(), getBarBodyColor());
            draw();
            GlStateManager.enableTexture();
        }

        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    public int getBarBodyColor() {
        return 0xffc0c0c0;
    }

    public int getBarBorderColor() {
        return 0xff808080;
    }

    public int getShadowColor() {
        return 0xff000000;
    }

    protected void drawOverlay() {
    }

    /**
     * Draw a vanilla style overlay.
     * <p>
     * If there is no world loaded, it will draw a dirt background; if a world is loaded, it will simply draw a vertical gradient rectangle
     * from {@code 0xc0101010} to {@code 0xd0101010}.
     */
    protected final void drawDefaultOverlay() {
        int left = getAbsoluteX();
        int top = getAbsoluteY();
        int right = getAbsoluteXRight();
        int bottom = getAbsoluteYBottom();
        if (Render2D.minecraft().world != null) {
            GuiUtils.drawGradientRect(0, left, top, right, bottom, 0xc0101010, 0xd0101010);
        } else {
            // Draw dark dirt background
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Render2D.bindTexture(AbstractGui.BACKGROUND_LOCATION);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            float texScale = 32.0F;
            BufferBuilder renderer = Tessellator.getInstance().getBuffer();
            renderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            renderer.pos(left, bottom, 0.0D).tex(left / texScale, (bottom + (int) scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            renderer.pos(right, bottom, 0.0D).tex(right / texScale, (bottom + (int) scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            renderer.pos(right, top, 0.0D).tex(right / texScale, (top + (int) scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            renderer.pos(left, top, 0.0D).tex(left / texScale, (top + (int) scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            draw();
        }
    }

    protected void drawBackground() {
    }

    @Override
    public List<T> getChildren() {
        return elements;
    }

    @Override
    public void reflow() {
        int offset = (int) -scrollDistance;
        int y = 0;
        for (T child : getChildren()) {
            child.setY(y + offset);
            y += child.getFullHeight() + getMarginMiddle();
        }
    }

    @Override
    public VerticalList<T> addChildren(T widget) {
        Preconditions.checkState(isValid());
        elements.add(widget);
        widget.attach(this);
        return this;
    }

    @Override
    public VerticalList<T> addChildren(Collection<T> widgets) {
        Preconditions.checkState(isValid());
        elements.addAll(widgets);
        for (T widget : widgets) {
            widget.attach(this);
        }
        return this;
    }

    protected int getContentHeight() {
        int contentHeight = 0;
        for (T child : getChildren()) {
            contentHeight += child.getFullHeight() + getMarginMiddle();
        }
        // Remove last unnecessary border
        return contentHeight - getMarginMiddle();
    }

    public int getScrollAmount() {
        return Config.CLIENT.scrollSpeed.get();
    }

    public int getMarginMiddle() {
        return marginMiddle;
    }

    public void setMarginMiddle(@Nonnegative int marginMiddle) {
        this.marginMiddle = Utils.lowerBound(marginMiddle, 0);
    }

    public int getBarLeft() {
        return getInnerXRight() - getBarWidth();
    }

    public int getAbsBarLeft() {
        return getAbsoluteXRight() - getBarWidth();
    }

    public int getMaxScroll() {
        return getContentHeight() - getHeight();
    }

    private void applyScrollLimits() {
        int max = Utils.lowerBound(getMaxScroll(), 0);
        scrollDistance = MathHelper.clamp(scrollDistance, 0F, max);
    }

    public int getBarWidth() {
        return 6;
    }

    public int getBarHeight() {
        int height = getHeight();
        return MathHelper.clamp((height * height) / getContentHeight(), MIN_BAR_HEIGHT, height);
    }

    public int getAbsBarTop() {
        int top = getAbsoluteY();
        return Utils.lowerBound((int) scrollDistance * (getHeight() - getBarHeight()) / getBarExtraHeight() + top, top);
    }

    public int getAbsBarBottom() {
        return getAbsBarTop() + getBarHeight();
    }

    public int getBarExtraHeight() {
        return (getContentHeight() + getBorderTop()) - getFullHeight();
    }

    public float getScrollDistance() {
        return scrollDistance;
    }

    public void setScrollDistance(float scrollDistance) {
        this.scrollDistance = scrollDistance;
        applyScrollLimits();
        reflow();
    }

    @Override
    public void provideInformation(ITextReceiver receiver) {
        super.provideInformation(receiver);
        receiver.line("Offset=" + scrollDistance);
    }
}
