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
import java.util.Objects;
import java.util.function.BiFunction;

import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellBase;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.kordamp.ikonli.typicons.Typicons;

import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute;
import io.github.aid_labor.classifier.gui.komponenten.KontrollElemente;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindung;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindungstyp;
import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;


public class UMLVerbindungenBearbeitenDialog3 extends Alert {
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
	
	public UMLVerbindungenBearbeitenDialog3(UMLProjekt projekt, boolean starteMitAssoziation) {
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
				this::erstelleAssoziationZeile, event -> {
					var verbindung = new UMLVerbindung(UMLVerbindungstyp.UNIDIREKTIONALE_ASSOZIATION, "", "");
					verbindung.setzeAutomatisch(false);
					this.umlProjekt.getAssoziationen().add(verbindung);
				});
		var vererbungAnzeige = erzeugeTabellenAnzeige(
				new String[] { "Klasse/Interface", "Superklasse/Interface", "Ausgeblendet" },
				this.umlProjekt.getVererbungen(),
				this::erstelleVererbungZeile, null);
		
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
	
	private <T> Pane erzeugeTabellenAnzeige(String[] labelBezeichnungen, ObservableList<T> inhalt,
			BiFunction<T, KontrollElemente<T>, Node[]> erzeugeZeile, EventHandler<ActionEvent> neuAktion) {
//		GridPane tabelle = new GridPane();
//		fuelleTabelle(tabelle, labelBezeichnungen, inhalt, erzeugeZeile);
//		tabelle.setHgap(5);
//		tabelle.setVgap(10);
		
		SpreadsheetView tabelle = new SpreadsheetView();
		fuelleTabelle(tabelle, labelBezeichnungen, inhalt, erzeugeZeile);
		
		BorderPane ansicht = new BorderPane();
		ansicht.setMaxWidth(Region.USE_PREF_SIZE);
		var scrollContainer = new ScrollPane(tabelle);
		scrollContainer.getStyleClass().add("edge-to-edge");
		ansicht.setCenter(scrollContainer);
		
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
		
		Platform.runLater(() -> {
			var vBar = ((ScrollPaneSkin) scrollContainer.getSkin()).getVerticalScrollBar();
			scrollContainer.prefWidthProperty().bind(tabelle.widthProperty()
					.add(new When(vBar.visibleProperty()).then(vBar.widthProperty()).otherwise(0)));
			loeseBindungen.add(scrollContainer.prefWidthProperty()::unbind);
		});
		
		NodeUtil.beobachteSchwach(tabelle, tabelle.widthProperty(),
				() -> Platform.runLater(scrollContainer::requestLayout));
		
		return ansicht;
	}
	
	private <T> void fuelleTabelle(SpreadsheetView tabelle, String[] labelBezeichnungen, ObservableList<T> inhalt,
			BiFunction<T, KontrollElemente<T>, Node[]> erzeugeZeile) {
		for (String bezeichnung : labelBezeichnungen) {
			String spaltenUeberschrift = sprache.getText(bezeichnung.toLowerCase(), bezeichnung);
			tabelle.getGrid().getColumnHeaders().add(spaltenUeberschrift);
		}
		tabelle.setShowRowHeader(false);
		tabelle.setShowColumnHeader(true);
		
		fuelleListenInhalt(tabelle, inhalt, erzeugeZeile);
		
		ListChangeListener<? super T> beobachter = aenderung -> {
			if (aenderung.next()) {
				fuelleListenInhalt(tabelle, inhalt, erzeugeZeile);
			}
		};
		loeseBindungen.add(() -> inhalt.removeListener(beobachter));
		inhalt.addListener(beobachter);
	}
	
