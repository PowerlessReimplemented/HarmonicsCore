package powerlessri.harmonics.gui.widget.panel;

import com.google.common.collect.ImmutableList;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractContainer;
import powerlessri.harmonics.gui.widget.IWidget;

import java.util.Collection;

// TODO
public class GridPanel<T extends IWidget> extends AbstractContainer<T> {

    @Override
    public Collection<T> getPanels() {
        return ImmutableList.of();
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
