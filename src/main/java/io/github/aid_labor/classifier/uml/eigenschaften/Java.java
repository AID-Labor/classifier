/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.eigenschaften;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;


class Java implements ProgrammierEigenschaften {
	
	private static final Logger log = Logger.getLogger(Java.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private static Java singletonInstanz;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	static Java getInstanz() {
		if (singletonInstanz == null) {
			singletonInstanz = new Java();
		}
		
		return singletonInstanz;
	}
	
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
	
	private Modifizierer privateErlaubt = new Modifizierer("private", true);
	private Modifizierer packagePrivateErlaubt = new Modifizierer("package-private", true);
	private Modifizierer protectedErlaubt = new Modifizierer("protected", true);
	private Modifizierer publicErlaubt = new Modifizierer("public", true);
	
	private Modifizierer privateNichtErlaubt = new Modifizierer("private", false);
	private Modifizierer protectedNichtErlaubt = new Modifizierer("protected", false);
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private Java() {
		// Singleton-Entwurfsmuster
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public List<Modifizierer> getTypModifizierer(KlassifiziererTyp typ) {
		return List.of(publicErlaubt, packagePrivateErlaubt, protectedNichtErlaubt,
				privateNichtErlaubt);
	}
	
	@Override
	public List<Modifizierer> getAttributModifizierer(KlassifiziererTyp typ) {
		return switch (typ) {
			case Interface -> List.of(publicErlaubt);
			case Klasse, AbstrakteKlasse, Enumeration -> List.of(publicErlaubt,
					protectedErlaubt, packagePrivateErlaubt, privateErlaubt);
			default -> {
				log.warning(() -> "unbekannter typ: " + typ);
				yield Collections.emptyList();
			}
		};
	}
	
	@Override
	public List<Modifizierer> getMethodenModifizierer(KlassifiziererTyp typ) {
		return switch (typ) {
			case Interface -> List.of(publicErlaubt);
			case Klasse, AbstrakteKlasse, Enumeration -> List.of(publicErlaubt,
					protectedErlaubt, packagePrivateErlaubt, privateErlaubt);
			default -> {
				log.warning(() -> "unbekannter typ: " + typ);
				yield Collections.emptyList();
			}
		};
	}
	
	@Override
	public boolean erlaubtInstanzAttribute(KlassifiziererTyp typ) {
		return switch (typ) {
			case Interface -> false;
			default -> true;
		};
	}
	
	@Override
	public boolean erlaubtSuperklasse(KlassifiziererTyp typ) {
		return switch (typ) {
			case Interface, Enumeration -> false;
			default -> true;
		};
	}
	
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
	
}