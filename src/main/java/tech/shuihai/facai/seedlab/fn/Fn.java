package tech.shuihai.facai.seedlab.fn;

import java.util.ArrayList;
import java.util.List;

public class Fn {

    public record WithIndex<T>(T value, int index) {
    }

    public static <T> List<WithIndex<T>> zipIndex(List<T> raw) {
        List<WithIndex<T>> res = new ArrayList<>();
        for (int i = 0; i < raw.size(); i++) {
            res.add(new WithIndex<>(raw.get(i), i));
        }
        return res;
    }

}
