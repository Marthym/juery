package fr.ght1pc9kc.juery.api.pagination;

import org.jetbrains.annotations.NotNull;

public record Order(
        Direction direction,
        String property
) {
    public Order {
        if (property.isBlank()) {
            throw new IllegalArgumentException("The property must not be blank !");
        }
    }

    public static Order asc(@NotNull String prop) {
        return new Order(Direction.ASC, prop);
    }

    public static Order desc(@NotNull String prop) {
        return new Order(Direction.DESC, prop);
    }
}
