/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.gui.util;

import java.util.Optional;

import com.dlsc.gemsfx.SearchField;
import com.tobiasdiez.easybind.EasyBind;
import com.tobiasdiez.easybind.Subscription;

import io.github.aid_labor.classifier.basis.validierung.SimpleValidierung;
import io.github.aid_labor.classifier.basis.validierung.Validierung;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;

public class FXValidierungUtil {

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
    public static final String CSS_EINGABE_FEHLER = "eingabefehler";
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
    
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
    public static Subscription setzeValidierungStyle(Node node, Validierung validierung) {
        return EasyBind.subscribe(validierung.isValidProperty(), valid -> {
            if (valid.booleanValue()) {
                if (node.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
                    node.getStyleClass().remove(CSS_EINGABE_FEHLER);
                }
            } else {
                if (!node.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
                    node.getStyleClass().add(CSS_EINGABE_FEHLER);
                }
            }
        });
    }
    
    public static Subscription setzePlatzhalter(TextInputControl eingabefeld, SimpleValidierung validierung) {
        return EasyBind.subscribe(validierung.isValidProperty(), valid -> {
            if (valid.booleanValue()) {
                if (eingabefeld.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
                    eingabefeld.getStyleClass().remove(CSS_EINGABE_FEHLER);
                }
                eingabefeld.setPromptText(null);
                eingabefeld.setTooltip(null);
            } else {
                if (!eingabefeld.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
                    eingabefeld.getStyleClass().add(CSS_EINGABE_FEHLER);
                }
                Optional<String> fehler = validierung.getHighestErrorMessage();
                if (fehler.isPresent()) {
                    eingabefeld.setPromptText(fehler.get());
                    eingabefeld.setTooltip(new Tooltip(fehler.get()));
                } else {
                    eingabefeld.setPromptText(null);
                    eingabefeld.setTooltip(null);
                }
            }
        });
    }
    
    public static Subscription setzePlatzhalter(SearchField<?> eingabefeld, SimpleValidierung validierung) {
        return EasyBind.subscribe(validierung.isValidProperty(), valid -> {
            if (valid.booleanValue()) {
                if (eingabefeld.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
                    eingabefeld.getStyleClass().remove(CSS_EINGABE_FEHLER);
                }
                eingabefeld.setPromptText(null);
                eingabefeld.setTooltip(null);
            } else {
                if (!eingabefeld.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
                    eingabefeld.getStyleClass().add(CSS_EINGABE_FEHLER);
                }
                Optional<String> fehler = validierung.getHighestErrorMessage();
                if (fehler.isPresent()) {
                    eingabefeld.setPromptText(fehler.get());
                    eingabefeld.setTooltip(new Tooltip(fehler.get()));
                } else {
                    eingabefeld.setPromptText(null);
                    eingabefeld.setTooltip(null);
                }
            }
        });
    }
    
    public static Subscription setzePlatzhalter(CheckBox eingabefeld, SimpleValidierung validierung) {
        return EasyBind.subscribe(validierung.isValidProperty(), valid -> {
            if (valid.booleanValue()) {
                if (eingabefeld.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
                    eingabefeld.getStyleClass().remove(CSS_EINGABE_FEHLER);
                }
                eingabefeld.setTooltip(null);
            } else {
                if (!eingabefeld.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
                    eingabefeld.getStyleClass().add(CSS_EINGABE_FEHLER);
                }
                Optional<String> fehler = validierung.getHighestErrorMessage();
                if (fehler.isPresent()) {
                    eingabefeld.setTooltip(new Tooltip(fehler.get()));
                } else {
                    eingabefeld.setTooltip(null);
                }
            }
        });
    }
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private FXValidierungUtil() {
        // Hilfsklasse, nicht instanziierbar!
        throw new UnsupportedOperationException("Hilfsklasse darf nicht instanziiert werden!");
    }
	
}