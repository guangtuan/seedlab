package tech.shuihai.facai.seedlab.fixed;

import java.util.function.Function;

public sealed interface FixedVariable permits
        FixedVariable.FixedVariableBySetter,
        FixedVariable.FixedVariableByName {

    record FixedVariableByName(
            String name,
            Function<Integer, Object> value
    ) implements FixedVariable {
    }

    record FixedVariableBySetter(
            String name,
            Function<Integer, Object> value
    ) implements FixedVariable {
    }

}
