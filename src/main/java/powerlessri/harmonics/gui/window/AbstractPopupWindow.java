package powerlessri.harmonics.gui.window;

import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.debug.ITextReceiver;

/**
 * Simple base class for a draggable popup. For more complicated usages see {@link AbstractDockableWindow}.
 */
public abstract class AbstractPopupWindow extends AbstractWindow implements IPopupWindow {

    private int initialDragLocalX, initialDragLocalY;
    private boolean alive = true;
    private int order;

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClickSubtree(mouseX, mouseY, button)) {
            return true;
        }
        if (isInside(mouseX, mouseY)) {
            setFocusedWidget(null);
            initialDragLocalX = (int) mouseX - getX();
            initialDragLocalY = (int) mouseY - getY();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (super.mouseDraggedSubtree(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }
        if (isDragging()) {
            int x = (int) mouseX - initialDragLocalX;
            int y = (int) mouseY - initialDragLocalY;
            setPosition(x, y);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.mouseReleasedSubtree(mouseX, mouseY, button)) {
            return true;
        }
        if (isInside(mouseX, mouseY)) {
            initialDragLocalX = -1;
            initialDragLocalY = -1;
            return true;
        }
        return false;
    }

    private boolean isDragging() {
        return initialDragLocalX != -1 && initialDragLocalY != -1;
    }

    public void discard() {
        alive = false;
    }

    @Override
    public boolean shouldDiscard() {
        return !alive;
    }

    @Override
    public float getZLevel() {
        return Render2D.POPUP_WINDOW_Z;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public void provideInformation(ITextReceiver receiver) {
        super.provideInformation(receiver);
        receiver.line("Order=" + order);
    }
}
