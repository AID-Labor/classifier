/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import com.dlsc.gemsfx.EnhancedLabel;

import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Attribut;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.When;
import javafx.collections.ObservableList;
import javafx.scene.Node;


public class AttributListeAnsicht extends ListenAnsicht<Attribut> {
//	private static final Logger log = Logger.getLogger(AttributListeAnsicht.class.getName());

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * CSS-Klasse {@code statisch} fuer statische Attribute
	 */
	public static String CSS_STATISCH_KLASSE = "statisch";
	
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
	
	public AttributListeAnsicht(ObservableList<Attribut> attributListe) {
		super(attributListe);
		fuelleListe();
	}
	
	@Override
	protected Node[] erstelleZeile(Attribut attribut) {
		var sichtbarkeit = new EnhancedLabel();
		sichtbarkeit.textProperty()
				.bind(attribut.sichtbarkeitProperty().get().getKurzform());
		attribut.sichtbarkeitProperty().addListener((p, alt, neu) -> {
			sichtbarkeit.textProperty().unbind();
			sichtbarkeit.textProperty()
					.bind(new StringBinding() {
						
						Observable x;
						{
							super.bind(attribut.sichtbarkeitProperty());
						}
						
						@Override
						protected String computeValue() {
							if (x != null) {
								super.unbind(x);
							}
							var prop = attribut.sichtbarkeitProperty().get();
							
							if (prop == null) {
								x = null;
								return "";
							} else {
								x = prop.getKurzform();
								super.bind(prop.getKurzform());
								return prop.getKurzform().get();
							}
						}
						
					});
		});
		
		var beschreibung = new EnhancedLabel();
		beschreibung.textProperty().bind(
				attribut.nameProperty()
				.concat(": ")
				.concat(attribut.getDatentyp().getTypNameProperty())
				.concat(new When(attribut.initialwertProperty().isEmpty())
						.then("")
						.otherwise(
								Bindings.concat(" = ", attribut.initialwertProperty()))));
		
		
		sichtbarkeit.getStyleClass().addAll("sichtbarkeit-label");
		
		if (attribut.istStatisch()) {
			beschreibung.getStyleClass().add(CSS_STATISCH_KLASSE);
		} else {
			beschreibung.getStyleClass().remove(CSS_STATISCH_KLASSE);
		}
		
		attribut.istStatischProperty().addListener((property, alt, istStatisch) -> {
			if (istStatisch) {
				beschreibung.getStyleClass().add(CSS_STATISCH_KLASSE);
			} else {
				beschreibung.getStyleClass().remove(CSS_STATISCH_KLASSE);
			}
		});
		
		return new Node[] { sichtbarkeit, beschreibung };
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