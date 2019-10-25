package powerlessri.harmonics.gui.widget.panel;

import net.minecraft.util.IStringSerializable;
import powerlessri.harmonics.collections.ReferenceList;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractContainer;
import powerlessri.harmonics.gui.widget.IWidget;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TabbedPanel<P extends IWidget> extends AbstractContainer<IWidget> {

    private TabHorizontalList tabs;
    private P activePanel;
    private final Collection<IWidget> children;

    private List<P> panels = new ArrayList<>();

    public TabbedPanel() {
        tabs = new TabHorizontalList(0, 16);
        children = ReferenceList.of(this::getTabsPanel, this::getActivePanelRaw);
    }

    public TabbedPanel<P> addPanel(P widget) {
        String name = widget instanceof IStringSerializable ? ((IStringSerializable) widget).getName() : "";
        panels.add(widget);
        tabs.addChildren(new Tab(name));
        widget.attach(this);
        return this;
    }

    public TabbedPanel<P> addPanel(Collection<P> widgets) {
        for (P widget : widgets) {
            String name = widget instanceof IStringSerializable ? ((IStringSerializable) widget).getName() : "";
            tabs.addChildren(new Tab(name));
            panels.add(widget);
            widget.attach(this);
        }
        return this;
    }

    @Override
    public Collection<IWidget> getPanels() {
        return children;
    }

    @Override
    public void onDimensionChanged() {
        reflow();
    }

    @Override
    public void reflow() {
        tabs.expandHorizontally();
        tabs.reflow();
    }

    public HorizontalList<Tab> getTabsPanel() {
        return tabs;
    }

    @Nullable
    private P getActivePanelRaw() {
        return activePanel;
    }

    public P getActivePanel() {
        if (activePanel == null) {
            activePanel = panels.get(0);
        }
        return activePanel;
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);

        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }
}
