package powerlessri.harmonics.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import powerlessri.harmonics.gui.*;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.Inspections;
import powerlessri.harmonics.gui.layout.ILayoutDataProvider;
import powerlessri.harmonics.gui.layout.properties.*;
import powerlessri.harmonics.gui.widget.mixin.ResizableWidgetMixin;

import javax.annotation.Nullable;
import java.awt.*;

public abstract class AbstractWidget implements IWidget, Inspections.IInspectionInfoProvider, ILayoutDataProvider, ResizableWidgetMixin {

    public static boolean isInside(int x, int y, int mx, int my) {
        return isInside(x, y, 0, 0, mx, my);
    }

    public static boolean isInside(int x, int y, int bx1, int by1, int bx2, int by2) {
        return x >= bx1 &&
                x < bx2 &&
                y >= by1 &&
                y < by2;
    }

    public static Minecraft minecraft() {
        return Minecraft.getInstance();
    }

    public static FontRenderer fontRenderer() {
        return Minecraft.getInstance().fontRenderer;
    }

    private Point location;
    private Dimension dimensions;
    private Insets border = new Insets(0, 0, 0, 0);

    private boolean enabled = true;
    private IWindow window;

    private IWidget parent;

    // Cached because this might reach all the up to the root node by recursion on getAbsoluteX/Y
    private int absX;
    private int absY;

    public AbstractWidget(IWindow window) {
        this(0, 0, window.getContentDimensions().width, window.getContentDimensions().height);
        this.window = window;
    }

    public AbstractWidget() {
        this(0, 0, 0, 0);
    }

    public AbstractWidget(int x, int y, int width, int height) {
        this.location = new Point(x, y);
        this.dimensions = new Dimension(width, height);
    }

    @Override
    public void setParentWidget(IWidget newParent) {
        this.parent = newParent;
        this.window = newParent.getWindow();
        onParentPositionChanged();
    }

    public void setWindow(IWindow window) {
        this.window = window;
        onParentPositionChanged();
    }

    @Override
    public void onParentPositionChanged() {
        updateAbsolutePosition();
    }

    @Override
    public void onRelativePositionChanged() {
        updateAbsolutePosition();
    }

    private void updateAbsolutePosition() {
        absX = getParentAbsXSafe() + getX() + getBorderLeft();
        absY = getParentAbsYSafe() + getY() + getBorderTop();
    }

    private int getParentAbsXSafe() {
        if (parent != null) {
            return parent.getAbsoluteX();
        }
        if (window != null) {
            return window.getContentX();
        }
        return 0;
    }

    private int getParentAbsYSafe() {
        if (parent != null) {
            return parent.getAbsoluteY();
        }
        if (window != null) {
            return window.getContentY();
        }
        return 0;
    }

    /**
     * A safe version of {@code getParentWidget().getHeight()} that prevents NPE.
     */
    public int getParentHeight() {
        if (parent != null) {
            return parent.getFullHeight();
        }
        if (window != null) {
            return window.getContentHeight();
        }
        return 0;
    }

    /**
     * A safe version of {@code getParentWidget().getWidth()} that prevents NPE.
     */
    public int getParentWidth() {
        if (parent != null) {
            return parent.getFullWidth();
        }
        if (window != null) {
            return window.getContentWidth();
        }
        return 0;
    }

    public void fillParentContainer() {
        setLocation(0, 0);
        setDimensions(parent.getDimensions());
    }

    public void expandHorizontally() {
        setWidth(Math.max(getFullWidth(), getParentWidth()));
    }

    public void expandVertically() {
        setHeight(Math.max(getFullHeight(), getParentHeight()));
    }

    @Override
    public boolean isFocused() {
        return getWindow().getFocusedWidget() == this;
    }

    public void alignTo(IWidget other, Side side, HorizontalAlignment alignment) {
        if (this.getParentWidget() != other.getParentWidget()) {
            return;
        }

        int otherLeft = other.getX();
        int otherTop = other.getY();
        int otherRight = otherLeft + other.getFullWidth();
        int otherBottom = otherTop + other.getFullHeight();

        switch (side) {
            case TOP:
                alignBottom(otherTop);
                alignHorizontally(alignment, otherLeft, otherRight);
                break;
            case BOTTOM:
                alignTop(otherBottom);
                alignHorizontally(alignment, otherLeft, otherRight);
                break;
            case LEFT:
                alignRight(otherLeft);
                alignVertically(alignment, otherTop, otherBottom);
                break;
            case RIGHT:
                alignLeft(otherRight);
                alignVertically(alignment, otherTop, otherBottom);
                break;
        }
    }

    private void alignHorizontally(HorizontalAlignment alignment, int left, int right) {
        switch (alignment) {
            case LEFT:
                alignLeft(left);
                break;
            case CENTER:
                alignCenterX(left, right);
                break;
            case RIGHT:
                alignRight(right);
                break;
        }
    }

