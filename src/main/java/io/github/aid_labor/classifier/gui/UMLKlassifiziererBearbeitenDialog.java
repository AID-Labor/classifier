/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.util.List;

import org.controlsfx.control.SegmentedButton;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.typicons.Typicons;

import com.dlsc.gemsfx.EnhancedLabel;

import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.eigenschaften.Attribut;
import io.github.aid_labor.classifier.uml.eigenschaften.Modifizierer;
import io.github.aid_labor.classifier.uml.eigenschaften.ProgrammierEigenschaften;
import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
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
	private final UMLKlassifizierer sicherungskopie;
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
		this.sicherungskopie = klassifizierer.erzeugeTiefeKopie();
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
	
	public UMLKlassifizierer getSicherungskopie() {
		return sicherungskopie;
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
		container.setAlignment(Pos.CENTER);
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
		GridPane tabelle = new GridPane();
		fuelleAttributTabelle(tabelle);
		tabelle.setHgap(5);
		tabelle.setVgap(10);
		
		Button neu = new Button();
		NodeUtil.fuegeIconHinzu(neu, Typicons.PLUS, 20);
		
		neu.setOnAction(e -> {
			this.klassifizierer.getAttribute()
					.add(new Attribut(Modifizierer.PRIVATE, ProgrammierEigenschaften
							.get(klassifizierer.getProgrammiersprache()).getLetzerDatentyp()));
		});
		
		HBox buttonBar = new HBox(neu);
		buttonBar.setSpacing(5);
		buttonBar.setAlignment(Pos.CENTER_LEFT);
		buttonBar.setPadding(new Insets(5, 0, 0, 0));
		
		BorderPane ansicht = new BorderPane();
		var scrollContainer = new ScrollPane(tabelle);
		scrollContainer.getStyleClass().add("edge-to-edge");
		ansicht.setCenter(scrollContainer);
		ansicht.setBottom(buttonBar);
		
		return ansicht;
	}
	
	private void fuelleAttributTabelle(GridPane tabelle) {
		String[] labelBezeichnungen = { "Sichtbarkeit", "Attributname", "Datentyp",
			"Initialwert", "Getter", "Setter" };
		
		for (String bezeichnung : labelBezeichnungen) {
			Label spaltenUeberschrift = SprachUtil.bindText(new EnhancedLabel(), sprache,
					bezeichnung.toLowerCase(), bezeichnung);
			tabelle.addRow(0, spaltenUeberschrift);
		}
		
		klassifizierer.getAttribute().addListener(new ListChangeListener<Attribut>() {
			@Override
			public void onChanged(Change<? extends Attribut> aenderung) {
				if (aenderung.next()) {
					fuelleAttributListe(tabelle, aenderung.getList());
				}
			}
		});
	}
	
	private void fuelleAttributListe(GridPane tabelle,
			List<? extends Attribut> attributListe) {
		tabelle.getChildren().removeIf(kind -> GridPane.getRowIndex(kind) > 0);
		int zeile = 1;
		for (var attribut : attributListe) {
			var inhalt = erstelleAttributZeile(attribut, zeile-1);
			tabelle.addRow(zeile, inhalt);
			zeile++;
		}
	}
	
	private Node[] erstelleAttributZeile(Attribut attribut, int zeile) {
		ComboBox<Modifizierer> sichtbarkeit = new ComboBox<>();
		sichtbarkeit.getItems().addAll(klassifizierer.getProgrammiersprache()
				.getEigenschaften().getAttributModifizierer(klassifizierer.getTyp()));
		sichtbarkeit.getSelectionModel().select(attribut.getSichtbarkeit());
		sichtbarkeit.getSelectionModel().selectedItemProperty().addListener((o, alt, neu) -> {
			attribut.setSichtbarkeit(neu);
		});
		
		TextField name = new TextField();
		name.textProperty().bindBidirectional(attribut.getNameProperty());
		
		TextField datentyp = new TextField(attribut.getDatentyp().getTypName());
		datentyp.textProperty().bindBidirectional(attribut.getDatentyp().getTypNameProperty());
		
		TextField initialwert = new TextField(attribut.getInitialwert());
		initialwert.textProperty().bindBidirectional(attribut.getInitialwertProperty());
		initialwert.setPrefWidth(60);
		
		CheckBox getter = new CheckBox();
		getter.selectedProperty().bindBidirectional(attribut.getHatGetterProperty());
		GridPane.setHalignment(getter, HPos.CENTER);
		
		CheckBox setter = new CheckBox();
		setter.selectedProperty().bindBidirectional(attribut.getHatSetterProperty());
		GridPane.setHalignment(setter, HPos.CENTER);
		
		Label loeschen = new Label();
		NodeUtil.erzeugeIconNode(loeschen, CarbonIcons.DELETE, 15);
		loeschen.setOnMouseClicked(e -> {
			klassifizierer.getAttribute().remove(attribut);
		});
		
		Label hoch = new Label();
		NodeUtil.erzeugeIconNode(hoch, BootstrapIcons.CARET_UP_FILL, 15);
		hoch.setOnMouseClicked(e -> {
			tausche(klassifizierer.getAttribute(), zeile, zeile-1);
		});
		
		if(zeile == 0) {
			hoch.setDisable(true);
		}
		
		Label runter = new Label();
		NodeUtil.erzeugeIconNode(runter, BootstrapIcons.CARET_DOWN_FILL, 15);
		runter.setOnMouseClicked(e -> {
			tausche(klassifizierer.getAttribute(), zeile, zeile+1);
		});
		
		if (zeile == klassifizierer.getAttribute().size()-1) {
			runter.setDisable(true);
		}
		
		return new Node[] { sichtbarkeit, name, datentyp, initialwert, getter, setter, hoch,
			runter, loeschen};
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
			new ButtonType(sprache.getText("APPLY", "Anwenden"), ButtonData.FINISH),
			new ButtonType(sprache.getText("CANCEL_CLOSE", "Abbrechen"),
					ButtonData.BACK_PREVIOUS)
		};
		this.getDialogPane().getButtonTypes().addAll(buttons);
		this.getDialogPane().lookupButton(buttons[0]).disableProperty()
				.bind(klassifizierer.nameProperty().isEmpty());
	}
	
	private <E> void tausche(List<E> liste, int indexA, int indexB) {
		var a = klassifizierer.getAttribute().get(indexA);
		var b = klassifizierer.getAttribute().get(indexB);
		klassifizierer.getAttribute().set(indexA, b);
		klassifizierer.getAttribute().set(indexB, a);
	}
}