/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.java;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Datentyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Modifizierer;
import io.github.aid_labor.classifier.uml.programmierung.ProgrammierEigenschaften;


public class Java implements ProgrammierEigenschaften {
	
	private static final Logger log = Logger.getLogger(Java.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public static Datentyp BOOLEAN_PRIMITIV() {
		return new Datentyp("boolean");
	}
	
	public static Datentyp BYTE_PRIMITIV() {
		return new Datentyp("byte");
	}
	
	public static Datentyp SHORT_PRIMITIV() {
		return new Datentyp("short");
	}
	
	public static Datentyp INT_PRIMITIV() {
		return new Datentyp("int");
	}
	
	public static Datentyp LONG_PRIMITIV() {
		return new Datentyp("long");
	}
	
	public static Datentyp FLOAT_PRIMITIV() {
		return new Datentyp("float");
	}
	
	public static Datentyp DOUBLE_PRIMITIV() {
		return new Datentyp("double");
	}
	
	public static Datentyp CHAR_PRIMITIV() {
		return new Datentyp("char");
	}
	
	public static Datentyp VOID_PRIMITIV() {
		return new Datentyp("void");
	}
	
	public static Datentyp STRING() {
		return new Datentyp("String");
	}
	
	public static List<Datentyp> PRIMITIVE_DATENTYPEN() {
		return Collections
				.unmodifiableList(List.of(BOOLEAN_PRIMITIV(), BYTE_PRIMITIV(), SHORT_PRIMITIV(), INT_PRIMITIV(),
						LONG_PRIMITIV(), FLOAT_PRIMITIV(), DOUBLE_PRIMITIV(), CHAR_PRIMITIV(), VOID_PRIMITIV()));
	}
	
	private static final List<String> primitiveDatentypen = List.of("boolean", "byte", "short", "char", "int", "long", 
			"float", "double");
	
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
		letzterDatentyp = STRING();
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
			case Record -> false;
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
			case Klasse, AbstrakteKlasse, Enumeration, Record -> m.istPrivate();
			default -> {
				log.warning(() -> "unbekannter typ: " + typ);
				yield false;
			}
		};
	}
	
	@Override
	public List<Modifizierer> getAttributModifizierer(KlassifiziererTyp typ) {
		return switch (typ) {
			case Interface -> List.of(Modifizierer.PUBLIC);
			case Klasse, AbstrakteKlasse, Enumeration, Record ->
				List.of(Modifizierer.PUBLIC, Modifizierer.PROTECTED, Modifizierer.PACKAGE, Modifizierer.PRIVATE);
			default -> {
				log.warning(() -> "unbekannter typ: " + typ);
				yield Collections.emptyList();
			}
		};
	}
	
	@Override
	public Modifizierer getStandardAttributModifizierer(KlassifiziererTyp typ) {
		return switch (typ) {
			case Interface, Enumeration -> Modifizierer.PUBLIC;
			default -> {
				yield Modifizierer.PRIVATE;
			}
		};
	}
	
	@Override
	public List<Modifizierer> getMethodenModifizierer(KlassifiziererTyp typ, boolean istStatisch, boolean istAbstrakt) {
		return switch (typ) {
			case Interface -> {
				if(istStatisch) {
					yield List.of(Modifizierer.PUBLIC, Modifizierer.PRIVATE);
				} else if (istAbstrakt) {
					yield List.of(Modifizierer.PUBLIC);
				} else {
					yield List.of(Modifizierer.PUBLIC, Modifizierer.PRIVATE);
				}
			}
			case Klasse, Enumeration ->
				List.of(Modifizierer.PUBLIC, Modifizierer.PROTECTED, Modifizierer.PACKAGE, Modifizierer.PRIVATE);
			case AbstrakteKlasse -> {
				if (istAbstrakt) {
					yield List.of(Modifizierer.PUBLIC, Modifizierer.PROTECTED, Modifizierer.PACKAGE);
				} else {
					yield List.of(Modifizierer.PUBLIC, Modifizierer.PROTECTED, Modifizierer.PACKAGE,
							Modifizierer.PRIVATE);
				}
			}
			default -> {
				log.warning(() -> "unbekannter typ: " + typ);
				yield Collections.emptyList();
			}
		};
	}
	
	@Override
	public Modifizierer getStandardKonstruktorModifizierer(KlassifiziererTyp typ) {
		return Modifizierer.PUBLIC;
	}
	
	@Override
	public List<Modifizierer> getKonstruktorModifizierer(KlassifiziererTyp typ) {
		return switch (typ) {
			case Interface -> Collections.emptyList();
			case Klasse, AbstrakteKlasse, Enumeration, Record ->
				List.of(Modifizierer.PUBLIC, Modifizierer.PROTECTED, Modifizierer.PACKAGE, Modifizierer.PRIVATE);
			default -> {
				log.warning(() -> "unbekannter typ: " + typ);
				yield Collections.emptyList();
			}
		};
	}
	
	@Override
	public Modifizierer getStandardMethodenModifizierer(KlassifiziererTyp typ) {
		return Modifizierer.PUBLIC;
	}
	
	@Override
	public boolean erlaubtInstanzAttribute(KlassifiziererTyp typ) {
		return switch (typ) {
			case Interface, Record -> false;
			default -> true;
		};
	}
	
	@Override
	public boolean erlaubtSuperklasse(KlassifiziererTyp typ) {
		return switch (typ) {
			case Interface, Enumeration, Record -> false;
			default -> true;
		};
	}
	
	@Override
	public boolean erlaubtAbstrakteMethode(KlassifiziererTyp typ) {
		return typ.equals(KlassifiziererTyp.Interface) || typ.equals(KlassifiziererTyp.AbstrakteKlasse);
	}
	
	@Override
	public boolean erlaubtNichtAbstrakteMethode(KlassifiziererTyp typ) {
		return true;
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
		if (datentyp == null || datentyp.getTypName() == null) {
			return false;
		}
		return datentyp.getTypName().equals("void");
	}
	
	@Override
	public List<String> getPrimitiveDatentypen() {
		return primitiveDatentypen;
	}
	
	@Override
	public String getVoid() {
		return "void";
	}
	
	private SortedSet<String> bekannteKlassen;
	private SortedSet<String> bekannteInterfaces;
	private SortedSet<String> bekannteEnumerationen;
	private Map<String, String> klassenPaketMap;
	
	@Override
	public SortedSet<String> getBekannteKlassen() {
		if (bekannteKlassen == null) {
			bekannteKlassen = Collections.unmodifiableSortedSet(JavaKlassenSucher.ladeKlassen());
		}
		return bekannteKlassen;
	}
	
	@Override
	public SortedSet<String> getBekannteInterfaces() {
		if (bekannteInterfaces == null) {
			bekannteInterfaces = Collections.unmodifiableSortedSet(JavaKlassenSucher.ladeInterfaces());
		}
		return bekannteInterfaces;
	}
	
	@Override
	public SortedSet<String> getBekannteEnumerationen() {
		if (bekannteEnumerationen == null) {
			bekannteEnumerationen = Collections.unmodifiableSortedSet(JavaKlassenSucher.ladeEnumerationen());
		}
		return bekannteEnumerationen;
	}
	
	@Override
	public Map<String, String> getKlassenPaketMap() {
		if (klassenPaketMap == null) {
			klassenPaketMap = new HashMap<>();
			teileInKlasseUndPaketAuf(getBekannteKlassen(), klassenPaketMap);
			teileInKlasseUndPaketAuf(getBekannteInterfaces(), klassenPaketMap);
			teileInKlasseUndPaketAuf(getBekannteEnumerationen(), klassenPaketMap);
			klassenPaketMap = Collections.unmodifiableMap(klassenPaketMap);
		}
		return klassenPaketMap;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private static void teileInKlasseUndPaketAuf(Collection<String> klassenliste, Map<String, String> klassenPaketMap) {
		for (String name : klassenliste) {
			String paket = "";
			String klasse;
			if (name.contains(":")) {
				int index = name.lastIndexOf(":");
				paket = name.substring(0, index);
				klasse = name.substring(index+1);
			} else {
				klasse = name;
			}
			klassenPaketMap.put(klasse, paket);
		}
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}