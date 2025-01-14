package tech.shuihai.facai.seedlab.core;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class FullyRandomSeedLab<T> {

    private final Class<T> type;

    private final Seeder seeder;

    private FullyRandomSeedLab(Class<T> type) {
        this.type = type;
        this.seeder = new Seeder();
    }

    public static <T> FullyRandomSeedLab<T> of(Class<T> type) {
        return new FullyRandomSeedLab<>(type);
    }

    public List<T> invoke(int size) {

        if (type.isEnum()) {
            return IntStream.range(0, size)
                    .mapToObj(_ -> seeder.of(type))
                    .toList();
        }

        @SuppressWarnings("unchecked")
        Constructor<T> builder = (Constructor<T>) Arrays
                .stream(type.getDeclaredConstructors())
                .max(Comparator.comparingInt(o -> o.getParameters().length))
                .orElseThrow(() -> new RuntimeException("no suitable constructor found"));
        if (builder.getParameters().length == 0) {
            return multiValueFieldByField(builder, type, size);
        }
        return produceByConstructor(builder, size);
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
            int size
    ) {
        return IntStream
                .range(0, size)
                .mapToObj(_ -> {
                    Object[] args = new Object[builder.getParameters().length];
                    for (int i = 0; i < builder.getParameters().length; i++) {
                        Parameter parameter = builder.getParameters()[i];
                        Class<?> type = parameter.getType();
                        args[i] = seeder.of(type);
                    }
                    try {
                        builder.setAccessible(true);
                        return builder.newInstance(args);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

}
