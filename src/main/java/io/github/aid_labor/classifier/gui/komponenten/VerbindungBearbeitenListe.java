/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Logger;

import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.kordamp.ikonli.typicons.Typicons;

import com.dlsc.gemsfx.FilterView;
import com.dlsc.gemsfx.FilterView.Filter;
import com.dlsc.gemsfx.FilterView.FilterGroup;
import com.tobiasdiez.easybind.EasyBind;

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
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


public class VerbindungBearbeitenListe extends BorderPane implements AutoCloseable {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public static final String CSS_EINGABE_FEHLER = "eingabefehler";
	
	private static final Logger log = Logger.getLogger(VerbindungBearbeitenListe.class.getName());
	
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
	private final UMLProjekt umlProjekt;
	private final BooleanProperty startBearbeitbar;
	private final ObservableList<UMLVerbindung> verbindungen;
	private final FilterView<UMLVerbindung> filterView;
	private final ObservableList<String> vorhandeneElementNamen;
	private final ObjectProperty<Supplier<UMLVerbindung>> verbindungErzeugerProperty;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public VerbindungBearbeitenListe(String[] spaltenNamen, Typ typ, ValidationSupport eingabeValidierung,
	        Sprache sprache, UMLProjekt umlProjekt, boolean startBearbeitbar) {
	    this(spaltenNamen, typ, eingabeValidierung, sprache, umlProjekt, startBearbeitbar, true);
	}
	@SuppressWarnings("unchecked")
    public VerbindungBearbeitenListe(String[] spaltenNamen, Typ typ, ValidationSupport eingabeValidierung,
			Sprache sprache, UMLProjekt umlProjekt, boolean startBearbeitbar, boolean showFilter) {
		this.loeseBindungen = new LinkedList<>();
		this.validierungsBeobachter = new LinkedList<>();
		this.eingabeValidierung = eingabeValidierung;
		this.sprache = new Sprache();
		this.umlProjekt = umlProjekt;
		this.startBearbeitbar = new SimpleBooleanProperty(startBearbeitbar);
		this.vorhandeneElementNamen = EasyBind.map(umlProjekt.getDiagrammElemente(), UMLDiagrammElement::getName);
		
		boolean spracheGesetzt = SprachUtil.setUpSprache(this.sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
				"VerbindungenBearbeitenListe");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		EventHandler<ActionEvent> neuAktion = null;
		Predicate<UMLVerbindung> filter = null;
		verbindungErzeugerProperty = new SimpleObjectProperty<>(() -> new UMLVerbindung(UMLVerbindungstyp.ASSOZIATION, "", ""));
		Function<String[], Collection<TableColumn<UMLVerbindung, ?>>> spaltenErzeuger = null;
		
		ObservableList<UMLVerbindung> sourceListe = null;
		
		switch (typ) {
			case ASSOZIATION -> {
				neuAktion = event -> {
					var verbindung = verbindungErzeugerProperty.get().get();
					verbindung.setzeAutomatisch(false);
					this.umlProjekt.getAssoziationen().add(verbindung);
				};
				filter = v -> v.getTyp().equals(UMLVerbindungstyp.ASSOZIATION);
				spaltenErzeuger = this::getAssoziationSpalten;
				sourceListe = umlProjekt.getAssoziationen();
			}
			case VERERBUNG -> {
				filter = v -> !v.getTyp().equals(UMLVerbindungstyp.ASSOZIATION);
				spaltenErzeuger = this::getVererbungSpalten;
				sourceListe = umlProjekt.getVererbungen();
			}
		}
		
		if (showFilter) {
		    filterView = new FilterView<>();
		    filterView.setShowHeader(false);
		    
		    FilterGroup<UMLVerbindung> startFilter = new FilterGroup<>(sprache.getText(spaltenNamen[0].toLowerCase(), spaltenNamen[0]));
		    
		    for (StringProperty name: umlProjekt.getElementNamen()) {
		        startFilter.getFilters().add(new Filter<UMLVerbindung>(name.get()) {
		            
		            {
		                nameProperty().bind(name);
		            }
		            
		            @Override
		            public boolean test(UMLVerbindung t) {
		                return UMLKlassifizierer.nameOhnePaket(t.getVerbindungsStart()).equals(name.get()) 
		                        || UMLKlassifizierer.nameOhnePaket(t.getVerbindungsEnde()).equals(name.get());
		            }
		        });
		    }
		    
		    FilterGroup<UMLVerbindung> ausgeblendetFilter = new FilterGroup<>(sprache.getText(spaltenNamen[2].toLowerCase(), spaltenNamen[2]));
		    
		    ausgeblendetFilter.getFilters().add(new Filter<UMLVerbindung>(sprache.getText("ausgeblendet", "Ausgeblendet")) {
		        @Override
		        public boolean test(UMLVerbindung t) {
		            return t.istAusgebelendet();
		        }
		    });
		    ausgeblendetFilter.getFilters().add(new Filter<UMLVerbindung>(sprache.getText("nichtAusgeblendet", "Nicht ausgeblendet")) {
		        @Override
		        public boolean test(UMLVerbindung t) {
		            return !t.istAusgebelendet();
		        }
		    });
		    
		    filterView.getFilterGroups().setAll(startFilter, ausgeblendetFilter);
		    filterView.setAdditionalFilterPredicate(filter);
		    filterView.setItems(sourceListe);
		    filterView.setPadding(new Insets(0, 0, 10, 0));
		    
		    verbindungen = new SortedList<>(filterView.getFilteredItems());
		    
		    this.setTop(filterView);
		} else {
		    verbindungen = sourceListe.filtered(filter);
		    filterView = null;
		}
		
		TableView<UMLVerbindung> tabelle = erzeugeTabellenAnzeige(verbindungen, spaltenErzeuger.apply(spaltenNamen), neuAktion);
		
		if (verbindungen instanceof SortedList<UMLVerbindung> sl) {
		    sl.comparatorProperty().bind(tabelle.comparatorProperty());
		}
		
		this.setCenter(tabelle);
		
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
		if (verbindungen instanceof FilteredList<UMLVerbindung> fl) {
		    fl.predicateProperty().set(filter);
		} else if (filterView != null) {
		    filterView.setAdditionalFilterPredicate(filter);
		} else {
		    log.warning("Filter konnten nicht angewendet werden");
		}
	}
	
