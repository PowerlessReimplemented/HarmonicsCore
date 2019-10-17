package powerlessri.harmonics.gui.widget.navigation;

import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.AbstractWidget;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;
import powerlessri.harmonics.gui.widget.panel.HorizontalList;
import powerlessri.harmonics.gui.window.AbstractDockableWindow;
import powerlessri.harmonics.gui.window.DockingBar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Objects;

import static powerlessri.harmonics.gui.Render2D.fontRenderer;

public class DockedWindow extends AbstractWidget implements LeafWidgetMixin {

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
        return 0xff000000;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
        this.setWidth(getSideMargin() + fontRenderer().getStringWidth(name) + getSideMargin());
    }

    public void restore() {
        window.restore();
        // TODO defer to not create CME
        getParent().removeChildren(this);
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
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
