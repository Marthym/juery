package fr.ght1pc9kc.juery.mongo;

import com.mongodb.client.model.Filters;
import fr.ght1pc9kc.juery.api.filter.AndOperation;
import fr.ght1pc9kc.juery.api.filter.CriteriaVisitor;
import fr.ght1pc9kc.juery.api.filter.EqualOperation;
import fr.ght1pc9kc.juery.api.filter.GreaterThanEqualsOperation;
import fr.ght1pc9kc.juery.api.filter.GreaterThanOperation;
import fr.ght1pc9kc.juery.api.filter.InOperation;
import fr.ght1pc9kc.juery.api.filter.LowerThanEqualsOperation;
import fr.ght1pc9kc.juery.api.filter.LowerThanOperation;
import fr.ght1pc9kc.juery.api.filter.NoCriterion;
import fr.ght1pc9kc.juery.api.filter.NotOperation;
import fr.ght1pc9kc.juery.api.filter.OrOperation;
import lombok.RequiredArgsConstructor;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.List;

@RequiredArgsConstructor
public class MongoFilterVisitor implements CriteriaVisitor<Bson> {

    private final List<String> idsFields;

    @Override
    public Bson visitNoCriteria(NoCriterion none) {
        return Filters.empty();
    }

    @Override
    public Bson visitAnd(AndOperation operation) {
        List<Bson> andList = operation.andCriteria.stream()
                .map(a -> a.accept(this))
                .toList();
        return Filters.and(andList);
    }

    @Override
    public Bson visitNot(NotOperation operation) {
        return Filters.not(operation.negative.accept(this));
    }

    @Override
    public Bson visitOr(OrOperation operation) {
        List<Bson> orList = operation.orCriteria.stream()
                .map(o -> o.accept(this))
                .toList();
        return Filters.or(orList);
    }

    @Override
    public <T> Bson visitEqual(EqualOperation<T> operation) {
        if (idsFields.contains(operation.field.property)) {
            return Filters.eq(operation.field.property, new ObjectId(String.valueOf(operation.value.value)));
        }
        return Filters.eq(operation.field.property, operation.value.value);
    }

    @Override
    public <T> Bson visitGreaterThan(GreaterThanOperation<T> operation) {
        if (idsFields.contains(operation.field.property)) {
            return Filters.gt(operation.field.property, new ObjectId(String.valueOf(operation.value.value)));
        }
        return Filters.gt(operation.field.property, operation.value.value);
    }

    @Override
    public <T> Bson visitGreaterThanEquals(GreaterThanEqualsOperation<T> operation) {
        if (idsFields.contains(operation.field.property)) {
            return Filters.gte(operation.field.property, new ObjectId(String.valueOf(operation.value.value)));
        }
        return Filters.gte(operation.field.property, operation.value.value);
    }

    @Override
    public <T> Bson visitLowerThan(LowerThanOperation<T> operation) {
        if (idsFields.contains(operation.field.property)) {
            return Filters.lt(operation.field.property, new ObjectId(String.valueOf(operation.value.value)));
        }
        return Filters.lt(operation.field.property, operation.value.value);
    }

    @Override
    public <T> Bson visitLowerThanEquals(LowerThanEqualsOperation<T> operation) {
        if (idsFields.contains(operation.field.property)) {
            return Filters.lte(operation.field.property, new ObjectId(String.valueOf(operation.value.value)));
        }
        return Filters.lte(operation.field.property, operation.value.value);
    }

    @Override
    public <T> Bson visitIn(InOperation<T> operation) {
        if (idsFields.contains(operation.field.property)) {
            List<ObjectId> list = operation.value.value.stream()
                    .map(value -> new ObjectId(String.valueOf(value)))
                    .toList();
            return Filters.in(operation.field.property, list);
        }
        return Filters.in(operation.field.property, operation.value.value);
    }

}
