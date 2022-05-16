module fr.brouillard.oss.cssfx {
    requires java.logging;
    requires jdk.unsupported;

    requires transitive javafx.base;
    requires transitive javafx.graphics;

    exports fr.brouillard.oss.cssfx;
    exports fr.brouillard.oss.cssfx.api;
    exports fr.brouillard.oss.cssfx.impl;
    exports fr.brouillard.oss.cssfx.impl.events;
    exports fr.brouillard.oss.cssfx.impl.log;
    exports fr.brouillard.oss.cssfx.impl.monitoring;

}
