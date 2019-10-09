package powerlessri.harmonics.gui.widget.navigation;

import powerlessri.harmonics.gui.layout.FlowLayout;
import powerlessri.harmonics.gui.widget.AbstractContainer;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.window.AbstractDockableWindow;

import java.util.*;

public class NavigationBar extends AbstractContainer<IWidget> {

    /**
     * Construct and return a standard navigation bar with three buttons: close window, maximize window, and minimize window. This does not
     * attach the return navigation bar to the given window, user must manually attach it.
     */
    public static <T extends IWidget> NavigationBar standard(AbstractDockableWindow<T> window) {
        NavigationBar bar = new NavigationBar(12);
        return bar;
    }

    private List<IWidget> children = new ArrayList<>();

    private int initialDragLocalX;
    private int initialDragLocalY;

    public NavigationBar(int height) {
        this.setHeight(height);
    }

    @Override
    public Collection<IWidget> getChildren() {
        return children;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isInside(mouseX, mouseY)) {
            initialDragLocalX = (int) mouseX - getX();
            initialDragLocalY = (int) mouseY - getY();
            getWindow().setFocusedWidget(this);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }
        if (isInside(mouseX, mouseY) && isDragging()) {
            int x = (int) mouseX - initialDragLocalX;
            int y = (int) mouseY - initialDragLocalY;
            getWindow().setPosition(x, y);
            return true;
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isInside(mouseX, mouseY)) {
            initialDragLocalX = -1;
            initialDragLocalY = -1;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean isDragging() {
        return initialDragLocalX != -1 && initialDragLocalY != -1;
    }

    @Override
    public NavigationBar addChildren(IWidget widget) {
        children.add(widget);
        widget.attach(this);
        return this;
    }

    @Override
    public NavigationBar addChildren(Collection<IWidget> widgets) {
        children.addAll(widgets);
        for (IWidget widget : widgets) {
            widget.attach(this);
        }
        return this;
    }

    @Override
    public void reflow() {
        FlowLayout.reverseHorizontal(children, getXRight(), 0, 2);
    }
}
