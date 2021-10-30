package fr.ght1pc9kc.juery.basic;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;
import java.util.Set;

/**
 * Build a QueryStringParser configuration.
 *
 * <p>This allows to specify or customize the querystring parameters names for pagination and sorting</p>
 *
 * <ul>
 *     <li><b>page</b>: The current page number to return.</li>
 *     <li><b>size</b>: The size of the page to return, the number of elements.</li>
 *     <li><b>from</b>: The first element number to return (the offset).</li>
 *     <li><b>to</b>: The last element number to return (the limit).</li>
 *     <li><b>sort</b>: The parameter used for sorting clause.</li>
 *     <li><b>max per page</b>: The maximum elements returned in a page./</li>
 * </ul>
 * @see QueryStringParser for more information
 */
@Value
public class ParserConfiguration {
    /**
     * <p>The default configuration:</p>
     * <ul>
     *     <li><b>page</b> = {@code "_p"}</li>
     *     <li><b>size</b> = {@code "_pp"}</li>
     *     <li><b>from</b> = {@code "_from"}</li>
     *     <li><b>to</b> = {@code "_to"}</li>
     *     <li><b>sort</b> = {@code "_s"}</li>
     *     <li><b>maxPerPage</b> = {@code 100}</li>
     * </ul>
     */
    public static final ParserConfiguration DEFAULT = ParserConfiguration.builder().build();

    private static final String DEFAULT_PAGE_PARAMETER = "_p";
    private static final String DEFAULT_SIZE_PARAMETER = "_pp";
    private static final String DEFAULT_FROM_PARAMETER = "_from";
    private static final String DEFAULT_TO_PARAMETER = "_to";
    private static final String DEFAULT_SORT_PARAMETER = "_s";
    private static final int DEFAULT_MAX_PER_PAGE = 100;

    String pageParameter;
    String sizeParameter;
    String fromParameter;
    String toParameter;
    String sortParameter;
    int maxPageSize;

    Set<String> excludeFilterParameters;

    @Builder
    private ParserConfiguration(String page, String size, String from, String to, String sort, int maxPageSize) {
        this.pageParameter = Optional.ofNullable(page).orElse(DEFAULT_PAGE_PARAMETER);
        this.sizeParameter = Optional.ofNullable(size).orElse(DEFAULT_SIZE_PARAMETER);
        this.fromParameter = Optional.ofNullable(from).orElse(DEFAULT_FROM_PARAMETER);
        this.toParameter = Optional.ofNullable(to).orElse(DEFAULT_TO_PARAMETER);
        this.sortParameter = Optional.ofNullable(sort).orElse(DEFAULT_SORT_PARAMETER);
        this.maxPageSize = (maxPageSize <= 0) ? DEFAULT_MAX_PER_PAGE : maxPageSize;
        this.excludeFilterParameters = Set.of(
                pageParameter, sizeParameter, fromParameter, toParameter, sortParameter
        );
    }
}
