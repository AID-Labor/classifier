/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.kordamp.ikonli.typicons.Typicons;

import com.dlsc.gemsfx.EnhancedLabel;

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
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;


public class VerbindungBearbeitenListe extends BorderPane implements AutoCloseable {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public static final String CSS_EINGABE_FEHLER = "eingabefehler";
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public enum Typ {
		ASSOZIATION, VERERBUNG;
	}
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final List<Runnable> loeseBindungen;
	private final ValidationSupport eingabeValidierung;
	private final List<ChangeListener<ValidationResult>> validierungsBeobachter;
	private final Sprache sprache;
	private final Sprache spracheLabels;
	private final UMLProjekt umlProjekt;
	private final List<String> vorhandeneElementNamen;
	private final BooleanProperty startBearbeitbar;
	private final FilteredList<UMLVerbindung> verbindungen;
	private Supplier<UMLVerbindung> verbindungErzeuger;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public VerbindungBearbeitenListe(String[] labelBezeichnungen, Typ typ, ValidationSupport eingabeValidierung,
			Sprache sprache, UMLProjekt umlProjekt, boolean startBearbeitbar) {
		this.loeseBindungen = new LinkedList<>();
		this.validierungsBeobachter = new LinkedList<>();
		this.eingabeValidierung = eingabeValidierung;
		this.sprache = new Sprache();
		this.spracheLabels = sprache;
		this.umlProjekt = umlProjekt;
		this.startBearbeitbar = new SimpleBooleanProperty(startBearbeitbar);
		
		boolean spracheGesetzt = SprachUtil.setUpSprache(this.sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
				"VerbindungenBearbeitenListe");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		List<UMLKlassifizierer> klassen = umlProjekt.getDiagrammElemente().stream()
				.filter(UMLKlassifizierer.class::isInstance).map(UMLKlassifizierer.class::cast).toList();
		this.vorhandeneElementNamen = klassen.parallelStream().map(UMLDiagrammElement::getName).toList();
		
		BiFunction<UMLVerbindung, KontrollElemente<UMLVerbindung>, Node[]> erzeugeZeile = null;
		EventHandler<ActionEvent> neuAktion = null;
		Predicate<UMLVerbindung> filter = null;
		verbindungErzeuger = () -> new UMLVerbindung(UMLVerbindungstyp.ASSOZIATION, "", "");
		
		switch (typ) {
			case ASSOZIATION -> {
				erzeugeZeile = this::erstelleAssoziationZeile;
				neuAktion = event -> {
					var verbindung = verbindungErzeuger.get();
					verbindung.setzeAutomatisch(false);
					this.umlProjekt.getAssoziationen().add(verbindung);
				};
				filter = v -> v.getTyp().equals(UMLVerbindungstyp.ASSOZIATION);
				verbindungen = umlProjekt.getAssoziationen().filtered(filter);
			}
			case VERERBUNG -> {
				erzeugeZeile = this::erstelleVererbungZeile;
				filter = v -> !v.getTyp().equals(UMLVerbindungstyp.ASSOZIATION);
				verbindungen = umlProjekt.getVererbungen().filtered(filter);
			}
			default -> verbindungen = null;
		}
		
		GridPane tabelle = new GridPane();
		fuelleTabelle(tabelle, labelBezeichnungen, verbindungen, erzeugeZeile);
		tabelle.setHgap(5);
		tabelle.setVgap(10);
		
		this.setMaxWidth(Region.USE_PREF_SIZE);
		var scrollContainer = new ScrollPane(tabelle);
		scrollContainer.getStyleClass().add("edge-to-edge");
		this.setCenter(scrollContainer);
		
		if (neuAktion != null) {
			Button neu = new Button();
			NodeUtil.fuegeIconHinzu(neu, Typicons.PLUS, 20, "neu-button-font-icon");
			neu.setOnAction(neuAktion);
			
			HBox tabellenButtons = new HBox(neu);
			tabellenButtons.setSpacing(5);
			tabellenButtons.setAlignment(Pos.CENTER_LEFT);
			tabellenButtons.setPadding(new Insets(5, 0, 0, 0));
			this.setBottom(tabellenButtons);
		}
		
		ChangeListener<Skin<?>> skinUeberwacher = (o, alt, skin) -> {
			if (skin != null) {
				Platform.runLater(() -> {
					var vBar = ((ScrollPaneSkin) scrollContainer.getSkin()).getVerticalScrollBar();
					scrollContainer.prefWidthProperty().bind(tabelle.widthProperty()
							.add(new When(vBar.visibleProperty()).then(vBar.widthProperty()).otherwise(0)));
					loeseBindungen.add(scrollContainer.prefWidthProperty()::unbind);
				});
			}
		};
		scrollContainer.skinProperty().addListener(skinUeberwacher);
		loeseBindungen.add(() -> scrollContainer.skinProperty().removeListener(skinUeberwacher));
		
		NodeUtil.beobachteSchwach(tabelle, tabelle.widthProperty(),
				() -> Platform.runLater(scrollContainer::requestLayout));
		
	}
	
