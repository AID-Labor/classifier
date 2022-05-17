module commons.validator {
    requires commons.beanutils;
    requires commons.logging;

    requires transitive commons.collections;
    requires transitive commons.digester;
    requires transitive java.xml;

    exports org.apache.commons.validator;
    exports org.apache.commons.validator.routines;
    exports org.apache.commons.validator.routines.checkdigit;
    exports org.apache.commons.validator.util;

}
