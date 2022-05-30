/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.projekt;

import java.util.NoSuchElementException;

/**
 * Datenstruktur nach dem Last-In-First-Out-Prinzip (LIFO-Prinzip). Null-Werte sind nicht als
 * Elemente zugelassen.
 * 
 * @author Tim Muehle
 *
 * @param <T> Typ der Elemente, die in diesem Stapel gespeichert werden
 */
public interface Stapel<T> {
	
	/**
	 * Legt ein Element auf den Stapel.
	 * 
	 * @param element Neues oberstes Element
	 * @throws NullPointerException Wenn das uebergebene Element {@code null} ist
	 */
	public void ablegen(T element) throws NullPointerException;
	
	/**
	 * Gibt das Element zurueck, das oben auf dem Stapel liegt.
	 * 
	 * @return oberstes Element des Stapels
	 * @throws NoSuchElementException Wenn der Stapel leer ist
	 */
	public T oben() throws NoSuchElementException;
	
	/**
	 * Entfernt das oberste Element auf dem Stapel. Neues oberstes Element wird das Element,
	 * das vor dem aktuellen obersten Element hinzugefuegt wurde oder {@code null}, falls
	 * dieses nicht (mehr) existiert.
	 * 
	 * @return entferntes oberstes Element
	 * @throws NoSuchElementException Wenn der Stapel leer ist
	 */
	public T entfernen() throws NoSuchElementException;
	
	/**
	 * Gibt an, ob der Stapel Leer ist (keine Elemente enthaelt)
	 * 
	 * @return {@code true, wenn keine Elemente im Stapel vorhanden sind}
	 */
	public boolean istLeer();
	
	/**
	 * Gibt die Anzahl der im Stapel enthaltenen Elemente zurueck
	 * @return
	 */
	public int getElementAnzahl();
	
	/**
	 * Loescht alle Elemente vom Stapel, falls vorhanden
	 */
	public void leeren();
}