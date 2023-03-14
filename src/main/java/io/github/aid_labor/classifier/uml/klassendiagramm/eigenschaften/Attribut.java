/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.json.JsonBooleanProperty;
import io.github.aid_labor.classifier.basis.json.JsonObjectProperty;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.projekt.editierung.EditierbarBasis;
import io.github.aid_labor.classifier.basis.projekt.editierung.EditierbarerBeobachter;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.validierung.SimpleValidierung;
import io.github.aid_labor.classifier.basis.validierung.Validierung;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/**
 * Model für Attribute in UML-Klassifizierern
 * 
 * @author Tim Muehle
 *
 */
public class Attribut extends EditierbarBasis implements EditierbarerBeobachter {

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
	
	private final JsonObjectProperty<Modifizierer> sichtbarkeit;
	private final JsonStringProperty name;
	private final Datentyp datentyp;
	@JsonIgnore
	private final JsonStringProperty initialwert;
	private final JsonBooleanProperty hatGetter;
	private final JsonBooleanProperty hatSetter;
	private final JsonBooleanProperty istStatisch;
	private final JsonBooleanProperty istFinal;
	@JsonBackReference("getterAttribut")
	private Methode getter;
	@JsonBackReference("setterAttribut")
	private Methode setter;
	@JsonIgnore
	private final List<Object> beobachterListe;
	@JsonIgnore
	private final long id;
	@JsonIgnore
    private Sprache sprache;
	@JsonIgnore
    private SimpleValidierung nameValidierung;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Konstruktor für alle Attribute zur Rekonstruktion bei Deserialisierung
	 * 
	 * @param sichtbarkeit	Sichtbarkeit des Attributes
	 * @param name			Attributname
	 * @param datentyp		Datentyp des Attributes
	 * @param initialwert	Optionaler Initialwert
	 * @param hatGetter		{@code true}, wenn das Attribut einen Getter hat
	 * @param hatSetter		{@code true}, wenn das Attribut einen Setter hat
	 * @param istStatisch	{@code true}, wenn das Attribut statisch hat
	 * @param istFinal		{@code true}, wenn das Attribut final ist
	 * @param getter		Optionale Getter-Methode
	 * @param setter		Optionale Setter-Methode
	 */
	@JsonCreator
	public Attribut(
			@JsonProperty("sichtbarkeit") Modifizierer sichtbarkeit,
			@JsonProperty("name") String name,
			@JsonProperty("datentyp") Datentyp datentyp,
			@JsonProperty("initialwert") String initialwert,
			@JsonProperty("hatGetter") boolean hatGetter,
			@JsonProperty("hatSetter") boolean hatSetter,
			@JsonProperty("istStatisch") boolean istStatisch,
			@JsonProperty(value = "istfinal", defaultValue = "false") boolean istFinal,
			@JsonProperty("getterAttribut") Methode getter,
			@JsonProperty("setterAttribut") Methode setter) {
		this.sichtbarkeit = new JsonObjectProperty<>(this, "sichtbarkeit", sichtbarkeit);
		this.datentyp = datentyp;
		this.name = new JsonStringProperty(this, "attributName", name);
		this.initialwert = new JsonStringProperty(this, "initialwert", initialwert);
		this.hatGetter = new JsonBooleanProperty(this, "hatGetter", hatGetter);
		this.hatSetter = new JsonBooleanProperty(this, "hatSetter", hatSetter);
		this.istStatisch = new JsonBooleanProperty(this, "istStatisch", istStatisch);
		this.istFinal = new JsonBooleanProperty(this, "istFinal", istFinal);
		this.getter = getter;
		this.setter = setter;
		this.beobachterListe = new LinkedList<>();
		this.id = naechsteId++;
		this.sprache = new Sprache();
        boolean spracheGesetzt = SprachUtil.setUpSprache(sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
                "UMLKlassifiziererBearbeitenDialog");
        if (!spracheGesetzt) {
            sprache.ignoriereSprachen();
        }
		
		this.ueberwachePropertyAenderung(this.sichtbarkeit, id + "_attribut_sichtbarkeit");
		this.ueberwachePropertyAenderung(this.datentyp.typNameProperty(), id + "_attribut_datentyp");
		this.ueberwachePropertyAenderung(this.name, id + "_attribut_name");
		this.ueberwachePropertyAenderung(this.initialwert, id + "_attribut_initialwert");
		this.ueberwachePropertyAenderung(this.hatGetter, id + "_attribut_getter");
		this.ueberwachePropertyAenderung(this.hatSetter, id + "_attribut_setter");
		this.ueberwachePropertyAenderung(this.istStatisch, id + "_attribut_statisch");
		this.ueberwachePropertyAenderung(this.istFinal, id + "_attribut_final");
	}
	
	/**
	 * Minimaler Konstruktor. Es sollte im Anschluss der Erzeugung sichergestellt werden, dass ein Name für das
	 * Attribut gesetzt wird.
	 * 
	 * @param sichtbarkeit	Sichtbarkeit des Attributes
	 * @param datentyp		Datentyp des Attributes
	 */
	public Attribut(Modifizierer sichtbarkeit, Datentyp datentyp) {
		this(sichtbarkeit, "", datentyp, null, false, false, false, false, null, null);
	}
	
