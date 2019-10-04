package powerlessri.harmonics.testmod.gui;

import net.minecraft.util.text.StringTextComponent;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.Label;
import powerlessri.harmonics.gui.widget.button.RadioButton;
import powerlessri.harmonics.gui.widget.button.RadioController;
import powerlessri.harmonics.gui.window.AbstractWindow;

import java.util.ArrayList;
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

        public Window() {
            setContents(120, 80);
            centralize();

            RadioController controller = new RadioController();
            children = new ArrayList<>();
            int y = 0;
            for (int i = 0; i < 6; i++) {
                RadioButton radioButton = new RadioButton(controller);
                radioButton.attachWindow(this);
                radioButton.setLocation(0, y);
                Label label = radioButton.textLabel("Radio button " + (i + 1));

                children.add(radioButton);
                children.add(label);

                y += radioButton.getFullHeight() + 2;
            }
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
            drawVanillaStyleBackground();
            renderChildren(mouseX, mouseY, particleTicks);
            RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
        }
    }
}
