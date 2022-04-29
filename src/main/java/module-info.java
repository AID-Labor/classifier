/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

module classifier {
	exports io.github.aid_labor.classifier.main;
	
	opens io.github.aid_labor.classifier.basis.json to com.fasterxml.jackson.databind;
	
	requires javafx.base;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.annotation;
	requires java.logging;
}
