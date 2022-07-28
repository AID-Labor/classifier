/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.java;

import java.util.LinkedList;
import java.util.List;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Attribut;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Datentyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Konstruktor;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Methode;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Modifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Parameter;


public class KlassifiziererBesucher extends VoidVisitorAdapter<Void> {

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
	
	private final List<UMLKlassifizierer> klassifiziererListe;
	private final String umgebenderTyp;
	private String paket;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public KlassifiziererBesucher(List<UMLKlassifizierer> klassifiziererListe) {
		this(klassifiziererListe, null, "");
	}
	
	private KlassifiziererBesucher(List<UMLKlassifizierer> klassifiziererListe, String umgebenderTyp, String paket) {
		this.klassifiziererListe = klassifiziererListe;
		this.umgebenderTyp = umgebenderTyp;
		this.paket = paket;
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public String getPaket() {
		return paket;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public void visit(PackageDeclaration paketDeklaration, Void arg) {
		this.paket = paketDeklaration.getNameAsString();
		for (var klassifizierer : klassifiziererListe) {
			if (klassifizierer.getPaket() == null || klassifizierer.getPaket().isBlank()) {
				klassifizierer.setPaket(paket);
			}
		}
	}
	
	@Override
	public void visit(ClassOrInterfaceDeclaration typDeklaration, Void arg) {
		KlassifiziererTyp typ = KlassifiziererTyp.Klasse;
		if (typDeklaration.isAbstract()) {
			typ = KlassifiziererTyp.AbstrakteKlasse;
		}
		if (typDeklaration.isInterface()) {
			typ = KlassifiziererTyp.Interface;
		}
		
		UMLKlassifizierer klassifizierer = erzeugeKlassifizierer(typ, typDeklaration);
		fuegeInterfacesUndSuperklasseHinzu(klassifizierer, typDeklaration);
		
		klassifiziererListe.add(klassifizierer);
		
		typDeklaration.getMembers().forEach(m -> {
			m.accept(new KlassifiziererBesucher(klassifiziererListe, typDeklaration.getNameAsString(), paket), null);
		});
		
	}
	
	@Override
	public void visit(EnumDeclaration typDeklaration, Void arg) {
		UMLKlassifizierer klassifizierer = erzeugeKlassifizierer(KlassifiziererTyp.Enumeration, typDeklaration);
		
		List<Attribut> enumAttribute = new LinkedList<>();
		typDeklaration.getEntries().forEach(enumKonstante -> {
			Attribut a = new Attribut(Modifizierer.PUBLIC, new Datentyp(klassifizierer.getName()));
			a.setName(enumKonstante.getNameAsString());
			a.setStatisch(true);
			a.setFinal(true);
			enumAttribute.add(a);
		});
		klassifizierer.attributeProperty().addAll(0, enumAttribute);
		
		klassifiziererListe.add(klassifizierer);
		
		typDeklaration.getMembers().forEach(m -> {
			m.accept(new KlassifiziererBesucher(klassifiziererListe, typDeklaration.getNameAsString(), paket), null);
		});
		
	}
	
	@Override
	public void visit(RecordDeclaration typDeklaration, Void arg) {
		UMLKlassifizierer klassifizierer = erzeugeKlassifizierer(KlassifiziererTyp.Record, typDeklaration);
		
		List<Attribut> recordKomponenten = new LinkedList<>();
		typDeklaration.getParameters().forEach(recordKomponente -> {
			Attribut komponente = new Attribut(Modifizierer.PUBLIC, new Datentyp(klassifizierer.getName()));
			komponente.setName(recordKomponente.getNameAsString());
			komponente.setStatisch(true);
			komponente.setFinal(true);
			recordKomponenten.add(komponente);
		});
		klassifizierer.attributeProperty().addAll(0, recordKomponenten);
		
		klassifiziererListe.add(klassifizierer);
		
		typDeklaration.getMembers().forEach(m -> {
			m.accept(new KlassifiziererBesucher(klassifiziererListe, typDeklaration.getNameAsString(), paket), null);
		});
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private UMLKlassifizierer erzeugeKlassifizierer(KlassifiziererTyp typ, TypeDeclaration<?> typDeklaration) {
		String name = typDeklaration.getNameAsString();
		if (this.umgebenderTyp != null && !this.umgebenderTyp.isBlank()) {
			name = this.umgebenderTyp + "." + name;
		}
		
		UMLKlassifizierer klassifizierer = new UMLKlassifizierer(typ, JavaProvider.provider(), name);
		klassifizierer.setSichtbarkeit(konvertiereModifizierer(typDeklaration.getAccessSpecifier()));
		
		if (paket != null && !paket.isBlank()) {
			klassifizierer.setPaket(paket);
		}
		
		typDeklaration.getFields().forEach(fieldsDeklaration -> {
			List<Attribut> attribute = erzeugeAttribute(fieldsDeklaration);
			klassifizierer.attributeProperty().addAll(attribute);
		});
		
		typDeklaration.getConstructors().forEach(konstruktorDeklaration -> {
			Konstruktor konstruktor = erzeugeKonstruktor(konstruktorDeklaration);
			klassifizierer.konstruktorProperty().add(konstruktor);
		});
		
		typDeklaration.getMethods().forEach(methodenDeklaration -> {
			Methode methode = erzeugeMethoden(methodenDeklaration, KlassifiziererTyp.Interface.equals(typ));
			klassifizierer.methodenProperty().add(methode);
		});
		
		return klassifizierer;
	}
	
	private Modifizierer konvertiereModifizierer(AccessSpecifier mod) {
		return switch (mod) {
			case PACKAGE_PRIVATE -> Modifizierer.PACKAGE;
			case PRIVATE -> Modifizierer.PRIVATE;
			case PROTECTED -> Modifizierer.PROTECTED;
			case PUBLIC -> Modifizierer.PUBLIC;
		};
	}
	
	private Datentyp konvertiereDatentyp(Type type) {
		if (type.isPrimitiveType()) {
			return new Datentyp(type.asPrimitiveType().asString());
		} else if (type.isVoidType()) {
			return new Datentyp("void");
		} else {
			return new Datentyp(type.asString());
		}
	}
	
	private List<Attribut> erzeugeAttribute(FieldDeclaration fieldsDeklaration) {
		Modifizierer sichtbarkeit = konvertiereModifizierer(fieldsDeklaration.getAccessSpecifier());
		List<Attribut> attribute = new LinkedList<>();
		for (VariableDeclarator attr : fieldsDeklaration.getVariables()) {
			Datentyp datentyp = konvertiereDatentyp(attr.getType());
			Attribut attribut = new Attribut(sichtbarkeit, datentyp);
			attribut.setName(attr.getNameAsString());
			attribut.setStatisch(fieldsDeklaration.isStatic());
			attribut.setFinal(fieldsDeklaration.isFinal());
			var init = attr.getInitializer().orElse(null);
			if (init != null) {
				String initialisierung = init.toString();
				if (initialisierung.contains(":")) {
					initialisierung = initialisierung.substring(initialisierung.indexOf(":"));
				}
				initialisierung = initialisierung.strip();
				attribut.setInitialwert(initialisierung);
			}
			attribute.add(attribut);
		}
		
		return attribute;
	}
	
	private Konstruktor erzeugeKonstruktor(ConstructorDeclaration konstruktorDeklaration) {
		Modifizierer sichtbarkeit = konvertiereModifizierer(konstruktorDeklaration.getAccessSpecifier());
		
		Konstruktor konstruktor = new Konstruktor(sichtbarkeit);
		konstruktor.setName(konstruktorDeklaration.getNameAsString());
		
		List<Parameter> parameterListe = erzeugeParameter(konstruktorDeklaration.getParameters());
		konstruktor.parameterListeProperty().addAll(parameterListe);
		
		return konstruktor;
	}
	
	private Methode erzeugeMethoden(MethodDeclaration methodenDeklaration, boolean istInterface) {
		Modifizierer sichtbarkeit = konvertiereModifizierer(methodenDeklaration.getAccessSpecifier());
		
		Datentyp datentyp = konvertiereDatentyp(methodenDeklaration.getType());
		Methode methode = new Methode(sichtbarkeit, datentyp, JavaProvider.provider());
		methode.setName(methodenDeklaration.getNameAsString());
		methode.setzeStatisch(methodenDeklaration.isStatic());
		methode.setzeFinal(methodenDeklaration.isFinal());
		
		if (istInterface && !methodenDeklaration.isStatic()) {
			methode.setzeAbstrakt(!methodenDeklaration.isDefault());
		} else {
			methode.setzeAbstrakt(methodenDeklaration.isAbstract());
		}
		
		List<Parameter> parameterListe = erzeugeParameter(methodenDeklaration.getParameters());
		methode.parameterListeProperty().addAll(parameterListe);
		
		return methode;
	}
	
	private List<Parameter> erzeugeParameter(
			NodeList<com.github.javaparser.ast.body.Parameter> parameterDeklararationen) {
		List<Parameter> parameterListe = new LinkedList<>();
		
		parameterDeklararationen.forEach(parameterDeklaration -> {
			Datentyp paramDatentyp = konvertiereDatentyp(parameterDeklaration.getType());
			String paramName = parameterDeklaration.getNameAsString();
			if (parameterDeklaration.isVarArgs()) {
				paramDatentyp.setTypName(paramDatentyp.getTypName() + "...");
			}
			Parameter param = new Parameter(paramDatentyp, paramName);
			parameterListe.add(param);
		});
		
		return parameterListe;
	}
	
	private void fuegeInterfacesUndSuperklasseHinzu(UMLKlassifizierer klassifizierer,
			ClassOrInterfaceDeclaration typDeklaration) {
		if (typDeklaration.isInterface()) {
			typDeklaration.getExtendedTypes().forEach(interfacetyp -> {
				String interfacename = interfacetyp.getNameAsString();
				klassifizierer.getInterfaces().add(interfacename);
			});
			typDeklaration.getImplementedTypes().forEach(interfacetyp -> {
				String interfacename = interfacetyp.getNameAsString();
				klassifizierer.getInterfaces().add(interfacename);
			});
		} else {
			if (typDeklaration.getExtendedTypes().size() > 0) {
				var supertyp = typDeklaration.getExtendedTypes(0);
				if (supertyp != null) {
					String supername = supertyp.getNameAsString();
					klassifizierer.setSuperklasse(supername);
				}
			}
			
			typDeklaration.getImplementedTypes().forEach(interfacetyp -> {
				String interfacename = interfacetyp.getNameAsString();
				klassifizierer.getInterfaces().add(interfacename);
			});
		}
	}
	
}