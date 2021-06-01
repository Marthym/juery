package fr.ght1pc9kc.juery.api.filter;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class EndWithOperation<T> extends BiOperand<T> {
    public EndWithOperation(CriterionProperty criteria, CriterionValue<T> value) {
        super(criteria, value);
    }

    @Override
    public <R> R visit(Visitor<R> visitor) {
        return visitor.visitEndWith(this);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
