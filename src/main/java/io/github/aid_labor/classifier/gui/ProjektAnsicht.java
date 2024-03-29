/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.lang.ref.WeakReference;
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
import io.github.aid_labor.classifier.gui.ProjekteAnsicht.ExportErgebnis;
import io.github.aid_labor.classifier.gui.komponenten.KlassifiziererKontextMenue;
import io.github.aid_labor.classifier.gui.komponenten.ProjektKontextMenue;
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
import javafx.application.HostServices;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.When;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.event.Event;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
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
	private final Map<Long, UMLElementBasisAnsicht<? extends UMLDiagrammElement>> ansichten;
	private final Map<Long, Node> verbindungsAnsichten;
	private final ObservableList<UMLElementBasisAnsicht<? extends UMLDiagrammElement>> selektion;
	private final Sprache sprache;
	private final ScrollPane inhalt;
	private final ReadOnlyBooleanWrapper kannKleinerZoomen;
	private final WeakReference<ProjekteAnsicht> projekteAnsichtRef;
	private final KlassifiziererKontextMenue klassifiziererKontextMenue;
	private final ProjektKontextMenue projektKontextMenue;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ProjektAnsicht(UMLProjekt projekt, Sprache sprache, DialogPane overlayDialog, ProgrammDetails programm,
			ProjekteAnsicht projekteAnsicht) {
		this.projekt = projekt;
		this.sprache = sprache;
		this.overlayDialog = overlayDialog;
		this.programm = programm;
		this.projekteAnsichtRef = new WeakReference<>(projekteAnsicht);
		this.controller = new ProjektKontrolle(this, sprache);
		this.zeichenflaeche = new Pane();
		this.ansichten = new HashMap<>();
		this.verbindungsAnsichten = new HashMap<>();
		this.selektion = FXCollections.observableArrayList();
		this.zeichenflaecheGruppe = new Group(zeichenflaeche);
		this.inhalt = new ScrollPane(zeichenflaecheGruppe);
		this.kannKleinerZoomen = new ReadOnlyBooleanWrapper(false);
		this.kannKleinerZoomen.bind(zeichenflaeche.scaleXProperty().greaterThan(0.2));
		
		this.zeichenflaeche.getStyleClass().add("zeichenflaeche");
		this.inhalt.getStyleClass().add("projekt-inhalt");
		this.zeichenflaeche.minHeightProperty().bind(inhalt.heightProperty());
		this.zeichenflaeche.minWidthProperty().bind(inhalt.widthProperty());
		this.zeichenflaeche.addEventFilter(DragEvent.ANY, controller::importiereAusDatei);
		this.klassifiziererKontextMenue = new KlassifiziererKontextMenue(this);
		this.projektKontextMenue = new ProjektKontextMenue(this);
		
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
		
		this.getContent().addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode().equals(KeyCode.A) && e.isShortcutDown()) {
				selektion.clear();
				selektion.addAll(ansichten.values());
			} else if (e.getCode().equals(KeyCode.ESCAPE)) {
				selektion.clear();
			}
		});
		
		this.getContent().addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
			if (!e.isConsumed() && e.getButton().equals(MouseButton.SECONDARY)
					&& !(e.getTarget() instanceof UMLElementBasisAnsicht)) {
				projektKontextMenue.show(this.getContent(), e.getScreenX(), e.getScreenY());
			} else {
				projektKontextMenue.hide();
			}
		});
		
		fuegeAlleHinzu(projekt.getDiagrammElemente(), 0);
		fuegeAlleHinzu(projekt.getVererbungen());
		fuegeAlleHinzu(projekt.getAssoziationen());
		
		this.inhalt.setOnZoom(event -> {
			double skalierung = getSkalierung() * event.getZoomFactor();
			if (skalierung >= 0.1) {
				skaliere(skalierung);
			}
		});
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public UMLProjekt getProjekt() {
		return projekt;
	}
	
	public void skaliere(double skalierung) {
	    if (skalierung < 0.1) {
	        skalierung = 0.1;
	    }
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
	
	public ProjekteAnsicht getProjekteAnsicht() {
		return projekteAnsichtRef.get();
	}
	
	public HostServices getRechnerService() {
		return projekteAnsichtRef.get().getRechnerService();
	}
	
	public void exportiereAlsQuellcode(Event event) {
		this.controller.exportiereAlsQuellcode(event);
	}
	
	public void exportiereAlsBild(Event event) {
		this.controller.exportiereAlsBild(event);
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
	
	public ExportErgebnis exportiereAlsQuellcode() throws IllegalStateException, Exception {
		List<UMLKlassifizierer> elemente;
		if (this.hatSelektion()) {
			elemente = this.getSelektion().stream().filter(UMLKlassifizierer.class::isInstance)
					.map(UMLKlassifizierer.class::cast).toList();
		} else {
			elemente = projekt.getDiagrammElemente().stream().filter(UMLKlassifizierer.class::isInstance)
					.map(UMLKlassifizierer.class::cast).toList();
		}
		if (elemente.isEmpty()) {
			throw new IllegalStateException();
		}
		
		return this.controller.exportiereAlsQuellcode(elemente, projekt);
	}
	
	public void importiereAusDatei() {
		this.controller.importiereAusDatei();
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
		projekt.getVererbungen().addListener((Change<? extends UMLVerbindung> aenderung) -> {
			this.ueberwacheVerbindungen(aenderung);
		});
		projekt.getAssoziationen().addListener((Change<? extends UMLVerbindung> aenderung) -> {
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
	
	boolean multiMoved = false;
	private void fuegeHinzu(UMLElementBasisAnsicht<? extends UMLDiagrammElement> ansicht, int index) {
		this.zeichenflaeche.getChildren().add(index, ansicht);
		ansicht.getUmlElement().setId(idZaehler);
		this.ansichten.put(idZaehler, ansicht);
		idZaehler++;
		
		ansicht.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (selektion.size() > 1 && e.getTarget() != ansicht && selektion.contains(ansicht)) {
                    for (var element: selektion) {
                        if (!e.getSource().equals(element)) {
                            NodeUtil.starteBewegung(element, e);
                        }
                    }
                }
			}
		});
		
		ansicht.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            if (selektion.size() > 1 && e.getTarget() != ansicht) {
                for (var element: selektion) {
                    if (!e.getSource().equals(element)) {
                        var eventCopy = e.copyFor(e.getSource(), element);
                        element.fireEvent(eventCopy);
                    }
                }
                multiMoved = true;
            }
        });
		
		ansicht.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				boolean multiSelektion = OS.getDefault().istMacOS() && e.isMetaDown()
						|| !OS.getDefault().istMacOS() && e.isControlDown();
				if (!multiSelektion && !multiMoved) {
					selektion.clear();
				}
				if (selektion.contains(ansicht) && !multiMoved) {
					selektion.remove(ansicht);
				} else {
					selektion.add(ansicht);
				}
				multiMoved = false;
				e.consume();
			}
		});
		
		ansicht.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
			if (e.getButton().equals(MouseButton.SECONDARY)) {
				if (!selektion.contains(ansicht)) {
					selektion.add(ansicht);
				}
				klassifiziererKontextMenue.show(ansicht, e.getScreenX(), e.getScreenY());
				projektKontextMenue.hide();
				e.consume();
			} else {
				klassifiziererKontextMenue.hide();
			}
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
					if (ansicht instanceof UMLVerbindungAnsicht va) {
						String s = va.toString();
						try {
							log.info(() -> "Schliesse " + va);
							va.close();
						} catch (Exception e) {
							log.log(Level.WARNING, e, () -> "Fehler beim Schliessen von " + s);
						}
					}
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
		}
	}
	
}