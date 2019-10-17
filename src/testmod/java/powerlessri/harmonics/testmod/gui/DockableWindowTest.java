package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import powerlessri.harmonics.gui.ITexture;
import powerlessri.harmonics.gui.Texture;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.layout.FlowLayout;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.button.ColoredTextButton;
import powerlessri.harmonics.gui.widget.button.GradientTextButton;
import powerlessri.harmonics.gui.widget.navigation.NavigationBar;
import powerlessri.harmonics.gui.window.*;

import java.util.List;

import static powerlessri.harmonics.gui.Render2D.scaledWidth;

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

            DockingBar dockingBar = new DockingBar((int) (scaledWidth() * (3F / 5F)), 20);
            dockingBar.moveToHorizontalCenter();
            dockingBar.moveToBottom();
            WidgetScreen.assertActive().addWindow(dockingBar);

            ColoredTextButton createWindow = ColoredTextButton.ofText("Window", b -> {
                Window window = new Window(dockingBar);
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

        public Window(DockingBar dockingBar) {
            super(dockingBar, 200, 160);
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

        @Override
        protected void onInitialized() {
            NavigationBar navBar = getNavigationBar();
            navBar.getIcon().setDimensions(8, 8);
            navBar.reflow();
        }

        @Override
        public ITexture getIcon() {
            return Texture.portion(new ResourceLocation("minecraft:textures/item/bucket.png"), 16, 16, 0, 0, 16, 16);
        }
    }
}
