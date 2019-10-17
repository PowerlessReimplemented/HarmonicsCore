package powerlessri.harmonics.gui.window;

import com.google.common.collect.ImmutableList;
import powerlessri.harmonics.gui.*;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.layout.FlowLayout;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.navigation.DockedWindow;
import powerlessri.harmonics.gui.widget.navigation.NavigationBar;
import powerlessri.harmonics.gui.widget.panel.Panel;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractDockableWindow<T extends IWidget> extends AbstractWindow implements IPopupWindow {

    private final DockingBar dockingBar;

    private final NavigationBar navigationBar;
    private final Panel<T> contentBox;
    private final List<IWidget> children;

    private boolean alive = true;
    private int order;

    public AbstractDockableWindow(@Nullable DockingBar dockingBar, int width, int height) {
        this.dockingBar = dockingBar;
        this.navigationBar = NavigationBar.standard(this);
        this.contentBox = new Panel<>();
        this.contentBox.attachWindow(this);
        this.children = ImmutableList.of(navigationBar, contentBox);

        setContents(width, height);
        FlowLayout.vertical(children, 0, 0, 0);
        populateContentBox();
        onInitialized();
    }

    protected void onInitialized() {
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

    public void maximize() {
        // No support by default
    }

    public void minimize() {
        DockedWindow item = new DockedWindow(this);
        dockingBar.addDockedWindow(item);
        discard();
    }

    public void restore() {
        alive = true;
        WidgetScreen.assertActive().addPopupWindow(this);
    }

    public ITexture getIcon() {
        return Texture.NONE;
    }

    public String getTitle() {
        return "test";
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    public Panel<T> getContentBox() {
        return contentBox;
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

    @Override
    public void provideInformation(ITextReceiver receiver) {
        super.provideInformation(receiver);
        receiver.line("Order=" + order);
    }
}
