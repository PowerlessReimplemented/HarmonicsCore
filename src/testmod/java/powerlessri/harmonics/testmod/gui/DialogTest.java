package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.button.ColoredTextButton;
import powerlessri.harmonics.gui.window.AbstractWindow;
import powerlessri.harmonics.gui.window.Dialog;

import java.util.List;

public class DialogTest extends WidgetScreen {

    public DialogTest() {
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
            setContents(100, 80);
            centralize();

            ColoredTextButton dialog =
                    ColoredTextButton.ofText("Dialog", b -> Dialog.createPrompt("Enter some text:",
                            (btn, t) -> MinecraftClient.getInstance().player.sendChatMessage("You have entered: " + t),
                            (btn, t) -> MinecraftClient.getInstance().player.sendChatMessage("You clicked cancel")).tryAddSelfToActiveGUI());
            dialog.attachWindow(this);

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
        public void render(int mouseX, int mouseY, float tickDelta) {
            RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
            renderVanillaStyleBackground();
            renderChildren(mouseX, mouseY, tickDelta);
            RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
        }
    }
}
