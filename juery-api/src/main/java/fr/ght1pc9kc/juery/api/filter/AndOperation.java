package fr.ght1pc9kc.juery.api.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import lombok.Value;

import java.util.List;
import java.util.Objects;

@Value
public class AndOperation implements Criteria {
    public final List<Criteria> andCriteria;

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public <R> R accept(CriteriaVisitor<R> visitor) {
        return visitor.visitAnd(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AndOperation that = (AndOperation) o;
        return this.andCriteria.containsAll(that.andCriteria)
                && that.andCriteria.containsAll(this.andCriteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), andCriteria);
    }
}
