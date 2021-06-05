package fr.ght1pc9kc.juery.api;

import fr.ght1pc9kc.juery.api.pagination.Direction;
import fr.ght1pc9kc.juery.api.pagination.Sort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PaginationTest {
    @Test
    void should_create_all_pagination() {
        Assertions.assertThat(Pagination.of(-1, 0)).isSameAs(Pagination.ALL);
        Assertions.assertThat(Pagination.of(0, -1)).isSameAs(Pagination.ALL);
        Assertions.assertThat(Pagination.of(-1, -1)).isSameAs(Pagination.ALL);
        Assertions.assertThat(Pagination.of(-1, -1, Sort.UNSORTED))
                .isSameAs(Pagination.ALL);
        Assertions.assertThat(Pagination.of(1, -1, Sort.UNSORTED))
                .isSameAs(Pagination.ALL);
    }

    @Test
    void should_create_first_pagination() {
        Sort dummySort = Sort.of(Direction.DESC, "test");
        Assertions.assertThat(Pagination.of(0, 1)).isSameAs(Pagination.FIRST);
        Assertions.assertThat(Pagination.of(0, 1, Sort.UNSORTED)).isSameAs(Pagination.FIRST);
        Assertions.assertThat(Pagination.of(0, 1, dummySort)).isSameAs(Pagination.FIRST);
    }
}
