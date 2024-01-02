package fr.ght1pc9kc.juery.database;

import java.util.List;
import java.util.Map;

public class SchemaSample {

    public static final String ID = "myID";
    public static final List<String> IDS_FIELD = List.of(ID);
    public static final Map<String, String> PROPERTIES_MAPPING = Map.of(
            ID, "_id",
            "pubdate", "pubdate",
            "title", "title",
            "id", "id");
}
