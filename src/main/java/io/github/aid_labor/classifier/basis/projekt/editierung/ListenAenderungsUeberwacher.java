/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.projekt.editierung;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ListChangeListener;


/**
 * {@code ListChangeListener} fuer Aenderungen an einer {code ObservableList}, der einen
 * {@link EditierBeobachter} ueber Editierungen an der Liste informiert.
 * 
 * @author Tim Muehle
 *
 * @param <E> Typ der Elemente in der zu beobachtenden Liste
 */
public class ListenAenderungsUeberwacher<E> implements ListChangeListener<E> {
	private static final Logger log = Logger.getLogger(ListenAenderungsUeberwacher.class.getName());
	
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
	
	protected final List<E> liste;
	protected final WeakReference<EditierBeobachter> beobachter;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Neuer ListenUeberwacher, der aus einer Listenaenderung verschiedene EditierBefehle
	 * erzeugt und den Beobachter darueber informiert.
	 * 
	 * @param liste      Liste, die beobachtet wird
	 * @param beobachter Objekt, das die Liste beobachtet und informiert werden moechte
	 */
	public ListenAenderungsUeberwacher(List<E> liste, EditierBeobachter beobachter) {
		this.liste = liste;
		this.beobachter = new WeakReference<>(beobachter);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * Verarbeitung von Aenderungen der Liste und informieren des Beobachters
	 */
	@Override
	public void onChanged(Change<? extends E> aenderung) {
		List<EditierBefehl> befehle = new LinkedList<>();
		while (aenderung.next()) {
			if (aenderung.wasPermutated()) {
				behandlePermutation(aenderung, befehle);
			}
			if (aenderung.wasReplaced()) {
				behandleErsetzung(aenderung, befehle);
			}
			if (aenderung.wasRemoved() && !aenderung.wasReplaced()) {
				behandleEntfernung(aenderung, befehle);
			}
			if (aenderung.wasAdded() && !aenderung.wasReplaced()) {
				behandleHinzugefuegt(aenderung, befehle);
			}
			if (aenderung.wasUpdated()) {
				behandleUpdate(aenderung, befehle);
			}
		}
		var sammelEditierung = new SammelEditierung();
		for (EditierBefehl editierBefehl : befehle) {
			sammelEditierung.speicherEditierung(editierBefehl);
		}
		beobachter.get().verarbeiteEditierung(sammelEditierung);
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	protected void behandlePermutation(Change<? extends E> aenderung, List<EditierBefehl> befehle) {
		int[] neueOrdnung = new int[aenderung.getList().size()];
		for (int i = 0; i < neueOrdnung.length; i++) {
			neueOrdnung[i] = aenderung.getPermutation(i);
		}
		log.finer(() -> "%s:  -> Reihenfolge geaendert [%s]".formatted(beobachter, Arrays.toString(neueOrdnung)));
		
		befehle.add(new EditierBefehl() {
			@Override
			public void wiederhole() {
				log.severe(() -> "EditierBefehl fuer Permutated implementieren!!!");
			}
			
			@Override
			public void macheRueckgaengig() {
				log.severe(() -> "EditierBefehl fuer Permutated implementieren!!!");
			}
			
			@Override
			public String toString() {
				log.severe(() -> "EditierBefehl fuer Permutated implementieren!!!");
				return "%s:  -> Reihenfolge geaendert [%s]".formatted(beobachter, Arrays.toString(neueOrdnung));
			}
			
			@Override
			public void close() throws Exception {
				log.severe(() -> "EditierBefehl fuer Update implementieren!!!");
			}
		});
		log.severe(() -> "EditierBefehl fuer Permutated implementieren!!!");
		// TODO EditierBefehl fuer Permutated implementieren!!!
	}
	
	protected void behandleErsetzung(Change<? extends E> aenderung, List<EditierBefehl> befehle) {
		log.finer(() -> {
			try {
				return "%s:  -> ersetzt [%s] durch [%s]".formatted(beobachter,
						Arrays.toString(aenderung.getRemoved().toArray()),
						Arrays.toString(aenderung.getAddedSubList().toArray()));
			} catch (Exception e) {
				return "";
			}
		});
		
		var befehl = new EditierBefehl() {
			
			int startIndex = aenderung.getFrom();
			List<E> hinzugefuegteAttribute = Collections.unmodifiableList(new ArrayList<>(aenderung.getAddedSubList()));
			List<E> entfernteAttribute = Collections.unmodifiableList(new ArrayList<>(aenderung.getRemoved()));
			
			@Override
			public void wiederhole() {
				int i = startIndex;
				for (E neu : hinzugefuegteAttribute) {
					liste.set(i, neu);
					i++;
				}
			}
			
			@Override
			public void macheRueckgaengig() {
				int i = startIndex;
				for (E alt : entfernteAttribute) {
					liste.set(i, alt);
					i++;
				}
			}
			
			@Override
			public String toString() {
				try {
					return "%s:  -> ersetzt [%s] durch [%s]".formatted(beobachter,
							Arrays.toString(entfernteAttribute.toArray()), 
							Arrays.toString(hinzugefuegteAttribute.toArray()));
				} catch (Exception e) {
					return "";
				}
			}
			
			@Override
			public void close() throws Exception {
				for (var entfernt : entfernteAttribute) {
					if (entfernt instanceof AutoCloseable c) {
						String entferntStr = c.toString();
						try {
							c.close();
							log.fine(() -> entferntStr + " wurde geschlossen");
						} catch (Exception e) {
							log.log(Level.CONFIG, e, () -> "Fehler beim Schliessen von " + entferntStr);
						}
					}
				}
			}
		};
		befehle.add(befehl);
	}
	
	protected void behandleEntfernung(Change<? extends E> aenderung, List<EditierBefehl> befehle) {
		log.finer(() -> {
			try {
				return "%s:  -> entfernt [%s]".formatted(beobachter, Arrays.toString(aenderung.getRemoved().toArray()));
			} catch (Exception e) {
				return "";
			}
		});
		
		var befehl = new EditierBefehl() {
			
			int startIndex = aenderung.getFrom();
			List<E> entfernteAttribute = Collections.unmodifiableList(new ArrayList<>(aenderung.getRemoved()));
			
			@Override
			public void wiederhole() {
				liste.removeAll(entfernteAttribute);
			}
			
			@Override
			public void macheRueckgaengig() {
				liste.addAll(startIndex, entfernteAttribute);
			}
			
			@Override
			public String toString() {
				try {
					return "%s:  -> entfernt <index %d> %s".formatted(beobachter, startIndex,
							Arrays.toString(entfernteAttribute.toArray()));
				} catch (Exception e) {
					return "";
				}
			}
			
			@Override
			public void close() throws Exception {
				for (var entfernt : entfernteAttribute) {
					if (entfernt instanceof AutoCloseable c) {
						String entferntStr = c.toString();
						try {
							c.close();
							log.fine(() -> entferntStr + " wurde geschlossen");
						} catch (Exception e) {
							log.log(Level.CONFIG, e, () -> "Fehler beim Schliessen von " + entferntStr);
						}
					}
				}
			}
		};
		befehle.add(befehl);
	}
	
	protected void behandleHinzugefuegt(Change<? extends E> aenderung, List<EditierBefehl> befehle) {
		log.finer(() -> "%s:  -> hinzugefuegt [%s]".formatted(beobachter,
				Arrays.toString(aenderung.getAddedSubList().toArray())));
		var befehl = new EditierBefehl() {
			
			int startIndex = aenderung.getFrom();
			List<E> neueAttribute = Collections.unmodifiableList(new ArrayList<>(aenderung.getAddedSubList()));
			
			@Override
			public void wiederhole() {
				liste.addAll(startIndex, neueAttribute);
			}
			
			@Override
			public void macheRueckgaengig() {
				liste.removeAll(neueAttribute);
			}
			
			@Override
			public String toString() {
				return "%s:  -> hinzugefuegt <index %d> [%s]".formatted(beobachter, startIndex,
						Arrays.toString(neueAttribute.toArray()));
			}
			
			@Override
			public void close() throws Exception {
				for (var entfernt : neueAttribute) {
					if (entfernt instanceof AutoCloseable c) {
						String entferntStr = c.toString();
						try {
							c.close();
							log.fine(() -> entferntStr + " wurde geschlossen");
						} catch (Exception e) {
							log.log(Level.CONFIG, e, () -> "Fehler beim Schliessen von " + entferntStr);
						}
					}
				}
			}
		};
		befehle.add(befehl);
	}
	
	protected void behandleUpdate(Change<? extends E> aenderung, List<EditierBefehl> befehle) {
		log.finer(() -> "%s:  -> Update [%d bis %d]".formatted(beobachter, aenderung.getFrom(), aenderung.getTo()));
		
		befehle.add(new EditierBefehl() {
			String s = "%s:  -> Update [%d bis %d]".formatted(beobachter, aenderung.getFrom(), aenderung.getTo());
			
			@Override
			public void wiederhole() {
				log.severe(() -> "EditierBefehl fuer Update implementieren!!!");
			}
			
			@Override
			public void macheRueckgaengig() {
				log.severe(() -> "EditierBefehl fuer Update implementieren!!!");
			}
			
			@Override
			public String toString() {
				log.severe(() -> "EditierBefehl fuer Update implementieren!!!");
				return s;
			}
			
			@Override
			public void close() throws Exception {
				log.severe(() -> "EditierBefehl fuer Update implementieren!!!");
			}
		});
		log.severe(() -> "EditierBefehl fuer Update implementieren!!!");
		// TODO EditierBefehl fuer Update implementieren!!!
	}
}