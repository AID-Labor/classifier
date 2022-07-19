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
import io.github.aid_labor.classifier.basis.json.JsonObjectProperty;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.projekt.editierung.EditierbarBasis;
import io.github.aid_labor.classifier.basis.projekt.editierung.EditierbarerBeobachter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;


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
	private final JsonStringProperty verbindungsStartProperty;
	private final JsonStringProperty verbindungsEndeProperty;
	private final JsonBooleanProperty ausgebelendetProperty;
	private final Position startPosition;
	private final Position endPosition;
	private final JsonObjectProperty<Orientierung> orientierungStartProperty;
	private final JsonObjectProperty<Orientierung> orientierungEndeProperty;
	@JsonIgnore
	private final ObjectProperty<UMLKlassifizierer> startElementProperty;
	@JsonIgnore
	private final ObjectProperty<UMLKlassifizierer> endElementProperty;
	@JsonIgnore
	private final long id;
	@JsonIgnore
	private final List<Object> beobachterListe;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@JsonCreator
	public UMLVerbindung(@JsonProperty("typ") UMLVerbindungstyp typ,
			@JsonProperty("verbindungsStartProperty") String verbindungsStart,
			@JsonProperty("verbindungsEndeProperty") String verbindungsEnde,
			@JsonProperty("startPosition") Position startPosition, @JsonProperty("endPosition") Position endPosition,
			@JsonProperty("ausgebelendetProperty") boolean ausgeblendet,
			@JsonProperty(value = "orientierungStartProperty", defaultValue = "UNBEKANNT") Orientierung orientierungStart,
			@JsonProperty(value = "orientierungEndeProperty", defaultValue = "UNBEKANNT") Orientierung orientierungEnde) {
		this.typ = typ;
		this.verbindungsStartProperty = new JsonStringProperty(this, "verbindungsStart", verbindungsStart);
		this.verbindungsEndeProperty = new JsonStringProperty(this, "verbindungsEnde", verbindungsEnde);
		this.ausgebelendetProperty = new JsonBooleanProperty(this, "ausgeblendet", ausgeblendet) {
			ChangeListener<Boolean> bindungsBeobachter;
			ObservableValue<? extends Boolean> observable;
			
			@Override
			public void bind(ObservableValue<? extends Boolean> observable) {
				Objects.requireNonNull(observable);
				if (this.observable != null) {
					throw new IllegalStateException("Mehr als eine Bindung nicht möglich!");
				}
				bindungsBeobachter = (p, alt, neu) -> set(neu);
				observable.addListener(new WeakChangeListener<>(bindungsBeobachter));
				this.observable = observable;
				this.set(observable.getValue());
			}
			
			@Override
			public void unbind() {
				if (observable != null) {
					observable.removeListener(bindungsBeobachter);
					observable = null;
					bindungsBeobachter = null;
				}
			}
		};
		this.startElementProperty = new SimpleObjectProperty<>(this, "startElement", null);
		this.endElementProperty = new SimpleObjectProperty<>(this, "endElement", null);
		this.startPosition = Objects.requireNonNull(startPosition);
		this.endPosition = Objects.requireNonNull(endPosition);
		this.orientierungStartProperty = new JsonObjectProperty<>(this, "orientierungStart", orientierungStart);
		this.orientierungEndeProperty = new JsonObjectProperty<>(this, "orientierungEnde", orientierungEnde);
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
		this.ueberwachePropertyAenderung(this.orientierungStartProperty,  id + "_orientierungStart");
		this.ueberwachePropertyAenderung(this.orientierungEndeProperty,  id + "_orientierungEnde");
	}
	
	public UMLVerbindung(UMLVerbindungstyp typ, String verbindungsStart, String verbindungsEnde, Position startPosition,
			Position endPosition) {
		this(typ, verbindungsStart, verbindungsEnde, startPosition, endPosition, false, Orientierung.UNBEKANNT,
				Orientierung.UNBEKANNT);
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
	
	public ObjectProperty<UMLKlassifizierer> startElementProperty() {
		return startElementProperty;
	}
	
	public UMLKlassifizierer getStartElement() {
		return startElementProperty.get();
	}
	
	public void setStartElement(UMLKlassifizierer startElement) {
		startElementProperty.set(startElement);
	}
	
	public ObjectProperty<UMLKlassifizierer> endElementProperty() {
		return endElementProperty;
	}
	
	public UMLKlassifizierer getEndElement() {
		return endElementProperty.get();
	}
	
	public void setEndElement(UMLKlassifizierer endElement) {
		endElementProperty.set(endElement);
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
	
	public JsonObjectProperty<Orientierung> orientierungStartProperty() {
		return orientierungStartProperty;
	}
	
	public Orientierung getOrientierungStart() {
		return orientierungStartProperty.get();
	}
	
	public void setOrientierungStart(Orientierung orientierungStart) {
		orientierungStartProperty.set(orientierungStart);
	}
	
	public JsonObjectProperty<Orientierung> orientierungEndeProperty() {
		return orientierungEndeProperty;
	}
	
	public Orientierung getOrientierungEnde() {
		return orientierungEndeProperty.get();
	}
	
	public void setOrientierungEnde(Orientierung orientierungEnde) {
		orientierungEndeProperty.set(orientierungEnde);
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
				new Position(endPosition), istAusgebelendet(), getOrientierungStart(), getOrientierungEnde());
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}