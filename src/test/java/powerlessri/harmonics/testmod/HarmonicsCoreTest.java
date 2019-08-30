package powerlessri.harmonics.testmod;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Hand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import powerlessri.harmonics.testmod.gui.TestGui1;

@Mod("hctest")
public class HarmonicsCoreTest {

    public HarmonicsCoreTest() {
        MinecraftForge.EVENT_BUS.addListener(this::onRightClick);
    }

    private void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getWorld().isRemote && event.getHand() == Hand.MAIN_HAND) {
            if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.SPONGE) {
                Minecraft.getInstance().displayGuiScreen(new TestGui1());
            }
        }
    }
}
