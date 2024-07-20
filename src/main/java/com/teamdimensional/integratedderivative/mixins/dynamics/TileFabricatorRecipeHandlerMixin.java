package com.teamdimensional.integratedderivative.mixins.dynamics;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import forestry.api.recipes.ICarpenterRecipe;
import forestry.api.recipes.IFabricatorRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler.TileFabricatorRecipeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = TileFabricatorRecipeHandler.class, remap = false)
public class TileFabricatorRecipeHandlerMixin {
    @WrapOperation(
        method = "transformRecipe(Lforestry/api/recipes/IFabricatorRecipe;)Lorg/cyclops/commoncapabilities/api/capability/recipehandler/IRecipeDefinition;",
        at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z")
    )
    private <E> boolean makePlanEmpty(List<E> instance, E e, Operation<Boolean> original) {
        return false;
    }

    @ModifyVariable(
        method = "findRecipe(Lorg/cyclops/commoncapabilities/api/ingredient/IMixedIngredients;)Lforestry/api/recipes/IFabricatorRecipe;",
        at = @At(value = "INVOKE", target = "Lforestry/factory/recipes/FabricatorRecipeManager;" +
            "findMatchingRecipe(Lnet/minecraft/item/ItemStack;Lnet/minecraft/inventory/IInventory;)Lforestry/core/recipes/RecipePair;")
    )
    private ItemStack makePlanEmpty(ItemStack plan) {
        return ItemStack.EMPTY;
    }

    @Inject(
        method = "findRecipe(Lorg/cyclops/commoncapabilities/api/ingredient/IMixedIngredients;)Lforestry/api/recipes/IFabricatorRecipe;",
        at = @At(value = "INVOKE", target = "Lforestry/factory/recipes/FabricatorRecipeManager;" +
            "findMatchingRecipe(Lnet/minecraft/item/ItemStack;Lnet/minecraft/inventory/IInventory;)Lforestry/core/recipes/RecipePair;")
    )
    private void dontAddPlanHereAlso(IMixedIngredients input, CallbackInfoReturnable<IFabricatorRecipe> cir,
                                     @Local List<ItemStack> itemStacks, @Local InventoryCrafting grid) {

        for(int i = 0; i < itemStacks.size(); ++i) {
            grid.setInventorySlotContents(i, itemStacks.get(i));
        }
    }
}
