package fr.ght1pc9kc.juery.mongo;

import com.mongodb.client.model.Filters;
import fr.ght1pc9kc.juery.api.Criteria;
import fr.ght1pc9kc.juery.database.SchemaSample;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static fr.ght1pc9kc.juery.database.SchemaSample.IDS_FIELD;
import static fr.ght1pc9kc.juery.database.SchemaSample.PROPERTIES_MAPPING;
import static org.assertj.core.api.Assertions.assertThat;


class MongoFilterVisitorTest {

    private static final LocalDateTime NOW = LocalDateTime.parse("2020-12-11T10:20:42");

    private final MongoFilterVisitor tested = new MongoFilterVisitor(IDS_FIELD, PROPERTIES_MAPPING);

    @ParameterizedTest
    @MethodSource("provideSCriteria")
    void should_create_condition_from_criteria(Criteria criteria, Bson expected) {
        Bson actual = criteria.accept(tested);
        assertThat(actual).isEqualTo(expected);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideSCriteria() {
        return Stream.of(
                Arguments.of(Criteria.property("pubdate").gt(NOW),
                        Filters.gt("pubdate", NOW)),
                Arguments.of(Criteria.property("pubdate").gt(NOW.toLocalDate()),
                        Filters.gt("pubdate", NOW.toLocalDate())),
                Arguments.of(Criteria.property("title").eq("title"),
                        Filters.eq("title", "title")),
                Arguments.of(Criteria.property("id").eq(1222),
                        Filters.eq("id", 1222)),
                Arguments.of(Criteria.property("id").gte(1222),
                        Filters.gte("id", 1222)),
                Arguments.of(Criteria.property("id").lt(1222),
                        Filters.lt("id", 1222)),
                Arguments.of(Criteria.property("id").in(List.of(1, 2)),
                        Filters.in("id", List.of(1, 2))),
                Arguments.of(Criteria.or(Criteria.property("id").eq(1),
                                Criteria.property("title").eq("title")),
                        Filters.or(Filters.eq("id", 1), Filters.eq("title", "title"))),
                Arguments.of(Criteria.not(Criteria.property("id").in(List.of(1, 2))),
                        Filters.not(Filters.in("id", List.of(1, 2)))),
                Arguments.of(Criteria.not(Criteria.property(SchemaSample.ID).in(List.of("632046635f85998fed60c32e", "632046635f85998fed60c32e"))),
                        Filters.not(Filters.in(
                                "_id",
                                List.of(new ObjectId("632046635f85998fed60c32e"),
                                        new ObjectId("632046635f85998fed60c32e"))))),
                Arguments.of(Criteria.and(Criteria.property("id").eq(1),
                                Criteria.property("title").eq("title")),
                        Filters.and(Filters.eq("id", 1), Filters.eq("title", "title"))),
                Arguments.of(Criteria.none(),
                        Filters.empty()),
                Arguments.of(Criteria.property(SchemaSample.ID).lt("632046635f85998fed60c32e"),
                        Filters.lt("_id", new ObjectId("632046635f85998fed60c32e")))
        );
    }

}
