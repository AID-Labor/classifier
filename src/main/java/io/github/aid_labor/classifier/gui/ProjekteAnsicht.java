/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.nio.file.Path;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dlsc.gemsfx.DialogPane;

import io.github.aid_labor.classifier.basis.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.projekt.UeberwachungsStatus;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKommentar;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener.Change;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.WindowEvent;


public class ProjekteAnsicht {
	private static final Logger log = Logger.getLogger(ProjekteAnsicht.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final static ObservableList<UMLDiagrammElement> kopiePuffer = FXCollections.observableArrayList();
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public static ObservableList<UMLDiagrammElement> getKopiepuffer() {
		return kopiePuffer;
	}
	
	public static class ExportErgebnis {
		
		public static ExportErgebnis keinErfolg = new ExportErgebnis(false, null);
		
		public static ExportErgebnis mitOrdner(Path ordner) {
			return new ExportErgebnis(true, ordner);
		}
		
		private final boolean erfolg;
		private final Path ordner;
		
		private ExportErgebnis(boolean erfolg, Path ordner) {
			this.erfolg = erfolg;
			this.ordner = ordner;
		}
		
		public boolean warErfolgreich() {
			return erfolg;
		}
		
		public Path getOrdner() {
			return ordner;
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
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private final ReadOnlyObjectWrapper<UMLProjekt> angezeigtesProjekt;
	/**
	 * Set, dass alle geoeffneten Projekte enthaelt. Projekte mit dem gleichen Speicherort
	 * duerfen nicht doppelt enthalten sein!
	 */
	private final ObservableSet<UMLProjekt> projekte;
	private final TabPane tabAnsicht;
	private final DialogPane overlayDialog;
	private final Sprache sprache;
	private final ProgrammDetails programm;
	private final ProjekteKontrolle kontroller;
	private final ReadOnlyObjectWrapper<ProjektAnsicht> anzeige;
	private final HostServices rechnerService;
	
	private EventHandler<WindowEvent> eventAktion = new EventHandler<WindowEvent>() {
		@Override
		public void handle(WindowEvent event) {
			allesSchliessen();
			if (!tabAnsicht.getTabs().isEmpty()) {
				event.consume();
			}
		}
	};
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ProjekteAnsicht(DialogPane overlayDialog, ProgrammDetails programm, HostServices rechnerService) {
		this.projekte = FXCollections.observableSet(new TreeSet<>((p1, p2) -> {
			if (p1.getSpeicherort() == null) {
				return -1;
			} else if (p2.getSpeicherort() == null) {
				return 1;
			} else {
				return p1.getSpeicherort().compareTo(p2.getSpeicherort());
			}
		}));
		this.overlayDialog = overlayDialog;
		this.sprache = new Sprache();
		this.programm = programm;
		this.rechnerService = rechnerService;
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
				"ProjektAnsicht");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		this.angezeigtesProjekt = new ReadOnlyObjectWrapper<>();
		this.anzeige = new ReadOnlyObjectWrapper<>();
		this.tabAnsicht = new TabPane();
		
		setzeListener();
		this.kontroller = new ProjekteKontrolle(this, sprache);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public ReadOnlyObjectProperty<UMLProjekt> angezeigtesProjektProperty() {
		return angezeigtesProjekt.getReadOnlyProperty();
	}
	
	public UMLProjekt getAngezeigtesProjekt() {
		return angezeigtesProjekt.get();
	}
	
	public Parent getAnsicht() {
		return this.tabAnsicht;
	}
	
	public ReadOnlyObjectProperty<ProjektAnsicht> projektAnsichtProperty() {
		return anzeige.getReadOnlyProperty();
	}
	
	public ProjektAnsicht getProjektAnsicht() {
		return anzeige.get();
	}
	
	public HostServices getRechnerService() {
		return rechnerService;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	protected DialogPane getOverlayDialog() {
		return this.overlayDialog;
	}
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public void auswahlKopieren() {
		kopiePuffer.setAll(getProjektAnsicht().getSelektion());
	}
	
	public void auswahlEinfuegen() {
		var klassenSuchBaum = new TreeSet<>(getProjektAnsicht().getProjekt().getDiagrammElemente().stream()
				.filter(UMLKlassifizierer.class::isInstance).map(k -> k.getName()).toList());
		var kopie = kopiePuffer.stream().map(umlElement -> {
			var umlKopie = umlElement.erzeugeTiefeKopie();
			var x = umlKopie.getPosition().getX() + 20;
			var y = umlKopie.getPosition().getY() + 20;
			umlKopie.getPosition().setX(x);
			umlKopie.getPosition().setY(y);
			if (umlKopie instanceof UMLKlassifizierer k && klassenSuchBaum.contains(k.getName())) {
				k.setName(k.getName() + "_kopie");
				int i = 1;
				String name = k.getName();
				while (klassenSuchBaum.contains(k.getName())) {
					k.setName(name + i);
					i++;
				}
				
			}
			if (umlKopie instanceof UMLKlassifizierer k) {
				klassenSuchBaum.add(k.getName());
			}
			return umlKopie;
		}).toList();
		fuegeEin(kopie);
	}
	
	public void auswahlLoeschen() {
		getProjektAnsicht().entferneAuswahl();
	}
	
	public boolean angezeigtesProjektSpeichern() {
		var tab = this.tabAnsicht.getSelectionModel().getSelectedItem();
		if (tab instanceof ProjektAnsicht pa) {
			return pa.projektSpeichern();
		} else {
			return false;
		}
	}
	
	public boolean angezeigtesProjektSpeicherUnter() {
		var tab = this.tabAnsicht.getSelectionModel().getSelectedItem();
		if (tab instanceof ProjektAnsicht pa) {
			return pa.projektSpeichernUnter();
		} else {
			return false;
		}
	}
	
	public void angezeigtesProjektSchliessen() {
		projektSchliessen(this.tabAnsicht.getSelectionModel().getSelectedItem());
	}
	
	public void projektSchliessen(Tab tab) {
		if (tab.isClosable()) {
			if (tab.getOnCloseRequest() != null) {
				var event = new Event(Tab.TAB_CLOSE_REQUEST_EVENT);
				tab.getOnCloseRequest().handle(event);
				if (!event.isConsumed()) {
					this.tabAnsicht.getTabs().remove(tab);
					if (tab.getOnClosed() != null) {
						tab.getOnClosed().handle(new Event(Tab.CLOSED_EVENT));
					}
				}
			} else {
				this.tabAnsicht.getTabs().remove(tab);
				if (tab.getOnClosed() != null) {
					tab.getOnClosed().handle(new Event(Tab.CLOSED_EVENT));
				}
			}
		}
	}
	
	public void allesSchliessen() {
		var iterator = this.tabAnsicht.getTabs().listIterator();
		iterator.forEachRemaining(tab -> {
			if (tab.isClosable()) {
				if (tab.getOnCloseRequest() != null) {
					var event = new Event(Tab.TAB_CLOSE_REQUEST_EVENT);
					tab.getOnCloseRequest().handle(event);
					if (!event.isConsumed()) {
						iterator.remove();
						if (tab.getOnClosed() != null) {
							tab.getOnClosed().handle(new Event(Tab.CLOSED_EVENT));
						}
					}
				} else {
					iterator.remove();
					if (tab.getOnClosed() != null) {
						tab.getOnClosed().handle(new Event(Tab.CLOSED_EVENT));
					}
				}
			}
		});
	}
	
	public void allesSpeichern() {
		for (Tab tab : this.tabAnsicht.getTabs()) {
			if (tab instanceof ProjektAnsicht pa) {
				try {
					pa.projektSpeichern();
				} catch (Exception e) {
					log.log(Level.WARNING, e,
							() -> "Fehler beim Speichern von Projekt %s aufgetreten".formatted(pa.getProjekt()));
				}
			}
		}
	}
	
	/**
	 * Zeigt ein Projekt in einem neuen Tab an.
	 * 
	 * @param projekt anzuzeigendes Projekt
	 * @throws UnsupportedOperationException Wenn ein Projekt mit dem gleichen Speicherort
	 *                                       bereits geoeffnet ist
	 */
	public void zeigeProjekt(UMLProjekt projekt) throws UnsupportedOperationException {
		if (projekt == null) {
			var exc = new NullPointerException("Das uebergebene Projekt darf nicht null sein");
			log.log(Level.SEVERE, exc, () -> "Projekt kann nicht angezeigt werden");
			throw exc;
		}
		log.finest(() -> "Zeige Projekt %s in neuem Tab".formatted(projekt));
		boolean hinzugefuegt = this.projekte.add(projekt);
		
		if (!hinzugefuegt) {
			var exc = new UnsupportedOperationException(("Projekt %s mit dem Speicherort '%s' "
					+ "kann nicht angezeigt werden, da dieses Projekt bereits geoeffnet ist")
					.formatted(projekt.getName(), projekt.getSpeicherort()));
			log.info(() -> "Projekt anzeigen abgebrochen: " + exc.getMessage());
			throw exc;
		}
		
		ProjektAnsicht tab = new ProjektAnsicht(projekt, sprache, overlayDialog, programm, this);
		
		this.tabAnsicht.getTabs().add(tab);
		this.tabAnsicht.getSelectionModel().select(tab);
		
		tab.setOnClosed(e -> this.projekte.remove(projekt));
		
		// Ueberwachung erst nach dem Zeichnen starten
		Platform.runLater(() -> projekt.setUeberwachungsStatus(UeberwachungsStatus.INKREMENTELL_SAMMELN));
	}
	
	public void vorherigerTab() {
		if (this.tabAnsicht.getSelectionModel().getSelectedIndex() > 0) {
			this.tabAnsicht.getSelectionModel().selectPrevious();
		} else {
			this.tabAnsicht.getSelectionModel().selectLast();
		}
	}
	
	public void naechsterTab() {
		if (this.tabAnsicht.getSelectionModel().getSelectedIndex() < this.tabAnsicht.getTabs().size() - 1) {
			this.tabAnsicht.getSelectionModel().selectNext();
		} else {
			this.tabAnsicht.getSelectionModel().selectFirst();
		}
	}
	
	public void legeNeuenKlassifiziererAn(KlassifiziererTyp typ) {
		this.kontroller.legeNeuenKlassifiziererAn(typ);
	}
	
	public void bearbeiteVerbindungen(boolean starteMitAssoziation) {
		this.kontroller.bearbeiteVerbindungen(starteMitAssoziation);
	}
	
	public void legeKommentarAn() {
		this.kontroller.legeKommentarAn();
	}
	
	public void fuegeEin(Iterable<UMLDiagrammElement> elemente) {
		for (var element : elemente) {
			if (element instanceof UMLKlassifizierer klassifizierer) {
				this.kontroller.legeNeuenKlassifiziererAn(klassifizierer);
			} else if (element instanceof UMLKommentar kommentar) {
				this.kontroller.legeKommentarAn(kommentar);
			} else {
				log.severe(() -> "unbekanntes UMLDiagrammelement: " + element);
			}
		}
	}
	
	public ExportErgebnis exportiereAlsQuellcode() throws IllegalStateException, Exception {
		if (getProjektAnsicht() != null) {
			return getProjektAnsicht().exportiereAlsQuellcode();
		} else {
			return ExportErgebnis.keinErfolg;
		}
	}
	
	public void importiereAusDatei() throws Exception {
		if (getProjektAnsicht() != null) {
			getProjektAnsicht().importiereAusDatei();
		}
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void setzeListener() {
		this.tabAnsicht.getSelectionModel().selectedItemProperty().addListener((itemProperty, alteWahl, neueWahl) -> {
			log.finest(() -> "neuer Tab ausgewaehlt: %s".formatted(neueWahl == null ? "null" : neueWahl.getText()));
			if (neueWahl instanceof ProjektAnsicht tab) {
				angezeigtesProjekt.set(tab.getProjekt());
				anzeige.set(tab);
			} else {
				angezeigtesProjekt.set(null);
				anzeige.set(null);
			}
		});
		
		this.angezeigtesProjekt.addListener((property, alt, neu) -> {
			String nameAlt = alt == null ? "null" : alt.getName();
			String nameNeu = neu == null ? "null" : neu.getName();
			log.finest(() -> "aktuelles Projekt geaendert -> alt: %s - neu: %s".formatted(nameAlt, nameNeu));
		});
		
		this.projekte.addListener((Change<?> aenderung) -> {
			log.fine(() -> "%cnderung der angezeigten Projekte > > > > > > > > > > > > > > >".formatted(Umlaute.AE));
			if (aenderung.getElementAdded() != null) {
				log.fine(() -> "Projekt hinzugef%cgt: %s".formatted(Umlaute.ue, aenderung.getElementAdded()));
			}
			if (aenderung.getElementRemoved() != null) {
				log.fine(() -> "Projekt entfernt: %s".formatted(aenderung.getElementRemoved()));
				
			}
			log.fine(() -> "%cnderung der angezeigten Projekte < < < < < < < < < < < < < < <".formatted(Umlaute.AE));
		});
		
		EventType<WindowEvent> eventTyp = WindowEvent.WINDOW_CLOSE_REQUEST;
		tabAnsicht.sceneProperty().addListener((property, alteSzene, neueSzene) -> {
			if (alteSzene != null && alteSzene.getWindow() != null) {
				alteSzene.getWindow().removeEventHandler(eventTyp, eventAktion);
			}
			if (neueSzene != null) {
				if (neueSzene.getWindow() != null) {
					neueSzene.getWindow().addEventHandler(eventTyp, eventAktion);
				}
				neueSzene.windowProperty().addListener((prop, altesFenster, neuesFenster) -> {
					if (altesFenster != null) {
						altesFenster.removeEventHandler(eventTyp, eventAktion);
					}
					if (neuesFenster != null) {
						neuesFenster.addEventHandler(eventTyp, eventAktion);
					}
				});
			}
		});
	}
	
}