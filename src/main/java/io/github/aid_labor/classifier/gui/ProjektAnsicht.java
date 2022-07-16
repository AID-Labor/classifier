/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dlsc.gemsfx.DialogPane;

import io.github.aid_labor.classifier.basis.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.system.OS;
import io.github.aid_labor.classifier.basis.projekt.UeberwachungsStatus;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.gui.komponenten.UMLElementBasisAnsicht;
import io.github.aid_labor.classifier.gui.komponenten.UMLKlassifiziererAnsicht;
import io.github.aid_labor.classifier.gui.komponenten.UMLKommentarAnsicht;
import io.github.aid_labor.classifier.gui.komponenten.UMLVerbindungAnsicht;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKommentar;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindung;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.When;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;


public class ProjektAnsicht extends Tab {
	private static final Logger log = Logger.getLogger(ProjektAnsicht.class.getName());
	private static String UNGESPEICHERT_CSS_CLASS = "tab-ungespeichert";
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private UMLProjekt projekt;
	private final ProjektKontrolle controller;
	private final DialogPane overlayDialog;
	private final ProgrammDetails programm;
	private final Pane zeichenflaeche;
	private final Group zeichenflaecheGruppe;
	private final Map<Long, Node> ansichten;
	private final Map<Long, Node> verbindungsAnsichten;
	private final ObservableList<UMLElementBasisAnsicht<? extends UMLDiagrammElement>> selektion;
	private final Sprache sprache;
	private final ScrollPane inhalt;
	private final ReadOnlyBooleanWrapper kannKleinerZoomen;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ProjektAnsicht(UMLProjekt projekt, Sprache sprache, DialogPane overlayDialog, ProgrammDetails programm) {
		this.projekt = projekt;
		this.sprache = sprache;
		this.overlayDialog = overlayDialog;
		this.programm = programm;
		this.controller = new ProjektKontrolle(this, sprache);
		this.zeichenflaeche = new Pane();
		this.ansichten = new HashMap<>();
		this.verbindungsAnsichten = new HashMap<>();
		this.selektion = FXCollections.observableArrayList();
		zeichenflaecheGruppe = new Group(zeichenflaeche);
		this.inhalt = new ScrollPane(zeichenflaecheGruppe);
		this.kannKleinerZoomen = new ReadOnlyBooleanWrapper(false);
		this.kannKleinerZoomen.bind(zeichenflaeche.scaleXProperty().greaterThan(0.6));
		
		this.zeichenflaeche.getStyleClass().add("zeichenflaeche");
		this.inhalt.getStyleClass().add("projekt-inhalt");
		zeichenflaeche.minHeightProperty().bind(inhalt.heightProperty());
		zeichenflaeche.minWidthProperty().bind(inhalt.widthProperty());
		
		this.setContent(inhalt);
		
		initialisiereInhalt();
		initialisiereProjekt();
		ueberwacheSelektion();
		
		this.textProperty().bind(
				new When(projekt.istGespeichertProperty()).then("").otherwise("*").concat(projekt.nameProperty()));
		
		this.setOnCloseRequest(this.controller::checkSchliessen);
		
		this.getContent().addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
			if (e.getTarget().equals(inhalt) || e.getTarget().equals(zeichenflaeche)) {
				selektion.clear();
			}
		});
		
		fuegeAlleHinzu(projekt.getDiagrammElemente(), 0);
		fuegeAlleHinzu(projekt.getVerbindungen());
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public UMLProjekt getProjekt() {
		return projekt;
	}
	
	public void skaliere(double skalierung) {
		this.zeichenflaeche.setScaleX(skalierung);
	}
	
	public double getSkalierung() {
		return this.zeichenflaeche.getScaleX();
	}
	
	public double getStandardSkalierung() {
		return 1;
	}
	
	public ReadOnlyBooleanProperty kannKleinerZoomenProperty() {
		return kannKleinerZoomen.getReadOnlyProperty();
	}
	
	public List<? extends UMLDiagrammElement> getSelektion() {
		return selektion.stream().map(UMLElementBasisAnsicht::getUmlElement).toList();
	}
	
	public boolean hatSelektion() {
		return !selektion.isEmpty();
	}
	
	public BooleanBinding hatSelektionProperty() {
		return Bindings.isEmpty(selektion);
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	protected DialogPane getOverlayDialog() {
		return this.overlayDialog;
	}
	
	protected ProgrammDetails getProgrammDetails() {
		return this.programm;
	}
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public boolean projektSpeichern() {
		return this.controller.projektSpeichern(this.projekt);
	}
	
	public boolean projektSpeichernUnter() {
		return this.controller.projektSpeichernUnter(this.projekt);
	}
	
	public void entferneAuswahl() {
		var elemente = this.getSelektion();
		log.fine(() -> "[%s] entferne %s".formatted(projekt, Arrays.toString(elemente.toArray())));
		var entfernenIds = elemente.stream().map(e -> e.getId()).toList();
		var status = this.projekt.getUeberwachungsStatus();
		this.projekt.setUeberwachungsStatus(UeberwachungsStatus.ZUSAMMENFASSEN);
		this.projekt.getDiagrammElemente().removeIf(element -> entfernenIds.contains(element.getId()));
		this.projekt.uebernehmeEditierungen();
		this.projekt.setUeberwachungsStatus(status);
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	void schliesse() {
		try {
			projekt.close();
		} catch (Exception e1) {
			log.log(Level.WARNING, e1, () -> "Fehler beim Schliessen des Projektes");
		}
		projekt = null;
		selektion.clear();
		ansichten.clear();
	}
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void initialisiereInhalt() {
		this.zeichenflaeche.getStyleClass().add("zeichenflaeche");
		this.zeichenflaeche.scaleYProperty().bind(zeichenflaeche.scaleXProperty());
	}
	
	private void initialisiereProjekt() {
		if (!projekt.istGespeichertProperty().get()) {
			this.getStyleClass().add(UNGESPEICHERT_CSS_CLASS);
		}
		
		projekt.istGespeichertProperty().addListener((property, alterWert, istGespeichert) -> {
			if (!istGespeichert) {
				this.getStyleClass().add(UNGESPEICHERT_CSS_CLASS);
			} else {
				this.getStyleClass().remove(UNGESPEICHERT_CSS_CLASS);
			}
		});
		
		projekt.getDiagrammElemente().addListener((Change<? extends UMLDiagrammElement> aenderung) -> {
			this.ueberwacheDiagrammElemente(aenderung);
		});
		projekt.getVerbindungen().addListener((Change<? extends UMLVerbindung> aenderung) -> {
			this.ueberwacheVerbindungen(aenderung);
		});
	}
	
	private void ueberwacheSelektion() {
		selektion.addListener((Change<? extends Node> aenderung) -> {
			log.finer(() -> "Selektion geandert: ");
			while (aenderung.next()) {
				if (aenderung.wasRemoved()) {
					log.finer(() -> "deselektiert: " + Arrays.toString(aenderung.getRemoved().toArray()));
					for (Node n : aenderung.getRemoved()) {
						n.setId("");
					}
				}
				if (aenderung.wasAdded()) {
					log.finer(() -> "selektiert: " + Arrays.toString(aenderung.getAddedSubList().toArray()));
					for (Node n : aenderung.getAddedSubList()) {
						n.setId("selektiert");
					}
				}
			}
		});
	}
	
	private long idZaehler = 0;
	
	private <E extends UMLDiagrammElement> void ueberwacheDiagrammElemente(Change<E> aenderung) {
		while (aenderung.next()) {
			if (aenderung.wasPermutated()) {
				var kopie = zeichenflaeche.getChildren().toArray(new Node[zeichenflaeche.getChildren().size()]);
				for (int i = aenderung.getFrom(); i < aenderung.getTo(); i++) {
					zeichenflaeche.getChildren().set(aenderung.getPermutation(i), kopie[i]);
				}
			}
			if (aenderung.wasRemoved()) {
				for (UMLDiagrammElement entfernt : aenderung.getRemoved()) {
					var ansicht = this.ansichten.remove(entfernt.getId());
					this.zeichenflaeche.getChildren().remove(ansicht);
				}
			}
			if (aenderung.wasAdded()) {
				fuegeAlleHinzu(aenderung.getAddedSubList(), aenderung.getFrom());
			}
		}
	}
	
	private <E extends UMLDiagrammElement> void fuegeAlleHinzu(List<E> elemente, int index) {
		for (var neu : elemente) {
			UMLElementBasisAnsicht<? extends UMLDiagrammElement> ansicht = null;
			if (neu instanceof UMLKlassifizierer klassifizierer) {
				ansicht = new UMLKlassifiziererAnsicht(klassifizierer);
			} else if (neu instanceof UMLKommentar kommentar) {
				ansicht = new UMLKommentarAnsicht(kommentar);
			} else {
				log.severe(() -> "unbekannter Typ: " + neu.getClass().getName());
				continue;
			}
			
			fuegeHinzu(ansicht, index);
			index++;
		}
	}
	
	private void fuegeHinzu(UMLElementBasisAnsicht<? extends UMLDiagrammElement> ansicht, int index) {
		this.zeichenflaeche.getChildren().add(index, ansicht);
		ansicht.getUmlElement().setId(idZaehler);
		this.ansichten.put(idZaehler, ansicht);
		idZaehler++;
		
		ansicht.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
			boolean multiSelektion = OS.getDefault().istMacOS() && e.isMetaDown()
					|| !OS.getDefault().istMacOS() && e.isControlDown();
			if (!multiSelektion) {
				selektion.clear();
			}
		});
		
		ansicht.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
			boolean multiSelektion = OS.getDefault().istMacOS() && e.isMetaDown()
					|| !OS.getDefault().istMacOS() && e.isControlDown();
			if (!multiSelektion) {
				selektion.clear();
			}
			selektion.add(ansicht);
			e.consume();
		});
		
		if (ansicht.getUmlElement() instanceof UMLKlassifizierer klassifizierer) {
			bearbeitenDialogOeffnen(ansicht, klassifizierer,
					projekt.nameProperty().concat(" > ").concat(new When(klassifizierer.nameProperty().isEmpty())
							.then(sprache.getText("unbenannt", "Unbenannt")).otherwise(klassifizierer.nameProperty())),
					() -> new UMLKlassifiziererBearbeitenDialog(klassifizierer, projekt));
		} else if (ansicht.getUmlElement() instanceof UMLKommentar kommentar) {
			bearbeitenDialogOeffnen(ansicht, kommentar,
					projekt.nameProperty().concat(" > ")
							.concat(sprache.getTextProperty("kommentarBearbeitenTitel", "Kommentar bearbeiten")),
					() -> new UMLKommentarBearbeitenDialog(kommentar));
		}
		
		Runnable vorPositionBearbeitung = () -> {
			projekt.setUeberwachungsStatus(UeberwachungsStatus.ZUSAMMENFASSEN);
		};
		
		BiConsumer<Bounds, Bounds> nachPositionBearbeitung = (start, ende) -> {
			if (start.equals(ende)) {
				projekt.verwerfeEditierungen();
			} else {
				projekt.uebernehmeEditierungen();
			}
			projekt.setUeberwachungsStatus(UeberwachungsStatus.INKREMENTELL_SAMMELN);
			updateZeichenflaechenGroesse();
		};
		
		NodeUtil.macheGroessenVeraenderlich(ansicht, vorPositionBearbeitung, nachPositionBearbeitung);
		NodeUtil.macheBeweglich(ansicht, vorPositionBearbeitung, nachPositionBearbeitung);
		updateZeichenflaechenGroesse();
	}
	
	private <T extends UMLDiagrammElement> void bearbeitenDialogOeffnen(Node ansicht, T element,
			ObservableValue<String> titel, Supplier<? extends Alert> dialogKonstruktor) {
		ansicht.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			if (event.getClickCount() == 2 && !event.isConsumed()) {
				event.consume();
				var alterStatus = projekt.getUeberwachungsStatus();
				projekt.setUeberwachungsStatus(UeberwachungsStatus.ZUSAMMENFASSEN);
				Alert dialog = dialogKonstruktor.get();
				dialog.initOwner(ansicht.getScene().getWindow());
				dialog.titleProperty().bind(titel);
				dialog.showAndWait().ifPresent(button -> {
					switch (button.getButtonData()) {
						case BACK_PREVIOUS -> {
							log.fine(() -> "Setze zurueck " + element);
							projekt.verwerfeEditierungen();
						}
						case FINISH -> {
							log.fine(() -> "Aenderungen an " + element + " uebernommen");
							projekt.uebernehmeEditierungen();
						}
						default -> log.severe(() -> "Unbekannter Buttontyp: " + button);
					}
					projekt.setUeberwachungsStatus(alterStatus);
				});
			}
		});
	}
	
	private void updateZeichenflaechenGroesse() {
		var maxX = projekt.getDiagrammElemente().stream()
				.mapToDouble(element -> element.getPosition().getX() + element.getPosition().getBreite()).max();
		var maxY = projekt.getDiagrammElemente().stream()
				.mapToDouble(element -> element.getPosition().getY() + element.getPosition().getHoehe()).max();
		this.zeichenflaeche.setPrefSize(maxX.orElse(100) + 100, maxY.orElse(100) + 100);
	}
	
	private void ueberwacheVerbindungen(Change<? extends UMLVerbindung> aenderung) {
		while (aenderung.next()) {
			if (aenderung.wasRemoved()) {
				for (UMLVerbindung entfernt : aenderung.getRemoved()) {
					var ansicht = this.verbindungsAnsichten.remove(entfernt.getId());
					this.zeichenflaeche.getChildren().remove(ansicht);
				}
			}
			if (aenderung.wasAdded()) {
				fuegeAlleHinzu(aenderung.getAddedSubList());
			}
		}
	}
	
	private void fuegeAlleHinzu(List<? extends UMLVerbindung> verbindungen) {
		for (var verbindung : verbindungen) {
			UMLVerbindungAnsicht ansicht = new UMLVerbindungAnsicht(verbindung, projekt);
			ansicht.setViewOrder(Double.MAX_VALUE);
			this.verbindungsAnsichten.put(verbindung.getId(), ansicht);
			this.zeichenflaeche.getChildren().add(ansicht);
			System.out.println("Verbindung " + verbindung + " hinzugefügt");
		}
	}
	
}