package com.teamdimensional.integratedderivative.mixins.terminals;

import com.teamdimensional.integratedderivative.IntegratedDerivativeConfig;
import org.cyclops.integratedterminals.core.terminalstorage.button.TerminalButtonItemStackCraftingGridClear;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = TerminalButtonItemStackCraftingGridClear.class, remap = false)
public class TerminalButtonItemStackCraftingGridClearMixin {
    @ModifyVariable(method = "clearGrid", at = @At("HEAD"), argsOnly = true)
    private static boolean editToStorage(boolean var) {
        return var ^ IntegratedDerivativeConfig.terminalsFixes.invertClearButton;
    }
}
