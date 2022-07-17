/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis.projekt.editierung;

/**
 * EditierBefehl fuer einfache Wertaenderung, die mit einem Setter
 * angewendet und rueckgaengig gemacht werden kann.
 * 
 * @author Tim Muehle
 *
 * @param <T> Typ des Editierbaren Wertes
 */
public interface WertEditierBefehl<T> extends EditierBefehl {
	
	/**
	 * Macht eine Editierung rueckgaengig
	 */
	@Override
	public default void macheRueckgaengig() {
		set(getVorher());
	}
	
	/**
	 * Wendet eine Editierung erneut an
	 */
	@Override
	public default void wiederhole() {
		set(getNachher());
	}
	
	public T getVorher();
	
	public T getNachher();
	
	public void set(T wert);
	
	/**
	 * eindeutige ID, um Editierungen an gleichen Attributen festzustellen
	 * @return eindeutige ID, um Editierungen an gleichen Attributen festzustellen
	 */
	public String id();
}