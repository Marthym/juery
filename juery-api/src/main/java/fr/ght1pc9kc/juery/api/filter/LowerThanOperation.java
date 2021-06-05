package fr.ght1pc9kc.juery.api.filter;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class LowerThanOperation<T> extends BiOperand<T> {
    public LowerThanOperation(CriterionProperty criteria, CriterionValue<T> value) {
        super(criteria, value);
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitLowerThan(this);
    }
}
