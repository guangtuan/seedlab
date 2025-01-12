package tech.shuihai.facai.seedlab.core;

import org.junit.jupiter.api.Assertions;
import tech.shuihai.facai.seedlab.businessMock.Priority;

import java.util.Arrays;

class FullyRandomSeedLabTest {

    @org.junit.jupiter.api.Test
    void invoke() {
        var priorityFullRandomProducer = FullyRandomSeedLab.of(Priority.class);
        var priorities = priorityFullRandomProducer.invoke(50);
        for (Priority priority : priorities) {
            Assertions.assertTrue(
                    Arrays.asList(Priority.values()).contains(priority)
            );
        }
    }

}