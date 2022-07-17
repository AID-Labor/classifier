/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.projekt.editierung;

public interface Editierbar {
	
	/**
	 * Registrierung von Beobachtern, die ueber Editierungen informiert werden.
	 * 
	 * @apiNote Es findet keine Ueberpruefung auf Mehrfache Registrierung statt. Beobachter,
	 *          die mehrfach dieser Methode uebergeben werden, werden auch mehrfach ueber die
	 *          Editierung
	 *          informiert.
	 * 
	 * @param beobachter Beobachter, der fuer Benachrichtigungen registriert wird
	 */
	public void meldeAn(EditierBeobachter beobachter);
	
	/**
	 * Entfernen von Beobachtern, die vorher mit {@link #meldeAn(EditierBeobachter)}
	 * registriert wurden.
	 * 
	 * @apiNote Wenn ein Beobachter mehrfach registriert wurde, sorgt diese Methode dafuer,
	 *          dass alle Referenzen auf den uebergebenen Beobachter entfernt werden und
	 *          dieser entgueltig nicht mehr ueber Editierungen informiert wird.
	 * 			
	 * @param beobachter Beobachter, der nicht mehr informiert werden soll
	 */
	public void meldeAb(EditierBeobachter beobachter);
	
	/**
	 * Methode, die alle registrierten Beobachter ueber Editierungen informiert
	 * 
	 * @param editierung Editierung, die diese Methode ausgeloest hat und an alle Beobachter
	 *                   weitergegeben wird
	 */
	public void informiere(EditierBefehl editierung);
	
}