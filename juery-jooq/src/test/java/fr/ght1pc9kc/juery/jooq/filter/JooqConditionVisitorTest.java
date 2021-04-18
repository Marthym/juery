package fr.ght1pc9kc.juery.jooq.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import org.jooq.Condition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Stream;

import static fr.ght1pc9kc.juery.jooq.database.SchemaSample.*;
import static org.assertj.core.api.Assertions.assertThat;

class JooqConditionVisitorTest {

    private static final LocalDateTime NOW = LocalDateTime.parse("2020-12-11T10:20:42");

    private final JooqConditionVisitor tested = new JooqConditionVisitor(Map.of(

    ));

    @ParameterizedTest
    @MethodSource("provideSCriteria")
    void should_create_condition_from_criteria(Criteria criteria, Condition expected) {
        Condition actual = criteria.visit(tested);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideSCriteria() {
        return Stream.of(
                Arguments.of(Criteria.property("title").eq("Obiwan")
                                .and(Criteria.property("publication").gt(NOW)
                                        .or(Criteria.property("publication").lt(NOW))),
                        NEWS_TITLE.eq("Obiwan").and(
                                NEWS_PUBLICATION.gt(LocalDateTime.parse("2020-12-11T10:20:42"))
                                        .or(NEWS_PUBLICATION.lt(LocalDateTime.parse("2020-12-11T10:20:42"))))),
                Arguments.of(Criteria.property("id").in("1", "2", "3", "4", "42"),
                        NEWS_ID.in("1", "2", "3", "4", "42"))
        );
    }
}