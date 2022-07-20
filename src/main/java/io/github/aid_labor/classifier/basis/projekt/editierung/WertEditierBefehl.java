/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis.projekt.editierung;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * EditierBefehl fuer einfache Wertaenderung, die mit einem Setter
 * angewendet und rueckgaengig gemacht werden kann.
 * 
 * @author Tim Muehle
 *
 * @param <T> Typ des Editierbaren Wertes
 */
public interface WertEditierBefehl<T> extends EditierBefehl {
	static final Logger log = Logger.getLogger(WertEditierBefehl.class.getName());
	
	/**
	 * Macht eine Editierung rueckgaengig
	 */
	@Override
	public default void macheRueckgaengig() {
		try {
			set(getVorher());
		} catch (Exception e) {
			log.log(Level.WARNING, e,
					() -> "Setzen von >%s< fehlgeschlagen".formatted(id()));
		}
	}
	
	/**
	 * Wendet eine Editierung erneut an
	 */
	@Override
	public default void wiederhole() {
		try {
			set(getNachher());
		} catch (Exception e) {
			log.log(Level.WARNING, e,
					() -> "Setzen von >%s< fehlgeschlagen".formatted(id()));
		}
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