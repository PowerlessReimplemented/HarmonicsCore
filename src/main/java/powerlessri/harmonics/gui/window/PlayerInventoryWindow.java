package powerlessri.harmonics.gui.window;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import powerlessri.harmonics.gui.ITexture;
import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.Texture;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.screen.BackgroundRenderers;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.button.SimpleIconButton;
import powerlessri.harmonics.gui.widget.slot.AbstractItemSlot;
import powerlessri.harmonics.gui.widget.slot.ItemSlot;
import powerlessri.harmonics.gui.widget.slot.ItemSlotPanel;

import java.util.List;
import java.util.function.Function;

public class PlayerInventoryWindow extends AbstractPopupWindow {

    private static final ITexture CLOSE = Texture.portion(Render2D.CLOSE, 16, 16, 0, 0, 16, 16);

    private final List<IWidget> children;

    public PlayerInventoryWindow() {
        this(0, 0, ItemSlot::new);
    }

    public PlayerInventoryWindow(int x, int y, Function<ItemStack, AbstractItemSlot> factory) {
        setPosition(x, y);

        PlayerInventory playerInventory = Minecraft.getInstance().player.inventory;
        ItemSlotPanel inventory = new ItemSlotPanel(9, 3, playerInventory.mainInventory.subList(9, playerInventory.mainInventory.size()), factory);
        inventory.setBorderTop(8 + 2);
        inventory.setBorderBottom(4);
        inventory.setLocation(0, 0);
        ItemSlotPanel hotbar = new ItemSlotPanel(9, 1, playerInventory.mainInventory.subList(0, 9), factory);
        hotbar.setLocation(0, inventory.getYBottom());
        SimpleIconButton close = new SimpleIconButton(inventory.getWidth() - 8, 0, CLOSE, CLOSE);
        close.setDimensions(8, 8);
        close.onClick(b -> discard());
        children = ImmutableList.of(close, inventory, hotbar);

        setContents(inventory.getWidth(), inventory.getFullHeight() + hotbar.getFullHeight());
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
