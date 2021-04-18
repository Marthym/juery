package fr.ght1pc9kc.juery.api.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter(AccessLevel.NONE)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CriterionValue<T> extends Criteria {
    static final CriterionValue<Void> NULL = new CriterionValue<>(null);

    public T value;

    @Override
    public boolean isEmpty() {
        return isNull() || value.toString().isEmpty();
    }

    public boolean isNull() {
        return NULL.equals(this);
    }

    @Override
    public <R> R visit(Visitor<R> visitor) {
        return visitor.visitValue(this);
    }
}
