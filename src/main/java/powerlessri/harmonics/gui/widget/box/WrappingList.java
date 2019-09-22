/* Code adapted from Steve's Factory Manager 2 by Vswe/gigabte101.
 * https://github.com/gigabit101/HarmonicsCore/blob/2.0.X/src/main/java/powerlessri.harmonics.components/ScrollController.java
 */

package powerlessri.harmonics.gui.widget.box;

import com.google.common.base.Preconditions;
import powerlessri.harmonics.gui.IWidget;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractContainer;
import powerlessri.harmonics.gui.widget.AbstractWidget;
import powerlessri.harmonics.gui.ScissorTest;
import powerlessri.harmonics.utils.Utils;

import java.util.*;

public class WrappingList extends AbstractContainer<IWidget> {

    // Scrolling states
    private int offset;
    private int rows;
    private boolean disabledScroll;

    // Child widgets
    private ScrollArrow scrollUpArrow;
    private ScrollArrow scrollDownArrow;
    private List<IWidget> contents = new ArrayList<>();
    private List<IWidget> children;

    public WrappingList() {
        super(0, 0, 80, 80);

        this.scrollUpArrow = ScrollArrow.up(0, 0);
        this.scrollUpArrow.attach(this);
        this.scrollDownArrow = ScrollArrow.down(0, 0);
        this.scrollDownArrow.attach(this);
        this.alignArrows();

        this.children = new AbstractList<IWidget>() {
            @Override
            public IWidget get(int i) {
                switch (i) {
                    case 0: return scrollUpArrow;
                    case 1: return scrollDownArrow;
                    default: return contents.get(i - 2);
                }
            }

            @Override
            public int size() {
                return 2 + contents.size();
            }
        };

        // Update arrow states
        this.scroll(1);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        if (!isInside(mouseX, mouseY) || !isEnabled()) {
            return false;
        }
        int x1 = getAbsoluteX();
        int y1 = getAbsoluteY();
        int x2 = x1 + getFullWidth();
        int y2 = y1 + getFullHeight();
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

        scrollUpArrow.render(mouseX, mouseY, particleTicks);
        scrollDownArrow.render(mouseX, mouseY, particleTicks);

        ScissorTest test = ScissorTest.scaled(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());

        int rTop = 0;
        int rBottom = getHeight();
        for (IWidget child : contents) {
            int y = child.getY();
            if (y >= rTop && y < rBottom) {
                child.render(mouseX, mouseY, particleTicks);
            }
        }

        test.destroy();
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    public void scroll(int change) {
        if (disabledScroll) {
            return;
        }
        offset += change;
        int min = 0;
        int contentHeight = rows * getItemSizeWithMargin();
        int visibleHeight = getVisibleRows() * getItemSizeWithMargin();
        int max = Utils.lowerBound(contentHeight - visibleHeight, 0);
        scrollUpArrow.setEnabled(true);
        scrollDownArrow.setEnabled(true);
        if (max == 0) {
            offset = 0;
            scrollUpArrow.setEnabled(false);
            scrollDownArrow.setEnabled(false);
        } else if (offset < min) {
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

    public List<IWidget> getContents() {
        return contents;
    }

    @Override
    public WrappingList addChildren(IWidget widget) {
        Preconditions.checkArgument(widget.getFullWidth() == getItemSize() && widget.getFullHeight() == getItemSize());
        widget.attach(this);
        contents.add(widget);
        reflow();
        return this;
    }

    @Override
    public WrappingList addChildren(Collection<IWidget> widgets) {
        for (IWidget widget : widgets) {
            Preconditions.checkArgument(widget.getFullWidth() == getItemSize() && widget.getFullHeight() == getItemSize());
            widget.attach(this);
            contents.add(widget);
        }
        reflow();
        return this;
    }

    public void setContentList(List<IWidget> list) {
        this.contents = list;
        for (IWidget widget : list) {
            widget.attach(this);
        }
    }

    @Override
    public void reflow() {
        int x = 0;
        int y = getFirstRowY();
        rows = 1;
        for (IWidget child : contents) {
            child.setLocation(x, y);
            x += getItemSizeWithMargin();
            if (x > getWidth()) {
                x = 0;
                y += getItemSizeWithMargin();
                rows++;
            }
        }
    }

    private int getFirstRowY() {
        return -offset;
    }

    public void setDisabledScroll(boolean disabledScroll) {
        this.disabledScroll = disabledScroll;
    }

    public int getItemsPerRow() {
        return (int) Math.ceil((double) getWidth() / getItemSizeWithMargin());
    }

    public void setItemsPerRow(int itemsPerRow) {
        setWidth(itemsPerRow * getItemSizeWithMargin() - getMargin());
    }

    public int getVisibleRows() {
        return (int) Math.ceil((double) getHeight() / getItemSizeWithMargin());
    }

    public void setVisibleRows(int visibleRows) {
        setHeight(visibleRows * getItemSizeWithMargin() - getMargin());
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

    public ScrollArrow getScrollUpArrow() {
        return scrollUpArrow;
    }

    public ScrollArrow getScrollDownArrow() {
        return scrollDownArrow;
    }

    public void placeArrows(int x, int y) {
        scrollUpArrow.setLocation(x, y);
        alignArrows();
    }

    public void alignArrows() {
        scrollDownArrow.setLocation(scrollUpArrow.getX(), scrollUpArrow.getY() + scrollUpArrow.getFullHeight() + getMargin());
    }

    @Override
    public void provideInformation(ITextReceiver receiver) {
        super.provideInformation(receiver);
        receiver.line("Offset=" + offset);
    }
}