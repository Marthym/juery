package fr.ght1pc9kc.juery.jooq.pagination;

import fr.ght1pc9kc.juery.api.pagination.Order;
import fr.ght1pc9kc.juery.api.Pagination;
import fr.ght1pc9kc.juery.api.pagination.Sort;
import org.assertj.core.api.Assertions;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Select;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static fr.ght1pc9kc.juery.jooq.database.SchemaSample.*;

class JooqPaginationTest {

    @Test
    void should_limit_query() {
        Pagination pagination = Pagination.of(5, 10);
        Select<Record1<Integer>> actual = JooqPagination.apply(pagination, DSL.selectCount().from(NEWS));
        Assertions.assertThat(actual.getSQL(ParamType.INLINED))
                .isEqualTo("select count(*) from \"NEWS\" limit 10 offset 50");
    }

    @Test
    void should_sort_query() {
        Assertions.assertThat(NEWS_TITLE).isNotNull();
        Assertions.assertThat(NEWS_PUBLICATION).isNotNull();

        Pagination pagination = Pagination.of(-1, -1, Sort.of(
                Order.asc("publication"),
                Order.desc("title")));
        Select<Record2<String, LocalDateTime>> actual = JooqPagination.apply(
                pagination, NEWS_PROPERTIES_MAPPING, DSL.select(NEWS_TITLE, NEWS_PUBLICATION).from(NEWS));
        Assertions.assertThat(actual.getSQL(ParamType.INLINED))
                .isEqualTo("select \"NEWS\".\"NEWS_TITLE\", \"NEWS\".\"NEWS_PUBLICATION\" " +
                        "from \"NEWS\" " +
                        "order by \"NEWS\".\"NEWS_PUBLICATION\" asc, " +
                        "\"NEWS\".\"NEWS_TITLE\" desc");
    }
}