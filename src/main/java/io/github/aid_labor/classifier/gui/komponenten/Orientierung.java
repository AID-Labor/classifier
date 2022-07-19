/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import io.github.aid_labor.classifier.uml.klassendiagramm.Position;


enum Orientierung {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	OBEN, RECHTS, UNTEN, LINKS, UNBEKANNT;
	
	// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private static final int deltaMin = 30;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	static boolean orientierungErlaubt(Orientierung neu, Orientierung andere, Position pos1, Position pos2) {
//		System.out.println(neu + " <=> " + andere);
//		if (UNBEKANNT.equals(neu) || UNBEKANNT.equals(andere)) {
//			return true;
//		}
		if (neu.equals(andere)) {
			return false;
		}
//		return switch (neu) {
//			case OBEN -> pruefeObenErlaubt(andere, pos1, pos2);
//			case RECHTS -> pruefeRechtsErlaubt(andere, pos1, pos2);
//			case UNTEN -> pruefeUntenErlaubt(andere, pos1, pos2);
//			case LINKS -> pruefeLinksErlaubt(andere, pos1, pos2);
//			case UNBEKANNT -> true;
//		};
		var x = switch (neu) {
			case OBEN -> pos1.istUnterhalbVon(pos2);
			case RECHTS -> pos1.istLinksVon(pos2);
			case UNTEN -> pos1.istOberhalbVon(pos2);
			case LINKS -> pos1.istRechtsVon(pos2);
			case UNBEKANNT -> true;
		};
		System.out.println("Pos1: " + pos1 + " Pos2: " + pos2 + " -> " + neu + " = " + x);
		return x;
	}
	
	// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private static boolean pruefeObenErlaubt(Orientierung andere, Position pos1, Position pos2) {
		double deltaLinks = pos2.getX() - pos1.getX();
		double deltaRechts = pos2.getMaxX() - pos1.getMaxX();
		return switch (andere) {
			// TODO
			case OBEN -> false;
			case RECHTS -> deltaRechts >= deltaMin;
			case UNTEN -> pos2.getMaxY() + deltaMin <= pos1.getY();
			case LINKS -> deltaLinks >= deltaMin;
			case UNBEKANNT -> true;
		};
	}
	
	private static boolean pruefeRechtsErlaubt(Orientierung andere, Position pos1, Position pos2) {
		return switch (andere) {
			case OBEN -> pos1.getMaxX() + deltaMin <= pos2.getMaxX();
			case RECHTS -> false;
			case UNTEN -> pos1.getMaxX() + deltaMin <= pos2.getMaxX();
			case LINKS -> pos1.getMaxX() + deltaMin <= pos2.getX();
			case UNBEKANNT -> true;
		};
	}
	
	private static boolean pruefeUntenErlaubt(Orientierung andere, Position pos1, Position pos2) {
		double deltaLinks = pos2.getX() - pos1.getX();
		double deltaRechts = pos2.getMaxX() - pos1.getMaxX();
		return switch (andere) {
			// TODO
			case OBEN -> pos1.getMaxY() + deltaMin >= pos2.getY();
			case RECHTS -> pos2.getMaxX() + deltaMin >= pos1.getMaxX();
			case UNTEN -> false;
			case LINKS -> pos1.getX() + deltaMin >= pos2.getX();
			case UNBEKANNT -> true;
		};
	}
	
	private static boolean pruefeLinksErlaubt(Orientierung andere, Position pos1, Position pos2) {
		return switch (andere) {
			case OBEN -> pos2.getX() + deltaMin >= pos1.getX();
			case RECHTS -> pos2.getMaxX() + deltaMin >= pos1.getX();
			case UNTEN -> pos2.getMaxX() + deltaMin <= pos1.getX();
			case LINKS -> false;
			case UNBEKANNT -> true;
		};
	}

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

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


}