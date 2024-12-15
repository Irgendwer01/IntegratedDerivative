package com.teamdimensional.integratedderivative.mixins.dynamics;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "org.cyclops.cyclopscore.infobook.InfoBookParser$3", remap = false)
public class InfoBookParserMixin {
    @Redirect(method = "create", at = @At(value = "INVOKE", target = "Lorg/cyclops/cyclopscore/helper/CraftingHelpers;findCraftingRecipe(Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/item/crafting/IRecipe;"))
    private IRecipe injected(ItemStack itemStack, int index) throws IllegalArgumentException {
        // copied from the cyclops core's code, replacing the complicated check for equality with
        // a check that ignores item NBT, because the new Omni-Directional Connector recipe warns otherwise
        if (itemStack == null) {
            throw new IllegalArgumentException("ItemStack cannot be null");
        }

        int indexAttempt = index;
        for (IRecipe recipe : CraftingManager.REGISTRY) {
            if (recipe.getRecipeOutput().getItem() == itemStack.getItem()) {
                int meta = recipe.getRecipeOutput().getMetadata();
                if (meta != itemStack.getMetadata() && itemStack.getMetadata() != OreDictionary.WILDCARD_VALUE) continue;
                if (indexAttempt-- == 0) return recipe;
            }
        }
        throw new IllegalArgumentException("Could not find crafting recipe for " + itemStack + "::"
                + itemStack.getTagCompound() + " with index " + index);

    }
}
