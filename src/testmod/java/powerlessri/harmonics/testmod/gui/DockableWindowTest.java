package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.text.StringTextComponent;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.layout.FlowLayout;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.button.ColoredTextButton;
import powerlessri.harmonics.gui.widget.button.GradientTextButton;
import powerlessri.harmonics.gui.window.AbstractDockableWindow;
import powerlessri.harmonics.gui.window.AbstractWindow;

import java.util.List;

public class DockableWindowTest extends WidgetScreen {

    public DockableWindowTest() {
        super(new StringTextComponent("test"));
    }

    @Override
    protected void init() {
        super.init();
        setPrimaryWindow(new PrimaryWindow());
    }

    public static class PrimaryWindow extends AbstractWindow {

        private final List<IWidget> children;

        public PrimaryWindow() {
            setContents(100, 80);
            centralize();

            ColoredTextButton createWindow = ColoredTextButton.ofText("Window", b -> {
                Window window = new Window();
                WidgetScreen.assertActive().addPopupWindow(window);
            });
            createWindow.attachWindow(this);

            this.children = ImmutableList.of(createWindow);
            this.updatePosition();
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
            renderVanillaStyleBackground();
            renderChildren(mouseX, mouseY, particleTicks);
            RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
        }
    }

    public static class Window extends AbstractDockableWindow<IWidget> {

        public Window() {
            super(200, 160);
        }

        @Override
        protected void populateContentBox() {
            GradientTextButton btn1 = new GradientTextButton("test1");
            GradientTextButton btn2 = new GradientTextButton("test2");
            GradientTextButton btn3 = new GradientTextButton("Long name button");
            GradientTextButton btn4 = new GradientTextButton(".");
            ColoredTextButton cbtn1 = ColoredTextButton.ofText("colored");
            ColoredTextButton cbtn2 = ColoredTextButton.ofText(".");
            ColoredTextButton cbtn3 = ColoredTextButton.ofText("COLORED 2");
            ColoredTextButton cbtn4 = ColoredTextButton.ofText("Longer name for colored");

            getContentBox()
                    .addChildren(btn1)
                    .addChildren(btn2)
                    .addChildren(btn3)
                    .addChildren(btn4)
                    .addChildren(cbtn1)
                    .addChildren(cbtn2)
                    .addChildren(cbtn4)
                    .addChildren(cbtn3);
            getContentBox().setLayout(w -> FlowLayout.vertical(w, 2, 2, 2));
            getContentBox().reflow();
        }
    }
}
