/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;

public class Konstruktor extends EditierbarBasis implements EditierbarerBeobachter, HatParameterListe {
    private static final Logger log = Logger.getLogger(Konstruktor.class.getName());

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
    private ObservableList<Parameter> parameterListe;
    @JsonIgnore
    private final List<Object> beobachterListe;
    @JsonIgnore
    private final long id;
    @JsonIgnore
    private UMLKlassifizierer klassifizierer;
    @JsonIgnore
    private SimpleValidierung signaturValidierung;
    @JsonIgnore
    private SimpleValidierung parameterValidierung;
    @JsonIgnore
    private final Sprache sprache;

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public Konstruktor(Modifizierer sichtbarkeit) {
        this.id = naechsteId++;
        this.beobachterListe = new LinkedList<>();
        this.sprache = new Sprache();
        boolean spracheGesetzt = SprachUtil.setUpSprache(sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
                "UMLKlassifiziererBearbeitenDialog");
        if (!spracheGesetzt) {
            sprache.ignoriereSprachen();
        }

        this.name = new JsonStringProperty(this, "konstruktornname", "");
        this.parameterListe = FXCollections.observableList(new LinkedList<>());

        this.sichtbarkeit = new JsonObjectProperty<>(this, "sichtbarkeit", sichtbarkeit);

        this.ueberwachePropertyAenderung(this.sichtbarkeit, id + "_konstruktor_sichtbarkeit");
        this.ueberwachePropertyAenderung(this.name, id + "_konstruktor_name");

        this.ueberwacheListenAenderung(this.parameterListe);

        ListChangeListener<Parameter> parameterUeberwacher = aenderung -> {
            while (aenderung.next()) {
                for (var parameterHinzu : aenderung.getAddedSubList()) {
                    if (klassifizierer != null) {
                        parameterHinzu.setUMLKlassifizierer(klassifizierer);
                    }
                    parameterHinzu.setTypMitParameterliste(this);
                }
            }
        };
        this.beobachterListe.add(parameterUeberwacher);
        this.parameterListe.addListener(new WeakListChangeListener<>(parameterUeberwacher));
    }

    @JsonCreator
    protected Konstruktor(@JsonProperty("sichtbarkeit") Modifizierer sichtbarkeit, @JsonProperty("name") String name,
            @JsonProperty("parameterListe") List<Parameter> parameterListe) {
        this(sichtbarkeit);
        this.parameterListe.addAll(parameterListe);
        this.setName(name);
    }

    private void initialisiereValidierung(UMLKlassifizierer klassifizierer) {
        var konstruktoren = FXCollections.observableList(klassifizierer.konstruktorProperty(),
                konstruktor -> new Observable[] { konstruktor.nameProperty(), FXCollections.observableList(
                        konstruktor.parameterListe,
                        parameter -> new Observable[] { parameter.getDatentyp().typNameProperty() }) });
        BooleanBinding gleicheSignatur = Bindings.createBooleanBinding(
                () -> konstruktoren.stream()
                        .filter(k -> k != this)
                        .anyMatch(m -> {
                            return Objects.deepEquals(
                                    parameterListe.stream().map(Parameter::getDatentyp).map(Datentyp::getTypName)
                                            .toList(),
                                    m.parameterListe.stream().map(Parameter::getDatentyp).map(Datentyp::getTypName)
                                            .toList());
                        }),
                konstruktoren, name);
        this.signaturValidierung = SimpleValidierung.of(gleicheSignatur.not(),
                sprache.getTextProperty("konstruktorValidierung",
                        "Ein Konstruktor mit gleicher Parameterliste ist bereits vorhanden"))
                .build();
        BooleanBinding doppelteParamter = Bindings.createBooleanBinding(() -> {
            Set<String> params = new HashSet<>();
            boolean doppelt = false;
            for (var param : parameterListeProperty()) {
                if (!params.add(param.getName())) {
                    doppelt = true;
                    break;
                }
            }
            return !doppelt;
        }, new Observable[] { FXCollections.observableList(
                parameterListe, parameter -> new Observable[] { parameter.nameProperty() }) });
        this.parameterValidierung = SimpleValidierung.of(doppelteParamter.not(),
                sprache.getTextProperty("parameterValidierung2",
                        "Es darf keine Parameter mit gleichem Namen geben"))
                .build();
    }

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

    public void setUMLKlassifizierer(UMLKlassifizierer klassifizierer) {
        this.klassifizierer = klassifizierer;
        for (var parameter : this.parameterListe) {
            parameter.setUMLKlassifizierer(klassifizierer);
        }
        initialisiereValidierung(klassifizierer);
    }

    public SimpleValidierung getSignaturValidierung() {
        return signaturValidierung;
    }

    public SimpleValidierung getParameterValidierung() {
        return parameterValidierung;
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

    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // * * * *

    @Override
    public ObservableList<Parameter> parameterListeProperty() {
        return parameterListe;
    }

    public ObjectProperty<Modifizierer> sichtbarkeitProperty() {
        return sichtbarkeit;
    }

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
    public String toString() {
        return "Methode <%s %s(%s)>".formatted(getSichtbarkeit(), getName(),
                parameterListeProperty().stream().map(p -> String.join(" ", p.getDatentyp().getTypName(), p.getName()))
                        .collect(Collectors.joining(", ")));
    }

    @Override
    public int hashCode() {
        return ClassifierUtil.hashAlle(getName(), parameterListeProperty(), getSichtbarkeit());
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
        Konstruktor k = (Konstruktor) obj;

        boolean nameGleich = Objects.equals(getName(), k.getName());
        boolean parameterListeGleich = ClassifierUtil.pruefeGleichheit(this.parameterListeProperty(),
                k.parameterListeProperty());
        boolean sichtbarkeitGleich = Objects.equals(getSichtbarkeit(), k.getSichtbarkeit());

        boolean istGleich = nameGleich && parameterListeGleich && sichtbarkeitGleich;

        log.finest(() -> """
                istGleich: %s
                   |-- nameGleich: %s
                   |-- parameterListeGleich: %s
                   ╰-- sichtbarkeitGleich: %s""".formatted(istGleich, nameGleich, parameterListeGleich,
                sichtbarkeitGleich));

        return istGleich;
    }

    public Konstruktor erzeugeTiefeKopie() {
        var kopie = new Konstruktor(getSichtbarkeit());
        kopie.setName(getName());

        for (var parameter : parameterListe) {
            kopie.parameterListe.add(parameter.erzeugeTiefeKopie());
        }

        return kopie;
    }

    @Override
    public void close() throws Exception {
        log.finest(() -> this + " leere Parameter");
        for (var param : parameterListe) {
            param.close();
        }
        try {
            parameterListe.clear();
        } catch (Exception e) {
            // ignorieren fuer Empty List
        }
        log.finest(() -> this + " leere listeners");
        beobachterListe.clear();
    }

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

}