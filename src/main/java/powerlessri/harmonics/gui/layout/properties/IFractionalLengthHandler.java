package powerlessri.harmonics.gui.layout.properties;

import java.util.function.IntSupplier;

@FunctionalInterface
public interface IFractionalLengthHandler extends IntSupplier {

    int getDenominator();

    @Deprecated
    @Override
    default int getAsInt() {
        return getDenominator();
    }
}
