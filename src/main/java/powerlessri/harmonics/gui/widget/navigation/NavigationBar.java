package powerlessri.harmonics.gui.widget.navigation;

import com.mojang.blaze3d.platform.GlStateManager;
import powerlessri.harmonics.gui.*;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.layout.FlowLayout;
import powerlessri.harmonics.gui.layout.properties.BoxSizing;
import powerlessri.harmonics.gui.widget.*;
import powerlessri.harmonics.gui.widget.button.SimpleIconButton;
import powerlessri.harmonics.gui.window.AbstractDockableWindow;
import powerlessri.harmonics.utils.Utils;

import java.util.*;
import java.util.function.Consumer;

import static powerlessri.harmonics.gui.Render2D.*;

public class NavigationBar extends AbstractContainer<IWidget> {

    public static final ITexture CLOSE = Texture.portion(Render2D.COMPONENTS, 256, 256, 0, 28, 9, 9);
    public static final ITexture MAXIMIZE = CLOSE.moveRight(1);
    public static final ITexture MINIMIZE = CLOSE.moveRight(2);
    public static final ITexture CLOSE_HOVERED = CLOSE.moveDown(1);
    public static final ITexture MAXIMIZE_HOVERED = MAXIMIZE.moveDown(1);
    public static final ITexture MINIMIZE_HOVERED = MINIMIZE.moveDown(1);

    /**
     * Construct and return a standard navigation bar with three buttons: close window, maximize window, and minimize window. This will also
     * attach the navigation bar to the window.
     */
    public static <T extends IWidget> NavigationBar standard(AbstractDockableWindow<T> window) {
        NavigationBar bar = new NavigationBar(10);
        bar.attachWindow(window);

        SimpleIconButton close = new SimpleIconButton(CLOSE, CLOSE_HOVERED);
        close.setClickAction(b -> window.discard());
        bar.addChildren(close);

        SimpleIconButton maximize = new SimpleIconButton(MAXIMIZE, MAXIMIZE_HOVERED);
        maximize.setClickAction(b -> window.maximize());
        bar.addChildren(maximize);

        SimpleIconButton minimize = new SimpleIconButton(MINIMIZE, MINIMIZE_HOVERED);
        minimize.setClickAction(b -> window.minimize());
        bar.addChildren(minimize);

        bar.icon.setTexture(window.getIcon());
        bar.icon.setHeight(Utils.lowerBound(bar.icon.getHeight(), 8));
        bar.title.text(window.getTitle());

        bar.render = b -> {
            GlStateManager.disableTexture();
            beginColoredQuad();
            int x1 = b.getOuterAbsoluteX() - 1;
            int x2 = b.getOuterAbsoluteXRight() + 1;
            int innerY = b.getAbsoluteYBottom();
            int outerY = b.getOuterAbsoluteYBottom();
            float z = b.getZLevel();
            coloredRect(x1, innerY, x2, outerY, z, 0xff797979);
            coloredRect(x1, innerY, x2, outerY - b.getBorderBottom() / 2, z, 0xffffffff);
            draw();
            GlStateManager.enableTexture();
        };

        bar.reflow();
        return bar;
    }

    private final List<IWidget> children = new ArrayList<>();
    private Icon icon;
    private Label title;

    public Consumer<NavigationBar> render = n -> {};

    private int initialDragLocalX = -1;
    private int initialDragLocalY = -1;

    public NavigationBar(int height) {
        this.setHeight(height);
        this.setBorderBottom(2);
    }

    @Override
    public void onInitialAttach() {
        icon = new Icon(Texture.NONE) {
            @Override
            public BoxSizing getBoxSizing() {
                return BoxSizing.PHANTOM;
            }
        };
        icon.setDimensions(0, 8);
        addChildrenInternal(icon);

        title = new Label(icon);
        title.setBoxSizing(BoxSizing.PHANTOM);
        addChildrenInternal(title);
        adjustMinHeight();
    }

    @Override
    public Collection<IWidget> getPanels() {
        return children;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isInside(mouseX, mouseY)) {
            initialDragLocalX = (int) mouseX - getWindow().getX();
            initialDragLocalY = (int) mouseY - getWindow().getY();
            getWindow().setFocusedWidget(this);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }
        if (isDragging()) {
            int x = (int) mouseX - initialDragLocalX;
            int y = (int) mouseY - initialDragLocalY;
            getWindow().setPosition(x, y);
            return true;
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        initialDragLocalX = -1;
        initialDragLocalY = -1;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean isDragging() {
        return initialDragLocalX != -1 && initialDragLocalY != -1;
    }

    private void addChildrenInternal(IWidget widget) {
        children.add(widget);
        widget.attach(this);
    }

    @Override
    public NavigationBar addChildren(IWidget widget) {
        addChildrenInternal(widget);
        adjustMinHeight();
        return this;
    }

    @Override
    public NavigationBar addChildren(Collection<IWidget> widgets) {
        children.addAll(widgets);
        for (IWidget widget : widgets) {
            widget.attach(this);
        }
        adjustMinHeight();
        return this;
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        renderChildren(mouseX, mouseY, particleTicks);
        render.accept(this);
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    public Icon getIcon() {
        return icon;
    }

    public Label getTitle() {
        return title;
    }

    @Override
    public void onDimensionChanged() {
        reflow();
    }

    @Override
    public void reflow() {
        // Do not change widget size, since size change will cause reflow
        FlowLayout.reverseHorizontal(children, getXRight(), 0, 2);
        if (isValid()) {
            icon.setLocation(0, 0);
            title.updatePosition();
        }
    }

    @Override
    public void provideInformation(ITextReceiver receiver) {
        super.provideInformation(receiver);
        receiver.line("DragLocalX=" + initialDragLocalX);
        receiver.line("DragLocalY=" + initialDragLocalY);
    }
}
