package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.text.StringTextComponent;
import powerlessri.harmonics.gui.BackgroundRenderers;
import powerlessri.harmonics.gui.IWidget;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.RadioButton;
import powerlessri.harmonics.gui.widget.RadioController;
import powerlessri.harmonics.gui.window.AbstractWindow;

import java.util.List;

public class RadioButtonTest extends WidgetScreen {

    public RadioButtonTest() {
        super(new StringTextComponent("Test"));
    }

    @Override
    protected void init() {
        super.init();
        setPrimaryWindow(new Window());
    }

    public static class Window extends AbstractWindow {

        private final List<IWidget> children;

        private RadioController controller;

        public Window() {
            setContents(120, 80);
            centralize();

            controller = new RadioController();
            RadioButton btn1 = new RadioButton(controller);
            RadioButton btn2 = new RadioButton(controller);
            RadioButton btn3 = new RadioButton(controller);
            btn1.setWindow(this);
            btn2.setWindow(this);
            btn3.setWindow(this);

            btn1.setLocation(0, 0);
            btn2.setLocation(0, btn1.getYBottom() + 2);
            btn3.setLocation(0, btn2.getYBottom() + 2);

            children = ImmutableList.of(btn1, btn2, btn3);
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
        public void render(int mouseX, int mouseY, float particleTicks) {
            RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
            BackgroundRenderers.drawVanillaStyle(getX(), getY(), getWidth(), getHeight(), 0F);
            renderChildren(mouseX, mouseY, particleTicks);
            RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
        }
    }
}