	public void setVerbindungErzeuger(Supplier<UMLVerbindung> verbindungErzeuger) {
		this.verbindungErzeugerProperty.set(verbindungErzeuger);
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
	
	
	private <T> TableView<T> erzeugeTabellenAnzeige(ObservableList<T> inhalt, Collection<TableColumn<T, ?>> spalten,
            EventHandler<ActionEvent> neuAktion) {
        TableView<T> tabelle = new TableView<>(inhalt);
        tabelle.getColumns().addAll(spalten);
        tabelle.setEditable(true);

        Button neu = new Button();
        NodeUtil.fuegeIconHinzu(neu, Typicons.PLUS, 20, "neu-button-font-icon");
        neu.setOnAction(neuAktion);

        return tabelle;
    }
	
	private Collection<TableColumn<UMLVerbindung, ?>> getAssoziationSpalten(String[] spaltenNamen) {
        TableColumn<UMLVerbindung, String> startSpalte = new TableColumn<>();
        SprachUtil.bindText(startSpalte.textProperty(), sprache, spaltenNamen[0].toLowerCase(), spaltenNamen[0]);
        startSpalte.setCellValueFactory(param -> param.getValue().verbindungsStartProperty());
        var verbindungWahlCellBuilder = CustomNodeTableCell.builder(UMLVerbindung.class, String.class, (Class<SearchableComboBox<String>>)(Class<?>)SearchableComboBox.class)
                .nodeFactory(() -> {
                    SearchableComboBox<String> verbindungWahl = new SearchableComboBox<>();
                    verbindungWahl.getItems().setAll(vorhandeneElementNamen);
                    return verbindungWahl;
                })
                .getProperty(combo -> combo.valueProperty())
                .getValue(combo -> combo.getSelectionModel().getSelectedItem())
                .setValue((combo, v) -> combo.getSelectionModel().select(v))
                .disableBinding(UMLVerbindung::automatischProperty);
        if (startBearbeitbar.get()) {
            startSpalte.setEditable(true);
            startSpalte.setCellFactory(spalte -> verbindungWahlCellBuilder.build());
        }
        
        TableColumn<UMLVerbindung, String> endSpalte = new TableColumn<>();
        SprachUtil.bindText(endSpalte.textProperty(), sprache, spaltenNamen[1].toLowerCase(), spaltenNamen[1]);
        endSpalte.setCellValueFactory(param -> param.getValue().verbindungsEndeProperty());
        endSpalte.setEditable(true);
        endSpalte.setCellFactory(spalte -> verbindungWahlCellBuilder.build());
        
        TableColumn<UMLVerbindung, Boolean> ausgeblendetSpalte = new TableColumn<>();
        SprachUtil.bindText(ausgeblendetSpalte.textProperty(), sprache, spaltenNamen[2].toLowerCase(), spaltenNamen[2]);
        ausgeblendetSpalte.setCellValueFactory(param -> param.getValue().ausgebelendetProperty());
        ausgeblendetSpalte.setEditable(true);
        ausgeblendetSpalte.setCellFactory(col -> new CheckBoxTableCell<>());
        
        TableColumn<UMLVerbindung, UMLVerbindung> kontrollSpalte = new TableColumn<>();
        kontrollSpalte.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue()));
        kontrollSpalte.setCellFactory(col -> new ListControlsTableCell<>(true, false, umlProjekt.getAssoziationen()));
        kontrollSpalte.setResizable(false);
        
