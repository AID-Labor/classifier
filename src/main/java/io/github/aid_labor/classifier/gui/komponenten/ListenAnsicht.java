/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import java.lang.ref.WeakReference;

import javafx.beans.binding.When;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;


public abstract class ListenAnsicht<T> extends GridPane {
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
	
	protected final WeakReference<ObservableList<T>> inhaltListeRef;
	private ListChangeListener<T> listenBeobachter;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ListenAnsicht(ObservableList<T> inhaltListe) {
		this.setMinSize(0, 0);
		this.setVisible(!inhaltListe.isEmpty());
		this.prefHeightProperty().bind(new When(this.visibleProperty()).then(Region.USE_COMPUTED_SIZE).otherwise(0));
		this.prefWidthProperty().bind(new When(this.visibleProperty()).then(Region.USE_COMPUTED_SIZE).otherwise(0));
		this.inhaltListeRef = new WeakReference<ObservableList<T>>(inhaltListe);
		
		WeakReference<ListenAnsicht<T>> ref = new WeakReference<ListenAnsicht<T>>(this);
		listenBeobachter = aenderung -> {
			ref.get().setVisible(!ref.get().inhaltListeRef.get().isEmpty());
			fuelleListe();
		};
		inhaltListe.addListener(new WeakListChangeListener<>(listenBeobachter));
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
	
	protected final void fuelleListe() {
		this.getChildren().clear();
		int zeile = 0;
		for (var attribut : inhaltListeRef.get()) {
			var inhalt = erstelleZeile(attribut);
			this.addRow(zeile, inhalt);
			zeile++;
		}
	}
	
	protected abstract Node[] erstelleZeile(T attribut);
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}