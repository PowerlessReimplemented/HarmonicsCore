package powerlessri.harmonics.testmod.gui;

import net.minecraft.text.LiteralText;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.*;
import powerlessri.harmonics.gui.window.AbstractWindow;

import java.util.ArrayList;
import java.util.List;

public class RadioButtonTest extends WidgetScreen {

    public RadioButtonTest() {
        super(new LiteralText("Test"));
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
                RadioInput radioInput = new RadioInput(controller);
                radioInput.attachWindow(this);
                radioInput.setLocation(0, y);
                Label label = radioInput.textLabel("Radio button " + (i + 1));

                children.add(radioInput);
                children.add(label);

                y += radioInput.getFullHeight() + 2;
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
        public void render(int mouseX, int mouseY, float tickDelta) {
            RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
            renderVanillaStyleBackground();
            renderChildren(mouseX, mouseY, tickDelta);
            RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
        }
    }
}
