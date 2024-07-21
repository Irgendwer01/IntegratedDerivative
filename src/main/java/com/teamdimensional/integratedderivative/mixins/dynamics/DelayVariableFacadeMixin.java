package com.teamdimensional.integratedderivative.mixins.dynamics;

import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.api.network.IPartNetwork;
import org.cyclops.integrateddynamics.core.helper.L10NValues;
import org.cyclops.integrateddynamics.core.item.DelayVariableFacade;
import org.cyclops.integrateddynamics.core.item.ProxyVariableFacade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DelayVariableFacade.class, remap = false)
public class DelayVariableFacadeMixin extends ProxyVariableFacade {
    public DelayVariableFacadeMixin(int id, int proxyId) {
        super(id, proxyId);
    }

    @Inject(method = "getProxyInvalidTypeError", at = @At("HEAD"), cancellable = true)
    private void fixDelayTypeError(IPartNetwork network, IValueType<?> containingValueType, IValueType<?> actualType, CallbackInfoReturnable<L10NHelpers.UnlocalizedString> cir) {
        cir.setReturnValue(
            new L10NHelpers.UnlocalizedString(L10NValues.DELAY_ERROR_DELAYINVALIDTYPE,
                getProxyId(),
                new L10NHelpers.UnlocalizedString(containingValueType.getTranslationKey()),
                new L10NHelpers.UnlocalizedString(actualType.getTranslationKey()))
        );
    }
}
