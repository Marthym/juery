package fr.ght1pc9kc.juery.basic;

import fr.ght1pc9kc.juery.api.Criteria;
import fr.ght1pc9kc.juery.api.PageRequest;
import fr.ght1pc9kc.juery.api.pagination.Direction;
import fr.ght1pc9kc.juery.api.pagination.Order;
import fr.ght1pc9kc.juery.api.pagination.Sort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

class PageRequestFormatterTest {
    @MethodSource
    @ParameterizedTest
    void should_parse_page_request_from_map(Map<String, String> queryString, PageRequest expected) {
        PageRequest actual = PageRequestFormatter.parse(queryString);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> should_parse_page_request_from_map() {
        return Stream.of(
                Arguments.of(
                        Map.of(
                                "_p", "2",
                                "_s", "name, -email",
                                "profile", "jedi",
                                "job", "master"),
                        PageRequest.builder()
                                .page(2).size(100)
                                .filter(Criteria.property("job").eq("master").and(Criteria.property("profile").eq("jedi")))
                                .sort(Sort.of(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "email")))
                                .build()),
                Arguments.of(
                        Map.of("_pp", "200"),
                        PageRequest.builder()
                                .page(0).size(100)
                                .filter(Criteria.none())
                                .sort(Sort.of())
                                .build()),
                Arguments.of(
                        Map.of(
                                "_pp", "200",
                                "name", ""),
                        PageRequest.builder()
                                .page(0).size(100)
                                .filter(Criteria.property("name").eq(true))
                                .sort(Sort.of())
                                .build())
        );
    }

    @MethodSource
    @ParameterizedTest
    void should_parse_sort_parameter(String sortValue, Sort expected) {
        Sort actual = PageRequestFormatter.parseSortParameter(sortValue);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> should_parse_sort_parameter() {
        return Stream.of(
                Arguments.of("+name,-mail,+karma", Sort.of(Order.asc("name"), Order.desc("mail"), Order.asc("karma"))),
                Arguments.of("name,mail,karma", Sort.of(Order.asc("name"), Order.asc("mail"), Order.asc("karma"))),
                Arguments.of("+name,,+karma", Sort.of(Order.asc("name"), Order.asc("karma"))),
                Arguments.of(",+name,+karma,", Sort.of(Order.asc("name"), Order.asc("karma"))),
                Arguments.of("", Sort.of()),
                Arguments.of("-", Sort.of()),
                Arguments.of("   ", Sort.of()),
                Arguments.of(",,,", Sort.of())
        );
    }

    @MethodSource
    @ParameterizedTest
    void should_parse_page_request_from_string(String qs, PageRequest expected) {
        PageRequest actual = PageRequestFormatter.parse(qs);

        Assertions.assertThat(actual.sort).isEqualTo(expected.sort);
        Assertions.assertThat(actual.filter).isEqualTo(expected.filter);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> should_parse_page_request_from_string() {
        return Stream.of(
                Arguments.of(
                        "_p=2&_s=name,-email&profile=jedi&job=master",
                        PageRequest.builder()
                                .page(2).size(100)
                                .filter(Criteria.property("job").eq("master").and(Criteria.property("profile").eq("jedi")))
                                .sort(Sort.of(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "email")))
                                .build()),
                Arguments.of(
                        "",
                        PageRequest.builder()
                                .page(-1).size(-1)
                                .filter(Criteria.none())
                                .sort(Sort.of())
                                .build()),
                Arguments.of(
                        "name",
                        PageRequest.builder()
                                .page(0).size(100)
                                .filter(Criteria.property("name").eq(true))
                                .sort(Sort.of())
                                .build())
        );
    }

    @MethodSource
    @ParameterizedTest
    void should_format_page_request_to_query_string(String expected, PageRequest pr) {
        String actual = PageRequestFormatter.formatPageRequest(pr);

        Assertions.assertThat(assertableQueryString(actual))
                .isEqualTo(assertableQueryString(expected));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> should_format_page_request_to_query_string() {
        return Stream.of(
                Arguments.of(
                        "_p=2&_s=name,-email&profile=jedi&job=master",
                        PageRequest.builder()
                                .page(2).size(100)
                                .filter(Criteria.property("profile").eq("jedi").and(Criteria.property("job").eq("master")))
                                .sort(Sort.of(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "email")))
                                .build()),
                Arguments.of(
                        "",
                        PageRequest.builder()
                                .page(0).size(100)
                                .filter(Criteria.none())
                                .sort(Sort.of())
                                .build()),
                Arguments.of(
                        "name=true",
                        PageRequest.builder()
                                .page(0).size(100)
                                .filter(Criteria.property("name").eq(true))
                                .sort(Sort.of())
                                .build())
        );
    }

    public static Set<String> assertableQueryString(String queryString) {
        return Set.of(queryString.split("&"));
    }
}