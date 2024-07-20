package com.teamdimensional.integratedderivative.mixins.dynamics;

import com.llamalad7.mixinextras.sugar.Local;
import com.teamdimensional.integratedderivative.IntegratedDerivative;
import com.teamdimensional.integratedderivative.IntegratedDerivativeConfig;
import com.teamdimensional.integratedderivative.enums.JEICompactingMode;
import com.teamdimensional.integratedderivative.util.StackUtil;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.integrateddynamics.core.logicprogrammer.ValueTypeRecipeLPElement;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;
import org.cyclops.integrateddynamicscompat.modcompat.jei.logicprogrammer.LogicProgrammerTransferHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(value = LogicProgrammerTransferHandler.class, remap = false)
public class LogicProgrammerTransferHandlerMixin<T extends ContainerLogicProgrammerBase> {
    @Unique
    private void derivative$attemptCompact(List<ItemStack> stacks, int maxSize) {
        boolean compacting = false;
        List<ItemStack> compacted = StackUtil.compactStacks(stacks, false);
        switch (IntegratedDerivativeConfig.dynamicsTweaks.jeiCompactingMode) {
            case COMPACT_ONLY_LARGE:
                compacting = stacks.size() > maxSize && compacted.size() <= maxSize;
                break;
            case COMPACT_ALWAYS:
                compacting = compacted.size() <= maxSize;
                break;
            case COMPACT_ALWAYS_LOSSY:
                compacting = true;
                if (compacted.size() > maxSize) compacted = compacted.subList(0, maxSize);
        }

        if (compacting) {
            stacks.clear();
            stacks.addAll(compacted);
        }
    }

    @Inject(method = "handleRecipeElement",
        at = @At(value = "INVOKE", target = "Lorg/cyclops/integrateddynamics/core/logicprogrammer/ValueTypeRecipeLPElement;" +
            "isValidForRecipeGrid(Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;)Z"))
    private void beforeValidityCheck(
        ValueTypeRecipeLPElement element, T container, IRecipeLayout recipeLayout, boolean doTransfer, CallbackInfoReturnable<IRecipeTransferError> cir,
        @Local(ordinal = 0) List<ItemStack> itemInputs, @Local(ordinal = 1) List<FluidStack> fluidInputs,
        @Local(ordinal = 2) List<ItemStack> itemOutputs, @Local(ordinal = 3) List<FluidStack> fluidOutputs) {

        String className = recipeLayout.getRecipeCategory().getClass().getName();
        // Fix an unfortunate issue with 10 stacks in Forestry machines when possible
        if (IntegratedDerivativeConfig.dynamicsTweaks.autoCompactForestryRecipes) {
            if (className.equals("forestry.factory.recipes.jei.carpenter.CarpenterRecipeCategory") && itemInputs.get(0).isEmpty())
                itemInputs.remove(0);
            else if (className.equals("forestry.factory.recipes.jei.fabricator.FabricatorRecipeCategory")) {
                // remove first 2 components, 1st one is plan, 2nd is sand
                itemInputs.remove(0);
                itemInputs.remove(0);
            }
        }

        if (!Arrays.asList(IntegratedDerivativeConfig.dynamicsTweaks.recipeCompactingBlacklist).contains(className)) {
            derivative$attemptCompact(itemInputs, 9);
            derivative$attemptCompact(itemOutputs, 3);
        }
    }
}
