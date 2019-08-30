package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import powerlessri.harmonics.HarmonicsCore;
import powerlessri.harmonics.gui.BackgroundRenderers;
import powerlessri.harmonics.gui.IWidget;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.button.TextButton;
import powerlessri.harmonics.gui.window.AbstractWindow;
import powerlessri.harmonics.gui.window.Dialog;

import java.util.List;

public class TestGui1 extends WidgetScreen {

    public TestGui1() {
        super(new StringTextComponent("Test"));
    }

    @Override
    protected void init() {
        super.init();
        setPrimaryWindow(new Window());
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        try {
            super.render(mouseX, mouseY, particleTicks);
        } catch (Throwable throwable) {
            HarmonicsCore.logger.error(throwable);
        }
    }

    public static class Window extends AbstractWindow {

        private final List<IWidget> children;

        public Window() {
            setContents(240, 180);
            centralize();

            TextButton button = TextButton.ofText("Dialog", b -> {
                Dialog.createPrompt("Enter some text:", (btn, t) -> {
                    Minecraft.getInstance().player.sendChatMessage("You have entered: " + t);
                }, (btn, t) -> {
                    Minecraft.getInstance().player.sendChatMessage("You clicked cancel");
                }).tryAddSelfToActiveGUI();
            });
            button.setWindow(this);
            this.children = ImmutableList.of(button);

            updatePosition();
        }

        @Override
        public int getBorderSize() {
            return 0;
        }

        @Override
        public List<? extends IWidget> getChildren() {
            return children;
        }

        @Override
        public void render(int mouseX, int mouseY, float particleTicks) {
            RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
            GlStateManager.disableAlphaTest();
            BackgroundRenderers.drawFlatStyle(getContentX(), getContentY(), getWidth(), getHeight(), 0F);
            renderChildren(mouseX, mouseY, particleTicks);
            RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
        }
    }
}
