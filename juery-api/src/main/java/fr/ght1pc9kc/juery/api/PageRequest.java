package fr.ght1pc9kc.juery.api;

import lombok.With;

/**
 * Define a Page Request with Search filters and Pagination
 *
 * <pre>{@code
 * PageRequest.builder()
 *     .page(2).size(100)
 *     .filter(Criteria.property("profile").eq("jedi").and(Criteria.property("job").eq("master")))
 *     .sort(Sort.of(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "email")))
 *     .build();
 * }</pre>
 */
public record PageRequest(
        Pagination pagination,
        @With Criteria filter
) {
    private static final PageRequest ALL = new PageRequest(Pagination.ALL, Criteria.none());


    /**
     * Create a {@link PageRequest} with specified {@link Pagination} and {@link Criteria} filters.
     *
     * @param page   The pagination data
     * @param filter The filtering data
     * @return the request
     */
    public static PageRequest of(Pagination page, Criteria filter) {
        if (Pagination.ALL == page && Criteria.none() == filter) {
            return ALL;
        }
        return new PageRequest(page, filter);
    }

    /**
     * Create a {@link PageRequest} with no pagination and no filter. A request for all elements.
     *
     * @return the request for all elements
     */
    public static PageRequest all() {
        return ALL;
    }

    /**
     * Create a {@link PageRequest} with no pagination and specified {@link Criteria} filters.
     *
     * @return the request for all filtered elements
     */
    public static PageRequest all(Criteria criteria) {
        return new PageRequest(Pagination.ALL, criteria);
    }

    /**
     * Create a {@link PageRequest} for the first matching the specified {@link Criteria} filters.
     *
     * @return the request for first filtered element
     */
    public static PageRequest one(Criteria criteria) {
        return new PageRequest(Pagination.FIRST, criteria);
    }

    public PageRequest and(Criteria criteria) {
        return new PageRequest(pagination, filter.and(criteria));
    }
}
