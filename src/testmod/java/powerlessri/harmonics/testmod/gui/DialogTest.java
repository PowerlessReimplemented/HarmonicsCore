package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import powerlessri.harmonics.gui.BackgroundRenderers;
import powerlessri.harmonics.gui.IWidget;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.button.SimpleTextButton;
import powerlessri.harmonics.gui.window.AbstractWindow;
import powerlessri.harmonics.gui.window.Dialog;

import java.util.List;

public class DialogTest extends WidgetScreen {

    public DialogTest() {
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
            setContents(100, 80);
            centralize();

            SimpleTextButton dialog =
                    SimpleTextButton.ofText("Dialog", b -> Dialog.createPrompt("Enter some text:",
                            (btn, t) -> Minecraft.getInstance().player.sendChatMessage("You have entered: " + t),
                            (btn, t) -> Minecraft.getInstance().player.sendChatMessage("You clicked cancel")).tryAddSelfToActiveGUI());
            dialog.setWindow(this);

            this.children = ImmutableList.of(dialog);
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
            BackgroundRenderers.drawVanillaStyle(getX(), getY(), getWidth(), getHeight(), 0F);
            renderChildren(mouseX, mouseY, particleTicks);
            RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
        }
    }
}