    private void alignVertically(HorizontalAlignment alignment, int top, int bottom) {
        switch (alignment) {
            case LEFT:
                alignTop(top);
                break;
            case CENTER:
                alignCenterY(top, bottom);
                break;
            case RIGHT:
                alignBottom(bottom);
                break;
        }
    }

    public void alignLeft(int left) {
        setX(left);
    }

    public void alignCenterX(int left, int right) {
        setX(RenderingHelper.getXForAlignedCenter(left, right, getFullWidth()));
    }

    public void alignRight(int right) {
        setX(RenderingHelper.getXForAlignedRight(right, getFullWidth()));
    }

    public void alignTop(int top) {
        setY(top);
    }

    public void alignCenterY(int top, int bottom) {
        setY(RenderingHelper.getYForAlignedCenter(top, bottom, getFullHeight()));
    }

    public void alignBottom(int bottom) {
        setY(RenderingHelper.getYForAlignedBottom(bottom, getFullHeight()));
    }

    @Override
    public Point getPosition() {
        return location;
    }

    @Override
    public Dimension getDimensions() {
        return dimensions;
    }

    @Override
    public Insets getBorders() {
        return border;
    }

    @Override
    public int getX() {
        return location.x;
    }

    @Override
    public int getY() {
        return location.y;
    }

    public int getXRight() {
        return location.x + getFullWidth();
    }

    public int getYBottom() {
        return location.y + getFullWidth();
    }

    @Override
    public int getInnerX() {
        return location.x + border.left;
    }

    @Override
    public int getInnerY() {
        return location.y + border.top;
    }

    public int getInnerXRight() {
        return location.x + border.left + dimensions.width;
    }

    public int getInnerYBottom() {
        return location.y + border.top + dimensions.height;
    }

    @Override
    public int getAbsoluteX() {
        return absX;
    }

    @Override
    public int getAbsoluteY() {
        return absY;
    }

    public int getAbsoluteXRight() {
        return absX + dimensions.width;
    }

    public int getAbsoluteYBottom() {
        return absY + dimensions.height;
    }

    @Override
    public int getOuterAbsoluteX() {
        return absX - border.top;
    }

    @Override
    public int getOuterAbsoluteY() {
        return absY - border.left;
    }

    public int getOuterAbsoluteXRight() {
        return absX + getFullWidth();
    }

    public int getOuterAbsoluteYBottom() {
        return absY + getFullWidth();
    }

    @Override
    public int getWidth() {
        return dimensions.width;
    }

    @Override
    public int getHeight() {
        return dimensions.height;
    }

    @Override
    public int getFullWidth() {
        return border.left + dimensions.width + border.right;
    }

    @Override
    public int getFullHeight() {
        return border.top + dimensions.height + border.bottom;
    }

    public void moveX(int dx) {
        setX(getX() + dx);
    }

    public void moveY(int dy) {
        setY(getY() + dy);
    }

    public void move(int dx, int dy) {
        moveX(dx);
        moveY(dy);
    }

    @Nullable
    @Override
    public IWidget getParentWidget() {
        return parent;
    }

    @Override
    public IWindow getWindow() {
        return window;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isInside(double x, double y) {
        return getAbsoluteX() <= x &&
                getAbsoluteXRight() > x &&
                getAbsoluteY() <= y &&
                getAbsoluteYBottom() > y;
    }

    @Override
    public BoxSizing getBoxSizing() {
        return BoxSizing.BORDER_BOX;
    }

    @Override
    public void provideInformation(ITextReceiver receiver) {
        receiver.line(this.toString());
        receiver.line("Position=" + this.getPosition());
        receiver.line("AbsX=" + this.getAbsoluteX());
        receiver.line("AbsY=" + this.getAbsoluteY());
        receiver.line("Dimensions=" + this.getDimensions());
        receiver.line("Borders=" + this.getBorders());
        receiver.line("Enabled=" + this.isEnabled());
    }

    @Override
    public int getBorderTop() {
        return border.top;
    }

    @Override
    public int getBorderRight() {
        return border.right;
    }

    @Override
    public int getBorderBottom() {
        return border.bottom;
    }

    @Override
    public int getBorderLeft() {
        return border.left;
    }

    public int getVerticalBorder() {
        return border.top + border.bottom;
    }

    public int getHorizontalBorder() {
        return border.left + border.right;
    }

    @Override
    public void setBorderTop(int top) {
        getBorders().top = top;
        onBorderChanged();
    }

    @Override
    public void setBorderRight(int right) {
        getBorders().right = right;
        onBorderChanged();
    }

    @Override
    public void setBorderBottom(int bottom) {
        getBorders().bottom = bottom;
        onBorderChanged();
    }

    @Override
    public void setBorderLeft(int left) {
        getBorders().left = left;
        onBorderChanged();
    }

    @Override
    public void setBorders(int top, int right, int bottom, int left) {
        getBorders().top = top;
        getBorders().right = right;
        getBorders().bottom = bottom;
        getBorders().left = left;
        onBorderChanged();
    }

    protected void onBorderChanged() {
        updateAbsolutePosition();
    }
}
