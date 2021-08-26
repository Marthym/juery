package fr.ght1pc9kc.juery.api.filter;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class GreaterThanEqualsOperation<T> extends BiOperand<T> {
    public GreaterThanEqualsOperation(CriterionProperty criteria, CriterionValue<T> value) {
        super(criteria, value);
    }

    @Override
    public <R> R accept(CriteriaVisitor<R> visitor) {
        return visitor.visitGreaterThanEquals(this);
    }
}
