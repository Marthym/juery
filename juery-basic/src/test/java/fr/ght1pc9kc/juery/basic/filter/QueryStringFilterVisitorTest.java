package fr.ght1pc9kc.juery.basic.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QueryStringFilterVisitorTest {
    public QueryStringFilterVisitor tested = new QueryStringFilterVisitor();

    @Test
    void should_consume_criteria_as_string() {
        Assertions.assertAll(
                () -> assertThat(Criteria.property("name").eq("Obiwan")
                        .and(Criteria.property("faction").eq("jedi")).accept(tested)).isEqualTo("name=Obiwan&faction=jedi"),
                () -> assertThat(Criteria.none().accept(tested)).isEmpty(),
                () -> assertThat(Criteria.property("name").startWith("Obiwan")
                        .and(Criteria.property("faction").endWith("jedi")).accept(tested)).isEqualTo("name=%5EObiwan&faction=%24jedi"),
                () -> assertThat(Criteria.property("name").contains("Obiwan").accept(tested)).isEqualTo("name=%E2%88%8BObiwan"),
                () -> assertThat(Criteria.property("id").in(42, 24).accept(tested)).isEqualTo("id=42&id=24"),
                () -> assertThat(Criteria.property("id").lt(42).accept(tested)).isEqualTo("id=%3C42"),
                () -> assertThat(Criteria.property("id").gt(42).accept(tested)).isEqualTo("id=%3E42"),
                () -> assertThat(Criteria.property("id").lte(42).accept(tested)).isEqualTo("id=%E2%89%A442"),
                () -> assertThat(Criteria.property("id").gte(42).accept(tested)).isEqualTo("id=%E2%89%A542"),
                () -> assertThat(Criteria.none().accept(tested)).isEmpty()
        );
    }

    @Test
    void should_fail_on_illegal() {
        Criteria obiwan = Criteria.property("name").eq("Obiwan");
        Criteria yoda = Criteria.property("name").eq("Yoda");
        Criteria obiwanOrYoda = obiwan.or(yoda);
        Criteria notObiwan = Criteria.not(obiwan);

        assertThatThrownBy(() -> obiwanOrYoda.accept(tested)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> notObiwan.accept(tested)).isInstanceOf(IllegalStateException.class);
    }
}