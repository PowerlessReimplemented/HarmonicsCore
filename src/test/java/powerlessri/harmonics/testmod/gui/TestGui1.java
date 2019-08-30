package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.util.text.StringTextComponent;
import powerlessri.harmonics.gui.BackgroundRenderers;
import powerlessri.harmonics.gui.IWidget;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.window.AbstractWindow;

import java.util.List;

public class TestGui1 extends WidgetScreen {

    public TestGui1() {
        super(new StringTextComponent("Test"));
    }

    @Override
    protected void init() {
        super.init();
        initializePrimaryWindow(new Window());
    }

    public static class Window extends AbstractWindow {

        @Override
        public int getBorderSize() {
            return 0;
        }

        @Override
        public List<? extends IWidget> getChildren() {
            return ImmutableList.of();
        }

        @Override
        public void render(int mouseX, int mouseY, float particleTicks) {
            GlStateManager.disableAlphaTest();
            BackgroundRenderers.drawFlatStyle(0, 0, 400, 300, 0F);
        }
    }
}
