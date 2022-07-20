/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.projekt.ProjektBasis;
import io.github.aid_labor.classifier.basis.projekt.UeberwachungsStatus;
import io.github.aid_labor.classifier.basis.projekt.editierung.ListenEditierUeberwacher;
import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindung;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindungstyp;
import io.github.aid_labor.classifier.uml.programmierung.Programmiersprache;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;


/**
 * Projektverwaltung für ein UML-Klassendiagramm
 * 
 * @author Tim Muehle
 *
 */

public class UMLProjekt extends ProjektBasis {
	private static final Logger log = Logger.getLogger(UMLProjekt.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Laedt eine Projektdatei (im json-Format) und rekonstruiert das gespeicherte Projekt.
	 * 
	 * @param datei Datei mit gespeichertem Projekt im json-Format
	 * @return gespeichertes Projekt als neue Instanz der spezifizierten Klasse
	 * @throws IOException
	 */
	public static UMLProjekt ausDateiOeffnen(Path datei) throws IOException {
		return ProjektBasis.ausDateiOeffnen(datei, UMLProjekt.class);
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
	
	private Programmiersprache programmiersprache;
	
	// -------------------------------------------------------------------------------------
	// final
	@JsonSerialize(typing = Typing.DYNAMIC)
	private final ObservableList<UMLDiagrammElement> diagrammElemente;
	private final ObservableList<UMLVerbindung> verbindungen;
	@JsonIgnore
	private final Map<String, ChangeListener<String>> superklassenBeobachter = new HashMap<>();
	@JsonIgnore
	private final Map<String, ListChangeListener<String>> interfaceBeobachter = new HashMap<>();
	@JsonIgnore
	private final Map<String, ChangeListener<String>> namenBeobachter = new HashMap<>();
	@JsonIgnore
	private final ListChangeListener<UMLDiagrammElement> elementeBeobachter;
	@JsonIgnore
	private final ListChangeListener<UMLVerbindung> verbindungenBeobachter;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Hauptkonstruktor. Anwendungen sollten nur diesen Konstruktor nutzen, um ein neues
	 * Projekt zu erzeugen. Zum oeffnen eines vorhandenen Projektes steht die Fabrikmethode
	 * {@link #ausDateiOeffnen(Path)} zur Verfuegung.
	 * 
	 * @param name                 Name des Projektes
	 * @param programmiersprache   genutzte Programmiersprache (wird fuer sprachspezifische
	 *                             Eigenschaften benoetigt)
	 * @param automatischSpeichern {@code true}, wenn dises Projekt regelmaessig im
	 *                             Hintergrund gespeichert werden soll, sonst {@code false}
	 * @throws NullPointerException falls einer der Parameter {@code null} ist
	 */
	public UMLProjekt(String name, Programmiersprache programmiersprache, boolean automatischSpeichern)
			throws NullPointerException {
		super(name, automatischSpeichern);
		this.programmiersprache = Objects.requireNonNull(programmiersprache);
		this.diagrammElemente = FXCollections.observableArrayList(e -> {
			if (e instanceof UMLKlassifizierer k) {
				return new Observable[] { k.nameProperty(), k.typProperty() };	// Liste bei Namensaenderung updaten
			} else {
				return new Observable[0];
			}
		});
		this.verbindungen = FXCollections.observableArrayList();
		
		// Diagramm-Elemente auf Aenderung Ueberwachen (sowhol die Liste als auch geaenderte
		// Objekte in der Liste)
		this.diagrammElemente.addListener(new ListenEditierUeberwacher<>(this.diagrammElemente, this));
		this.verbindungen.addListener(new ListenEditierUeberwacher<>(this.verbindungen, this));
		elementeBeobachter = this::ueberwacheDiagrammElemente;
		this.diagrammElemente.addListener(new WeakListChangeListener<>(elementeBeobachter));
		verbindungenBeobachter = this::ueberwacheVerbindungen;
		this.verbindungen.addListener(new WeakListChangeListener<>(verbindungenBeobachter));
	}
	
	/**
	 * Konstruktor, den jackson verwendet, um eine Instanz aus einer json-Datei zu erzeugen
	 * 
	 * @param name                 JsonProperty "name"
	 * @param programmiersprache   JsonProperty "programmiersprache"
	 * @param automatischSpeichern JsonProperty "automatischSpeichern"
	 * @param diagrammElemente     JsonProperty "diagrammElemente"
	 * @param verbindungen         JsonProperty "verbindungen"
	 */
	@JsonCreator
	protected UMLProjekt(@JsonProperty("name") String name,
			@JsonProperty("programmiersprache") Programmiersprache programmiersprache,
			@JsonProperty("automatischSpeichern") boolean automatischSpeichern,
			@JsonProperty("diagrammElemente") List<UMLDiagrammElement> diagrammElemente,
			@JsonProperty("verbindungen") List<UMLVerbindung> verbindungen) {
		this(name, programmiersprache, automatischSpeichern);
		if (verbindungen != null) {	// Support alter Programmdateien ohne Verbindungen
			this.verbindungen.addAll(verbindungen);
		}
		this.diagrammElemente.addAll(diagrammElemente);
		this.istGespeichertProperty.set(true);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * Eingestellte Programmiersprache
	 * 
	 * @return eingestellte Programmiersprache
	 */
	public Programmiersprache getProgrammiersprache() {
		return this.programmiersprache;
	}
	
	/**
	 * Liste, die alle Diagramm-Elemente dieses Projektes beinhaltet. Bei Veraenderung dieser
	 * Liste wird dieses Projekt als nicht-gespeichert markiert (siehe
	 * {@link #istGespeichertProperty()}).
	 * 
	 * @return Liste, die alle Diagramm-Elemente dieses Projektes beinhaltet
	 */
	public ObservableList<UMLDiagrammElement> getDiagrammElemente() {
		return this.diagrammElemente;
	}
	
	public ObservableList<UMLVerbindung> getVerbindungen() {
		return verbindungen;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public int hashCode() {
		return ClassifierUtil.hashAlle(automatischSpeichern(), diagrammElemente, verbindungen, getName(),
				programmiersprache, getSpeicherort());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UMLProjekt proj = (UMLProjekt) obj;
		
		boolean automatischSpeichernGleich = automatischSpeichern() == proj.automatischSpeichern();
		boolean diagrammElementeGleich = ClassifierUtil.pruefeGleichheit(this.diagrammElemente, proj.diagrammElemente);
		boolean verbindungenGleich = ClassifierUtil.pruefeGleichheit(this.verbindungen, proj.verbindungen);
		boolean nameGleich = Objects.equals(getName(), proj.getName());
		boolean programmierspracheGleich = getProgrammiersprache().equals(proj.getProgrammiersprache());
		boolean speicherortGleich = Objects.equals(getSpeicherort(), proj.getSpeicherort());
		
		boolean istGleich = automatischSpeichernGleich && diagrammElementeGleich && verbindungenGleich && nameGleich
				&& programmierspracheGleich && speicherortGleich;
		
		log.finest(() -> """
				istGleich: %s
				   |-- automatischSpeichernGleich: %s
				   |-- diagrammElementeGleich: %s
				   |-- verbindungenGleich: %s
				   |-- nameGleich: %s
				   |-- programmierspracheGleich: %s
				   ╰-- speicherortGleich: %s""".formatted(istGleich, automatischSpeichernGleich, diagrammElementeGleich,
				verbindungenGleich, nameGleich, programmierspracheGleich, speicherortGleich));
		
		return istGleich;
	}
	
	@Override
	public void close() throws Exception {
		log.finest(() -> this + " leere diagrammelemente");
		this.setUeberwachungsStatus(UeberwachungsStatus.IGNORIEREN);
		for (var element : getDiagrammElemente()) {
			element.setDarfGeschlossenWerden(true);
			element.close();
		}
		try {
			getDiagrammElemente().clear();
		} catch (Exception e1) {
			// ignore
		}
		super.close();
		
		for (var attribut : this.getClass().getDeclaredFields()) {
			try {
				if (!Modifier.isStatic(attribut.getModifiers())) {
					attribut.setAccessible(true);
					attribut.set(this, null);
				}
			} catch (Exception e) {
				// ignore
			}
		}
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void ueberwacheDiagrammElemente(Change<? extends UMLDiagrammElement> aenderung) {
		while (aenderung.next()) {
			if (aenderung.wasRemoved()) {
				for (UMLDiagrammElement element : aenderung.getRemoved()) {
					System.out.println("#------------");
					if (!(element instanceof UMLKlassifizierer klassifizierer)) {
						continue;
					}
					verbindungen.removeIf(v -> Objects.equals(v.getVerbindungsStart(), klassifizierer.getName()));
					
					var superklassenUeberwacher = superklassenBeobachter.get(klassifizierer.getName());
					if (superklassenUeberwacher != null) {
						klassifizierer.superklasseProperty().removeListener(superklassenUeberwacher);
					}
					
					var interfaceUeberwacher = interfaceBeobachter.get(klassifizierer.getName());
					if (interfaceUeberwacher != null) {
						klassifizierer.getInterfaces().removeListener(interfaceUeberwacher);
					}
					
					var nameUeberwacher = namenBeobachter.get(klassifizierer.getName());
					if (nameUeberwacher != null) {
						klassifizierer.superklasseProperty().removeListener(nameUeberwacher);
					}
					
					element.setDarfGeschlossenWerden(true);
				}
			}
			if (aenderung.wasAdded()) {
				for (UMLDiagrammElement element : aenderung.getAddedSubList()) {
					if (!(element instanceof UMLKlassifizierer klassifizierer)) {
						continue;
					}
					
					verbindungen.removeAll(zuEntfernendeVerbindungen);
					zuEntfernendeVerbindungen.clear();
					
					var superklassenUeberwacher = ueberwacheSuperklasse(klassifizierer);
					superklassenBeobachter.put(klassifizierer.getName(), superklassenUeberwacher);
					
					String superklasse = klassifizierer.getSuperklasse();
					if (superklasse != null && !superklasse.isBlank()) {
						if (verbindungen.stream().filter(v -> Objects.equals(v.getTyp(), UMLVerbindungstyp.VERERBUNG)
								&& Objects.equals(v.getVerbindungsStart(), klassifizierer.getName())
								&& Objects.equals(v.getVerbindungsEnde(), superklasse)).count() < 1) {
							UMLVerbindung vererbung = new UMLVerbindung(UMLVerbindungstyp.VERERBUNG, 
									klassifizierer.getName(), superklasse);
							vererbung.verbindungsStartProperty().bindBidirectional(klassifizierer.nameProperty());
							verbindungen.add(vererbung);
						}
					}
					
					var interfaceUeberwacher = ueberwacheInterfaces(klassifizierer);
					interfaceBeobachter.put(klassifizierer.getName(), interfaceUeberwacher);
					
					for (String interfaceName: klassifizierer.getInterfaces()) {
						if (verbindungen.stream().filter(v -> 
								Objects.equals(v.getTyp(), UMLVerbindungstyp.SCHNITTSTELLEN_VERERBUNG)
								&& Objects.equals(v.getVerbindungsStart(), klassifizierer.getName())
								&& Objects.equals(v.getVerbindungsEnde(), interfaceName)).count() < 1) {
							UMLVerbindung vererbung = new UMLVerbindung(UMLVerbindungstyp.SCHNITTSTELLEN_VERERBUNG,
									klassifizierer.getName(), interfaceName);
							vererbung.verbindungsStartProperty().bindBidirectional(klassifizierer.nameProperty());
							verbindungen.add(vererbung);
						}
					}
					
					verbindungen.removeAll(zuEntfernendeVerbindungen);
					zuEntfernendeVerbindungen.clear();
					
					var nameUeberwacher = ueberwacheName(klassifizierer);
					namenBeobachter.put(klassifizierer.getName(), nameUeberwacher);
					
					element.setDarfGeschlossenWerden(false);
				}
			}
		}
	}
	
	private ChangeListener<String> ueberwacheSuperklasse(UMLKlassifizierer klassifizierer) {
		ChangeListener<String> superklassenUeberwacher = (p, alteSuperklasse, neueSuperklasse) -> {
			if (Objects.equals(neueSuperklasse, alteSuperklasse)) {
				return;
			}
			if (alteSuperklasse != null && (neueSuperklasse == null || neueSuperklasse.isBlank())) {
				verbindungen.removeIf(v -> Objects.equals(v.getTyp(), UMLVerbindungstyp.VERERBUNG)
						&& Objects.equals(alteSuperklasse, v.getVerbindungsEnde())
						&& Objects.equals(klassifizierer.getName(), v.getVerbindungsStart()));
			} else if (alteSuperklasse == null || alteSuperklasse.isBlank()) {
				UMLVerbindung vererbung = new UMLVerbindung(UMLVerbindungstyp.VERERBUNG, klassifizierer.getName(),
						neueSuperklasse);
				vererbung.verbindungsStartProperty().bindBidirectional(klassifizierer.nameProperty());
				verbindungen.add(vererbung);
			} else {
				verbindungen.stream()
						.filter(v -> Objects.equals(v.getTyp(), UMLVerbindungstyp.VERERBUNG)
								&& Objects.equals(v.getVerbindungsStart(), klassifizierer.getName())
								&& Objects.equals(v.getVerbindungsEnde(), alteSuperklasse))
						.forEach(vererbung -> vererbung.setVerbindungsEnde(neueSuperklasse));
			}
		};
		klassifizierer.superklasseProperty().addListener(superklassenUeberwacher);
		return superklassenUeberwacher;
	}
	
	private ListChangeListener<String> ueberwacheInterfaces(UMLKlassifizierer klassifizierer) {
		ListChangeListener<String> interfaceUeberwacher = (Change<? extends String> aenderung) -> {
			while (aenderung.next()) {
				for (String interfaceHinzu : aenderung.getAddedSubList()) {
					UMLVerbindung vererbung = new UMLVerbindung(UMLVerbindungstyp.SCHNITTSTELLEN_VERERBUNG,
							klassifizierer.getName(), interfaceHinzu);
					vererbung.verbindungsStartProperty().bindBidirectional(klassifizierer.nameProperty());
					verbindungen.add(vererbung);
				}
				for (String interfaceEntfernt : aenderung.getRemoved()) {
					verbindungen.removeIf(v -> Objects.equals(v.getTyp(), UMLVerbindungstyp.VERERBUNG)
							&& Objects.equals(interfaceEntfernt, v.getVerbindungsEnde())
							&& Objects.equals(klassifizierer.getName(), v.getVerbindungsStart()));
				}
			}
		};
		klassifizierer.getInterfaces().addListener(interfaceUeberwacher);
		return interfaceUeberwacher;
	}
	
	private ChangeListener<String> ueberwacheName(UMLKlassifizierer klassifizierer) {
		ChangeListener<String> nameUeberwacher = (p, alterName, neuerName) -> {
			if (Objects.equals(alterName, neuerName) || diagrammElemente.stream()
					.filter(e -> e instanceof UMLKlassifizierer k && Objects.equals(k.getName(), alterName)
							&& k.getId() != klassifizierer.getId())	// Anderes Element mit gleichem Namen -> Kein
																		// Update!!!
					.count() > 0 || alterName == null || alterName.isBlank()) {
				return;
			}
			
			diagrammElemente.stream().filter(e -> e instanceof UMLKlassifizierer k
					&& k.getId() != klassifizierer.getId() && Objects.equals(k.getSuperklasse(), alterName))
					.forEach(subklasse -> {
						((UMLKlassifizierer) subklasse).setSuperklasse(neuerName);
					});
		};
		klassifizierer.nameProperty().addListener(nameUeberwacher);
		return nameUeberwacher;
	}
	
	private final List<UMLVerbindung> zuEntfernendeVerbindungen = new ArrayList<>();
	private void ueberwacheVerbindungen(Change<? extends UMLVerbindung> aenderung) {
		while (aenderung.next()) {
			if (aenderung.wasRemoved()) {
				for (UMLVerbindung entfernteVerbindung : aenderung.getRemoved()) {
					entfernteVerbindung.setDarfGeschlossenWerden(true);
					entfernteVerbindung.ausgebelendetProperty().unbind();
				}
			}
			if (aenderung.wasAdded()) {
				for (UMLVerbindung neueVerbindung : aenderung.getAddedSubList()) {
					if (neueVerbindung.darfGeschlossenWerden()) {
						zuEntfernendeVerbindungen.add(neueVerbindung);
					} else {
						bindeElementeAnVerbindung(neueVerbindung);
						neueVerbindung.setDarfGeschlossenWerden(false);
					}
				}
			}
		}
	}
	
	private void bindeElementeAnVerbindung(UMLVerbindung verbindung) {
		switch (verbindung.getTyp()) {
			case SCHNITTSTELLEN_VERERBUNG ->
				verbindung.endElementProperty().bind(sucheInterface(verbindung.verbindungsEndeProperty()));
			default -> verbindung.endElementProperty().bind(sucheKlassifizierer(verbindung.verbindungsEndeProperty()));
		}
		verbindung.startElementProperty().bind(sucheKlassifizierer(verbindung.verbindungsStartProperty()));
	}
	
	private ObjectBinding<UMLKlassifizierer> sucheKlassifizierer(StringProperty name) {
		var startelemente = diagrammElemente.filtered(erzeugeNameVergleich(name.get()));
		startelemente.predicateProperty()
				.bind(Bindings.createObjectBinding(() -> erzeugeNameVergleich(name.get()), name));
		var hilfe = Bindings.valueAt(startelemente, 0);
		return Bindings.createObjectBinding(() -> startelemente.isEmpty() ? null : (UMLKlassifizierer) hilfe.get(),
				hilfe, startelemente);
	}
	
	private Predicate<UMLDiagrammElement> erzeugeNameVergleich(String gesuchterName) {
		return e -> e instanceof UMLKlassifizierer k && !KlassifiziererTyp.Interface.equals(k.getTyp())
				&& Objects.equals(gesuchterName, e.getName());
	}
	
	private ObjectBinding<UMLKlassifizierer> sucheInterface(StringProperty name) {
		var startelemente = diagrammElemente.filtered(erzeugeNameVergleichInterface(name.get()));
		startelemente.predicateProperty()
				.bind(Bindings.createObjectBinding(() -> erzeugeNameVergleichInterface(name.get()), name));
		var hilfe = Bindings.valueAt(startelemente, 0);
		return Bindings.createObjectBinding(() -> startelemente.isEmpty() ? null : (UMLKlassifizierer) hilfe.get(), hilfe, startelemente);
	}
	
	private Predicate<UMLDiagrammElement> erzeugeNameVergleichInterface(String gesuchterName) {
		return e -> e instanceof UMLKlassifizierer k && KlassifiziererTyp.Interface.equals(k.getTyp())
				&& Objects.equals(gesuchterName, e.getName());
	}
	
}