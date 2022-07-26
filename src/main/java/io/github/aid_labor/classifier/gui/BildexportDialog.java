/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.text.NumberFormat;

import org.controlsfx.control.textfield.CustomTextField;

import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.io.Theme;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;


class BildexportDialog extends Dialog<io.github.aid_labor.classifier.gui.BildexportDialog.ExportParameter> {

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	static class ExportParameter {
		private final Theme farbe;
		private final double skalierung;
		private final Scene snapshotScene;
		private final boolean hintergrundTransparent;
		
		private ExportParameter(Theme farbe, double skalierung, Scene snapshotScene, boolean hintergrundTransparent) {
			this.farbe = farbe;
			this.skalierung = skalierung;
			this.snapshotScene = snapshotScene;
			this.hintergrundTransparent = hintergrundTransparent;
		}
		
		public Theme getFarbe() {
			return farbe;
		}
		
		public double getSkalierung() {
			return skalierung;
		}
		
		public Scene getSnapshotScene() {
			return snapshotScene;
		}
		
		public boolean istHintergrundTransparent() {
			return hintergrundTransparent;
		}
	}
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final ObjectProperty<Theme> farbe;
	private final DoubleProperty skalierung;
	private final Node vorschau;
	private final Sprache sprache;
	private final BooleanProperty hintergrundTransparentProperty;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	BildexportDialog(Node vorschau) {
		this.farbe = new SimpleObjectProperty<>(Einstellungen.getBenutzerdefiniert().exportThemeProperty().get());
		this.skalierung = new SimpleDoubleProperty(
				Einstellungen.getBenutzerdefiniert().exportSkalierungProperty().get());
		this.vorschau = vorschau;
		this.hintergrundTransparentProperty = new SimpleBooleanProperty();
		vorschau.setScaleX(1);
		vorschau.setScaleY(1);
		this.sprache = new Sprache();
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
				"BildexportDialog");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		erstelleDialog();
		setzeErgebnisKonverter();
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
	
	private void erstelleDialog() {
		ComboBox<Theme> farbeAuswahl = erstelleFarbWahl();
		ComboBox<Double> skalierungAuswahl = erstelleSkalierungWahl();
		CustomTextField breiteEingabe = erstelleBreiteEingabe();
		CustomTextField hoeheEingabe = erstelleHoeheEingabe();
		CheckBox hintergrundTransparent = new CheckBox();
		this.hintergrundTransparentProperty.bind(hintergrundTransparent.selectedProperty());
		hintergrundTransparent.setSelected(Einstellungen.getBenutzerdefiniert().exportTransparentProperty().get());
		ScrollPane vorschauBereich = new ScrollPane(new StackPane(vorschau));
		vorschauBereich.getStyleClass().add("vorschau");
		int maxBreite = 800;
		int maxHoehe = 700;
		vorschauBereich.setMaxSize(maxBreite, maxHoehe);
		
		NodeUtil.setzeBreite(150, farbeAuswahl, skalierungAuswahl, breiteEingabe, hoeheEingabe);
		
		GridPane tabelle = new GridPane();
		erstelleLabels(tabelle);
		
		tabelle.add(farbeAuswahl, 1, 0);
		tabelle.add(skalierungAuswahl, 1, 1);
		tabelle.add(breiteEingabe, 1, 2);
		tabelle.add(hoeheEingabe, 1, 3);
		tabelle.add(hintergrundTransparent, 1, 4);
		tabelle.add(vorschauBereich, 1, 5);
		
		tabelle.setHgap(20);
		tabelle.setVgap(10);
		tabelle.setPadding(new Insets(30));
		this.getDialogPane().setContent(tabelle);
	}
	
	private ComboBox<Theme> erstelleFarbWahl() {
		ComboBox<Theme> farbeAuswahl = new ComboBox<>();
		farbeAuswahl.setConverter(new StringConverter<Theme>() {
			@Override
			public String toString(Theme farbe) {
				if (farbe == null) {
					return "";
				}
				return sprache.getText(farbe.name(), farbe.name().toLowerCase());
			}
			
			@Override
			public Theme fromString(String string) {
				return Theme.valueOf(string);
			}
		});
		farbeAuswahl.getItems().addAll(Theme.LIGHT, Theme.DARK);
		farbeAuswahl.getSelectionModel().select(Einstellungen.getBenutzerdefiniert().exportThemeProperty().get());
		farbe.bind(farbeAuswahl.getSelectionModel().selectedItemProperty());
		
		return farbeAuswahl;
	}
	
