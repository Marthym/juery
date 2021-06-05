package fr.ght1pc9kc.juery.api.filter;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class GreaterThanOperation<T> extends BiOperand<T> {
    public GreaterThanOperation(CriterionProperty criteria, CriterionValue<T> value) {
        super(criteria, value);
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitGreaterThan(this);
    }
}
