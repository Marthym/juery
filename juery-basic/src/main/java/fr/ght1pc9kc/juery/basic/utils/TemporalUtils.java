package fr.ght1pc9kc.juery.basic.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public final class TemporalUtils {
    public static boolean isCreatable(String datetime) {
        ManagedTemporal temporal = detectCreatable(datetime);
        if (temporal != null) {
            try {
                temporal.getParser().parseBest(datetime,
                        LocalDateTime::from, LocalDate::from, Instant::from);
                return true;
            } catch (Exception ignore) {
                // Do nothing, just return false
            }
        }
        return false;
    }

    @SuppressWarnings({"UnnecessaryContinue", "squid:S3776", "squid:S3626"})
    private static ManagedTemporal detectCreatable(String date) {
        boolean hasTime = false;
        boolean hasDate = false;
        for (char c : date.toCharArray()) {
            if (c == 90 /*Z*/ || c == 122 /*z*/) {
                if (hasDate && hasTime) return ManagedTemporal.INSTANT;
                else return null;
            } else if ((c >= 48 && c <= 58) || c == 46 /*.*/) continue;
            else if (c == 45 /*-*/) hasDate = true;
            else if (c == 84 /*T*/ || c == 116 /*t*/) hasTime = true;
            else return null;
        }
        if (hasDate && hasTime) return ManagedTemporal.LOCAL_DATE_TIME;
        else if (hasDate) return ManagedTemporal.LOCAL_DATE;
        else return null;
    }

    public static TemporalAccessor create(String datetime) {
        ManagedTemporal managedTemporal = detectCreatable(datetime);
        if (managedTemporal == null) {
            throw new IllegalArgumentException("Not a managed temporal !");
        }

        return managedTemporal.getParser().parseBest(datetime,
                LocalDateTime::from, LocalDate::from, Instant::from);
    }

    private enum ManagedTemporal {
        LOCAL_DATE_TIME(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        LOCAL_DATE(DateTimeFormatter.ISO_LOCAL_DATE),
        INSTANT(DateTimeFormatter.ISO_INSTANT);

        private final DateTimeFormatter formatter;

        ManagedTemporal(DateTimeFormatter formatter) {
            this.formatter = formatter;
        }

        public DateTimeFormatter getParser() {
            return formatter;
        }
    }
}
