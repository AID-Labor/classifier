/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.eigenschaften;

import java.util.List;

import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;


public interface ProgrammierEigenschaften {
	
	public List<Modifizierer> getTypModifizierer(KlassifiziererTyp typ);
	
	public List<Modifizierer> getAttributModifizierer(KlassifiziererTyp typ);
	
	public List<Modifizierer> getMethodenModifizierer(KlassifiziererTyp typ);
	
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