/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.projekt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
public class ListenUeberwacher<E extends Editierbar> implements ListChangeListener<E> {
	private static final Logger log = Logger.getLogger(ListenUeberwacher.class.getName());
	
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
	
	private final List<E> liste;
	private final EditierBeobachter beobachter;
	
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
	public ListenUeberwacher(List<E> liste, EditierBeobachter beobachter) {
		this.liste = liste;
		this.beobachter = beobachter;
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
			if (aenderung.wasRemoved()) {
				behandleEntfernung(aenderung, befehle);
			}
			if (aenderung.wasAdded()) {
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
		beobachter.verarbeiteEditierung(sammelEditierung);
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void behandlePermutation(Change<? extends E> aenderung,
			List<EditierBefehl> befehle) {
		int[] neueOrdnung = new int[aenderung.getList().size()];
		for (int i = 0; i < neueOrdnung.length; i++) {
			neueOrdnung[i] = aenderung.getPermutation(i);
		}
		log.finer(() -> "%s:  -> Reihenfolge geaendert [%s]"
				.formatted(beobachter, Arrays.toString(neueOrdnung)));
		
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
				return "%s:  -> Reihenfolge geaendert [%s]"
						.formatted(beobachter, Arrays.toString(neueOrdnung));
			}
		});
		log.severe(() -> "EditierBefehl fuer Permutated implementieren!!!");
		// TODO EditierBefehl fuer Permutated implementieren!!!
	}
	
	private void behandleEntfernung(Change<? extends E> aenderung,
			List<EditierBefehl> befehle) {
		log.finer(() -> "%s:  -> entfernt [%s]".formatted(beobachter,
				Arrays.toString(aenderung.getRemoved().toArray())));
		for (E entfernt : aenderung.getRemoved()) {
			entfernt.meldeAb(beobachter);
		}
		var befehl = new EditierBefehl() {
			
			int startIndex = aenderung.getFrom();
			List<E> entfernteAttribute = Collections.unmodifiableList(
					new ArrayList<>(aenderung.getRemoved()));
			
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
				return "%s:  -> entfernt [%s]".formatted(beobachter,
						Arrays.toString(entfernteAttribute.toArray()));
			}
		};
		befehle.add(befehl);
	}
	
	private void behandleHinzugefuegt(Change<? extends E> aenderung,
			List<EditierBefehl> befehle) {
		log.finer(() -> "%s:  -> hinzugefuegt [%s]".formatted(beobachter,
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
			
			@Override
			public String toString() {
				return "%s:  -> hinzugefuegt [%s]".formatted(beobachter,
						Arrays.toString(neueAttribute.toArray()));
			}
		};
		befehle.add(befehl);
	}
	
	private void behandleUpdate(Change<? extends E> aenderung, List<EditierBefehl> befehle) {
		log.finer(() -> "%s:  -> Update [%d bis %d]".formatted(beobachter,
				aenderung.getFrom(), aenderung.getTo()));
		
		befehle.add(new EditierBefehl() {
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
				return "%s:  -> Update [%d bis %d]".formatted(beobachter,
						aenderung.getFrom(), aenderung.getTo());
			}
		});
		log.severe(() -> "EditierBefehl fuer Update implementieren!!!");
		// TODO EditierBefehl fuer Update implementieren!!!
	}
}