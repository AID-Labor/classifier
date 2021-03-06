/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.klassendiagramm;

public enum Orientierung {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	OBEN, RECHTS, UNTEN, LINKS, UNBEKANNT;
	
	// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private static final int deltaMin = 40;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public static boolean orientierungErlaubt(Orientierung neu, Orientierung andere, Position pos1, Position pos2) {
		if (neu == null || neu.equals(andere)) {
			return false;
		}
		Punkt pos1_OL = new Punkt(pos1.getX(), pos1.getY());
		Punkt pos2_OL = new Punkt(pos2.getX(), pos2.getY());
		Punkt pos1_UR = new Punkt(pos1.getMaxX(), pos1.getMaxY());
		Punkt pos2_UR = new Punkt(pos2.getMaxX(), pos2.getMaxY());
		return switch (neu) {
			case OBEN -> pos1_OL.istUnterhalbVon(pos2_UR, deltaMin);
			case RECHTS -> pos1_UR.istLinksVon(pos2_UR, deltaMin);
			case UNTEN -> pos1_UR.istOberhalbVon(pos2_OL, deltaMin);
			case LINKS -> pos1_OL.istRechtsVon(pos2_OL, deltaMin);
			case UNBEKANNT -> true;
		};
	}
	
	public static boolean orientierungErlaubt(Orientierung neu, Orientierung andere, Position pos1, Position pos2, 
			int deltaMin) {
		if (neu == null || neu.equals(andere)) {
			return false;
		}
		Punkt pos1_OL = new Punkt(pos1.getX(), pos1.getY());
		Punkt pos2_OL = new Punkt(pos2.getX(), pos2.getY());
		Punkt pos1_UR = new Punkt(pos1.getMaxX(), pos1.getMaxY());
		Punkt pos2_UR = new Punkt(pos2.getMaxX(), pos2.getMaxY());
		return switch (neu) {
			case OBEN -> pos1_OL.istUnterhalbVon(pos2_UR, deltaMin);
			case RECHTS -> pos1_UR.istLinksVon(pos2_UR, deltaMin);
			case UNTEN -> pos1_UR.istOberhalbVon(pos2_OL, deltaMin);
			case LINKS -> pos1_OL.istRechtsVon(pos2_OL, deltaMin);
			case UNBEKANNT -> true;
		};
	}
	
	// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private static class Punkt {
		private final double x, y;

		public Punkt(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public boolean istRechtsVon(Punkt p, double deltaMin) {
			return this.x - deltaMin > p.x;
		}
		
		public boolean istLinksVon(Punkt p, double deltaMin) {
			return this.x + deltaMin < p.x;
		}
		
		public boolean istOberhalbVon(Punkt p, double deltaMin) {
			return this.y + deltaMin < p.y;
		}
		
		public boolean istUnterhalbVon(Punkt p, double deltaMin) {
			return this.y - deltaMin > p.y;
		}
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