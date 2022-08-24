/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.projekt.editierung;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;


/**
 * Objekt, das Editierungen von anderen Objekten ueberwacht und verarbeitet.
 * Zur Ueberwachung eines editierbaren Objekts muss sich der Beobachter beim editierbaren
 * Objekt mit der Methode {@link Editierbar#meldeAn(EditierBeobachter)} registrieren.
 * 
 * @author Tim Muehle
 *
 */
public interface EditierBeobachter extends AutoCloseable {
	
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
	
	public List<Object> getBeobachterListe();
	
	/**
	 * Installiert einen {@code ChangeListener}, der diesen {@code EditierBeobachter} bei
	 * Aenderung der uebergebenen Eigenschaft mit einem entsprechenden {@link EditierBefehl}
	 * benachrichtigt
	 * 
	 * @param <T>      Typparameter des Uebergabeparameters
	 * @param property Eigenschaft, die ueber Anderungen informiert
	 * @param id       ID zur eindeutigen Identifizierung von Editierungen
	 */
	public default <T> void ueberwachePropertyAenderung(Property<T> property, String id) {
		var beobachter = new PropertyUeberwachung<>(this, property, id);
		getBeobachterListe().add(beobachter);
		property.addListener(new WeakChangeListener<>(beobachter));
	}
	
	public static class PropertyUeberwachung<T> implements ChangeListener<T> {
		
		private boolean wiederhole;
		private final WeakReference<EditierBeobachter> beobachterRef;
		private final WeakReference<Property<T>> propertyRef;
		private final String id;
		
		private PropertyUeberwachung(EditierBeobachter beobachter, Property<T> property, String id) {
			this.wiederhole = false;
			this.beobachterRef = new WeakReference<>(beobachter);
			this.propertyRef = new WeakReference<>(property);
			this.id = id;
		}
		
		@Override
		public void changed(ObservableValue<? extends T> aufrufer, T alterWert, T neuerWert) {
			if (!Objects.equals(alterWert, neuerWert) && !wiederhole) {
				beobachterRef.get().verarbeiteEditierung(new PropertyEditierBefehl(alterWert, neuerWert));
			}
		}
		
		private class PropertyEditierBefehl implements WertEditierBefehl<T> {
			private final T vorher;
			private final T nachher;
			
			PropertyEditierBefehl(T vorher, T nachher) {
				this.vorher = vorher;
				this.nachher = nachher;
			}

			@Override
			public String toString() {
				return "EditierBefehl: %s -> %s { alt: %s neu: %s }".formatted(beobachterRef.get(),
						propertyRef.get(), vorher, nachher);
			}
			
			@Override
			public String id() {
				return id;
			}
			
			@Override
			public T getVorher() {
				return vorher;
			}
			
			@Override
			public T getNachher() {
				return nachher;
			}
			
			@Override
			public void set(T wert) {
				wiederhole = true;
				try {
					propertyRef.get().setValue(wert);
				} catch (Exception e) {
					log.log(Level.WARNING, e,
							() -> "Setzen von Property >%s< fehlgeschlagen".formatted(propertyRef.get()));
				}
				wiederhole = false;
			}

			@Override
			public void close() throws Exception {
				// nicht benötigt
			}
		}
	}
	
	public default <E extends Editierbar> void ueberwacheListenAenderung(ObservableList<E> liste) {
		var beobachter = new ListenEditierUeberwacher<>(liste, this);
		getBeobachterListe().add(beobachter);
		liste.addListener(new WeakListChangeListener<>(beobachter));
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
}