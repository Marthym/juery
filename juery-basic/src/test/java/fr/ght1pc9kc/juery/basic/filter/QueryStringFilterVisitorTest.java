package fr.ght1pc9kc.juery.basic.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QueryStringFilterVisitorTest {
    public QueryStringFilterVisitor tested = new QueryStringFilterVisitor();

    @Test
    void should_consume_criteria_as_string() {
        assertThat(Criteria.property("name").eq("Obiwan")
                .and(Criteria.property("faction").eq("jedi")).visit(tested)).isEqualTo("name=Obiwan&faction=jedi");
        assertThat(Criteria.none().visit(tested)).isEmpty();
    }

    @Test
    void should_fail_on_illegal() {
        Criteria obiwan = Criteria.property("name").eq("Obiwan");
        Criteria yoda = Criteria.property("name").eq("Yoda");
        Criteria obiwanOrYoda = obiwan.or(yoda);
        Criteria nameInObiwanYoda = Criteria.property("name").in("Obiwan", "Yoda");
        Criteria notObiwan = Criteria.not(obiwan);
        Criteria lowerThan = Criteria.property("age").lt(57);
        Criteria greaterThan = Criteria.property("age").gt(57);

        assertThatThrownBy(() -> obiwanOrYoda.visit(tested)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> nameInObiwanYoda.visit(tested)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> notObiwan.visit(tested)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> lowerThan.visit(tested)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> greaterThan.visit(tested)).isInstanceOf(IllegalStateException.class);
    }
}