	private <T> void fuelleListenInhalt(SpreadsheetView tabelle, ObservableList<T> inhalt,
			BiFunction<T, KontrollElemente<T>, Node[]> erzeugeZeile) {
		var zuEntfernen = tabelle.getGrid().getRows().stream().flatMap(r -> r.stream()).map(zelle -> zelle.getGraphic())
				.toList();
		for (var kind : zuEntfernen) {
			if (kind instanceof Control c) {
				eingabeValidierung.registerValidator(c, false,
						(control, wert) -> ValidationResult.fromInfoIf(control, "ungenutzt", false));
				NodeUtil.entferneSchwacheBeobachtung(kind);
			}
		}
		tabelle.getGrid().getRows().clear();
		int zeilenIndex = 1;
		for (T element : inhalt) {
			var zeilenInhalt = erzeugeZeile.apply(element, new KontrollElemente<>(inhalt, element, zeilenIndex - 1));
			ObservableList<SpreadsheetCell> zeile = FXCollections.observableList(new ArrayList<>(zeilenInhalt.length));
			int spaltenIndex = 0;
			for (Node zelle : zeilenInhalt) {
				var tabellenZelle = new SpreadsheetCellBase(zeilenIndex, spaltenIndex, 1, 1);
				tabellenZelle.setGraphic(zelle);
				zeile.add(tabellenZelle);
				spaltenIndex++;
			}
			while (zeile.size() < tabelle.getColumns().size()) {
				zeile.add(new SpreadsheetCellBase(zeilenIndex, spaltenIndex++, 1, 1));
			}
			tabelle.getGrid().getRows().add(zeile);
			zeilenIndex++;
		}
	}
	
	private Node[] erstelleAssoziationZeile(UMLVerbindung verbindung,
			KontrollElemente<UMLVerbindung> kontrollelemente) {
		ComboBox<String> start = new SearchableComboBox<>();
		start.getItems().addAll(this.vorhandeneElementNamen);
		start.getSelectionModel().select(verbindung.getVerbindungsStart());
		NodeUtil.beobachteSchwach(start, start.getSelectionModel().selectedItemProperty(),
				verbindung::setVerbindungsStart);
		NodeUtil.beobachteSchwach(start, start.getSelectionModel().selectedItemProperty(),
				() -> eingabeValidierung.revalidate());
		
		ComboBox<String> ende = new SearchableComboBox<>();
		ende.getItems().addAll(this.vorhandeneElementNamen);
		ende.getSelectionModel().select(verbindung.getVerbindungsEnde());
		NodeUtil.beobachteSchwach(ende, ende.getSelectionModel().selectedItemProperty(),
				verbindung::setVerbindungsEnde);
		NodeUtil.beobachteSchwach(ende, ende.getSelectionModel().selectedItemProperty(),
				() -> eingabeValidierung.revalidate());
		
		CheckBox ausgeblendet = new CheckBox();
		bindeBidirektional(ausgeblendet.selectedProperty(), verbindung.ausgebelendetProperty());
		GridPane.setHalignment(ausgeblendet, HPos.CENTER);
		NodeUtil.beobachteSchwach(ausgeblendet, ausgeblendet.selectedProperty(), () -> eingabeValidierung.revalidate());
		
		validiereAssoziation(start, ende, ausgeblendet, verbindung);
		
		if (verbindung.getVerbindungsStart().isEmpty()) {
			Platform.runLater(start::requestFocus);
		} else if (verbindung.getVerbindungsEnde().isEmpty()) {
			Platform.runLater(ende::requestFocus);
		}
		
		start.disableProperty().bind(verbindung.automatischProperty());
		ende.disableProperty().bind(verbindung.automatischProperty());
		kontrollelemente.getLoeschen().disableProperty().bind(verbindung.automatischProperty());
		kontrollelemente.getLoeschen()
				.setOnMouseClicked(e -> umlProjekt.getAssoziationen().removeIf(v -> v.getId() == verbindung.getId()));
		
		return new Node[] { start, ende, ausgeblendet, kontrollelemente.getLoeschen() };
	}
	