	private ComboBox<Double> erstelleSkalierungWahl() {
		ComboBox<Double> skalierungAuswahl = new ComboBox<>();
		skalierungAuswahl.getItems().addAll(1.0, 1.1, 1.15, 1.2, 1.25, 1.3, 1.4, 1.5, 1.75, 2.0, 2.25, 2.50, 2.75, 3.0);
		skalierungAuswahl.setConverter(new StringConverter<Double>() {
			@Override
			public String toString(Double d) {
				d = d == null ? 0.0 : d;
				return String.format("%.2f%%", d * 100);
			}
			
			@Override
			public Double fromString(String string) {
				double d = skalierung.get();
				try {
					int wert = trimmeZuZahl(string);
					return wert / 100.0;
				} catch (Exception e) {
					e.printStackTrace();
					return d;
				}
			}
		});
		skalierungAuswahl.getSelectionModel().select(Double.NaN);
		
		Platform.runLater(() -> skalierungAuswahl.getSelectionModel().select(skalierung.get()));
		
		skalierungAuswahl.getSelectionModel().selectedItemProperty().addListener((p, alt, neu) -> {
			if (neu != null && neu.compareTo(alt) != 0) {
				skalierung.set(neu);
			}
		});
		skalierung.addListener((p, alt, neu) -> {
			if (Double.compare(neu.doubleValue(), alt.doubleValue()) != 0) {
				skalierungAuswahl.getSelectionModel().select(neu.doubleValue());
			}
		});
		skalierungAuswahl.setEditable(true);
		
		return skalierungAuswahl;
	}
	
	private CustomTextField erstelleBreiteEingabe() {
		CustomTextField breiteEingabe = new CustomTextField();
		breiteEingabe.setRight(new Label("px"));
		breiteEingabe.textProperty().bindBidirectional(skalierung, new StringConverter<Number>() {
			@Override
			public String toString(Number object) {
				var bild = vorschau.snapshot(new SnapshotParameters(), null);
				return String.format("%.2f", bild.getWidth() * skalierung.get());
			}
			
			@Override
			public Double fromString(String string) {
				try {
					int i = trimmeZuZahl(string);
					var bild = vorschau.snapshot(new SnapshotParameters(), null);
					return i / bild.getWidth();
				} catch (Exception e) {
					return skalierung.get();
				}
			}
		});
		
		return breiteEingabe;
	}
	
	private CustomTextField erstelleHoeheEingabe() {
		CustomTextField hoeheEingabe = new CustomTextField();
		hoeheEingabe.setRight(new Label("px"));
		hoeheEingabe.textProperty().bindBidirectional(skalierung, new StringConverter<Number>() {
			@Override
			public String toString(Number object) {
				var bild = vorschau.snapshot(new SnapshotParameters(), null);
				return String.format("%.2f", bild.getHeight() * skalierung.get());
			}
			
			@Override
			public Double fromString(String string) {
				try {
					int i = trimmeZuZahl(string);
					var bild = vorschau.snapshot(new SnapshotParameters(), null);
					return i / bild.getHeight();
				} catch (Exception e) {
					return skalierung.get();
				}
			}
		});
		
		return hoeheEingabe;
	}
	
	private NumberFormat parser = NumberFormat.getInstance();
	
	private int trimmeZuZahl(String s) throws Exception {
		if (parser.format(1.2).contains(".")) {
			s = s.replaceAll("[^0-9\\.]", "");
		} else {
			s = s.replaceAll("[^0-9,]", "");
		}
		return parser.parse(s).intValue();
	}
	
	private void erstelleLabels(GridPane tabelle) {
		String[] schluessel = { "farbe", "skalierung", "breite", "hoehe", "hintergrundTransparent", "vorschau" };
		
		int zeile = 0;
		for (String s : schluessel) {
			Label l = new Label();
			l.textProperty()
					.bind(sprache.getTextProperty(s, String.format("%S%s", s.charAt(0), s.substring(1))).concat(":"));
			tabelle.add(l, 0, zeile);
			zeile++;
		}
	}
	
	private void setzeErgebnisKonverter() {
		ButtonType[] buttons = { new ButtonType(sprache.getText("APPLY", "Exportieren"), ButtonData.APPLY),
			new ButtonType(sprache.getText("CANCEL_CLOSE", "Abbrechen"), ButtonData.CANCEL_CLOSE) };
		this.getDialogPane().getButtonTypes().addAll(buttons);
		this.setResultConverter(button -> {
			return switch (button.getButtonData()) {
				case APPLY -> {
					var ergebnis = new ExportParameter(farbe.get(), skalierung.get(), vorschau.getScene(),
							hintergrundTransparentProperty.get());
					yield ergebnis;
				}
				default -> null;
			};
		});
	}
}