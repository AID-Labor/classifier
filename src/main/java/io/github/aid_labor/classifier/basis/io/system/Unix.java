/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.io.system;

import io.github.aid_labor.classifier.basis.ProgrammDetails;

sealed class Unix extends OS permits Linux, MacOS {
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##

//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	// Sichtbarkeit des Konstruktors auf package beschraenken
	Unix() {
		super();
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * {@inheritDoc}
	 * 
	 * @implNote Als Konfigurationsordner wird {@code $HOME/.config/{programm.name()}}
	 *           eingestellt
	 */
	@Override
	public String getKonfigurationsOrdner(ProgrammDetails programm) {
		return this.pfadAus(new StringBuilder(this.nutzerOrdner), ".config", programm.name())
				.toString();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @apiNote Diese Implementierung gibt standardmaessig {@code false zurueck}. Abgeleitete
	 *          Klassen sollten diese Methode sinnvol ueberschreiben, wenn ein Darkmode
	 *          ermittelt werden kann.
	 */
	@Override
	public boolean systemNutztDarkTheme() {
		return false;
	}
	
	@Override
	public final boolean istUnix() {
		return true;
	}
}
