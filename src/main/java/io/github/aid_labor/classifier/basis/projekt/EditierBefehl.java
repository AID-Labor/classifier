/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis.projekt;


public interface EditierBefehl {
	
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
	 * @return Beschreibung der Editierung
	 */
	@Override
	public String toString();
}