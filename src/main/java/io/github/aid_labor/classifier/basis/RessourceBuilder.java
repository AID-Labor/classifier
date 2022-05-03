/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.nio.file.Path;
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
		Path konfigurationspfad = OS.getDefault().getKonfigurationsOrdnerPath(this.programm);
		return new Ressource(konfigurationspfad.resolve(typ.getOrdner()).toString(), name);
	}
	
	public Ressource erzeuge(String unterOrdner, String name) {
		Path konfigurationspfad = OS.getDefault().getKonfigurationsOrdnerPath(this.programm);
		return new Ressource(konfigurationspfad.resolve(unterOrdner).toString(), name);
	}
	
	public Ressource erzeuge(String name) {
		Path konfigurationspfad = OS.getDefault().getKonfigurationsOrdnerPath(this.programm);
		return new Ressource(konfigurationspfad.toString(), name);
	}
	
	public Ressource css(String name) {
		return this.erzeuge(RessourceTyp.CSS, name);
	}
	
	public Ressource grafik(String name) {
		return this.erzeuge(RessourceTyp.GRAFIK, name);
	}
	
	public Ressource konfigurationsdatei(String name) {
		return this.erzeuge(RessourceTyp.KONFIGURATIONSDATEI, name);
	}
	
}