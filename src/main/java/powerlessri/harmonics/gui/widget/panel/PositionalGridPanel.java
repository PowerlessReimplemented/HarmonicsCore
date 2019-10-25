package powerlessri.harmonics.gui.widget.panel;

import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractContainer;
import powerlessri.harmonics.gui.widget.IWidget;

import java.util.*;

public class PositionalGridPanel<T extends IWidget> extends AbstractContainer<T> {

    private List<T> children = new ArrayList<>();
    private int cellSize;

    public PositionalGridPanel(int cellSize) {
        this.cellSize = cellSize;
    }

    @Override
    public Collection<T> getPanels() {
        return children;
    }

    @Override
    public PositionalGridPanel<T> addChildren(T widget) {
        children.add(widget);
        widget.attach(this);
        return this;
    }

    public PositionalGridPanel<T> addElement(T widget, int gridX, int gridY) {
        children.add(widget);
        widget.attach(this);
        widget.setLocation(gridX * cellSize, gridY * cellSize);
        return this;
    }

    @Override
    public void reflow() {
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        renderChildren(mouseX, mouseY, particleTicks);
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }
}
