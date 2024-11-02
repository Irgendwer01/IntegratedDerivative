package com.teamdimensional.integratedderivative.integration.jei;

import com.teamdimensional.integratedderivative.recipe.CopyODCChannelRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CopyODCChannel implements ICraftingRecipeWrapper {
    private final IRecipe recipe;

    public CopyODCChannel(IRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        List<ItemStack> inputs = new ArrayList<>();
        inputs.add(new ItemStack(CopyODCChannelRecipe.connector()));
        inputs.add(new ItemStack(CopyODCChannelRecipe.connector()));
        ingredients.setInputs(VanillaTypes.ITEM, inputs);

        List<ItemStack> outputs = new ArrayList<>();
        outputs.add(recipe.getRecipeOutput());
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return recipe.getRegistryName();
    }
}
