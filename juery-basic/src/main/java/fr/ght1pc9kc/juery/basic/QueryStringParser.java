package fr.ght1pc9kc.juery.basic;

import fr.ght1pc9kc.juery.api.Criteria;
import fr.ght1pc9kc.juery.api.PageRequest;
import fr.ght1pc9kc.juery.api.pagination.Sort;
import fr.ght1pc9kc.juery.basic.parser.QueryStringParserImpl;

import java.util.List;
import java.util.Map;

/**
 * <p>Allow parsing and formatting a querystring</p>
 *
 * <p>The provided implementation of {@link QueryStringParser} allow two ways to specify page to be returned :</p>
 * <ul>
 *     <li>by offset, with {@code from} and {@code to}</li>
 *     <li>by page, with {@code page} and {@code size}</li>
 * </ul>
 * <p>This two ways are exclusive, so you can't use both, {@code page} and {@code from}, in the querystring.
 * In the querystring, if {@code page} <b>and</b> {@code from} parameters are present, the page is resolved <i>by page</i>.
 * The {@code from} parameter is ignored.</p>
 * <p>This is true only for the provided implementation.</p>
 */
public interface QueryStringParser {

    /**
     * <p>Format a {@link PageRequest} into an URL querystring.</p>
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

    /**
     * <p>Format a {@link Sort} into a sort querystring parameter</p>
     *
     * <p>Ex:</p>
     * <pre>{@code
     *   Sort.of(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "email")))
     * }</pre>
     * <p>will result in {@code `_s=name,-email`}</p>
     *
     * @param sort The {@link Sort} to format
     * @return The string representing a valid sort parameter for querystring
     */
    String formatSortValue(Sort sort);

    /**
     * Parse divided querystring into {@link PageRequest}.
     *
     * @param queryString The separated querystring parameters as {@code Map<String, List<String>>}
     * @return The PageRequest
     */
    PageRequest parse(Map<String, List<String>> queryString);

    /**
     * Parse a URL querystring into {@link PageRequest}.
     *
     * @param queryString The querystring to parse
     * @return The PageRequest
     */
    PageRequest parse(String queryString);

    /**
     * Parse querystring parameter into a {@link Criteria}.
     *
     * @param key        The querystring to parse
     * @param paramValue The value of the parameter. Can be a List if the same parameter as multiple values.
     * @return The {@link Criteria}
     */
    Criteria parseCriterionParameter(String key, List<String> paramValue);

    /**
     * <p>Build a parser with specific {@link ParserConfiguration}.</p>
     * <p>This allow to customize the querystring parameters name for pagination and sorting</p>
     *
     * @param config The configuration
     * @return The {@link QueryStringParser} using the specified configuration
     */
    static QueryStringParser withConfig(ParserConfiguration config) {
        return new QueryStringParserImpl(config);
    }

    /**
     * <p>Build a parser with default configuration: {@link ParserConfiguration#DEFAULT}</p>
     * <p>This allow to customize the querystring parameters name for pagination and sorting</p>
     *
     * @return The {@link QueryStringParser} using the default configuration
     * @see ParserConfiguration#DEFAULT
     */
    static QueryStringParser withDefaultConfig() {
        return new QueryStringParserImpl(ParserConfiguration.DEFAULT);
    }
}
