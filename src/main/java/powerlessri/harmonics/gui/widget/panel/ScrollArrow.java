package powerlessri.harmonics.gui.widget.panel;

import powerlessri.harmonics.gui.*;
import powerlessri.harmonics.gui.widget.button.AbstractIconButton;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class ScrollArrow extends AbstractIconButton implements LeafWidgetMixin {

    private static final ITexture UP_NORMAL = Texture.portion256x256(Render2D.COMPONENTS, 0, 0, 10, 6);
    private static final ITexture UP_HOVERED = UP_NORMAL.moveRight(1);
    private static final ITexture UP_CLICKED = UP_NORMAL.moveRight(2);
    private static final ITexture UP_DISABLED = UP_NORMAL.moveRight(3);

    public static ScrollArrow up(int x, int y) {
        return new ScrollArrow(x, y) {
            @Override
            public ITexture getTextureNormal() {
                return UP_NORMAL;
            }

            @Override
            public ITexture getTextureHovered() {
                return UP_HOVERED;
            }

            @Override
            public ITexture getTextureClicked() {
                return UP_CLICKED;
            }

            @Override
            public ITexture getTextureDisabled() {
                return UP_DISABLED;
            }

            @Override
            protected int getScrollDirectionMask() {
                return -1;
            }
        };
    }

    private static final ITexture DOWN_NORMAL = UP_NORMAL.moveDown(1);
    private static final ITexture DOWN_HOVERED = UP_HOVERED.moveDown(1);
    private static final ITexture DOWN_CLICKED = UP_CLICKED.moveDown(1);
    private static final ITexture DOWN_DISABLED = UP_DISABLED.moveDown(1);

    public static ScrollArrow down(int x, int y) {
        return new ScrollArrow(x, y) {
            @Override
            public ITexture getTextureNormal() {
                return DOWN_NORMAL;
            }

            @Override
            public ITexture getTextureHovered() {
                return DOWN_HOVERED;
            }

            @Override
            public ITexture getTextureClicked() {
                return DOWN_CLICKED;
            }

            @Override
            public ITexture getTextureDisabled() {
                return DOWN_DISABLED;
            }

            @Override
            protected int getScrollDirectionMask() {
                return 1;
            }
        };
    }

    public ScrollArrow(int x, int y) {
        this.setLocation(x, y);
        this.setDimensions(10, 6);
    }

    @Override
    public void update(float particleTicks) {
        if (isClicked()) {
            WrappingList parent = getParent();
            parent.scroll(parent.getScrollSpeed() * getScrollDirectionMask());
        }
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        if (!isEnabled()) {
            return false;
        }
        return super.onMouseClicked(mouseX, mouseY, button);
    }

    @Nonnull
    @Override
    public WrappingList getParent() {
        return Objects.requireNonNull((WrappingList) super.getParent());
    }

    protected abstract int getScrollDirectionMask();
}
