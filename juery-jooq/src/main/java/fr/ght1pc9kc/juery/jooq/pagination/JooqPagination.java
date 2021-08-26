package fr.ght1pc9kc.juery.jooq.pagination;

import fr.ght1pc9kc.juery.api.PageRequest;
import fr.ght1pc9kc.juery.api.Pagination;
import fr.ght1pc9kc.juery.api.pagination.Order;
import fr.ght1pc9kc.juery.api.pagination.Sort;
import lombok.experimental.UtilityClass;
import org.jooq.*;

import java.util.Map;

@UtilityClass
public class JooqPagination {
    public static <T extends Record> Select<T> apply(Pagination page, Map<String, Field<?>> propertiesMapper, SelectQuery<T> query) {
        if (page.sort() != null && !Sort.UNSORTED.equals(page.sort())) {
            for (Order order : page.sort().orders()) {
                Field<?> field = propertiesMapper.get(order.property());
                if (field != null) {
                    SortField<?> sort = field.sort(SortOrder.valueOf(order.direction().name()));
                    query.addOrderBy(sort);
                }
            }
        }
        if (page.size() > 0) {
            query.addLimit(page.size());
            if (page.offset() > 0) {
                query.addOffset(page.offset());
            }
        }
        return query;
    }

    public static <T extends Record> Select<T> apply(Pagination page, Map<String, Field<?>> propertiesMapper, SelectFinalStep<T> query) {
        return apply(page, propertiesMapper, query.getQuery());
    }

    public static <T extends Record> Select<T> apply(Pagination page, SelectFinalStep<T> query) {
        return apply(page, Map.of(), query);
    }

    /**
     * @deprecated Since 1.0.1 use instead {@link #apply(Pagination, Map, SelectQuery)}
     */
    @Deprecated(forRemoval = true, since = "1.1.0")
    public static <T extends Record> Select<T> apply(PageRequest page, Map<String, Field<?>> propertiesMapper, SelectFinalStep<T> query) {
        return apply(page.pagination(), propertiesMapper, query.getQuery());
    }

    /**
     * @deprecated Since 1.0.1 use instead {@link #apply(Pagination, SelectFinalStep)}
     */
    @Deprecated(forRemoval = true, since = "1.1.0")
    public static <T extends Record> Select<T> apply(PageRequest page, SelectFinalStep<T> query) {
        return apply(page.pagination(), Map.of(), query);
    }
}
