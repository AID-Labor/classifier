/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.java;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindung;


public class KlassifiziererBesucher extends GenericVisitorAdapter<List<UMLKlassifizierer>, Collection<UMLVerbindung>> {
//	private static final Logger log = Logger.getLogger(KlassifiziererBesucher.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private final String umgebenderTyp;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public KlassifiziererBesucher() {
		this.umgebenderTyp = null;
	}
	
	private KlassifiziererBesucher(String umgebenderTyp) {
		this.umgebenderTyp = umgebenderTyp;
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
	
	@Override
	public List<UMLKlassifizierer> visit(ClassOrInterfaceDeclaration typDeklaration,
			Collection<UMLVerbindung> verbindungen) {
		KlassifiziererTyp typ = KlassifiziererTyp.Klasse;
		if (typDeklaration.isAbstract()) {
			typ = KlassifiziererTyp.AbstrakteKlasse;
		}
		if (typDeklaration.isInterface()) {
			typ = KlassifiziererTyp.Interface;
		}
		
		UMLKlassifizierer klassifizierer = erzeugeKlassifizierer(typ, typDeklaration);
		
		LinkedList<UMLKlassifizierer> liste = new LinkedList<>();
		liste.add(klassifizierer);
		
		typDeklaration.getMembers().forEach(m -> {
			List<UMLKlassifizierer> innereTypen = m.accept(new KlassifiziererBesucher(typDeklaration.getNameAsString()),
					verbindungen);
			if (innereTypen != null && !innereTypen.isEmpty()) {
				liste.addAll(innereTypen);
			}
		});
		
		return liste;
	}
	
	@Override
	public List<UMLKlassifizierer> visit(EnumDeclaration typDeklaration, Collection<UMLVerbindung> verbindungen) {
		UMLKlassifizierer klassifizierer = erzeugeKlassifizierer(KlassifiziererTyp.Enumeration, typDeklaration);
		
		LinkedList<UMLKlassifizierer> liste = new LinkedList<>();
		liste.add(klassifizierer);
		
		typDeklaration.getMembers().forEach(m -> {
			List<UMLKlassifizierer> innereTypen = m.accept(new KlassifiziererBesucher(typDeklaration.getNameAsString()),
					verbindungen);
			if (innereTypen != null && !innereTypen.isEmpty()) {
				liste.addAll(innereTypen);
			}
		});
		
		return liste;
	}
	
	@Override
	public List<UMLKlassifizierer> visit(RecordDeclaration typDeklaration, Collection<UMLVerbindung> verbindungen) {
		UMLKlassifizierer klassifizierer = erzeugeKlassifizierer(KlassifiziererTyp.Record, typDeklaration);
		
		
		LinkedList<UMLKlassifizierer> liste = new LinkedList<>();
		liste.add(klassifizierer);
		
		typDeklaration.getMembers().forEach(m -> {
			List<UMLKlassifizierer> innereTypen = m.accept(new KlassifiziererBesucher(typDeklaration.getNameAsString()),
					verbindungen);
			if (innereTypen != null && !innereTypen.isEmpty()) {
				liste.addAll(innereTypen);
			}
		});
		
		return liste;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private UMLKlassifizierer erzeugeKlassifizierer(KlassifiziererTyp typ, TypeDeclaration<?> typDeklaration) {
		String name = typDeklaration.getNameAsString();
		if (this.umgebenderTyp != null && !this.umgebenderTyp.isBlank()) {
			name = this.umgebenderTyp + "." + name;
		}
		System.out.println(name);
		UMLKlassifizierer klassifizierer = new UMLKlassifizierer(typ, JavaProvider.provider(), name);
		
		return klassifizierer;
	}
	
}