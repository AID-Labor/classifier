/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.io;

import java.util.Objects;

import io.github.aid_labor.classifier.basis.io.system.OS;


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
	private StringBuilder pfad;
	private String name;
	private boolean nutztKonfigurationsordner;
	private boolean nutztKlassenpfad;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public RessourceBuilder(ProgrammDetails programm) {
		this.programm = Objects.requireNonNull(programm);
		reset();
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public RessourceBuilder konfigurationsOrdner() {
		if(nutztKlassenpfad) {
			throw new IllegalStateException("Es kann nur eine der beiden Optionen Klassenpfad "
					+ "oder Konfigurationsordner genutzt werden");
		}
		
		if(!pfad.isEmpty()) {
			pfad.setLength(0);
		}
		pfad.append(OS.getDefault().getKonfigurationsOrdner(programm));
		this.nutztKonfigurationsordner = true;
		return this;
	}
	
	public RessourceBuilder klassenpfad() {
		if(nutztKonfigurationsordner) {
			throw new IllegalStateException("Es kann nur eine der beiden Optionen Klassenpfad "
					+ "oder Konfigurationsordner genutzt werden");
		}
		this.nutztKlassenpfad = true;
		return this;
	}
	
	public RessourceBuilder alsTyp(RessourceTyp typ) {
		return inOrdner(typ.getOrdner());
	}
	
	public RessourceBuilder inOrdner(String ordner) {
		if(!pfad.isEmpty()) {
			pfad.append(System.getProperty("file.separator"));
		}
		pfad.append(Objects.requireNonNull(ordner));
		return this;
	}
	
	public RessourceBuilder name(String name) {
		if(name.isBlank()) {
			throw new IllegalStateException("Der Name darf kein leerer String sein");
		}
		this.name = name;
		return this;
	}
	
	public Ressource erzeuge() {
		Ressource r;
		if(nutztKlassenpfad) {
			r = new KlassenpfadRessource(pfad.toString(), name);
		} else {
			r = new LokaleRessource(pfad.toString(), name);
		}
		
		reset();
		return r;
	}

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void reset() {
		this.pfad = new StringBuilder();
		this.name = "";
		this.nutztKonfigurationsordner = false;
		this.nutztKlassenpfad = false;
	}
	
}