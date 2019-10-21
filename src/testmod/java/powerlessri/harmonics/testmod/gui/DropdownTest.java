package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.text.LiteralText;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.*;
import powerlessri.harmonics.gui.widget.panel.VerticalList;
import powerlessri.harmonics.gui.window.AbstractWindow;

import java.util.List;

public class DropdownTest extends WidgetScreen {

    public DropdownTest() {
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
            setContents(100, 80);
            centralize();

            Dropdown<IWidget, Paragraph, VerticalList<TextField>> dropdown = Dropdown.textAndList(60, 20, 100);
            dropdown.attachWindow(this);
            dropdown.getLabel().addLineSplit("Example dropdown");
            VerticalList<TextField> list = dropdown.getPanel();
            for (int i = 0; i < 16; i++) {
                TextField textField = new TextField(list.getWidth() - list.getBarWidth(), 14);
                textField.setText("i=" + i);
                list.addChildren(textField);
            }
            list.reflow();

            children = ImmutableList.of(dropdown);
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
