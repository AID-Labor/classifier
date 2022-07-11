/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.json.JsonBooleanProperty;
import io.github.aid_labor.classifier.basis.json.JsonObjectProperty;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.projekt.EditierbarBasis;
import io.github.aid_labor.classifier.basis.projekt.EditierbarerBeobachter;
import io.github.aid_labor.classifier.uml.programmierung.Programmiersprache;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Methode extends EditierbarBasis implements EditierbarerBeobachter {
	private static final Logger log = Logger.getLogger(Methode.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static long naechsteId = 0;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public static Methode erstelleGetter(Attribut attribut, Programmiersprache programmiersprache) {
		return new Methode(Modifizierer.PUBLIC, attribut.getDatentyp(), attribut.istStatisch(), true, false, attribut,
				null, programmiersprache);
	}
	
	public static Methode erstelleSetter(Attribut attribut, Programmiersprache programmiersprache) {
		return new Methode(Modifizierer.PUBLIC, attribut.getDatentyp(), attribut.istStatisch(), false, true, null,
				attribut, programmiersprache);
	}
	
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
	private final Datentyp rueckgabeTyp;
	private ObservableList<Parameter> parameterListe;
	private final JsonBooleanProperty istAbstrakt;
	private final JsonBooleanProperty istFinal;
	private final JsonBooleanProperty istStatisch;
	private final boolean istGetter;
	private final boolean istSetter;
	@JsonManagedReference("getterAttribut")
	private Attribut getterAttribut;
	@JsonManagedReference("setterAttribut")
	private Attribut setterAttribut;
	private final Programmiersprache programmiersprache;
	@JsonIgnore
	private final List<Object> beobachterListe;
	@JsonIgnore
	private final long id;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public Methode(Modifizierer sichtbarkeit, Datentyp rueckgabeTyp, Programmiersprache programmiersprache) {
		this(sichtbarkeit, rueckgabeTyp, false, programmiersprache);
	}
	
	public Methode(Modifizierer sichtbarkeit, Datentyp rueckgabeTyp, boolean istStatisch,
			Programmiersprache programmiersprache) {
		this(sichtbarkeit, rueckgabeTyp, istStatisch, false, false, null, null, programmiersprache);
	}
	
	protected Methode(Modifizierer sichtbarkeit, Datentyp rueckgabeTyp, boolean istStatisch, boolean istGetter,
			boolean istSetter, Attribut getterAttribut, Attribut setterAttribut,
			Programmiersprache programmiersprache) {
		if (istGetter && istSetter) {
			throw new IllegalStateException("Kann nur getter oder setter sein, nicht beides!");
		}
		this.id = naechsteId++;
		this.beobachterListe = new LinkedList<>();
		
		this.name = new JsonStringProperty(this, "methodenname", "");
		this.istStatisch = new JsonBooleanProperty(this, "istStatisch", istStatisch);
		if (istGetter) {
			this.rueckgabeTyp = Objects.requireNonNull(getterAttribut.getDatentyp().erzeugeTiefeKopie());
			this.rueckgabeTyp.typNameProperty().bind(getterAttribut.getDatentyp().typNameProperty());
			this.istStatisch.bindBidirectional(getterAttribut.istStatischProperty());
			this.name.set("get" + Character.toUpperCase(getterAttribut.getName().charAt(0))
					+ getterAttribut.getName().substring(1));
			this.parameterListe = FXCollections.emptyObservableList();
			getterAttribut.setGetter(this);
		} else if (istSetter) {
			this.rueckgabeTyp = programmiersprache.getEigenschaften().getVoid();
			this.istStatisch.bindBidirectional(setterAttribut.istStatischProperty());
			this.name.set("set" + Character.toUpperCase(setterAttribut.getName().charAt(0))
					+ setterAttribut.getName().substring(1));
			var param = new Parameter(setterAttribut.getDatentyp().erzeugeTiefeKopie(), setterAttribut.getName());
			param.nameProperty().bind(setterAttribut.nameProperty());
			param.getDatentyp().typNameProperty().bind(setterAttribut.getDatentyp().typNameProperty());
			this.parameterListe = FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(param));
			setterAttribut.setSetter(this);
		} else {
			this.rueckgabeTyp = Objects.requireNonNull(rueckgabeTyp);
			this.ueberwachePropertyAenderung(this.rueckgabeTyp.typNameProperty(), id + "_methode_rueckgabetyp");
			this.parameterListe = FXCollections.observableList(new LinkedList<>());
			this.ueberwachePropertyAenderung(this.istStatisch, id + "_methode_statisch");
		}
		
		this.sichtbarkeit = new JsonObjectProperty<>(this, "sichtbarkeit", sichtbarkeit);
		this.istAbstrakt = new JsonBooleanProperty(this, "istAbstrakt", false);
		this.istFinal = new JsonBooleanProperty(this, "istFinal", false);
		this.istGetter = istGetter;
		this.istSetter = istSetter;
		this.getterAttribut = getterAttribut;
		this.setterAttribut = setterAttribut;
		this.programmiersprache = programmiersprache;
		
		this.ueberwachePropertyAenderung(this.sichtbarkeit, id + "_methode_sichtbarkeit");
		this.ueberwachePropertyAenderung(this.name, id + "_methode_name");
		this.ueberwachePropertyAenderung(this.istAbstrakt, id + "_methode_abstrakt");
		this.ueberwachePropertyAenderung(this.istFinal, id + "_methode_final");
		
		this.ueberwacheListenAenderung(this.parameterListe);
	}
	
	@JsonCreator
	protected Methode(@JsonProperty("sichtbarkeit") Modifizierer sichtbarkeit, @JsonProperty("name") String name,
			@JsonProperty("rueckgabeTyp") Datentyp rueckgabeTyp,
			@JsonProperty("parameterListe") List<Parameter> parameterListe,
			@JsonProperty("getterAttribut") Attribut getterAttribut,
			@JsonProperty("setterAttribut") Attribut setterAttribut, @JsonProperty("istAbstrakt") boolean istAbstrakt,
			@JsonProperty("istFinal") boolean istFinal, @JsonProperty("istStatisch") boolean istStatisch,
			@JsonProperty("istGetter") boolean istGetter, @JsonProperty("istSetter") boolean istSetter,
			@JsonProperty("programmiersprache") Programmiersprache programmiersprache) {
		this(sichtbarkeit, rueckgabeTyp, istStatisch, istGetter, istSetter, getterAttribut, setterAttribut,
				programmiersprache);
		if (!istGetter && !istSetter && parameterListe != null) {
			this.parameterListe.addAll(parameterListe);
		}
		this.setName(name);
		this.setzeAbstrakt(istAbstrakt);
		this.setzeFinal(istFinal);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
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
	
	public Datentyp getRueckgabeTyp() {
		return rueckgabeTyp;
	}
	
	@JsonProperty("istAbstrakt")
	public boolean istAbstrakt() {
		return istAbstrakt.get();
	}
	
	public void setzeAbstrakt(boolean istAbstrakt) {
		this.istAbstrakt.set(istAbstrakt);
	}
	
	public boolean istFinal() {
		return istFinal.get();
	}
	
	public void setzeFinal(boolean istFinal) {
		this.istFinal.set(istFinal);
	}
	
	public boolean istStatisch() {
		return istStatisch.get();
	}
	
	public void setzeStatisch(boolean istStatisch) {
		this.istStatisch.set(istStatisch);
	}
	
	public boolean istGetter() {
		return istGetter;
	}
	
	public boolean istSetter() {
		return istSetter;
	}
	
	public Attribut getAttribut() {
		if (istGetter) {
			return getterAttribut;
		}
		if (istSetter) {
			return setterAttribut;
		}
		return null;
	}
	
	public ObservableList<Parameter> parameterListeProperty() {
		return parameterListe;
	}
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ObjectProperty<Modifizierer> sichtbarkeitProperty() {
		return sichtbarkeit;
	}
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public BooleanProperty istAbstraktProperty() {
		return istAbstrakt;
	}
	
	public BooleanProperty istFinalProperty() {
		return istFinal;
	}
	
	public BooleanProperty istStatischProperty() {
		return istStatisch;
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
		return "Methode <%s %s %s(%s) {%s %s}>".formatted(getSichtbarkeit(), getRueckgabeTyp().getTypName(), getName(),
				parameterListeProperty().stream().map(p -> String.join(" ", p.getDatentyp().getTypName(), p.getName()))
						.collect(Collectors.joining(", ")),
				istAbstrakt() ? " abstract" : "", istFinal() ? "final " : "");
	}
	
	@Override
	public int hashCode() {
		return ClassifierUtil.hashAlle(istAbstrakt(), istFinal(), getName(), parameterListeProperty(),
				getRueckgabeTyp(), getSichtbarkeit(), istStatisch(), istGetter(), istSetter());
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
		Methode m = (Methode) obj;
		
		boolean istAbstraktGleich = istAbstrakt() == m.istAbstrakt();
		boolean istFinalGleich = istFinal() == m.istFinal();
		boolean nameGleich = Objects.equals(getName(), m.getName());
		boolean parameterListeGleich = ClassifierUtil.pruefeGleichheit(this.parameterListeProperty(),
				m.parameterListeProperty());
		boolean rueckgabeTypGleich = Objects.equals(getRueckgabeTyp(), m.getRueckgabeTyp());
		boolean sichtbarkeitGleich = Objects.equals(getSichtbarkeit(), m.getSichtbarkeit());
		boolean statischGleich = this.istStatisch() == m.istStatisch();
		boolean getterGleich = this.istGetter() == m.istGetter();
		boolean setterGleich = this.istSetter() == m.istSetter();
		boolean programmierspracheGleich = Objects.equals(this.programmiersprache, m.programmiersprache);
		
		boolean istGleich = istAbstraktGleich && istFinalGleich && nameGleich && parameterListeGleich
				&& rueckgabeTypGleich && sichtbarkeitGleich && getterGleich && statischGleich && statischGleich
				&& programmierspracheGleich;
		
		log.finest(() -> """
				istGleich: %s
				   |-- istAbstraktGleich: %s
				   |-- istFinalGleich: %s
				   |-- nameGleich: %s
				   |-- parameterListeGleich: %s
				   |-- rueckgabeTypGleich: %s
				   |-- sichtbarkeitGleich: %s
				   |-- getterGleich: %s
				   |-- setterGleich: %s
				   |-- statischGleich: %s
				   ╰-- programmierspracheGleich: %s""".formatted(istGleich, istAbstraktGleich, istFinalGleich,
				nameGleich, parameterListeGleich, rueckgabeTypGleich, sichtbarkeitGleich, getterGleich, setterGleich,
				statischGleich, programmierspracheGleich));
		
		return istGleich;
	}
	
	public Methode erzeugeTiefeKopie(Attribut attribut) {
		if (getterAttribut != null && !getterAttribut.equals(attribut)) {
			throw new IllegalArgumentException(
					"Uebergebenes Attribut %s und Getter %s stimmen nicht überein".formatted(attribut, getterAttribut));
		}
		if (setterAttribut != null && !setterAttribut.equals(attribut)) {
			throw new IllegalArgumentException(
					"Uebergebenes Attribut %s und Setter %s stimmen nicht überein".formatted(attribut, setterAttribut));
		}
		
		Attribut getterAttributKopie = getterAttribut == null ? null : attribut;
		Attribut setterAttributKopie = setterAttribut == null ? null : attribut;
		
		var kopie = new Methode(getSichtbarkeit(), getRueckgabeTyp().erzeugeTiefeKopie(), istStatisch(), istGetter(),
				istSetter(), getterAttributKopie, setterAttributKopie, programmiersprache);
		kopie.setName(getName());
		kopie.setzeAbstrakt(istAbstrakt());
		kopie.setzeFinal(istFinal());
		
		if (!istGetter && !istSetter) {
			for (var parameter : parameterListe) {
				kopie.parameterListe.add(parameter.erzeugeTiefeKopie());
			}
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
		getterAttribut = null;
		setterAttribut = null;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}