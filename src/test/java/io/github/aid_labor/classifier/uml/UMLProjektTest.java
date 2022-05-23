/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;


class UMLProjektTest {
	private UMLProjekt projekt;
	
	@BeforeEach
	void setUp() throws Exception {
		projekt = new UMLProjekt("Test", Programmiersprache.Java, false);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		projekt = null;
	}
	
	@Test
	void testName() {
		testName(projekt);
	}
	
	void testName(UMLProjekt projekt) {
		assertEquals("Test", projekt.getName());
		ReadOnlyStringProperty nameProperty = projekt.nameProperty();
		assertNotNull(nameProperty);
		assertEquals("Test", nameProperty.get());
		projekt.setName("NeuerName");
		assertEquals("NeuerName", projekt.getName());
		assertEquals("NeuerName", nameProperty.get());
		assertFalse(projekt.istGespeichertProperty().get());
		assertThrows(NullPointerException.class, () -> projekt.setName(null));
	}
	
	@Test
	void testSpeicherOrt() {
		assertNull(projekt.getSpeicherort());
		testSpeicherOrt(projekt);
	}
	
	void testSpeicherOrt(UMLProjekt projekt) {
		Path datei = null;
		try {
			datei = Files.createTempFile("test", "projekt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		projekt.setSpeicherort(datei);
		assertEquals(datei, projekt.getSpeicherort());
		projekt.setSpeicherort(null);
		assertNull(projekt.getSpeicherort());
		projekt.speichern(datei);
		assertEquals(datei, projekt.getSpeicherort());
	}
	
	@Test
	void testSpeichernUndOeffnen() {
		ReadOnlyBooleanProperty gespeichert = projekt.istGespeichertProperty();
		assertFalse(gespeichert.get());
		assertThrows(IllegalStateException.class, () -> projekt.speichern());
		
		Path datei = null;
		try {
			datei = Files.createTempFile("test", ".projekt");
			
			assertTrue(projekt.speichern(datei));
			assertTrue(gespeichert.get());
			
			projekt.getDiagrammElemente().addAll(new TestElement(), new TestElement());
			
			assertFalse(gespeichert.get());
			assertTrue(projekt.speichern());
			Files.newBufferedReader(datei).lines().forEach(zeile -> System.out.println(zeile));
			assertTrue(gespeichert.get());
			
			UMLProjekt geoeffnet = UMLProjekt.ausDateiOeffnen(datei);
			assertTrue(geoeffnet.istGespeichertProperty().get());
			testName(geoeffnet);
			assertEquals(datei, geoeffnet.getSpeicherort());
			testSpeicherOrt(geoeffnet);
			assertEquals(Programmiersprache.Java, geoeffnet.getProgrammiersprache());
			assertEquals(projekt.getDiagrammElemente().size(), geoeffnet.getDiagrammElemente().size());
			for (int i = 0; i < projekt.getDiagrammElemente().size(); i++) {
				var original = projekt.getDiagrammElemente().get(i);
				var kopie = geoeffnet.getDiagrammElemente().get(i);
				assertEquals(original, kopie);
			}
			
			var hilf = datei.toFile();
			hilf.setReadOnly();
			datei = hilf.toPath();
			assertFalse(projekt.speichern(datei));
		} catch (IOException e) {
			e.printStackTrace();
			fail(e);
		}
	}
	
	@Test
	void testProgrammiersprache() {
		assertEquals(Programmiersprache.Java, projekt.getProgrammiersprache());
	}
	
	@Test
	void testDiagrammElemente() {
		assertTrue(projekt.getDiagrammElemente().isEmpty());
		projekt.getDiagrammElemente().add(new TestElement());
		assertEquals(1, projekt.getDiagrammElemente().size());
		assertFalse(projekt.istGespeichertProperty().get());
	}
}