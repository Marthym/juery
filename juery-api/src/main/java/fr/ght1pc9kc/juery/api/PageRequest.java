package fr.ght1pc9kc.juery.api;

import fr.ght1pc9kc.juery.api.pagination.Sort;
import lombok.RequiredArgsConstructor;
import lombok.Value;

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
@Value
@RequiredArgsConstructor
public class PageRequest {
    private static final PageRequest ALL = new PageRequest(Pagination.ALL, Criteria.none());

    final Pagination pagination;
    final Criteria filter;

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
     * Create a {@link PageRequest} with Pagination without any filter
     *
     * @param page The page number
     * @param size The size of one page
     * @return The request
     * @deprecated Use instead {@link PageRequest#of(Pagination, Criteria)}
     */
    @Deprecated(forRemoval = true, since = "1.1.0")
    public static PageRequest of(int page, int size) {
        Pagination pagination = (page < 0 || size < 0) ? Pagination.ALL : Pagination.of(page, size);
        return of(pagination, Criteria.none());
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

    /**
     * Create a {@link PageRequest} with pagination and filtering
     *
     * @param page   The page number
     * @param size   The size of one page
     * @param sort   The sorting data
     * @param filter The criteria filters
     * @deprecated Use instead {@link PageRequest#of(Pagination, Criteria)}
     */
    @Deprecated(forRemoval = true, since = "1.1.0")
    PageRequest(int page, int size, Sort sort, Criteria filter) {
        this.pagination = Pagination.of(page, size, sort);
        this.filter = filter;
    }
}
