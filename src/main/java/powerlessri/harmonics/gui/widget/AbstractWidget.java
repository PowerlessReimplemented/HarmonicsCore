package powerlessri.harmonics.gui.widget;

import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.contextmenu.ContextMenuBuilder;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.Inspections;
import powerlessri.harmonics.gui.layout.properties.*;
import powerlessri.harmonics.gui.widget.mixin.ResizableWidgetMixin;
import powerlessri.harmonics.gui.window.IWindow;

import javax.annotation.Nullable;
import java.awt.*;

public abstract class AbstractWidget implements IWidget, Inspections.IInfoProvider, Inspections.IHighlightRenderer, ISizedBox, ResizableWidgetMixin {

    private Point location;
    private Dimension dimensions;
    private Insets border = new Insets(0, 0, 0, 0);
    private float z = 0F;

    private boolean enabled = true;
    private IWindow window;
    private IWidget parent;

    // Cached because this might reach all the up to the root node by recursion on getAbsoluteX/Y
    private int absX;
    private int absY;

    public AbstractWidget(int width, int height) {
        this(0, 0, width, height);
    }

    public AbstractWidget(int x, int y, int width, int height) {
        this.location = new Point(x, y);
        this.dimensions = new Dimension(width, height);
    }

    @Override
    public void attach(IWidget newParent) {
        IWidget oldParent = parent;
        this.parent = newParent;
        this.window = newParent.getWindow();
        onParentPositionChanged();
        onAttach(oldParent, newParent);
        if (oldParent == null) {
            onInitialAttach();
        }
        z = newParent.getZLevel() + 1F;
    }

    public void onAttach(@Nullable IWidget oldParent, IWidget newParent) {
    }

    public void onInitialAttach() {
    }

    @Override
    public boolean isValid() {
        return parent != null;
    }

    public void setFocused(boolean focused) {
        if (getWindow() != null) {
            getWindow().setFocusedWidget(focused ? this : null);
        }
    }

    public void attachWindow(IWindow window) {
        this.window = window;
        this.z = window.getZLevel() + 1F;
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
        if (this.getParent() != other.getParent()) {
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
        setX(Render2D.computeCenterX(left, right, getFullWidth()));
    }

    public void alignRight(int right) {
        setX(Render2D.computeRightX(right, getFullWidth()));
    }

    public void alignTop(int top) {
        setY(top);
    }

    public void alignCenterY(int top, int bottom) {
        setY(Render2D.computeCenterY(top, bottom, getFullHeight()));
    }

    public void alignBottom(int bottom) {
        setY(Render2D.computeBottomY(bottom, getFullHeight()));
    }

    @Override
    public float getZLevel() {
        return z;
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
    public void setLocation(Point point) {
        setLocation(point.x, point.y);
    }

    @Override
    public void setLocation(int x, int y) {
        getPosition().x = x;
        getPosition().y = y;
        onRelativePositionChanged();
    }

    @Override
    public void setX(int x) {
        getPosition().x = x;
        onRelativePositionChanged();
    }

    @Override
    public void setY(int y) {
        getPosition().y = y;
        onRelativePositionChanged();
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
        return absX - border.left;
    }

    @Override
    public int getOuterAbsoluteY() {
        return absY - border.top;
    }

    public int getOuterAbsoluteXRight() {
        return absX + getFullWidth();
    }

    public int getOuterAbsoluteYBottom() {
        return absY + getFullHeight();
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
    public IWidget getParent() {
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
        return getOuterAbsoluteX() <= x &&
                getOuterAbsoluteXRight() > x &&
                getOuterAbsoluteY() <= y &&
                getOuterAbsoluteYBottom() > y;
    }

    @Override
    public BoxSizing getBoxSizing() {
        return BoxSizing.BORDER_BOX;
    }

    @Override
    public void provideInformation(ITextReceiver receiver) {
        receiver.line(this.toString());
        receiver.line("Position=(" + location.x + ", " + location.y + ")");
        receiver.line("Dimensions=(" + dimensions.width + ", " + dimensions.height + ")");
        receiver.line(String.format("Borders={top: %d, right: %d, bottom: %d, left: %d}", border.top, border.right, border.bottom, border.left));
        receiver.line("Enabled=" + isEnabled());
        receiver.line("Z=" + z);
        receiver.line("AbsX=" + getAbsoluteX());
        receiver.line("AbsY=" + getAbsoluteY());
    }

    @Override
    public void renderHighlight() {
        Inspections.renderBorderedHighlight(
                getOuterAbsoluteX(), getOuterAbsoluteY(),
                getAbsoluteX(), getAbsoluteY(),
                getWidth(), getHeight(),
                getFullWidth(), getFullHeight());
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

    @Override
    public void setBorders(int borders) {
        setBorders(borders, borders, borders, borders);
    }

    protected void onBorderChanged() {
        updateAbsolutePosition();
    }

    public final void createContextMenu(double x, double y) {
        ContextMenuBuilder builder = new ContextMenuBuilder();
        buildContextMenu(builder);
        builder.buildAndAdd();
    }

    protected void buildContextMenu(ContextMenuBuilder builder) {
    }
}
