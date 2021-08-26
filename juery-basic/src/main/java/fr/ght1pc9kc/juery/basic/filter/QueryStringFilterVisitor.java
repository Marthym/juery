package fr.ght1pc9kc.juery.basic.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import fr.ght1pc9kc.juery.api.filter.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QueryStringFilterVisitor implements CriteriaVisitor<String> {

    public static final char QS_START_CHAR = '^';
    public static final char QS_END_CHAR = '$';
    public static final char QS_CONTAINS_CHAR = '∋';
    public static final char QS_LT_CHAR = '<';
    public static final char QS_LTE_CHAR = '≤';
    public static final char QS_GT_CHAR = '>';
    public static final char QS_GTE_CHAR = '≥';

    private static final String ENCODED_START_CHAR = URLEncoder.encode(String.valueOf(QS_START_CHAR), StandardCharsets.UTF_8);
    private static final String ENCODED_END_CHAR = URLEncoder.encode(String.valueOf(QS_END_CHAR), StandardCharsets.UTF_8);
    private static final String ENCODED_CONTAINS_CHAR = URLEncoder.encode(String.valueOf(QS_CONTAINS_CHAR), StandardCharsets.UTF_8);
    private static final String ENCODED_LT_CHAR = URLEncoder.encode(String.valueOf(QS_LT_CHAR), StandardCharsets.UTF_8);
    private static final String ENCODED_LTE_CHAR = URLEncoder.encode(String.valueOf(QS_LTE_CHAR), StandardCharsets.UTF_8);
    private static final String ENCODED_GT_CHAR = URLEncoder.encode(String.valueOf(QS_GT_CHAR), StandardCharsets.UTF_8);
    private static final String ENCODED_GTE_CHAR = URLEncoder.encode(String.valueOf(QS_GTE_CHAR), StandardCharsets.UTF_8);

    @Override
    public String visitNoCriteria(NoCriterion none) {
        return "";
    }

    @Override
    public String visitAnd(AndOperation operation) {
        return operation.andCriteria.stream()
                .map(a -> a.accept(this))
                .collect(Collectors.joining("&"));
    }

    @Override
    public String visitNot(NotOperation operation) {
        throw new IllegalStateException("Operation 'not' not permitted in query string !");
    }

    @Override
    public String visitOr(OrOperation operation) {
        throw new IllegalStateException("Operation 'or' not permitted in query string !");
    }

    @Override
    public <T> String visitEqual(EqualOperation<T> operation) {
        return URLEncoder.encode(operation.field.property, StandardCharsets.UTF_8) + "=" + operation.value.accept(this);
    }

    @Override
    public <T> String visitIn(InOperation<T> operation) {
        List<Criteria> ands = new ArrayList<>(operation.value.value.size());
        for (T t : operation.value.value) {
            ands.add(Criteria.property(operation.field.property).eq(t));
        }
        return Criteria.and(ands.toArray(Criteria[]::new)).accept(this);
    }

    @Override
    public <T> String visitGreaterThan(GreaterThanOperation<T> operation) {
        return URLEncoder.encode(operation.field.property, StandardCharsets.UTF_8) + "="
                + ENCODED_GT_CHAR + operation.value.accept(this);
    }

    @Override
    public <T> String visitGreaterThanEquals(GreaterThanEqualsOperation<T> operation) {
        return URLEncoder.encode(operation.field.property, StandardCharsets.UTF_8) + "="
                + ENCODED_GTE_CHAR + operation.value.accept(this);
    }

    @Override
    public <T> String visitLowerThan(LowerThanOperation<T> operation) {
        return URLEncoder.encode(operation.field.property, StandardCharsets.UTF_8) + "="
                + ENCODED_LT_CHAR + operation.value.accept(this);
    }

    @Override
    public <T> String visitLowerThanEquals(LowerThanEqualsOperation<T> operation) {
        return URLEncoder.encode(operation.field.property, StandardCharsets.UTF_8) + "="
                + ENCODED_LTE_CHAR + operation.value.accept(this);
    }

    @Override
    public <T> String visitValue(CriterionValue<T> value) {
        return URLEncoder.encode(value.value.toString(), StandardCharsets.UTF_8);
    }

    @Override
    public <T> String visitStartWith(StartWithOperation<T> operation) {
        return URLEncoder.encode(operation.field.property, StandardCharsets.UTF_8) + "="
                + ENCODED_START_CHAR + operation.value.accept(this);
    }

    @Override
    public <T> String visitEndWith(EndWithOperation<T> operation) {
        return URLEncoder.encode(operation.field.property, StandardCharsets.UTF_8) + "="
                + ENCODED_END_CHAR + operation.value.accept(this);
    }

    @Override
    public <T> String visitContains(ContainsOperation<T> operation) {
        return URLEncoder.encode(operation.field.property, StandardCharsets.UTF_8) + "="
                + ENCODED_CONTAINS_CHAR + operation.value.accept(this);
    }
}
