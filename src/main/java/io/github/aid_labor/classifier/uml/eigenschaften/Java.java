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


public class Java implements ProgrammierEigenschaften {
	
	private static final Logger log = Logger.getLogger(Java.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public static Datentyp BOOLEAN_PRIMITIV = new Datentyp("boolean");
	public static Datentyp BYTE_PRIMITIV = new Datentyp("byte");
	public static Datentyp SHORT_PRIMITIV = new Datentyp("short");
	public static Datentyp INT_PRIMITIV = new Datentyp("int");
	public static Datentyp LONG_PRIMITIV = new Datentyp("long");
	public static Datentyp FLOAT_PRIMITIV = new Datentyp("float");
	public static Datentyp DOUBLE_PRIMITIV = new Datentyp("double");
	public static Datentyp CHAR_PRIMITIV = new Datentyp("char");
	public static Datentyp VOID_PRIMITIV = new Datentyp("void");
	
	public static List<Datentyp> PRIMITIVE_DATENTYPEN = Collections.unmodifiableList(List.of(
			BOOLEAN_PRIMITIV,
			BYTE_PRIMITIV,
			SHORT_PRIMITIV,
			INT_PRIMITIV,
			LONG_PRIMITIV,
			FLOAT_PRIMITIV,
			DOUBLE_PRIMITIV,
			CHAR_PRIMITIV,
			VOID_PRIMITIV));
	
	public static Datentyp STRING = new Datentyp("String");
	
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
	
	private Datentyp letzterDatentyp;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private Java() {
		// Singleton-Entwurfsmuster
		letzterDatentyp = STRING;
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public boolean istTypModifiziererErlaubt(KlassifiziererTyp typ, Modifizierer m) {
		return m.equals(Modifizierer.PUBLIC) || m.equals(Modifizierer.PACKAGE);
	}
	
	@Override
	public boolean istAttributModifiziererErlaubt(KlassifiziererTyp typ, Modifizierer m) {
		return switch (typ) {
			case Interface -> m.istPublic();
			case Klasse, AbstrakteKlasse, Enumeration -> m.istPrivate();
			default -> {
				log.warning(() -> "unbekannter typ: " + typ);
				yield false;
			}
		};
	}
	
	@Override
	public boolean istMethodenModifiziererErlaubt(KlassifiziererTyp typ, Modifizierer m) {
		return switch (typ) {
			case Interface -> m.istPublic();
			case Klasse, AbstrakteKlasse, Enumeration -> m.istPrivate();
			default -> {
				log.warning(() -> "unbekannter typ: " + typ);
				yield false;
			}
		};
	}
	
	@Override
	public Modifizierer[] getAttributModifizierer(KlassifiziererTyp typ) {
		return switch (typ) {
			case Interface -> new Modifizierer[] { Modifizierer.PUBLIC };
			case Klasse, AbstrakteKlasse, Enumeration ->
				new Modifizierer[] { Modifizierer.PUBLIC, Modifizierer.PROTECTED,
					Modifizierer.PACKAGE, Modifizierer.PRIVATE };
			default -> {
				log.warning(() -> "unbekannter typ: " + typ);
				yield new Modifizierer[0];
			}
		};
	}
	
	@Override
	public Modifizierer[] getMethodenModifizierer(KlassifiziererTyp typ) {
		return switch (typ) {
			case Interface -> new Modifizierer[] { Modifizierer.PUBLIC };
			case Klasse, AbstrakteKlasse, Enumeration ->
				new Modifizierer[] { Modifizierer.PUBLIC, Modifizierer.PROTECTED,
					Modifizierer.PACKAGE, Modifizierer.PRIVATE };
			default -> {
				log.warning(() -> "unbekannter typ: " + typ);
				yield new Modifizierer[0];
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
	
	@Override
	public Datentyp getLetzerDatentyp() {
		return letzterDatentyp.erzeugeTiefeKopie();
	}
	
	@Override
	public void setLetzerDatentyp(Datentyp letzterDatentyp) {
		this.letzterDatentyp.set(letzterDatentyp);
	}
	
	@Override
	public boolean istVoid(Datentyp datentyp) {
		return datentyp.getTypName().equals("void");
	}
	
	@Override
	public List<Datentyp> getPrimitiveDatentypen() {
		return PRIMITIVE_DATENTYPEN;
	}
	
	@Override
	public Datentyp getVoid() {
		return VOID_PRIMITIV;
	}
	
	@Override
	public List<Datentyp> getBekannteDatentypen() {
		log.severe(() -> "getBekannteDatentypen noch nicht implementiert !!! !!! !!! !!! !!!");
		return Collections.emptyList();
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