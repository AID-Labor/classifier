/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.kordamp.ikonli.typicons.Typicons;

import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindung;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindungstyp;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;


public class UMLVerbindungenBearbeitenDialog2 extends Alert {
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
	private final List<String> vorhandeneElementNamen;
	private final List<Runnable> loeseBindungen;
	private final boolean starteMitAssoziation;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLVerbindungenBearbeitenDialog2(UMLProjekt projekt, boolean starteMitAssoziation) {
		super(AlertType.NONE);
		this.sprache = new Sprache();
		this.starteMitAssoziation = starteMitAssoziation;
		this.eingabeValidierung = new ValidationSupport();
		this.validierungsBeobachter = new LinkedList<>();
		this.loeseBindungen = new LinkedList<>();
		this.umlProjekt = projekt;
		List<UMLKlassifizierer> klassen = projekt.getDiagrammElemente().stream()
				.filter(UMLKlassifizierer.class::isInstance).map(UMLKlassifizierer.class::cast).toList();
		this.vorhandeneElementNamen = klassen.parallelStream().map(UMLDiagrammElement::getName).toList();
		
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
			}
			
			for (var c : eingabeValidierung.getRegisteredControls()) {
				eingabeValidierung.registerValidator(c, Validator.createPredicateValidator(x -> true, ""));
			}
			for (var prop : loeseBindungen) {
				prop.run();
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
		var assoziationAnzeige = erzeugeTabellenAnzeige(
				new String[] { "Klasse/Interface", "Verwendet...", "Ausgeblendet" },
				this.umlProjekt.getAssoziationen(),
				this.erstelleAssoziationZeile(), event -> {
					var verbindung = new UMLVerbindung(UMLVerbindungstyp.ASSOZIATION, "", "");
					verbindung.setzeAutomatisch(false);
					this.umlProjekt.getAssoziationen().add(verbindung);
				});
//		var assoziationAnzeige = erzeugeTabellenAnzeige(
//				new String[] { "Klasse/Interface", "Verwendet...", "Ausgeblendet" },
//				this.umlProjekt.getVerbindungen().filtered(v -> v.getTyp().equals(UMLVerbindungstyp.ASSOZIATION)),
//				this.erstelleAssoziationZeile(), event -> {
//					var verbindung = new UMLVerbindung(UMLVerbindungstyp.ASSOZIATION, "", "");
//					verbindung.setzeAutomatisch(false);
//					this.umlProjekt.getVerbindungen().add(verbindung);
//				});
//		var vererbungAnzeige = erzeugeTabellenAnzeige(
//				new String[] { "Klasse/Interface", "Superklasse/Interface", "Ausgeblendet" },
//				this.umlProjekt.getVerbindungen().filtered(v -> !v.getTyp().equals(UMLVerbindungstyp.ASSOZIATION)),
//				this::erstelleVererbungZeile, null);
		
//		StackPane container = new StackPane(assoziationAnzeige, vererbungAnzeige);
		StackPane container = new StackPane(assoziationAnzeige);
		container.setPadding(new Insets(0, 20, 10, 20));
		container.setMaxWidth(Region.USE_PREF_SIZE);
		container.setAlignment(Pos.TOP_CENTER);
		BorderPane.setAlignment(container, Pos.TOP_CENTER);
		wurzel.setCenter(container);
		
		ueberwacheSelektion(this.assoziation, assoziationAnzeige);
//		ueberwacheSelektion(this.vererbung, vererbungAnzeige);
	}
	
	private void ueberwacheSelektion(ToggleButton button, Node anzeige) {
		anzeige.setVisible(button.isSelected());
		anzeige.visibleProperty().bind(button.selectedProperty());
	}
	
