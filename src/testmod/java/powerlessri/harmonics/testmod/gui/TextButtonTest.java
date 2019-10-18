package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.text.StringTextComponent;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.layout.FlowLayout;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.AbstractWidget;
import powerlessri.harmonics.gui.widget.button.ColoredTextButton;
import powerlessri.harmonics.gui.widget.button.TextButton;
import powerlessri.harmonics.gui.window.AbstractWindow;

import java.util.List;

public class TextButtonTest extends WidgetScreen {

    public TextButtonTest() {
        super(new StringTextComponent("Test"));
    }

    @Override
    protected void init() {
        super.init();
        setPrimaryWindow(new Window());
    }

    public static class Window extends AbstractWindow {

        private final List<AbstractWidget> children;

        public Window() {
            setContents(160, 160);
            centralize();

            TextButton btn1 = new TextButton("test1");
            TextButton btn2 = new TextButton("test2");
            TextButton btn3 = new TextButton("Long name button");
            TextButton btn4 = new TextButton(".");
            ColoredTextButton cbtn1 = ColoredTextButton.ofText("colored");
            ColoredTextButton cbtn2 = ColoredTextButton.ofText(".");
            ColoredTextButton cbtn3 = ColoredTextButton.ofText("COLORED 2");
            ColoredTextButton cbtn4 = ColoredTextButton.ofText("Longer name for colored");
            children = ImmutableList.of(btn1, btn2, btn3, btn4, cbtn1, cbtn2, cbtn3, cbtn4);
            for (AbstractWidget child : children) {
                child.attachWindow(this);
            }
            FlowLayout.vertical(children, 0, 0, 0);
        }

        @Override
        public int getBorderSize() {
            return 4;
        }

        @Override
        public List<AbstractWidget> getChildren() {
            return children;
        }

        @Override
        public void render(int mouseX, int mouseY, float particleTicks) {
            RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
            renderVanillaStyleBackground();
            renderChildren(mouseX, mouseY, particleTicks);
            RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
        }
    }
}
