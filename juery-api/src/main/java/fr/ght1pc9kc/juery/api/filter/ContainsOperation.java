package fr.ght1pc9kc.juery.api.filter;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ContainsOperation<T> extends BiOperand<T> {
    public ContainsOperation(CriterionProperty criteria, CriterionValue<T> value) {
        super(criteria, value);
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitContains(this);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
