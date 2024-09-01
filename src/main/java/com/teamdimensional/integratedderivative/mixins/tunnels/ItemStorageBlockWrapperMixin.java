package com.teamdimensional.integratedderivative.mixins.tunnels;

import com.teamdimensional.integratedderivative.IntegratedDerivativeConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import org.cyclops.integratedtunnels.core.ItemStorageBlockWrapper;
import org.cyclops.integratedtunnels.core.PlayerHelpers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = ItemStorageBlockWrapper.class, remap = false)
public class ItemStorageBlockWrapperMixin {
    @Shadow @Final
    private WorldServer world;

    @Shadow @Final
    private BlockPos pos;

    @Inject(method = "getItemStacks", at = @At("HEAD"), cancellable = true)
    private void fixBedrockBreaking(CallbackInfoReturnable<List<ItemStack>> cir) {
        if (IntegratedDerivativeConfig.tunnelsFixes.fixBedrockBreaker) {
            IBlockState bs = world.getBlockState(pos);
            if (bs.getBlockHardness(world, pos) < 0 ||
                bs.getPlayerRelativeBlockHardness(PlayerHelpers.getFakePlayer(world), world, pos) < 0) {
                cir.setReturnValue(new ArrayList<>());
            }
        }
    }
}
