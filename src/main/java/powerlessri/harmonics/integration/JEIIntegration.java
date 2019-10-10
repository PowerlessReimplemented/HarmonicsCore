package powerlessri.harmonics.integration;

import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.util.ResourceLocation;
import powerlessri.harmonics.HarmonicsCore;

import javax.annotation.ParametersAreNonnullByDefault;

@JeiPlugin
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class JEIIntegration implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(HarmonicsCore.MODID, "jei_integration");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        // TODO JEI excluded areas
    }
}
