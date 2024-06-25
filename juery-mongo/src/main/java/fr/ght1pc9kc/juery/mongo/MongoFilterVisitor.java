package fr.ght1pc9kc.juery.mongo;

import com.mongodb.client.model.Filters;
import fr.ght1pc9kc.juery.api.filter.AndOperation;
import fr.ght1pc9kc.juery.api.filter.ContainsOperation;
import fr.ght1pc9kc.juery.api.filter.CriteriaVisitor;
import fr.ght1pc9kc.juery.api.filter.EndWithOperation;
import fr.ght1pc9kc.juery.api.filter.EqualOperation;
import fr.ght1pc9kc.juery.api.filter.GreaterThanEqualsOperation;
import fr.ght1pc9kc.juery.api.filter.GreaterThanOperation;
import fr.ght1pc9kc.juery.api.filter.InOperation;
import fr.ght1pc9kc.juery.api.filter.LowerThanEqualsOperation;
import fr.ght1pc9kc.juery.api.filter.LowerThanOperation;
import fr.ght1pc9kc.juery.api.filter.NoCriterion;
import fr.ght1pc9kc.juery.api.filter.NotOperation;
import fr.ght1pc9kc.juery.api.filter.OrOperation;
import fr.ght1pc9kc.juery.api.filter.StartWithOperation;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Convert a {@link fr.ght1pc9kc.juery.api.Criteria} into a Mongo filter query
 */
public class MongoFilterVisitor implements CriteriaVisitor<Bson> {
    private static final MongoFilterVisitor DEFAULT_INSTANCE = new MongoFilterVisitor(Set.of(), null);

    private final Set<String> objectIdFields;
    private final @Nullable Map<String, String> propertiesMapping;

    /**
     * Create a new MongoFilterVisitor
     *
     * @param objectIdFields    A Set of mongo properties that must be considered as Mongo {@link ObjectId}
     * @param propertiesMapping A properties mapping from query property name to Mongo database field name
     */
    private MongoFilterVisitor(Collection<String> objectIdFields, @Nullable Map<String, String> propertiesMapping) {
        this.objectIdFields = Set.copyOf(objectIdFields);
        this.propertiesMapping = nonNull(propertiesMapping) ? Map.copyOf(propertiesMapping) : null;
    }

    public static MongoFilterVisitor getMongoFilter() {
        return DEFAULT_INSTANCE;
    }

    public static MongoFilterVisitor getMongoFilter(Collection<String> objectIdFields, Map<String, String> propertiesMapping) {
        return new MongoFilterVisitor(objectIdFields, propertiesMapping);
    }

    public static MongoFilterVisitor getMongoFilter(Collection<String> objectIdFields) {
        return new MongoFilterVisitor(objectIdFields, Map.of());
    }

    public static MongoFilterVisitor getMongoFilter(Map<String, String> propertiesMapping) {
        return new MongoFilterVisitor(Set.of(), propertiesMapping);
    }

    @Override
    public Bson visitNoCriteria(NoCriterion none) {
        return Filters.empty();
    }

    @Override
    public Bson visitAnd(AndOperation operation) {
        List<Bson> andList = operation.andCriteria.stream()
                .map(a -> a.accept(this))
                .filter(b -> !b.toBsonDocument().isEmpty())
                .toList();
        if (andList.isEmpty()) {
            return Filters.empty();
        } else if (andList.size() == 1) {
            return andList.get(0);
        } else {
            return Filters.and(andList);
        }
    }

    @Override
    public Bson visitNot(NotOperation operation) {
        return Filters.not(operation.negative.accept(this));
    }

    @Override
    public Bson visitOr(OrOperation operation) {
        List<Bson> orList = operation.orCriteria.stream()
                .map(o -> o.accept(this))
                .filter(b -> !b.toBsonDocument().isEmpty())
                .toList();
        if (orList.isEmpty()) {
            return Filters.empty();
        } else if (orList.size() == 1) {
            return orList.get(0);
        } else {
            return Filters.or(orList);
        }
    }

    @Override
    public <T> Bson visitEqual(EqualOperation<T> operation) {
        String mappedProperty = (isNull(propertiesMapping)) ? operation.field.property : propertiesMapping.get(operation.field.property);
        if (mappedProperty == null) {
            return Filters.empty();

        } else if (objectIdFields.contains(mappedProperty)) {
            return Filters.eq(mappedProperty, new ObjectId(String.valueOf(operation.value.value)));

        } else {
            return Filters.eq(mappedProperty, operation.value.value);
        }
    }