        return List.of(startSpalte, endSpalte, ausgeblendetSpalte, kontrollSpalte);
    }
	
	private Collection<TableColumn<UMLVerbindung, ?>> getVererbungSpalten(String[] spaltenNamen) {
        TableColumn<UMLVerbindung, String> startSpalte = new TableColumn<>();
        SprachUtil.bindText(startSpalte.textProperty(), sprache, spaltenNamen[0].toLowerCase(), spaltenNamen[0]);
        startSpalte.setCellValueFactory(param -> param.getValue().verbindungsStartProperty());
        
        TableColumn<UMLVerbindung, String> endSpalte = new TableColumn<>();
        SprachUtil.bindText(endSpalte.textProperty(), sprache, spaltenNamen[1].toLowerCase(), spaltenNamen[1]);
        endSpalte.setCellValueFactory(param -> param.getValue().verbindungsEndeProperty());
        endSpalte.setEditable(true);
        
        TableColumn<UMLVerbindung, Boolean> ausgeblendetSpalte = new TableColumn<>();
        SprachUtil.bindText(ausgeblendetSpalte.textProperty(), sprache, spaltenNamen[2].toLowerCase(), spaltenNamen[2]);
        ausgeblendetSpalte.setCellValueFactory(param -> param.getValue().ausgebelendetProperty());
        ausgeblendetSpalte.setEditable(true);
        ausgeblendetSpalte.setCellFactory(col -> new CheckBoxTableCell<>());
        
        return List.of(startSpalte, endSpalte, ausgeblendetSpalte);
    }
	
	private Node[] erstelleAssoziationZeile(UMLVerbindung verbindung,
			KontrollElemente<UMLVerbindung> kontrollelemente) {
		ComboBox<String> start = new SearchableComboBox<>();
		start.getItems().addAll(vorhandeneElementNamen);
		Bindings.bindContent(start.getItems(), vorhandeneElementNamen);
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
		startFest.textProperty().bind(verbindung.verbindungsStartProperty());
		startFest.setEditable(false);
		startFest.setPrefHeight(20);
		
		Label startHilfe = new Label();
		startHilfe.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		startHilfe.graphicProperty().bind(new When(startBearbeitbar).then((Node) start).otherwise(startFest));
		
		ComboBox<String> ende = new SearchableComboBox<>();
		ende.getItems().addAll(vorhandeneElementNamen);
		Bindings.bindContent(ende.getItems(), vorhandeneElementNamen);
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
		start.textProperty().bind(verbindung.verbindungsStartProperty());
		start.setEditable(false);
		
		TextField ende = new TextField();
		ende.setText(verbindung.getVerbindungsEnde());
		ende.textProperty().bind(verbindung.verbindungsEndeProperty());
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