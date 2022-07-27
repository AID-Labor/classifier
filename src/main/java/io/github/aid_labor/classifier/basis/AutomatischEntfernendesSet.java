/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.util.TreeSet;
import java.util.Set;
import java.util.logging.Logger;


/**
 * Sortiertes Set, das eine maximale Groesse hat. Wenn beim Hinzufügen die maximale Groesse
 * überschritten wird, wird das kleinste Element (basierend auf der natürlichen
 * Ordnung der Elemente, siehe {@link TreeSet}, {@link Set}, {@link Comparable}) automatisch entfernt.
 * 
 * @author Tim Muehle
 *
 * @param <E> Typ der Elemente, die in diesem Set gespeichert werden
 */
public class AutomatischEntfernendesSet<E> extends TreeSet<E> {
	
	private static final Logger log = Logger.getLogger(AutomatischEntfernendesSet.class.getName());
	private static final long serialVersionUID = -3027636633260070772L;
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final int maximaleAnzahl;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Erzeugt ein neues automatisch entfernendes Set, das Elemente basierend auf der natürlichen Ordnung
	 * durch Implementierung von {@link Comparable} sortiert. Das Set hat Platz für die übergebene Anzahl
	 * an Elementen.
	 * 
	 * @param maximaleAnzahl maximale Größe des Sets
	 * @throws IllegalArgumentException wenn der Parameter {@code maximaleAnzahl} kleiner oder gleich {@code 0} ist
	 */
	public AutomatischEntfernendesSet(int maximaleAnzahl) {
		super();
		if (maximaleAnzahl < 1) {
			throw new IllegalArgumentException("maximaleAnzahl muss groesser als 0 sein");
		}
		this.maximaleAnzahl = maximaleAnzahl;
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * maximale Anzahl der Enthaltenen Elemente
	 * @return maximale Anzahl der Enthaltenen Elemente
	 */
	public int getMaximaleAnzahl() {
		return maximaleAnzahl;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * Fügt ein Element hinzu, falls dieses noch nicht im Set enthalten ist.
	 * Wird dabei die maxiamle Größe überschritten, wird das kleinste Element
	 * automatisch entfernt.
	 * 
	 * @param e einzufügendes Element
	 */
	@Override
	public boolean add(E e) {
		boolean hinzugefuegt = super.add(e);
		log.finest(() -> """
				Element %s hinzugefuegt.
				          Aktuelle Groesse: %d
				          Maximale Groesse: %d""".formatted(e, this.size(),
				this.maximaleAnzahl));
		
		if (hinzugefuegt && this.size() > this.maximaleAnzahl) {
			E entfernt = this.pollFirst();
			log.finer(() -> "Letztes Element [] entfernt".formatted(entfernt));
		}
		
		return hinzugefuegt;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}