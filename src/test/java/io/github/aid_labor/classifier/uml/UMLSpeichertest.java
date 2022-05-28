/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.aid_labor.classifier.LoggingEinstellung;
import io.github.aid_labor.classifier.uml.eigenschaften.Attribut;
import io.github.aid_labor.classifier.uml.eigenschaften.Datentyp.BasisDatentyp;
import io.github.aid_labor.classifier.uml.eigenschaften.Methode;
import io.github.aid_labor.classifier.uml.eigenschaften.Modifizierer;
import io.github.aid_labor.classifier.uml.eigenschaften.Programmiersprache;
import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKommentar;


class UMLSpeichertest {
	
	@BeforeAll
	static void setUpClass() {
		LoggingEinstellung.initialisiereTestLogging();
	}
	
	private UMLProjekt projekt;
	private Path datei;
	
	@BeforeEach
	void setUp() throws Exception {
		datei = Files.createTempFile("test", ".projekt");
		projekt = new UMLProjekt("Test", Programmiersprache.Java, false);
		projekt.setSpeicherort(datei);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		try {
			Files.deleteIfExists(datei);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void test() {
		testeSpeichernUndOeffnen();
		
		projekt.getDiagrammElemente().add(new UMLKommentar());
		testeSpeichernUndOeffnen();
		
		var element = new UMLKlassifizierer(KlassifiziererTyp.Klasse, Programmiersprache.Java,
				"testPaket", "TestKlasse");
		projekt.getDiagrammElemente().add(element);
		testeSpeichernUndOeffnen();
		
		element = new UMLKlassifizierer(KlassifiziererTyp.Klasse, Programmiersprache.Java,
				"testPaket", "TestKlasse2");
		var attribut = new Attribut(new Modifizierer("private", true), BasisDatentyp.STRING);
		attribut.setName("testAttribut");
		element.getAttribute().add(attribut);
		var methode = new Methode(new Modifizierer("public", true), BasisDatentyp.INT);
		methode.setName("testMethode");
		element.getMethoden().add(methode);
		projekt.getDiagrammElemente().add(element);
		testeSpeichernUndOeffnen();
	}
	
	void testeSpeichernUndOeffnen() {
		assertFalse(projekt.istGespeichertProperty().get());
		assertTrue(projekt.speichern());
		assertTrue(projekt.istGespeichertProperty().get());
		try {
			Files.newBufferedReader(datei).lines().forEach(zeile -> System.out.println(zeile));
			System.out.println("\n\n# # # # # # # # # # # # # # # # # # # # # # #\n\n");
			UMLProjekt geoeffnet = UMLProjekt.ausDateiOeffnen(datei);
			assertTrue(geoeffnet.istGespeichertProperty().get());
			assertEquals(projekt, geoeffnet);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e);
		}
	}
}

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
