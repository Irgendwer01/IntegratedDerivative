package com.teamdimensional.integratedderivative.mixins.dynamics;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import forestry.api.recipes.ICarpenterRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.modcompat.forestry.capability.recipehandler.TileCarpenterRecipeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = TileCarpenterRecipeHandler.class, remap = false)
public class TileCarpenterRecipeHandlerMixin {
    @WrapOperation(
        method = "transformRecipe(Lforestry/api/recipes/ICarpenterRecipe;)Lorg/cyclops/commoncapabilities/api/capability/recipehandler/IRecipeDefinition;",
        at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z")
    )
    private <E> boolean dontAddBox(List<E> instance, E e, Operation<Boolean> original) {
        return false;
    }

    @ModifyVariable(
        method = "findRecipe(Lorg/cyclops/commoncapabilities/api/ingredient/IMixedIngredients;)Lforestry/api/recipes/ICarpenterRecipe;",
        at = @At(value = "INVOKE", target = "Lorg/cyclops/commoncapabilities/api/ingredient/IMixedIngredients;getFirstNonEmpty" +
            "(Lorg/cyclops/commoncapabilities/api/ingredient/IngredientComponent;)Ljava/lang/Object;")
    )
    private ItemStack makeBoxEmpty(ItemStack box) {
        return ItemStack.EMPTY;
    }

    @Inject(
        method = "findRecipe(Lorg/cyclops/commoncapabilities/api/ingredient/IMixedIngredients;)Lforestry/api/recipes/ICarpenterRecipe;",
        at = @At(value = "INVOKE", target = "Lorg/cyclops/commoncapabilities/api/ingredient/IMixedIngredients;getFirstNonEmpty" +
            "(Lorg/cyclops/commoncapabilities/api/ingredient/IngredientComponent;)Ljava/lang/Object;")
    )
    private void dontAddBoxHereAlso(IMixedIngredients input, CallbackInfoReturnable<ICarpenterRecipe> cir,
                                    @Local List<ItemStack> itemStacks, @Local InventoryCrafting grid) {

        for(int i = 0; i < itemStacks.size(); ++i) {
            grid.setInventorySlotContents(i, itemStacks.get(i));
        }
    }
}
