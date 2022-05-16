/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;

import io.github.aid_labor.classifier.basis.json.JsonReadOnlyBooleanPropertyWrapper;
import io.github.aid_labor.classifier.basis.json.JsonUtil;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Projektverwaltung für ein UML-Klassendiagramm
 * 
 * @author Tim Muehle
 *
 */
@JsonAutoDetect(getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE, fieldVisibility = Visibility.ANY)
public class UMLProjekt implements Projekt {
	private static final Logger log = Logger.getLogger(UMLProjekt.class.getName());
	
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
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private String name;
	private boolean automatischSpeichern;
	private Programmiersprache programmiersprache;
	
	@JsonIgnore
	private ReadOnlyStringWrapper nameProperty;
	@JsonIgnore
	private Path speicherort;
	@JsonIgnore
	private final JsonReadOnlyBooleanPropertyWrapper istGespeichertProperty;
	
	// -------------------------------------------------------------------------------------
	// final
	@JsonSerialize(typing = Typing.DYNAMIC)
	private final ObservableList<UMLDiagrammElement> diagrammElemente;
	
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
	public UMLProjekt(String name, Programmiersprache programmiersprache,
			boolean automatischSpeichern) throws NullPointerException {
		this.name = Objects.requireNonNull(name);
		this.programmiersprache = Objects.requireNonNull(programmiersprache);
		this.diagrammElemente = FXCollections.observableArrayList();
		this.istGespeichertProperty = new JsonReadOnlyBooleanPropertyWrapper(false);
		this.setAutomatischSpeichern(automatischSpeichern);
		
		this.diagrammElemente.addListener(
				((InvalidationListener) aenderung -> this.istGespeichertProperty.set(false)));
		
	}
	
	/**
	 * Laedt eine Projektdatei (im json-Format) und rekonstruiert das gespeicherte Projekt.
	 * 
	 * @param datei Datei mit gespeichertem Projekt im json-Format
	 * @return gespeichertes Projekt als neue Instanz
	 * @throws IOException
	 */
	public static UMLProjekt ausDateiOeffnen(Path datei) throws IOException {
		try (JsonParser json = JsonUtil.getUTF8JsonParser(datei)) {
			UMLProjekt projekt = json.readValueAs(UMLProjekt.class);
			projekt.setSpeicherort(datei);
			projekt.istGespeichertProperty.set(true);
			return projekt;
		}
	}
	
	/**
	 * Konstruktor, den jackson verwendet, um eine Instanz aus einer json-Datei zu erzeugen
	 * 
	 * @param name                 JsonProperty "name"
	 * @param programmiersprache   JsonProperty "programmiersprache"
	 * @param automatischSpeichern JsonProperty "automatischSpeichern"
	 * @param diagrammElemente     JsonProperty "diagrammElemente"
	 */
	@JsonCreator
	protected UMLProjekt(
			@JsonProperty("name") String name,
			@JsonProperty("programmiersprache") Programmiersprache programmiersprache,
			@JsonProperty("automatischSpeichern") boolean automatischSpeichern,
			@JsonProperty("diagrammElemente") List<UMLDiagrammElement> diagrammElemente) {
		this(name, programmiersprache, automatischSpeichern);
		this.diagrammElemente.addAll(diagrammElemente);
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
	public Programmiersprache getProgrammiersprache() {
		return this.programmiersprache;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ObservableList<UMLDiagrammElement> getDiagrammElemente() {
		return this.diagrammElemente;
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
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}