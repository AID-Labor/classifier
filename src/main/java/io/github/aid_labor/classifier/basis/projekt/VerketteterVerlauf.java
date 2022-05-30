/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.projekt;

import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Objects;


/**
 * Implementierung des Abstrakten Datentyps {@link Verlaufspuffer}, die im Hintergrund mit
 * verketteten Knoten arbeitet.
 * 
 * <b>
 * Instanzen dieser Klasse sind nicht automatisch synchronisiert. Zum Synchronisieren
 * einer Instanz von VerketteterVerlauf kann die Methode
 * {@link #synchronisierterVerlauf(VerketteterVerlauf)} verwendet werden.
 * </b>
 * 
 * @author Tim Muehle
 *
 * @param <T>
 */
public class VerketteterVerlauf<T> implements Verlaufspuffer<T> {
//	private static final Logger log = Logger.getLogger(VerketteterVerlauf.class.getName());

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Erzeugt eine neue synchronisierte Instanz von VerketteterVerlauf. Die maximale Anzahl
	 * an
	 * zu speichernden Elementen wird auf {@link Integer#MAX_VALUE} gesetzt.
	 * 
	 * @return neue synchronisierte Instanz von VerketteterVerlauf
	 */
	public static <T> VerketteterVerlauf<T> synchronisierterVerlauf() {
		return new SynchronisierterVerketteterVerlauf<>();
	}
	
	/**
	 * Erzeugt einen neue synchronisierte Instanz von VerketteterVerlauf mit der uebergebenen
	 * maximalen Anzahl von zu speichernden Elementen
	 * 
	 * @param maximaleAnzahl maximale Anzahl an zu speichernden Elementen
	 * @return neue synchronisierte Instanz von VerketteterVerlauf
	 * @throws IllegalArgumentException Wenn {@code maximaleAnzahl} kleiner als 1 ist
	 */
	public static <T> VerketteterVerlauf<T> synchronisierterVerlauf(int maximaleAnzahl)
			throws IllegalArgumentException {
		return new SynchronisierterVerketteterVerlauf<>(maximaleAnzahl);
	}
	
	private static class SynchronisierterVerketteterVerlauf<T> extends VerketteterVerlauf<T> {
		
		public SynchronisierterVerketteterVerlauf() {
			super();
		}
		
		public SynchronisierterVerketteterVerlauf(int maximaleAnzahl)
				throws IllegalArgumentException {
			super(maximaleAnzahl);
		}
		
		// ===================================================================================
		// @formatter:off
		@Override
		public void ablegen(T element) throws NullPointerException {
			synchronized (this) { super.ablegen(element); }
		}
		
		@Override
		public T oben() throws NoSuchElementException {
			synchronized (this) { return super.oben(); }
		}
		
		@Override
		public T entfernen() throws NoSuchElementException {
			synchronized (this) { return super.entfernen(); }
		}
		
		@Override
		public int getElementAnzahl() {
			synchronized (this) { return super.getElementAnzahl(); }
		}
		
		@Override
		public boolean istLeer() {
			synchronized (this) { return super.istLeer(); }
		}
		
		@Override
		public int getMaximaleAnzahl() {
			synchronized (this) { return super.getMaximaleAnzahl(); }
		}
		
		@Override
		public void setMaximaleAnzahl(int maximaleAnzahl) {
			synchronized (this) { super.setMaximaleAnzahl(maximaleAnzahl); }
		}
		
		@Override
		public void leeren() {
			synchronized (this) { super.leeren(); }
		}
		// @formatter:on
	}
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final Deque<T> internerStapel;
	private int maximaleAnzahl;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Erzeugt einen neuen VerkettetenVerlauf. Die maximale Anzahl an zu speichernden
	 * Elementen wird auf {@link Integer#MAX_VALUE} gesetzt.
	 */
	public VerketteterVerlauf() {
		this(Integer.MAX_VALUE);
	}
	
	/**
	 * Erzeugt einen neuen VerkettetenVerlauf mit der uebergebenen maximalen Anzahl von zu
	 * speichernden Elementen
	 * 
	 * @param maximaleAnzahl maximale Anzahl an zu speichernden Elementen
	 * @throws IllegalArgumentException Wenn {@code maximaleAnzahl} kleiner als 1 ist
	 */
	public VerketteterVerlauf(int maximaleAnzahl) throws IllegalArgumentException {
		if (maximaleAnzahl < 1) {
			throw new IllegalArgumentException(
					"Die maximale Anzahl darf 1 nicht unterschreiten");
		}
		this.internerStapel = new LinkedList<>();
		this.maximaleAnzahl = maximaleAnzahl;
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T oben() throws NoSuchElementException {
		return this.internerStapel.getFirst();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getElementAnzahl() {
		return this.internerStapel.size();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean istLeer() {
		return this.internerStapel.isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaximaleAnzahl() {
		return this.maximaleAnzahl;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMaximaleAnzahl(int maximaleAnzahl) throws IllegalArgumentException {
		if (maximaleAnzahl < 1) {
			throw new IllegalArgumentException(
					"Die maximale Anzahl darf 1 nicht unterschreiten");
		}
		
		this.maximaleAnzahl = maximaleAnzahl;
		loescheUeberlauf();
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ablegen(T element) throws NullPointerException {
		this.internerStapel.addFirst(Objects.requireNonNull(element));
		
		loescheUeberlauf();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T entfernen() throws NoSuchElementException {
		return this.internerStapel.removeFirst();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void leeren() {
		this.internerStapel.clear();
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void loescheUeberlauf() {
		while ((long) this.getElementAnzahl() > (long) maximaleAnzahl) {
			internerStapel.pollLast();
		}
	}
	
}