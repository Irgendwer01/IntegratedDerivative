package com.teamdimensional.integratedderivative.mixins.terminals;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamdimensional.integratedderivative.IntegratedDerivative;
import com.teamdimensional.integratedderivative.IntegratedDerivativeConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientCollectionMutable;
import org.cyclops.cyclopscore.network.PacketBase;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.integratedterminals.IntegratedTerminals;
import org.cyclops.integratedterminals.api.terminalstorage.ITerminalStorageTabCommon;
import org.cyclops.integratedterminals.core.terminalstorage.TerminalStorageTabIngredientComponentItemStackCraftingCommon;
import org.cyclops.integratedterminals.inventory.container.ContainerTerminalStorage;
import org.cyclops.integratedterminals.network.packet.TerminalStorageIngredientItemStackCraftingGridClear;
import org.cyclops.integratedterminalscompat.modcompat.jei.terminalstorage.TerminalStorageRecipeTransferHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = TerminalStorageRecipeTransferHandler.class, remap = false)
public class TerminalStorageRecipeTransferHandlerMixin {
    @WrapOperation(method =
        "transferRecipe(Lorg/cyclops/integratedterminals/inventory/container/ContainerTerminalStorage;" +
            "Lmezz/jei/api/gui/IRecipeLayout;Lnet/minecraft/entity/player/EntityPlayer;ZZ)Lmezz/jei/api/recipe/transfer/IRecipeTransferError;",
        at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", ordinal = 1))
    private boolean partialJEIPull(List<Integer> instance, Operation<Boolean> original) {
        if (IntegratedDerivativeConfig.terminalsFixes.allowPartialJeiPull) return true;
        return original.call(instance);
    }

    @WrapOperation(method =
        "transferRecipe(Lorg/cyclops/integratedterminals/inventory/container/ContainerTerminalStorage;" +
            "Lmezz/jei/api/gui/IRecipeLayout;Lnet/minecraft/entity/player/EntityPlayer;ZZ)Lmezz/jei/api/recipe/transfer/IRecipeTransferError;",
        at = @At(value = "INVOKE", target = "Lorg/cyclops/cyclopscore/network/PacketHandler;sendToServer(Lorg/cyclops/cyclopscore/network/PacketBase;)V"))
    private void clearGridBeforeExecuting(
        PacketHandler instance, PacketBase packet, Operation<Void> original,
        @Local(argsOnly = true) ContainerTerminalStorage storage
    ) {
        if (IntegratedDerivativeConfig.terminalsFixes.invertClearButton) {
            ITerminalStorageTabCommon tab = storage.getTabCommon(storage.getSelectedTab());
            if (tab instanceof TerminalStorageTabIngredientComponentItemStackCraftingCommon) {
                IntegratedTerminals._instance.getPacketHandler().sendToServer(
                    new TerminalStorageIngredientItemStackCraftingGridClear(tab.getName().toString(), storage.getSelectedChannel(), true));
            }
        }
        original.call(instance, packet);
    }

    @Inject(method =
        "transferRecipe(Lorg/cyclops/integratedterminals/inventory/container/ContainerTerminalStorage;" +
            "Lmezz/jei/api/gui/IRecipeLayout;Lnet/minecraft/entity/player/EntityPlayer;ZZ)Lmezz/jei/api/recipe/transfer/IRecipeTransferError;",
        at = @At(value = "INVOKE", target = "Lorg/cyclops/cyclopscore/ingredient/collection/IIngredientCollectionMutable;addAll(Ljava/lang/Iterable;)Z"))
    private void addPlayerInventoryToHayStack(CallbackInfoReturnable<Boolean> cir,
        @Local IIngredientCollectionMutable<ItemStack, Integer> hayStack, @Local(argsOnly = true) EntityPlayer player) {

        if (IntegratedDerivativeConfig.terminalsFixes.shiftClickFromPlayerInventory) {
            hayStack.addAll(player.inventory.mainInventory);
        }
    }

}
