package com.teamdimensional.integratedderivative.mixins.terminals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.cyclops.integratedterminals.core.terminalstorage.TerminalStorageTabIngredientComponentItemStackCraftingCommon;
import org.cyclops.integratedterminals.core.terminalstorage.TerminalStorageTabIngredientComponentServer;
import org.cyclops.integratedterminals.network.packet.TerminalStorageIngredientItemStackCraftingGridClear;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TerminalStorageIngredientItemStackCraftingGridClear.class, remap = false)
public class TerminalStorageIngredientItemStackCraftingGridClearMixin {

    @Inject(method = "clearGrid", at = @At("TAIL"))
    private static void properlyMarkDirty(
        TerminalStorageTabIngredientComponentItemStackCraftingCommon tabCommon, TerminalStorageTabIngredientComponentServer<ItemStack, Integer> tabServer,
        int channel, boolean toStorage, EntityPlayer player, CallbackInfo ci) {

        tabCommon.getInventoryCrafting().markDirty();
    }

}
