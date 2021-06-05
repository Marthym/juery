package fr.ght1pc9kc.juery.api.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode
public class NoCriterion implements Criteria {
    public static final NoCriterion NO_CRITERION = new NoCriterion();

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitNoCriteria(this);
    }
}
