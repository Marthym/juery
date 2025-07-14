package fr.ght1pc9kc.juery.mongo.pagination;

import fr.ght1pc9kc.juery.api.Pagination;
import fr.ght1pc9kc.juery.api.pagination.Order;
import fr.ght1pc9kc.juery.api.pagination.Sort;
import org.assertj.core.api.Assertions;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.junit.jupiter.api.Test;

import java.util.Map;

class MongoPaginationTest {
    @Test
    void should_create_sorting() {
        Bson actual = MongoPagination.toSortBson(Pagination.of(0, 20, Sort.of(Order.asc("title"), Order.desc("obiwan"))), Map.of());

        Assertions.assertThat(actual.toBsonDocument().toJson(JsonWriterSettings.builder().indent(true).build()))
                .isEqualTo("""
                        {
                          "title": 1,
                          "obiwan": -1
                        }""");
    }
}