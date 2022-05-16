/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.uml;

import java.nio.file.Path;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.collections.ObservableList;

public interface Projekt {
	
	public String getName();
	public void setName(String name) throws NullPointerException;
	public ReadOnlyStringProperty nameProperty();
	
	public Path getSpeicherort();
	public void setSpeicherort(Path speicherort);
	public boolean speichern() throws IllegalStateException;
	
	default public boolean speichern(Path neuerSpeicherort) {
		this.setSpeicherort(neuerSpeicherort);
		return this.speichern();
	}
	
	public Programmiersprache getProgrammiersprache();
	
	public ObservableList<UMLDiagrammElement> getDiagrammElemente();
	
	public ReadOnlyBooleanProperty istGespeichertProperty();
	public boolean automatischSpeichern();
	public void setAutomatischSpeichern(boolean automatischSpeichern);
}
