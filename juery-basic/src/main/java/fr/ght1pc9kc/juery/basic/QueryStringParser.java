package fr.ght1pc9kc.juery.basic;

import fr.ght1pc9kc.juery.api.Criteria;
import fr.ght1pc9kc.juery.api.PageRequest;
import fr.ght1pc9kc.juery.api.pagination.Sort;
import fr.ght1pc9kc.juery.basic.parser.QueryStringParserImpl;

import java.util.List;
import java.util.Map;

public interface QueryStringParser {

    /**
     * <p>Format a {@link PageRequest} into a Query String.</p>
     *
     * <p>Ex:</p>
     * <pre>{@code
     *   PageRequest.of(
     *           Pagination.of(2, 100, Sort.of(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "email"))),
     *           Criteria.property("profile").eq("jedi").and(Criteria.property("job").eq("master")))),
     * }</pre>
     * <p>will result in {@code `_p=2&_s=name,-email&profile=jedi&job=master`}</p>
     *
     * @param pr The {@link PageRequest} to format
     * @return The string representing a valid QueryString
     */
    String format(PageRequest pr);

    String formatSortValue(Sort sort);

    /**
     * Parse splitted Query String into {@link PageRequest}.
     *
     * @param queryString The splitted QueryString as {@code Map<String, List<String>>}
     * @return The PageRequest
     */
    PageRequest parse(Map<String, List<String>> queryString);

    PageRequest parse(String queryString);

    Criteria parseCriterionParameter(String key, List<String> paramValue);

    static QueryStringParser withConfig(ParserConfiguration config) {
        return new QueryStringParserImpl(config);
    }

    static QueryStringParser _default() {
        return new QueryStringParserImpl(ParserConfiguration.builder().build());
    }
}
