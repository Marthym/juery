module juery.mongo {
    requires juery.api;
    requires static java.desktop;
    requires static lombok;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;

    exports fr.ght1pc9kc.juery.mongo;
}