    @Override
    public <T> Bson visitGreaterThan(GreaterThanOperation<T> operation) {
        String mappedProperty = (isNull(propertiesMapping)) ? operation.field.property : propertiesMapping.get(operation.field.property);
        if (mappedProperty == null) {
            return Filters.empty();

        } else if (objectIdFields.contains(mappedProperty)) {
            return Filters.gt(mappedProperty, new ObjectId(String.valueOf(operation.value.value)));
        }
        return Filters.gt(mappedProperty, operation.value.value);
    }

    @Override
    public <T> Bson visitGreaterThanEquals(GreaterThanEqualsOperation<T> operation) {
        String mappedProperty = (isNull(propertiesMapping)) ? operation.field.property : propertiesMapping.get(operation.field.property);
        if (mappedProperty == null) {
            return Filters.empty();

        } else if (objectIdFields.contains(mappedProperty)) {
            return Filters.gte(mappedProperty, new ObjectId(String.valueOf(operation.value.value)));
        }
        return Filters.gte(mappedProperty, operation.value.value);
    }

    @Override
    public <T> Bson visitLowerThan(LowerThanOperation<T> operation) {
        String mappedProperty = (isNull(propertiesMapping)) ? operation.field.property : propertiesMapping.get(operation.field.property);
        if (mappedProperty == null) {
            return Filters.empty();

        } else if (objectIdFields.contains(mappedProperty)) {
            return Filters.lt(mappedProperty, new ObjectId(String.valueOf(operation.value.value)));
        }
        return Filters.lt(mappedProperty, operation.value.value);
    }

    @Override
    public <T> Bson visitLowerThanEquals(LowerThanEqualsOperation<T> operation) {
        String mappedProperty = (isNull(propertiesMapping)) ? operation.field.property : propertiesMapping.get(operation.field.property);
        if (mappedProperty == null) {
            return Filters.empty();

        } else if (objectIdFields.contains(mappedProperty)) {
            return Filters.lte(mappedProperty, new ObjectId(String.valueOf(operation.value.value)));
        }
        return Filters.lte(mappedProperty, operation.value.value);
    }

    @Override
    public <T> Bson visitIn(InOperation<T> operation) {
        String mappedProperty = (isNull(propertiesMapping)) ? operation.field.property : propertiesMapping.get(operation.field.property);
        if (mappedProperty == null) {
            return Filters.empty();

        } else if (objectIdFields.contains(mappedProperty)) {
            List<ObjectId> list = operation.value.value.stream()
                    .map(value -> new ObjectId(String.valueOf(value)))
                    .toList();
            return Filters.in(mappedProperty, list);
        }
        return Filters.in(mappedProperty, operation.value.value);
    }

    @Override
    public <T> Bson visitStartWith(StartWithOperation<T> operation) {
        String mappedProperty = (isNull(propertiesMapping)) ? operation.field.property : propertiesMapping.get(operation.field.property);
        if (mappedProperty == null) {
            return Filters.empty();

        } else if (objectIdFields.contains(mappedProperty)) {
            throw new IllegalArgumentException("StartWith operator not allowed for ObjectId fields");
        }
        Pattern pattern = Pattern.compile("^" + Pattern.quote(operation.value.value.toString()) + ".*", Pattern.CASE_INSENSITIVE);
        return Filters.regex(mappedProperty, pattern);
    }

    @Override
    public <T> Bson visitEndWith(EndWithOperation<T> operation) {
        String mappedProperty = (isNull(propertiesMapping)) ? operation.field.property : propertiesMapping.get(operation.field.property);
        if (mappedProperty == null) {
            return Filters.empty();

        } else if (objectIdFields.contains(mappedProperty)) {
            throw new IllegalArgumentException("EndWith operator not allowed for ObjectId fields");
        }
        Pattern pattern = Pattern.compile(".*" + Pattern.quote(operation.value.value.toString()) + "$", Pattern.CASE_INSENSITIVE);
        return Filters.regex(mappedProperty, pattern);
    }

    @Override
    public <T> Bson visitContains(ContainsOperation<T> operation) {
        String mappedProperty = (isNull(propertiesMapping)) ? operation.field.property : propertiesMapping.get(operation.field.property);
        if (mappedProperty == null) {
            return Filters.empty();

        } else if (objectIdFields.contains(mappedProperty)) {
            throw new IllegalArgumentException("Contains operator not allowed for ObjectId fields");
        }
        Pattern pattern = Pattern.compile(".*" + Pattern.quote(operation.value.value.toString()) + ".*", Pattern.CASE_INSENSITIVE);
        return Filters.regex(mappedProperty, pattern);
    }
}
