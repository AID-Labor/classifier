/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.kordamp.ikonli.typicons.Typicons;

import com.dlsc.gemsfx.EnhancedLabel;
import com.dlsc.gemsfx.SearchField;

import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.HatParameterListe;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Methode;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Modifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Parameter;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.binding.When;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;


public class MethodenBearbeitenListe extends TableView<Methode> {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public static final String CSS_EINGABE_FEHLER = "eingabefehler";
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static Validator<?> ignorierenValidierung() {
		return Validator.createPredicateValidator(x -> true, "", Severity.OK);
	}
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final UMLKlassifizierer klassifizierer;
	private final ValidationSupport eingabeValidierung;
	private final Sprache sprache;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@SuppressWarnings("unchecked")
	public MethodenBearbeitenListe(ObservableList<Methode> methoden, UMLKlassifizierer klassifizierer, Sprache sprache,
			ValidationSupport eingabeValidierung) {
		super(methoden);
		this.klassifizierer = klassifizierer;
		this.eingabeValidierung = eingabeValidierung;
		this.sprache = sprache;
		
		TableColumn<Methode, Modifizierer> spalte1 = erstelleSichbarkeitSpalte(
				sprache.getTextProperty("sichtbarkeit", "Sichtbarkeit"));
		TableColumn<Methode, String> spalte2 = erstelleNameSpalte(
				sprache.getTextProperty("methodenname", "Methodenname"));
		TableColumn<Methode, Methode> spalte3 = erstelleParameterSpalte(
				sprache.getTextProperty("parameterliste", "Parameterliste"));
		TableColumn<Methode, String> spalte4 = erstelleRueckgabeSpalte(
				sprache.getTextProperty("rueckgabetyp", "R%cckgabetyp".formatted(Umlaute.ue)));
		TableColumn<Methode, Boolean> spalte5 = erstelleAbstraktSpalte(sprache.getTextProperty("abstrakt", "Abstrakt"));
		TableColumn<Methode, Boolean> spalte6 = erstelleStatischSpalte(sprache.getTextProperty("static", "Static"));
		TableColumn<Methode, Boolean> spalte7 = erstelleFinalSpalte(sprache.getTextProperty("final", "Final"));
		TableColumn<Methode, Methode> spalte8 = erstelleKontrollSpalte();
		
		this.getColumns().addAll(spalte1, spalte2, spalte3, spalte4, spalte5, spalte6, spalte7, spalte8);
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
	
	private TableColumn<Methode, Modifizierer> erstelleSichbarkeitSpalte(ReadOnlyStringProperty titel) {
		TableColumn<Methode, Modifizierer> spalte = new TableColumn<>();
		spalte.textProperty().bind(titel);
		spalte.setCellValueFactory(daten -> daten.getValue().sichtbarkeitProperty());
		
		spalte.setCellFactory(s -> new TableCell<Methode, Modifizierer>() {
			private final ComboBox<Modifizierer> sichtbarkeit;
			private final Runnable updater;
			
			{
				sichtbarkeit = new ComboBox<>();
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				setGraphic(sichtbarkeit);
				updater = () -> {
					if (getTableRow() != null && getTableRow().getItem() != null && !isEmpty()) {
						updateSichtbarkeit(getTableRow().getItem());
					}
				};
			}
			
			@Override
			protected void updateItem(Modifizierer mod, boolean istLeer) {
				NodeUtil.entferneSchwacheBeobachtung(sichtbarkeit);
				super.updateItem(mod, istLeer);
				getGraphic().setVisible(!istLeer && mod != null);
				setText(null);
				if (getTableRow() == null || getTableRow().getItem() == null) {
					return;
				}
				
				if (!istLeer && mod != null) {
					NodeUtil.beobachteSchwach(sichtbarkeit, sichtbarkeit.getSelectionModel().selectedItemProperty(),
							neu -> getTableRow().getItem().setSichtbarkeit(neu));
					NodeUtil.beobachteSchwach(sichtbarkeit, getTableRow().getItem().istAbstraktProperty(), updater);
					NodeUtil.beobachteSchwach(sichtbarkeit, getTableRow().getItem().istStatischProperty(), updater);
					updateSichtbarkeit(getTableRow().getItem());
					NodeUtil.beobachteSchwach(sichtbarkeit, klassifizierer.typProperty(), updater);
				}
			}
			
			private void updateSichtbarkeit(Methode methode) {
				List<Modifizierer> modifizierer = klassifizierer.getProgrammiersprache().getEigenschaften()
						.getMethodenModifizierer(klassifizierer.getTyp(), methode.istStatisch(), methode.istAbstrakt());
				var aktuellerModifizierer = methode.getSichtbarkeit();
				sichtbarkeit.getItems().setAll(modifizierer);
				if (aktuellerModifizierer != null && modifizierer.contains(aktuellerModifizierer)) {
					sichtbarkeit.getSelectionModel().select(aktuellerModifizierer);
				} else {
					sichtbarkeit.getSelectionModel().selectFirst();
				}
			}
		});
		
		return spalte;
	}
	
	private TableColumn<Methode, String> erstelleNameSpalte(ReadOnlyStringProperty titel) {
		TableColumn<Methode, String> spalte = new TableColumn<>();
		spalte.textProperty().bind(titel);
		spalte.setCellValueFactory(daten -> daten.getValue().nameProperty());
		
		spalte.setCellFactory(s -> new TableCell<Methode, String>() {
			private final TextField name;
			private Methode m;
			
			{
				name = new TextField();
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				setGraphic(name);
				new TextFieldTableCell<>();
			}
			
			@Override
			protected void updateItem(String methodenName, boolean istLeer) {
				super.updateItem(methodenName, istLeer);
				getGraphic().setVisible(!istLeer && methodenName != null);
				if (getTableRow() == null || Objects.equals(m, getTableRow().getItem())) {
					return;
				}
				NodeUtil.entferneSchwacheBeobachtung(name);
				m = getTableRow().getItem();
				if (!istLeer && methodenName != null) {
					name.setText(methodenName);
					NodeUtil.beobachteSchwach(name, name.textProperty(), text -> {
						getTableRow().getItem().setName(text);
					});
					Platform.runLater(() -> {
						eingabeValidierung.registerValidator(name,
								Validator.createEmptyValidator(sprache.getText("nameValidierung", "Name angeben")));
						setzePlatzhalter(name);
						if (Einstellungen.getBenutzerdefiniert().erweiterteValidierungAktivierenProperty().get()) {
							NodeUtil.beobachteSchwach(name, name.textProperty(), () -> eingabeValidierung.revalidate());
						}
						
					});
					if (name.getText().isEmpty()) {
						Platform.runLater(name::requestFocus);
					}
				} else {
					setText(null);
					eingabeValidierung.registerValidator(name, ignorierenValidierung());
				}
			}
		});
		
		return spalte;
	}
	
	private TableColumn<Methode, Methode> erstelleParameterSpalte(ReadOnlyStringProperty titel) {
		TableColumn<Methode, Methode> spalte = new TableColumn<>();
		spalte.textProperty().bind(titel);
		spalte.setCellValueFactory(daten -> new ObservableValueBase<Methode>() {
			@Override
			public Methode getValue() {
				return daten.getValue();
			}
		});
		
		spalte.setCellFactory(s -> new TableCell<Methode, Methode>() {
			private TextField parameter = new TextField();
			
			{
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				setGraphic(parameter);
				parameter.setEditable(false);
				parameter.prefColumnCountProperty()
						.bind(new When(parameter.textProperty().length()
								.greaterThanOrEqualTo(TextField.DEFAULT_PREF_COLUMN_COUNT))
								.then(parameter.textProperty().length())
								.otherwise(TextField.DEFAULT_PREF_COLUMN_COUNT));
				parameter.setOnMousePressed(e -> {
					if (!isEmpty() && getItem() != null) {
						bearbeiteParameter(parameter, getTableRow().getItem());
					}
				});
				parameter.setOnAction(e -> {
					if (!isEmpty() && getItem() != null) {
						bearbeiteParameter(parameter, getTableRow().getItem());
					}
				});
			}
			
			@Override
			protected void updateItem(Methode meth, boolean istLeer) {
				super.updateItem(meth, istLeer);
				setText(null);
				parameter.textProperty().unbind();
				parameter.setVisible(meth != null && !istLeer);
				if (getTableRow() == null || getTableRow().getItem() == null) {
					eingabeValidierung.registerValidator(parameter, false, ignorierenValidierung());
					return;
				}
				
				if (!istLeer && meth != null) {
					erzeugeParameterBindung(parameter, getTableRow().getItem());
					validiereParameter(getTableRow().getItem());
					if (getTableRow().getItem().istGetter()) {
						parameter.setDisable(true);
					}
				} else {
					eingabeValidierung.registerValidator(parameter, false, ignorierenValidierung());
				}
			}
			
			private void erzeugeParameterBindung(TextField parameter, Methode methode) {
				parameter.textProperty().bind(Bindings.concat("(").concat(new StringBinding() {
					{
						super.bind(methode.parameterListeProperty());
					}
					StringExpression stringExpr = null;
					
					@Override
					protected String computeValue() {
						if (stringExpr != null) {
							super.unbind(stringExpr);
						}
						var params = methode.parameterListeProperty().stream()
								.map(param -> Bindings.concat(
										new When(Einstellungen.getBenutzerdefiniert().zeigeParameterNamenProperty())
												.then(param.nameProperty().concat(": ")).otherwise(""),
										param.getDatentyp().typNameProperty()))
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
			}
			
			private void validiereParameter(Methode methode) {
				Platform.runLater(() -> {
					if (Einstellungen.getBenutzerdefiniert().erweiterteValidierungAktivierenProperty().get()) {
						eingabeValidierung.registerValidator(parameter,
								Validator.combine(
										Validator.createPredicateValidator(
												tf -> klassifizierer.methodenProperty().stream()
														.filter(m -> Objects.equals(m.getName(), methode.getName())
																&& Objects.deepEquals(m.parameterListeProperty()
																		.stream().map(Parameter::getDatentyp).toList(),
																		methode.parameterListeProperty().stream()
																				.map(Parameter::getDatentyp).toList()))
														.count() <= 1,
												sprache.getText("methodeValidierung",
														"Eine Methode mit gleicher Signatur (Name und Parameterliste) "
																+ "ist bereits vorhanden")),
										Validator.createPredicateValidator(tf -> {
											Set<String> params = new HashSet<>();
											boolean doppelt = false;
											for (var param : methode.parameterListeProperty()) {
												if (!params.add(param.getName())) {
													doppelt = true;
													break;
												}
											}
											return !doppelt;
										}, sprache.getText("parameterValidierung2",
												"Es darf keine Parameter mit gleichem Namen geben"))));
						setzePlatzhalter(parameter);
					}
				});
			}
			
		});
		
		return spalte;
	}
	
	private void bearbeiteParameter(TextField parameter, HatParameterListe typMitParameterliste) {
		var parameterListe = erzeugeTabellenAnzeige(new String[] { "Parametername", "Datentyp" },
				typMitParameterliste.parameterListeProperty(), (param, kontrollelemente) -> {
					TextField name = new TextField();
					name.setText(param.getName());
					bindeBidirektional(name.textProperty(), param.nameProperty(), name);
					
					SearchField<String> datentyp = new DatentypFeld(param.getDatentyp().getTypName(), true,
							klassifizierer.getProgrammiersprache(), eingabeValidierung);
					bindeBidirektional(datentyp.selectedItemProperty(), param.getDatentyp().typNameProperty(),
							datentyp);
					
					if (typMitParameterliste instanceof Methode m && (m.istGetter() || m.istSetter())) {
						datentyp.setDisable(true);
						kontrollelemente.getHoch().setDisable(true);
						kontrollelemente.getRunter().setDisable(true);
						kontrollelemente.getLoeschen().setDisable(true);
					}
					
					validiereParameter(name, datentyp, typMitParameterliste, param);
					
					if (name.getText().isEmpty()) {
						Platform.runLater(name::requestFocus);
					}
					
					return new Node[] { name, datentyp, kontrollelemente.getHoch(), kontrollelemente.getRunter(),
						kontrollelemente.getLoeschen() };
				}, event -> {
					if (!(typMitParameterliste instanceof Methode m) || (!m.istGetter() && !m.istSetter())) {
						var programmierEigenschaften = klassifizierer.getProgrammiersprache().getEigenschaften();
						typMitParameterliste.parameterListeProperty()
								.add(new Parameter(programmierEigenschaften.getLetzerDatentyp()));
					}
				});
		
		parameterListe.setPadding(new Insets(15));
		PopOver parameterDialog = new PopOver(parameterListe);
		parameterDialog.setArrowLocation(ArrowLocation.TOP_CENTER);
		parameterDialog.getRoot().getStylesheets().addAll(parameter.getScene().getStylesheets());
		parameterDialog.show(parameter);
	}
	
	private void validiereParameter(TextField name, SearchField<String> datentyp,
			HatParameterListe typMitParameterListe, Parameter param) {
		Platform.runLater(() -> {
			if (Einstellungen.getBenutzerdefiniert().erweiterteValidierungAktivierenProperty().get()) {
				eingabeValidierung.registerValidator(name,
						Validator.combine(
								Validator.createEmptyValidator(sprache.getText("nameValidierung", "Name angeben")),
								Validator.createPredicateValidator(
										tf -> typMitParameterListe.parameterListeProperty().stream()
												.filter(p -> Objects.equals(p.getName(), name.getText())).count() <= 1,
										sprache.getText("parameterValidierung",
												"Ein Parameter mit diesem Namen ist bereits vorhanden"))));
				NodeUtil.beobachteSchwach(name, name.textProperty(), () -> eingabeValidierung.revalidate());
			} else {
				eingabeValidierung.registerValidator(name,
						Validator.createEmptyValidator(sprache.getText("nameValidierung", "Name angeben")));
			}
			
			eingabeValidierung.registerValidator(datentyp.getEditor(), Validator.combine(
					Validator.createEmptyValidator(sprache.getText("datentypValidierung", "Datentyp angeben")),
					Validator.createPredicateValidator(
							tf -> !klassifizierer.getProgrammiersprache().getEigenschaften()
									.istVoid(param.getDatentyp()),
							sprache.getText("attributValidierungVoid", "Der Typ void ist hier nicht erlaubt"))));
			setzePlatzhalter(name);
			setzePlatzhalter(datentyp);
			
			if (Einstellungen.getBenutzerdefiniert().erweiterteValidierungAktivierenProperty().get()) {
				NodeUtil.beobachteSchwach(name, name.textProperty(), () -> eingabeValidierung.revalidate());
				NodeUtil.beobachteSchwach(datentyp, datentyp.textProperty(), () -> eingabeValidierung.revalidate());
			}
		});
	}
	
	private <T> Pane erzeugeTabellenAnzeige(String[] labelBezeichnungen, ObservableList<T> inhalt,
			BiFunction<T, KontrollElemente<T>, Node[]> erzeugeZeile, EventHandler<ActionEvent> neuAktion) {
		GridPane tabelle = new GridPane();
		fuelleTabelle(tabelle, labelBezeichnungen, inhalt, erzeugeZeile);
		tabelle.setHgap(5);
		tabelle.setVgap(10);
		
		Button neu = new Button();
		NodeUtil.fuegeIconHinzu(neu, Typicons.PLUS, 20, "neu-button-font-icon");
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
		});
		
		NodeUtil.beobachteSchwach(tabelle, tabelle.widthProperty(),
				() -> Platform.runLater(scrollContainer::requestLayout));
		
		return ansicht;
	}
	
	private <T> void fuelleTabelle(GridPane tabelle, String[] labelBezeichnungen, ObservableList<T> inhalt,
			BiFunction<T, KontrollElemente<T>, Node[]> erzeugeZeile) {
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
		inhalt.addListener(beobachter);
	}
	
	private <T> void fuelleListenInhalt(GridPane tabelle, ObservableList<T> inhalt,
			BiFunction<T, KontrollElemente<T>, Node[]> erzeugeZeile) {
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
		for (T element : inhalt) {
			var zeilenInhalt = erzeugeZeile.apply(element, new KontrollElemente<>(inhalt, element, zeile - 1));
			tabelle.addRow(zeile, zeilenInhalt);
			zeile++;
		}
	}
	
	private TableColumn<Methode, String> erstelleRueckgabeSpalte(ReadOnlyStringProperty titel) {
		TableColumn<Methode, String> spalte = new TableColumn<>();
		spalte.textProperty().bind(titel);
		spalte.setCellValueFactory(daten -> daten.getValue().getRueckgabeTyp().typNameProperty());
		
		spalte.setCellFactory(s -> new TableCell<Methode, String>() {
			private SearchField<String> rueckgabetyp = new DatentypFeld("", true,
					klassifizierer.getProgrammiersprache(), eingabeValidierung);
			
			{
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				setGraphic(rueckgabetyp);
			}
			
			@Override
			protected void updateItem(String typ, boolean istLeer) {
				super.updateItem(typ, istLeer);
				setText(null);
				NodeUtil.entferneSchwacheBeobachtung(rueckgabetyp);
				rueckgabetyp.setVisible(typ != null && !istLeer);
				if (getTableRow() == null || getTableRow().getItem() == null) {
					eingabeValidierung.registerValidator(rueckgabetyp, false, ignorierenValidierung());
					return;
				}
				
				if (!istLeer && typ != null) {
					rueckgabetyp.setSelectedItem(typ);
					rueckgabetyp.setText(typ);
					rueckgabetyp.cancel();
					bindeBidirektional(rueckgabetyp.selectedItemProperty(),
							getTableRow().getItem().getRueckgabeTyp().typNameProperty(), rueckgabetyp);
					if (getTableRow().getItem().istGetter() || getTableRow().getItem().istSetter()) {
						rueckgabetyp.setDisable(true);
					}
					
					Platform.runLater(() -> {
						eingabeValidierung.registerValidator(rueckgabetyp.getEditor(), Validator
								.createEmptyValidator(sprache.getText("datentypValidierung", "Datentyp angeben")));
						setzePlatzhalter(rueckgabetyp);
					});
				} else {
					eingabeValidierung.registerValidator(rueckgabetyp, false, ignorierenValidierung());
				}
			}
			
		});
		
		return spalte;
	}
	
	private TableColumn<Methode, Boolean> erstelleAbstraktSpalte(ReadOnlyStringProperty titel) {
		TableColumn<Methode, Boolean> spalte = new TableColumn<>();
		spalte.textProperty().bind(titel);
		spalte.setCellValueFactory(daten -> daten.getValue().istAbstraktProperty());
		
		spalte.setCellFactory(s -> new TableCell<Methode, Boolean>() {
			private CheckBox abstrakt = new CheckBox();
			
			{
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				setGraphic(abstrakt);
			}
			
			@Override
			protected void updateItem(Boolean istAbstrakt, boolean istLeer) {
				super.updateItem(istAbstrakt, istLeer);
				abstrakt.setVisible(istAbstrakt != null && !istLeer);
				setText(null);
				NodeUtil.entferneSchwacheBeobachtung(abstrakt);
				if (getTableRow() == null || getTableRow().getItem() == null) {
					eingabeValidierung.registerValidator(abstrakt, false, ignorierenValidierung());
					return;
				}
				
				if (!istLeer && istAbstrakt != null) {
					abstrakt.setSelected(istAbstrakt);
					bindeBidirektional(abstrakt.selectedProperty(), getTableRow().getItem().istAbstraktProperty(),
							abstrakt);
					
					Platform.runLater(() -> {
						eingabeValidierung.registerValidator(abstrakt, Validator.createPredicateValidator(c -> {
							return !(abstrakt.isSelected() && getTableRow().getItem().istFinal());
						}, sprache.getText("abstraktFinalValidierung",
								"Eine abstrakte Methode darf nicht final sein")));
						
						setzePlatzhalter(abstrakt);
					});
					
					updateMethode(getTableRow().getItem(), klassifizierer.getTyp());
					NodeUtil.beobachteSchwach(abstrakt, klassifizierer.typProperty(),
							typ -> updateMethode(getTableRow().getItem(), typ));
					NodeUtil.beobachteSchwach(abstrakt, abstrakt.selectedProperty(), (alt, istAbstraktNeu) -> {
						if (istAbstraktNeu.booleanValue()) {
							getTableRow().getItem().setzeStatisch(false);
						}
						eingabeValidierung.revalidate();
					});
				} else {
					eingabeValidierung.registerValidator(abstrakt, false, ignorierenValidierung());
				}
			}
			
			private void updateMethode(Methode methode, KlassifiziererTyp typ) {
				boolean abstraktErlaubt = klassifizierer.getProgrammiersprache().getEigenschaften()
						.erlaubtAbstrakteMethode(typ);
				boolean abstraktErzwungen = !klassifizierer.getProgrammiersprache().getEigenschaften()
						.erlaubtNichtAbstrakteMethode(typ);
				
				abstrakt.setDisable(!abstraktErlaubt);
				
				if (abstraktErzwungen) {
					abstrakt.setDisable(true);
				}
				
				boolean instanzAttributeErlaubt = klassifizierer.getProgrammiersprache().getEigenschaften()
						.erlaubtInstanzAttribute(typ);
				if (!instanzAttributeErlaubt && (methode.istGetter() || methode.istSetter())) {
					// Getter und Setter werden über Attribut gesteuert
					abstrakt.setDisable(true);
				}
				
			}
			
		});
		
		return spalte;
	}
	
	private TableColumn<Methode, Boolean> erstelleStatischSpalte(ReadOnlyStringProperty titel) {
		TableColumn<Methode, Boolean> spalte = new TableColumn<>();
		spalte.textProperty().bind(titel);
		spalte.setCellValueFactory(daten -> daten.getValue().istStatischProperty());
		
		spalte.setCellFactory(s -> new TableCell<Methode, Boolean>() {
			private CheckBox statisch = new CheckBox();
			
			{
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				setGraphic(statisch);
			}
			
			@Override
			protected void updateItem(Boolean istStatisch, boolean istLeer) {
				super.updateItem(istStatisch, istLeer);
				setText(null);
				NodeUtil.entferneSchwacheBeobachtung(statisch);
				statisch.setVisible(istStatisch != null && !istLeer);
				if (getTableRow() == null || getTableRow().getItem() == null) {
					eingabeValidierung.registerValidator(statisch, false, ignorierenValidierung());
					return;
				}
				
				if (!istLeer && istStatisch != null) {
					statisch.setSelected(istStatisch);
					bindeBidirektional(statisch.selectedProperty(), getTableRow().getItem().istStatischProperty(),
							statisch);
					NodeUtil.beobachteSchwach(statisch, statisch.selectedProperty(), (alt, istStatischNeu) -> {
						if (istStatischNeu.booleanValue()) {
							getTableRow().getItem().setzeAbstrakt(false);
						}
					});
					updateMethode(getTableRow().getItem(), klassifizierer.getTyp());
					NodeUtil.beobachteSchwach(statisch, klassifizierer.typProperty(),
							typ -> updateMethode(getTableRow().getItem(), typ));
				} else {
					eingabeValidierung.registerValidator(statisch, false, ignorierenValidierung());
				}
			}
			
			private void updateMethode(Methode methode, KlassifiziererTyp typ) {
				boolean instanzAttributeErlaubt = klassifizierer.getProgrammiersprache().getEigenschaften()
						.erlaubtInstanzAttribute(typ);
				if (!instanzAttributeErlaubt && (methode.istGetter() || methode.istSetter())) {
					// Getter und Setter werden über Attribut gesteuert
					statisch.setDisable(true);
				}
				
			}
		});
		
		return spalte;
	}
	
	private TableColumn<Methode, Boolean> erstelleFinalSpalte(ReadOnlyStringProperty titel) {
		TableColumn<Methode, Boolean> spalte = new TableColumn<>();
		spalte.textProperty().bind(titel);
		spalte.setCellValueFactory(daten -> daten.getValue().istFinalProperty());
		
		spalte.setCellFactory(s -> new TableCell<Methode, Boolean>() {
			private CheckBox istFinal = new CheckBox();
			
			{
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				setGraphic(istFinal);
			}
			
			@Override
			protected void updateItem(Boolean finalGewaehlt, boolean istLeer) {
				super.updateItem(finalGewaehlt, istLeer);
				setText(null);
				NodeUtil.entferneSchwacheBeobachtung(istFinal);
				istFinal.setVisible(finalGewaehlt != null && !istLeer);
				if (getTableRow() == null || getTableRow().getItem() == null) {
					eingabeValidierung.registerValidator(istFinal, false, ignorierenValidierung());
					return;
				}
				
				if (!istLeer && finalGewaehlt != null) {
					istFinal.setSelected(getTableRow().getItem().istFinal());
					bindeBidirektional(istFinal.selectedProperty(), getTableRow().getItem().istFinalProperty(), istFinal);
					NodeUtil.beobachteSchwach(istFinal, istFinal.selectedProperty(), () -> eingabeValidierung.revalidate());
					Platform.runLater(() -> {
						eingabeValidierung.registerValidator(istFinal, Validator.createPredicateValidator(c -> {
							return !(getTableRow().getItem().istAbstrakt() && istFinal.isSelected());
						}, sprache.getText("abstraktFinalValidierung", "Eine abstrakte Methode darf nicht final sein")));
						
						setzePlatzhalter(istFinal);
					});
				} else {
					eingabeValidierung.registerValidator(istFinal, false, ignorierenValidierung());
				}
			}
			
		});
		
		return spalte;
	}
	
	private TableColumn<Methode, Methode> erstelleKontrollSpalte() {
		TableColumn<Methode, Methode> spalte = new TableColumn<>();
		spalte.setCellValueFactory(daten -> new ObservableValueBase<Methode>() {
			@Override
			public Methode getValue() {
				return daten.getValue();
			}
		});
		
		spalte.setCellFactory(s -> new TableCell<Methode, Methode>() {
			private KontrollElemente<Methode> kontrollElemente = new KontrollElemente<>(s.getTableView().getItems(),
					this.itemProperty(), this.indexProperty());
			
			{
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				setGraphic(new HBox(10, kontrollElemente.getHoch(), kontrollElemente.getRunter(),
						kontrollElemente.getLoeschen()));
			}
			
			@Override
			protected void updateItem(Methode m, boolean istLeer) {
				super.updateItem(m, istLeer);
				setText(null);
				getGraphic().setVisible(m != null && !istLeer);
				if (getTableRow() == null || getTableRow().getItem() == null) {
					return;
				}
				
				if (!istLeer && m != null) {
					var methode = getTableRow().getItem();
					if (methode.istGetter() || methode.istSetter()) {
						kontrollElemente.getLoeschen().setDisable(true);
					} else {
						kontrollElemente.getLoeschen().setDisable(false);
					}
				}
			}
		});
		
		return spalte;
	}
	
	private <T> void bindeBidirektional(Property<T> p1, Property<T> p2, Node n) {
		NodeUtil.beobachteSchwach(n, p1, (alterWert, neuerWert) -> {
			if (!Objects.equals(alterWert, neuerWert)) {
				p2.setValue(neuerWert);
			}
		});
		NodeUtil.beobachteSchwach(n, p2, (alterWert, neuerWert) -> {
			if (!Objects.equals(alterWert, neuerWert)) {
				p1.setValue(neuerWert);
			}
		});
	}
	
	private void setzePlatzhalter(Control eingabefeld) {
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
	}
	
	private void setzePlatzhalter(SearchField<?> eingabefeld) {
		ChangeListener<ValidationResult> beobachter = (p, alt, neu) -> {
			var fehler = eingabeValidierung.getHighestMessage(eingabefeld.getEditor());
			if (fehler.isPresent()) {
				eingabefeld.getEditor().setPromptText(fehler.get().getText());
				if (!eingabefeld.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
					eingabefeld.getStyleClass().add(CSS_EINGABE_FEHLER);
				}
				eingabefeld.setTooltip(new Tooltip(fehler.get().getText()));
			} else {
				eingabefeld.getEditor().setPromptText(null);
				if (eingabefeld.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
					eingabefeld.getStyleClass().remove(CSS_EINGABE_FEHLER);
				}
				eingabefeld.setTooltip(null);
			}
		};
		eingabeValidierung.validationResultProperty().addListener(beobachter);
	}
	
}