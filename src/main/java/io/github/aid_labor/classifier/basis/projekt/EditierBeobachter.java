/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.projekt;

/**
 * Objekt, das Editierungen von anderen Objekten ueberwacht und verarbeitet.
 * Zur Ueberwachung eines editierbaren Objekts muss sich der Beobachter beim editierbaren
 * Objekt mit der Methode {@link Editierbar#meldeAn(EditierBeobachter)} registrieren.
 * 
 * @author Tim Muehle
 *
 */
public interface EditierBeobachter {
	
	/**
	 * Methode, die eine Instanz von {@link Editierbar} aufruft, um ueber eine Editierung
	 * zu informieren.
	 * 
	 * Typischerweise wird in dieser Methode der uebergebene {@link EditierBefehl} in einen
	 * Puffer eingereiht, um die Editierung ggf. spaeter widerrufen zu koennen.
	 * 
	 * @param editierung Editierung, die zum Aufruf dieser Methode gefuehrt hat
	 */
	public void verarbeiteEditierung(EditierBefehl editierung);
	
}