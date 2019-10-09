package powerlessri.harmonics.gui.window;

import com.google.common.collect.ImmutableList;
import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.panel.Panel;
import powerlessri.harmonics.gui.widget.navigation.NavigationBar;

import java.util.List;

public abstract class AbstractDockableWindow<T extends IWidget> extends AbstractWindow implements IPopupWindow {

    private final NavigationBar navigationBar;
    private final Panel<T> contentBox;
    private final List<IWidget> children;

    private boolean alive = true;
    private int order;

    public AbstractDockableWindow(int width, int height) {
        this.setContents(width, height);
        this.navigationBar = NavigationBar.standard(this);
        this.navigationBar.attachWindow(this);
        this.contentBox = new Panel<>();
        this.contentBox.attachWindow(this);
        this.children = ImmutableList.of(navigationBar, contentBox);

        contentBox.setY(navigationBar.getYBottom());
        onResize();
        populateContentBox();
    }

    protected abstract void populateContentBox();

    @Override
    protected void onResize() {
        navigationBar.expandHorizontally();
        contentBox.expandHorizontally();
        contentBox.setHeight(getContentHeight() - navigationBar.getHeight());
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        renderVanillaStyleBackground();
        renderChildren(mouseX, mouseY, particleTicks);
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isInside(mouseX, mouseY)) {
            WidgetScreen.assertActive().raiseWindowToTop(this);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public List<? extends IWidget> getChildren() {
        return children;
    }

    @Override
    public int getBorderSize() {
        return 4;
    }

    @Override
    public float getZLevel() {
        return Render2D.POPUP_WINDOW_Z;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean shouldDiscard() {
        return !alive;
    }

    public void discard() {
        alive = false;
    }
}
