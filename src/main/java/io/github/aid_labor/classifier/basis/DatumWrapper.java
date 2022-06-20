/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.time.Instant;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Speicherung eines unveränderlichen Wertes mit Zuordnung eines Zeitstempels
 * 
 * @author Tim Muehle
 *
 * @param <E> Typ des gespeicherten Wertes
 */
public class DatumWrapper<E> implements Comparable<DatumWrapper<E>> {
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final Instant zeitpunkt;
	private final E element;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Speicherung eines Wertes mit aktuellem Zeitstempel
	 * @param element	Wert, der gespeichert wird
	 */
	public DatumWrapper(E element) {
		this.zeitpunkt = Instant.now();
		this.element = element;
	}
	
	@JsonCreator
	DatumWrapper(
			@JsonProperty("element") E element, 
			@JsonProperty("zeitpunkt") Instant zeitpunkt) {
		this.zeitpunkt = Objects.requireNonNull(zeitpunkt);
		this.element = element;
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Zeitstempel des Erzeugungszeitpunktes
	 * @return Zeitstempel des Erzeugungszeitpunktes
	 */
	public Instant getZeitpunkt() {
		return zeitpunkt;
	}
	
	/**
	 * gespeichertes Element
	 * @return gespeichertes Element
	 */
	public E getElement() {
		return element;
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * Vergleich auf Basis des Zeitstempels (siehe {@link #getZeitpunkt()})
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(DatumWrapper<E> o) {
		return this.zeitpunkt.compareTo(o.zeitpunkt);
	}
	
	/**
	 * Gleichheit, wenn gespeicherte Elemente gleich sind. Der Zeitstempel wird bei Gleichheitsprüfung nicht einbezogen!
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DatumWrapper<?> w) {
			return Objects.equals(this.element, w.element);
		}
		return false;
	}
	
	/**
	 * hashCode, den {@link Objects#hashCode()} für das gespeicherte Element berechnet
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(this.element);
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}