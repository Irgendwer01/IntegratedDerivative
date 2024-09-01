package com.teamdimensional.integratedderivative.mixins.dynamics;

import com.teamdimensional.integratedderivative.mixins.api.IValueTypeNumberExtended;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeBase;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeDouble;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;

@Mixin(ValueTypeDouble.class)
public abstract class ValueTypeDoubleExtender extends ValueTypeBase<ValueTypeDouble.ValueDouble> implements IValueTypeNumberExtended<ValueTypeDouble.ValueDouble> {

    public ValueTypeDoubleExtender(String typeName, int color, String colorFormat, @Nullable Class<ValueTypeDouble.ValueDouble> valueClass) {
        super(typeName, color, colorFormat, valueClass);
    }

    @Override
    public ValueTypeDouble.ValueDouble gateway$power(ValueTypeDouble.ValueDouble a, ValueTypeDouble.ValueDouble b) throws EvaluationException {
        if (a.getRawValue() < 0) {
            throw new EvaluationException("Cannot compute powers of negative Double");
        }
        return ValueTypeDouble.ValueDouble.of(Math.pow(a.getRawValue(), b.getRawValue()));
    }
}
