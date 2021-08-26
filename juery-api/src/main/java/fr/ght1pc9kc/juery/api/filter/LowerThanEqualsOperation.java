package fr.ght1pc9kc.juery.api.filter;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class LowerThanEqualsOperation<T> extends BiOperand<T> {
    public LowerThanEqualsOperation(CriterionProperty criteria, CriterionValue<T> value) {
        super(criteria, value);
    }

    @Override
    public <R> R accept(CriteriaVisitor<R> visitor) {
        return visitor.visitLowerThanEquals(this);
    }
}
