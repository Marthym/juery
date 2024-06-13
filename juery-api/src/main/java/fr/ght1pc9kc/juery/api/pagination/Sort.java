package fr.ght1pc9kc.juery.api.pagination;

import java.util.Arrays;
import java.util.List;

public record Sort(
        List<Order> orders
) {
    public static final Sort UNSORTED = new Sort(List.of());

    public static Sort of() {
        return UNSORTED;
    }

    public static Sort of(Direction direction, String... properties) {
        return new Sort(Arrays.stream(properties)
                .map(p -> new Order(direction, p))
                .toList());
    }

    public static Sort of(Order... orders) {
        return new Sort(List.of(orders));
    }
}
