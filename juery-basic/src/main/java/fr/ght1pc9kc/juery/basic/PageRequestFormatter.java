package fr.ght1pc9kc.juery.basic;

import fr.ght1pc9kc.juery.api.Criteria;
import fr.ght1pc9kc.juery.api.PageRequest;
import fr.ght1pc9kc.juery.api.pagination.Sort;
import lombok.experimental.UtilityClass;

import java.util.*;

/**
 * Utility class to parse and format QueryString.
 * <p>
 * Query String parameters :
 * <ul>
 *     <li><strong>_p</strong>: The page number</li>
 *     <li><strong>_pp</strong>: The number of item Per Page</li>
 *     <li><strong>_s</strong>: The sorting information</li>
 *     <li>All others parameter are parsed as {@link Criteria}</li>
 * </ul>
 * @deprecated Use {@link QueryStringParser} instead.
 */
@Deprecated(since = "1.2.0", forRemoval = true)
@UtilityClass
public class PageRequestFormatter {
    private static final QueryStringParser DELEGATE = QueryStringParser.withDefaultConfig();

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
    public static String formatPageRequest(PageRequest pr) {
        return QueryStringParser.withDefaultConfig().format(pr);
    }

    public static String formatSortValue(Sort sort) {
        return DELEGATE.formatSortValue(sort);
    }

    /**
     * Parse splitted Query String into {@link PageRequest}.
     *
     * @param queryString The splitted QueryString as {@code Map<String, List<String>>}
     * @return The PageRequest
     */
    public static PageRequest parse(Map<String, List<String>> queryString) {
        return DELEGATE.parse(queryString);
    }

    public static PageRequest parse(String queryString) {
        return DELEGATE.parse(queryString);
    }

    public static Criteria parseCriterionParameter(String key, List<String> paramValue) {
        return DELEGATE.parseCriterionParameter(key, paramValue);
    }
}
