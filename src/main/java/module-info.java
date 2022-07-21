import io.github.aid_labor.classifier.java.JavaProvider;
import io.github.aid_labor.classifier.uml.programmierung.Programmiersprache;

/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

/**
 * Graphischer UML-Klassendiagramm Editor mit javaFX.
 * 
 * @author Tim Muehle
 * 
 * @provides Programmiersprache Bereitstellung der Anforderungen und Funktionen der Programmierprache Java für das
 *           UML-Klassendiagramm
 * @uses Programmiersprache Service zur Bereitstellung von Anforderungen und Konfigurationen von Programmiersprachen im
 *       Zusammenhang mit dem UML-Klassendiagramm
 * 		
 */
module classifier {
	exports io.github.aid_labor.classifier.main;
	exports io.github.aid_labor.classifier.basis.json;
	exports io.github.aid_labor.classifier.uml.klassendiagramm;
	exports io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften;
	exports io.github.aid_labor.classifier.uml.programmierung;
	
	opens io.github.aid_labor.classifier.basis to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.basis.json to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.basis.projekt to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.gui.util to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.uml to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.uml.klassendiagramm to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.uml.programmierung to com.fasterxml.jackson.databind;
	
	requires transitive java.logging;
	
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
	requires javafx.web;
	requires java.xml;
	requires javafx.swing;
	
	uses Programmiersprache;
	provides Programmiersprache with JavaProvider;
}
