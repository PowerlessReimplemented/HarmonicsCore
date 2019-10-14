package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.layout.FlowLayout;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.*;
import powerlessri.harmonics.gui.window.AbstractWindow;

import java.util.List;

public class Switch8CheckboxTest extends WidgetScreen {

    public Switch8CheckboxTest() {
        super(new StringTextComponent("test"));
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

            Checkbox checkbox = new Checkbox();
            checkbox.attachWindow(this);
            checkbox.onStateChange = b -> Minecraft.getInstance().player.sendChatMessage("Clicked checkbox: " + b);
            Switch switchButton = new Switch();
            switchButton.attachWindow(this);
            switchButton.onStateChange = b -> Minecraft.getInstance().player.sendChatMessage("Clicked switch: " + b);
            children = ImmutableList.of(checkbox, switchButton);
            FlowLayout.vertical(children, 0, 0, 4);
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
}
