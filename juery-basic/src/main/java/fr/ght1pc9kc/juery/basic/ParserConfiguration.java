package fr.ght1pc9kc.juery.basic;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;
import java.util.Set;

@Value
public class ParserConfiguration {
    String pageParameter;
    String sizeParameter;
    String fromParameter;
    String toParameter;
    String sortParameter;
    int maxPageSize;

    Set<String> excludeFilterParameters;

    @Builder
    private ParserConfiguration(String page, String size, String from, String to, String sort, int maxPageSize) {
        this.pageParameter = Optional.ofNullable(page).orElse("_p");
        this.sizeParameter = Optional.ofNullable(size).orElse("_pp");
        this.fromParameter = Optional.ofNullable(from).orElse("_from");
        this.toParameter = Optional.ofNullable(to).orElse("_to");
        this.sortParameter = Optional.ofNullable(sort).orElse("_s");
        this.maxPageSize = (maxPageSize <= 0) ? 100 : maxPageSize;
        this.excludeFilterParameters = Set.of(
                pageParameter, sizeParameter, fromParameter, toParameter, sortParameter
        );
    }
}
