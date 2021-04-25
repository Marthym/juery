package fr.ght1pc9kc.juery.basic.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ListPropertiesCriteriaVisitorTest {

    private final ListPropertiesCriteriaVisitor tested = new ListPropertiesCriteriaVisitor();

    @ParameterizedTest
    @MethodSource(value = "provideSCriteria")
    void should_consume_criteria_as_string(Criteria criteria, List<String> expected) {
        List<String> actual = criteria.visit(tested);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideSCriteria() {
        return Stream.of(
                Arguments.of(Criteria.property("jedi").eq("Obiwan")
                                .and(Criteria.property("age").gt(40).or(Criteria.property("age").lt(20))),
                        List.of("jedi", "age")),
                Arguments.of(Criteria.property("jedi").in("Obiwan", "Anakin", "Luke")
                                .and(Criteria.property("age").gt(40).or(Criteria.property("age").lt(20))),
                        List.of("jedi", "age")),
                Arguments.of(Criteria.not(Criteria.property("faction").eq("sith"))
                                .and(Criteria.property("age").gt(40))
                                .or(Criteria.property("age").lt(20)),
                        List.of("faction", "age")),
                Arguments.of(Criteria.none(), List.of())
        );
    }
}