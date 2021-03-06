package powerlessri.harmonics.gui.widget.navigation;

import com.mojang.blaze3d.platform.GlStateManager;
import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.AbstractWidget;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;
import powerlessri.harmonics.gui.widget.panel.HorizontalList;
import powerlessri.harmonics.gui.window.AbstractDockableWindow;
import powerlessri.harmonics.gui.window.DockingBar;

import javax.annotation.Nonnull;
import java.util.Objects;

import static powerlessri.harmonics.gui.Render2D.*;

public class DockedWindow extends AbstractWidget implements LeafWidgetMixin {

    public static final int TOP_LEFT_COLOR = 0xff2b2b2b;
    public static final int BOTTOM_RIGHT_COLOR = 0xffffffff;
    public static final int FILL_COLOR = 0xff5c5f61;

    private final AbstractDockableWindow<?> window;
    private String name = "";

    public DockedWindow(AbstractDockableWindow<?> window) {
        this.setDimensions(getSideMargin() * 2, 20);
        this.window = window;
        this.setName(window.getTitle());
    }

    public int getSideMargin() {
        return 4;
    }

    public int getTextColor() {
        return 0xffffffff;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
        this.setWidth(getSideMargin() + fontRenderer().getStringWidth(name) + getSideMargin());
    }

    public void restore() {
        WidgetScreen.assertActive().defer(() -> {
            window.restore();
            HorizontalList<DockedWindow> list = getParent();
            list.removeChildren(this);
            list.reflow();
        });
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        GlStateManager.disableTexture();
        beginColoredQuad();
        thickBeveledBox(getAbsoluteX(), getAbsoluteY(), getAbsoluteXRight(), getAbsoluteYBottom(), getZLevel(), 1, TOP_LEFT_COLOR, BOTTOM_RIGHT_COLOR, FILL_COLOR);
        draw();
        GlStateManager.enableTexture();
        Render2D.renderCenteredText(name, getAbsoluteY(), getAbsoluteYBottom(), getAbsoluteX(), getAbsoluteXRight(), getZLevel(), getTextColor());
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        restore();
        return true;
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public HorizontalList<DockedWindow> getParent() {
        return (HorizontalList<DockedWindow>) Objects.requireNonNull(super.getParent());
    }

    @Override
    public DockingBar getWindow() {
        return (DockingBar) super.getWindow();
    }
}
