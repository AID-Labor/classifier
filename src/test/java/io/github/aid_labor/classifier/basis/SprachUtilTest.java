/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.aid_labor.classifier.basis.Sprache.SprachDatei;


class SprachUtilTest {
	static Path ordner;
	static Path de;
	static Path en;
	static Path dateiDE;
	static Path dateiEN;
	
	private static final String praefix = "test";
	private static final String schluessel = "Sprache";
	private static final String textDE = "Deutsch";
	private static final String textEN = "English";
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		ordner = Files.createTempDirectory("Sprache");
		de = Files.createDirectory(ordner.resolve("de"));
		en = Files.createDirectory(ordner.resolve("en"));
		
		dateiDE = Files.createTempFile(de, praefix, "_de.properties");
		try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(dateiDE))) {
			pw.println(schluessel + " = " + textDE);
		}
		
		dateiEN = Files.createTempFile(en, praefix, "_en.properties");
		try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(dateiEN))) {
			pw.println(schluessel + " = " + textEN);
		}
	}
	
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		try {
			Files.delete(dateiEN);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Files.delete(dateiDE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Files.delete(en);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Files.delete(de);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Files.delete(ordner);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testSucheSprachdateien() throws IOException {
		var dateiMap = SprachUtil.sucheSprachdateien(ordner, praefix);
		
		assertEquals(2, dateiMap.size());
	}
	
	@Test
	void testSortiereSprachdateien() throws IOException {
		Map<Locale, SprachDatei> dateiMap = new HashMap<>();
		var sprachDateiDE = new SprachDatei(dateiDE, Locale.forLanguageTag("de"));
		var sprachDateiEN = new SprachDatei(dateiEN, Locale.forLanguageTag("en"));
		dateiMap.put(sprachDateiEN.sprache(), sprachDateiEN);
		dateiMap.put(sprachDateiDE.sprache(), sprachDateiDE);
		
		var sortiert = SprachUtil.sortiereSprachdateien(dateiMap, Locale.GERMAN,
				Locale.ENGLISH);
		assertEquals(2, sortiert.size());
		assertEquals(sprachDateiDE, sortiert.poll());
		assertEquals(sprachDateiEN, sortiert.poll());
		assertTrue(sortiert.isEmpty());
	}
	
	@Test
	void testSetUpSprache() throws IOException {
		Sprache sprache = new Sprache();
		assertTrue(SprachUtil.setUpSprache(sprache, ordner, praefix, Locale.GERMAN,
				Locale.ENGLISH));
		assertEquals(Locale.GERMAN, sprache.getAktuelleSprache());
		assertTrue(SprachUtil.setUpSprache(sprache, ordner, praefix, Locale.ENGLISH,
				Locale.GERMAN));
		assertEquals(Locale.ENGLISH, sprache.getAktuelleSprache());
	}
}