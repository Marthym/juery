package fr.ght1pc9kc.juery.api.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unchecked")
class CriterionValueTest {
    @Test
    void should_have_null_criteria() {
        Criteria actual = Criteria.property("sith").isNull();
        Assertions.assertThat(actual).isInstanceOf(EqualOperation.class);
        Assertions.assertThat(((EqualOperation<Void>) actual).value.isNull()).isTrue();
    }

    @Test
    void should_have_empty_criteria() {
        Criteria actual = Criteria.property("c3po").eq("");
        Assertions.assertThat(actual).isInstanceOf(EqualOperation.class);
        Assertions.assertThat(((EqualOperation<Void>) actual).value.isEmpty()).isTrue();
    }
}