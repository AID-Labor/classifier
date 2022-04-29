/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.util.Objects;


public class RessourceBuilder {
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final ProgrammDetails programm;
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		
	public RessourceBuilder(ProgrammDetails programm) {
		this.programm = Objects.requireNonNull(programm);
	}
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

	public Ressource erzeuge(RessourceTyp typ, String name) {
		String konfigurationspfad = OS.getDefault().getKonfigurationsOrdner(this.programm);
		return new Ressource(konfigurationspfad.concat(typ.getOrdner()), name);
	}
	
	public Ressource erzeuge(String unterOrdner, String name) {
		String konfigurationspfad = OS.getDefault().getKonfigurationsOrdner(this.programm);
		return new Ressource(konfigurationspfad.concat(unterOrdner), name);
	}
	
	public Ressource css(String name) {
		return this.erzeuge(RessourceTyp.CSS, name);
	}
	
	public Ressource konfigurationsdatei(String name) {
		return this.erzeuge(RessourceTyp.KONFIGURATIONSDATEI, name);
	}
	
}