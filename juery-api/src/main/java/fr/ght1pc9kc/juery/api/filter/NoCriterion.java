package fr.ght1pc9kc.juery.api.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class NoCriterion extends Criteria {
    public static final NoCriterion NONE = new NoCriterion();

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public <R> R visit(Visitor<R> visitor) {
        return visitor.visitNoCriteria(this);
    }
}
