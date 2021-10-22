package fr.ght1pc9kc.juery.basic.parser;

import fr.ght1pc9kc.juery.api.Criteria;
import fr.ght1pc9kc.juery.api.PageRequest;
import fr.ght1pc9kc.juery.api.Pagination;
import fr.ght1pc9kc.juery.api.pagination.Direction;
import fr.ght1pc9kc.juery.api.pagination.Order;
import fr.ght1pc9kc.juery.api.pagination.Sort;
import fr.ght1pc9kc.juery.basic.QueryStringParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

class QueryStringParserImplDefaultTest {
    private final QueryStringParser tested = QueryStringParser._default();

    @MethodSource
    @ParameterizedTest
    void should_parse_page_request_from_map(Map<String, List<String>> queryString, PageRequest expected) {
        PageRequest actual = tested.parse(queryString);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> should_parse_page_request_from_map() {
        return Stream.of(
                Arguments.of(
                        Map.of(
                                "_p", List.of("2"),
                                "_s", List.of("name, -email"),
                                "profile", List.of("jedi"),
                                "job", List.of("master"),
                                "firstname", List.of("^Obiwan"),
                                "lastname", List.of("$Kenobi"),
                                "name", List.of("∋biwan"),
                                "birthday", List.of("<2021-09-12T12:42:55")
                        ),
                        PageRequest.of(
                                Pagination.of(200, 100,
                                        Sort.of(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "email"))),
                                Criteria.property("job").eq("master").and(Criteria.property("profile").eq("jedi"))
                                        .and(Criteria.property("firstname").startWith("Obiwan"))
                                        .and(Criteria.property("lastname").endWith("Kenobi"))
                                        .and(Criteria.property("name").contains("biwan"))
                                        .and(Criteria.property("birthday").lt(LocalDateTime.parse("2021-09-12T12:42:55"))))),
                Arguments.of(
                        Map.of("_pp", List.of("200")),
                        PageRequest.of(
                                Pagination.of(1, 100, Sort.of()),
                                Criteria.none())),
                Arguments.of(
                        Map.of(
                                "_pp", List.of("200"),
                                "name", List.of("")),
                        PageRequest.of(
                                Pagination.of(1, 100, Sort.of()),
                                Criteria.property("name").eq(true))),
                Arguments.of(
                        Map.of(
                                "_pp", List.of("200"),
                                "name", List.of()),
                        PageRequest.of(
                                Pagination.of(1, 100, Sort.of()),
                                Criteria.property("name").eq(true))),
                Arguments.of(
                        Map.of("_s", List.of("name", "-email")),
                        PageRequest.of(
                                Pagination.of(1, 100,
                                        Sort.of(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "email"))),
                                Criteria.none())),
                Arguments.of(
                        Map.of(
                                "_from", List.of("101"),
                                "_s", List.of("name, -email"),
                                "profile", List.of("jedi"),
                                "job", List.of("master"),
                                "firstname", List.of("^Obiwan"),
                                "lastname", List.of("$Kenobi"),
                                "name", List.of("∋biwan")
                        ),
                        PageRequest.of(
                                Pagination.of(101, 100,
                                        Sort.of(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "email"))),
                                Criteria.property("job").eq("master").and(Criteria.property("profile").eq("jedi"))
                                        .and(Criteria.property("firstname").startWith("Obiwan"))
                                        .and(Criteria.property("lastname").endWith("Kenobi"))
                                        .and(Criteria.property("name").contains("biwan")))),
                Arguments.of(
                        Map.of(
                                "_from", List.of("11"),
                                "_to", List.of("21"),
                                "_s", List.of("name", "-email")
                        ),
                        PageRequest.of(
                                Pagination.of(11, 10,
                                        Sort.of(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "email"))),
                                Criteria.none()))
        );
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
        PageRequest actual = tested.parse(qs);

        Assertions.assertThat(actual.pagination().sort()).isEqualTo(expected.pagination().sort());
        Assertions.assertThat(actual.filter()).isEqualTo(expected.filter());
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> should_parse_page_request_from_string() {
        return Stream.of(
                Arguments.of(
                        "_p=2&_s=name,-email&profile=jedi&job=master",
                        PageRequest.of(
                                Pagination.of(200, 100, Sort.of(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "email"))),
                                Criteria.property("job").eq("master").and(Criteria.property("profile").eq("jedi")))),
                Arguments.of("", PageRequest.all()),
                Arguments.of(
                        "name",
                        PageRequest.of(
                                Pagination.of(1, 100),
                                Criteria.property("name").eq(true))),
                Arguments.of("name=%5EObiwan", PageRequest.of(
                        Pagination.of(1, 100),
                        Criteria.property("name").startWith("Obiwan"))),
                Arguments.of("name=%E2%88%8Bbiwa", PageRequest.of(
                        Pagination.of(1, 100),
                        Criteria.property("name").contains("biwa"))),
                Arguments.of("id[]=42&id[]=24&name=%5EObiwan", PageRequest.of(
                        Pagination.of(1, 100),
                        Criteria.property("id").in(42, 24)
                                .and(Criteria.property("name").startWith("Obiwan")))),
                Arguments.of("id=<42&name=%3CObiwan", PageRequest.of(
                        Pagination.of(1, 100),
                        Criteria.property("id").lt(42)
                                .and(Criteria.property("name").lt("Obiwan")))),
                Arguments.of("id=>42&name=%3EObiwan", PageRequest.of(
                        Pagination.of(1, 100),
                        Criteria.property("id").gt(42)
                                .and(Criteria.property("name").gt("Obiwan")))),
                Arguments.of("id=≤42&name=%E2%89%A4Obiwan", PageRequest.of(
                        Pagination.of(1, 100),
                        Criteria.property("id").lte(42)
                                .and(Criteria.property("name").lte("Obiwan")))),
                Arguments.of("id=≥42&name=%E2%89%A5Obiwan", PageRequest.of(
                        Pagination.of(1, 100),
                        Criteria.property("id").gte(42)
                                .and(Criteria.property("name").gte("Obiwan"))))
        );
    }

    @MethodSource
    @ParameterizedTest
    void should_format_page_request_to_query_string(String expected, PageRequest pr) {
        String actual = tested.format(pr);

        Assertions.assertThat(assertableQueryString(actual))
                .isEqualTo(assertableQueryString(expected));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> should_format_page_request_to_query_string() {
        return Stream.of(
                Arguments.of("_from=2&_s=name,-email&profile=jedi&job=master",
                        PageRequest.of(
                                Pagination.of(2, 100, Sort.of(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "email"))),
                                Criteria.property("profile").eq("jedi").and(Criteria.property("job").eq("master")))),
                Arguments.of("", PageRequest.of(Pagination.of(0, 100), Criteria.none())),
                Arguments.of("name=true", PageRequest.of(Pagination.of(0, 100),
                        Criteria.property("name").eq(true))),
                Arguments.of("id=42&id=24&name=true", PageRequest.of(Pagination.of(0, 100),
                        Criteria.property("id").in(42, 24)
                                .and(Criteria.property("name").eq(true))))
        );
    }

    public static Set<String> assertableQueryString(String queryString) {
        return Set.of(queryString.split("&"));
    }
}