package fr.ght1pc9kc.juery.basic;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class ParserConfiguration {
    @Builder.Default String pageParameter = "_p";
    @Builder.Default String sizeParameter = "_pp";
    @Builder.Default String fromParameter = "_from";
    @Builder.Default String toParameter = "_to";
    @Builder.Default String sortParameter = "_s";
    @Builder.Default int maxPageSize = 100;

    Set<String> excludeFilterParameters = Set.of(
            pageParameter, sizeParameter, fromParameter, toParameter, sortParameter
    );
}
