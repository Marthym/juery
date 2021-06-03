package fr.ght1pc9kc.juery.basic.filter;

import fr.ght1pc9kc.juery.api.Criteria;
import fr.ght1pc9kc.juery.api.filter.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class QueryStringFilterVisitor implements Criteria.Visitor<String> {

    private static final String ENCODED_START_CHAR = URLEncoder.encode("^", StandardCharsets.UTF_8);
    private static final String ENCODED_END_CHAR = URLEncoder.encode("$", StandardCharsets.UTF_8);
    private static final String ENCODED_CONTAINS_CHAR = URLEncoder.encode("∋", StandardCharsets.UTF_8);

    @Override
    public String visitNoCriteria(NoCriterion none) {
        return "";
    }

    @Override
    public String visitAnd(AndOperation operation) {
        return operation.andCriteria.stream()
                .map(a -> a.visit(this))
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
        return URLEncoder.encode(operation.field.property, StandardCharsets.UTF_8) + "=" + operation.value.visit(this);
    }

    @Override
    public <T> String visitIn(InOperation<T> operation) {
        throw new IllegalStateException("Operation 'in' not permitted in query string !");
    }

    @Override
    public <T> String visitGreaterThan(GreaterThanOperation<T> operation) {
        throw new IllegalStateException("Operation '>' not permitted in query string !");
    }

    @Override
    public <T> String visitLowerThan(LowerThanOperation<T> operation) {
        throw new IllegalStateException("Operation '<' not permitted in query string !");
    }

    @Override
    public <T> String visitValue(CriterionValue<T> value) {
        return URLEncoder.encode(value.value.toString(), StandardCharsets.UTF_8);
    }

    @Override
    public <T> String visitStartWith(StartWithOperation<T> operation) {
        return URLEncoder.encode(operation.field.property, StandardCharsets.UTF_8) + "="
                + ENCODED_START_CHAR + operation.value.visit(this);
    }

    @Override
    public <T> String visitEndWith(EndWithOperation<T> operation) {
        return URLEncoder.encode(operation.field.property, StandardCharsets.UTF_8) + "="
                + ENCODED_END_CHAR + operation.value.visit(this);
    }

    @Override
    public <T> String visitContains(ContainsOperation<T> operation) {
        return URLEncoder.encode(operation.field.property, StandardCharsets.UTF_8) + "="
                + ENCODED_CONTAINS_CHAR + operation.value.visit(this);
    }
}
