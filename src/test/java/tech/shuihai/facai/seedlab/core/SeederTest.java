package tech.shuihai.facai.seedlab.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.shuihai.facai.seedlab.businessMock.Person;

import static org.junit.jupiter.api.Assertions.*;

class SeederTest {

    @Test
    void of() {
        Seeder seeder = new Seeder();
        Class<?>[] classes = {
                Integer.class,
                Double.class,
                Float.class,
                Short.class,
                Long.class,
                Byte.class,
                Boolean.class,
                String.class
        };
        for (Class<?> aClass : classes) {
            Assertions.assertNotNull(
                    seeder.of(aClass)
            );
        }
    }

}