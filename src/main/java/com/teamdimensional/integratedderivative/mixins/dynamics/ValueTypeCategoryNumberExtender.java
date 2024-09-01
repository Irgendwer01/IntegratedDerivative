package com.teamdimensional.integratedderivative.mixins.dynamics;

import com.teamdimensional.integratedderivative.mixins.api.IValueCategoryNumberExtended;
import com.teamdimensional.integratedderivative.mixins.api.IValueTypeNumberExtended;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueCastRegistry;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueTypeNumber;
import org.cyclops.integrateddynamics.api.evaluate.variable.IVariable;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeCategoryNumber;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = ValueTypeCategoryNumber.class, remap = false)
public abstract class ValueTypeCategoryNumberExtender implements IValueCategoryNumberExtended {
    @Shadow public abstract IValueTypeNumber getLowestType(IValueTypeNumber... types);

    @Shadow protected abstract IValueTypeNumber getType(IVariable v) throws EvaluationException;

    @Shadow protected abstract IValue castValue(IValueTypeNumber type, IValue value) throws IValueCastRegistry.ValueCastException;

    @Unique
    public IValue gateway$power(IVariable a, IVariable b) throws EvaluationException {
        IValueTypeNumber type = getLowestType(getType(a), getType(b));
        if (!(type instanceof IValueTypeNumberExtended)) throw new EvaluationException("Unknown number types in POWER operation");
        return ((IValueTypeNumberExtended) type).gateway$power(
            castValue(type, a.getValue()),
            castValue(type, b.getValue())
        );
    }
}
