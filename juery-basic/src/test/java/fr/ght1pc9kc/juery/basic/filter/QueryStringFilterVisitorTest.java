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
                .and(Criteria.property("faction").eq("jedi")).accept(tested)).isEqualTo("name=Obiwan&faction=jedi");
        assertThat(Criteria.none().accept(tested)).isEmpty();
        assertThat(Criteria.property("name").startWith("Obiwan")
                .and(Criteria.property("faction").endWith("jedi")).accept(tested)).isEqualTo("name=%5EObiwan&faction=%24jedi");
        assertThat(Criteria.property("name").contains("Obiwan").accept(tested)).isEqualTo("name=%E2%88%8BObiwan");
        assertThat(Criteria.none().accept(tested)).isEmpty();
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

        assertThatThrownBy(() -> obiwanOrYoda.accept(tested)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> nameInObiwanYoda.accept(tested)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> notObiwan.accept(tested)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> lowerThan.accept(tested)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> greaterThan.accept(tested)).isInstanceOf(IllegalStateException.class);
    }
}