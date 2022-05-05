/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.logging.Logger;


final class KlassenpfadRessource extends Ressource {
	
	private static final Logger log = Logger.getLogger(KlassenpfadRessource.class.getName());
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public KlassenpfadRessource(String ordner, String name) {
		super(ordner, name);
	}
	
	public KlassenpfadRessource(String pfad) {
		super(pfad);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@Override
	public Path alsPath() {
		log.finest(() -> "erzeuge Path fuer " + pfad);
		return Path.of(this.getClass().getResource(pfad).toExternalForm());
	}
	
	@Override
	public InputStream oeffneStream() {
		log.finest(() -> "oeffneStream fuer " + pfad);
		System.out.println(this.getClass().getResourceAsStream("LICENSE.txt"));
		return this.getClass().getResourceAsStream(pfad);
	}
	
}