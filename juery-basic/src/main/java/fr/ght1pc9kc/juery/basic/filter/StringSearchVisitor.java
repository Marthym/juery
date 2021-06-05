package fr.ght1pc9kc.juery.basic.filter;

import fr.ght1pc9kc.juery.api.filter.*;

import java.util.stream.Collectors;

public class StringSearchVisitor implements CriteriaVisitor<String> {
    @Override
    public String visitNoCriteria(NoCriterion none) {
        return "";
    }

    @Override
    public String visitAnd(AndOperation operation) {
        return operation.andCriteria.stream()
                .map(a -> a.accept(this))
                .collect(Collectors.joining(" and ", "(", ")"));
    }

    @Override
    public String visitNot(NotOperation operation) {
        return "not (" + operation.negative.accept(this) + ")";
    }

    @Override
    public String visitOr(OrOperation operation) {
        return operation.orCriteria.stream()
                .map(a -> a.accept(this))
                .collect(Collectors.joining(" or ", "(", ")"));
    }

    @Override
    public <T> String visitEqual(EqualOperation<T> operation) {
        return operation.field.property + " = '" + operation.value.accept(this) + "'";
    }

    @Override
    public <T> String visitStartWith(StartWithOperation<T> operation) {
        return operation.field.property + " =~ '^" + operation.value.accept(this) + ".*'";
    }

    @Override
    public <T> String visitEndWith(EndWithOperation<T> operation) {
        return operation.field.property + " =~ '.*" + operation.value.accept(this) + "$'";
    }

    @Override
    public <T> String visitContains(ContainsOperation<T> operation) {
        return operation.field.property + " =~ '.*" + operation.value.accept(this) + ".*'";
    }

    @Override
    public <T> String visitIn(InOperation<T> operation) {
        return operation.field.property + " in " + operation.value.accept(this);
    }

    @Override
    public <T> String visitGreaterThan(GreaterThanOperation<T> operation) {
        return operation.field.property + " > " + operation.value.accept(this);
    }

    @Override
    public <T> String visitLowerThan(LowerThanOperation<T> operation) {
        return operation.field.property + " < " + operation.value.accept(this);
    }

    @Override
    public <T> String visitValue(CriterionValue<T> value) {
        return value.value.toString();
    }
}
