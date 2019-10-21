package powerlessri.harmonics.gui;

import org.jetbrains.annotations.NotNull;

public interface IOrdered extends Comparable<IOrdered> {

    int getOrder();

    void setOrder(int order);

    @Override
    default int compareTo(@NotNull IOrdered that) {
        return this.getOrder() - that.getOrder();
    }
}
