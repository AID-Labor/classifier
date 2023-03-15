/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.projekt.editierung.EditierbarBasis;
import io.github.aid_labor.classifier.basis.projekt.editierung.EditierbarerBeobachter;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.validierung.SimpleValidierung;
import io.github.aid_labor.classifier.basis.validierung.ValidierungCollection;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class Parameter extends EditierbarBasis implements EditierbarerBeobachter {
//	private static final Logger log = Logger.getLogger(Parameter.class.getName());

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

    private final JsonStringProperty name;
    private final Datentyp datentyp;
    @JsonIgnore
    private final List<Object> beobachterListe;
    @JsonIgnore
    private final long id;
    @JsonIgnore
    private final Sprache sprache;
    @JsonIgnore
    private SimpleValidierung nameValidierung;
    @JsonIgnore
    private final ValidierungCollection parameterValid;

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    @JsonCreator
    public Parameter(@JsonProperty("datentyp") Datentyp datentyp, @JsonProperty("name") String name) {
        this.datentyp = Objects.requireNonNull(datentyp);
        this.name = new JsonStringProperty(name);
        this.beobachterListe = new LinkedList<>();
        this.id = naechsteId++;
        this.parameterValid = new ValidierungCollection();

        this.ueberwachePropertyAenderung(datentyp.typNameProperty(), id + "_parameter_typ");
        this.ueberwachePropertyAenderung(this.name, id + "_parameter_name");
        this.sprache = new Sprache();
        boolean spracheGesetzt = SprachUtil.setUpSprache(sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
                "UMLKlassifiziererBearbeitenDialog");
        if (!spracheGesetzt) {
            sprache.ignoriereSprachen();
        }
    }

    public Parameter(Datentyp datentyp) {
        this(datentyp, "");
    }

    private void initialisiereNameValidierung(HatParameterListe typ) {
        var parameterNamen = FXCollections.observableList(typ.parameterListeProperty(),
                parameter -> new Observable[] { parameter.nameProperty() });
        BooleanBinding gleicherName = Bindings.createBooleanBinding(
                () -> parameterNamen.stream().anyMatch(a -> {
                    return a != this && getName().equals(a.getName()) && !getName().isEmpty();
                }),
                parameterNamen, nameProperty());
        var supressValidierung = Einstellungen.getBenutzerdefiniert().zeigeParameterNamenProperty().not();
        this.nameValidierung = SimpleValidierung.of(gleicherName.not(),
                sprache.getTextProperty("parameterValidierung", "Ein Parameter mit diesem Namen ist bereits vorhanden"))
                .and(name.isNotEmpty().or(supressValidierung),
                        sprache.getTextProperty("nameValidierung", "Name angegeben"))
                .build();
        this.parameterValid.add(nameValidierung);
    }

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

    public void setUMLKlassifizierer(UMLKlassifizierer klassifizierer) {
        datentyp.setValidierung(klassifizierer, false);
        this.parameterValid.add(datentyp.getTypValidierung());
    }

    public void setTypMitParameterliste(HatParameterListe typ) {
        this.initialisiereNameValidierung(typ);
    }

    public SimpleValidierung getNameValidierung() {
        return nameValidierung;
    }
    
    public ValidierungCollection getParameterValid() {
        return parameterValid;
    }

    @Override
    public List<Object> getBeobachterListe() {
        return beobachterListe;
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

    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // * * * *

    public StringProperty nameProperty() {
        return name;
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
        return ClassifierUtil.hashAlle(getDatentyp(), getName());
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
        Parameter other = (Parameter) obj;
        return Objects.equals(getDatentyp(), other.getDatentyp()) && Objects.equals(getName(), other.getName());
    }

    public Parameter erzeugeTiefeKopie() {
        return new Parameter(getDatentyp().erzeugeTiefeKopie(), getName());
    }

    @Override
    public void close() throws Exception {
        log.finest(() -> this + " leere listener");
        beobachterListe.clear();
    }

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

}