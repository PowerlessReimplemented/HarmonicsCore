package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.text.LiteralText;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.layout.FlowLayout;
import powerlessri.harmonics.gui.layout.properties.BoxSizing;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.*;
import powerlessri.harmonics.gui.window.AbstractWindow;

import java.util.List;

public class SliderTest extends WidgetScreen {

    public SliderTest() {
        super(new LiteralText("test"));
    }

    @Override
    protected void init() {
        super.init();
        setPrimaryWindow(new Window());
    }

    public static class Window extends AbstractWindow {

        private final List<IWidget> children;

        public Window() {
            setContents(120, 80);
            centralize();

            Slider hslider = new Slider(60, 12);
            hslider.setHorizontal();
            hslider.attachWindow(this);

            Label hlabel = new Label(hslider);
            hlabel.setBoxSizing(BoxSizing.PHANTOM);
            hslider.onValueChanged = i -> hlabel.text(String.valueOf(i));

            Slider vslider = new Slider(10, 60);
            vslider.setVertical();
            vslider.attachWindow(this);

            Label vlabel = new Label(vslider);
            vlabel.setBoxSizing(BoxSizing.PHANTOM);
            vslider.onValueChanged = i -> vlabel.text(String.valueOf(i));
            children = ImmutableList.of(hslider, hlabel, vslider, vlabel);

            FlowLayout.vertical(children, 0, 0, 4);
            hlabel.text("none");
            vlabel.text("none");
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
