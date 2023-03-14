/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.klassendiagramm;

import java.io.Closeable;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.json.JsonEnumProperty;
import io.github.aid_labor.classifier.basis.json.JsonEnumProperty.EnumPropertyZuStringKonverter;
import io.github.aid_labor.classifier.basis.json.JsonObjectProperty;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.projekt.editierung.ListenAenderungsUeberwacher;
import io.github.aid_labor.classifier.basis.projekt.editierung.ListenEditierUeberwacher;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.validierung.Validierung;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Attribut;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Konstruktor;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Methode;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Modifizierer;
import io.github.aid_labor.classifier.uml.programmierung.Programmiersprache;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.WeakListChangeListener;

public class UMLKlassifizierer extends UMLBasisElement {
    private static final Logger log = Logger.getLogger(UMLKlassifizierer.class.getName());

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public static String nameOhnePaket(String name) {
        if (name.contains(":")) {
            return name.substring(name.indexOf(":") + 1);
        } else {
            return name;
        }
    }

    private class KonstruktorLinkedList extends LinkedList<Konstruktor> {

        private static final long serialVersionUID = 1628790459929603311L;

        @Override
        public boolean add(Konstruktor k) {
            if (KlassifiziererTyp.Interface.equals(getTyp())) {
                throw new UnsupportedOperationException("keine Konstruktoren bei Interfaces erlaubt");
            }
            return super.add(k);
        }

        @Override
        public void addFirst(Konstruktor e) {
            if (KlassifiziererTyp.Interface.equals(getTyp())) {
                throw new UnsupportedOperationException("keine Konstruktoren bei Interfaces erlaubt");
            }
            super.addFirst(e);
        }

        @Override
        public void addLast(Konstruktor e) {
            if (KlassifiziererTyp.Interface.equals(getTyp())) {
                throw new UnsupportedOperationException("keine Konstruktoren bei Interfaces erlaubt");
            }
            super.addLast(e);
        }

        @Override
        public boolean addAll(Collection<? extends Konstruktor> c) {
            if (KlassifiziererTyp.Interface.equals(getTyp())) {
                throw new UnsupportedOperationException("keine Konstruktoren bei Interfaces erlaubt");
            }
            return super.addAll(c);
        }

        @Override
        public boolean addAll(int index, Collection<? extends Konstruktor> c) {
            if (KlassifiziererTyp.Interface.equals(getTyp())) {
                throw new UnsupportedOperationException("keine Konstruktoren bei Interfaces erlaubt");
            }
            return super.addAll(index, c);
        }

        @Override
        public void add(int index, Konstruktor element) {
            if (KlassifiziererTyp.Interface.equals(getTyp())) {
                throw new UnsupportedOperationException("keine Konstruktoren bei Interfaces erlaubt");
            }
            super.add(index, element);
        }

        @Override
        public boolean offer(Konstruktor e) {
            if (KlassifiziererTyp.Interface.equals(getTyp())) {
                throw new UnsupportedOperationException("keine Konstruktoren bei Interfaces erlaubt");
            }
            return super.offer(e);
        }

        @Override
        public boolean offerFirst(Konstruktor e) {
            if (KlassifiziererTyp.Interface.equals(getTyp())) {
                throw new UnsupportedOperationException("keine Konstruktoren bei Interfaces erlaubt");
            }
            return super.offerFirst(e);
        }

        @Override
        public boolean offerLast(Konstruktor e) {
            if (KlassifiziererTyp.Interface.equals(getTyp())) {
                throw new UnsupportedOperationException("keine Konstruktoren bei Interfaces erlaubt");
            }
            return super.offerLast(e);
        }

