package fr.ght1pc9kc.juery.jooq.pagination;

import fr.ght1pc9kc.juery.api.PageRequest;
import fr.ght1pc9kc.juery.api.pagination.Order;
import fr.ght1pc9kc.juery.api.pagination.Sort;
import org.assertj.core.api.Assertions;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Select;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static fr.ght1pc9kc.juery.jooq.database.SchemaSample.*;

class JooqPaginationTest {

    @Test
    void should_limit_query() {
        PageRequest pageRequest = PageRequest.builder()
                .page(5)
                .size(10)
                .build();
        Select<Record1<Integer>> actual = JooqPagination.apply(pageRequest, DSL.selectCount().from(NEWS));
        Assertions.assertThat(actual.getSQL(ParamType.INLINED))
                .isEqualTo("select count(*) from \"PUBLIC\".\"NEWS\" limit 10 offset 50");
    }

    @Test
    void should_sort_query() {
        Assertions.assertThat(NEWS_TITLE).isNotNull();
        Assertions.assertThat(NEWS_PUBLICATION).isNotNull();

        PageRequest pageRequest = PageRequest.builder()
                .sort(Sort.of(
                        Order.asc("publication"),
                        Order.desc("title")))
                .build();
        Select<Record2<String, LocalDateTime>> actual = JooqPagination.apply(
                pageRequest, NEWS_PROPERTIES_MAPPING, DSL.select(NEWS_TITLE, NEWS_PUBLICATION).from(NEWS));
        Assertions.assertThat(actual.getSQL(ParamType.INLINED))
                .isEqualTo("select \"PUBLIC\".\"NEWS\".\"NEWS_TITLE\", \"PUBLIC\".\"NEWS\".\"NEWS_PUBLICATION\" " +
                        "from \"PUBLIC\".\"NEWS\" " +
                        "order by \"PUBLIC\".\"NEWS\".\"NEWS_PUBLICATION\" asc, " +
                        "\"PUBLIC\".\"NEWS\".\"NEWS_TITLE\" desc");
    }
}