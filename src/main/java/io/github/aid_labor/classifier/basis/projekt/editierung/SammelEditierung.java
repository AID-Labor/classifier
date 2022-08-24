/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.projekt.editierung;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SammelEditierung implements EditierBefehl {
	
	private static final Logger log = Logger.getLogger(SammelEditierung.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static class AenderbareWertEditierung<T> implements WertEditierBefehl<T> {
		
		private T vorher;
		private T nachher;
		private final Consumer<T> setter;
		private final String id;
		
		public AenderbareWertEditierung(WertEditierBefehl<T> wertEditierung) {
			this(wertEditierung.id(), wertEditierung.getVorher(), wertEditierung.getNachher(), wertEditierung::set);
		}
		
		public AenderbareWertEditierung(String id, T vorher, T nachher, Consumer<T> setter) {
			this.id = id;
			this.vorher = vorher;
			this.nachher = nachher;
			this.setter = setter;
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
		
		@SuppressWarnings("unused")
		public void setVorher(T vorher) {
			this.vorher = vorher;
		}
		
		public void setNachher(T nachher) {
			this.nachher = nachher;
		}
		
		@Override
		public void set(T wert) {
			setter.accept(wert);
		}
		
		@Override
		public String toString() {
			return "%s {%s -> %s}".formatted(id, vorher, nachher);
		}

		@Override
		public void close() throws Exception {
			// nichts zum Schliessen
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
	
	private Deque<EditierBefehl> befehle;
	private final Map<String, AenderbareWertEditierung<?>> wertEditierungen;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public SammelEditierung() {
		this.befehle = new LinkedList<>();
		this.wertEditierungen = new HashMap<>();
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public <T> void speicherEditierung(EditierBefehl editierung) {
		log.finest(() -> "sammel + " + editierung);
		
		if (WertEditierBefehl.class.isAssignableFrom(editierung.getClass())) {
			@SuppressWarnings("unchecked")
			WertEditierBefehl<T> wertEditierung = (WertEditierBefehl<T>) editierung;
			@SuppressWarnings("unchecked")
			AenderbareWertEditierung<T> vorherigeEditierung = (AenderbareWertEditierung<T>) this.wertEditierungen
					.get(wertEditierung.id());
			if (vorherigeEditierung != null) {
				vorherigeEditierung.setNachher(wertEditierung.getNachher());
			} else {
				vorherigeEditierung = new AenderbareWertEditierung<>(wertEditierung);
				wertEditierungen.put(wertEditierung.id(), vorherigeEditierung);
				befehle.add(vorherigeEditierung);
			}
		} else {
			befehle.add(editierung);
		}
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public void macheRueckgaengig() {
		Deque<EditierBefehl> rueckgaengigSchlange = new LinkedList<>(befehle);
		while (!rueckgaengigSchlange.isEmpty()) {
			var befehl = rueckgaengigSchlange.pollLast();
			log.finest(() -> "Mache rueckgaengig: " + befehl);
			try {
				befehl.macheRueckgaengig();
			} catch (Exception e) {
				log.log(Level.WARNING, e,
						() -> "Ruckgaengig machen von Befehl >%s< fehlgeschlagen".formatted(befehl));
			}
		}
	}
	
	@Override
	public void wiederhole() {
		Deque<EditierBefehl> wiederholSchlange = new LinkedList<>(befehle);
		while (!wiederholSchlange.isEmpty()) {
			var befehl = wiederholSchlange.pollFirst();
			log.finest(() -> "Wiederhole: " + befehl);
			try {
				befehl.wiederhole();
			} catch (Exception e) {
				log.log(Level.WARNING, e,
						() -> "Wiederholen von Befehl >%s< fehlgeschlagen".formatted(befehl));
			}
		}
	}
	
	@Override
	public String toString() {
		return "Sammeleditierung: \n    " + Arrays.toString(befehle.toArray());
	}

	@Override
	public void close() throws Exception {
		for (var befehl : befehle) {
			String entferntStr = befehl.toString();
			try {
				befehl.close();
				log.fine(() -> entferntStr + " wurde geschlossen");
			} catch (Exception e) {
				log.log(Level.CONFIG, e, () -> "Fehler beim Schliessen von " + entferntStr);
			}
		}
	}

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}