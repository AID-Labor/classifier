/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.util.Objects;


public class RessourceBuilder {
	
	private final ProgrammDetails programm;
	
	public RessourceBuilder(ProgrammDetails programm) {
		this.programm = Objects.requireNonNull(programm);
	}
	
	public Ressource erzeuge(RessourceTyp typ, String name) {
		String konfigurationspfad = OS.getDefault().getKonfigurationsOrdner(this.programm);
		return new Ressource(konfigurationspfad.concat(typ.getOrdner()), name);
	}
	
	public Ressource erzeuge(String unterOrdner, String name) {
		String konfigurationspfad = OS.getDefault().getKonfigurationsOrdner(this.programm);
		return new Ressource(konfigurationspfad.concat(unterOrdner), name);
	}
	
}