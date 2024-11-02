package com.teamdimensional.integratedderivative.mixins.dynamics;

import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.cyclops.integrateddynamics.part.PartTypeConnectorOmniDirectional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PartTypeConnectorOmniDirectional.class, remap = false)
public class PartTypeConnectorOmniDirectionalMixin {

    @Inject(method = "onCrafted", at = @At("HEAD"), cancellable = true)
    private void onCrafted(PlayerEvent.ItemCraftedEvent event, CallbackInfo ci) {
        // Replace buggy Omni-directional connector duplication recipe with one that actually works
        ci.cancel();
    }

}
