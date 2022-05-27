/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

module classifier {
	exports io.github.aid_labor.classifier.main;
	opens io.github.aid_labor.classifier.basis to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.basis.json to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.basis.projekt to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.gui.util to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.uml to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.uml.eigenschaften to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.uml.klassendiagramm to com.fasterxml.jackson.databind;
	
	requires java.logging;

	requires javafx.base;
	requires javafx.controls;
	requires transitive javafx.graphics;
	
	requires transitive com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.annotation;
	requires commons.cli;
	requires java.base;
	requires fxribbon;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.ikonli.core;
	requires org.kordamp.ikonli.remixicon;
	requires org.kordamp.ikonli.carbonicons;
	requires org.kordamp.ikonli.typicons;
	requires org.kordamp.ikonli.whhg;
	requires org.kordamp.ikonli.bootstrapicons;
	requires com.dlsc.gemsfx;
	requires fr.brouillard.oss.cssfx;
	requires org.controlsfx.controls;
}
