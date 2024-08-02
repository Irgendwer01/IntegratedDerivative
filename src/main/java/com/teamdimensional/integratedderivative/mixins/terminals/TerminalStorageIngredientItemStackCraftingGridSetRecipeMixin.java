package com.teamdimensional.integratedderivative.mixins.terminals;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamdimensional.integratedderivative.IntegratedDerivativeConfig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.inventory.PlayerInventoryIterator;
import org.cyclops.integratedterminalscompat.network.packet.TerminalStorageIngredientItemStackCraftingGridSetRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Map;

@Mixin(value = TerminalStorageIngredientItemStackCraftingGridSetRecipe.class, remap = false)
public class TerminalStorageIngredientItemStackCraftingGridSetRecipeMixin {

    @WrapOperation(method =
        "actionServer",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 1))
    private boolean pullMissingFromInventory(
        ItemStack instance, Operation<Boolean> original, @Local Slot slot,
        @Local(argsOnly = true) EntityPlayerMP player,
        @Local Map.Entry<Integer, List<ItemStack>> entry
    ) {
        if (!original.call(instance)) {
            return false;
        }

        if (IntegratedDerivativeConfig.terminalsFixes.shiftClickFromPlayerInventory) {
            PlayerInventoryIterator iterator = new PlayerInventoryIterator(player);
            while (iterator.hasNext()) {
                ItemStack inInv = iterator.next();
                if (entry.getValue().stream().anyMatch(inInv::isItemEqual)) {
                    ItemStack copied = inInv.copy();
                    copied.setCount(1);
                    inInv.shrink(1);
                    iterator.replace(inInv);
                    slot.putStack(copied);
                    // We keep true return value so our inventory stack doesn't get overridden by the empty stack
                    break;
                }
            }
        }

        return true;
    }

}
