/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.MissingResourceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.aid_labor.classifier.basis.Sprache.SprachDatei;
import javafx.beans.property.ReadOnlyStringProperty;

class SpracheTest {
	
	private SprachDatei deutsch;
	private SprachDatei englisch;
	private Sprache sprache;
	private ReadOnlyStringProperty property;
	private final String schluessel = "Sprache";
	private final String textDE = "Deutsch";
	private final String textEN = "English";
	
	@BeforeEach
	void setUp() throws Exception {
		Path dateiDE = Files.createTempFile("test", "_de_de.properties");
		try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(dateiDE))) {
			pw.println(schluessel + " = " + textDE);
		}
		
		Path dateiEN = Files.createTempFile("test", "_en_en.properties");
		try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(dateiEN))) {
			pw.println(schluessel + " = " + textEN);
		}
		
		this.deutsch = new SprachDatei(dateiDE, Locale.GERMAN);
		this.englisch = new SprachDatei(dateiEN, Locale.ENGLISH);
	}
	
	@Test
	void testDeutsch() {
		assertDoesNotThrow(() -> sprache = new Sprache(deutsch));
		assertEquals(textDE, sprache.getText(schluessel));
		assertThrows(MissingResourceException.class, () -> sprache.getText("blabla"));
		assertDoesNotThrow(() -> sprache.getText("blabla", "alternativ"));
		assertEquals(textDE, sprache.getText(schluessel));
		assertDoesNotThrow(() -> property = sprache.getTextProperty(schluessel));
		assertEquals(textDE, property.get());
		assertThrows(MissingResourceException.class, () -> sprache.getTextProperty("blabla"));
		assertDoesNotThrow(() -> sprache.getTextProperty("blabla", "alternativ"));
		assertEquals(textDE, sprache.getText(schluessel, "alternativ"));
		assertEquals("bla", sprache.getText("blabla", "bla"));
	}
	
	@Test
	void testSprachWechsel() {
		assertDoesNotThrow(() -> sprache = new Sprache(deutsch));
		assertEquals(textDE, sprache.getText(schluessel));
		assertDoesNotThrow(() -> sprache.nutzeSprache(englisch));
		assertEquals(textEN, sprache.getText(schluessel));
	}
	
	@Test
	void testSprachHinzufuegen() {
		assertDoesNotThrow(() -> sprache = new Sprache());
		assertDoesNotThrow(() -> sprache.nutzeSprache(deutsch));
		assertDoesNotThrow(() -> sprache.nutzeSprache(englisch));
	}
	
}