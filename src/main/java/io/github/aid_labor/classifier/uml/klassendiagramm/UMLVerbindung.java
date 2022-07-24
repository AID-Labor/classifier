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
import io.github.aid_labor.classifier.basis.json.JsonDoubleProperty;
import io.github.aid_labor.classifier.basis.json.JsonObjectProperty;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.projekt.Schliessbar;
import io.github.aid_labor.classifier.basis.projekt.editierung.EditierbarBasis;
import io.github.aid_labor.classifier.basis.projekt.editierung.EditierbarerBeobachter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;


//@formatter:off
@JsonAutoDetect(
		getterVisibility = Visibility.NONE,
		isGetterVisibility = Visibility.NONE,
		setterVisibility = Visibility.NONE,
		fieldVisibility = Visibility.ANY
)
//@formatter:on
public final class UMLVerbindung extends EditierbarBasis implements EditierbarerBeobachter, Schliessbar {
	
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
	private final JsonDoubleProperty mitteVerschiebungProperty;
	private final JsonBooleanProperty ausgebelendetProperty;
	private final JsonBooleanProperty automatischProperty;
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
	private final List<Object> schwacheBeobachterListe;
	@JsonIgnore
	private final List<Object> beobachterListe;
	@JsonIgnore
	private boolean istGeschlossen = false;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@JsonCreator
	public UMLVerbindung(@JsonProperty("typ") UMLVerbindungstyp typ,
			@JsonProperty("verbindungsStartProperty") String verbindungsStart,
			@JsonProperty("verbindungsEndeProperty") String verbindungsEnde,
			@JsonProperty("startPosition") Position startPosition, @JsonProperty("endPosition") Position endPosition,
			@JsonProperty("ausgebelendetProperty") boolean ausgeblendet,
			@JsonProperty(value = "automatischProperty", defaultValue = "true") boolean automatisch,
			@JsonProperty(value = "orientierungStartProperty", defaultValue = "UNBEKANNT") Orientierung orientierungStart,
			@JsonProperty(value = "orientierungEndeProperty", defaultValue = "UNBEKANNT") Orientierung orientierungEnde,
			@JsonProperty(value = "mitteVerschiebungProperty", defaultValue = "0") double mitteVerschiebung) {
		this.typ = typ;
		this.verbindungsStartProperty = new JsonStringProperty(this, "verbindungsStart", verbindungsStart);
		this.verbindungsEndeProperty = new JsonStringProperty(this, "verbindungsEnde", verbindungsEnde);
		this.ausgebelendetProperty = new JsonBooleanProperty(this, "ausgeblendet", ausgeblendet);
		this.automatischProperty = new JsonBooleanProperty(this, "automatisch", automatisch);
		this.startElementProperty = new SimpleObjectProperty<>(this, "startElement", null);
		this.endElementProperty = new SimpleObjectProperty<>(this, "endElement", null);
		this.startPosition = startPosition == null ? new Position(this) : new Position(startPosition, this);
		this.endPosition = endPosition == null ? new Position(this) : new Position(endPosition, this);
		this.mitteVerschiebungProperty = new JsonDoubleProperty(this, "mitteVerschiebung", mitteVerschiebung);
		this.orientierungStartProperty = new JsonObjectProperty<>(this, "orientierungStart", orientierungStart);
		this.orientierungEndeProperty = new JsonObjectProperty<>(this, "orientierungEnde", orientierungEnde);
		this.id = naechsteId++;
		this.beobachterListe = new LinkedList<>();
		this.schwacheBeobachterListe = new LinkedList<>();
		ChangeListener<UMLKlassifizierer> beobachterEnde = (p, alt, neu) -> setzeAusgebelendet(neu == null);
		this.schwacheBeobachterListe.add(beobachterEnde);
		this.endElementProperty.addListener(new WeakChangeListener<>(beobachterEnde));
		this.setzeAusgebelendet(endElementProperty.get() == null);
		
		this.ueberwachePropertyAenderung(verbindungsStartProperty, id + "_verbindungsStart");
		this.ueberwachePropertyAenderung(verbindungsEndeProperty, id + "_verbindungsEnde");
		this.ueberwachePropertyAenderung(ausgebelendetProperty, id + "_ausgeblendet");
		this.ueberwachePropertyAenderung(automatischProperty, id + "_automatisch");
		this.ueberwachePropertyAenderung(this.startPosition.xProperty(), id + "_startX");
		this.ueberwachePropertyAenderung(this.startPosition.yProperty(), id + "_startY");
		this.ueberwachePropertyAenderung(this.startPosition.hoeheProperty(), id + "_startHoehe");
		this.ueberwachePropertyAenderung(this.startPosition.breiteProperty(), id + "_startBreite");
		this.ueberwachePropertyAenderung(this.endPosition.xProperty(), id + "_endeX");
		this.ueberwachePropertyAenderung(this.endPosition.yProperty(), id + "_endeY");
		this.ueberwachePropertyAenderung(this.endPosition.hoeheProperty(), id + "_endeHoehe");
		this.ueberwachePropertyAenderung(this.endPosition.breiteProperty(), id + "_endeBreite");
		this.ueberwachePropertyAenderung(this.mitteVerschiebungProperty, id + "_mitteVerschiebung");
		this.ueberwachePropertyAenderung(orientierungStartProperty, id + "_orientierungStart");
		this.ueberwachePropertyAenderung(orientierungEndeProperty, id + "_orientierungEnde");
	}
	
