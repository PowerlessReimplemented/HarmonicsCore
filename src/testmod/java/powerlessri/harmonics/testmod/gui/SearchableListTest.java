package powerlessri.harmonics.testmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.tuple.Pair;
import powerlessri.harmonics.gui.*;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.*;
import powerlessri.harmonics.gui.widget.button.AbstractIconButton;
import powerlessri.harmonics.gui.widget.panel.FilteredList;
import powerlessri.harmonics.gui.widget.panel.WrappingList;
import powerlessri.harmonics.gui.window.AbstractWindow;
import powerlessri.harmonics.testmod.HarmonicsCoreTest;

import java.util.ArrayList;
import java.util.List;

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

            List<IconBtn> buttons = new ArrayList<>();
            Pair<WrappingList, TextField> pair = FilteredList.createSearchableList(buttons, "");

            TextField textField = pair.getRight();
            textField.attachWindow(this);
            textField.setLocation(0, 0);

            WrappingList list = pair.getLeft();
            list.attachWindow(this);
            list.setItemsPerRow(4);
            list.setVisibleRows(4);
            list.setHeight(60);
            list.setLocation(0, textField.getFullHeight() + 4);
            list.getScrollUpArrow().setLocation(list.getXRight() + 2, 0);
            list.alignArrows();

            for (int i = 0; i < 40; i++) {
                buttons.add(new IconBtn("Test" + i));
            }

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
            renderVanillaStyleBackground();
            renderChildren(mouseX, mouseY, particleTicks);
            RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
        }
    }

    private static class IconBtn extends AbstractIconButton implements INamedElement {

        private static final ITexture NORMAL = Texture.portion(new ResourceLocation(HarmonicsCoreTest.MODID, "textures/gui/icons.png"), 256, 256, 0, 0, 16, 16);
        private static final ITexture HOVERED = NORMAL.moveRight(1);

        private final String name;

        public IconBtn(String name) {
            this.setDimensions(16, 16);
            this.name = name;
        }

        @Override
        public ITexture getTextureNormal() {
            return NORMAL;
        }

        @Override
        public ITexture getTextureHovered() {
            return HOVERED;
        }

        @Override
        public void render(int mouseX, int mouseY, float particleTicks) {
            super.render(mouseX, mouseY, particleTicks);
            if (isInside(mouseX, mouseY)) {
                Render2D.drawHoveringText(ImmutableList.of(name), mouseX, mouseY);
            }
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
