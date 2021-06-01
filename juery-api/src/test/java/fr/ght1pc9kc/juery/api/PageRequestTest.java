package fr.ght1pc9kc.juery.api;

import fr.ght1pc9kc.juery.api.pagination.Direction;
import fr.ght1pc9kc.juery.api.pagination.Sort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;

class PageRequestTest {
    @Test
    void should_create_page_request() {
        PageRequest actual = PageRequest.of(1, 5);

        Assertions.assertThat(actual.pagination().page()).isEqualTo(1);
        Assertions.assertThat(actual.pagination().size()).isEqualTo(5);
        Assertions.assertThat(actual.filter()).isEqualTo(Criteria.none());
        Assertions.assertThat(actual.pagination().sort()).isEqualTo(Sort.of());
    }

    @Test
    void should_create_page_request_zero() {
        PageRequest actual = PageRequest.of(-1, 10);

        Assertions.assertThat(actual).isEqualTo(PageRequest.all());
    }

    @Test
    void should_create_page_request_one() {
        PageRequest actual = PageRequest.one(Criteria.none());

        assertAll(
                () -> Assertions.assertThat(actual.pagination()).isSameAs(Pagination.FIRST),
                () -> Assertions.assertThat(actual.filter()).isEqualTo(Criteria.none())
        );
    }

    @Test
    void shoudl_add_criteria_to_pagerequest() {
        PageRequest actual = PageRequest.all(Criteria.property("name").eq("Obiwan"))
                .and(Criteria.property("faction").eq("jedi"));

        Assertions.assertThat(actual.pagination().page()).isEqualTo(-1);
        Assertions.assertThat(actual.pagination().size()).isEqualTo(-1);
        Assertions.assertThat(actual.filter()).isEqualTo(Criteria.and(
                Criteria.property("name").eq("Obiwan"),
                Criteria.property("faction").eq("jedi")
        ));
        Assertions.assertThat(actual.pagination().sort()).isEqualTo(Sort.of());
    }

    @Test
    void should_build_full_page_request() {
        PageRequest actual = PageRequest.of(
                Pagination.of(1, 10, Sort.of(Direction.ASC, "name")),
                Criteria.property("faction").eq("jedi")
        );

        Assertions.assertThat(actual.pagination().page()).isEqualTo(1);
        Assertions.assertThat(actual.pagination().size()).isEqualTo(10);
        Assertions.assertThat(actual.filter()).isEqualTo(Criteria.property("faction").eq("jedi"));
        Assertions.assertThat(actual.pagination().sort()).isEqualTo(Sort.of(Direction.ASC, "name"));
    }
}