	@Override
	public void close() {
		for (var beobachter : validierungsBeobachter) {
			eingabeValidierung.validationResultProperty().removeListener(beobachter);
		}
		
		for (Node n : this.getChildren()) {
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
	}
	
	private void entferneAlleBeobachter(Node n) {
		NodeUtil.entferneSchwacheBeobachtung(n);
		n.getProperties().remove(this);
		if (n instanceof Parent p) {
			for (Node kind : p.getChildrenUnmodifiable()) {
				entferneAlleBeobachter(kind);
			}
		}
		if (n instanceof AutoCloseable ac) {
			try {
				ac.close();
			} catch (Exception e1) {
				//
			}
		}
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public void setVerbindungenFilter(Predicate<UMLVerbindung> filter) {
		verbindungen.predicateProperty().set(filter);
	}
	
	public void setVerbindungErzeuger(Supplier<UMLVerbindung> verbindungErzeuger) {
		this.verbindungErzeuger = verbindungErzeuger;
	}
	
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
	
	private void fuelleTabelle(GridPane tabelle, String[] labelBezeichnungen, ObservableList<UMLVerbindung> inhalt,
			BiFunction<UMLVerbindung, KontrollElemente<UMLVerbindung>, Node[]> erzeugeZeile) {
		for (String bezeichnung : labelBezeichnungen) {
			Label spaltenUeberschrift = SprachUtil.bindText(new EnhancedLabel(), spracheLabels,
					bezeichnung.toLowerCase(), bezeichnung);
			tabelle.addRow(0, spaltenUeberschrift);
		}
		
		fuelleListenInhalt(tabelle, inhalt, erzeugeZeile);
		
		ListChangeListener<? super UMLVerbindung> beobachter = aenderung -> {
			if (aenderung.next()) {
				fuelleListenInhalt(tabelle, inhalt, erzeugeZeile);
			}
		};
		loeseBindungen.add(() -> inhalt.removeListener(beobachter));
		inhalt.addListener(beobachter);
	}
	
	private void fuelleListenInhalt(GridPane tabelle, ObservableList<UMLVerbindung> inhalt,
			BiFunction<UMLVerbindung, KontrollElemente<UMLVerbindung>, Node[]> erzeugeZeile) {
		var zuEntfernen = tabelle.getChildren().stream().filter(kind -> GridPane.getRowIndex(kind) > 0).toList();
		for (var kind : zuEntfernen) {
			if (kind instanceof Control c) {
				eingabeValidierung.registerValidator(c, false,
						(control, wert) -> ValidationResult.fromInfoIf(control, "ungenutzt", false));
				NodeUtil.entferneSchwacheBeobachtung(kind);
			}
		}
		tabelle.getChildren().removeIf(kind -> GridPane.getRowIndex(kind) > 0);
		int zeile = 1;
		for (UMLVerbindung element : inhalt) {
			var zeilenInhalt = erzeugeZeile.apply(element, new KontrollElemente<>(inhalt, element, zeile - 1));
			tabelle.addRow(zeile, zeilenInhalt);
			zeile++;
		}
	}
	
	private Node[] erstelleAssoziationZeile(UMLVerbindung verbindung,
			KontrollElemente<UMLVerbindung> kontrollelemente) {
		ComboBox<String> start = new SearchableComboBox<>();
		start.getItems().addAll(this.vorhandeneElementNamen);
		start.getSelectionModel().select(verbindung.getVerbindungsStart());
		start.setMaxWidth(Double.MAX_VALUE);
		
		if (startBearbeitbar.get()) {
			NodeUtil.beobachteSchwach(start, start.getSelectionModel().selectedItemProperty(),
					verbindung::setVerbindungsStart);
			NodeUtil.beobachteSchwach(start, start.getSelectionModel().selectedItemProperty(),
					() -> eingabeValidierung.revalidate());
		}
		
		TextField startFest = new TextField();
		startFest.setText(verbindung.getVerbindungsStart());
		startFest.setEditable(false);
		startFest.setPrefHeight(20);
		
		Label startHilfe = new Label();
		startHilfe.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		startHilfe.graphicProperty().bind(new When(startBearbeitbar).then((Node) start).otherwise(startFest));
		
		ComboBox<String> ende = new SearchableComboBox<>();
		ende.getItems().addAll(this.vorhandeneElementNamen);
		ende.getSelectionModel().select(verbindung.getVerbindungsEnde());
		NodeUtil.beobachteSchwach(ende, ende.getSelectionModel().selectedItemProperty(),
				verbindung::setVerbindungsEnde);
		NodeUtil.beobachteSchwach(ende, ende.getSelectionModel().selectedItemProperty(),
				() -> eingabeValidierung.revalidate());
		ende.setMaxWidth(Double.MAX_VALUE);
		
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
		
		NodeUtil.setzeHoehe(30, start);
		NodeUtil.setzeHoehe(30, startFest);
		NodeUtil.setzeHoehe(30, startHilfe);
		NodeUtil.setzeHoehe(30, ende);
		return new Node[] { startHilfe, ende, ausgeblendet, kontrollelemente.getLoeschen() };
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
		
		eingabeValidierung.registerValidator(ausgeblendet, Validator.createPredicateValidator(tf -> {
			if (ausgeblendet.isSelected()) {
				return true;
			}
			var startname = entfernePaketname(start.getText());
			var endname = entfernePaketname(ende.getText());
			return vorhandeneElementNamen.contains(startname) && vorhandeneElementNamen.contains(endname);
		}, sprache.getText("ausgeblendetValidierung",
				"Es m%cssen alle Klassen/Interfaces vorhanden sein, um die Verbindung einzublenden"
						.formatted(Umlaute.ue))));
		setzePlatzhalter(ausgeblendet);
		
		return new Node[] { start, ende, ausgeblendet };
	}
	
	private <T> void bindeBidirektional(Property<T> p1, Property<T> p2) {
		p1.bindBidirectional(p2);
		loeseBindungen.add(() -> p1.unbindBidirectional(p2));
	}
}