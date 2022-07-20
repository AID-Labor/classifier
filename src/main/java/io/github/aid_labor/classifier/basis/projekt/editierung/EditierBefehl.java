/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.projekt.editierung;

/**
 * Abbild einer Editierung, die rückgängig gemacht und wiederholt werden kann. Implementierungen sollten
 * {@link #toString()} entsprechend überschreiben, um sinnvolles Logging zu ermöglichen.
 * 
 * @author Tim Muehle
 *
 */
public interface EditierBefehl extends AutoCloseable {
	
	/**
	 * Macht eine Editierung rueckgaengig
	 */
	public void macheRueckgaengig();
	
	/**
	 * Wendet eine Editierung erneut an
	 */
	public void wiederhole();
	
	/**
	 * Beschreibung der Editierung
	 * 
	 * @return Beschreibung der Editierung
	 */
	@Override
	public String toString();
	
}