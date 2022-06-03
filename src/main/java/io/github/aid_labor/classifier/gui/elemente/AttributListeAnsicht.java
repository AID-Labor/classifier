/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.elemente;

import java.lang.ref.WeakReference;

import com.dlsc.gemsfx.EnhancedLabel;

import io.github.aid_labor.classifier.uml.eigenschaften.Attribut;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;


public class AttributListeAnsicht extends GridPane {
//	private static final Logger log = Logger.getLogger(AttributListeAnsicht.class.getName());

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
	
	private final ObservableList<Attribut> attributListe;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public AttributListeAnsicht(ObservableList<Attribut> attributListe) {
		this.setMinSize(0, 0);
		this.setVisible(attributListe.size() > 0);
		this.attributListe = attributListe;
		this.getStyleClass().add("attribut-liste");
		
		WeakReference<AttributListeAnsicht> ref = new WeakReference<AttributListeAnsicht>(
				this);
		attributListe.addListener(new ListChangeListener<Attribut>() {
			@Override
			public void onChanged(Change<? extends Attribut> aenderung) {
				ref.get().setVisible(attributListe.size() > 0);
				
				fuelleListe();
			}
		});
		
		fuelleListe();
	}
	
	private void fuelleListe() {
		this.getChildren().clear();
		int zeile = 0;
		for (var attribut : attributListe) {
			var inhalt = erstelleAttributAnsicht(attribut);
			this.addRow(zeile, inhalt);
			zeile++;
		}
	}
	
	private Node[] erstelleAttributAnsicht(Attribut attribut) {
		var sichtbarkeit = new EnhancedLabel();
		sichtbarkeit.textProperty()
				.bind(attribut.getSichtbarkeitProperty().get().getKurzform());
		attribut.getSichtbarkeitProperty().addListener((p, alt, neu) -> {
			sichtbarkeit.textProperty().unbind();
			sichtbarkeit.textProperty()
					.bind(attribut.getSichtbarkeitProperty().get().getKurzform());
		});
		
		var beschreibung = new EnhancedLabel();
		beschreibung.textProperty().bind(
				attribut.getNameProperty()
				.concat(": ")
				.concat(attribut.getDatentyp().getTypNameProperty())
				.concat(new When(attribut.getInitialwertProperty().isEmpty())
						.then("")
						.otherwise(
								Bindings.concat(" = ", attribut.getInitialwertProperty()))));
		
		
		sichtbarkeit.getStyleClass().addAll("sichtbarkeit-label");
		
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