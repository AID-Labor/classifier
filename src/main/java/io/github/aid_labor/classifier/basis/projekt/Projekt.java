/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.projekt;

import java.nio.file.Path;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.StringProperty;


/**
 * Basis fuer ein Projekt. Jedes Projekt braucht einen Namen und kann auf der Festplatte
 * gespeichert werden. Das Projekt kann Editierungen beobachten, rueckgaengig machen und 
 * diese auch wiederholen.
 * 
 * @author Tim Muehle
 *
 */
public interface Projekt extends EditierBeobachter {
	
	/**
	 * Projektname
	 * 
	 * @return Projektname
	 */
	public String getName();
	
	/**
	 * Aendert den Projektnamen. Dieses Projekt wird als nicht-gespeichert markiert (siehe
	 * {@link #istGespeichertProperty()}).
	 * 
	 * @param name neuer Projektname
	 * @throws NullPointerException Falls {@code name} {@code null} ist
	 */
	public void setName(String name) throws NullPointerException;
	
	/**
	 * Projektname mit automatischer Aktualisierung bei Aenderung
	 * 
	 * @return Projektname mit automatischer Aktualisierung
	 */
	public StringProperty nameProperty();
	
	/**
	 * Gibt den aktuellen Speicherort zurueck
	 * 
	 * @return aktueller Speicherort oder {@code null}, falls kein Speicherort eingestellt ist
	 * @see #setSpeicherort(Path)
	 * @see #speichern()
	 * @see #speichern(Path)
	 */
	public Path getSpeicherort();
	
	/**
	 * Setzt den neuen Speicherort. Dieses Projekt wird als nicht-gespeichert markiert (siehe
	 * {@link #istGespeichertProperty()}).
	 * 
	 * @param speicherort neuer Speicherort
	 */
	public void setSpeicherort(Path speicherort);
	
	/**
	 * Speichert dieses Projekt im eingestellten Speicherort (siehe
	 * {@link #setSpeicherort(Path)}).
	 * Bei Erfolg wird dieses Projekt als gespeichert markiert (siehe
	 * {@link #istGespeichertProperty()}), sonst als nicht-gespeichert.
	 * 
	 * @return {@code true}, wenn das Speichern erfolgreich war, sonst {@code false}
	 * @throws IllegalStateException falls {@link #getSpeicherort()} code null} ist
	 * 
	 * @see #setSpeicherort(Path)
	 * @see #speichern(Path)
	 */
	public boolean speichern() throws IllegalStateException;
	
	/**
	 * Setzt einen neuen Speicherort und speichert im Anschluss dieses Projekt.
	 * Diese Methode hat den selben Effekt wie die Aufrufe von
	 * {@code setSpeicherort(neuerSpeicherort)} und {@code speichern()} (in dieser
	 * Reihenfolge).
	 * 
	 * @see #setSpeicherort(Path)
	 * @see #speichern()
	 * @param neuerSpeicherort neuer Speicherort
	 * @return {@code true}, wenn das Speichern erfolgreich war, sonst {@code false}
	 * @throws IllegalStateException falls {@link #getSpeicherort()} code null} ist
	 */
	default public boolean speichern(Path neuerSpeicherort) throws IllegalStateException {
		this.setSpeicherort(neuerSpeicherort);
		return this.speichern();
	}
	
	/**
	 * Angabe, ob dieses Projekt am eingestellten Speicherort ({@link #getSpeicherort()} mit
	 * dem aktuellen Zustand gespeichert ist.
	 * 
	 * @return Diese Einstellung ist {@code true}, wenn der aktuelle Zustand am eingestellten
	 *         Speicherort gespeichert ist, sonst {@code false}
	 */
	public ReadOnlyBooleanProperty istGespeichertProperty();
	
	/**
	 * Angabe, ob automatisches Speichern im Hintergrund aktiviert ist.
	 * 
	 * @return {@code true}, wenn dises Projekt regelmaessig im Hintergrund gespeichert wird,
	 *         sonst {@code false}
	 */
	public boolean automatischSpeichern();
	
	/**
	 * Stellt das automatische Speichern im Hintergrund ein. Dies bezieht sich auf
	 * Sicherungspunkte, die gesondert vom eingestellten Speicherort gespeichert werden.
	 * Dieses Projekt wird als nicht-gespeichert markiert (siehe
	 * {@link #istGespeichertProperty()}).
	 * 
	 * @param automatischSpeichern {@code true}, wenn dises Projekt regelmaessig im
	 *                             Hintergrund gespeichert werden soll, sonst {@code false}
	 */
	public void setAutomatischSpeichern(boolean automatischSpeichern);
	
	/**
	 * Gibt an, ob eine oder mehrere Aenderungen mit der Methode {@link #macheRueckgaengig()}
	 * rueckgaengig gemacht werden koennen
	 * 
	 * @return {@code true}, wenn es widerrufbare Aenderungen im Verlauf gibt
	 */
	public boolean kannRueckgaengigGemachtWerden();
	
	/**
	 * Gibt an, ob eine oder mehrere Aenderungen mit der Methode {@link #macheRueckgaengig()}
	 * rueckgaengig gemacht werden koennen
	 * 
	 * @return {@code true}, wenn es widerrufbare Aenderungen im Verlauf gibt
	 */
	public ReadOnlyBooleanProperty kannRueckgaengigGemachtWerdenProperty();
	
	/**
	 * Macht die letzte Aenderung an diesem Projekt Rueckgaengig. Wenn diese Funktion nicht
	 * zur Verfuegung steht, passiert nichts.
	 */
	public void macheRueckgaengig();
	
	/**
	 * Gibt an, ob eine oder mehrere Rueckgaengig gemachte Aenderungen mit der Methode
	 * {@link #wiederhole()} wiederhergestellt werden koennen
	 * 
	 * @return {@code true}, wenn es wiederhergestellt Aenderungen im Verlauf gibt
	 */
	public boolean kannWiederholen();
	
	/**
	 * Gibt an, ob eine oder mehrere Rueckgaengig gemachte Aenderungen mit der Methode
	 * {@link #wiederhole()} wiederhergestellt werden koennen
	 * 
	 * @return {@code true}, wenn es wiederhergestellt Aenderungen im Verlauf gibt
	 */
	public ReadOnlyBooleanProperty kannWiederholenProperty();
	
	/**
	 * Wiederholt die letzte Rueckgaengig gemachte Aenderung. Wenn diese Funktion nicht zur
	 * Verfuegung steht, passiert nichts.
	 */
	public void wiederhole();
}
