package fr.ght1pc9kc.juery.api.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class NotOperation extends Criteria {
    public Criteria negative;

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public <R> R visit(Visitor<R> visitor) {
        return visitor.visitNot(this);
    }
}
