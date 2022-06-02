/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import org.controlsfx.control.SegmentedButton;
import org.kordamp.ikonli.typicons.Typicons;

import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.eigenschaften.Attribut;
import io.github.aid_labor.classifier.uml.eigenschaften.Datentyp;
import io.github.aid_labor.classifier.uml.eigenschaften.Modifizierer;
import io.github.aid_labor.classifier.uml.eigenschaften.ProgrammierEigenschaften;
import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


public class UMLKlassifiziererBearbeitenDialog extends Alert {
//	private static final Logger log = Logger
//			.getLogger(UMLKlassifiziererBearbeitenDialog.class.getName());

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
	
	private final UMLKlassifizierer klassifizierer;
	private final Sprache sprache;
	private final SegmentedButton buttonBar;
	private final ToggleButton allgemein;
	private final ToggleButton attribute;
	private final ToggleButton methoden;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLKlassifiziererBearbeitenDialog(UMLKlassifizierer klassifizierer) {
		super(AlertType.NONE);
		this.klassifizierer = klassifizierer;
		this.sprache = new Sprache();
		
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache,
				Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
				"UMLKlassifiziererBearbeitenDialog");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		this.allgemein = new ToggleButton(sprache.getText("allgemein", "Allgemein"));
		this.attribute = new ToggleButton(sprache.getText("attribute", "Attribute"));
		this.methoden = new ToggleButton(sprache.getText("methoden", "Methoden"));
		this.buttonBar = new SegmentedButton(allgemein, attribute, methoden);
		this.buttonBar.getToggleGroup().selectToggle(allgemein);
		HBox buttonContainer = initialisiereButtonBar();
		
		BorderPane wurzel = new BorderPane();
		wurzel.setTop(buttonContainer);
		
		erzeugeInhalt(wurzel);
		
