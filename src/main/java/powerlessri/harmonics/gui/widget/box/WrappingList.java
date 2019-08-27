/* Code adapted from Steve's Factory Manager 2 by Vswe/gigabte101.
 * https://github.com/gigabit101/HarmonicsCore/blob/2.0.X/src/main/java/powerlessri.harmonics.components/ScrollController.java
 */

package powerlessri.harmonics.gui.widget.box;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.lwjgl.glfw.GLFW;
import powerlessri.harmonics.gui.IWidget;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.RenderingHelper;
import powerlessri.harmonics.gui.widget.TextField;
import powerlessri.harmonics.gui.widget.*;
import powerlessri.harmonics.gui.widget.TextField.BackgroundStyle;
import powerlessri.harmonics.gui.widget.mixin.ResizableWidgetMixin;

import java.awt.*;
import java.util.List;
import java.util.*;


public class WrappingList<T extends IWidget & INamedElement> extends AbstractContainer<IWidget> implements ResizableWidgetMixin {

    // Scrolling states
    private int offset;
    private int rows;
    private boolean disabledScroll;
    private Rectangle contentArea = new Rectangle();

    // Search box states
    private boolean hasSearchBox;
    private List<T> searchResults;

    // Child widgets
    private TextField searchBox;
    private ScrollArrow scrollUpArrow;
    private ScrollArrow scrollDownArrow;
    private List<T> contents = new ArrayList<>();
    private List<IWidget> children;

    public WrappingList(boolean hasSearchBox) {
        this(hasSearchBox ? "" : null);
    }

    public WrappingList(String defaultText) {
        super(0, 0, 80, 80);
        this.contentArea.setSize(getDimensions());

        // Too lazy to add text change events, just make pressing enter update search
        this.hasSearchBox = defaultText != null;
        this.searchBox = hasSearchBox ? createSearchBox(defaultText) : TextField.DUMMY;

        this.scrollUpArrow = ScrollArrow.up(0, 0);
        this.scrollUpArrow.setParentWidget(this);
        this.scrollDownArrow = ScrollArrow.down(0, 0);
        this.scrollDownArrow.setParentWidget(this);
        this.alignArrows();

        this.children = new AbstractList<IWidget>() {
            @Override
            public IWidget get(int i) {
                switch (i) {
                    case 0: return searchBox;
                    case 1: return scrollUpArrow;
                    case 2: return scrollDownArrow;
                    default: return contents.get(i - 3);
                }
            }

            @Override
            public int size() {
                return 3 + contents.size();
            }
        };
        updateSearch();
    }

    private TextField createSearchBox(String defaultText) {
        TextField t = new TextField(0, 0, getSearchBoxWidth(), getSearchBoxHeight()) {
            @Override
            public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                if (keyCode == GLFW.GLFW_KEY_ENTER) {
                    updateSearch();
                    return true;
                }
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        };
        t.setParentWidget(this);
        t.setBackgroundStyle(BackgroundStyle.RED_OUTLINE);
        t.setEnabled(hasSearchBox);
        t.setText(defaultText);
        return t;
    }

    protected List<T> searchItems(String search) {
        List<T> result = new ArrayList<>();
        for (T child : contents) {
            if (search.equals(child.getName())) {
                result.add(child);
            }
        }
        return result;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isInside(mouseX, mouseY)) {
            return false;
        }

        // TODO fix focus target for pressing enter
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        if (!isInside(mouseX, mouseY)) {
            return false;
        }
        int x1 = getAbsoluteX() + getScrollingSectionY();
        int y1 = getAbsoluteY() + getScrollingSectionY();
        int x2 = x1 + getScrollingSectionWidth();
        int y2 = y1 + getScrollingSectionHeight();
        if (!AbstractWidget.isInside((int) mouseX, (int) mouseY, x1, y1, x2, y2)) {
            return false;
        }
        // "Windows style scrolling": scroll wheel is controlling the page
        scroll((int) scroll * -5);
        return true;
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        // TODO add search status
//            if (searchBox.getText().length() > 0 || children.size() > 0) {
//                gui.drawString(Localization.ITEMS_FOUND.toString() + " " + children.size(), getStatusTextX, getStatusTextY, 0.7F, 0x404040);
//            }

        RenderingHelper.drawRect(getAbsoluteX(), getAbsoluteY(), getAbsoluteXRight(), getAbsoluteYBottom(), 0xaaffffff);
        searchBox.render(mouseX, mouseY, particleTicks);
        scrollUpArrow.render(mouseX, mouseY, particleTicks);
        scrollDownArrow.render(mouseX, mouseY, particleTicks);

        int left = getAbsoluteX() + getScrollingSectionX();
        int top = getAbsoluteY() + getScrollingSectionY();
        RenderingHelper.enableScissor(left, top, contentArea.width, contentArea.height);