        @Override
        public void push(Konstruktor e) {
            if (KlassifiziererTyp.Interface.equals(getTyp())) {
                throw new UnsupportedOperationException("keine Konstruktoren bei Interfaces erlaubt");
            }
            super.push(e);
        }
    }

// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    @JsonSerialize(converter = EnumPropertyZuStringKonverter.class)
    private final JsonEnumProperty<KlassifiziererTyp> typ;
    private final JsonObjectProperty<Modifizierer> sichtbarkeit;
    private final Programmiersprache programmiersprache;
    private final JsonStringProperty paket;
    private final JsonStringProperty name;
    private final JsonStringProperty superklasse;
    private final ObservableList<String> interfaceListe;
    private final ObservableList<Attribut> attribute;
    @JsonManagedReference
    private final ObservableList<Methode> methoden;
    private final ObservableList<Konstruktor> konstruktoren;
    @JsonIgnore
    private final List<Object> schwacheUeberwacher;
    @JsonIgnore
    private Sprache sprache;
    @JsonIgnore
    private Validierung nameValidierung;
    @JsonIgnore
    private ObservableSet<StringProperty> klassenNamen;

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public UMLKlassifizierer(KlassifiziererTyp typ, Programmiersprache programmiersprache, String name) {
        this.typ = new JsonEnumProperty<>(this, "typ", typ);
        this.sichtbarkeit = new JsonObjectProperty<>(this, "sichtbarkeit", Modifizierer.PUBLIC);
        this.programmiersprache = Objects.requireNonNull(programmiersprache);
        this.paket = new JsonStringProperty(this, "paket", "");
        this.name = new JsonStringProperty(this, "name", name);
        this.superklasse = new JsonStringProperty(this, "superklasse", "");
        this.interfaceListe = FXCollections.observableList(new LinkedList<>());
        this.attribute = FXCollections.observableList(new LinkedList<>());
        this.methoden = FXCollections.observableList(new LinkedList<>());
        this.konstruktoren = FXCollections.observableList(new KonstruktorLinkedList());
        this.schwacheUeberwacher = new LinkedList<>();
        this.sprache = new Sprache();
        boolean spracheGesetzt = SprachUtil.setUpSprache(sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
                "UMLKlassifiziererBearbeitenDialog");
        if (!spracheGesetzt) {
            sprache.ignoriereSprachen();
        }

        this.attribute.addListener(new ListenEditierUeberwacher<>(this.attribute, this));
        this.methoden.addListener(new ListenEditierUeberwacher<>(this.methoden, this));
        this.konstruktoren.addListener(new ListenEditierUeberwacher<>(this.konstruktoren, this));
        this.ueberwachePropertyAenderung(this.name, getId() + "_klassifizierername");
        this.ueberwachePropertyAenderung(this.paket, getId() + "_paket");
        this.ueberwachePropertyAenderung(this.typ, getId() + "_klassifizierertyp");
        this.ueberwachePropertyAenderung(this.superklasse, getId() + "_superklasse");
        this.ueberwachePropertyAenderung(this.sichtbarkeit, getId() + "_sichtbarkeit");
        ueberwacheGetterUndSetter();
        ueberwacheTyp();
        this.interfaceListe.addListener(new ListenAenderungsUeberwacher<>(this.interfaceListe, this));
        
        ListChangeListener<Attribut> attributUeberwacher = aenderung -> {
            while (aenderung.next()) {
                for (var attributHinzu : aenderung.getAddedSubList()) {
                    attributHinzu.setUMLKlassifizierer(this);
                }
            }
        };
        this.schwacheUeberwacher.add(attributUeberwacher);
        this.attribute.addListener(new WeakListChangeListener<>(attributUeberwacher));
        
        ListChangeListener<Methode> methodenUeberwacher = aenderung -> {
            while (aenderung.next()) {
                for (var methodeHinzu : aenderung.getAddedSubList()) {
                    methodeHinzu.setUMLKlassifizierer(this);
                }
            }
        };
        this.schwacheUeberwacher.add(methodenUeberwacher);
        this.methoden.addListener(new WeakListChangeListener<>(methodenUeberwacher));
        
        ListChangeListener<Konstruktor> konstruktorUeberwacher = aenderung -> {
            while (aenderung.next()) {
                for (var konstruktorHinzu : aenderung.getAddedSubList()) {
                    konstruktorHinzu.setName(getName());
                    konstruktorHinzu.setUMLKlassifizierer(this);
                }
            }
        };
        this.schwacheUeberwacher.add(konstruktorUeberwacher);
        this.konstruktoren.addListener(new WeakListChangeListener<>(konstruktorUeberwacher));

        ChangeListener<String> namenUeberwacher = (p, alt, neuerName) -> {
            for (Konstruktor k : konstruktoren) {
                k.setName(neuerName);
            }
        };
        schwacheUeberwacher.add(namenUeberwacher);
        this.name.addListener(new WeakChangeListener<>(namenUeberwacher));
    }

