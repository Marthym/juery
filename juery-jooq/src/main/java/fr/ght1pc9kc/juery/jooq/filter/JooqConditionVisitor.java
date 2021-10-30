package fr.ght1pc9kc.juery.jooq.filter;

import fr.ght1pc9kc.juery.api.filter.*;
import lombok.AllArgsConstructor;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@AllArgsConstructor
public class JooqConditionVisitor implements CriteriaVisitor<Condition> {

    private final Function<String, Field<?>> propertiesSupplier;

    public JooqConditionVisitor(Map<String, Field<?>> properties) {
        this.propertiesSupplier = properties::get;
    }

    @Override
    public Condition visitNoCriteria(NoCriterion none) {
        return DSL.noCondition();
    }

    @Override
    public Condition visitAnd(AndOperation operation) {
        Condition[] conditions = operation.andCriteria.stream()
                .map(a -> a.accept(this))
                .toArray(Condition[]::new);
        return DSL.and(conditions);
    }

    @Override
    public Condition visitNot(NotOperation operation) {
        return DSL.not(operation.negative.accept(this));
    }

    @Override
    public Condition visitOr(OrOperation operation) {
        Condition[] conditions = operation.orCriteria.stream()
                .map(o -> o.accept(this))
                .toArray(Condition[]::new);
        return DSL.or(conditions);
    }

    @Override
    public <T> Condition visitIn(InOperation<T> operation) {
        return readFieldToCondition(operation, Field::in);
    }

    @Override
    public <T> Condition visitEqual(EqualOperation<T> operation) {
        return readFieldToCondition(operation, Field::eq);
    }

    @Override
    public <T> Condition visitStartWith(StartWithOperation<T> operation) {
        return readFieldToCondition(operation, Field::startsWith);
    }

    @Override
    public <T> Condition visitEndWith(EndWithOperation<T> operation) {
        return readFieldToCondition(operation, Field::endsWith);
    }

    @Override
    public <T> Condition visitContains(ContainsOperation<T> operation) {
        return readFieldToCondition(operation, Field::contains);
    }

    @Override
    public <T> Condition visitGreaterThan(GreaterThanOperation<T> operation) {
        return readFieldToCondition(operation, Field::gt);
    }

    @Override
    public <T> Condition visitGreaterThanEquals(GreaterThanEqualsOperation<T> operation) {
        return readFieldToCondition(operation, Field::ge);
    }

    @Override
    public <T> Condition visitLowerThan(LowerThanOperation<T> operation) {
        return readFieldToCondition(operation, Field::lt);
    }

    @Override
    public <T> Condition visitLowerThanEquals(LowerThanEqualsOperation<T> operation) {
        return readFieldToCondition(operation, Field::le);
    }

    @SuppressWarnings("unchecked")
    private <T, R> Condition readFieldToCondition(BiOperand<R> operation, BiFunction<Field<T>, T, Condition> op) {
        return Optional.ofNullable(propertiesSupplier.apply(operation.field.property))
                .map(f -> {
                    if (operation.value.isNull()) {
                        return f.isNull();

                    } else if (f.getDataType().isDate()) {
                        if (operation.value.value instanceof Instant) {
                            return op.apply((Field<T>) f, (T) Instant.from((TemporalAccessor) operation.value.value)
                                    .atOffset(ZoneOffset.UTC).toLocalDate());
                        } else {
                            return op.apply((Field<T>) f, (T) LocalDate.from((TemporalAccessor) operation.value.value));
                        }

                    } else if (f.getDataType().isDateTime()) {
                        if (operation.value.value instanceof Instant) {
                            return op.apply((Field<T>) f, (T) Instant.from((TemporalAccessor) operation.value.value)
                                    .atOffset(ZoneOffset.UTC).toLocalDateTime());
                        } else if (operation.value.value instanceof LocalDate) {
                            return op.apply((Field<T>) f, (T) LocalDate.from((TemporalAccessor) operation.value.value)
                                    .atStartOfDay());
                        } else {
                            return op.apply((Field<T>) f, (T) LocalDateTime.from((TemporalAccessor) operation.value.value));
                        }

                    } else {
                        return op.apply((Field<T>) f, (T) operation.value.value);
                    }
                }).orElse(DSL.noCondition());
    }
}
