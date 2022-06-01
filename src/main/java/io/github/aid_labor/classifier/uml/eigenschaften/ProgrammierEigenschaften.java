/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.eigenschaften;

import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;


public interface ProgrammierEigenschaften {
	
	public boolean istTypModifiziererErlaubt(KlassifiziererTyp typ, Modifizierer m);
	
	public boolean istAttributModifiziererErlaubt(KlassifiziererTyp typ, Modifizierer m);
	
	public boolean istMethodenModifiziererErlaubt(KlassifiziererTyp typ, Modifizierer m);
	
	public boolean erlaubtInstanzAttribute(KlassifiziererTyp typ);
	public boolean erlaubtSuperklasse(KlassifiziererTyp typ);
	
	/**
	 * Gibt eine passende Instanz fuer die uebergebene Programmiersprache zurueck
	 * 
	 * @param programmiersprache gewuenschte Programmiersprache
	 * @return passende Instanz fuer die uebergebene Programmiersprache oder {@code null},
	 *         falls keine passende Implementierung gefunden wurde
	 */
	public static ProgrammierEigenschaften get(Programmiersprache programmiersprache) {
		return switch (programmiersprache) {
			case Java -> Java.getInstanz();
			default -> null;
		};
	}
	
}