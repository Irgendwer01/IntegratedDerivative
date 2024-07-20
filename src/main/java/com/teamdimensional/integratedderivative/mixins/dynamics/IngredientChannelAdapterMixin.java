package com.teamdimensional.integratedderivative.mixins.dynamics;

import org.cyclops.integrateddynamics.core.network.IngredientChannelAdapter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = IngredientChannelAdapter.class, remap = false)
public interface IngredientChannelAdapterMixin {
    @Accessor
    boolean getLimitsEnabled();
}
