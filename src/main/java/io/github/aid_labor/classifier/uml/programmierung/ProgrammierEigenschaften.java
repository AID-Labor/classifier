/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.programmierung;

import java.util.List;

import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Datentyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Modifizierer;


public interface ProgrammierEigenschaften {

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
	
	
	public boolean istTypModifiziererErlaubt(KlassifiziererTyp typ, Modifizierer m);
	public boolean istAttributModifiziererErlaubt(KlassifiziererTyp typ, Modifizierer m);
	public boolean istMethodenModifiziererErlaubt(KlassifiziererTyp typ, Modifizierer m);
	
	public Modifizierer[] getAttributModifizierer(KlassifiziererTyp typ);
	public Modifizierer[] getMethodenModifizierer(KlassifiziererTyp typ);
	
	public boolean erlaubtInstanzAttribute(KlassifiziererTyp typ);
	public boolean erlaubtSuperklasse(KlassifiziererTyp typ);
	public boolean erlaubtAbstrakteMethode(KlassifiziererTyp typ);
	public boolean erlaubtNichtAbstrakteMethode(KlassifiziererTyp typ);
	
	public Datentyp getLetzerDatentyp();
	public void setLetzerDatentyp(Datentyp letzterDatentyp);
	
	public boolean istVoid(Datentyp datentyp);

	public List<Datentyp> getPrimitiveDatentypen();
	public Datentyp getVoid();
	public List<Datentyp> getBekannteDatentypen();


	public Modifizierer getStandardAttributModifizierer(KlassifiziererTyp typ);
	public Modifizierer getStandardMethodenModifizierer(KlassifiziererTyp typ);

	
}