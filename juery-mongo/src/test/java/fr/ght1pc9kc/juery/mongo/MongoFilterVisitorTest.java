package fr.ght1pc9kc.juery.mongo;

import com.mongodb.client.model.Filters;
import fr.ght1pc9kc.juery.api.Criteria;
import fr.ght1pc9kc.juery.database.SchemaSample;
import fr.ght1pc9kc.juery.mongo.filter.MongoFilterVisitor;
import org.assertj.core.api.SoftAssertions;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static fr.ght1pc9kc.juery.database.SchemaSample.IDS_FIELD;
import static fr.ght1pc9kc.juery.database.SchemaSample.PROPERTIES_MAPPING;
import static fr.ght1pc9kc.juery.mongo.MongoFilterOptions.DEFAULT_OPTIONS;
import static fr.ght1pc9kc.juery.mongo.MongoFilterOptions.USE_DEFAULT_VALUES;
import static org.assertj.core.api.Assertions.assertThat;


class MongoFilterVisitorTest {

    private static final LocalDateTime NOW = LocalDateTime.parse("2020-12-11T10:20:42");

    @Test
    void should_create_visitor() {
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(MongoFilterVisitor.getMongoFilter()).isNotNull();
            soft.assertThat(MongoFilterVisitor.getMongoFilter(IDS_FIELD, PROPERTIES_MAPPING, DEFAULT_OPTIONS)).isNotNull();
            soft.assertThat(MongoFilterVisitor.getMongoFilter(PROPERTIES_MAPPING, DEFAULT_OPTIONS)).isNotNull();
            soft.assertThat(MongoFilterVisitor.getMongoFilter(IDS_FIELD)).isNotNull();
        });
    }

    @ParameterizedTest
    @MethodSource("provideCriteriaWithDefault")
    void should_create_condition_from_criteria_with_default_values(int visitorOptions, Criteria criteria, String expected) {
        MongoFilterVisitor tested = MongoFilterVisitor.getMongoFilter(IDS_FIELD, PROPERTIES_MAPPING, visitorOptions);
        Bson actual = criteria.accept(tested);
        assertThat(actual.toString()).hasToString(expected);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideCriteriaWithDefault() {
        return Stream.of(
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("pubdate").gt(NOW),
                        Filters.gt("pubdate", NOW).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("pubdate").gt(NOW.toLocalDate()),
                        Filters.gt("pubdate", NOW.toLocalDate()).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property(SchemaSample.ID).gt("632046635f85998fed60c32e"),
                        Filters.gt("_id", new ObjectId("632046635f85998fed60c32e")).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("obiwanKenobi").gt(NOW),
                        Filters.gt("obiwanKenobi", NOW).toString()),
                Arguments.of(DEFAULT_OPTIONS ^ USE_DEFAULT_VALUES,
                        Criteria.property("obiwanKenobi").gt(NOW),
                        Filters.empty().toString()),

                Arguments.of(DEFAULT_OPTIONS, Criteria.property("title").eq("title"),
                        Filters.eq("title", "title").toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("id").eq(1222),
                        Filters.eq("id", 1222).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property(SchemaSample.ID).eq("632046635f85998fed60c32e"),
                        Filters.eq("_id", new ObjectId("632046635f85998fed60c32e")).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("obiwanKenobi").eq(NOW),
                        Filters.eq("obiwanKenobi", NOW).toString()),
                Arguments.of(DEFAULT_OPTIONS ^ USE_DEFAULT_VALUES,
                        Criteria.property("obiwanKenobi").eq(NOW),
                        Filters.empty().toString()),

                Arguments.of(DEFAULT_OPTIONS, Criteria.property("id").gte(1222),
                        Filters.gte("id", 1222).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property(SchemaSample.ID).gte("632046635f85998fed60c32e"),
                        Filters.gte("_id", new ObjectId("632046635f85998fed60c32e")).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("obiwanKenobi").gte("632046635f85998fed60c32e"),
                        Filters.gte("obiwanKenobi", new ObjectId("632046635f85998fed60c32e")).toString()),
                Arguments.of(DEFAULT_OPTIONS ^ USE_DEFAULT_VALUES,
                        Criteria.property("obiwanKenobi").gte("632046635f85998fed60c32e"),
                        Filters.empty().toString()),

                Arguments.of(DEFAULT_OPTIONS, Criteria.property("id").lt(1222),
                        Filters.lt("id", 1222).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property(SchemaSample.ID).lt("632046635f85998fed60c32e"),
                        Filters.lt("_id", new ObjectId("632046635f85998fed60c32e")).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("obiwanKenobi").lt("632046635f85998fed60c32e"),
                        Filters.lt("obiwanKenobi", new ObjectId("632046635f85998fed60c32e")).toString()),
                Arguments.of(DEFAULT_OPTIONS ^ USE_DEFAULT_VALUES,
                        Criteria.property("obiwanKenobi").lt("632046635f85998fed60c32e"),
                        Filters.empty().toString()),

                Arguments.of(DEFAULT_OPTIONS, Criteria.property("id").lte(1222),
                        Filters.lte("id", 1222).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property(SchemaSample.ID).lte("632046635f85998fed60c32e"),
                        Filters.lte("_id", new ObjectId("632046635f85998fed60c32e")).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("obiwanKenobi").lte("632046635f85998fed60c32e"),
                        Filters.lte("obiwanKenobi", new ObjectId("632046635f85998fed60c32e")).toString()),
                Arguments.of(DEFAULT_OPTIONS ^ USE_DEFAULT_VALUES,
                        Criteria.property("obiwanKenobi").lte("632046635f85998fed60c32e"),
                        Filters.empty().toString()),

                Arguments.of(DEFAULT_OPTIONS, Criteria.property("id").in(List.of(1, 2)),
                        Filters.in("id", List.of(1, 2)).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property(SchemaSample.ID).in("632046635f85998fed60c32e", "632046635f85998fed60c32e"),
                        Filters.in("_id", List.of(
                                new ObjectId("632046635f85998fed60c32e"),
                                new ObjectId("632046635f85998fed60c32e")
                        )).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("obiwanKenobi").in("632046635f85998fed60c32e", "632046635f85998fed60c32e"),
                        Filters.in("obiwanKenobi", List.of(
                                new ObjectId("632046635f85998fed60c32e"),
                                new ObjectId("632046635f85998fed60c32e")
                        )).toString()),
                Arguments.of(DEFAULT_OPTIONS ^ USE_DEFAULT_VALUES,
                        Criteria.property("obiwanKenobi").in("632046635f85998fed60c32e", "632046635f85998fed60c32e"),
                        Filters.empty().toString()),

                Arguments.of(DEFAULT_OPTIONS, Criteria.or(Criteria.property("id").eq(1),
                                Criteria.property("title").eq("title")),
                        Filters.or(Filters.eq("id", 1), Filters.eq("title", "title")).toString()),
                Arguments.of(DEFAULT_OPTIONS ^ USE_DEFAULT_VALUES,
                        Criteria.or(
                                Criteria.property("obiwanKenobi").eq(1),
                                Criteria.property("title").eq("title")),
                        Filters.eq("title", "title").toString()),
                Arguments.of(DEFAULT_OPTIONS ^ USE_DEFAULT_VALUES,
                        Criteria.or(
                                Criteria.property("obiwanKenobi").eq(1),
                                Criteria.property("obiwanKenobi").eq("title")),
                        Filters.empty().toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.not(Criteria.property("id").in(List.of(1, 2))),
                        Filters.not(Filters.in("id", List.of(1, 2))).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.not(Criteria.property(SchemaSample.ID).in(List.of("632046635f85998fed60c32e", "632046635f85998fed60c32e"))),
                        Filters.not(Filters.in(
                                "_id",
                                List.of(new ObjectId("632046635f85998fed60c32e"),
                                        new ObjectId("632046635f85998fed60c32e")))).toString()),
                Arguments.of(DEFAULT_OPTIONS ^ USE_DEFAULT_VALUES,
                        Criteria.and(
                                Criteria.property("obiwanKenobi").eq(1),
                                Criteria.property("title").eq("title")),
                        Filters.eq("title", "title").toString()),
                Arguments.of(DEFAULT_OPTIONS ^ USE_DEFAULT_VALUES,
                        Criteria.and(
                                Criteria.property("obiwanKenobi").eq(1),
                                Criteria.property("obiwanKenobi").eq("title")),
                        Filters.empty().toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.and(Criteria.property("id").eq(1),
                                Criteria.property("title").eq("title")),
                        Filters.and(Filters.eq("id", 1), Filters.eq("title", "title")).toString()),

                Arguments.of(DEFAULT_OPTIONS, Criteria.none(),
                        Filters.empty().toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property(SchemaSample.ID).lt("632046635f85998fed60c32e"),
                        Filters.lt("_id", new ObjectId("632046635f85998fed60c32e")).toString()),

                Arguments.of(DEFAULT_OPTIONS, Criteria.property("title").contains("The Force"),
                        Filters.regex("title", Pattern.compile(".*\\QThe Force\\E.*")).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("title").startWith("The Force"),
                        Filters.regex("title", Pattern.compile("^\\QThe Force\\E.*")).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("obiwanKenobi").startWith("The Force"),
                        Filters.regex("obiwanKenobi", Pattern.compile("^\\QThe Force\\E.*")).toString()),
                Arguments.of(DEFAULT_OPTIONS ^ USE_DEFAULT_VALUES,
                        Criteria.property("obiwanKenobi").startWith("The Force"),
                        Filters.empty().toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("title").endWith("The Force"),
                        Filters.regex("title", Pattern.compile(".*\\QThe Force\\E$")).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("obiwanKenobi").endWith("The Force"),
                        Filters.regex("obiwanKenobi", Pattern.compile(".*\\QThe Force\\E$")).toString()),
                Arguments.of(DEFAULT_OPTIONS ^ USE_DEFAULT_VALUES,
                        Criteria.property("obiwanKenobi").endWith("The Force"),
                        Filters.empty().toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("title").contains("The.*Force"),
                        Filters.regex("title", Pattern.compile(".*\\QThe.*Force\\E.*")).toString()),
                Arguments.of(DEFAULT_OPTIONS, Criteria.property("obiwanKenobi").contains("The.*Force"),
                        Filters.regex("obiwanKenobi", Pattern.compile(".*\\QThe.*Force\\E.*")).toString()),
                Arguments.of(DEFAULT_OPTIONS ^ USE_DEFAULT_VALUES,
                        Criteria.property("obiwanKenobi").contains("The.*Force"),
                        Filters.empty().toString())
        );
    }
}