		getDialogPane().setContent(wurzel);
		initialisiereButtons();
		this.setResizable(true);
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
		allgemein.setSelected(true);
		for (var button : buttonBar.getButtons()) {
			button.setFocusTraversable(false);
		}
		buttonBar.getToggleGroup().selectedToggleProperty()
				.addListener((property, alteWahl, neueWahl) -> {
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
		var allgemeinAnzeige = erzeugeAllgemeinAnzeige();
		var attributeAnzeige = erzeugeAttributeAnzeige();
		
		StackPane container = new StackPane(allgemeinAnzeige);
		container.setPadding(new Insets(0, 20, 10, 20));
		wurzel.setCenter(container);
		
		ueberwacheSelektion(this.allgemein, allgemeinAnzeige, container);
		ueberwacheSelektion(this.attribute, attributeAnzeige, container);
		ueberwacheSelektion(this.methoden, new Label("M"), container);
	}
	
	private void ueberwacheSelektion(ToggleButton button, Node anzeige, StackPane container) {
		button.setOnAction(e -> {
			if (button.isSelected() && !container.getChildren().contains(anzeige)) {
				container.getChildren().clear();
				container.getChildren().add(anzeige);
			}
		});
	}
	
	private Node erzeugeAllgemeinAnzeige() {
		GridPane tabelle = new GridPane();
		
		String[] labelBezeichnungen = {
			"Typ", "Paket", "Name", "Sichtbarkeit", "Superklasse", "Interfaces"
		};
		
		for (int zeile = 0; zeile < labelBezeichnungen.length; zeile++) {
			String bezeichnung = labelBezeichnungen[zeile];
			var label = SprachUtil.bindText(new Label(), sprache, bezeichnung,
					bezeichnung + ":");
			tabelle.add(label, 0, zeile);
		}
		
		ComboBox<KlassifiziererTyp> typ = new ComboBox<>();
		typ.getItems().addAll(KlassifiziererTyp.values());
		typ.getSelectionModel().select(this.klassifizierer.getTyp());
		tabelle.add(typ, 1, 0);
		
		TextField paket = new TextField(this.klassifizierer.getPaket());
		tabelle.add(paket, 1, 1);
		
		TextField name = new TextField(this.klassifizierer.getName());
		tabelle.add(name, 1, 2);
		
		var sichtbarkeit = erzeugeSichtbarkeit();
		tabelle.add(sichtbarkeit, 1, 3);
		
		TextField superklasse = new TextField(this.klassifizierer.getSuperklasse());
		tabelle.add(superklasse, 1, 4);
		
		// TODO nutze GemsFX Tags Field
		TextField interfaces = new TextField(this.klassifizierer.getInterfaces());
		tabelle.add(interfaces, 1, 5);
		
		this.klassifizierer.getTypProperty()
				.bind(typ.getSelectionModel().selectedItemProperty());
		this.klassifizierer.getPaketProperty().bind(paket.textProperty());
		this.klassifizierer.nameProperty().bind(name.textProperty());
		this.klassifizierer.superklasseProperty().bind(superklasse.textProperty());
		this.klassifizierer.interfacesProperty().bind(interfaces.textProperty());
		
		tabelle.setHgap(5);
		tabelle.setVgap(15);
		
		return tabelle;
	}
	
	private Node erzeugeAttributeAnzeige() {
		TableView<Attribut> tabelle = new TableView<>(this.klassifizierer.getAttribute());
		fuelleAttributTabelle(tabelle);
		tabelle.setEditable(true);
		
		Button neu = new Button();
		NodeUtil.fuegeIconHinzu(neu, Typicons.PLUS);
		
		neu.setOnAction(e -> {
			this.klassifizierer.getAttribute()
					.add(new Attribut(Modifizierer.PRIVATE, ProgrammierEigenschaften
							.get(klassifizierer.getProgrammiersprache()).getLetzerDatentyp()));
		});
		
		Button entfernen = new Button();
		NodeUtil.fuegeIconHinzu(entfernen, Typicons.MINUS);
		
		HBox buttonBar = new HBox(neu, entfernen);
		buttonBar.setSpacing(5);
		buttonBar.setAlignment(Pos.CENTER_LEFT);
		buttonBar.setPadding(new Insets(5, 0, 0, 0));
		
		BorderPane ansicht = new BorderPane();
		ansicht.setCenter(tabelle);
		ansicht.setBottom(buttonBar);
		
		return ansicht;
	}
	
	private void fuelleAttributTabelle(TableView<Attribut> tabelle) {
		TableColumn<Attribut, Modifizierer> sichtbarkeit = new TableColumn<>();
		sichtbarkeit.setCellFactory(
				ComboBoxTableCell.forTableColumn(
						klassifizierer.getProgrammiersprache().getEigenschaften()
								.getAttributModifizierer(klassifizierer.getTyp())));
		sichtbarkeit.setCellValueFactory(
				zellDaten -> zellDaten.getValue().getSichtbarkeitProperty());
		SprachUtil.bindText(sichtbarkeit.textProperty(), sprache, "sichtbarkeit",
				"Sichtbarkeit");
		
		TableColumn<Attribut, String> attributname = new TableColumn<>();
		attributname.setCellValueFactory(zellDaten -> zellDaten.getValue().getNameProperty());
		SprachUtil.bindText(attributname.textProperty(), sprache, "attributname",
				"Attributname");
		
		TableColumn<Attribut, Datentyp> datentyp = new TableColumn<>();
		datentyp.setCellValueFactory(zellDaten -> zellDaten.getValue().getDatentypProperty());
		SprachUtil.bindText(datentyp.textProperty(), sprache, "datentyp", "Datentyp");
		
		TableColumn<Attribut, String> initialwert = new TableColumn<>();
		initialwert.setCellValueFactory(
				zellDaten -> zellDaten.getValue().getInitialwertProperty());
		SprachUtil.bindText(initialwert.textProperty(), sprache, "initialwert", "Initialwert");
		
		TableColumn<Attribut, Boolean> getter = new TableColumn<>();
		getter.setCellValueFactory(zellDaten -> zellDaten.getValue().getHatGetterProperty());
		SprachUtil.bindText(getter.textProperty(), sprache, "getter", "Getter");
		
		TableColumn<Attribut, Boolean> setter = new TableColumn<>();
		setter.setCellValueFactory(zellDaten -> zellDaten.getValue().getHatSetterProperty());
		SprachUtil.bindText(setter.textProperty(), sprache, "setter", "Setter");
		
		tabelle.getColumns().add(sichtbarkeit);
		tabelle.getColumns().add(attributname);
		tabelle.getColumns().add(datentyp);
		tabelle.getColumns().add(initialwert);
		tabelle.getColumns().add(getter);
		tabelle.getColumns().add(setter);
	}
	
	private HBox erzeugeSichtbarkeit() {
		HBox sichtbarkeit = new HBox();
		ToggleGroup sichtbarkeitGruppe = new ToggleGroup();
		Modifizierer[] modifizierer = {
			Modifizierer.PUBLIC,
			Modifizierer.PACKAGE,
			Modifizierer.PRIVATE,
			Modifizierer.PROTECTED
		};
		for (Modifizierer m : modifizierer) {
			RadioButton rb = new RadioButton(m.toString());
			rb.setUserData(m);
			rb.setDisable(!ProgrammierEigenschaften.get(klassifizierer.getProgrammiersprache())
					.istTypModifiziererErlaubt(klassifizierer.getTyp(), m));
			sichtbarkeitGruppe.getToggles().add(rb);
			sichtbarkeit.getChildren().add(rb);
			if (m.equals(klassifizierer.getSichtbarkeit())) {
				sichtbarkeitGruppe.selectToggle(rb);
			}
		}
		sichtbarkeit.setSpacing(10);
		
		sichtbarkeitGruppe.selectedToggleProperty()
				.addListener((property, alteWahl, neueWahl) -> {
					if (neueWahl.getUserData() instanceof Modifizierer m) {
						klassifizierer.setSichtbarkeit(m);
					}
				});
		return sichtbarkeit;
	}
	
	private void initialisiereButtons() {
		ButtonType[] buttons = {
			new ButtonType(sprache.getText("APPLY", "Anwenden"), ButtonData.APPLY),
			new ButtonType(sprache.getText("CANCEL_CLOSE", "Abbrechen"),
					ButtonData.CANCEL_CLOSE)
		};
		this.getDialogPane().getButtonTypes().addAll(buttons);
		this.getDialogPane().lookupButton(buttons[0]).disableProperty()
				.bind(klassifizierer.nameProperty().isEmpty());
	}
}