/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

module classifier {
	exports io.github.aid_labor.classifier.main;
	opens io.github.aid_labor.classifier.basis to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.basis.json to com.fasterxml.jackson.databind;
	opens io.github.aid_labor.classifier.gui.util to com.fasterxml.jackson.databind;
	
	requires java.logging;
	
	requires javafx.base;
	requires javafx.controls;
	requires transitive javafx.graphics;
	
	requires transitive com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.annotation;
	requires commons.cli;
	requires java.base;
	requires com.dlsc.gemsfx;
}
