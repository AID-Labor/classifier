/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.klassendiagramm;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.json.JsonBooleanProperty;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.projekt.EditierbarBasis;
import io.github.aid_labor.classifier.basis.projekt.EditierbarerBeobachter;
import javafx.beans.property.StringProperty;


//@formatter:off
@JsonAutoDetect(
		getterVisibility = Visibility.NONE,
		isGetterVisibility = Visibility.NONE,
		setterVisibility = Visibility.NONE,
		fieldVisibility = Visibility.ANY
)
//@formatter:on
public final class UMLVerbindung extends EditierbarBasis implements EditierbarerBeobachter {
	
	private static final Logger log = Logger.getLogger(UMLVerbindung.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static long naechsteId = 0;
	
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
	
	private final UMLVerbindungstyp typ;
	private JsonStringProperty verbindungsStartProperty;
	private JsonStringProperty verbindungsEndeProperty;
	private final JsonBooleanProperty ausgebelendetProperty;
	private final Position startPosition;
	private final Position endPosition;
	@JsonIgnore
	private final long id;
	@JsonIgnore
	private final List<Object> beobachterListe;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@JsonCreator
	public UMLVerbindung(
			@JsonProperty("typ") UMLVerbindungstyp typ,
			@JsonProperty("verbindungsStartProperty") String verbindungsStart,
			@JsonProperty("verbindungsEndeProperty") String verbindungsEnde,
			@JsonProperty("startPosition") Position startPosition,
			@JsonProperty("endPosition") Position endPosition,
			@JsonProperty("ausgebelendetProperty") boolean ausgeblendet) {
		this.typ = typ;
		this.verbindungsStartProperty = new JsonStringProperty(this, "verbindungsStart", verbindungsStart);
		this.verbindungsEndeProperty = new JsonStringProperty(this, "verbindungsEnde", verbindungsEnde);
		this.ausgebelendetProperty = new JsonBooleanProperty(this, "ausgeblendet", ausgeblendet);
		this.startPosition = Objects.requireNonNull(startPosition);
		this.endPosition = Objects.requireNonNull(endPosition);
		this.id = naechsteId++;
		this.beobachterListe = new LinkedList<>();
		
		this.ueberwachePropertyAenderung(verbindungsStartProperty, id + "_verbindungsStart");
		this.ueberwachePropertyAenderung(verbindungsEndeProperty, id + "_verbindungsEnde");
		this.ueberwachePropertyAenderung(ausgebelendetProperty, id + "_ausgeblendet");
		this.ueberwachePropertyAenderung(startPosition.xProperty(), id + "_startX");
		this.ueberwachePropertyAenderung(startPosition.yProperty(), id + "_startY");
		this.ueberwachePropertyAenderung(startPosition.hoeheProperty(), id + "_startHoehe");
		this.ueberwachePropertyAenderung(startPosition.breiteProperty(), id + "_startBreite");
		this.ueberwachePropertyAenderung(endPosition.xProperty(), id + "_endeX");
		this.ueberwachePropertyAenderung(endPosition.yProperty(), id + "_endeY");
		this.ueberwachePropertyAenderung(endPosition.hoeheProperty(), id + "_endeHoehe");
		this.ueberwachePropertyAenderung(endPosition.breiteProperty(), id + "_endeBreite");
	}
	
	public UMLVerbindung(UMLVerbindungstyp typ, String verbindungsStart, String verbindungsEnde, Position startPosition,
			Position endPosition) {
		this(typ, verbindungsStart, verbindungsEnde, startPosition, endPosition, false);
	}
	
	public UMLVerbindung(UMLVerbindungstyp typ, String verbindungsStart, String verbindungsEnde) {
		this(typ, verbindungsStart, verbindungsEnde, new Position(), new Position());
		
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public UMLVerbindungstyp getTyp() {
		return typ;
	}
	
	public StringProperty verbindungsStartProperty() {
		return verbindungsStartProperty;
	}
	
	public String getVerbindungsStart() {
		return verbindungsStartProperty.get();
	}
	
	public void setVerbindungsStart(String verbindungsStart) {
		this.verbindungsStartProperty.set(verbindungsStart);
	}
	
	public StringProperty verbindungsEndeProperty() {
		return verbindungsEndeProperty;
	}
	
	public String getVerbindungsEnde() {
		return verbindungsEndeProperty.get();
	}
	
	public void setVerbindungsEnde(String verbindungsEnde) {
		this.verbindungsEndeProperty.set(verbindungsEnde);
	}
	
	public JsonBooleanProperty ausgebelendetProperty() {
		return ausgebelendetProperty;
	}
	
	public boolean istAusgebelendet() {
		return ausgebelendetProperty.get();
	}
	
	public void setzeAusgebelendet(boolean ausblenden) {
		ausgebelendetProperty.set(ausblenden);
	}
	
	public Position getStartPosition() {
		return startPosition;
	}
	
	public Position getEndPosition() {
		return endPosition;
	}
	
	public long getId() {
		return id;
	}
	
	@Override
	public List<Object> getBeobachterListe() {
		return beobachterListe;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public void close() throws Exception {
		log.config(() -> "schliesse UMLVerbindung " + this.toString());
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
	
	@Override
	public int hashCode() {
		return ClassifierUtil.hashAlle(istAusgebelendet(), getEndPosition(), getStartPosition(), getTyp(),
				getVerbindungsEnde(), getVerbindungsStart());
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
		UMLVerbindung other = (UMLVerbindung) obj;
		return Objects.equals(ausgebelendetProperty, other.ausgebelendetProperty)
				&& Objects.equals(endPosition, other.endPosition) && Objects.equals(startPosition, other.startPosition)
				&& typ == other.typ && Objects.equals(verbindungsEndeProperty, other.verbindungsEndeProperty)
				&& Objects.equals(verbindungsStartProperty, other.verbindungsStartProperty);
	}
	
	@Override
	public String toString() {
		return "%s #%d [%s -> %s]".formatted(this.getClass().getSimpleName(), id, getVerbindungsStart(),
				getVerbindungsEnde());
	}
	
	public UMLVerbindung erzeugeTiefeKopie() {
		return new UMLVerbindung(typ, getVerbindungsStart(), getVerbindungsEnde(), new Position(startPosition),
				new Position(endPosition), istAusgebelendet());
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}