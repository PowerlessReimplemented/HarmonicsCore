package powerlessri.harmonics.gui.widget.box;

import com.google.common.collect.ImmutableList;
import powerlessri.harmonics.gui.widget.AbstractContainer;
import powerlessri.harmonics.gui.widget.IWidget;

import java.util.Collection;

public class GridPanel<T extends IWidget> extends AbstractContainer<T> {

    public GridPanel() {
        super(0, 0);
    }

    @Override
    public Collection<T> getChildren() {
        return ImmutableList.of();
    }

    @Override
    public void reflow() {
    }
}
