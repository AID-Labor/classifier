/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.projekt;

/**
 * Erweiterung eines Stapels zu einem Verlauf mit maximaler Anzahl von gespeicherten
 * Elementen.
 * <b>Wird beim Hinzufuegen eines Elementes die maximale Anzahl ueberschritten, wird das
 * aelteste
 * (unterste) Element ohne Warnung geloescht.</b>
 * 
 * @author Tim Muehle
 *
 * @param <T> Typ der Elemente, die in diesem Verlauf gespeichert werden
 */
public interface Verlaufspuffer<T> extends Stapel<T> {
	
	/**
	 * Legt ein Element auf den Stapel. <b>Wenn die maximale Anzahl von Elementen 
	 * ueberschritten wird, wird das aelteste (unterste) Element ohne Nachfrage geloescht!</b>
	 * 
	 * @param element Neues oberstes Element
	 * @throws NullPointerException Wenn das uebergebene Element {@code null} ist
	 */
	@Override
	public void ablegen(T element) throws NullPointerException;
	
	/**
	 * Setzt die maximale Anzahl an gespeicherten Elementen.
	 * 
	 * @apiNote Sind bereits mehr Elemente im Verlauf gespeichert, werden die aeltesten
	 *          Elemente geloescht, bis die maximale Anzahl nicht mehr ueberschritten wird.
	 * 			
	 * @param maximaleAnzahl neue maximale Anzahl
	 * @throws IllegalArgumentException Wenn {@code maximaleAnzahl} kleiner als 1 ist
	 */
	public void setMaximaleAnzahl(int maximaleAnzahl) throws IllegalArgumentException;
	
	/**
	 * Gibt die maximale Anzahl der gespeicherten Elemente zurueck
	 * 
	 * @return maximale Anzahl der gespeicherten Elemente
	 */
	public int getMaximaleAnzahl();
	
}