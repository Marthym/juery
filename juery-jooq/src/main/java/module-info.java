module juery.jooq {
    requires juery.api;
    requires org.jooq;
    requires static lombok;
    requires static java.desktop;

    exports fr.ght1pc9kc.juery.jooq.filter;
    exports fr.ght1pc9kc.juery.jooq.pagination;
}