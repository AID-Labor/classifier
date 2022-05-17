package io.github.aid_labor.classifier.basis;

import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Informationssammlung zu einem Programm
 * 
 * @author Tim Muehle
 *
 */
public record ProgrammDetails (
		String version,
		String name,
		String info,
		String lizenz,
		String homepage,
		Class<?> hauptklasse,
		ExtensionFilter[] dateiZuordnung
) {
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	// impliziter Konstruktor fuer die Record-Komponenten
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public String getVersionName() {
		return this.name  + " V-" + this.version;
	}
	
}
