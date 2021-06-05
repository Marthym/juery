package fr.ght1pc9kc.juery.api.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode
public class NotOperation implements Criteria {
    public final Criteria negative;

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitNot(this);
    }
}
