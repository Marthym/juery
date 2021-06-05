package fr.ght1pc9kc.juery.api;

import fr.ght1pc9kc.juery.api.filter.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The main class to use <strong>Juery</strong>
 *
 * Some examples:
 *
 * <pre>{@code
 * Criteria.property("jedi").eq("Obiwan")
 *     .and(Criteria.property("age").gt(40)
 *     .or(Criteria.property("age").lt(20)));
 * }</pre>
 */
public interface Criteria {

    static NoCriterion none() {
        return NoCriterion.NO_CRITERION;
    }

    static Criteria and(Criteria... andCriteria) {
        List<Criteria> filtered = Arrays.stream(andCriteria)
                .filter(Predicate.not(Criteria::isEmpty))
                .flatMap(a -> {
                    if (a instanceof AndOperation) {
                        return ((AndOperation) a).andCriteria.stream();
                    } else {
                        return Stream.of(a);
                    }
                })
                .distinct()
                .collect(Collectors.toUnmodifiableList());
        if (filtered.isEmpty()) {
            return Criteria.none();
        } else if (filtered.size() == 1) {
            return filtered.iterator().next();
        }
        return new AndOperation(filtered);
    }

    static Criteria or(Criteria... orCriteria) {
        List<Criteria> filtered = Arrays.stream(orCriteria)
                .filter(Predicate.not(Criteria::isEmpty))
                .flatMap(a -> {
                    if (a instanceof OrOperation) {
                        return ((OrOperation) a).orCriteria.stream();
                    } else {
                        return Stream.of(a);
                    }
                })
                .distinct()
                .collect(Collectors.toUnmodifiableList());
        if (filtered.isEmpty()) {
            return Criteria.none();
        } else if (filtered.size() == 1) {
            return filtered.iterator().next();
        }
        return new OrOperation(filtered);
    }

    static Criteria not(Criteria criteria) {
        if (criteria.isEmpty()) {
            return criteria;
        }
        if (criteria instanceof NotOperation) {
            return ((NotOperation) criteria).negative;
        }
        return new NotOperation(criteria);
    }

    static CriterionProperty property(String property) {
        return new CriterionProperty(property);
    }

    default Criteria and(Criteria right) {
        return and(this, right);
    }

    default Criteria or(Criteria right) {
        return or(this, right);
    }

    boolean isEmpty();

    <R> R accept(CriteriaVisitor<R> visitor);
}
