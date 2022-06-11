/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.projekt;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.json.JsonReadOnlyBooleanPropertyWrapper;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.json.JsonUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.StringProperty;


/**
 * Basis-Implementierung von {@link Projekt}. Die Speicherfunktion sowie die Verwaltung
 * eines Verlaufs fuer Editierungen, die rueckggaengig gemacht oder wiederholt werden
 * koennen, sind bereits vollstaendig implementiert.
 * <p>
 * Das Speichern ist mit dem Framework Jackson implementiert. Subklassen muessen ggf.
 * weitere Einstellungen fuer die Serialisierung/Deserialisierung vornehmen.
 * Standardmaessig werden ausschliesslich alle Attribute serialisiert, die nicht mit der
 * Annotation {@code @JsonIgnore} gekennzeichnet sind.
 * </p>
 * 
 * <p>
 * Editierungen muessen von Subklassen mit der Methode
 * {@link #verarbeiteEditierung(EditierBefehl)} in den Verlauf eingereiht werden.
 * Die Verarbeitung des Verlaufes erledigt diese Basis-Klasse dann selbststaendig.
 * </p>
 * 
 * <p>
 * Diese Klasse ueberschreibt die Methoden {@link #hashCode()} und {@link #equals(Object)}
 * fuer die Attribute {@code name}, {@code automatischSpeichern} und {@code speicherort}.
 * Subklassen sollten diese Implementierungen ggf. anpassen!
 * </p>
 * 
 * @author Tim Muehle
 *
 */
//@formatter:off
@JsonAutoDetect(
		getterVisibility = Visibility.NONE,
		isGetterVisibility = Visibility.NONE,
		setterVisibility = Visibility.NONE,
		fieldVisibility = Visibility.ANY
)
//@formatter:on
public abstract class ProjektBasis implements Projekt {
	private static final Logger log = Logger.getLogger(ProjektBasis.class.getName());
	
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
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	@JsonIgnore
	protected final JsonReadOnlyBooleanPropertyWrapper istGespeichertProperty;
	@JsonIgnore
	protected final Verlaufspuffer<EditierBefehl> rueckgaengigVerlauf;
	@JsonIgnore
	protected final Verlaufspuffer<EditierBefehl> wiederholenVerlauf;
	@JsonIgnore
	protected SammelEditierung sammelEditierung;
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private JsonStringProperty name;
	private boolean automatischSpeichern;
	
