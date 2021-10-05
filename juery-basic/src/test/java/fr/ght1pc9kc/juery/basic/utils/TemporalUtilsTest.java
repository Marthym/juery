package fr.ght1pc9kc.juery.basic.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.format.DateTimeParseException;
import java.util.Objects;

class TemporalUtilsTest {

    @ParameterizedTest
    @CsvSource({
            "2021-08-22T12:42:24, true",
            "2021-08-22T12:42:24Z, true",
            "2021-08-22T12:42:24.542Z, true",
            "2021-08-22, true",
            "2021-08-22T124224.542Z, false",
            "20210822T12:42:24.542Z, false",
            "20210822, false",
            "12:42:24, false",
            "2021/08/22T12:42:24, false",
            "20/08/2021, false",
    })
    void should_test_string_date(String tested, boolean expected) {
        Assertions.assertThat(TemporalUtils.isCreatable(tested)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "2021-08-22T12:42:24, LocalDateTime, 2021-08-22T12:42:24",
            "2021-08-22T12:42:24Z, Instant, 2021-08-22T12:42:24Z",
            "2021-08-22T12:42:24.542Z, Instant, 2021-08-22T12:42:24.542Z",
            "2021-08-22, LocalDate, 2021-08-22",
    })
    void should_create_date_from_string(String tested, String expectedClass, String expected) throws ClassNotFoundException {
        Assertions.assertThat(TemporalUtils.create(tested))
                .isInstanceOf(Class.forName("java.time." + expectedClass))
                .extracting(Objects::toString)
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "2021-08-22T124224.542Z",
            "20210822T12:42:24.542Z",
            "20210822",
            "12:42:24",
            "2021/08/22T12:42:24",
            "20/08/2021",
    })
    void should_throw_exception(String tested) {
        Assertions.assertThatThrownBy(() -> TemporalUtils.create(tested))
                .isInstanceOfAny(DateTimeParseException.class, IllegalArgumentException.class);
    }
}