package tech.shuihai.facai.seedlab.core;

import tech.shuihai.facai.seedlab.fixed.FixedVariable;
import tech.shuihai.facai.seedlab.fn.Fn;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.IntStream;

public class LittleRandomSeedLab<T> {

    private final Class<T> type;

    private final Seeder seeder;

    private final List<FixedVariable> fixedVariables;

    private LittleRandomSeedLab(Class<T> type, List<FixedVariable> fixedVariables) {
        this.type = type;
        this.fixedVariables = fixedVariables;
        this.seeder = new Seeder();
    }

    public static <T> LittleRandomSeedLab<T> of(Class<T> type, List<FixedVariable> fixedVariables) {
        return new LittleRandomSeedLab<>(type, fixedVariables);
    }

    public List<T> invoke(int size) {
        @SuppressWarnings("unchecked")
        Constructor<T> builder = (Constructor<T>) Arrays
                .stream(type.getDeclaredConstructors())
                .max(Comparator.comparingInt(o -> o.getParameters().length))
                .orElseThrow(() -> new RuntimeException("no suitable constructor found"));
        if (builder.getParameters().length == 0) {
            return multiValueFieldByField(builder, type, size);
        }
        return produceByConstructor(builder, size, fixedVariables);
    }

    private List<T> multiValueFieldByField(
            Constructor<T> builder,
            Class<T> type,
            int size
    ) {
        builder.setAccessible(true);
        return IntStream
                .range(0, size)
                .mapToObj(_ -> {
                    try {
                        T res = builder.newInstance();
                        for (Field declaredField : type.getDeclaredFields()) {
                            // if is final, skip
                            if (Modifier.isFinal(declaredField.getModifiers())) {
                                continue;
                            }
                            declaredField.setAccessible(true);
                            declaredField.set(res, seeder.of(declaredField.getType()));
                        }
                        return res;
                    } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    private List<T> produceByConstructor(
            Constructor<T> builder,
            int size,
            List<FixedVariable> fixedVariables
    ) {
        List<Object[]> argss = new ArrayList<>();
        List<Fn.WithIndex<Parameter>> params = Fn.zipIndex(Arrays.stream(builder.getParameters()).toList());
        Object[] standard = firstLine(builder, fixedVariables, params);
        argss.add(standard);
        argss.addAll(IntStream.range(1, size)
                .boxed()
                .map(valueIndex -> params
                        .stream()
                        .map(parameterWithIndex -> {
                            int paramIndex = parameterWithIndex.index();
                            Parameter parameter = parameterWithIndex.value();
                            FixOrNot match = tryToFix(fixedVariables, parameter, valueIndex);
                            return switch (match) {
                                case LittleRandomSeedLab.DoFix fixed -> fixed.value;
                                case NoFixed ignored -> argss.getFirst()[paramIndex];
                            };
                        })
                        .toArray())
                .toList()
        );
        return argss
                .stream()
                .map(args -> {
                    try {
                        builder.setAccessible(true);
                        return builder.newInstance(args);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    private Object[] firstLine(Constructor<T> builder, List<FixedVariable> fixedVariables, List<Fn.WithIndex<Parameter>> params) {
        Object[] standard = new Object[builder.getParameters().length];
        params.forEach(param -> {
            int i = param.index();
            Parameter parameter = param.value();
            FixOrNot match = tryToFix(fixedVariables, parameter, 0);
            switch (match) {
                case LittleRandomSeedLab.DoFix fixed -> standard[i] = fixed.value;
                case NoFixed ignored -> standard[i] = seeder.of(parameter.getType());
            }
        });
        return standard;
    }

    sealed interface FixOrNot permits DoFix, NoFixed {

    }

    record DoFix(Object value) implements FixOrNot {
    }

    record NoFixed() implements FixOrNot {
    }

    private FixOrNot tryToFix(List<FixedVariable> fixedVariables, Parameter parameter, int valueIndex) {
        NoFixed noFixed = new NoFixed();
        return fixedVariables.stream()
                .map(fixedVariable -> {
                    switch (fixedVariable) {
                        case FixedVariable.FixedVariableByName fixedByName: {
                            if (fixedByName.name().equals(parameter.getName())) {
                                return new DoFix(fixedByName.value().apply(valueIndex));
                            } else {
                                return noFixed;
                            }
                        }
                        case FixedVariable.FixedVariableBySetter _: {
                            return noFixed;
                        }
                    }
                })
                .filter(it -> it instanceof DoFix)
                .findFirst()
                .orElse(noFixed);
    }

}
