package powerlessri.harmonics.gui.layout.properties;

import com.google.common.base.Preconditions;
import powerlessri.harmonics.gui.widget.IWidget;

public abstract class Length<W> {

    public static Length<?> px(int pixels) {
        return new Length<Object>() {
            @Override
            public void resolve(Object target) {
                this.resolved = true;
            }

            @Override
            public int getPixels() {
                Preconditions.checkState(resolved);
                return pixels;
            }
        };
    }

    public static <W extends IFractionalLengthHandler> Length<W> fr(int numerator) {
        return new Length<W>() {
            private int actualLength;

            @Override
            public void resolve(W target) {
                actualLength = (int) ((float) numerator / target.getDenominator());
                this.resolved = true;
            }

            @Override
            public int getPixels() {
                return actualLength;
            }
        };
    }

    public static <W extends IFractionalLengthHandler> Length<W> auto() {
        return fr(1);
    }

    public static Length<IWidget> ww(int n) {
        Preconditions.checkArgument(n <= 100);
        return new Length<IWidget>() {
            private int actualLength;

            @Override
            public void resolve(IWidget target) {
                actualLength = (int) ((float) target.getWindow().getWidth() / 100 * n);
                this.resolved = true;
            }

            @Override
            public int getPixels() {
                return actualLength;
            }
        };
    }

    public static Length<IWidget> wh(int n) {
        Preconditions.checkArgument(n <= 100);
        return new Length<IWidget>() {
            private int actualLength;

            @Override
            public void resolve(IWidget target) {
                actualLength = (int) ((float) target.getWindow().getHeight() / 100 * n);
                this.resolved = true;
            }

            @Override
            public int getPixels() {
                return actualLength;
            }
        };
    }

    protected boolean resolved = false;

    public abstract void resolve(W target);

    public abstract int getPixels();
}