	public UMLVerbindung(UMLVerbindungstyp typ, String verbindungsStart, String verbindungsEnde, Position startPosition,
			Position endPosition) {
		this(typ, verbindungsStart, verbindungsEnde, startPosition, endPosition, false, true, Orientierung.UNBEKANNT,
				Orientierung.UNBEKANNT, 0);
	}
	
	public UMLVerbindung(UMLVerbindungstyp typ, String verbindungsStart, String verbindungsEnde) {
		this(typ, verbindungsStart, verbindungsEnde, null, null);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public UMLVerbindungstyp getTyp() {
		return typ;
	}
	
	public StringProperty verbindungsStartProperty() {
		if (istGeschlossen) {
			return null;
		}
		return verbindungsStartProperty;
	}
	
	public String getVerbindungsStart() {
		if (istGeschlossen) {
			return null;
		}
		return verbindungsStartProperty.get();
	}
	
	public void setVerbindungsStart(String verbindungsStart) {
		this.verbindungsStartProperty.set(verbindungsStart);
	}
	
	public StringProperty verbindungsEndeProperty() {
		if (istGeschlossen) {
			return null;
		}
		return verbindungsEndeProperty;
	}
	
	public String getVerbindungsEnde() {
		if (istGeschlossen) {
			return null;
		}
		return verbindungsEndeProperty.get();
	}
	
	public void setVerbindungsEnde(String verbindungsEnde) {
		this.verbindungsEndeProperty.set(verbindungsEnde);
	}
	
	public ObjectProperty<UMLKlassifizierer> startElementProperty() {
		if (istGeschlossen) {
			return null;
		}
		return startElementProperty;
	}
	
	public UMLKlassifizierer getStartElement() {
		if (istGeschlossen) {
			return null;
		}
		return startElementProperty.get();
	}
	
	public void setStartElement(UMLKlassifizierer startElement) {
		startElementProperty.set(startElement);
	}
	
	public ObjectProperty<UMLKlassifizierer> endElementProperty() {
		if (istGeschlossen) {
			return null;
		}
		return endElementProperty;
	}
	
	public UMLKlassifizierer getEndElement() {
		if (istGeschlossen) {
			return null;
		}
		return endElementProperty.get();
	}
	
	public void setEndElement(UMLKlassifizierer endElement) {
		endElementProperty.set(endElement);
	}
	
	public JsonBooleanProperty ausgebelendetProperty() {
		if (istGeschlossen) {
			return null;
		}
		return ausgebelendetProperty;
	}
	
	public boolean istAusgebelendet() {
		return ausgebelendetProperty.get();
	}
	
	public void setzeAusgebelendet(boolean ausblenden) {
		ausgebelendetProperty.set(ausblenden);
	}
	
	public JsonBooleanProperty automatischProperty() {
		if (istGeschlossen) {
			return null;
		}
		return automatischProperty;
	}
	
	public boolean istAutomatisch() {
		return automatischProperty.get();
	}
	
	public void setzeAutomatisch(boolean automatisch) {
		automatischProperty.set(automatisch);
	}
	
	public Position getStartPosition() {
		if (istGeschlossen) {
			return null;
		}
		return startPosition;
	}
	
	public Position getEndPosition() {
		if (istGeschlossen) {
			return null;
		}
		return endPosition;
	}
	
	public DoubleProperty mitteVerschiebungProperty() {
		return mitteVerschiebungProperty;
	}
	
	public double getMitteVerschiebung() {
		return mitteVerschiebungProperty.get();
	}
	
	public void setMitteVerschiebung(double mitteVerschiebung) {
		mitteVerschiebungProperty.set(mitteVerschiebung);
	}
	
	public long getId() {
		return id;
	}
	
	public JsonObjectProperty<Orientierung> orientierungStartProperty() {
		if (istGeschlossen) {
			return null;
		}
		return orientierungStartProperty;
	}
	
	public Orientierung getOrientierungStart() {
		if (istGeschlossen) {
			return null;
		}
		return orientierungStartProperty.get();
	}
	
	public void setOrientierungStart(Orientierung orientierungStart) {
		orientierungStartProperty.set(orientierungStart);
	}
	
	public JsonObjectProperty<Orientierung> orientierungEndeProperty() {
		if (istGeschlossen) {
			return null;
		}
		return orientierungEndeProperty;
	}
	
	public Orientierung getOrientierungEnde() {
		if (istGeschlossen) {
			return null;
		}
		return orientierungEndeProperty.get();
	}
	
	public void setOrientierungEnde(Orientierung orientierungEnde) {
		orientierungEndeProperty.set(orientierungEnde);
	}
	
	public boolean istGeschlossen() {
		return istGeschlossen;
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
	public void schliesse() throws Exception {
		log.config(() -> "schliesse UMLVerbindung " + this.toString());
		istGeschlossen = true;
		for (var attribut : this.getClass().getDeclaredFields()) {
			try {
				if (!Modifier.isStatic(attribut.getModifiers()) && !attribut.getName().equals("istGeschlossen")) {
					attribut.setAccessible(true);
					attribut.set(this, null);
				}
			} catch (Exception e) {
				// ignore
			}
		}
		beobachterListe.clear();
		schwacheBeobachterListe.clear();
	}
	
	private boolean darfGeschlossenWerden = false;
	
	@Override
	public boolean darfGeschlossenWerden() {
		return darfGeschlossenWerden;
	}
	
	@Override
	public void setDarfGeschlossenWerden(boolean wert) {
		darfGeschlossenWerden = wert;
	}
	
	@Override
	public int hashCode() {
		return ClassifierUtil.hashAlle(istAusgebelendet(), getEndPosition(), getStartPosition(), getTyp(),
				getVerbindungsEnde(), getVerbindungsStart(), getOrientierungEnde(), getOrientierungStart(),
				getMitteVerschiebung());
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
				&& Objects.equals(verbindungsStartProperty, other.verbindungsStartProperty)
				&& getMitteVerschiebung() == other.getMitteVerschiebung();
	}
	
	@Override
	public String toString() {
		return "%s #%d [%s -> %s]".formatted(this.getClass().getSimpleName(), id, getVerbindungsStart(),
				getVerbindungsEnde());
	}
	
	public UMLVerbindung erzeugeTiefeKopie() {
		return new UMLVerbindung(typ, getVerbindungsStart(), getVerbindungsEnde(), new Position(startPosition),
				new Position(endPosition), istAusgebelendet(), istAutomatisch(), getOrientierungStart(),
				getOrientierungEnde(), getMitteVerschiebung());
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}