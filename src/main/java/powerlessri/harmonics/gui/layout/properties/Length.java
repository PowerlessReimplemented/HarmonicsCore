package powerlessri.harmonics.gui.layout.properties;

import com.google.common.base.Preconditions;
import powerlessri.harmonics.gui.widget.IWidget;

public abstract class Length<T> {

    public static Length<?> px(int pixels) {
        return new Length<Object>() {
            @Override
            public void resolve(Object target) {
                this.resolved = true;
            }

            @Override
            public float get() {
                Preconditions.checkState(resolved);
                return pixels;
            }
        };
    }

    public static <T extends IFractionalLengthHandler> Length<T> fr(int numerator) {
        return new Length<T>() {
            private float value;

            @Override
            public void resolve(T target) {
                value = (float) numerator / target.getDenominator();
                this.resolved = true;
            }

            @Override
            public float get() {
                return value;
            }
        };
    }

    public static <W extends IFractionalLengthHandler> Length<W> auto() {
        return fr(1);
    }

    public static Length<IWidget> ww(int n) {
        Preconditions.checkArgument(n <= 100);
        return new Length<IWidget>() {
            private float actualLength;

            @Override
            public void resolve(IWidget target) {
                actualLength = (float) target.getWindow().getWidth() / 100 * n;
                this.resolved = true;
            }

            @Override
            public float get() {
                return actualLength;
            }
        };
    }

    public static Length<IWidget> wh(int n) {
        Preconditions.checkArgument(n <= 100);
        return new Length<IWidget>() {
            private float actualLength;

            @Override
            public void resolve(IWidget target) {
                actualLength = (float) target.getWindow().getHeight() / 100 * n;
                this.resolved = true;
            }

            @Override
            public float get() {
                return actualLength;
            }
        };
    }

    protected boolean resolved = false;

    public abstract void resolve(T target);

    public abstract float get();

    public int getInt() {
        return (int) get();
    }
}
