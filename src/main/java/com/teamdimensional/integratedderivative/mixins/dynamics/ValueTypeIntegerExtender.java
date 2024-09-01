package com.teamdimensional.integratedderivative.mixins.dynamics;

import com.teamdimensional.integratedderivative.mixins.api.IValueTypeNumberExtended;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeBase;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeInteger;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;

@Mixin(value = ValueTypeInteger.class, remap = false)
public abstract class ValueTypeIntegerExtender extends ValueTypeBase<ValueTypeInteger.ValueInteger> implements IValueTypeNumberExtended<ValueTypeInteger.ValueInteger> {

    public ValueTypeIntegerExtender(String typeName, int color, String colorFormat, @Nullable Class<ValueTypeInteger.ValueInteger> valueClass) {
        super(typeName, color, colorFormat, valueClass);
    }

    @Override
    public ValueTypeInteger.ValueInteger gateway$power(ValueTypeInteger.ValueInteger a, ValueTypeInteger.ValueInteger b) throws EvaluationException {
        if (a.getRawValue() == 0 && b.getRawValue() == 0) {
            throw new EvaluationException("Cannot compute 0^0");
        }
        return ValueTypeInteger.ValueInteger.of((int) Math.pow(a.getRawValue(), b.getRawValue()));
    }
}
