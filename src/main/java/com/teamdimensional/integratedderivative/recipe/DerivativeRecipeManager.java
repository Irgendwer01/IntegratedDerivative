package com.teamdimensional.integratedderivative.recipe;

import com.teamdimensional.integratedderivative.IntegratedDerivative;
import com.teamdimensional.integratedderivative.IntegratedDerivativeConfig;
import com.teamdimensional.integratedderivative.Tags;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber
public class DerivativeRecipeManager {
    @SubscribeEvent
    public static void register(RegistryEvent.Register<IRecipe> event) {
        if (IntegratedDerivativeConfig.dynamicsFixes.fixODCCrafting) {
            IForgeRegistryModifiable<IRecipe> r = (IForgeRegistryModifiable<IRecipe>) event.getRegistry();

            List<ResourceLocation> badRecipes = new ArrayList<>();
            List<IRecipe> newRecipes = new ArrayList<>();
            for (Map.Entry<ResourceLocation, IRecipe> ent : r.getEntries()) {
                IRecipe rec = ent.getValue();
                if (rec.getRecipeOutput().getItem() == CopyODCChannelRecipe.connector() && rec.getRecipeOutput().getCount() > 1) {
                    boolean foundInput = false;
                    for (Ingredient ing : rec.getIngredients()) {
                        if (ing.test(new ItemStack(CopyODCChannelRecipe.connector()))) {
                            foundInput = true;
                            break;
                        }
                    }

                    if (foundInput) {
                        newRecipes.add(rec);
                        badRecipes.add(ent.getKey());
                    }
                }
            }

            IntegratedDerivative.LOGGER.info("Replacing {} recipes that output an Omni-Directional Connector...", badRecipes.size());
            for (ResourceLocation key : badRecipes) {
                r.remove(key);
            }
            for (IRecipe base : newRecipes) {
                String registryName = Objects.requireNonNull(base.getRegistryName()).toString();
                r.register(new CopyODCChannelRecipe(base).setRegistryName(Tags.MOD_ID, registryName.replace(':', '_')));
            }
        }
    }
}
