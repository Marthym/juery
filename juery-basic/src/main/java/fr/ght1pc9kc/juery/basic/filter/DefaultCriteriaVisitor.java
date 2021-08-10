package fr.ght1pc9kc.juery.basic.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import fr.ght1pc9kc.juery.api.filter.*;

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
        return operation;
    }

    @Override
    public Criteria visitNot(NotOperation operation) {
        return operation;
    }

    @Override
    public Criteria visitOr(OrOperation operation) {
        return operation;
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
    public <T> Criteria visitLowerThan(LowerThanOperation<T> operation) {
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
