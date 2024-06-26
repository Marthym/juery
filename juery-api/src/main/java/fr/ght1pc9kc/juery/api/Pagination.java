package fr.ght1pc9kc.juery.api;

import fr.ght1pc9kc.juery.api.pagination.Sort;

/**
 * The pagination query parameters.
 */
public record Pagination(
        int offset,
        int size,
        Sort sort
) {
    /**
     * The {@code FIRST} element of the pageable list
     */
    public static final Pagination FIRST = new Pagination(0, 1, Sort.UNSORTED);
    /**
     * {@code ALL} the elements of the pageable list
     */
    public static final Pagination ALL = new Pagination(0, Integer.MAX_VALUE, Sort.UNSORTED);

    /**
     * Create a {@link Pagination} object
     *
     * @param offset The number of element to skip from the start of the list.
     * @param size   The number of element to include in the page
     * @return The Pagination object
     */
    public static Pagination of(int offset, int size) {
        if (offset < 0 || size < 0) {
            return ALL;
        } else if (offset == 0 && size == 1) {
            return FIRST;
        } else {
            return new Pagination(offset, size, Sort.UNSORTED);
        }
    }

    /**
     * Create a {@link Pagination} object
     *
     * @param offset The number of element to skip from the start of the list.
     * @param size   The number of element to include in the page
     * @param sort   The {@link Sort} information
     * @return The Pagination object
     */
    public static Pagination of(int offset, int size, Sort sort) {
        if (sort == Sort.UNSORTED) {
            return of(offset, size);
        } else if (offset == 0 && size == 1) {
            return FIRST;
        } else {
            return new Pagination(offset, size, sort);
        }
    }
}
