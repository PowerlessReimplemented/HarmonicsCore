package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.text.LiteralText;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.layout.FlowLayout;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.TextField;
import powerlessri.harmonics.gui.widget.panel.HorizontalList;
import powerlessri.harmonics.gui.widget.panel.VerticalList;
import powerlessri.harmonics.gui.window.AbstractWindow;

import java.util.List;

public class ScrollingListTest extends WidgetScreen {

    public ScrollingListTest() {
        super(new LiteralText("test"));
    }

    @Override
    protected void init() {
        super.init();
        setPrimaryWindow(new Window());
    }

    public static class Window extends AbstractWindow {

        private List<IWidget> children;

        public Window() {
            setContents(100, 80);
            centralize();

            HorizontalList<TextField> hlist = new HorizontalList<>(getContentWidth(), 20);
            hlist.attachWindow(this);
            for (int i = 0; i < 40; i++) {
                TextField widget = new TextField();
                widget.setDimensions(40, 12);
                widget.setText("i=" + i);
                hlist.addChildren(widget);
            }
            hlist.reflow();

            VerticalList<TextField> vlist = new VerticalList<>(getContentWidth(), getContentHeight() - 20 - 2);
            vlist.attachWindow(this);
            for (int i = 0; i < 40; i++) {
                TextField widget = new TextField();
                widget.setDimensions(40, 12);
                widget.setText("i=" + i);
                vlist.addChildren(widget);
            }
            vlist.reflow();

            children = ImmutableList.of(hlist, vlist);
            FlowLayout.vertical(children, 0, 0, 2);
        }

        @Override
        public int getBorderSize() {
            return 4;
        }

        @Override
        public List<? extends IWidget> getChildren() {
            return children;
        }

        @Override
        public void render(int mouseX, int mouseY, float tickDelta) {
            RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
            renderVanillaStyleBackground();
            renderChildren(mouseX, mouseY, tickDelta);
            RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
        }
    }
}
