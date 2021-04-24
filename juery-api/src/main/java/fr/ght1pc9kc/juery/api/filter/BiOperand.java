package fr.ght1pc9kc.juery.api.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor
public abstract class BiOperand<T> implements Criteria {
    public CriterionProperty field;
    public CriterionValue<T> value;

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "('" + field.property + "', " + value.value + ')';
    }
}
