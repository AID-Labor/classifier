/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.util.TreeSet;
import java.util.logging.Logger;


/**
 * Set, das eine maximale Groesse hat. Wenn beim Hinzufuegen die maximale Groesse
 * ueberschritten wird, wird das kleinste Element automatisch entfernt.
 * 
 * @author Tim Muehle
 *
 */
public class AutomatischEntfernendesSet<E> extends TreeSet<E> {
	private static final Logger log = Logger
			.getLogger(AutomatischEntfernendesSet.class.getName());
	
	private static final long serialVersionUID = -3027636633260070772L;

// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final int maximaleAnzahl;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public AutomatischEntfernendesSet(int maximaleAnzahl) {
		super();
		if(maximaleAnzahl < 1) {
			throw new IllegalArgumentException("maximaleAnzahl muss groesser als 0 sein");
		}
		this.maximaleAnzahl = maximaleAnzahl;
	}
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public int getMaximaleAnzahl() {
		return maximaleAnzahl;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public boolean add(E e) {
		boolean hinzugefuegt = super.add(e);
		log.finest(() -> """
				Element %s hinzugefuegt.
				Aktuelle Groesse: %d
				Maximale Groesse: %d""".formatted(e, this.size(), this.maximaleAnzahl));
		
		if(hinzugefuegt && this.size() > this.maximaleAnzahl) {
			E entfernt = this.pollFirst();
			log.finer(() -> "Letztes Element [] entfernt".formatted(entfernt));
		}
		
		return hinzugefuegt;
	}

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}