package fr.ght1pc9kc.juery.api.filter;

public interface CriteriaVisitor<R> {
    R visitNoCriteria(NoCriterion none);

    R visitAnd(AndOperation operation);

    R visitNot(NotOperation operation);

    R visitOr(OrOperation operation);

    <T> R visitEqual(EqualOperation<T> operation);

    <T> R visitGreaterThan(GreaterThanOperation<T> operation);

    default <T> R visitGreaterThanEquals(GreaterThanEqualsOperation<T> operation) {
        throw new IllegalStateException("START_WITH operation not implemented in visitor");
    }

    <T> R visitLowerThan(LowerThanOperation<T> operation);

    default <T> R visitLowerThanEquals(LowerThanEqualsOperation<T> operation) {
        throw new IllegalStateException("START_WITH operation not implemented in visitor");
    }

    default <T> R visitStartWith(StartWithOperation<T> operation) {
        throw new IllegalStateException("START_WITH operation not implemented in visitor");
    }

    default <T> R visitEndWith(EndWithOperation<T> operation) {
        throw new IllegalStateException("END_WITH operation not implemented in visitor");
    }

    default <T> R visitContains(ContainsOperation<T> operation) {
        throw new IllegalStateException("CONTAINS operation not implemented in visitor");
    }

    default <T> R visitIn(InOperation<T> operation) {
        throw new IllegalStateException("IN operation not implemented in visitor");
    }

    default <T> R visitValue(CriterionValue<T> value) {
        throw new IllegalStateException("Value not implemented in visitor");
    }
}
