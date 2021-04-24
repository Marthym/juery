package fr.ght1pc9kc.juery.api;

import fr.ght1pc9kc.juery.api.pagination.Direction;
import fr.ght1pc9kc.juery.api.pagination.Sort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PageRequestTest {
    @Test
    void should_create_page_request() {
        PageRequest actual = PageRequest.of(1, 5);

        Assertions.assertThat(actual.page).isEqualTo(1);
        Assertions.assertThat(actual.size).isEqualTo(5);
        Assertions.assertThat(actual.filter).isEqualTo(Criteria.none());
        Assertions.assertThat(actual.sort).isEqualTo(Sort.of());
    }

    @Test
    void should_create_page_request_zero() {
        PageRequest actual = PageRequest.of(-1, 10);

        Assertions.assertThat(actual).isEqualTo(PageRequest.all());
    }

    @Test
    void shoudl_add_criteria_to_pagerequest() {
        PageRequest actual = PageRequest.all(Criteria.property("name").eq("Obiwan"))
                .and(Criteria.property("faction").eq("jedi"));

        Assertions.assertThat(actual.page).isEqualTo(-1);
        Assertions.assertThat(actual.size).isEqualTo(-1);
        Assertions.assertThat(actual.filter).isEqualTo(Criteria.and(
                Criteria.property("name").eq("Obiwan"),
                Criteria.property("faction").eq("jedi")
        ));
        Assertions.assertThat(actual.sort).isEqualTo(Sort.of());
    }

    @Test
    void should_build_full_page_request() {
        PageRequest actual = PageRequest.builder()
                .page(1)
                .size(10)
                .sort(Sort.of(Direction.ASC, "name"))
                .filter(Criteria.property("faction").eq("jedi"))
                .build();

        Assertions.assertThat(actual.page).isEqualTo(1);
        Assertions.assertThat(actual.size).isEqualTo(10);
        Assertions.assertThat(actual.filter).isEqualTo(Criteria.property("faction").eq("jedi"));
        Assertions.assertThat(actual.sort).isEqualTo(Sort.of(Direction.ASC, "name"));
    }
}
