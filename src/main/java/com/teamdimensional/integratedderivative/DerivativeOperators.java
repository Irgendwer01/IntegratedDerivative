package com.teamdimensional.integratedderivative;

import com.teamdimensional.integratedderivative.mixins.api.IValueCategoryNumberExtended;
import org.cyclops.integrateddynamics.api.evaluate.operator.IOperator;
import org.cyclops.integrateddynamics.core.evaluate.OperatorBuilders;
import org.cyclops.integrateddynamics.core.evaluate.operator.Operators;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;

public class DerivativeOperators {
    // Power operator
    public static final IOperator POWER = Operators.REGISTRY.register(OperatorBuilders.ARITHMETIC_2.symbol("^").operatorName("pow")
        .modId("integratedderivative")
        .function(
            variables -> ((IValueCategoryNumberExtended) ValueTypes.CATEGORY_NUMBER).gateway$power(variables.getVariables()[0], variables.getVariables()[1])
        ).build());

    // This method solely exists to force this file to load
    public static void load() {}

}
