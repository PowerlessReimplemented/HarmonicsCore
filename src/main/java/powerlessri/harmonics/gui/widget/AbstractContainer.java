package powerlessri.harmonics.gui.widget;

import powerlessri.harmonics.gui.contextmenu.ContextMenuBuilder;
import powerlessri.harmonics.gui.widget.mixin.ContainerWidgetMixin;
import powerlessri.harmonics.gui.widget.mixin.RelocatableContainerMixin;
import powerlessri.harmonics.gui.window.IWindow;

import java.util.Collection;

public abstract class AbstractContainer<T extends IWidget> extends AbstractWidget implements IContainer<T>, ContainerWidgetMixin<T>, RelocatableContainerMixin<T> {

    public AbstractContainer(IWindow window) {
        super(window);
    }

    public AbstractContainer() {
    }

    public AbstractContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void attach(IWidget newParent) {
        super.attach(newParent);
        ContainerWidgetMixin.super.attach(newParent);
    }

    @Override
    public IContainer<T> addChildren(T widget) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IContainer<T> addChildren(Collection<T> widgets) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void attachWindow(IWindow window) {
        super.attachWindow(window);
        for (T child : getChildren()) {
            // This will also inherit window reference from this widget
            child.attach(this);
        }
    }

    @Override
    public void onParentPositionChanged() {
        super.onParentPositionChanged();
        notifyChildrenForPositionChange();
    }

    @Override
    public void onRelativePositionChanged() {
        super.onRelativePositionChanged();
        notifyChildrenForPositionChange();
    }

    public void adjustMinContent() {
        if (getChildren().isEmpty()) {
            return;
        }

        int rightmost = 0;
        int bottommost = 0;
        for (IWidget child : getChildren()) {
            int right = child.getX() + child.getFullWidth();
            int bottom = child.getY() + child.getFullHeight();
            if (right > rightmost) {
                rightmost = right;
            }
            if (bottom > bottommost) {
                bottommost = bottom;
            }
        }
        setDimensions(rightmost, bottommost);
    }

    public void fillWindow() {
        setLocation(0, 0);
        setDimensions(getWindow().getContents());
    }

    @Override
    protected void buildContextMenu(ContextMenuBuilder builder) {
        propagateBuildActionMenu(builder);
    }

    private void propagateBuildActionMenu(ContextMenuBuilder builder) {
        propagateBuildActionMenu(this, builder);
    }

    private static void propagateBuildActionMenu(IContainer<?> container, ContextMenuBuilder builder) {
        for (IWidget child : container.getChildren()) {
            if (child instanceof AbstractWidget) {
                ((AbstractWidget) child).buildContextMenu(builder);
            } else if (child instanceof IContainer<?>) {
                propagateBuildActionMenu((IContainer<?>) child, builder);
            }
        }
    }
}
