package fr.ght1pc9kc.juery.api;

import fr.ght1pc9kc.juery.api.pagination.Sort;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class PageRequest {
    private static final PageRequest ALL = new PageRequest(Pagination.ALL, Criteria.none());

    final Pagination pagination;
    final Criteria filter;

    public static PageRequest of(Pagination page, Criteria filter) {
        if (Pagination.ALL == page && Criteria.none() == filter) {
            return ALL;
        }
        return new PageRequest(page, filter);
    }

    @Deprecated(forRemoval = true, since = "1.1.0")
    public static PageRequest of(int page, int size) {
        Pagination pagination = (page < 0 || size < 0) ? Pagination.ALL : Pagination.of(page, size);
        return of(pagination, Criteria.none());
    }

    public static PageRequest all() {
        return ALL;
    }

    public static PageRequest all(Criteria criteria) {
        return new PageRequest(Pagination.ALL, criteria);
    }

    public static PageRequest one(Criteria criteria) {
        return new PageRequest(Pagination.FIRST, criteria);
    }

    public PageRequest and(Criteria criteria) {
        return new PageRequest(pagination, filter.and(criteria));
    }

    @Deprecated(forRemoval = true, since = "1.1.0")
    PageRequest(int page, int size, Sort sort, Criteria filter) {
        this.pagination = Pagination.of(page, size, sort);
        this.filter = filter;
    }
}