	private <T> Pane erzeugeTabellenAnzeige(String[] labelBezeichnungen, ObservableList<T> inhalt,
			List<TableColumn<T, ?>> spalten, EventHandler<ActionEvent> neuAktion) {
//		GridPane tabelle = new GridPane();
//		fuelleTabelle(tabelle, labelBezeichnungen, inhalt, erzeugeZeile);
//		tabelle.setHgap(5);
//		tabelle.setVgap(10);
		
		TableView<T> tabelle = new TableView<>();
		tabelle.setItems(inhalt);
		tabelle.getColumns().setAll(spalten);
		tabelle.setEditable(true);
//		
//		tabelle.getItems().setAll(inhalt);
//		System.out.println(">>>");
//		System.out.println(Arrays.toString(inhalt.toArray()));
//		System.out.println("<<<");
//		Bindings.bindContent(tabelle.getItems(), inhalt);
//		tabelle.setPrefHeight(Region.USE_PREF_SIZE);
		
		BorderPane ansicht = new BorderPane();
		ansicht.setMaxWidth(Region.USE_PREF_SIZE);
//		var scrollContainer = new ScrollPane(tabelle);
//		scrollContainer.getStyleClass().add("edge-to-edge");
//		ansicht.setCenter(scrollContainer);
		
		ansicht.setCenter(new StackPane(tabelle));
		
		if (neuAktion != null) {
			Button neu = new Button();
			NodeUtil.fuegeIconHinzu(neu, Typicons.PLUS, 20, "neu-button-font-icon");
			neu.setOnAction(neuAktion);
			
			HBox tabellenButtons = new HBox(neu);
			tabellenButtons.setSpacing(5);
			tabellenButtons.setAlignment(Pos.CENTER_LEFT);
			tabellenButtons.setPadding(new Insets(5, 0, 0, 0));
			ansicht.setBottom(tabellenButtons);
		}
		
//		Platform.runLater(() -> {
//			var vBar = ((ScrollPaneSkin) scrollContainer.getSkin()).getVerticalScrollBar();
//			scrollContainer.prefWidthProperty().bind(tabelle.widthProperty()
//					.add(new When(vBar.visibleProperty()).then(vBar.widthProperty()).otherwise(0)));
//			loeseBindungen.add(scrollContainer.prefWidthProperty()::unbind);
//		});
//		
//		NodeUtil.beobachteSchwach(tabelle, tabelle.widthProperty(),
//				() -> Platform.runLater(scrollContainer::requestLayout));
		
		return ansicht;
	}
	
	
	private List<TableColumn<UMLVerbindung, ?>> erstelleAssoziationZeile() {
		TableColumn<UMLVerbindung, String> spalte1 = new TableColumn<>("Start");
		spalte1.setCellValueFactory(data -> data.getValue().verbindungsStartProperty());
		spalte1.setCellFactory(sp -> new TableCell<UMLVerbindung, String>() {
			private ComboBox<String> start = new SearchableComboBox<>();
			
			{
				start.getItems().addAll(vorhandeneElementNamen);
				this.setGraphic(start);
				this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			}
			
			private void setzeValidierung() {
				Platform.runLater(() ->{
					eingabeValidierung.registerValidator(start, Validator.combine(
							Validator.createPredicateValidator(
									c -> getItem() == null || (getItem() != null
											&& !getItem().isBlank()),
									sprache.getText("klasseLeerValidierung",
											"Es muss eine Klasse oder ein Interface gew%chlt werden".formatted(Umlaute.ae)))));
//							Validator.createPredicateValidator(
//									tf -> getItem() == null || (!Objects.equals(getItem(), getItem().getVerbindungsEnde())),
//									sprache.getText("klassenGleichValidierung",
//											"Es m%cssen verschiedene Klassen/Interfaces gew%chlt werden".formatted(Umlaute.ue,
//													Umlaute.ae)))));
					setzePlatzhalter(start);
				});
			}
			
			@Override
			protected void updateItem(String verbindung, boolean empty) {
				NodeUtil.entferneSchwacheBeobachtung(start);
				System.out.println(verbindung);
				
				if (empty || verbindung == null) {
					start.getSelectionModel().clearSelection();
					eingabeValidierung.registerValidator(start, Validator.createPredicateValidator(x -> true, ""));
					this.setContentDisplay(ContentDisplay.TEXT_ONLY);
					this.setVisible(false);
				} else {
					start.getSelectionModel().select(verbindung);
//					NodeUtil.beobachteSchwach(start, start.getSelectionModel().selectedItemProperty(),
//							verbindung::setVerbindungsStart);
					NodeUtil.beobachteSchwach(start, start.getSelectionModel().selectedItemProperty(),
							() -> eingabeValidierung.revalidate());
					setzeValidierung();
					this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
					this.setVisible(true);
				}
				
				eingabeValidierung.revalidate();
			}
			
		});
		
		TableColumn<UMLVerbindung, String> spalte2 = new TableColumn<>("Ende");
		spalte2.setCellValueFactory(data -> data.getValue().verbindungsEndeProperty());
		spalte2.setCellFactory(sp -> new TableCell<UMLVerbindung, String>() {
			private ComboBox<String> start = new SearchableComboBox<>();
			
			{
				start.getItems().addAll(vorhandeneElementNamen);
				this.setGraphic(start);
				this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			}
			
			private void setzeValidierung() {
				Platform.runLater(() ->{
					eingabeValidierung.registerValidator(start, Validator.combine(
							Validator.createPredicateValidator(
									c -> getItem() == null || (getItem() != null
											&& !getItem().isBlank()),
									sprache.getText("klasseLeerValidierung",
											"Es muss eine Klasse oder ein Interface gew%chlt werden".formatted(Umlaute.ae)))));
//							Validator.createPredicateValidator(
//									tf -> getItem() == null || (!Objects.equals(getItem(), getItem().getVerbindungsEnde())),
//									sprache.getText("klassenGleichValidierung",
//											"Es m%cssen verschiedene Klassen/Interfaces gew%chlt werden".formatted(Umlaute.ue,
//													Umlaute.ae)))));
					setzePlatzhalter(start);
				});
			}
			
			@Override
			protected void updateItem(String verbindung, boolean empty) {
				NodeUtil.entferneSchwacheBeobachtung(start);
				
				if (empty || verbindung == null) {
					start.getSelectionModel().clearSelection();
					eingabeValidierung.registerValidator(start, Validator.createPredicateValidator(x -> true, ""));
					this.setContentDisplay(ContentDisplay.TEXT_ONLY);
					this.setVisible(false);
				} else {
					start.getSelectionModel().select(verbindung);
//					NodeUtil.beobachteSchwach(start, start.getSelectionModel().selectedItemProperty(),
//							verbindung::setVerbindungsStart);
					NodeUtil.beobachteSchwach(start, start.getSelectionModel().selectedItemProperty(),
							() -> eingabeValidierung.revalidate());
					setzeValidierung();
					this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
					this.setVisible(true);
				}
				
				eingabeValidierung.revalidate();
			}
			
		});
		
		spalte1.setCellFactory(col -> new ComboBoxTableCell<UMLVerbindung, String>(vorhandeneElementNamen.toArray(new String[vorhandeneElementNamen.size()])));
		spalte2.setCellFactory(col -> new ComboBoxTableCell<UMLVerbindung, String>(vorhandeneElementNamen.toArray(new String[vorhandeneElementNamen.size()])));
		
		List<TableColumn<UMLVerbindung, ?>> spalten = new ArrayList<>();
		spalten.add(spalte1);
		spalten.add(spalte2);
		return spalten;
	}
	
	
	private void initialisiereButtons() {
		ButtonType[] buttons = { new ButtonType(sprache.getText("APPLY", "Anwenden"), ButtonData.FINISH),
			new ButtonType(sprache.getText("CANCEL_CLOSE", "Abbrechen"), ButtonData.BACK_PREVIOUS) };
		this.getDialogPane().getButtonTypes().addAll(buttons);
		this.getDialogPane().lookupButton(buttons[0]).disableProperty().bind(eingabeValidierung.invalidProperty());
		loeseBindungen.add(this.getDialogPane().lookupButton(buttons[0]).disableProperty()::unbind);
	}
	
	private void setzePlatzhalter(ComboBox<String> eingabefeld) {
		ChangeListener<ValidationResult> beobachter = (p, alt, neu) -> {
			var fehler = eingabeValidierung.getHighestMessage(eingabefeld);
			if (fehler.isPresent()) {
				eingabefeld.setPromptText(fehler.get().getText());
				if (!eingabefeld.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
					eingabefeld.getStyleClass().add(CSS_EINGABE_FEHLER);
				}
				eingabefeld.setTooltip(new Tooltip(fehler.get().getText()));
			} else {
				eingabefeld.setPromptText(null);
				if (eingabefeld.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
					eingabefeld.getStyleClass().remove(CSS_EINGABE_FEHLER);
				}
				eingabefeld.setTooltip(null);
			}
		};
		eingabeValidierung.validationResultProperty().addListener(beobachter);
		validierungsBeobachter.add(beobachter);
	}
	
}