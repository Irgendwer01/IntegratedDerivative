package com.teamdimensional.integratedderivative.mixins.api;

import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueTypeNamed;

public interface IValueTypeNumberExtended<V extends IValue> extends IValueType<V>, IValueTypeNamed<V> {
    V gateway$power(V a, V b) throws EvaluationException;
}