	private void initialisiereNameValidierung(UMLKlassifizierer klassifizierer) {
        var attributNamen = FXCollections.observableList(klassifizierer.attributeProperty(),
                attribut -> new Observable[] { attribut.nameProperty() });
        BooleanBinding gleicherName = Bindings.createBooleanBinding(
                () -> attributNamen.stream().anyMatch(a -> {
                    return getName().equals(a.getName()) && a != this;
                }),
                attributNamen, nameProperty());
        this.nameValidierung = SimpleValidierung.of(gleicherName.not(), 
                        sprache.getTextProperty("nameValidierung2", "Ein Attribut mit diesem Namen ist bereits vorhanden"))
                .and(name.isNotEmpty(),
                        sprache.getTextProperty("nameValidierung", "Name angegeben"))
                .build();
    }
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public void setUMLKlassifizierer(UMLKlassifizierer klassifizierer) {
	    this.initialisiereNameValidierung(klassifizierer);
	    datentyp.setValidierung(klassifizierer, false);
	}
	
	@Override
	public List<Object> getBeobachterListe() {
		return beobachterListe;
	}
	
	public Modifizierer getSichtbarkeit() {
		return sichtbarkeit.get();
	}
	
	public void setSichtbarkeit(Modifizierer sichtbarkeit) {
		this.sichtbarkeit.set(sichtbarkeit);
	}
	
	public String getName() {
		return name.get();
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public Datentyp getDatentyp() {
		return datentyp;
	}
	
	@JsonProperty("initialwert")
	public String getInitialwert() {
		return initialwert.get();
	}
	
	public void setInitialwert(String initialwert) {
		this.initialwert.set(initialwert);
	}
	
	public boolean hatGetter() {
		return hatGetter.get();
	}
	
	public void nutzeGetter(boolean hatGetter) {
		this.hatGetter.set(hatGetter);
	}
	
	public boolean hatSetter() {
		return hatSetter.get();
	}
	
	public void nutzeSetter(boolean hatSetter) {
		this.hatSetter.set(hatSetter);
	}
	
	public boolean istStatisch() {
		return istStatisch.get();
	}
	
	public void setStatisch(boolean istStatisch) {
		this.istStatisch.set(istStatisch);
	}
	
	public boolean istFinal() {
		return istFinal.get();
	}
	
	public void setFinal(boolean istFinal) {
		this.istFinal.set(istFinal);
	}
	
	public Methode getGetter() {
		return getter;
	}
	
	public void setGetter(Methode getter) {
		this.getter = getter;
	}
	
	public Methode getSetter() {
		return setter;
	}
	
	public void setSetter(Methode setter) {
		this.setter = setter;
	}
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ObjectProperty<Modifizierer> sichtbarkeitProperty() {
		return sichtbarkeit;
	}
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public StringProperty initialwertProperty() {
		return initialwert;
	}
	
	public BooleanProperty hatGetterProperty() {
		return hatGetter;
	}
	
	public BooleanProperty hatSetterProperty() {
		return hatSetter;
	}
	
	public BooleanProperty istStatischProperty() {
		return istStatisch;
	}
	
	public BooleanProperty istFinalProperty() {
		return istFinal;
	}
	
	public SimpleValidierung getNameValidierung() {
        return nameValidierung;
    }
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public String toString() {
		return "Attribut <%s %s %s%s {%s %s}>".formatted(getSichtbarkeit(),
				getDatentyp().getTypName(), getName(),
				getInitialwert() != null ? "=" + getInitialwert() : "",
				hatGetter() ? " Getter" : "",
				hatSetter() ? "Setter " : "");
	}
	
	@Override
	public int hashCode() {
		return ClassifierUtil.hashAlle(getDatentyp(), hatGetter(), hatSetter(), getInitialwert(),
				getName(), getSichtbarkeit(), istStatisch());
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
		Attribut other = (Attribut) obj;
		boolean datentypGleich = Objects.equals(getDatentyp(), other.getDatentyp());
		boolean getterGleich = hatGetter() == other.hatGetter();
		boolean setterGleich = hatSetter() == other.hatSetter();
		boolean initialwertGleich = Objects.equals(getInitialwert(), other.getInitialwert());
		boolean nameGleich = Objects.equals(getName(), other.getName());
		boolean sichtbarkeitGleich = Objects.equals(getSichtbarkeit(),
				other.getSichtbarkeit());
		boolean statischGleich = this.istStatisch() == other.istStatisch();
		boolean istGleich = datentypGleich && getterGleich && setterGleich && initialwertGleich
				&& nameGleich && sichtbarkeitGleich && statischGleich;
		
		log.finest(() -> """
				istGleich: %s
				   |-- datentypGleich: %s
				   |-- getterGleich: %s
				   |-- setterGleich: %s
				   |-- initialwertGleich: %s
				   |-- nameGleich: %s
				   |-- sichtbarkeitGleich: %s
				   ╰-- statischGleich: %s""".formatted(istGleich, datentypGleich, getterGleich,
				setterGleich, initialwertGleich, nameGleich, sichtbarkeitGleich,
				statischGleich));
		
		return istGleich;
	}
	
	/**
	 * Erzeugt eine Tiefe Kopie dieses Objektes und dem Inhalt dieses Objektes
	 * @return neues Attribut-Objekt mit gleichen Werten (als tiefe Kopie)
	 */
	public Attribut erzeugeTiefeKopie() {
		var kopie = new Attribut(getSichtbarkeit(), getDatentyp().erzeugeTiefeKopie());
		kopie.setInitialwert(getInitialwert());
		kopie.setName(getName());
		kopie.nutzeGetter(hatGetter());
		kopie.nutzeSetter(hatSetter());
		kopie.setStatisch(istStatisch());
		
		return kopie;
	}
	
	@Override
	public void close() throws Exception {
		getter = null;
		setter = null;
		log.finest(() -> this + " leere listeners");
		beobachterListe.clear();
	}

	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}