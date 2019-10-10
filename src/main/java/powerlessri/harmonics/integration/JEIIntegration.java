package powerlessri.harmonics.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.util.ResourceLocation;
import powerlessri.harmonics.HarmonicsCore;

@JeiPlugin
public class JEIIntegration implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(HarmonicsCore.MODID, "jei_integration");
    }
}
