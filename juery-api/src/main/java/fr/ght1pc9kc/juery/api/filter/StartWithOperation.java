package fr.ght1pc9kc.juery.api.filter;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class StartWithOperation<T> extends BiOperand<T> {
    public StartWithOperation(CriterionProperty criteria, CriterionValue<T> value) {
        super(criteria, value);
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitStartWith(this);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
