package fr.ght1pc9kc.juery.api;

import fr.ght1pc9kc.juery.api.pagination.Sort;
import lombok.Value;

@Value
public final class Pagination {
    public static final Pagination FIRST = new Pagination(0, 1, Sort.UNSORTED);
    public static final Pagination ALL = new Pagination(-1, -1, Sort.UNSORTED);

    final int page;
    final int size;
    final Sort sort;

    public static Pagination of(int page, int size) {
        if (page < 0 || size < 0) {
            return ALL;
        } else if (page == 0 && size == 1) {
            return FIRST;
        } else {
            return new Pagination(page, size, Sort.UNSORTED);
        }
    }

    public static Pagination of(int page, int size, Sort sort) {
        if (sort == Sort.UNSORTED) {
            return of(page, size);
        } else {
            return new Pagination(page, size, sort);
        }
    }
}
