/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.elemente;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.uml.eigenschaften.Attribut;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


public class AttributListeAnsicht extends GridPane {
	private static final Logger log = Logger.getLogger(AttributListeAnsicht.class.getName());
	
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
	
	public AttributListeAnsicht(ObservableList<Attribut> items) {
		this.setMinSize(0, 0);
		this.setVisible(items.size() > 0);
		this.setHgap(5);
		
		WeakReference<AttributListeAnsicht> ref = new WeakReference<AttributListeAnsicht>(
				this);
		items.addListener(new ListChangeListener<Attribut>() {
			@Override
			public void onChanged(Change<? extends Attribut> aenderung) {
				ref.get().setVisible(items.size() > 0);
				
				while (aenderung.next()) {
					if (aenderung.wasPermutated()) {
						for (var kind : getChildren()) {
							GridPane.setRowIndex(kind,
									aenderung.getPermutation(GridPane.getRowIndex(kind)));
						}
					}
					if (aenderung.wasRemoved()) {
						entferne(aenderung.getFrom(), aenderung.getTo());
					}
					if (aenderung.wasAdded()) {
						fuelleListe(aenderung.getFrom(), aenderung.getAddedSubList());
					}
				}
			}

			
		});
		
	}
	
	private void entferne(int von, int bis) {
		int verschiebung = bis - von;
		var iterator = this.getChildren().iterator();
		iterator.forEachRemaining(kind -> {
			if (GridPane.getRowIndex(kind) >= von && GridPane.getRowIndex(kind) < bis) {
				iterator.remove();
			} else if (GridPane.getRowIndex(kind) >= bis) {
				GridPane.setRowIndex(kind, GridPane.getRowIndex(kind) - verschiebung);
			}
		});
	}
	
	private void verschiebe(int von, int bis, int verschiebung) {
		this.getChildren().forEach(kind -> {
			if (GridPane.getRowIndex(kind) >= von && GridPane.getRowIndex(kind) < bis) {
				GridPane.setRowIndex(kind, GridPane.getRowIndex(kind) + verschiebung);
			}
		});
	}
	
	private void fuelleListe(int von, List<? extends Attribut> neueAttribute) {
		verschiebe(von, this.getRowCount(), neueAttribute.size());
		for (int zeile = von; zeile < neueAttribute.size(); zeile++) {
			var inhalt = erstelleAttributAnsicht(neueAttribute.get(zeile));
			this.addRow(zeile, inhalt);
		}
	}
	
	private Node[] erstelleAttributAnsicht(Attribut attribut) {
		var sichtbarkeit = new TextField();
		sichtbarkeit.textProperty()
				.bind(attribut.getSichtbarkeitProperty().get().getKurzform());
		attribut.getSichtbarkeitProperty().addListener(o -> {
			sichtbarkeit.textProperty()
					.bind(attribut.getSichtbarkeitProperty().get().getKurzform());
		});
		
		var beschreibung = new TextField();
		beschreibung.textProperty().bind(attribut.getNameProperty().concat(": ")
				.concat(attribut.getDatentypProperty().getName()));
		attribut.getDatentypProperty().addListener(o -> {
			beschreibung.textProperty().bind(attribut.getNameProperty().concat(": ")
					.concat(attribut.getDatentypProperty().getName()));
		});
		
		sichtbarkeit.setEditable(false);
		sichtbarkeit.getStyleClass().clear();
		sichtbarkeit.getStyleClass().add("label");
		beschreibung.setEditable(false);
		beschreibung.getStyleClass().clear();
		beschreibung.getStyleClass().add("label");
		
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