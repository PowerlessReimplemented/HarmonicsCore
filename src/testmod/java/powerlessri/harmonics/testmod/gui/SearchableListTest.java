package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.apache.commons.lang3.tuple.Pair;
import powerlessri.harmonics.gui.*;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.BackgroundRenderers;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.INamedElement;
import powerlessri.harmonics.gui.widget.TextField;
import powerlessri.harmonics.gui.widget.box.FilteredList;
import powerlessri.harmonics.gui.widget.box.WrappingList;
import powerlessri.harmonics.gui.widget.button.AbstractIconButton;
import powerlessri.harmonics.gui.window.AbstractWindow;
import powerlessri.harmonics.testmod.HarmonicsCoreTest;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class SearchableListTest extends WidgetScreen {

    public SearchableListTest() {
        super(new StringTextComponent("Test"));
    }

    @Override
    protected void init() {
        super.init();
        setPrimaryWindow(new Window());
    }

    public static class Window extends AbstractWindow {

        private List<IWidget> children;

        public Window() {
            setContents(100, 80);
            centralize();

            ImmutableList.Builder<IconBtn> buttons = ImmutableList.builder();
            for (int i = 0; i < 40; i++) {
                buttons.add(new IconBtn("Test" + i));
            }
            Pair<WrappingList, TextField> pair = FilteredList.createSearchableList(buttons.build(), "");

            TextField textField = pair.getRight();
            textField.setWindow(this);
            textField.setLocation(0, 0);

            WrappingList list = pair.getLeft();
            list.setWindow(this);
            list.setItemsPerRow(4);
            list.setVisibleRows(4);
            list.setHeight(60);
            list.setLocation(0, textField.getFullHeight() + 4);
            list.getScrollUpArrow().setLocation(list.getXRight() + 2, 0);
            list.alignArrows();

            children = ImmutableList.of(textField, list);
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

    private static class IconBtn extends AbstractIconButton implements INamedElement {

        private static final TextureWrapper NORMAL = new TextureWrapper(new ResourceLocation(HarmonicsCoreTest.MODID, "textures/gui/icons.png"), 256, 256, 0, 0, 16, 16);
        private static final TextureWrapper HOVERED = NORMAL.right(16);

        private final String name;

        public IconBtn(String name) {
            super(0, 0, 16, 16);
            this.name = name;
        }

        @Override
        public TextureWrapper getTextureNormal() {
            return NORMAL;
        }

        @Override
        public TextureWrapper getTextureHovered() {
            return HOVERED;
        }

        @Override
        public void render(int mouseX, int mouseY, float particleTicks) {
            super.render(mouseX, mouseY, particleTicks);
            if (isInside(mouseX, mouseY)) {
                glDisable(GL_SCISSOR_TEST);
                GuiUtils.drawHoveringText(ImmutableList.of(name), mouseX, mouseY, scaledWidth(), scaledHeight(), Integer.MAX_VALUE, fontRenderer());
                glEnable(GL_SCISSOR_TEST);
            }
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
