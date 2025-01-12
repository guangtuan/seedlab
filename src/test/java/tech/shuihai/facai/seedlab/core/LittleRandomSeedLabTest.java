package tech.shuihai.facai.seedlab.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.shuihai.facai.seedlab.businessMock.Person;
import tech.shuihai.facai.seedlab.businessMock.School;
import tech.shuihai.facai.seedlab.fixed.FixedVariable;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class LittleRandomSeedLabTest {

    @Test
    void invoke() {
        LittleRandomSeedLab<Person> address = LittleRandomSeedLab.of(
                Person.class,
                List.of(
                        new FixedVariable.FixedVariableByName(
                                "age",
                                valueIndex -> {
                                    if (valueIndex == 0) {
                                        return 20;
                                    }
                                    if (valueIndex == 1) {
                                        return 21;
                                    }
                                    return valueIndex;
                                }
                        )
                )
        );
        List<Person> people = address.invoke(2);
        Assertions.assertEquals(
                20, people.getFirst().age()
        );
        Assertions.assertEquals(
                21, people.get(1).age()
        );
    }

}