    @JsonCreator
    protected UMLKlassifizierer(@JsonProperty("typ") KlassifiziererTyp typ,
            @JsonProperty("sichtbarkeit") Modifizierer sichtbarkeit,
            @JsonProperty("programmiersprache") Programmiersprache programmiersprache,
            @JsonProperty("paket") String paket, @JsonProperty("name") String name,
            @JsonProperty("superklasse") String superklasse,
            @JsonProperty("interfaceListe") List<String> interfaceListe, @JsonProperty("interfaces") String interfaces,
            @JsonProperty("position") Position position, @JsonProperty("attribute") List<Attribut> attribute,
            @JsonProperty("methoden") List<Methode> methoden,
            @JsonProperty("konstruktoren") List<Konstruktor> konstruktoren) {
        this(typ, programmiersprache, name);
        this.getPosition().setPosition(position);
        this.setPaket(paket);
        this.setSuperklasse(superklasse);
        this.setSichtbarkeit(sichtbarkeit);
        if (interfaceListe != null) {
            this.interfaceListe.addAll(interfaceListe);
        }
        if (interfaces != null && !interfaces.isBlank()) {
            this.interfaceListe.add(interfaces);
        }

        for (var attribut : attribute) {
            if (attribut.hatGetter()) {
                initialisiereGetterBindung(attribut, methoden);
            }
            if (attribut.hatSetter()) {
                initialisiereSetterBindung(attribut, methoden);
            }
        }

        this.attribute.addAll(attribute);
        this.methoden.addAll(methoden);
        if (!KlassifiziererTyp.Interface.equals(typ) && konstruktoren != null) {
            this.konstruktoren.addAll(konstruktoren);
        }
    }

    private void initialisiereGetterBindung(Attribut attribut, List<Methode> methoden) {
        var mList = methoden.stream().filter(m -> m.istGetter() && m.getAttribut().equals(attribut)).toList();
        if (!mList.isEmpty()) {
            var methode = mList.get(0);
            var getter = Methode.erstelleGetter(attribut, programmiersprache);
            getter.setName(methode.getName());
            getter.setSichtbarkeit(methode.getSichtbarkeit());
            getter.setzeFinal(methode.istFinal());
            getter.setzeAbstrakt(methode.istAbstrakt());
            attribut.setGetter(getter);
            int index = methoden.indexOf(methode);
            methoden.set(index, getter);
            for (var m : mList) {
                try {
                    m.close();
                } catch (Exception e) {
                    //
                }
            }
        }
    }

    private void initialisiereSetterBindung(Attribut attribut, List<Methode> methoden) {
        var mList = methoden.stream().filter(m -> m.istSetter() && m.getAttribut().equals(attribut)).toList();
        if (!mList.isEmpty()) {
            var methode = mList.get(0);
            var setter = Methode.erstelleSetter(attribut, programmiersprache);
            setter.setName(methode.getName());
            setter.setSichtbarkeit(methode.getSichtbarkeit());
            setter.setzeFinal(methode.istFinal());
            setter.setzeAbstrakt(methode.istAbstrakt());
            attribut.setSetter(setter);
            int index = methoden.indexOf(methode);
            methoden.set(index, setter);
            for (var m : mList) {
                try {
                    m.close();
                } catch (Exception e) {
                    //
                }
            }
        }
    }

    private void initialisiereValidierung(UMLProjekt projekt) {
        klassenNamen = projekt.getElementNamen();
        BooleanBinding gleicherName = Bindings.createBooleanBinding(
                () -> klassenNamen.stream().anyMatch(kn -> {
                    return getName().equals(kn.get()) && kn.getBean() != this;
                }),
                klassenNamen, name);
        this.nameValidierung = Validierung.of(gleicherName.not(), 
                        sprache.getTextProperty("klassennameValidierung2", "Ein Element mit diesem Namen ist bereits vorhanden"))
                .and(name.isNotEmpty(),
                        sprache.getTextProperty("klassennameValidierung", "Der Klassenname muss angegeben werden"))
                .build();
        
    }

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

    @Override
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getNameVollstaendig() {
        if (getPaket() != null && !getPaket().isBlank()) {
            return getPaket() + ":" + getName();
        } else {
            return getName();
        }
    }

    public KlassifiziererTyp getTyp() {
        return typ.get();
    }

    public void setTyp(KlassifiziererTyp typ) {
        this.typ.set(typ);
    }

    public ObjectProperty<KlassifiziererTyp> typProperty() {
        return typ;
    }

    public String getSuperklasse() {
        return superklasse.get();
    }

    public void setSuperklasse(String superklasse) {
        this.superklasse.set(superklasse);
    }

    public StringProperty superklasseProperty() {
        return superklasse;
    }

    public ObservableList<String> getInterfaces() {
        return interfaceListe;
    }

    public Modifizierer getSichtbarkeit() {
        return sichtbarkeit.get();
    }

    public void setSichtbarkeit(Modifizierer sichtbarkeit) {
        this.sichtbarkeit.set(sichtbarkeit);
    }

    public ObjectProperty<Modifizierer> sichtbarkeitProperty() {
        return sichtbarkeit;
    }

    public String getStereotyp() {
        return typ.get().getStereotyp();
    }

    public String getPaket() {
        return paket.get();
    }

