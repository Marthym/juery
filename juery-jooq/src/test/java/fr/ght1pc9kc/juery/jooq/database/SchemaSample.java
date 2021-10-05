package fr.ght1pc9kc.juery.jooq.database;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public final class SchemaSample {
    public static final Table<Record> NEWS = DSL.table(DSL.name("NEWS"));
    public static final Field<String> NEWS_ID = DSL.field(DSL.name("NEWS", "NEWS_ID"),
            SQLDataType.VARCHAR(12));
    public static final Field<String> NEWS_TITLE = DSL.field(DSL.name("NEWS", "NEWS_TITLE"),
            SQLDataType.VARCHAR(255));
    public static final Field<LocalDateTime> NEWS_PUBLICATION = DSL.field(DSL.name("NEWS", "NEWS_PUBLICATION"),
            SQLDataType.LOCALDATETIME(6).nullable(false));
    public static final Field<LocalDate> NEWS_PUBDATE = DSL.field(DSL.name("NEWS", "NEWS_PUBDATE"),
            SQLDataType.LOCALDATE.nullable(false));

    public static final Map<String, Field<?>> NEWS_PROPERTIES_MAPPING = Map.of(
            "id", NEWS_ID,
            "title", NEWS_TITLE,
            "publication", NEWS_PUBLICATION,
            "pubdate", NEWS_PUBDATE
    );
}
