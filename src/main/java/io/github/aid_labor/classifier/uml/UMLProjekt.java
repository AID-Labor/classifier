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
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.projekt.ListenUeberwacher;
import io.github.aid_labor.classifier.basis.projekt.ProjektBasis;
import io.github.aid_labor.classifier.uml.eigenschaften.Programmiersprache;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


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
		super(name, automatischSpeichern);
		this.programmiersprache = Objects.requireNonNull(programmiersprache);
		this.diagrammElemente = FXCollections.observableArrayList();
		
		// Diagramm-Elemente auf Aenderung Ueberwachen (sowhol die Liste als auch geaenderte
		// Objekte in der Liste)
		this.diagrammElemente
				.addListener(new ListenUeberwacher<>(this.diagrammElemente, this));
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
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public int hashCode() {
		return Objects.hash(automatischSpeichern(), diagrammElemente, getName(),
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
		
		boolean automatischSpeichernGleich = automatischSpeichern() == proj
				.automatischSpeichern();
		boolean diagrammElementeGleich = ClassifierUtil.pruefeGleichheit(this.diagrammElemente,
				proj.diagrammElemente);
		boolean nameGleich = Objects.equals(getName(), proj.getName());
		boolean programmierspracheGleich = getProgrammiersprache()
				.equals(proj.getProgrammiersprache());
		boolean speicherortGleich = Objects.equals(getSpeicherort(), proj.getSpeicherort());
		
		boolean istGleich = automatischSpeichernGleich && diagrammElementeGleich && nameGleich
				&& programmierspracheGleich && speicherortGleich;
		
		log.finest(() -> """
				istGleich: %s
				   |-- automatischSpeichernGleich: %s
				   |-- diagrammElementeGleich: %s
				   |-- nameGleich: %s
				   |-- programmierspracheGleich: %s
				   ╰-- speicherortGleich: %s"""
				.formatted(istGleich, automatischSpeichernGleich, diagrammElementeGleich,
						nameGleich, programmierspracheGleich, speicherortGleich));
		
		return istGleich;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}