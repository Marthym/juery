package fr.ght1pc9kc.juery.basic.parser;

import fr.ght1pc9kc.juery.api.Criteria;
import fr.ght1pc9kc.juery.api.PageRequest;
import fr.ght1pc9kc.juery.api.Pagination;
import fr.ght1pc9kc.juery.api.filter.CriterionProperty;
import fr.ght1pc9kc.juery.api.pagination.Direction;
import fr.ght1pc9kc.juery.api.pagination.Order;
import fr.ght1pc9kc.juery.api.pagination.Sort;
import fr.ght1pc9kc.juery.basic.ParserConfiguration;
import fr.ght1pc9kc.juery.basic.QueryStringParser;
import fr.ght1pc9kc.juery.basic.common.lang3.BooleanUtils;
import fr.ght1pc9kc.juery.basic.common.lang3.NumberUtils;
import fr.ght1pc9kc.juery.basic.common.lang3.StringUtils;
import fr.ght1pc9kc.juery.basic.filter.QueryStringFilterVisitor;
import fr.ght1pc9kc.juery.basic.utils.TemporalUtils;
import lombok.RequiredArgsConstructor;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.ght1pc9kc.juery.basic.filter.QueryStringFilterVisitor.QS_CONTAINS_CHAR;
import static fr.ght1pc9kc.juery.basic.filter.QueryStringFilterVisitor.QS_END_CHAR;
import static fr.ght1pc9kc.juery.basic.filter.QueryStringFilterVisitor.QS_GTE_CHAR;
import static fr.ght1pc9kc.juery.basic.filter.QueryStringFilterVisitor.QS_GT_CHAR;
import static fr.ght1pc9kc.juery.basic.filter.QueryStringFilterVisitor.QS_LTE_CHAR;
import static fr.ght1pc9kc.juery.basic.filter.QueryStringFilterVisitor.QS_LT_CHAR;
import static fr.ght1pc9kc.juery.basic.filter.QueryStringFilterVisitor.QS_START_CHAR;
import static java.util.function.Predicate.not;

@RequiredArgsConstructor
public final class QueryStringParserImpl implements QueryStringParser {
    private static final int PAGE_START_INDEX = 0;
    private static final QueryStringFilterVisitor CRITERIA_FORMATTER = new QueryStringFilterVisitor();

    private final ParserConfiguration config;

    @Override
    public String format(PageRequest pr) {
        var qs = new StringBuilder();
        if (pr.pagination().offset() > 1) {
            qs.append(config.fromParameter()).append("=").append(pr.pagination().offset()).append('&');
        }
        if (pr.pagination().size() < config.maxPageSize()) {
            qs.append(config.sizeParameter()).append("=").append(pr.pagination().size()).append('&');
        }
        if (!pr.pagination().sort().equals(Sort.of())) {
            qs.append(config.sortParameter()).append("=").append(formatSortValue(pr.pagination().sort())).append('&');
        }
        if (!pr.filter().isEmpty()) {
            qs.append(pr.filter().accept(CRITERIA_FORMATTER));
        }
        if (qs.length() == 0) {
            return "";
        }
        var c = qs.charAt(qs.length() - 1);
        if (c == '&') {
            qs.setLength(qs.length() - 1);
        }
        return qs.toString();
    }

    @Override
    public String formatSortValue(Sort sort) {
        var qs = new StringBuilder();
        for (Order order : sort.orders()) {
            if (order.direction() == Direction.DESC) {
                qs.append('-');
            }
            qs.append(URLEncoder.encode(order.property(), StandardCharsets.UTF_8));
            qs.append(',');
        }
        qs.setLength(qs.length() - 1);
        return qs.toString();
    }

    @Override
    public PageRequest parse(Map<String, List<String>> queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return PageRequest.all();
        }

        Pagination pagination = (queryString.containsKey(config.pageParameter()))
                ? parsePaginationByPage(queryString)
                : parsePaginationByOffset(queryString);

        Criteria[] filters = queryString.entrySet().stream()
                .filter(e -> !config.excludeFilterParameters().contains(e.getKey()))
                .sorted(Map.Entry.comparingByKey())
                .map(e -> parseCriterionParameter(e.getKey(), e.getValue()))
                .toArray(Criteria[]::new);

