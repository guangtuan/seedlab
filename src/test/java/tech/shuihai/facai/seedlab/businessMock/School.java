package tech.shuihai.facai.seedlab.businessMock;

import java.util.Arrays;

public class School {
    private String name;
    private String address;
    private final String[] teachers = {"a", "b", "c"};

    @Override
    public String toString() {
        return "School{name='%s', address='%s', teachers=%s}"
                .formatted(
                        name,
                        address,
                        Arrays.toString(teachers)
                );
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

}