        int sy = getScrollingSectionY();
        for (T child : searchResults) {
            int cy = child.getY();
            if (cy + child.getHeight() > sy && cy < sy + contentArea.height) {
                child.render(mouseX, mouseY, particleTicks);
            }
        }

        RenderingHelper.disableScissor();
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    public void scroll(int change) {
        if (disabledScroll) {
            return;
        }
        offset += change;
        int min = 0;
        int max = rows * getItemSizeWithMargin() - getVisibleRows() * getItemSizeWithMargin();
        scrollUpArrow.setEnabled(true);
        scrollDownArrow.setEnabled(true);
        if (offset < min) {
            offset = min;
            scrollUpArrow.setEnabled(false);
        } else if (offset > max) {
            offset = max;
            scrollDownArrow.setEnabled(false);
        }

        reflow();
    }

    public void scrollUp(int change) {
        scroll(-change);
    }

    public void scrollUpUnit() {
        scroll(-getScrollSpeed());
    }

    public void scrollDown(int change) {
        scroll(change);
    }

    public void scrollDownUnit() {
        scroll(getScrollSpeed());
    }

    @Override
    public List<IWidget> getChildren() {
        return children;
    }

    public List<T> getContents() {
        return contents;
    }

    @Override
    public WrappingList<T> addChildren(IWidget widget) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WrappingList<T> addChildren(Collection<IWidget> widgets) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    public WrappingList<T> addElement(T widget) {
        Preconditions.checkArgument(widget.getWidth() == getItemSize() && widget.getHeight() == getItemSize());
        contents.add(widget);
        reflow();
        return this;
    }

    @Override
    public void reflow() {
        int initialX = getScrollingSectionX();
        int x = initialX;
        int y = getFirstRowY();
        rows = 1;
        for (T child : contents) {
            child.setLocation(x, y);
            x += getItemSizeWithMargin();
            if (x > contentArea.width) {
                x = initialX;
                y += getItemSizeWithMargin();
                rows++;
            }
        }
    }

    public void updateScrolling() {
        boolean canScroll = contents.size() > getItemsPerRow() * getVisibleRows();
        if (!canScroll) {
            offset = 0;
        }
        scrollUpArrow.setEnabled(canScroll);
        scrollDownArrow.setEnabled(canScroll);
    }

    public void updateSearch() {
        if (hasSearchBox()) {
            searchResults = searchBox.getText().isEmpty() ? contents : searchItems(searchBox.getText().toLowerCase());
        } else {
            searchResults = contents;
        }
        updateScrolling();
    }

    public void setSearchTarget(String s) {
        searchBox.setText(s);
        updateSearch();
    }

    public String getSearchTarget() {
        return searchBox.getText();
    }

    public void setDisabledScroll(boolean disabledScroll) {
        this.disabledScroll = disabledScroll;
    }

    public boolean hasSearchBox() {
        return hasSearchBox;
    }

    public Rectangle getContentArea() {
        return contentArea;
    }

    public int getScrollingSectionX() {
        return contentArea.x;
    }

    public int getScrollingSectionY() {
        return contentArea.y;
    }

    public int getScrollingSectionWidth() {
        return contentArea.width;
    }

    public int getScrollingSectionHeight() {
        return contentArea.height;
    }

    public int getItemsPerRow() {
        return (int) Math.ceil((double) contentArea.width / getItemSizeWithMargin());
    }

    public void setItemsPerRow(int itemsPerRow) {
        contentArea.width = itemsPerRow * getItemSizeWithMargin() - getMargin();
    }

    public int getVisibleRows() {
        return (int) Math.ceil((double) contentArea.height / getItemSizeWithMargin());
    }

    public void setVisibleRows(int visibleRows) {
        contentArea.height = visibleRows * getItemSizeWithMargin() - getMargin();
    }

    private int getFirstRowY() {
        return getScrollingSectionY() - offset;
    }

    public int getMargin() {
        return 4;
    }

    public int getItemSize() {
        return 16;
    }

    public int getItemSizeWithMargin() {
        return getItemSize() + getMargin();
    }

    public int getScrollSpeed() {
        return 5;
    }

    public int getSearchBoxWidth() {
        return 64;
    }

    public int getSearchBoxHeight() {
        return 12;
    }

    public int getStatusTextX() {
        return 75;
    }

    public int getStatusTextY() {
        return 9;
    }

    public ScrollArrow getScrollUpArrow() {
        return scrollUpArrow;
    }

    public ScrollArrow getScrollDownArrow() {
        return scrollDownArrow;
    }

    public TextField getSearchBox() {
        return searchBox;
    }

    public void placeArrows(int x, int y) {
        scrollUpArrow.setLocation(x, y);
        alignArrows();
    }

    public void alignArrows() {
        scrollDownArrow.setLocation(scrollUpArrow.getX(), scrollUpArrow.getY() + scrollUpArrow.getHeight() + getMargin());
    }
}