	private void validiereAssoziation(ComboBox<String> start, ComboBox<String> ende, CheckBox ausgeblendet,
			UMLVerbindung verbindung) {
		Platform.runLater(() -> {
			eingabeValidierung.registerValidator(start, Validator.combine(
					Validator.createPredicateValidator(
							c -> verbindung.getVerbindungsStart() != null
									&& !verbindung.getVerbindungsStart().isBlank(),
							sprache.getText("klasseLeerValidierung",
									"Es muss eine Klasse oder ein Interface gew%chlt werden".formatted(Umlaute.ae))),
					Validator.createPredicateValidator(
							tf -> !Objects.equals(verbindung.getVerbindungsStart(), verbindung.getVerbindungsEnde()),
							sprache.getText("klassenGleichValidierung",
									"Es m%cssen verschiedene Klassen/Interfaces gew%chlt werden".formatted(Umlaute.ue,
											Umlaute.ae)))));
			eingabeValidierung.registerValidator(ende, Validator.combine(
					Validator.createPredicateValidator(
							c -> verbindung.getVerbindungsEnde() != null && !verbindung.getVerbindungsEnde().isBlank(),
							sprache.getText("klasseLeerValidierung",
									"Es muss eine Klasse oder ein Interface gew%chlt werden".formatted(Umlaute.ae))),
					Validator.createPredicateValidator(
							tf -> !Objects.equals(verbindung.getVerbindungsStart(), verbindung.getVerbindungsEnde()),
							sprache.getText("klassenGleichValidierung",
									"Es m%cssen verschiedene Klassen/Interfaces gew%chlt werden".formatted(Umlaute.ue,
											Umlaute.ae)))));
			eingabeValidierung.registerValidator(ausgeblendet, Validator.createPredicateValidator(tf -> {
				if (ausgeblendet.isSelected()) {
					return true;
				}
				var startname = entfernePaketname(start.getSelectionModel().getSelectedItem());
				var endname = entfernePaketname(ende.getSelectionModel().getSelectedItem());
				return vorhandeneElementNamen.contains(startname) && vorhandeneElementNamen.contains(endname);
			}, sprache.getText("ausgeblendetValidierung",
					"Es m%cssen alle Klassen/Interfaces vorhanden sein, um die Verbindung einzublenden"
							.formatted(Umlaute.ue))));
			setzePlatzhalter(start);
			setzePlatzhalter(ende);
			setzePlatzhalter(ausgeblendet);
		});
	}
	
	private String entfernePaketname(String name) {
		if (name == null) {
			return "";
		}
		
		if (name.contains(":")) {
			return name.substring(name.indexOf(":") + 1);
		} else {
			return name;
		}
	}
	
	private Node[] erstelleVererbungZeile(UMLVerbindung verbindung, KontrollElemente<UMLVerbindung> kontrollelemente) {
		TextField start = new TextField();
		start.setText(verbindung.getVerbindungsStart());
		start.setEditable(false);
		
		TextField ende = new TextField();
		ende.setText(verbindung.getVerbindungsEnde());
		ende.setEditable(false);
		
		CheckBox ausgeblendet = new CheckBox();
		bindeBidirektional(ausgeblendet.selectedProperty(), verbindung.ausgebelendetProperty());
		GridPane.setHalignment(ausgeblendet, HPos.CENTER);
		
		return new Node[] { start, ende, ausgeblendet };
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
	
	private void setzePlatzhalter(CheckBox eingabefeld) {
		ChangeListener<ValidationResult> beobachter = (p, alt, neu) -> {
			var fehler = eingabeValidierung.getHighestMessage(eingabefeld);
			if (fehler.isPresent()) {
				if (!eingabefeld.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
					eingabefeld.getStyleClass().add(CSS_EINGABE_FEHLER);
				}
				eingabefeld.setTooltip(new Tooltip(fehler.get().getText()));
			} else {
				if (eingabefeld.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
					eingabefeld.getStyleClass().remove(CSS_EINGABE_FEHLER);
				}
				eingabefeld.setTooltip(null);
			}
		};
		eingabeValidierung.validationResultProperty().addListener(beobachter);
		validierungsBeobachter.add(beobachter);
	}
	
	private <T> void bindeBidirektional(Property<T> p1, Property<T> p2) {
		p1.bindBidirectional(p2);
		loeseBindungen.add(() -> p1.unbindBidirectional(p2));
	}
}