/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis;

public enum RessourceTyp {
	CSS("/css"), EINSTELLUNGSDATEI("/einstellungen");
	
	private final String ordner;
	
	private RessourceTyp(String ordner) {
		this.ordner = ordner;
	}
	
	protected String getOrdner() {
		return this.ordner;
	}
}