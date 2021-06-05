package fr.ght1pc9kc.juery.basic;

import fr.ght1pc9kc.juery.api.Criteria;
import fr.ght1pc9kc.juery.api.PageRequest;
import fr.ght1pc9kc.juery.api.Pagination;
import fr.ght1pc9kc.juery.api.filter.CriterionProperty;
import fr.ght1pc9kc.juery.api.pagination.Direction;
import fr.ght1pc9kc.juery.api.pagination.Order;
import fr.ght1pc9kc.juery.api.pagination.Sort;
import fr.ght1pc9kc.juery.basic.common.lang3.BooleanUtils;
import fr.ght1pc9kc.juery.basic.common.lang3.NumberUtils;
import fr.ght1pc9kc.juery.basic.common.lang3.StringUtils;
import fr.ght1pc9kc.juery.basic.filter.QueryStringFilterVisitor;
import lombok.experimental.UtilityClass;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@UtilityClass
public class PageRequestFormatter {
    private static final String DEFAULT_PAGE_PARAMETER = "_p";
    private static final String DEFAULT_SIZE_PARAMETER = "_pp";
    private static final String DEFAULT_SORT_PARAMETER = "_s";
    private static final Set<String> EXCLUDE_FILTER_PARAMETERS = Set.of(
            DEFAULT_PAGE_PARAMETER, DEFAULT_SIZE_PARAMETER, DEFAULT_SORT_PARAMETER
    );
    private static final int MAX_PAGE_SIZE = 100;
    private static final QueryStringFilterVisitor CRITERIA_FORMATTER = new QueryStringFilterVisitor();

    public static String formatPageRequest(PageRequest pr) {
        var qs = new StringBuilder();
        if (pr.pagination().page() > 0) {
            qs.append(DEFAULT_PAGE_PARAMETER + "=").append(pr.pagination().page()).append('&');
        }
        if (pr.pagination().size() < MAX_PAGE_SIZE) {
            qs.append(DEFAULT_SORT_PARAMETER + "=").append(pr.pagination().size()).append('&');
        }
        if (!pr.pagination().sort().equals(Sort.of())) {
            qs.append(DEFAULT_SORT_PARAMETER + "=").append(formatSortValue(pr.pagination().sort())).append('&');
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

    public static String formatSortValue(Sort sort) {
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

    public static PageRequest parse(Map<String, String> queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return PageRequest.all();
        }
        int page = Optional.ofNullable(queryString.get(DEFAULT_PAGE_PARAMETER))
                .map(Integer::parseInt)
                .orElse(0);
        int perPage = Optional.ofNullable(queryString.get(DEFAULT_SIZE_PARAMETER))
                .map(Integer::parseInt)
                .map(i -> Math.min(i, MAX_PAGE_SIZE))
                .orElse(MAX_PAGE_SIZE);
        Sort sort = Optional.ofNullable(queryString.get(DEFAULT_SORT_PARAMETER))
                .map(PageRequestFormatter::parseSortParameter)
                .orElse(Sort.of());

        Criteria[] filters = queryString.entrySet().stream()
                .filter(e -> !EXCLUDE_FILTER_PARAMETERS.contains(e.getKey()))
                .sorted(Entry.comparingByKey())
                .map(e -> parseCriterionParameter(e.getKey(), e.getValue()))
                .toArray(Criteria[]::new);

        return PageRequest.of(
                Pagination.of(page, perPage, sort),
                Criteria.and(filters)
        );
    }

    public static PageRequest parse(String queryString) {
        return parse(queryStringToMap(queryString));
    }

    public static Criteria parseCriterionParameter(String key, String paramValue) {
        String tmp = paramValue;

        // Parse operation
        BiFunction<CriterionProperty, Object, Criteria> operation = CriterionProperty::eq;
        if (!StringUtils.isBlank(tmp)) {
            switch (tmp.charAt(0)) {
                case '^':
                    tmp = tmp.substring(1);
                    operation = CriterionProperty::startWith;
                    break;
                case '$':
                    tmp = tmp.substring(1);
                    operation = CriterionProperty::endWith;
                    break;
                case '∋':
                    tmp = tmp.substring(1);
                    operation = CriterionProperty::contains;
                    break;
                default:
            }
        }

        // Parse value type
        Object value;
        var bValue = BooleanUtils.toBooleanObject(tmp);
        if (bValue != null) {
            value = bValue;
        } else if (NumberUtils.isCreatable(tmp)) {
            value = NumberUtils.createNumber(tmp);
        } else {
            value = (tmp != null && !tmp.isBlank())
                    ? tmp : Boolean.TRUE;
        }

        return operation.apply(Criteria.property(key), value);
    }

    public static Sort parseSortParameter(String value) {
        if (value == null || value.isEmpty()) {
            return Sort.of();
        }

        String[] segment = value.split(",");
        Order[] orders = Arrays.stream(segment)
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

    private static Map<String, String> queryStringToMap(String queryString) {
        if (queryString == null || queryString.isBlank()) {
            return Map.of();
        }
        return Arrays.stream(queryString.split("&"))
                .map(PageRequestFormatter::splitQueryParameter)
                .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));
    }

    private static Entry<String, String> splitQueryParameter(String it) {
        final int idx = it.indexOf('=');
        boolean hasEqualSymbol = idx > 0;
        final String key = hasEqualSymbol ? it.substring(0, idx) : it;
        final String value = hasEqualSymbol && it.length() > idx + 1 ? it.substring(idx + 1) : "";
        return Map.entry(
                URLDecoder.decode(key, StandardCharsets.UTF_8),
                URLDecoder.decode(value, StandardCharsets.UTF_8)
        );
    }
}
