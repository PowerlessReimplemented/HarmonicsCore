package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.util.text.StringTextComponent;
import powerlessri.harmonics.gui.screen.BackgroundRenderers;
import powerlessri.harmonics.gui.IWidget;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.button.SimpleTextButton;
import powerlessri.harmonics.gui.window.AbstractWindow;

import java.util.List;

public class BackgroundTest extends WidgetScreen {

    public BackgroundTest() {
        super(new StringTextComponent("Test"));
    }

    @Override
    protected void init() {
        super.init();
        setPrimaryWindow(new Window());
    }

    public static class Window extends AbstractWindow {

        public static final int VANILLA = 0;
        public static final int FLAT = 1;
        public static final int STYLE_MAX = 1;

        private final List<IWidget> children;
        private int style = 0;

        public Window() {
            setContents(120, 80);
            centralize();
            SimpleTextButton switchBkg = SimpleTextButton.ofText("Switch Background", b -> {
                int next = style + 1;
                style = next > STYLE_MAX ? 0 : next;
                centralize();
                updatePosition();
            });
            switchBkg.attachWindow(this);
            this.children = ImmutableList.of(switchBkg);
        }

        @Override
        public int getBorderSize() {
            switch (style) {
                case VANILLA: return 4;
                case FLAT: return 2;
                default: return 0;
            }
        }

        @Override
        public List<? extends IWidget> getChildren() {
            return children;
        }

        @Override
        public void render(int mouseX, int mouseY, float particleTicks) {
            RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
            switch (style) {
                case VANILLA:
                    BackgroundRenderers.drawVanillaStyle(getX(), getY(), getWidth(), getHeight(), 0F);
                    break;
                case FLAT:
                    GlStateManager.disableAlphaTest();
                    BackgroundRenderers.drawFlatStyle(getX(), getY(), getWidth(), getHeight(), 0F);
                    break;
            }
            renderChildren(mouseX, mouseY, particleTicks);
            RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
        }
    }
}
