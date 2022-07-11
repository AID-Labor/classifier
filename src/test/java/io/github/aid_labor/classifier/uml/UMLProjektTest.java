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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.aid_labor.classifier.basis.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.projekt.ProjektBasis;
import io.github.aid_labor.classifier.basis.projekt.UeberwachungsStatus;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKommentar;
import io.github.aid_labor.classifier.uml.programmierung.JavaProvider;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;


class UMLProjektTest {
	private UMLProjekt projekt;
	
	@BeforeAll
	static void setUpClass() {
		Ressourcen.setProgrammDetails(new ProgrammDetails(null, UMLProjektTest.class.getName(),
				null, null, UMLProjektTest.class, null));
	}
	
	@BeforeEach
	void setUp() throws Exception {
		projekt = new UMLProjekt("Test", JavaProvider.provider(), false);
		projekt.setUeberwachungsStatus(UeberwachungsStatus.INKREMENTELL_SAMMELN);
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
			projekt.getDiagrammElemente().add(new UMLKommentar());
			assertFalse(gespeichert.get());
			assertTrue(projekt.speichern());
			Files.newBufferedReader(datei).lines().forEach(zeile -> System.out.println(zeile));
			assertTrue(gespeichert.get());
			
			UMLProjekt geoeffnet = ProjektBasis.ausDateiOeffnen(datei, UMLProjekt.class);
			assertTrue(geoeffnet.istGespeichertProperty().get());
			geoeffnet.setUeberwachungsStatus(UeberwachungsStatus.INKREMENTELL_SAMMELN);
			testName(geoeffnet);
			assertEquals(datei, geoeffnet.getSpeicherort());
			testSpeicherOrt(geoeffnet);
			assertEquals(JavaProvider.provider(), geoeffnet.getProgrammiersprache());
			assertEquals(projekt.getDiagrammElemente().size(),
					geoeffnet.getDiagrammElemente().size());
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
		assertEquals(JavaProvider.provider(), projekt.getProgrammiersprache());
	}
	
	@Test
	void testDiagrammElemente() {
		assertTrue(projekt.getDiagrammElemente().isEmpty());
		projekt.getDiagrammElemente().add(new UMLKommentar());
		assertEquals(1, projekt.getDiagrammElemente().size());
		assertFalse(projekt.istGespeichertProperty().get());
	}
}
