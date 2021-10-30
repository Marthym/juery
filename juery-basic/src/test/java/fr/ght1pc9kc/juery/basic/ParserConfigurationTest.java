package fr.ght1pc9kc.juery.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ParserConfigurationTest {
    @Test
    void should_create_default_configuration() {
        ParserConfiguration actual = ParserConfiguration.builder().build();
        Assertions.assertThat(actual.pageParameter()).isEqualTo("_p");
        Assertions.assertThat(actual.sizeParameter()).isEqualTo("_pp");
        Assertions.assertThat(actual.fromParameter()).isEqualTo("_from");
        Assertions.assertThat(actual.toParameter()).isEqualTo("_to");
        Assertions.assertThat(actual.sortParameter()).isEqualTo("_s");
        Assertions.assertThat(actual.maxPageSize()).isEqualTo(100);
        Assertions.assertThat(actual.excludeFilterParameters()).isEqualTo(Set.of(
                "_p", "_pp", "_s", "_from", "_to"
        ));
    }

    @Test
    void should_specify_configuration() {
        ParserConfiguration actual = ParserConfiguration.builder()
                .page("1")
                .size("2")
                .from("3")
                .to("4")
                .sort("5")
                .maxPageSize(6)
                .build();
        Assertions.assertThat(actual.pageParameter()).isEqualTo("1");
        Assertions.assertThat(actual.sizeParameter()).isEqualTo("2");
        Assertions.assertThat(actual.fromParameter()).isEqualTo("3");
        Assertions.assertThat(actual.toParameter()).isEqualTo("4");
        Assertions.assertThat(actual.sortParameter()).isEqualTo("5");
        Assertions.assertThat(actual.maxPageSize()).isEqualTo(6);
        Assertions.assertThat(actual.excludeFilterParameters()).isEqualTo(Set.of(
                "1", "2", "3", "4", "5"
        ));
    }
}