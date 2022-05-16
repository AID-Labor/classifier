module commons.beanutils {
    requires commons.logging;

    requires transitive commons.collections;
    requires transitive java.desktop;
    requires transitive java.sql;

    exports org.apache.commons.beanutils;
    exports org.apache.commons.beanutils.converters;
    exports org.apache.commons.beanutils.expression;
    exports org.apache.commons.beanutils.locale;
    exports org.apache.commons.beanutils.locale.converters;

}
