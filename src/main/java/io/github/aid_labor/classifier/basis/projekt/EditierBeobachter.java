/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.projekt;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;


/**
 * Objekt, das Editierungen von anderen Objekten ueberwacht und verarbeitet.
 * Zur Ueberwachung eines editierbaren Objekts muss sich der Beobachter beim editierbaren
 * Objekt mit der Methode {@link Editierbar#meldeAn(EditierBeobachter)} registrieren.
 * 
 * @author Tim Muehle
 *
 */
public interface EditierBeobachter {
	
	public static final Logger log = Logger.getLogger(EditierBeobachter.class.getName());
	
	/**
	 * Methode, die eine Instanz von {@link Editierbar} aufruft, um ueber eine Editierung
	 * zu informieren.
	 * 
	 * Typischerweise wird in dieser Methode der uebergebene {@link EditierBefehl} in einen
	 * Puffer eingereiht, um die Editierung ggf. spaeter widerrufen zu koennen.
	 * 
	 * @param editierung Editierung, die zum Aufruf dieser Methode gefuehrt hat
	 */
	public void verarbeiteEditierung(EditierBefehl editierung);
	
	/**
	 * Installiert einen {@code ChangeListener}, der diesen {@code EditierBeobachter} bei
	 * Aenderung der uebergebenen Eigenschaft mit einem entsprechenden {@link EditierBefehl}
	 * benachrichtigt
	 * 
	 * @param <T>      Typparameter des Uebergabeparameters
	 * @param property Eigenschaft, die ueber Anderungen informiert
	 */
	public default <T> void ueberwachePropertyAenderung(Property<T> property) {
		property.addListener(new PropertyUeberwachung<>(this, property));
	}
	
	public static class PropertyUeberwachung<T> implements ChangeListener<T> {
		
		private boolean wiederhole;
		private final WeakReference<EditierBeobachter> beobachterRef;
		private final WeakReference<Property<T>> propertyRef;
		
		private PropertyUeberwachung(EditierBeobachter beobachter, Property<T> property) {
			this.wiederhole = false;
			this.beobachterRef = new WeakReference<>(beobachter);
			this.propertyRef = new WeakReference<>(property);
		}
		
		@Override
		public void changed(ObservableValue<? extends T> aufrufer, T alterWert, T neuerWert) {
			if (!wiederhole) {
				beobachterRef.get().verarbeiteEditierung(new EditierBefehl() {
					
					@Override
					public void wiederhole() {
						wiederhole = true;
						propertyRef.get().setValue(neuerWert);
						wiederhole = false;
					}
					
					@Override
					public void macheRueckgaengig() {
						wiederhole = true;
						propertyRef.get().setValue(alterWert);
						wiederhole = false;
					}
					
					@Override
					public String toString() {
						return "EditierBefehl: " + beobachterRef.get() + "->" + propertyRef.get()
								+ " { alt: %s neu: %s }".formatted(alterWert, neuerWert);
					}
				});
			}
		}
		
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public static <E extends Editierbar> ListChangeListener<E> ueberwacheListenAenderung(
			List<E> liste, EditierBeobachter beobachter,
			Consumer<EditierBefehl> verarbeitung) {
		return aenderung -> {
			List<EditierBefehl> befehle = new LinkedList<>();
			while (aenderung.next()) {
				if (aenderung.wasPermutated()) {
					int[] neueOrdnung = new int[aenderung.getList().size()];
					for (int i = 0; i < neueOrdnung.length; i++) {
						neueOrdnung[i] = aenderung.getPermutation(i);
					}
					log.finer(() -> "%s: \n    -> Reihenfolge geaendert [%s]"
							.formatted(beobachter, Arrays.toString(neueOrdnung)));
					log.severe(() -> "EditierBefehl fuer Permutated implementieren!!!");
					// TODO EditierBefehl fuer Permutated implementieren!!!
				}
				if (aenderung.wasRemoved()) {
					log.finer(() -> "%s: \n    -> entfernt [%s]".formatted(beobachter,
							Arrays.toString(aenderung.getRemoved().toArray())));
					for (E entfernt : aenderung.getRemoved()) {
						entfernt.meldeAb(beobachter);
					}
					var befehl = new EditierBefehl() {
						
						int startIndex = aenderung.getFrom();
						List<E> entfernteAttribute = Collections.unmodifiableList(
								new ArrayList<>(aenderung.getAddedSubList()));
						
						@Override
						public void wiederhole() {
							liste.removeAll(entfernteAttribute);
						}
						
						@Override
						public void macheRueckgaengig() {
							liste.addAll(startIndex, entfernteAttribute);
						}
					};
					befehle.add(befehl);
				}
				if (aenderung.wasAdded()) {
					log.finer(() -> "%s: \n    -> hinzugefuegt [%s]".formatted(beobachter,
							Arrays.toString(aenderung.getAddedSubList().toArray())));
					for (E hinzu : aenderung.getAddedSubList()) {
						hinzu.meldeAn(beobachter);
					}
					var befehl = new EditierBefehl() {
						
						int startIndex = aenderung.getFrom();
						List<E> neueAttribute = Collections.unmodifiableList(
								new ArrayList<>(aenderung.getAddedSubList()));
						
						@Override
						public void wiederhole() {
							liste.addAll(startIndex, neueAttribute);
						}
						
						@Override
						public void macheRueckgaengig() {
							liste.removeAll(neueAttribute);
						}
					};
					befehle.add(befehl);
				}
				if (aenderung.wasUpdated()) {
					log.finer(() -> "%s: \n    -> Update [%d bis %d]".formatted(beobachter,
							aenderung.getFrom(), aenderung.getTo()));
					log.severe(() -> "EditierBefehl fuer Update implementieren!!!");
					// TODO EditierBefehl fuer Update implementieren!!!
				}
			}
			
			for (EditierBefehl editierBefehl : befehle) {
				verarbeitung.accept(editierBefehl);
			}
		};
	}
}