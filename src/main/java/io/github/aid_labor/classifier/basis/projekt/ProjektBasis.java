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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import io.github.aid_labor.classifier.basis.json.JsonReadOnlyBooleanPropertyWrapper;
import io.github.aid_labor.classifier.basis.json.JsonUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;


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
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private String name;
	private boolean automatischSpeichern;
	
	@JsonIgnore
	private ReadOnlyStringWrapper nameProperty;
	@JsonIgnore
	private Path speicherort;
	
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
	public ProjektBasis(String name, boolean automatischSpeichern)
			throws NullPointerException {
		this.name = Objects.requireNonNull(name);
		this.istGespeichertProperty = new JsonReadOnlyBooleanPropertyWrapper(false);
		this.setAutomatischSpeichern(automatischSpeichern);
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
	public String getName() {
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		this.name = Objects.requireNonNull(name);
		this.istGespeichertProperty.set(false);
		if (nameProperty != null) {
			this.nameProperty.set(name);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadOnlyStringProperty nameProperty() {
		if (nameProperty == null) {
			this.nameProperty = new ReadOnlyStringWrapper(this.name);
		}
		return this.nameProperty.getReadOnlyProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Path getSpeicherort() {
		return this.speicherort;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSpeicherort(Path speicherort) {
		this.speicherort = speicherort;
		this.istGespeichertProperty.set(false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadOnlyBooleanProperty istGespeichertProperty() {
		return this.istGespeichertProperty.getReadOnlyProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean automatischSpeichern() {
		return this.automatischSpeichern;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAutomatischSpeichern(boolean automatischSpeichern) {
		this.automatischSpeichern = automatischSpeichern;
		this.istGespeichertProperty.set(false);
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
	 * @implNote Zum Serialisieren wird das json-Format verwendet.
	 */
	@Override
	public boolean speichern() {
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
		} catch (IOException e) {
			log.log(Level.WARNING, e,
					() -> "Projekt %s konnte nicht gespeichert werden".formatted(this.name));
		}
		
		this.istGespeichertProperty.set(erfolg);
		return erfolg;
	}
	
	@Override
	public String toString() {
		return "%s [Datei: %s]".formatted(this.name, this.speicherort);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(automatischSpeichern, name, speicherort);
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
		boolean nameGleich = Objects.equals(name, proj.name);
		boolean speicherortGleich = Objects.equals(speicherort, proj.speicherort);
		
		boolean istGleich = automatischSpeichernGleich && nameGleich && speicherortGleich;
		
		log.finer(() -> """
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
	
}