    public void setPaket(String paket) {
        this.paket.set(paket);
    }

    public StringProperty paketProperty() {
        return paket;
    }

    public Programmiersprache getProgrammiersprache() {
        return programmiersprache;
    }

    public ObservableList<Attribut> attributeProperty() {
        return attribute;
    }

    public ObservableList<Methode> methodenProperty() {
        return methoden;
    }

    public ObservableList<Konstruktor> konstruktorProperty() {
        return konstruktoren;
    }

    public void setUMLProjekt(UMLProjekt projekt) {
        this.initialisiereValidierung(projekt);
    }
    
    public Validierung getNameValidierung() {
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
        return "%s@%s [ %s '%s' ]".formatted(this.getClass().getSimpleName(),
                Integer.toHexString(((Object) this).hashCode()), this.getTyp().name(), this.getName());
    }

    @Override
    public int hashCode() {
        return ClassifierUtil.hashAlle(attributeProperty(), methodenProperty(), konstruktorProperty(), getName(),
                getPaket(), getProgrammiersprache(), getPosition(), getTyp(), getSichtbarkeit());
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
        UMLKlassifizierer klassifizierer = (UMLKlassifizierer) obj;

        boolean attributeGleich = ClassifierUtil.pruefeGleichheit(this.attributeProperty(),
                klassifizierer.attributeProperty());
        boolean methodenGleich = ClassifierUtil.pruefeGleichheit(this.methodenProperty(),
                klassifizierer.methodenProperty());
        boolean konstruktorenGleich = ClassifierUtil.pruefeGleichheit(this.konstruktorProperty(),
                klassifizierer.konstruktorProperty());
        boolean nameGleich = Objects.equals(getName(), klassifizierer.getName());
        boolean paketGleich = Objects.equals(getPaket(), klassifizierer.getPaket());
        boolean sichtbarkeitGleich = Objects.equals(getSichtbarkeit(), klassifizierer.getSichtbarkeit());
        boolean programmierspracheGleich = getProgrammiersprache().equals(klassifizierer.getProgrammiersprache());
        boolean typGleich = getTyp().equals(klassifizierer.getTyp());
        boolean positionGleich = getPosition().equals(klassifizierer.getPosition());

        boolean istGleich = attributeGleich && methodenGleich && konstruktorenGleich && nameGleich && paketGleich
                && sichtbarkeitGleich && programmierspracheGleich && positionGleich && typGleich;

        log.finest(() -> """
                istGleich: %s
                   |-- attributeGleich: %s
                   |-- methodenGleich: %s
                   |-- konstruktorenGleich: %s
                   |-- nameGleich: %s
                   |-- paketGleich: %s
                   |-- sichtbarkeitGleich: %s
                   |-- programmierspracheGleich: %s
                   |-- positionGleich: %s
                   ╰-- typGleich: %s""".formatted(istGleich, attributeGleich, methodenGleich, konstruktorenGleich,
                nameGleich, paketGleich, sichtbarkeitGleich, programmierspracheGleich, positionGleich, typGleich));

        return istGleich;
    }

    @Override
    public UMLKlassifizierer erzeugeTiefeKopie() {
        var kopie = new UMLKlassifizierer(getTyp(), getProgrammiersprache(), getName());
        kopie.setPaket(getPaket());
        kopie.getPosition().set(getPosition());
        kopie.setSuperklasse(getSuperklasse());
        kopie.getInterfaces().addAll(getInterfaces());

        Map<String, Attribut> getterUndSetterPosition = new HashMap<>();
        for (var attribut : attribute) {
            var attributKopie = attribut.erzeugeTiefeKopie();
            kopie.attribute.add(attributKopie);

            if (attributKopie.hatGetter() || attributKopie.hatSetter()) {
                getterUndSetterPosition.put(attributKopie.getName(), attributKopie);
            }
        }

        for (var methode : methoden) {
            String name = methode.getAttribut() == null ? "" : methode.getAttribut().getName();
            Attribut attributKopie = getterUndSetterPosition.getOrDefault(name, null);
            var methodeKopie = methode.erzeugeTiefeKopie(attributKopie);
            kopie.methoden.add(methodeKopie);
        }

        for (var konstrukor : konstruktoren) {
            kopie.konstruktoren.add(konstrukor.erzeugeTiefeKopie());
        }

        return kopie;
    }

