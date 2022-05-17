module fxribbon {
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;

    exports com.pixelduke.control;
    exports com.pixelduke.control.ribbon;
    exports impl.com.pixelduke.skin;
    exports impl.com.pixelduke.skin.ribbon;

}
