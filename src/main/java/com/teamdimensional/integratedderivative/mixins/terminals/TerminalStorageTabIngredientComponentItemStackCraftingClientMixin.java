package com.teamdimensional.integratedderivative.mixins.terminals;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamdimensional.integratedderivative.IntegratedDerivative;
import com.teamdimensional.integratedderivative.IntegratedDerivativeConfig;
import com.teamdimensional.integratedderivative.network.TerminalPacketShiftClickOutputOptimized;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.network.PacketBase;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.integratedterminals.core.terminalstorage.TerminalStorageTabIngredientComponentClient;
import org.cyclops.integratedterminals.core.terminalstorage.TerminalStorageTabIngredientComponentItemStackCraftingClient;
import org.cyclops.integratedterminals.inventory.container.ContainerTerminalStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = TerminalStorageTabIngredientComponentItemStackCraftingClient.class, remap = false)
public abstract class TerminalStorageTabIngredientComponentItemStackCraftingClientMixin extends TerminalStorageTabIngredientComponentClient<ItemStack, Integer> {

    public TerminalStorageTabIngredientComponentItemStackCraftingClientMixin(
        ContainerTerminalStorage container, ResourceLocation name, IngredientComponent<?, ?> ingredientComponent) {
        super(container, name, ingredientComponent);
    }

    @WrapOperation(
        method = "handleClick",
        at = @At(value = "INVOKE", target =
            "Lorg/cyclops/cyclopscore/network/PacketHandler;" +
            "sendToServer(Lorg/cyclops/cyclopscore/network/PacketBase;)V")
    )
    private void sendOurPacket(PacketHandler instance, PacketBase packet, Operation<Void> original, @Local(argsOnly = true, ordinal = 0) int channel) {
        if (IntegratedDerivativeConfig.terminalsFixes.optimizeShiftClickCrafting) {
            IntegratedDerivative.INSTANCE.getPacketHandler().sendToServer(
                new TerminalPacketShiftClickOutputOptimized(getName().toString(), channel, IntegratedDerivativeConfig.terminalsTweaks.shiftClickCraftingBehavior.value));
        } else {
            original.call(instance, packet);
        }
    }

}
