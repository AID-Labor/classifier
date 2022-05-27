/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.gui.elemente;

import java.util.logging.Logger;

import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKommentar;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class UMLKommentarAnsicht extends UMLElementBasisAnsicht<UMLKommentar> {
	private static final Logger log = Logger.getLogger(UMLKommentarAnsicht.class.getName());

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	
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
	
	public UMLKommentarAnsicht(UMLKommentar kommentar) {
		super(kommentar);
		this.setMinSize(50, 50);
		this.setBackground(new Background(new BackgroundFill(Color.BEIGE, new CornerRadii(0), new Insets(0))));
	}
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
	
}