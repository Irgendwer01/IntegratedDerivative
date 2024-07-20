package com.teamdimensional.integratedderivative.mixins.terminals;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamdimensional.integratedderivative.IntegratedDerivative;
import com.teamdimensional.integratedderivative.IntegratedDerivativeConfig;
import com.teamdimensional.integratedderivative.network.TerminalPacketExtractOneStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.network.PacketBase;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.integratedterminals.api.terminalstorage.TerminalClickType;
import org.cyclops.integratedterminals.core.terminalstorage.TerminalStorageTabIngredientComponentClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = TerminalStorageTabIngredientComponentClient.class, remap = false)
public abstract class TerminalStorageTabIngredientComponentClientMixin {

    @Shadow
    public abstract ResourceLocation getName();

    @WrapOperation(
        method = "handleClick",
        at = @At(ordinal = 2, value = "INVOKE", target =
            "Lorg/cyclops/cyclopscore/network/PacketHandler;" +
                "sendToServer(Lorg/cyclops/cyclopscore/network/PacketBase;)V")
    )
    private void sendOurPacket(
        PacketHandler instance, PacketBase packet, Operation<Void> original,
        @Local(argsOnly = true, ordinal = 0) int channel, @Local TerminalClickType clickType, @Local(ordinal = 1) Optional<ItemStack> stack
        ) {
        if (IntegratedDerivativeConfig.terminalsTweaks.shiftClickOneStack &&
            stack.isPresent() &&
            clickType.equals(TerminalClickType.STORAGE_QUICK_MOVE) &&
            (getName().toString().equals("minecraft:itemstack") || getName().toString().equals("minecraft:itemstack_crafting"))) {
            IntegratedDerivative.INSTANCE.getPacketHandler().sendToServer(
                new TerminalPacketExtractOneStack(getName().toString(), channel, stack.get()));
        } else {
            original.call(instance, packet);
        }
    }

}
