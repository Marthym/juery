package fr.ght1pc9kc.juery.basic.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import fr.ght1pc9kc.juery.api.filter.AndOperation;
import fr.ght1pc9kc.juery.api.filter.ContainsOperation;
import fr.ght1pc9kc.juery.api.filter.CriteriaVisitor;
import fr.ght1pc9kc.juery.api.filter.CriterionValue;
import fr.ght1pc9kc.juery.api.filter.EndWithOperation;
import fr.ght1pc9kc.juery.api.filter.EqualOperation;
import fr.ght1pc9kc.juery.api.filter.GreaterThanEqualsOperation;
import fr.ght1pc9kc.juery.api.filter.GreaterThanOperation;
import fr.ght1pc9kc.juery.api.filter.InOperation;
import fr.ght1pc9kc.juery.api.filter.LowerThanEqualsOperation;
import fr.ght1pc9kc.juery.api.filter.LowerThanOperation;
import fr.ght1pc9kc.juery.api.filter.NoCriterion;
import fr.ght1pc9kc.juery.api.filter.NotOperation;
import fr.ght1pc9kc.juery.api.filter.OrOperation;
import fr.ght1pc9kc.juery.api.filter.StartWithOperation;

/**
 * The Visitor do only <b>nothing</b>. Extend it to apply some modifications on
 * {@link Criteria} without need to implement all the interface.
 */
public class DefaultCriteriaVisitor implements CriteriaVisitor<Criteria> {
    @Override
    public Criteria visitNoCriteria(NoCriterion none) {
        return none;
    }

    @Override
    public Criteria visitAnd(AndOperation operation) {
        return Criteria.and(operation.andCriteria().stream()
                .map(c -> c.accept(this))
                .toArray(Criteria[]::new));
    }

    @Override
    public Criteria visitNot(NotOperation operation) {
        Criteria accepted = operation.negative().accept(this);
        return (accepted == operation.negative()) ? operation : Criteria.not(accepted);
    }

    @Override
    public Criteria visitOr(OrOperation operation) {
        return Criteria.or(operation.orCriteria().stream()
                .map(c -> c.accept(this))
                .toArray(Criteria[]::new));
    }

    @Override
    public <T> Criteria visitEqual(EqualOperation<T> operation) {
        return operation;
    }

    @Override
    public <T> Criteria visitGreaterThan(GreaterThanOperation<T> operation) {
        return operation;
    }

    @Override
    public <T> Criteria visitGreaterThanEquals(GreaterThanEqualsOperation<T> operation) {
        return operation;
    }

    @Override
    public <T> Criteria visitLowerThan(LowerThanOperation<T> operation) {
        return operation;
    }

    @Override
    public <T> Criteria visitLowerThanEquals(LowerThanEqualsOperation<T> operation) {
        return operation;
    }

    @Override
    public <T> Criteria visitStartWith(StartWithOperation<T> operation) {
        return operation;
    }

    @Override
    public <T> Criteria visitEndWith(EndWithOperation<T> operation) {
        return operation;
    }

    @Override
    public <T> Criteria visitContains(ContainsOperation<T> operation) {
        return operation;
    }

    @Override
    public <T> Criteria visitIn(InOperation<T> operation) {
        return operation;
    }

    @Override
    public <T> Criteria visitValue(CriterionValue<T> value) {
        return value;
    }
}
