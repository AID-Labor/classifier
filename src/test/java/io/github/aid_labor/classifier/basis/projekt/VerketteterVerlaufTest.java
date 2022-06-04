/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.projekt;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


class VerketteterVerlaufTest {
	
	private VerketteterVerlauf<Integer> verlauf;
	
	@ParameterizedTest
	@ValueSource(ints = { 1, 5, 1000000 })
	void testPuffergroesse(int anzahl) {
		verlauf = new VerketteterVerlauf<>(anzahl);
		
		assertTrue(verlauf.istLeer());
		assertEquals(0, verlauf.getElementAnzahl());
		assertEquals(anzahl, verlauf.getMaximaleAnzahl());
		
		for (int i = 1; i <= anzahl; i++) {
			verlauf.ablegen(i);
			assertEquals(i, verlauf.oben());
			assertEquals(i, verlauf.getElementAnzahl());
		}
		assertEquals(anzahl, verlauf.getElementAnzahl());
		
		verlauf.ablegen(-1);
		assertEquals(-1, verlauf.oben());
		assertEquals(anzahl, verlauf.getElementAnzahl());
		assertEquals(-1, verlauf.entfernen());
		assertEquals(anzahl - 1, verlauf.getElementAnzahl());
		
		for (int i = anzahl; i > 1; i--) {
			assertEquals(i, verlauf.oben());
			assertEquals(i, verlauf.entfernen());
		}
		
		assertTrue(verlauf.istLeer());
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "normal", "synchronisiert" })
	void testPufferVerkleinern(String typ) {
		final int anzahl = 10;
		verlauf = switch (typ) {
			case "normal" -> {
				yield new VerketteterVerlauf<>(anzahl);
			}
			case "synchronisiert" -> {
				yield VerketteterVerlauf.synchronisierterVerlauf(anzahl);
			}
			default -> {
				fail("Durchlauf " + typ + " nicht implementiert!");
				yield null;
			}
		};
		for (int i = 1; i <= anzahl; i++) {
			verlauf.ablegen(i);
			assertEquals(i, verlauf.oben());
			assertEquals(i, verlauf.getElementAnzahl());
		}
		assertEquals(anzahl, verlauf.getElementAnzahl());
		
		final int neueAnzahl = anzahl / 2;
		verlauf.setMaximaleAnzahl(neueAnzahl);
		assertEquals(neueAnzahl, verlauf.getMaximaleAnzahl());
		assertEquals(neueAnzahl, verlauf.getElementAnzahl());
		
		int vergleich = anzahl;
		while (!verlauf.istLeer()) {
			int entfernt = verlauf.entfernen();
			assertEquals(vergleich, entfernt);
			vergleich--;
		}
		
		assertEquals(neueAnzahl, vergleich);
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "normal-unbegrenzt", "normal-begrenzt",
		"synchronisiert-unbegrenzt", "synchronisiert-begrenzt" })
	void testExceptions(String typ) {
		verlauf = switch (typ) {
			case "normal-unbegrenzt" -> {
				yield new VerketteterVerlauf<>();
			}
			case "normal-begrenzt" -> {
				assertThrows(IllegalArgumentException.class,
						() -> new VerketteterVerlauf<>(0));
				yield new VerketteterVerlauf<>(1);
			}
			case "synchronisiert-unbegrenzt" -> {
				yield VerketteterVerlauf.synchronisierterVerlauf();
			}
			case "synchronisiert-begrenzt" -> {
				assertThrows(IllegalArgumentException.class,
						() -> VerketteterVerlauf.synchronisierterVerlauf(0));
				yield VerketteterVerlauf.synchronisierterVerlauf(1);
			}
			default -> {
				fail("Durchlauf " + typ + " nicht implementiert!");
				yield null;
			}
		};
		
		assertThrows(NullPointerException.class, () -> verlauf.ablegen(null));
		assertThrows(NoSuchElementException.class, () -> verlauf.entfernen());
		assertThrows(NoSuchElementException.class, () -> verlauf.oben());
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
