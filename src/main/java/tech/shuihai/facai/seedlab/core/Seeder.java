package tech.shuihai.facai.seedlab.core;

import java.util.Random;

public class Seeder {

    private final Random random = new Random();

    public <T> T of(Class<T> type) {
        if (type.isEnum()) {
            T[] enumConstants = type.getEnumConstants();
            int idx = random.nextInt(enumConstants.length);
            return enumConstants[idx];
        }
        if (Integer.class.equals(type)) {
            return (T) (Integer.valueOf(random.nextInt(Integer.MAX_VALUE)));
        }
        if (Double.class.equals(type)) {
            return (T) (Double.valueOf(random.nextDouble()));
        }
        if (Float.class.equals(type)) {
            return (T) (Float.valueOf(random.nextFloat()));
        }
        if (Long.class.equals(type)) {
            return (T) (Long.valueOf(random.nextLong()));
        }
        if (Byte.class.equals(type)) {
            return (T) (Byte.valueOf((byte) random.nextInt(Byte.MAX_VALUE)));
        }
        if (Short.class.equals(type)) {
            return (T) (Short.valueOf((short) random.nextInt(Short.MAX_VALUE)));
        }
        if (Boolean.class.equals(type)) {
            return (T) (Boolean.valueOf(random.nextBoolean()));
        }
        if (String.class.equals(type)) {
            // get from a-z, with length [10, 20]
            int length = random.nextInt(10) + 10;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                char c = (char) (random.nextInt(26) + 'a');
                sb.append(c);
            }
            return (T) sb.toString();
        }
        return null;
    }

}
