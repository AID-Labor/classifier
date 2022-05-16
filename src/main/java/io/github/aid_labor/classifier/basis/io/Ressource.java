/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract sealed class Ressource permits KlassenpfadRessource, LokaleRessource {
	
	private static Logger log = Logger.getLogger(Ressource.class.getName());
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	protected final String pfad;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	Ressource(String pfad) {
		this.pfad = pfad;
	}
	
	Ressource(String ordner, String name) {
		this(ordner + System.getProperty("file.separator") + name);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public abstract Path alsPath();
	
	public abstract InputStream oeffneStream() throws IOException;
	
	public String externeForm() {
		String externeForm = alsPath().toString();
		try {
			externeForm = alsPath().toFile().toURI().toURL().toExternalForm();
		} catch (MalformedURLException e) {
			log.log(Level.WARNING, e,
					() -> "URL fuer %s konnte nicht erzeugt werden".formatted(alsPath()));
			try {
				externeForm = alsPath().toRealPath().toString();
			} catch (IOException e1) {
				log.log(Level.WARNING, e,
						() -> "Realer Pfad fuer %s konnte nicht erzeugt werden"
								.formatted(alsPath()));
			}
		}
		
		return externeForm;
	}
	
	@Override
	public String toString() {
		return alsPath().toString();
	}
	
}