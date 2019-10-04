package powerlessri.harmonics.testmod.registry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import powerlessri.harmonics.setup.builder.BlockBuilder;
import powerlessri.harmonics.setup.builder.RegistryObjectBuilder;
import powerlessri.harmonics.testmod.HarmonicsCoreTest;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = HarmonicsCoreTest.MODID, bus = Bus.MOD)
public final class RegistryHandler {

    private RegistryHandler() {
    }

    private static List<RegistryObjectBuilder<Item, Item.Properties>> pendingItems = new ArrayList<>();
    private static List<BlockBuilder> pendingBlocks = new ArrayList<>();

    public static void setup() {
        pendingItems.add(new RegistryObjectBuilder<Item, Item.Properties>(new ResourceLocation(HarmonicsCoreTest.MODID, "test_item"))
                .builder(defaultItemProperties())
                .factory(Item::new));

        pendingBlocks.add(new BlockBuilder<>(new ResourceLocation(HarmonicsCoreTest.MODID, "test_block"))
                .properties(Block.Properties.create(Material.ROCK))
                .item(defaultItemProperties())
                .constructor(Block::new));
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        pendingItems.forEach(b -> event.getRegistry().register(b.construct()));
        pendingBlocks.forEach(b -> event.getRegistry().register(b.constructItemBlock()));
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        pendingBlocks.forEach(b -> event.getRegistry().register(b.construct()));
    }

    @SubscribeEvent
    public static void finishLoading(FMLLoadCompleteEvent event) {
        pendingItems = null;
        pendingBlocks = null;
    }

    public static Item.Properties defaultItemProperties() {
        return new Item.Properties().group(ItemGroup.MISC);
    }
}
