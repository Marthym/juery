module juery.basic {
    requires static lombok;
    requires juery.api;
    requires java.desktop; // Need for lombok.anyConstructor.addConstructorProperties

    exports fr.ght1pc9kc.juery.basic;
    exports fr.ght1pc9kc.juery.basic.filter;
}