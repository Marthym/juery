module juery.mongo {
    requires juery.api;
    requires static java.desktop;
    requires static lombok;
    requires static org.jetbrains.annotations;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;

    exports fr.ght1pc9kc.juery.mongo;
    exports fr.ght1pc9kc.juery.mongo.filter;
    exports fr.ght1pc9kc.juery.mongo.pagination;
}