        return PageRequest.of(pagination, Criteria.and(filters));
    }

    @Override
    public PageRequest parse(String queryString) {
        return parse(queryStringToMap(queryString));
    }

    @Override
    public Criteria parseCriterionParameter(String key, List<String> paramValue) {
        if (paramValue == null || paramValue.isEmpty()) {
            return Criteria.property(key).eq(Boolean.TRUE);
        }

        List<Object> filteredListValues = paramValue.stream()
                .distinct()
                .map(QueryStringParserImpl::parseValueType)
                .toList();

        if (paramValue.size() > 1) {
            return Criteria.property(key).in(filteredListValues);
        }

        String strValue = paramValue.get(0);

        // Parse operation
        BiFunction<CriterionProperty, Object, Criteria> operation = CriterionProperty::eq;
        Object typedValue = null;
        if (!StringUtils.isBlank(strValue)) {
            switch (strValue.charAt(0)) {
                case QS_START_CHAR:
                    operation = CriterionProperty::startWith;
                    typedValue = parseValueType(strValue.substring(1));
                    break;
                case QS_END_CHAR:
                    operation = CriterionProperty::endWith;
                    typedValue = parseValueType(strValue.substring(1));
                    break;
                case QS_CONTAINS_CHAR:
                    operation = CriterionProperty::contains;
                    typedValue = strValue.substring(1);
                    break;
                case QS_LT_CHAR:
                    operation = CriterionProperty::lt;
                    typedValue = parseValueType(strValue.substring(1));
                    break;
                case QS_GT_CHAR:
                    operation = CriterionProperty::gt;
                    typedValue = parseValueType(strValue.substring(1));
                    break;
                case QS_LTE_CHAR:
                    operation = CriterionProperty::lte;
                    typedValue = parseValueType(strValue.substring(1));
                    break;
                case QS_GTE_CHAR:
                    operation = CriterionProperty::gte;
                    typedValue = parseValueType(strValue.substring(1));
                    break;
                default:
                    typedValue = parseValueType(strValue);
            }
        } else {
            typedValue = parseValueType(strValue);
        }

        return operation.apply(Criteria.property(key), typedValue);
    }

    private Pagination parsePaginationByPage(Map<String, List<String>> queryString) {
        int page = Optional.ofNullable(queryString.get(config.pageParameter()))
                .flatMap(l -> Optional.ofNullable(l.get(0)))
                .map(Integer::parseInt)
                .orElse(0);

        int size = Optional.ofNullable(queryString.get(config.sizeParameter()))
                .flatMap(l -> Optional.ofNullable(l.get(0)))
                .map(Integer::parseInt)
                .map(i -> Math.min(i, config.maxPageSize()))
                .orElse(config.maxPageSize());

        Sort sort = Optional.ofNullable(queryString.get(config.sortParameter()))
                .map(QueryStringParserImpl::parseSortParameter)
                .orElse(Sort.of());

        return Pagination.of(page * size, size, sort);
    }

    private Pagination parsePaginationByOffset(Map<String, List<String>> queryString) {
        int offset = Optional.ofNullable(queryString.get(config.fromParameter()))
                .flatMap(l -> Optional.ofNullable(l.get(0)))
                .map(Integer::parseInt)
                .orElse(PAGE_START_INDEX);

        int maxTo = offset + config.maxPageSize() - 1;

        int size = Optional.ofNullable(queryString.get(config.sizeParameter()))
                .flatMap(l -> Optional.ofNullable(l.get(0)))
                .map(Integer::parseInt)
                .map(i -> Math.min(i, config.maxPageSize()))
                .orElseGet(() -> Optional.ofNullable(queryString.get(config.toParameter()))
                        .flatMap(l -> Optional.ofNullable(l.get(0)))
                        .map(Integer::parseInt)
                        .map(i -> Math.min(i, maxTo))
                        .filter(i -> i > offset)
                        .map(i -> i - offset)
                        .orElse(config.maxPageSize()));

        Sort sort = Optional.ofNullable(queryString.get(config.sortParameter()))
                .map(QueryStringParserImpl::parseSortParameter)
                .orElse(Sort.of());

        return Pagination.of(offset, size, sort);
    }

    static Sort parseSortParameter(List<String> value) {
        Order[] orders = value.stream()
                .flatMap(p -> Stream.of(p.split(",")))
                .map(String::strip)
                .filter(not(String::isBlank))
                .filter(s -> s.length() > 1 || (s.charAt(0) != '-' && s.charAt(0) != '+'))
                .map(s -> {
                    var d = s.charAt(0);
                    if (d == '-') {
                        return Order.desc(s.substring(1).strip());
                    } else if (d == '+') {
                        return Order.asc(s.substring(1).strip());
                    } else {
                        return Order.asc(s);
                    }
                })
                .toArray(Order[]::new);
        return Sort.of(orders);
    }

    private static Map<String, List<String>> queryStringToMap(String queryString) {
        if (queryString == null || queryString.isBlank()) {
            return Map.of();
        }
        return Arrays.stream(queryString.split("&"))
                .map(QueryStringParserImpl::splitQueryParameter)
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toUnmodifiableList())));
    }

    private static Map.Entry<String, String> splitQueryParameter(String it) {
        final int idx = it.indexOf('=');
        boolean hasEqualSymbol = idx > 0;
        int arrayKeyIdx = it.indexOf('[');
        arrayKeyIdx = (arrayKeyIdx > 0) ? Math.min(arrayKeyIdx, idx) : idx;
        final String key = (hasEqualSymbol ? it.substring(0, arrayKeyIdx) : it);
        final String value = hasEqualSymbol && it.length() > idx + 1 ? it.substring(idx + 1) : "";
        return Map.entry(
                URLDecoder.decode(key, StandardCharsets.UTF_8),
                URLDecoder.decode(value, StandardCharsets.UTF_8)
        );
    }

    private static Object parseValueType(String strValue) {
        Object value;
        var bValue = BooleanUtils.toBooleanObject(strValue);
        if (bValue != null) {
            value = bValue;
        } else if (NumberUtils.isCreatable(strValue)) {
            value = NumberUtils.createNumber(strValue);
        } else if (TemporalUtils.isCreatable(strValue)) {
            value = TemporalUtils.create(strValue);
        } else {
            value = (strValue != null && !strValue.isBlank())
                    ? strValue : Boolean.TRUE;
        }
        return value;
    }
}
