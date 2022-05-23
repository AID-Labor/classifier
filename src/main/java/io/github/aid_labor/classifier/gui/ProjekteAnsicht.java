/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dlsc.gemsfx.DialogPane;

import io.github.aid_labor.classifier.basis.io.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener.Change;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;


public class ProjekteAnsicht {
	private static final Logger log = Logger.getLogger(ProjekteAnsicht.class.getName());
	
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
	
	private final ReadOnlyObjectWrapper<UMLProjekt> angezeigtesProjekt;
	/**
	 * Set, dass alle geoeffneten Projekte enthaelt. Projekte mit dem gleichen Speicherort
	 * duerfen nicht doppelt enthalten sein!
	 */
	private final ObservableSet<UMLProjekt> projekte;
	private final TabPane tabAnsicht;
	private final DialogPane overlayDialog;
	private final Sprache sprache;
	private final ProgrammDetails programm;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ProjekteAnsicht(DialogPane overlayDialog, ProgrammDetails programm) {
		this.projekte = FXCollections.observableSet(
				new TreeSet<>((p1, p2) -> {
					if(p1.getSpeicherort() == null) {
						return -1;
					} else if (p2.getSpeicherort() == null) {
						return 1;
					} else {
						return p1.getSpeicherort().compareTo(p2.getSpeicherort());
					}
				}));
		this.overlayDialog = overlayDialog;
		this.sprache = new Sprache();
		this.programm = programm;
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache,
				Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(), "ProjektAnsicht");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		this.angezeigtesProjekt = new ReadOnlyObjectWrapper<>();
		this.tabAnsicht = new TabPane();
		
		setzeListener();
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public ReadOnlyObjectProperty<UMLProjekt> getAngezeigtesProjektProperty() {
		return angezeigtesProjekt.getReadOnlyProperty();
	}
	
	public Parent getAnsicht() {
		return this.tabAnsicht;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	protected DialogPane getOverlayDialog() {
		return this.overlayDialog;
	}
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void setzeListener() {
		this.tabAnsicht.getSelectionModel().selectedItemProperty()
				.addListener((itemProperty, alteWahl, neueWahl) -> {
					log.finest(() -> "neuer Tab ausgewaehlt: %s"
							.formatted(neueWahl == null ? "null" : neueWahl.getText()));
					if (neueWahl instanceof ProjektAnsicht tab) {
						angezeigtesProjekt.set(tab.getProjekt());
					} else {
						angezeigtesProjekt.set(null);
					}
				});
		
		this.angezeigtesProjekt.addListener((property, alt, neu) -> {
			String nameAlt = alt == null ? "null" : alt.getName();
			String nameNeu = neu == null ? "null" : neu.getName();
			log.finest(() -> "aktuelles Projekt geaendert -> alt: %s - neu: %s"
					.formatted(nameAlt, nameNeu));
		});
		
		this.projekte.addListener((Change<?> aenderung) -> {
			log.fine(() -> "%cnderung der angezeigten Projekte > > > > > > > > > > > > > > >"
					.formatted(Umlaute.AE));
			if (aenderung.getElementAdded() != null) {
				log.fine(() -> "Projekt hinzugef%cgt: %s".formatted(Umlaute.ue,
						aenderung.getElementAdded()));
			}
			if (aenderung.getElementRemoved() != null) {
				log.fine(
						() -> "Projekt entfernt: %s".formatted(aenderung.getElementRemoved()));
			}
			log.fine(() -> "%cnderung der angezeigten Projekte < < < < < < < < < < < < < < <"
					.formatted(Umlaute.AE));
		});
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public boolean angezeigtesProjektSpeichern() {
		var tab = this.tabAnsicht.getSelectionModel().getSelectedItem();
		if (tab instanceof ProjektAnsicht pa) {
			return pa.projektSpeichern();
		} else {
			return false;
		}
	}
	
	public boolean angezeigtesProjektSpeicherUnter() {
		var tab = this.tabAnsicht.getSelectionModel().getSelectedItem();
		if (tab instanceof ProjektAnsicht pa) {
			return pa.projektSpeichernUnter();
		} else {
			return false;
		}
	}
	
	/**
	 * Zeigt ein Projekt in einem neuen Tab an.
	 * 
	 * @param projekt anzuzeigendes Projekt
	 * @throws UnsupportedOperationException Wenn ein Projekt mit dem gleichen Speicherort
	 *                                       bereits geoeffnet ist
	 */
	public void zeigeProjekt(UMLProjekt projekt) throws UnsupportedOperationException {
		if(projekt == null) {
			var exc = new NullPointerException("Das uebergebene Projekt darf nicht null sein");
			log.log(Level.SEVERE, exc, () -> "Projekt kann nicht angezeigt werden");
			throw exc;
		}
		log.finest(() -> "Zeige Projekt %s in neuem Tab".formatted(projekt));
		boolean hinzugefuegt = this.projekte.add(projekt);
		
		if (!hinzugefuegt) {
			var exc = new UnsupportedOperationException("Projekt %s mit dem Speicherort '%s' "
					+ "kann nicht angezeigt werden, da dieses Projekt bereits geoeffnet ist"
							.formatted(projekt.getName(), projekt.getSpeicherort()));
			log.log(Level.INFO, exc, () -> "Projekt anzeigen abgebrochen");
			throw exc;
		}
		
		ProjektAnsicht tab = new ProjektAnsicht(projekt, sprache, overlayDialog, programm);
		
		this.tabAnsicht.getTabs().add(tab);
		this.tabAnsicht.getSelectionModel().select(tab);
		
		tab.setOnClosed(e -> this.projekte.remove(projekt));
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}