package com.teamdimensional.integratedderivative.recipe;

import com.teamdimensional.integratedderivative.IntegratedDerivativeConfig;
import com.teamdimensional.integratedderivative.Tags;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryModifiable;

@Mod.EventBusSubscriber
public class DerivativeRecipeManager {
    @SubscribeEvent
    public static void register(RegistryEvent.Register<IRecipe> event) {
        if (IntegratedDerivativeConfig.dynamicsFixes.fixODCCrafting) {
            IForgeRegistryModifiable<IRecipe> r = (IForgeRegistryModifiable<IRecipe>) event.getRegistry();
            r.remove(new ResourceLocation("integrateddynamics:part_connector_omni_directional_item_12"));
            r.register(new CopyODCChannelRecipe().setRegistryName(Tags.MOD_ID, "copy_omni_connector"));
        }
    }
}
