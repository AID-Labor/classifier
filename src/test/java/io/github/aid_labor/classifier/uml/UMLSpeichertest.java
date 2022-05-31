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
import io.github.aid_labor.classifier.basis.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.uml.eigenschaften.Attribut;
import io.github.aid_labor.classifier.uml.eigenschaften.Datentyp.BasisDatentyp;
import io.github.aid_labor.classifier.uml.eigenschaften.Methode;
import io.github.aid_labor.classifier.uml.eigenschaften.Modifizierer;
import io.github.aid_labor.classifier.uml.eigenschaften.Parameter;
import io.github.aid_labor.classifier.uml.eigenschaften.Programmiersprache;
import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKommentar;


class UMLSpeichertest {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@BeforeAll
	static void setUpClass() {
		LoggingEinstellung.initialisiereTestLogging();
		Ressourcen.setProgrammDetails(new ProgrammDetails(null, UMLProjektTest.class.getName(),
				null, null, UMLProjektTest.class, null));
	}
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private UMLProjekt projekt;
	private Path datei;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Vorbereitung																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
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
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Tests																				*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@Test
	void testeDiagrammUeberwachung() {
		testeSpeichernUndOeffnen();
		
		// Teste hinzufuegen
		projekt.getDiagrammElemente().add(new UMLKommentar());
		testeSpeichernUndOeffnen();
		
		projekt.getDiagrammElemente().add(new UMLKlassifizierer(KlassifiziererTyp.Klasse,
				Programmiersprache.Java, "testPaket", "TestKlasse_1"));
		testeSpeichernUndOeffnen();
		
		projekt.getDiagrammElemente().add(new UMLKlassifizierer(KlassifiziererTyp.Klasse,
				Programmiersprache.Java, "testPaket", "TestKlasse_2"));
		testeSpeichernUndOeffnen();
		
		// Teste Verschiebung
		projekt.getDiagrammElemente().add(1, new UMLKlassifizierer(KlassifiziererTyp.Klasse,
				Programmiersprache.Java, "testPaket", "TestKlasse_3"));
		testeSpeichernUndOeffnen();
		
		// Teste Entfernen
		projekt.getDiagrammElemente().remove(0);
		testeSpeichernUndOeffnen();
	}
	
	@Test
	void testeUMLKlassifizierer() {
		var umlKlasse = new UMLKlassifizierer(KlassifiziererTyp.Interface,
				Programmiersprache.Java, "testPaket", "TestUMLKlassifizierer_Interface");
		projekt.getDiagrammElemente().add(umlKlasse);
		testeSpeichernUndOeffnen();
		
		umlKlasse.setName("TestUMLKlassifizierer_Klasse");
		testeSpeichernUndOeffnen();
		
		umlKlasse.setPaket("neuesPaket");
		testeSpeichernUndOeffnen();
		
		umlKlasse.setTyp(KlassifiziererTyp.Klasse);
		testeSpeichernUndOeffnen();
	}
	
	@Test
	void testeAttributeUeberwachung() {
		var umlKlasse = new UMLKlassifizierer(KlassifiziererTyp.Klasse,
				Programmiersprache.Java, "testPaket", "TestKlasseAttribute");
		projekt.getDiagrammElemente().add(umlKlasse);
		
		var attribut = new Attribut(new Modifizierer("private", true), BasisDatentyp.STRING);
		attribut.setName("testAttribut");
		umlKlasse.getAttribute().add(attribut);
		testeSpeichernUndOeffnen();
		
		umlKlasse.getAttribute()
				.add(new Attribut(new Modifizierer("protected", true), BasisDatentyp.DOUBLE));
		testeSpeichernUndOeffnen();
		
		attribut.setName("testAttributNeu");
		testeSpeichernUndOeffnen();
		
		attribut.setInitialwert("'A'");
		testeSpeichernUndOeffnen();
		
		attribut.nutzeGetter(true);
		testeSpeichernUndOeffnen();
		
		attribut.nutzeSetter(true);
		testeSpeichernUndOeffnen();
		
		attribut.setSichtbarkeit(new Modifizierer("public", true));
		testeSpeichernUndOeffnen();
		
		attribut.setDatentyp(BasisDatentyp.CHAR);
		testeSpeichernUndOeffnen();
	}
	
	@Test
	void testeMethodenUeberwachung() {
		var umlKlasse = new UMLKlassifizierer(KlassifiziererTyp.Klasse,
				Programmiersprache.Java, "testPaket", "TestKlasseMethoden");
		projekt.getDiagrammElemente().add(umlKlasse);
		
		var methode = new Methode(new Modifizierer("public", true), BasisDatentyp.INT);
		methode.setName("testMethode");
		umlKlasse.getMethoden().add(methode);
		testeSpeichernUndOeffnen();
		
		umlKlasse.getMethoden()
				.add(new Methode(new Modifizierer("protected", true), BasisDatentyp.DOUBLE));
		testeSpeichernUndOeffnen();
		
		methode.setName("testMethodeNeu");
		testeSpeichernUndOeffnen();
		
		methode.setSichtbarkeit(new Modifizierer("private", true));
		testeSpeichernUndOeffnen();
		
		var parameter = new Parameter(BasisDatentyp.INT, "arg");
		methode.getParameterListe().add(parameter);
		testeSpeichernUndOeffnen();
		
		parameter.setName("param");
		testeSpeichernUndOeffnen();
		
		parameter.setDatentyp(BasisDatentyp.STRING);
		testeSpeichernUndOeffnen();
		
		methode.setRueckgabeTyp(BasisDatentyp.CHAR);
		testeSpeichernUndOeffnen();
		
		methode.setzeAbstrakt(true);
		testeSpeichernUndOeffnen();
		
		methode.setzeFinal(true);
		testeSpeichernUndOeffnen();
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private void testeSpeichernUndOeffnen() {
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