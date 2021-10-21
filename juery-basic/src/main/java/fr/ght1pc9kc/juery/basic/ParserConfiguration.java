package fr.ght1pc9kc.juery.basic;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;
import java.util.Set;

@Value
public class ParserConfiguration {
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
