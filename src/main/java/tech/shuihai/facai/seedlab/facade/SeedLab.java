package tech.shuihai.facai.seedlab.facade;

import tech.shuihai.facai.seedlab.core.FullyRandomSeedLab;
import tech.shuihai.facai.seedlab.core.LittleRandomSeedLab;
import tech.shuihai.facai.seedlab.fixed.FixedVariable;

import java.util.List;

public class SeedLab {

    public static <T> FullyRandomSeedLab<T> fullyRandom(
            Class<T> type
    ) {
        return FullyRandomSeedLab.of(type);
    }

    public static <T> LittleRandomSeedLab<T> littleRandom(
            Class<T> type,
            List<FixedVariable> fixedVariables
    ) {
        return LittleRandomSeedLab.of(type, fixedVariables);
    }

}