    @Override
    public void schliesse() throws Exception {
        log.finest(() -> this + " leere ueberwacher");
        this.schwacheUeberwacher.clear();

        log.finest(() -> this + " leere inhalt");
        for (var attribut : attribute) {
            attribut.close();
        }
        for (var methode : methoden) {
            methode.close();
        }
        for (var konstruktor : konstruktoren) {
            konstruktor.close();
        }
        methoden.clear();
        attribute.clear();
        konstruktoren.clear();
        super.schliesse();

        for (var attribut : this.getClass().getDeclaredFields()) {
            try {
                if (!Modifier.isStatic(attribut.getModifiers())) {
                    attribut.setAccessible(true);
                    if (attribut.get(this) instanceof Closeable c) {
                        log.finest(() -> this + " schliesse " + c);
                        c.close();
                    }
                    attribut.set(this, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

    private void ueberwacheGetterUndSetter() {
        ListChangeListener<Attribut> ueberwacher = (Change<? extends Attribut> aenderung) -> {
            final Map<Attribut, List<ChangeListener<?>>> attributUeberwacher = new HashMap<>();
            while (aenderung.next()) {
                for (var attribut : aenderung.getRemoved()) {
                    try {
                        attributUeberwacher.remove(attribut).clear();
                    } catch (Exception e) {
                        /* ignorieren */ }

                    if (attribut.getGetter() != null) {
                        methoden.remove(attribut.getGetter());
                    }
                    if (attribut.getSetter() != null) {
                        methoden.remove(attribut.getSetter());
                    }
                }
                for (var attribut : aenderung.getAddedSubList()) {
                    List<ChangeListener<?>> ueberwacherListe = new ArrayList<>(2);
                    final ChangeListener<Boolean> getterUeberwacher = (p, hatteGet, hatGetter) -> {
                        if (hatGetter == hatteGet) {
                            return;
                        }

                        if (hatGetter) {
                            if (attribut.getGetter() == null) {
                                attribut.setGetter(Methode.erstelleGetter(attribut, getProgrammiersprache()));
                            }
                            methoden.add(attribut.getGetter());
                        } else {
                            methoden.remove(attribut.getGetter());
                        }
                    };
                    attribut.hatGetterProperty().addListener(getterUeberwacher);

                    final ChangeListener<Boolean> setterUeberwacher = (p, hatteSet, hatSetter) -> {
                        if (hatSetter == hatteSet) {
                            return;
                        }

                        if (hatSetter) {
                            if (attribut.getSetter() == null) {
                                attribut.setSetter(Methode.erstelleSetter(attribut, getProgrammiersprache()));
                            }
                            methoden.add(attribut.getSetter());
                        } else {
                            methoden.remove(attribut.getSetter());
                        }
                    };
                    attribut.hatSetterProperty().addListener(setterUeberwacher);

                    ueberwacherListe.add(getterUeberwacher);
                    ueberwacherListe.add(setterUeberwacher);
                    attributUeberwacher.put(attribut, ueberwacherListe);
                }
            }
        };

        this.attribute.addListener(new WeakListChangeListener<>(ueberwacher));
        this.schwacheUeberwacher.add(ueberwacher);
    }

    private void ueberwacheTyp() {
        ChangeListener<KlassifiziererTyp> ueberwacher = (p, alt, neuerKlassifizierer) -> {
            if (alt != neuerKlassifizierer) {
                this.attribute.forEach(a -> updateAttribut(a, neuerKlassifizierer));
                this.methoden.forEach(m -> updateMethode(m, neuerKlassifizierer));
                if (KlassifiziererTyp.Interface.equals(neuerKlassifizierer)) {
                    this.konstruktoren.clear();
                }
            }
        };

        this.typ.addListener(new WeakChangeListener<>(ueberwacher));
        this.schwacheUeberwacher.add(ueberwacher);
    }

    private void updateAttribut(Attribut attribut, KlassifiziererTyp typ) {
        boolean instanzAttributeErlaubt = this.programmiersprache.getEigenschaften().erlaubtInstanzAttribute(typ);
        if (!instanzAttributeErlaubt) {
            attribut.setStatisch(true);
        }
    }

    private void updateMethode(Methode methode, KlassifiziererTyp typ) {
        boolean abstraktErlaubt = this.programmiersprache.getEigenschaften().erlaubtAbstrakteMethode(typ);
        boolean abstraktErzwungen = !this.programmiersprache.getEigenschaften().erlaubtNichtAbstrakteMethode(typ);

        if (abstraktErzwungen) {
            methode.setzeAbstrakt(true);
        }

        if (!abstraktErlaubt) {
            methode.setzeAbstrakt(false);
        }

        if (typ.equals(KlassifiziererTyp.Interface) && !methode.istGetter() && !methode.istSetter()
                && !methode.istStatisch()) {
            // Methoden in Interface standardmaessig abstrakt setzen
            methode.setzeAbstrakt(true);
        }
    }
}