package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.text.StringTextComponent;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.layout.FlowLayout;
import powerlessri.harmonics.gui.screen.BackgroundRenderers;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.button.GradientTextButton;
import powerlessri.harmonics.gui.window.AbstractWindow;

import java.util.List;

public class GradientButtonTest extends WidgetScreen {

    public GradientButtonTest() {
        super(new StringTextComponent("Test"));
    }

    @Override
    protected void init() {
        super.init();
        setPrimaryWindow(new Window());
    }

    public static class Window extends AbstractWindow {

        private final List<GradientTextButton> children;

        public Window() {
            setContents(120, 80);
            centralize();

            GradientTextButton btn1 = new GradientTextButton("test1");
            GradientTextButton btn2 = new GradientTextButton("test2");
            GradientTextButton btn3 = new GradientTextButton("Long name button");
            GradientTextButton btn4 = new GradientTextButton(".");
            children = ImmutableList.of(btn1, btn2, btn3, btn4);
            for (GradientTextButton child : children) {
                child.attachWindow(this);
            }
            FlowLayout.reflow(children);
        }

        @Override
        public int getBorderSize() {
            return 4;
        }

        @Override
        public List<GradientTextButton> getChildren() {
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
