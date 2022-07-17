/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis.projekt.editierung;

import java.util.logging.Logger;

public interface EditierbarerBeobachter extends Editierbar, EditierBeobachter {
	
	static final Logger log = Logger.getLogger(EditierbarerBeobachter.class.getName());
	
	@Override
	default void verarbeiteEditierung(EditierBefehl editierung) {
		log.finest(() -> "verarbeite " + editierung);
		this.informiere(editierung);
	}
	
}