package fr.ght1pc9kc.juery.api;

import fr.ght1pc9kc.juery.api.filter.AndOperation;
import fr.ght1pc9kc.juery.api.filter.EndWithOperation;
import fr.ght1pc9kc.juery.api.filter.OrOperation;
import fr.ght1pc9kc.juery.api.filter.StartWithOperation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CriteriaTest {
    @Test
    void should_and_none_criteria() {
        {
            Criteria expected = Criteria.property("obiwan").eq("kenobi");
            Criteria actual = Criteria.and(Criteria.none(), expected);

            Assertions.assertThat(actual).isEqualTo(expected);
        }
        {
            Criteria expected = Criteria.none();
            Criteria actual = Criteria.and(Criteria.none(), expected);

            Assertions.assertThat(actual).isEqualTo(expected);
        }
    }

    @Test
    void should_or_none_criteria() {
        {
            Criteria expected = Criteria.property("size").gt(42);
            Criteria actual = Criteria.or(Criteria.none(), expected);

            Assertions.assertThat(actual).isEqualTo(expected);
        }
        {
            Criteria expected = Criteria.none();
            Criteria actual = Criteria.or(Criteria.none(), expected);

            Assertions.assertThat(actual).isEqualTo(expected);
        }
    }

    @Test
    void should_not_none_criteria() {
        Criteria actual = Criteria.not(Criteria.none());
        Assertions.assertThat(actual).isEqualTo(Criteria.none());
    }

    @Test
    void should_and_multiple_criteria() {
        {
            Criteria actual = Criteria.and();
            Assertions.assertThat(actual).isEqualTo(Criteria.none());
        }
        {
            Criteria actual = Criteria.and(
                    Criteria.property("obiwan").eq("kenobi"),
                    Criteria.property("luke").eq("skywalker"),
                    Criteria.property("obiwan").eq("kenobi"),
                    Criteria.property("leia").in("jedi", "politician")
            );
            Assertions.assertThat(actual).isInstanceOf(AndOperation.class);
            Assertions.assertThat(((AndOperation) actual).andCriteria).containsExactly(
                    Criteria.property("obiwan").eq("kenobi"),
                    Criteria.property("luke").eq("skywalker"),
                    Criteria.property("leia").in("jedi", "politician")
            );
        }
    }

    @Test
    void should_or_multiple_criteria() {
        {
            Criteria actual = Criteria.or();
            Assertions.assertThat(actual).isEqualTo(Criteria.none());
        }
        {
            Criteria actual = Criteria.or(
                    Criteria.property("obiwan").eq("kenobi"),
                    Criteria.not(Criteria.property("luke").eq("skywalker")),
                    Criteria.property("obiwan").eq("kenobi"),
                    Criteria.property("leia").eq("organa")
            );
            Assertions.assertThat(actual).isInstanceOf(OrOperation.class);
            Assertions.assertThat(((OrOperation) actual).orCriteria).containsExactly(
                    Criteria.property("obiwan").eq("kenobi"),
                    Criteria.not(Criteria.property("luke").eq("skywalker")),
                    Criteria.property("leia").eq("organa")
            );
        }
    }

    @Test
    void should_reverse_equal_and() {
        Criteria left = Criteria.property("name").eq("obiwan");
        Criteria right = Criteria.property("force").eq("light");

        Assertions.assertThat(left.and(right)).isEqualTo(right.and(left));
    }

    @Test
    void should_reverse_equal_or() {
        Criteria left = Criteria.property("name").eq("obiwan");
        Criteria right = Criteria.property("age").lt(57);

        Assertions.assertThat(left.or(right)).isEqualTo(right.or(left));
    }

    @Test
    void should_ignore_double_negative() {
        Criteria expected = Criteria.property("name").eq("obiwan");
        Criteria actual = Criteria.not(Criteria.not(expected));

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void should_flatten_or_operation() {
        Criteria actual = Criteria.or(
                Criteria.or(Criteria.property("name").eq("Obiwan"), Criteria.property("lastname").eq("Kenobi")),
                Criteria.or(Criteria.property("name").eq("Luke"), Criteria.property("lastname").eq("Skywalker"))
        );

        Assertions.assertThat(actual).isEqualTo(Criteria.or(
                Criteria.property("name").eq("Obiwan"),
                Criteria.property("lastname").eq("Kenobi"),
                Criteria.property("name").eq("Luke"),
                Criteria.property("lastname").eq("Skywalker")
        ));
    }

    @Test
    void should_flatten_and_operation() {
        Criteria actual = Criteria.and(
                Criteria.and(Criteria.property("name").eq("Obiwan"), Criteria.property("lastname").eq("Kenobi")),
                Criteria.and(Criteria.property("faction").eq("jedi"), Criteria.property("age").lt(57))
        );

        Assertions.assertThat(actual).isEqualTo(Criteria.and(
                Criteria.property("name").eq("Obiwan"),
                Criteria.property("lastname").eq("Kenobi"),
                Criteria.property("faction").eq("jedi"),
                Criteria.property("age").lt(57)
        ));
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_create_startend_with_operation() {
        Criteria actualStartWith = Criteria.property("name").startWith("Obiwan");
        Assertions.assertThat(actualStartWith).isInstanceOf(StartWithOperation.class);
        Assertions.assertThat(((StartWithOperation<String>) actualStartWith).value.value).isEqualTo("Obiwan");

        Criteria actualEndWith = Criteria.property("name").endWith("Kenobi");
        Assertions.assertThat(actualEndWith).isInstanceOf(EndWithOperation.class);
        Assertions.assertThat(((EndWithOperation<String>) actualEndWith).value.value).isEqualTo("Kenobi");
    }
}