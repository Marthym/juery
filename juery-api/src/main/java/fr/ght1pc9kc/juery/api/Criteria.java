package fr.ght1pc9kc.juery.api;

import fr.ght1pc9kc.juery.api.filter.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    <R> R visit(Visitor<R> visitor);

    interface Visitor<R> {
        R visitNoCriteria(NoCriterion none);

        R visitAnd(AndOperation operation);

        R visitNot(NotOperation operation);

        R visitOr(OrOperation operation);

        <T> R visitEqual(EqualOperation<T> operation);

        <T> R visitGreaterThan(GreaterThanOperation<T> operation);

        <T> R visitLowerThan(LowerThanOperation<T> operation);

        default <T> R visitStartWith(StartWithOperation<T> operation) {
            throw new IllegalStateException("START_WITH operation not implemented in visitor");
        }

        default <T> R visitEndWith(EndWithOperation<T> operation) {
            throw new IllegalStateException("END_WITH operation not implemented in visitor");
        }

        default <T> R visitIn(InOperation<T> operation) {
            throw new IllegalStateException("IN operation not implemented in visitor");
        }

        default <T> R visitValue(CriterionValue<T> value) {
            throw new IllegalStateException("Value not implemented in visitor");
        }
    }
}
