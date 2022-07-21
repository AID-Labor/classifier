/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.programmierung;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Datentyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Modifizierer;


public interface ProgrammierEigenschaften {
	
	public boolean istTypModifiziererErlaubt(KlassifiziererTyp typ, Modifizierer m);
	public boolean istAttributModifiziererErlaubt(KlassifiziererTyp typ, Modifizierer m);
	public boolean istMethodenModifiziererErlaubt(KlassifiziererTyp typ, Modifizierer m);
	
	public List<Modifizierer> getAttributModifizierer(KlassifiziererTyp typ);
	public List<Modifizierer> getMethodenModifizierer(KlassifiziererTyp typ, boolean istStatisch, boolean istAbstrakt);
	
	public boolean erlaubtInstanzAttribute(KlassifiziererTyp typ);
	public boolean erlaubtSuperklasse(KlassifiziererTyp typ);
	public boolean erlaubtAbstrakteMethode(KlassifiziererTyp typ);
	public boolean erlaubtNichtAbstrakteMethode(KlassifiziererTyp typ);
	
	public Datentyp getLetzerDatentyp();
	public void setLetzerDatentyp(Datentyp letzterDatentyp);
	
	public boolean istVoid(Datentyp datentyp);

	public List<String> getPrimitiveDatentypen();
	public String getVoid();
	public SortedSet<String> getBekannteKlassen();
	public SortedSet<String> getBekannteInterfaces();
	public SortedSet<String> getBekannteEnumerationen();
	public Map<String, String> getKlassenPaketMap();


	public Modifizierer getStandardAttributModifizierer(KlassifiziererTyp typ);
	public Modifizierer getStandardMethodenModifizierer(KlassifiziererTyp typ);

	
}