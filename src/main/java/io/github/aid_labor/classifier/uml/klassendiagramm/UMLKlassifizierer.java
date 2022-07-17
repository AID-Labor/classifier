/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.klassendiagramm;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
import io.github.aid_labor.classifier.basis.json.JsonEnumProperty;
import io.github.aid_labor.classifier.basis.json.JsonEnumProperty.EnumPropertyZuStringKonverter;
import io.github.aid_labor.classifier.basis.json.JsonObjectProperty;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.projekt.editierung.ListenAenderungsUeberwacher;
import io.github.aid_labor.classifier.basis.projekt.editierung.ListenEditierUeberwacher;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Attribut;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Methode;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Modifizierer;
import io.github.aid_labor.classifier.uml.programmierung.Programmiersprache;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;


public class UMLKlassifizierer extends UMLBasisElement {
	private static final Logger log = Logger.getLogger(UMLKlassifizierer.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
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
	@JsonIgnore
	private final List<Object> schwacheUeberwacher;
	
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
		this.schwacheUeberwacher = new LinkedList<>();
		
		this.attribute.addListener(new ListenEditierUeberwacher<>(this.attribute, this));
		this.methoden.addListener(new ListenEditierUeberwacher<>(this.methoden, this));
		this.ueberwachePropertyAenderung(this.name, getId() + "_klassifizierername");
		this.ueberwachePropertyAenderung(this.paket, getId() + "_paket");
		this.ueberwachePropertyAenderung(this.typ, getId() + "_klassifizierertyp");
		this.ueberwachePropertyAenderung(this.superklasse, getId() + "_superklasse");
		ueberwacheGetterUndSetter();
		ueberwacheTyp();
		this.interfaceListe.addListener(new ListenAenderungsUeberwacher<>(this.interfaceListe, this));
	}
	
	@JsonCreator
	protected UMLKlassifizierer(@JsonProperty("typ") KlassifiziererTyp typ,
			@JsonProperty("sichtbarkeit") Modifizierer sichtbarkeit,
			@JsonProperty("programmiersprache") Programmiersprache programmiersprache,
			@JsonProperty("paket") String paket, @JsonProperty("name") String name,
			@JsonProperty("superklasse") String superklasse, 
			@JsonProperty("interfaceListe") List<String> interfaceListe,
			@JsonProperty("interfaces") String interfaces,
			@JsonProperty("position") Position position, @JsonProperty("attribute") List<Attribut> attribute,
			@JsonProperty("methoden") List<Methode> methoden) {
		this(typ, programmiersprache, name);
		this.getPosition().setPosition(position);
		this.setPaket(paket);
		this.setSuperklasse(superklasse);
		this.setSichtbarkeit(sichtbarkeit);
		if (interfaceListe != null) {
			this.interfaceListe.addAll(interfaceListe);
		}
		if (interfaces != null) {
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
		return ClassifierUtil.hashAlle(attributeProperty(), methodenProperty(), getName(), getPaket(),
				getProgrammiersprache(), getPosition(), getTyp());
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
		boolean nameGleich = Objects.equals(getName(), klassifizierer.getName());
		boolean paketGleich = Objects.equals(getPaket(), klassifizierer.getPaket());
		boolean programmierspracheGleich = getProgrammiersprache().equals(klassifizierer.getProgrammiersprache());
		boolean typGleich = getTyp().equals(klassifizierer.getTyp());
		boolean positionGleich = getPosition().equals(klassifizierer.getPosition());
		
		boolean istGleich = attributeGleich && methodenGleich && nameGleich && paketGleich && programmierspracheGleich
				&& positionGleich && typGleich;
		
		log.finest(() -> """
				istGleich: %s
				   |-- attributeGleich: %s
				   |-- methodenGleich: %s
				   |-- nameGleich: %s
				   |-- paketGleich: %s
				   |-- programmierspracheGleich: %s
				   |-- positionGleich: %s
				   ╰-- typGleich: %s""".formatted(istGleich, attributeGleich, methodenGleich, nameGleich, paketGleich,
				programmierspracheGleich, positionGleich, typGleich));
		
		return istGleich;
	}
	
	@Override
	public UMLKlassifizierer erzeugeTiefeKopie() {
		var kopie = new UMLKlassifizierer(getTyp(), getProgrammiersprache(), getName());
		kopie.setPaket(getPaket());
		kopie.getPosition().set(getPosition());
		
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
		
		return kopie;
	}
	
	@Override
	public void close() throws Exception {
		log.finest(() -> this + " leere ueberwacher");
		this.schwacheUeberwacher.clear();
		
		log.finest(() -> this + " leere inhalt");
		for (var attribut : attribute) {
			attribut.close();
		}
		for (var methode : methoden) {
			methode.close();
		}
		methoden.clear();
		attribute.clear();
		super.close();
		
		for (var attribut : this.getClass().getDeclaredFields()) {
			try {
				if (!Modifier.isStatic(attribut.getModifiers())) {
					attribut.setAccessible(true);
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