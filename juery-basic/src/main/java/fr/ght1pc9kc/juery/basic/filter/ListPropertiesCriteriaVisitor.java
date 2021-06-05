package fr.ght1pc9kc.juery.basic.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import fr.ght1pc9kc.juery.api.filter.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This visitor extract an immutable list of the properties contained in the {@link Criteria} object.
 * <p>
 * For example the criteria : {@code id = 42 and name in ('obiwan', 'yoda')} will return
 * {@code List<String>['id', 'name']}.
 * <p>
 * Useful to check allowed filters.
 */
public class ListPropertiesCriteriaVisitor implements CriteriaVisitor<List<String>> {
    @Override
    public List<String> visitNoCriteria(NoCriterion none) {
        return List.of();
    }

    @Override
    public List<String> visitAnd(AndOperation operation) {
        return operation.andCriteria.stream()
                .flatMap(a -> a.accept(this).stream())
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<String> visitNot(NotOperation operation) {
        return operation.negative.accept(this);
    }

    @Override
    public List<String> visitOr(OrOperation operation) {
        return operation.orCriteria.stream()
                .flatMap(a -> a.accept(this).stream())
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public <T> List<String> visitEqual(EqualOperation<T> operation) {
        return List.of(operation.field.property);
    }

    @Override
    public <T> List<String> visitIn(InOperation<T> operation) {
        return List.of(operation.field.property);
    }

    @Override
    public <T> List<String> visitGreaterThan(GreaterThanOperation<T> operation) {
        return List.of(operation.field.property);
    }

    @Override
    public <T> List<String> visitLowerThan(LowerThanOperation<T> operation) {
        return List.of(operation.field.property);
    }

    @Override
    public <T> List<String> visitStartWith(StartWithOperation<T> operation) {
        return List.of(operation.field.property);
    }

    @Override
    public <T> List<String> visitEndWith(EndWithOperation<T> operation) {
        return List.of(operation.field.property);
    }

    @Override
    public <T> List<String> visitContains(ContainsOperation<T> operation) {
        return List.of(operation.field.property);
    }
}
