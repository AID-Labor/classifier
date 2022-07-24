/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import org.controlsfx.control.SegmentedButton;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.gui.komponenten.VerbindungBearbeitenListe;
import io.github.aid_labor.classifier.gui.komponenten.VerbindungBearbeitenListe.Typ;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;


public class UMLVerbindungenBearbeitenDialog extends Alert {
//	private static final Logger log = Logger.getLogger(UMLKlassifiziererBearbeitenDialog.class.getName());

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public static final String CSS_EINGABE_FEHLER = "eingabefehler";
	
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
	
	private final UMLProjekt umlProjekt;
	private final Sprache sprache;
	private final SegmentedButton buttonBar;
	private final ToggleButton vererbung;
	private final ToggleButton assoziation;
	private final ValidationSupport eingabeValidierung;
	private final List<ChangeListener<ValidationResult>> validierungsBeobachter;
	private final boolean starteMitAssoziation;
	private final List<Runnable> loeseBindungen;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLVerbindungenBearbeitenDialog(UMLProjekt projekt, boolean starteMitAssoziation) {
		super(AlertType.NONE);
		this.sprache = new Sprache();
		this.starteMitAssoziation = starteMitAssoziation;
		this.eingabeValidierung = new ValidationSupport();
		this.validierungsBeobachter = new LinkedList<>();
		this.loeseBindungen = new LinkedList<>();
		this.umlProjekt = projekt;
		
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
				"UMLKlassifiziererBearbeitenDialog");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		this.assoziation = new ToggleButton(sprache.getText("assoziationen", "Assoziationen"));
		this.vererbung = new ToggleButton(sprache.getText("vererbungen", "Vererbungen"));
		this.buttonBar = new SegmentedButton(assoziation, vererbung);
		this.buttonBar.getToggleGroup().selectToggle(starteMitAssoziation ? assoziation : vererbung);
		HBox buttonContainer = initialisiereButtonBar();
		
		BorderPane wurzel = new BorderPane();
		wurzel.setTop(buttonContainer);
		
		erzeugeInhalt(wurzel);
		
		getDialogPane().setContent(wurzel);
		initialisiereButtons();
		this.setResizable(true);
		this.getDialogPane().setPrefSize(920, 512);
		
		this.setOnHidden(e -> {
			for (var beobachter : validierungsBeobachter) {
				eingabeValidierung.validationResultProperty().removeListener(beobachter);
			}
			
			for (Node n : this.getDialogPane().getChildren()) {
				entferneAlleBeobachter(n);
				if (n instanceof AutoCloseable ac) {
					try {
						ac.close();
					} catch (Exception e1) {
						// 
					}
				}
			}
			
			for (var c : eingabeValidierung.getRegisteredControls()) {
				eingabeValidierung.registerValidator(c, Validator.createPredicateValidator(x -> true, ""));
			}
			
			for (var r : loeseBindungen) {
				r.run();
			}
			loeseBindungen.clear();
			
			for (var attribut : this.getClass().getDeclaredFields()) {
				try {
					if (!Modifier.isStatic(attribut.getModifiers()) && !attribut.getName().equals("istGeschlossen")) {
						attribut.setAccessible(true);
						attribut.set(this, null);
					}
				} catch (Exception ex) {
					// ignore
				}
			}
		});
	}
	
	private void entferneAlleBeobachter(Node n) {
		NodeUtil.entferneSchwacheBeobachtung(n);
		n.getProperties().remove(this);
		if (n instanceof Parent p) {
			for (Node kind : p.getChildrenUnmodifiable()) {
				entferneAlleBeobachter(kind);
			}
		}
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
	
	private HBox initialisiereButtonBar() {
		vererbung.setSelected(!starteMitAssoziation);
		assoziation.setSelected(starteMitAssoziation);
		for (var button : buttonBar.getButtons()) {
			button.setFocusTraversable(false);
		}
		NodeUtil.beobachteSchwach(buttonBar, buttonBar.getToggleGroup().selectedToggleProperty(),
				(alteWahl, neueWahl) -> {
					if (neueWahl == null) {
						buttonBar.getToggleGroup().selectToggle(alteWahl);
					}
				});
		HBox buttonContainer = new HBox(buttonBar);
		buttonContainer.setPadding(new Insets(5, 20, 40, 20));
		buttonContainer.setAlignment(Pos.CENTER);
		return buttonContainer;
	}
	
	private void erzeugeInhalt(BorderPane wurzel) {
		var assoziationAnzeige = new VerbindungBearbeitenListe(
				new String[] { "Klasse/Interface", "Verwendet...", "Ausgeblendet" }, 
				Typ.ASSOZIATION, eingabeValidierung, sprache, this.umlProjekt, true);
		var vererbungAnzeige = new VerbindungBearbeitenListe(
				new String[] { "Klasse/Interface", "Superklasse/Interface", "Ausgeblendet" }, 
				Typ.VERERBUNG, eingabeValidierung, sprache, this.umlProjekt, true);
		
		StackPane container = new StackPane(assoziationAnzeige, vererbungAnzeige);
		container.setPadding(new Insets(0, 20, 10, 20));
		container.setMaxWidth(Region.USE_PREF_SIZE);
		container.setAlignment(Pos.TOP_CENTER);
		BorderPane.setAlignment(container, Pos.TOP_CENTER);
		wurzel.setCenter(container);
		
		ueberwacheSelektion(this.assoziation, assoziationAnzeige);
		ueberwacheSelektion(this.vererbung, vererbungAnzeige);
	}
	
	private void ueberwacheSelektion(ToggleButton button, Node anzeige) {
		anzeige.setVisible(button.isSelected());
		anzeige.visibleProperty().bind(button.selectedProperty());
	}
	
	private void initialisiereButtons() {
		ButtonType[] buttons = { new ButtonType(sprache.getText("APPLY", "Anwenden"), ButtonData.FINISH),
			new ButtonType(sprache.getText("CANCEL_CLOSE", "Abbrechen"), ButtonData.BACK_PREVIOUS) };
		this.getDialogPane().getButtonTypes().addAll(buttons);
		this.getDialogPane().lookupButton(buttons[0]).disableProperty().bind(eingabeValidierung.invalidProperty());
		loeseBindungen.add(this.getDialogPane().lookupButton(buttons[0]).disableProperty()::unbind);
	}
	
}