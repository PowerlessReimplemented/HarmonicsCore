package powerlessri.harmonics.gui.widget.panel;

import com.google.common.collect.ImmutableList;
import powerlessri.harmonics.gui.widget.AbstractContainer;
import powerlessri.harmonics.gui.widget.IWidget;

import java.util.Collection;

public class GridPanel<T extends IWidget> extends AbstractContainer<T> {

    @Override
    public Collection<T> getChildren() {
        return ImmutableList.of();
    }

    @Override
    public void reflow() {
    }
}
