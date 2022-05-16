module commons.digester {
    requires commons.beanutils;
    requires java.desktop;

    requires transitive commons.logging;
    requires transitive java.xml;

    exports org.apache.commons.digester;
    exports org.apache.commons.digester.annotations;
    exports org.apache.commons.digester.annotations.handlers;
    exports org.apache.commons.digester.annotations.internal;
    exports org.apache.commons.digester.annotations.providers;
    exports org.apache.commons.digester.annotations.reflect;
    exports org.apache.commons.digester.annotations.rules;
    exports org.apache.commons.digester.annotations.spi;
    exports org.apache.commons.digester.annotations.utils;
    exports org.apache.commons.digester.parser;
    exports org.apache.commons.digester.plugins;
    exports org.apache.commons.digester.plugins.strategies;
    exports org.apache.commons.digester.substitution;
    exports org.apache.commons.digester.xmlrules;

}
