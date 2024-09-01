package com.teamdimensional.integratedderivative.mixins.api;

import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IVariable;

public interface IValueCategoryNumberExtended {
    IValue gateway$power(IVariable a, IVariable b) throws EvaluationException;
}