	@JsonIgnore
	private Path speicherort;
	@JsonIgnore
	private ReadOnlyBooleanWrapper kannRueckgaengigGemachtWerdenProperty;
	@JsonIgnore
	private ReadOnlyBooleanWrapper kannWiederholenProperty;
	@JsonIgnore
	private UeberwachungsStatus ueberwachungsStatus;
	@JsonIgnore
	private long gespeicherterHash;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Hauptkonstruktor. Anwendungen sollten nur diesen Konstruktor nutzen, um ein neues
	 * Projekt zu erzeugen. Zum Oeffnen eines vorhandenen Projektes steht die Fabrikmethode
	 * {@link #ausDateiOeffnen(Path)} zur Verfuegung.
	 * 
	 * Zu Beginn ist die Rueckgaengig-Funktion ausgeschaltet. Diese kann mit dem Setzen
	 * eines UberwachungsStatus mit der Methode
	 * {@link #setUeberwachungsStatus(UeberwachungsStatus)} eingeschaltet und zudem mit
	 * den Methoden {@link #uebernehmeEditierungen()} und {@link #verwerfeEditierungen()}
	 * gesteuert werden.
	 * 
	 * @param name                 Name des Projektes
	 * @param programmiersprache   genutzte Programmiersprache (wird fuer sprachspezifische
	 *                             Eigenschaften benoetigt)
	 * @param automatischSpeichern {@code true}, wenn dises Projekt regelmaessig im
	 *                             Hintergrund gespeichert werden soll, sonst {@code false}
	 * @throws NullPointerException falls einer der Parameter {@code null} ist
	 */
	@JsonCreator
	public ProjektBasis(String name, boolean automatischSpeichern)
			throws NullPointerException {
		this.name = new JsonStringProperty(this, "name", Objects.requireNonNull(name));
		this.setUeberwachungsStatus(UeberwachungsStatus.IGNORIEREN);
		this.istGespeichertProperty = new JsonReadOnlyBooleanPropertyWrapper(this,
				"istGespeichert", false);
		this.setAutomatischSpeichern(automatischSpeichern);
		this.rueckgaengigVerlauf = VerketteterVerlauf.synchronisierterVerlauf(
				Einstellungen.getBenutzerdefiniert().verlaufAnzahl.get());
		this.wiederholenVerlauf = VerketteterVerlauf.synchronisierterVerlauf(
				Einstellungen.getBenutzerdefiniert().verlaufAnzahl.get());
		
		this.ueberwachePropertyAenderung(this.name);
		this.istGespeichertProperty.addListener((__, ___, istGespeichert) -> {
			if (istGespeichert) {
				int neuerHash = hashCode();
				log.fine(() -> "Projekt wurde gespeichert. Update hash von "
						+ gespeicherterHash + " zu " + neuerHash);
				gespeicherterHash = neuerHash;
			}
		});
	}
	
	/**
	 * Laedt eine Projektdatei (im json-Format) und rekonstruiert das gespeicherte Projekt.
	 * 
	 * @param datei Datei mit gespeichertem Projekt im json-Format
	 * @param typ   Klassen, die aus der Datei gelesen werden soll
	 * @return gespeichertes Projekt als neue Instanz der spezifizierten Klasse
	 * @throws IOException
	 */
	public static <T extends ProjektBasis> T ausDateiOeffnen(Path datei, Class<T> typ)
			throws IOException {
		try (JsonParser json = JsonUtil.getUTF8JsonParser(datei)) {
			T projekt = json.readValueAs(typ);
			projekt.setSpeicherort(datei);
			projekt.istGespeichertProperty.set(true);
			return projekt;
		}
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getName() {
		return this.name.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setName(String name) {
		this.name.set(Objects.requireNonNull(name));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StringProperty nameProperty() {
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Path getSpeicherort() {
		return this.speicherort;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setSpeicherort(Path speicherort) {
		this.speicherort = speicherort;
		this.istGespeichertProperty.set(false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final ReadOnlyBooleanProperty istGespeichertProperty() {
		return this.istGespeichertProperty.getReadOnlyProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean automatischSpeichern() {
		return this.automatischSpeichern;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setAutomatischSpeichern(boolean automatischSpeichern) {
		this.automatischSpeichern = automatischSpeichern;
		this.istGespeichertProperty.set(false);
		
		log.severe(
				() -> "Die Funktion >>automatisches Speichern<< ist noch nicht implementiert!");
		// TODO automatisches Speichern implementieren
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * {@inheritDoc}
	 * 
	 * @implNote Zum Serialisieren wird das json-Format mit dem Framework Jackson verwendet.
	 */
	@Override
	public final boolean speichern() {
		if (this.speicherort == null) {
			var exception = new IllegalStateException(
					"Vor dem Speichern muss der Speicherort gesetzt werden!");
			log.throwing(UMLProjekt.class.getName(), "speichern", exception);
			throw exception;
		}
		
		boolean erfolg = false;
		
		try (JsonGenerator json = JsonUtil.getUTF8JsonGenerator(speicherort)) {
			json.writePOJO(this);
			json.flush();
			erfolg = true;
			wiederholenVerlauf.leeren();
			updateVerlaufProperties();
		} catch (IOException e) {
			log.log(Level.WARNING, e, () -> "Projekt %s konnte nicht gespeichert werden"
					.formatted(this.getName()));
		}
		
		this.istGespeichertProperty.set(erfolg);
		return erfolg;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean kannRueckgaengigGemachtWerden() {
		return !rueckgaengigVerlauf.istLeer();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final ReadOnlyBooleanProperty kannRueckgaengigGemachtWerdenProperty() {
		if (kannRueckgaengigGemachtWerdenProperty == null) {
			kannRueckgaengigGemachtWerdenProperty = new ReadOnlyBooleanWrapper(
					kannRueckgaengigGemachtWerden());
		}
		return kannRueckgaengigGemachtWerdenProperty.getReadOnlyProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void macheRueckgaengig() {
		if (!rueckgaengigVerlauf.istLeer()) {
			var befehl = rueckgaengigVerlauf.entfernen();
			log.fine(() -> "%s\n    -> mache rueckgaengig {%s}".formatted(this, befehl));
			var status = this.getUeberwachungsStatus();
			this.setUeberwachungsStatus(UeberwachungsStatus.IGNORIEREN);
			befehl.macheRueckgaengig();
			this.setUeberwachungsStatus(status);
			
			wiederholenVerlauf.ablegen(befehl);
			this.istGespeichertProperty.set(gespeicherterHash == this.hashCode());
		}
		log.fine(() -> "Hash: " + this.hashCode() + " ### gespeichert: " + gespeicherterHash);
		updateVerlaufProperties();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean kannWiederholen() {
		return !wiederholenVerlauf.istLeer();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final ReadOnlyBooleanProperty kannWiederholenProperty() {
		if (kannWiederholenProperty == null) {
			kannWiederholenProperty = new ReadOnlyBooleanWrapper(kannWiederholen());
		}
		return kannWiederholenProperty.getReadOnlyProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void wiederhole() {
		if (!wiederholenVerlauf.istLeer()) {
			var befehl = wiederholenVerlauf.entfernen();
			log.fine(() -> "%s\n    -> wiederhole {%s}".formatted(this, befehl));
			var status = this.getUeberwachungsStatus();
			this.setUeberwachungsStatus(UeberwachungsStatus.IGNORIEREN);
			befehl.wiederhole();
			this.setUeberwachungsStatus(status);
			rueckgaengigVerlauf.ablegen(befehl);
			this.istGespeichertProperty.set(gespeicherterHash == this.hashCode());
		}
		updateVerlaufProperties();
		log.fine(() -> "Hash: " + this.hashCode() + " ### gespeichert: " + gespeicherterHash);
	}
	
	/**
	 * Reiht die geschehene Editierung in den Verlauf zum ruekgaengig machen ein und markiert
	 * dieses Projekt als nicht-gespeichert.
	 * 
	 * @see #macheRueckgaengig()
	 * @see #istGespeichertProperty()
	 */
	@Override
	public final void verarbeiteEditierung(EditierBefehl editierung) {
		switch (ueberwachungsStatus) {
			case IGNORIEREN -> {
				log.finer(() -> "[%s] ignoriere Editierung: %s".formatted(ueberwachungsStatus,
						editierung));
			}
			case INKREMENTELL_SAMMELN -> {
				legeEditierungAb(editierung);
			}
			case ZUSAMMENFASSEN -> {
				if (sammelEditierung == null) {
					log.fine(() -> "[%s] erzeuge neue SammelEditierung"
							.formatted(ueberwachungsStatus));
					sammelEditierung = new SammelEditierung();
				}
				sammelEditierung.speicherEditierung(editierung);
			}
			default -> {
				log.warning(() -> "unbekannter UeberwachunsStatus: " + ueberwachungsStatus);
			}
		}
	}
	
	@Override
	public void setUeberwachungsStatus(UeberwachungsStatus status) {
		log.config(() -> "%s Ueberwachungsstatus %s -> %s".formatted(this,
				this.ueberwachungsStatus, status));
		if (!Objects.equals(this.ueberwachungsStatus, status)
				&& Objects.equals(this.ueberwachungsStatus, UeberwachungsStatus.ZUSAMMENFASSEN)
				&& sammelEditierung != null) {
			log.info(() -> "[!] ungespeicherte SammelEditierung wird uebernommen");
			this.uebernehmeEditierungen();
			this.sammelEditierung = null;
		}
		this.ueberwachungsStatus = status;
	}
	
	@Override
	public UeberwachungsStatus getUeberwachungsStatus() {
		return ueberwachungsStatus;
	}
	
	@Override
	public void uebernehmeEditierungen() throws IllegalStateException {
		if (ueberwachungsStatus.equals(UeberwachungsStatus.IGNORIEREN)) {
			throw new IllegalStateException("Bei der Einstellung "
					+ "UeberwachungsStatus.IGNORIEREN kann die Editierung nicht "
					+ "uebernommen werden!");
		}
		if (sammelEditierung != null) {
			legeEditierungAb(sammelEditierung);
			sammelEditierung = null;
		}
	}
	
	@Override
	public void verwerfeEditierungen() throws IllegalStateException {
		if (ueberwachungsStatus.equals(UeberwachungsStatus.INKREMENTELL_SAMMELN)) {
			throw new IllegalStateException("Bei der Einstellung "
					+ "UeberwachungsStatus.INKREMENTELL_SAMMELN kann die Editierung nicht "
					+ "verworfen werden!");
		}
		if (sammelEditierung != null) {
			log.fine(() -> "verwerfe Editierungen: " + sammelEditierung);
			sammelEditierung.macheRueckgaengig();
			sammelEditierung = null;
		}
	}
	
	@Override
	public String toString() {
		return "%s [Datei: %s]".formatted(this.getName(), this.speicherort);
	}
	
	@Override
	public int hashCode() {
		return ClassifierUtil.hashAlle(automatischSpeichern, getName(), speicherort);
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
		ProjektBasis proj = (ProjektBasis) obj;
		
		boolean automatischSpeichernGleich = automatischSpeichern == proj.automatischSpeichern;
		boolean nameGleich = Objects.equals(getName(), proj.getName());
		boolean speicherortGleich = Objects.equals(speicherort, proj.speicherort);
		
		boolean istGleich = automatischSpeichernGleich && nameGleich && speicherortGleich;
		
		log.finest(() -> """
				istGleich: %s
				   |-- automatischSpeichernGleich: %s
				   |-- nameGleich: %s
				   ╰-- speicherortGleich: %s"""
				.formatted(istGleich, automatischSpeichernGleich, nameGleich,
						speicherortGleich));
		
		return istGleich;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void legeEditierungAb(EditierBefehl editierung) {
		log.finer(() -> "[%s] Editierung in den Verlauf legen: %s"
				.formatted(ueberwachungsStatus, editierung));
		rueckgaengigVerlauf.ablegen(editierung);
		wiederholenVerlauf.leeren();
		updateVerlaufProperties();
		this.istGespeichertProperty.set(false);
	}
	
	private void updateVerlaufProperties() {
		if (kannWiederholenProperty != null) {
			kannWiederholenProperty.set(!wiederholenVerlauf.istLeer());
		}
		if (kannRueckgaengigGemachtWerdenProperty != null) {
			kannRueckgaengigGemachtWerdenProperty.set(!rueckgaengigVerlauf.istLeer());
		}
	}
}