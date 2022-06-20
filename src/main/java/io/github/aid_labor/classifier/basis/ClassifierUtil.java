/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.util.Iterator;

/**
 * Sammlung von allgemein hilfreichen Methoden
 * 
 * @author Tim Muehle
 *
 */
public class ClassifierUtil {
//	private static final Logger log = Logger.getLogger(ClassifierUtil.class.getName());
	
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
	
	/**
	 * Testet alle Elemente in einem Container aus Gleichheit. Zwei Container sind nur gleich,
	 * wenn sie in der gleichen Reihenfolge die Gleichen Elemente laut {@code equals} haben oder beide {@code null}
	 * sind. Zwei {@code null}-Werte im Inhalts-Vergleich der Container ergeben ebenfalls {@code true}.
	 * 
	 * @param <T>               Containertyp
	 * @param <E>               Inhaltstyp der Container
	 * @param ersterContainer   Erster Container
	 * @param zweiterConateiner Zweiter Container
	 * @return {@code true}, wenn alle Inhalte gleich sind und in der selben Reihenfolge
	 *         vorliegen
	 */
	public static <T extends Iterable<E>, E> boolean pruefeGleichheit(T ersterContainer,
			T zweiterConateiner) {
		if (ersterContainer == null || zweiterConateiner == null) {
			return ersterContainer == null && zweiterConateiner == null;
		}
		
		boolean elementeGleich = true;
		Iterator<E> elemente1 = ersterContainer.iterator();
		Iterator<E> elemente2 = zweiterConateiner.iterator();
		
		while (elemente1.hasNext() && elemente2.hasNext()) {
			E e1 = elemente1.next();
			E e2 = elemente2.next();
			if (e1 == e2 || (e1 == null && e2 == null)) {
				continue;
			}
			if (e1 != null && !e1.equals(e2)) {
				elementeGleich = false;
				break;
			}
		}
		
		if (elemente1.hasNext() || elemente2.hasNext()) {
			elementeGleich = false;
		}
		
		return elementeGleich;
	}
	
	/**
	 * Berechnet einen gemeinsamen Hash-Wert für alle Übergabeparameter.
	 * Für die Berechnung wird auf allen Objekten {@code hashCode()} aufgerufen. Ist ein Übergabeparameter iterierbar
	 * (siehe {@link Iterable}), wird über den gesamten Inhalt iteriert, für jedes Element {@code hashCode()} 
	 * aufgerufen und der Wert mit der Position im {@link Iterator} multipliziert. Jeder {@code null}-Wert hat den
	 * Hash-Wert {@code 0}.
	 * 
	 * @param objekte Objekte, die gehasht werden
	 * @return berechneter Hash-Wert
	 */
	public static int hashAlle(Object... objekte) {
		long hash = 0;
		for (var obj : objekte) {
			if (obj instanceof Iterable<?> iterierbar) {
				int i = 1;
				for (var element : iterierbar) {
					hash = 31*hash + i * (element == null ? 0 : element.hashCode());
					i++;
				}
			} else {
				hash = 31*hash + (obj == null ? 0 : obj.hashCode());
			}
		}
		
		return (int) hash;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private ClassifierUtil() {
		// Hilfsklasse, nicht instanziierbar!
		throw new UnsupportedOperationException("Hilfsklasse darf nicht instanziiert werden!");
	}
	
}