package powerlessri.harmonics.gui.widget.box;

import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractContainer;
import powerlessri.harmonics.gui.widget.mixin.ResizableWidgetMixin;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;

/**
 * A ready-to-use box widget for grouping widgets.
 */
public class Box<T extends IWidget> extends AbstractContainer<T> implements ResizableWidgetMixin {

    private List<T> children = new ArrayList<>();

    private Consumer<List<T>> layout = l -> {};
    private boolean paused = false;

    public Box(int width, int height) {
        this(0, 0, width, height);
    }

    public Box(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public List<T> getChildren() {
        return children;
    }

    @Override
    public Box<T> addChildren(T widget) {
        children.add(widget);
        widget.attach(this);
        reflow();
        return this;
    }

    @Override
    public Box<T> addChildren(Collection<T> widgets) {
        children.addAll(widgets);
        for (T widget : widgets) {
            widget.attach(this);
        }
        reflow();
        return this;
    }

    @Override
    public Box<T> addChildren(Iterable<T> widgets) {
        super.addChildren(widgets);
        return this;
    }

    @Override
    public Box<T> addChildren(Iterator<T> widgets) {
        super.addChildren(widgets);
        return this;
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        if (isEnabled()) {
            RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
            for (T child : children) {
                child.render(mouseX, mouseY, particleTicks);
            }
            RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
        }
    }

    /**
     * Cancel all reflow actions until {@link #unpause()} gets triggered. This should be used as a way to avoid unnecessary reflow (layout
     * updates) when changing widget properties in batch.
     */
    @SuppressWarnings("UnusedReturnValue")
    public Box<T> pause() {
        paused = true;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Box<T> unpause() {
        paused = false;
        reflow();
        return this;
    }

    public boolean isPaused() {
        return paused;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Box<T> setLayout(Consumer<List<T>> layout) {
        this.layout = layout;
        return this;
    }

    @Override
    public void reflow() {
        layout.accept(children);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isEnabled()) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isEnabled()) {
            return super.mouseReleased(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isEnabled()) {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        if (isEnabled()) {
            return super.mouseScrolled(mouseX, mouseY, scroll);
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isEnabled()) {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (isEnabled()) {
            return super.keyReleased(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public boolean charTyped(char charTyped, int keyCode) {
        if (isEnabled()) {
            return super.charTyped(charTyped, keyCode);
        }
        return false;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (isEnabled()) {
            super.mouseMoved(mouseX, mouseY);
        }
    }

    @Override
    public void update(float particleTicks) {
        if (isEnabled()) {
            super.update(particleTicks);
        }
    }
}
