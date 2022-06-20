/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.typicons.Typicons;

import com.dlsc.gemsfx.EnhancedLabel;

import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Attribut;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Methode;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Modifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Parameter;
import io.github.aid_labor.classifier.uml.programmierung.ProgrammierEigenschaften;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.binding.When;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;


public class UMLKlassifiziererBearbeitenDialog extends Alert {
//	private static final Logger log = Logger
//			.getLogger(UMLKlassifiziererBearbeitenDialog.class.getName());

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
	
	private final WeakReference<UMLKlassifizierer> klassifiziererRef;
	private final Sprache sprache;
	private final SegmentedButton buttonBar;
	private final ToggleButton allgemein;
	private final ToggleButton attribute;
	private final ToggleButton methoden;
	private final ValidationSupport eingabeValidierung;
	private final List<ChangeListener<KlassifiziererTyp>> typBeobachterListe;
	private final List<ChangeListener<ValidationResult>> validierungsBeobachter;
	private final List<ListChangeListener<?>> listenBeobachter;
	private final List<String> vorhandeneElementNamen;
	private final List<Runnable> loeseBindungen;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLKlassifiziererBearbeitenDialog(UMLKlassifizierer klassifizierer, UMLProjekt projekt) {
		super(AlertType.NONE);
		this.klassifiziererRef = new WeakReference<>(klassifizierer);
		this.sprache = new Sprache();
		this.eingabeValidierung = new ValidationSupport();
		this.typBeobachterListe = new LinkedList<>();
		this.validierungsBeobachter = new LinkedList<>();
		this.listenBeobachter = new LinkedList<>();
		this.loeseBindungen = new LinkedList<>();
		this.vorhandeneElementNamen = projekt.getDiagrammElemente().parallelStream()
				.filter(element -> element.getId() != klassifizierer.getId()).map(UMLDiagrammElement::getName).toList();
		
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
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
		this.getDialogPane().setPrefSize(920, 512);
		
		this.setOnHidden(e -> {
			for (var beobachter : typBeobachterListe) {
				klassifizierer.typProperty().removeListener(beobachter);
			}
			for (var beobachter : validierungsBeobachter) {
				eingabeValidierung.validationResultProperty().removeListener(beobachter);
			}
			for (Node n : this.getDialogPane().getChildren()) {
				entferneAlleBeobachter(n);
			}
			listenBeobachter.clear();
			for (var c : eingabeValidierung.getRegisteredControls()) {
				eingabeValidierung.registerValidator(c, Validator.createPredicateValidator(x -> true, ""));
			}
			for (var prop : loeseBindungen) {
				prop.run();
			}
			loeseBindungen.clear();
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
	
	private UMLKlassifizierer getKlassifizierer() {
		return klassifiziererRef.get();
	}
	
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
		var allgemeinAnzeige = erzeugeAllgemeinAnzeige();
		var attributeAnzeige = erzeugeTabellenAnzeige(new String[] { "Sichtbarkeit", "Attributname", "Datentyp",
			"Initialwert", "Getter", "Setter", "static" }, this.getKlassifizierer().getAttribute(),
				this::erstelleAttributZeile, event -> {
					var programmierEigenschaften = getKlassifizierer().getProgrammiersprache().getEigenschaften();
					this.getKlassifizierer().getAttribute()
							.add(new Attribut(
									programmierEigenschaften.getStandardAttributModifizierer(getKlassifizierer().getTyp()),
									programmierEigenschaften.getLetzerDatentyp()));
				});
		var methodenAnzeige = erzeugeTabellenAnzeige(new String[] { "Sichtbarkeit", "Methodenname", "Parameterliste",
			"Rueckgabetyp", "abstrakt", "final", "static" }, this.getKlassifizierer().getMethoden(),
				this::erstelleMethodenZeile, event -> {
					var programmierEigenschaften = getKlassifizierer().getProgrammiersprache().getEigenschaften();
					var methode = new Methode(
							programmierEigenschaften.getStandardMethodenModifizierer(getKlassifizierer().getTyp()),
							programmierEigenschaften.getLetzerDatentyp(), getKlassifizierer().getProgrammiersprache());
					if (getKlassifizierer().getTyp().equals(KlassifiziererTyp.Interface)) {
						methode.setzeAbstrakt(true);
					}
					this.getKlassifizierer().getMethoden().add(methode);
				});
		
		StackPane container = new StackPane(allgemeinAnzeige, attributeAnzeige, methodenAnzeige);
		container.setPadding(new Insets(0, 20, 10, 20));
		container.setMaxWidth(Region.USE_PREF_SIZE);
		container.setAlignment(Pos.TOP_CENTER);
		BorderPane.setAlignment(container, Pos.TOP_CENTER);
		wurzel.setCenter(container);
		
		ueberwacheSelektion(this.allgemein, allgemeinAnzeige);
		ueberwacheSelektion(this.attribute, attributeAnzeige);
		ueberwacheSelektion(this.methoden, methodenAnzeige);
	}
	
	private void ueberwacheSelektion(ToggleButton button, Node anzeige) {
		anzeige.setVisible(button.isSelected());
		anzeige.visibleProperty().bind(button.selectedProperty());
	}
	
	private GridPane erzeugeAllgemeinAnzeige() {
		GridPane tabelle = new GridPane();
		
		String[] labelBezeichnungen = { "Typ", "Paket", "Name", "Sichtbarkeit", "Superklasse", "Interfaces" };
		
		for (int zeile = 0; zeile < labelBezeichnungen.length; zeile++) {
			String bezeichnung = labelBezeichnungen[zeile];
			var label = SprachUtil.bindText(new Label(), sprache, bezeichnung, bezeichnung + ":");
			tabelle.add(label, 0, zeile);
		}
		
		ComboBox<KlassifiziererTyp> typ = new ComboBox<>();
		typ.getItems().addAll(KlassifiziererTyp.values());
		typ.getSelectionModel().select(this.getKlassifizierer().getTyp());
		tabelle.add(typ, 1, 0);
		
		TextField paket = new TextField(this.getKlassifizierer().getPaket());
		tabelle.add(paket, 1, 1);
		
		TextField name = new TextField(this.getKlassifizierer().getName());
		tabelle.add(name, 1, 2);
		
		var sichtbarkeit = erzeugeSichtbarkeit();
		tabelle.add(sichtbarkeit, 1, 3);
		
		TextField superklasse = new TextField(this.getKlassifizierer().getSuperklasse());
		tabelle.add(superklasse, 1, 4);
		
		// TODO nutze GemsFX Tags Field
		TextField interfaces = new TextField(this.getKlassifizierer().getInterfaces());
		tabelle.add(interfaces, 1, 5);
		
		NodeUtil.beobachteSchwach(typ, typ.getSelectionModel().selectedItemProperty(), getKlassifizierer()::setTyp);
		bindeBidirektional(getKlassifizierer().getPaketProperty(), paket.textProperty());
		bindeBidirektional(getKlassifizierer().nameProperty(), name.textProperty());
		bindeBidirektional(getKlassifizierer().superklasseProperty(), superklasse.textProperty());
		bindeBidirektional(getKlassifizierer().interfacesProperty(), interfaces.textProperty());
		
		tabelle.setHgap(5);
		tabelle.setVgap(15);
		tabelle.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		
		Platform.runLater(() -> {
			eingabeValidierung.registerValidator(name,
					Validator.combine(
							Validator.createEmptyValidator(
									sprache.getText("klassennameValidierung", "Der Klassenname muss angegeben werden")),
							Validator.createPredicateValidator(tf -> !vorhandeneElementNamen.contains(name.getText()),
									sprache.getText("klassennameValidierung2",
											"Ein Element mit diesem Namen ist bereits vorhanden"))));
			setzePlatzhalter(name);
		});
		
		updateSuperklasse(superklasse, getKlassifizierer().getTyp());
		ChangeListener<KlassifiziererTyp> typBeobachter = (p, alteWahl, neueWahl) -> updateSuperklasse(superklasse,
				neueWahl);
		this.typBeobachterListe.add(typBeobachter);
		typ.getSelectionModel().selectedItemProperty().addListener(typBeobachter);
		
		this.setOnShown(e -> Platform.runLater(name::requestFocus));
		
		return tabelle;
	}
	
	private <T> Pane erzeugeTabellenAnzeige(String[] labelBezeichnungen, ObservableList<T> inhalt,
			BiFunction<T, Integer, Node[]> erzeugeZeile, EventHandler<ActionEvent> neuAktion) {
		GridPane tabelle = new GridPane();
		fuelleTabelle(tabelle, labelBezeichnungen, inhalt, erzeugeZeile);
		tabelle.setHgap(5);
		tabelle.setVgap(10);
		
		Button neu = new Button();
		NodeUtil.fuegeIconHinzu(neu, Typicons.PLUS, 20);
		neu.setOnAction(neuAktion);
		
		HBox tabellenButtons = new HBox(neu);
		tabellenButtons.setSpacing(5);
		tabellenButtons.setAlignment(Pos.CENTER_LEFT);
		tabellenButtons.setPadding(new Insets(5, 0, 0, 0));
		
		BorderPane ansicht = new BorderPane();
		ansicht.setMaxWidth(Region.USE_PREF_SIZE);
		var scrollContainer = new ScrollPane(tabelle);
		scrollContainer.getStyleClass().add("edge-to-edge");
		ansicht.setCenter(scrollContainer);
		ansicht.setBottom(tabellenButtons);
		
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
	
	private <T> void fuelleTabelle(GridPane tabelle, String[] labelBezeichnungen, ObservableList<T> inhalt,
			BiFunction<T, Integer, Node[]> erzeugeZeile) {
		for (String bezeichnung : labelBezeichnungen) {
			Label spaltenUeberschrift = SprachUtil.bindText(new EnhancedLabel(), sprache, bezeichnung.toLowerCase(),
					bezeichnung);
			tabelle.addRow(0, spaltenUeberschrift);
		}
		
		fuelleListenInhalt(tabelle, inhalt, erzeugeZeile);
		
		ListChangeListener<? super T> beobachter = aenderung -> {
			if (aenderung.next()) {
				fuelleListenInhalt(tabelle, inhalt, erzeugeZeile);
			}
		};
		listenBeobachter.add(beobachter);
		inhalt.addListener(new WeakListChangeListener<>(beobachter));
	}
	
	private <T> void fuelleListenInhalt(GridPane tabelle, ObservableList<T> inhalt,
			BiFunction<T, Integer, Node[]> erzeugeZeile) {
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
		for (var attribut : inhalt) {
			var zeilenInhalt = erzeugeZeile.apply(attribut, zeile - 1);
			tabelle.addRow(zeile, zeilenInhalt);
			zeile++;
		}
	}
	
	private Node[] erstelleAttributZeile(Attribut attribut, int zeile) {
		ComboBox<Modifizierer> sichtbarkeit = new ComboBox<>();
		sichtbarkeit.getItems().addAll(getKlassifizierer().getProgrammiersprache().getEigenschaften()
				.getAttributModifizierer(getKlassifizierer().getTyp()));
		sichtbarkeit.getSelectionModel().select(attribut.getSichtbarkeit());
		NodeUtil.beobachteSchwach(sichtbarkeit, sichtbarkeit.getSelectionModel().selectedItemProperty(),
				attribut::setSichtbarkeit);
		
		TextField name = new TextField();
		bindeBidirektional(name.textProperty(), attribut.nameProperty());
		
		TextField datentyp = new TextField(attribut.getDatentyp().getTypName());
		bindeBidirektional(datentyp.textProperty(), attribut.getDatentyp().getTypNameProperty());
		
		TextField initialwert = new TextField(attribut.getInitialwert());
		bindeBidirektional(initialwert.textProperty(),attribut.initialwertProperty());
		
		CheckBox getter = new CheckBox();
		bindeBidirektional(getter.selectedProperty(), attribut.hatGetterProperty());
		GridPane.setHalignment(getter, HPos.CENTER);
		
		CheckBox setter = new CheckBox();
		bindeBidirektional(setter.selectedProperty(), attribut.hatSetterProperty());
		GridPane.setHalignment(setter, HPos.CENTER);
		
		CheckBox statisch = new CheckBox();
		bindeBidirektional(statisch.selectedProperty(), attribut.istStatischProperty());
		GridPane.setHalignment(statisch, HPos.CENTER);
		
		Label loeschen = new Label();
		NodeUtil.erzeugeIconNode(loeschen, CarbonIcons.DELETE, 15);
		loeschen.setOnMouseClicked(e -> getKlassifizierer().getAttribute().remove(attribut));
		
		Label hoch = new Label();
		NodeUtil.erzeugeIconNode(hoch, BootstrapIcons.CARET_UP_FILL, 15);
		hoch.setOnMouseClicked(e -> tausche(getKlassifizierer().getAttribute(), zeile, zeile - 1));
		
		if (zeile == 0) {
			hoch.setDisable(true);
		}
		
		Label runter = new Label();
		NodeUtil.erzeugeIconNode(runter, BootstrapIcons.CARET_DOWN_FILL, 15);
		runter.setOnMouseClicked(e -> tausche(getKlassifizierer().getAttribute(), zeile, zeile + 1));
		
		if (zeile == getKlassifizierer().getAttribute().size() - 1) {
			runter.setDisable(true);
		}
		
		Platform.runLater(() -> {
			if (Einstellungen.getBenutzerdefiniert().erweiterteValidierungAktivierenProperty().get()) {
				eingabeValidierung.registerValidator(name,
						Validator.combine(
								Validator.createEmptyValidator(sprache.getText("nameValidierung", "Name angeben")),
								Validator.createPredicateValidator(
										tf -> getKlassifizierer().getAttribute().stream()
												.filter(a -> Objects.equals(a.getName(), name.getText())).count() <= 1,
										sprache.getText("nameValidierung2",
												"Ein Attribut mit diesem Namen ist bereits vorhanden"))));
				NodeUtil.beobachteSchwach(name, name.textProperty(), () -> eingabeValidierung.revalidate());
			} else {
				eingabeValidierung.registerValidator(name,
						Validator.createEmptyValidator(sprache.getText("nameValidierung", "Name angeben")));
			}
			
			eingabeValidierung.registerValidator(datentyp,
					Validator.createEmptyValidator(sprache.getText("datentypValidierung", "Datentyp angeben")));
			setzePlatzhalter(name);
			setzePlatzhalter(datentyp);
		});
		
		updateStatischAttribut(statisch, getKlassifizierer().getTyp());
		ChangeListener<KlassifiziererTyp> typBeobachter = (p, alteWahl, neueWahl) -> updateStatischAttribut(statisch,
				neueWahl);
		this.typBeobachterListe.add(typBeobachter);
		getKlassifizierer().typProperty().addListener(typBeobachter);
		
		if (name.getText().isEmpty()) {
			Platform.runLater(name::requestFocus);
		}
		
		return new Node[] { sichtbarkeit, name, datentyp, initialwert, getter, setter, statisch, hoch, runter,
			loeschen };
	}
	
	private HBox erzeugeSichtbarkeit() {
		HBox sichtbarkeit = new HBox();
		ToggleGroup sichtbarkeitGruppe = new ToggleGroup();
		Modifizierer[] modifizierer = { Modifizierer.PUBLIC, Modifizierer.PACKAGE, Modifizierer.PRIVATE,
			Modifizierer.PROTECTED };
		for (Modifizierer m : modifizierer) {
			RadioButton rb = new RadioButton(m.toString());
			rb.setUserData(m);
			rb.setDisable(!ProgrammierEigenschaften.get(getKlassifizierer().getProgrammiersprache())
					.istTypModifiziererErlaubt(getKlassifizierer().getTyp(), m));
			sichtbarkeitGruppe.getToggles().add(rb);
			sichtbarkeit.getChildren().add(rb);
			if (m.equals(getKlassifizierer().getSichtbarkeit())) {
				sichtbarkeitGruppe.selectToggle(rb);
			}
		}
		sichtbarkeit.setSpacing(10);
		
		ChangeListener<Toggle> beobachter = (property, alteWahl, neueWahl) -> {
			if (neueWahl.getUserData() instanceof Modifizierer m) {
				getKlassifizierer().setSichtbarkeit(m);
			}
		};
		sichtbarkeit.getProperties().put(this, beobachter);
		sichtbarkeitGruppe.selectedToggleProperty().addListener(new WeakChangeListener<>(beobachter));
		
		return sichtbarkeit;
	}
	
	private Node[] erstelleMethodenZeile(Methode methode, int zeile) {
		ComboBox<Modifizierer> sichtbarkeit = new ComboBox<>();
		sichtbarkeit.getItems().addAll(getKlassifizierer().getProgrammiersprache().getEigenschaften()
				.getMethodenModifizierer(getKlassifizierer().getTyp()));
		sichtbarkeit.getSelectionModel().select(methode.getSichtbarkeit());
		NodeUtil.beobachteSchwach(sichtbarkeit, sichtbarkeit.getSelectionModel().selectedItemProperty(),
				methode::setSichtbarkeit);
		
		TextField name = new TextField();
		bindeBidirektional(name.textProperty(), methode.getNameProperty());
		
		TextField parameter = new TextField();
		parameter.textProperty().bind(Bindings.concat("(").concat(new StringBinding() {
			{
				super.bind(methode.getParameterListe());
			}
			StringExpression stringExpr = null;
			
			@Override
			protected String computeValue() {
				if (stringExpr != null) {
					super.unbind(stringExpr);
				}
				var params = methode.getParameterListe().stream()
						.map(param -> Bindings.concat(
								new When(Einstellungen.getBenutzerdefiniert().zeigeParameterNamenProperty())
										.then(param.getNameProperty().concat(": ")).otherwise(""),
								param.getDatentyp().getTypNameProperty()))
						.toArray();
				
				if (params.length < 1) {
					return "";
				}
				
				stringExpr = Bindings.concat(params[0]);
				for (int i = 1; i < params.length; i++) {
					stringExpr = stringExpr.concat(", ");
					stringExpr = stringExpr.concat(params[i]);
				}
				
				super.bind(stringExpr);
				return stringExpr.get();
			}
		}).concat(")"));
		loeseBindungen.add(parameter.textProperty()::unbind);
		parameter.setEditable(false);
		parameter.prefColumnCountProperty().bind(
				new When(parameter.textProperty().length().greaterThanOrEqualTo(TextField.DEFAULT_PREF_COLUMN_COUNT))
						.then(parameter.textProperty().length()).otherwise(TextField.DEFAULT_PREF_COLUMN_COUNT));
		loeseBindungen.add(parameter.prefColumnCountProperty()::unbind);
		parameter.setOnMousePressed(e -> bearbeiteParameter(parameter, methode));
		parameter.setOnAction(e -> bearbeiteParameter(parameter, methode));
		
		TextField rueckgabetyp = new TextField(methode.getRueckgabeTyp().getTypName());
		bindeBidirektional(rueckgabetyp.textProperty(), methode.getRueckgabeTyp().getTypNameProperty());
		rueckgabetyp.setPrefWidth(70);
		
		CheckBox abstrakt = new CheckBox();
		bindeBidirektional(abstrakt.selectedProperty(), methode.getIstAbstraktProperty());
		GridPane.setHalignment(abstrakt, HPos.CENTER);
		
		CheckBox istFinal = new CheckBox();
		bindeBidirektional(istFinal.selectedProperty(), methode.getIstFinalProperty());
		GridPane.setHalignment(istFinal, HPos.CENTER);
		
		CheckBox statisch = new CheckBox();
		bindeBidirektional(statisch.selectedProperty(), methode.getIstStatischProperty());
		GridPane.setHalignment(statisch, HPos.CENTER);
		
		Label loeschen = new Label();
		NodeUtil.erzeugeIconNode(loeschen, CarbonIcons.DELETE, 15);
		loeschen.setOnMouseClicked(e -> getKlassifizierer().getMethoden().remove(methode));
		
		Label hoch = new Label();
		NodeUtil.erzeugeIconNode(hoch, BootstrapIcons.CARET_UP_FILL, 15);
		hoch.setOnMouseClicked(e -> tausche(getKlassifizierer().getMethoden(), zeile, zeile - 1));
		
		if (zeile == 0) {
			hoch.setDisable(true);
		}
		
		Label runter = new Label();
		NodeUtil.erzeugeIconNode(runter, BootstrapIcons.CARET_DOWN_FILL, 15);
		runter.setOnMouseClicked(e -> tausche(getKlassifizierer().getMethoden(), zeile, zeile + 1));
		
		if (zeile == getKlassifizierer().getMethoden().size() - 1) {
			runter.setDisable(true);
		}
		
		Platform.runLater(() -> {
			if (Einstellungen.getBenutzerdefiniert().erweiterteValidierungAktivierenProperty().get()) {
				eingabeValidierung.registerValidator(parameter, Validator.combine(
						Validator.createPredicateValidator(
								tf -> getKlassifizierer().getMethoden().stream()
										.filter(m -> Objects.equals(m.getName(), methode.getName()) && Objects
												.deepEquals(m.getParameterListe(), methode.getParameterListe()))
										.count() <= 1,
								sprache.getText("methodeValidierung",
										"Eine Methode mit gleicher Signatur (Name und Parameterliste) "
												+ "ist bereits vorhanden")),
						Validator.createPredicateValidator(
								tf -> {
									Set<String> params = new HashSet<>();
									boolean doppelt = false;
									for (var param : methode.getParameterListe()) {
										if (!params.add(param.getName())) {
											doppelt = true;
											break;
										}
									}
									return !doppelt;
								},
								sprache.getText("parameterValidierung2",
										"Es darf keine Parameter mit gleichem Namen geben"))
						));
				setzePlatzhalter(parameter);
				NodeUtil.beobachteSchwach(name, name.textProperty(), () -> eingabeValidierung.revalidate());
			}
			eingabeValidierung.registerValidator(name,
					Validator.createEmptyValidator(sprache.getText("nameValidierung", "Name angeben")));
			eingabeValidierung.registerValidator(rueckgabetyp,
					Validator.createEmptyValidator(sprache.getText("datentypValidierung", "Datentyp angeben")));
			
			setzePlatzhalter(name);
			setzePlatzhalter(rueckgabetyp);
		});
		
		updateMethode(abstrakt, statisch, getKlassifizierer().getTyp(), methode);
		ChangeListener<KlassifiziererTyp> typBeobachter = (p, alteWahl, neueWahl) -> updateMethode(abstrakt, statisch,
				neueWahl, methode);
		this.typBeobachterListe.add(typBeobachter);
		getKlassifizierer().typProperty().addListener(typBeobachter);
		
		if (methode.istGetter() || methode.istSetter()) {
			rueckgabetyp.setDisable(true);
		}
		
		if (methode.istGetter()) {
			parameter.setDisable(true);
		}
		
		NodeUtil.beobachteSchwach(abstrakt, abstrakt.selectedProperty(), (alt, istAbstrakt) -> {
			if (istAbstrakt.booleanValue()) {
				statisch.setSelected(false);
			}
		});
		
		NodeUtil.beobachteSchwach(statisch, statisch.selectedProperty(), (alt, istStatisch) -> {
			if (istStatisch.booleanValue()) {
				abstrakt.setSelected(false);
			}
		});
		
		if (name.getText().isEmpty()) {
			Platform.runLater(name::requestFocus);
		}
		
		return new Node[] { sichtbarkeit, name, parameter, rueckgabetyp, abstrakt, istFinal, statisch, hoch, runter,
			loeschen };
	}
	
	private void bearbeiteParameter(TextField parameter, Methode methode) {
		var parameterListe = erzeugeTabellenAnzeige(new String[] { "Parametername", "Datentyp" },
				methode.getParameterListe(), (param, zeile) -> {
					TextField name = new TextField();
					bindeBidirektional(name.textProperty(), param.getNameProperty());
					
					TextField datentyp = new TextField(param.getDatentyp().getTypName());
					bindeBidirektional(datentyp.textProperty(), param.getDatentyp().getTypNameProperty());
					
					Label loeschen = new Label();
					NodeUtil.erzeugeIconNode(loeschen, CarbonIcons.DELETE, 15);
					loeschen.setOnMouseClicked(e -> methode.getParameterListe().remove(param));
					
					Label hoch = new Label();
					NodeUtil.erzeugeIconNode(hoch, BootstrapIcons.CARET_UP_FILL, 15);
					hoch.setOnMouseClicked(e -> tausche(methode.getParameterListe(), zeile, zeile - 1));
					
					if (zeile == 0) {
						hoch.setDisable(true);
					}
					
					Label runter = new Label();
					NodeUtil.erzeugeIconNode(runter, BootstrapIcons.CARET_DOWN_FILL, 15);
					runter.setOnMouseClicked(e -> tausche(methode.getParameterListe(), zeile, zeile + 1));
					
					if (zeile == methode.getParameterListe().size() - 1) {
						runter.setDisable(true);
					}
					
					if (methode.istGetter() || methode.istSetter()) {
						datentyp.setDisable(true);
						loeschen.setDisable(true);
						hoch.setDisable(true);
						runter.setDisable(true);
					}
					
					Platform.runLater(() -> {
						if (Einstellungen.getBenutzerdefiniert().erweiterteValidierungAktivierenProperty().get()) {
							eingabeValidierung.registerValidator(name,
									Validator.combine(
											Validator.createEmptyValidator(
													sprache.getText("nameValidierung", "Name angeben")),
											Validator.createPredicateValidator(
													tf -> methode.getParameterListe().stream()
															.filter(p -> Objects.equals(p.getName(), name.getText()))
															.count() <= 1,
													sprache.getText("parameterValidierung",
															"Ein Parameter mit diesem Namen ist bereits vorhanden"))));
							NodeUtil.beobachteSchwach(name, name.textProperty(), () -> eingabeValidierung.revalidate());
						} else {
							eingabeValidierung.registerValidator(name,
									Validator.createEmptyValidator(sprache.getText("nameValidierung", "Name angeben")));
						}
						
						eingabeValidierung.registerValidator(datentyp, Validator
								.createEmptyValidator(sprache.getText("datentypValidierung", "Datentyp angeben")));
						setzePlatzhalter(name);
						setzePlatzhalter(datentyp);
						
						if (Einstellungen.getBenutzerdefiniert().erweiterteValidierungAktivierenProperty().get()) {
							NodeUtil.beobachteSchwach(name, name.textProperty(), () -> eingabeValidierung.revalidate());
							NodeUtil.beobachteSchwach(datentyp, datentyp.textProperty(),
									() -> eingabeValidierung.revalidate());
						}
					});
					
					if (name.getText().isEmpty()) {
						Platform.runLater(name::requestFocus);
					}
					
					return new Node[] { name, datentyp, loeschen, hoch, runter };
				}, event -> {
					if (!methode.istGetter() && !methode.istSetter()) {
						var programmierEigenschaften = getKlassifizierer().getProgrammiersprache().getEigenschaften();
						methode.getParameterListe().add(new Parameter(programmierEigenschaften.getLetzerDatentyp()));
					}
				});
		
		parameterListe.setPadding(new Insets(15));
		PopOver parameterDialog = new PopOver(parameterListe);
		parameterDialog.setArrowLocation(ArrowLocation.TOP_CENTER);
		parameterDialog.show(parameter);
	}
	
	private void initialisiereButtons() {
		ButtonType[] buttons = { new ButtonType(sprache.getText("APPLY", "Anwenden"), ButtonData.FINISH),
			new ButtonType(sprache.getText("CANCEL_CLOSE", "Abbrechen"), ButtonData.BACK_PREVIOUS) };
		this.getDialogPane().getButtonTypes().addAll(buttons);
		this.getDialogPane().lookupButton(buttons[0]).disableProperty().bind(eingabeValidierung.invalidProperty());
		loeseBindungen.add(this.getDialogPane().lookupButton(buttons[0]).disableProperty()::unbind);
	}
	
	private void setzePlatzhalter(TextInputControl eingabefeld) {
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
	
	private <E> void tausche(List<E> liste, int indexA, int indexB) {
		var a = liste.get(indexA);
		var b = liste.get(indexB);
		liste.set(indexA, b);
		liste.set(indexB, a);
	}
	
	private void updateSuperklasse(TextField superklasse, KlassifiziererTyp typ) {
		boolean superklasseErlaubt = getKlassifizierer().getProgrammiersprache().getEigenschaften().erlaubtSuperklasse(typ);
		if (!superklasseErlaubt) {
			superklasse.setText("");
		}
		superklasse.setDisable(!superklasseErlaubt);
	}
	
	private void updateStatischAttribut(CheckBox statisch, KlassifiziererTyp typ) {
		boolean instanzAttributeErlaubt = getKlassifizierer().getProgrammiersprache().getEigenschaften()
				.erlaubtInstanzAttribute(typ);
		if (!instanzAttributeErlaubt) {
			statisch.setSelected(true);
		}
		if (typ.equals(KlassifiziererTyp.Interface)) {
			statisch.setSelected(true);
		}
		statisch.setDisable(!instanzAttributeErlaubt);
	}
	
	private void updateMethode(CheckBox abstrakt, CheckBox statisch, KlassifiziererTyp typ, Methode methode) {
		boolean abstraktErlaubt = getKlassifizierer().getProgrammiersprache().getEigenschaften()
				.erlaubtAbstrakteMethode(typ);
		boolean abstraktErzwungen = !getKlassifizierer().getProgrammiersprache().getEigenschaften()
				.erlaubtNichtAbstrakteMethode(typ);
		
		if (abstraktErzwungen) {
			abstrakt.setSelected(true);
			abstrakt.setDisable(true);
		}
		
		if (!abstraktErlaubt) {
			abstrakt.setSelected(false);
			abstrakt.setDisable(true);
		} else {
			abstrakt.setDisable(false);
		}
		
		if (typ.equals(KlassifiziererTyp.Interface) && !methode.istGetter() && !methode.istSetter()
				&& !methode.istStatisch()) {
			abstrakt.setSelected(true);
		} else if (typ.equals(KlassifiziererTyp.Interface) && (methode.istGetter() || methode.istSetter())) {
			abstrakt.setDisable(true);
			statisch.setDisable(true);
		}
	}
	
	private <T> void bindeBidirektional(Property<T> p1, Property<T> p2) {
		p1.bindBidirectional(p2);
		loeseBindungen.add(() -> p1.unbindBidirectional(p2));
	}
}