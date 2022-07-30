/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.java;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.PrimitiveType.Primitive;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.printer.DefaultPrettyPrinter;
import com.github.javaparser.printer.Printer;

import io.github.aid_labor.classifier.basis.io.system.OS;
import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindung;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindungstyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Attribut;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Datentyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Konstruktor;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Methode;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Modifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Parameter;
import io.github.aid_labor.classifier.uml.programmierung.ExportImportVerarbeitung;
import io.github.aid_labor.classifier.uml.programmierung.ImportException;
import javafx.stage.FileChooser.ExtensionFilter;


public class JavaExportImportVerarbeitung implements ExportImportVerarbeitung {
	private static final Logger log = Logger.getLogger(JavaExportImportVerarbeitung.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static final List<ExtensionFilter> importDateierweiterungen = List
			.of(new ExtensionFilter("Java", "*.java"));
	
	private static final String TODO = " TODO: implement";
	
	private static JavaExportImportVerarbeitung instanz;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	static JavaExportImportVerarbeitung getSingletonInstanz() {
		if (instanz == null) {
			instanz = new JavaExportImportVerarbeitung();
		}
		return instanz;
	}
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private JavaExportImportVerarbeitung() {
		// Singleton-Entwurfsmuster
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public String erzeugeDateiName(UMLKlassifizierer klassifizierer) {
		if (klassifizierer.getPaket() != null && !klassifizierer.getPaket().isBlank()) {
			String[] paket = klassifizierer.getPaket().split("\\.");
			return OS.getDefault().pfadAus(OS.getDefault().pfadAus(paket), klassifizierer.getName()).toString()
					+ ".java";
		} else {
			return klassifizierer.getName() + ".java";
		}
	}
	
	@Override
	public Collection<ExtensionFilter> getImportDateierweiterungen() {
		return importDateierweiterungen;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public void exportiere(UMLKlassifizierer klassifizierer, Writer ausgabe) throws Exception {
		CompilationUnit javaDatei = new CompilationUnit();
		
		if (klassifizierer.getPaket() != null && !klassifizierer.getPaket().isBlank()) {
			javaDatei.setPackageDeclaration(klassifizierer.getPaket());
		}
		
		TypeDeclaration<?> typDeklaration = erzeugeTypDeklaration(klassifizierer);
		javaDatei.addType(typDeklaration);
		
		Printer formatierer = new DefaultPrettyPrinter();
		String quellcode = formatierer.print(javaDatei);
		try (var pw = new PrintWriter(ausgabe)) {
			pw.print(quellcode);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public ImportErgebnis importiere(File quelle) throws ImportException, IOException {
		
		ParserConfiguration konfiguration = new ParserConfiguration();
		konfiguration.setAttributeComments(false);
		konfiguration.setLanguageLevel(LanguageLevel.BLEEDING_EDGE);
		JavaParser parser = new JavaParser();
		ParseResult<CompilationUnit> ergebnis = parser.parse(quelle);
		
		if (ergebnis.getResult().isPresent()) {
			try {
				List<UMLKlassifizierer> klassifiziererListe = new LinkedList<>();
				List<UMLVerbindung> verbindungen = new LinkedList<>();
				ergebnis.getResult().get().accept(new KlassifiziererBesucher(klassifiziererListe), null);
				
				List<UMLVerbindung> assoziationen = sucheAssoziationen(klassifiziererListe);
				verbindungen.addAll(assoziationen);
				
				return new ImportErgebnis(klassifiziererListe, assoziationen, erzeugeFehler(ergebnis, quelle));
			} catch (Exception e) {
				throw erzeugeFehler(ergebnis, quelle);
			}
		} else if (!ergebnis.isSuccessful()) {
			throw erzeugeFehler(ergebnis, quelle);
		} else {
			throw new ImportException("Kein Ergebnis!");
		}
	}
	
	private ImportException erzeugeFehler(ParseResult<CompilationUnit> ergebnis, File quelle) {
		Queue<Throwable> exceptions = new LinkedList<>();
		StringBuilder message = new StringBuilder("[");
		message.append(ergebnis.getProblems().size());
		message.append("] Problem(s) in ");
		message.append(quelle.getName());
		message.append(":\n");
		for (var problem : ergebnis.getProblems()) {
			problem.getCause().ifPresent(exceptions::add);
			message.append(problem.getVerboseMessage());
			message.append("\n");
		}
		var exc = new ImportException(message.toString());
		if (!exceptions.isEmpty()) {
			exc.initCause(exceptions.poll());
		}
		while (!exceptions.isEmpty()) {
			exc.addSuppressed(exceptions.poll());
		}
		return exc;
	}
	
	private List<UMLVerbindung> sucheAssoziationen(List<UMLKlassifizierer> klassifiziererListe) {
		List<UMLVerbindung> assoziationen = new LinkedList<>();
		
		for (UMLKlassifizierer umlKlassifizierer : klassifiziererListe) {
			List<UMLVerbindung> neueAssoziationen = erzeugeAssoziationen(umlKlassifizierer);
			assoziationen.addAll(neueAssoziationen);
		}
		
		return assoziationen;
	}
	
	private List<UMLVerbindung> erzeugeAssoziationen(UMLKlassifizierer klassifizierer) {
		Set<String> datentypen = new HashSet<>();
		
		List<String> attributeDatentypen = klassifizierer.attributeProperty().parallelStream()
				.map(Attribut::getDatentyp).map(Datentyp::getTypName).toList();
		datentypen.addAll(attributeDatentypen);
		
//		List<String> konstruktorParameter = klassifizierer.konstruktorProperty().parallelStream()
//				.flatMap(k -> k.parameterListeProperty().parallelStream()).map(Parameter::getDatentyp)
//				.map(Datentyp::getTypName).toList();
//		datentypen.addAll(konstruktorParameter);
//		
//		List<String> methodenDatentypen = klassifizierer.methodenProperty().parallelStream()
//				.map(Methode::getRueckgabeTyp).map(Datentyp::getTypName).toList();
//		datentypen.addAll(methodenDatentypen);
//		
//		List<String> methodenParameter = klassifizierer.methodenProperty().parallelStream()
//				.map(Methode::parameterListeProperty).flatMap(Collection::parallelStream).map(Parameter::getDatentyp)
//				.map(Datentyp::getTypName).toList();
//		datentypen.addAll(methodenParameter);
		
		List<UMLVerbindung> assoziationen = new LinkedList<>();
		for (String verwendeterDatentyp : datentypen) {
			if (!klassifizierer.getNameVollstaendig().equals(verwendeterDatentyp)
					&& !klassifizierer.getName().equals(verwendeterDatentyp)) {
				UMLVerbindung assoziation = new UMLVerbindung(UMLVerbindungstyp.ASSOZIATION,
						klassifizierer.getNameVollstaendig(), verwendeterDatentyp);
				assoziationen.add(assoziation);
			}
		}
		
		return assoziationen;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@SuppressWarnings("unused")
	private UMLKlassifizierer importiereMitJavaCompilerAPI(File quelle, List<UMLVerbindung> verbindungen)
			throws ImportException, IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		
		try (StandardJavaFileManager dateiManager = compiler.getStandardFileManager(null, null, null);
				StringWriter out = new StringWriter()) {
			Iterable<? extends JavaFileObject> quelldateien = dateiManager.getJavaFileObjects(quelle);
			var task = compiler.getTask(out, dateiManager, null, null, null, quelldateien);
			
			boolean erfolg = task.call();
			if (!erfolg) {
				throw new ImportException(out.toString());
			}
		}
		
		return null;
	}
	
	private TypeDeclaration<?> erzeugeTypDeklaration(UMLKlassifizierer klassifizierer) {
		TypeDeclaration<?> typDeklaration;
		final boolean istAbstrakt;
		switch (klassifizierer.getTyp()) {
			case Interface -> {
				typDeklaration = new ClassOrInterfaceDeclaration();
				((ClassOrInterfaceDeclaration) typDeklaration).setInterface(true);
				istAbstrakt = false;
			}
			case AbstrakteKlasse -> {
				typDeklaration = new ClassOrInterfaceDeclaration();
				istAbstrakt = true;
			}
			case Klasse -> {
				typDeklaration = new ClassOrInterfaceDeclaration();
				istAbstrakt = false;
			}
			case Enumeration -> {
				typDeklaration = new EnumDeclaration();
				istAbstrakt = false;
			}
			case Record -> {
				typDeklaration = new RecordDeclaration();
				istAbstrakt = false;
			}
			default -> throw new IllegalStateException("unbekannter typ: " + klassifizierer.getTyp());
		}
		
		typDeklaration.setName(klassifizierer.getName());
		var zugriff = konvertiereModifizierer(klassifizierer.getSichtbarkeit());
		
		if (zugriff != null) {
			typDeklaration.addModifier(zugriff);
		}
		typDeklaration.ifClassOrInterfaceDeclaration(clazz -> clazz.setAbstract(istAbstrakt));
		
		fuelleSuperklasseUndInterfaces(klassifizierer, typDeklaration);
		
		fuelleInhalt(klassifizierer, typDeklaration);
		
		return typDeklaration;
	}
	
	private void fuelleInhalt(UMLKlassifizierer klassifizierer, TypeDeclaration<?> typDeklaration) {
		var klassenAttributKommentar = " Klassenattribute ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##";
		fuelleAttribute(klassifizierer.attributeProperty().filtered(Attribut::istStatisch), typDeklaration,
				new LineComment(klassenAttributKommentar));
		
		var klassenMethodenKommentar = " Klassenmethoden  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##";
		fuelleMethoden(klassifizierer.methodenProperty().filtered(Methode::istStatisch), typDeklaration,
				klassifizierer.getTyp().equals(KlassifiziererTyp.Interface), new LineComment(klassenMethodenKommentar));
		
		var attributeKommentar = " Attribute    ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##";
		fuelleAttribute(klassifizierer.attributeProperty().filtered(a -> !a.istStatisch()), typDeklaration,
				new LineComment(attributeKommentar));
		
		var konstruktorenKommentar = " Konstruktoren    ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##";
		fuelleKonstruktoren(klassifizierer.konstruktorProperty(), typDeklaration,
				new LineComment(konstruktorenKommentar));
		
		var getterUndSetterKommentar = " Getter und Setter    ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##";
		fuelleMethoden(
				klassifizierer.methodenProperty().filtered(m -> !m.istStatisch() && (m.istGetter() || m.istSetter())),
				typDeklaration, klassifizierer.getTyp().equals(KlassifiziererTyp.Interface),
				new LineComment(getterUndSetterKommentar));
		
		var methodenKommentar = " Methoden ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##";
		fuelleMethoden(
				klassifizierer.methodenProperty().filtered(m -> !m.istStatisch() && !m.istGetter() && !m.istSetter()),
				typDeklaration, klassifizierer.getTyp().equals(KlassifiziererTyp.Interface),
				new LineComment(methodenKommentar));
	}
	
	private void fuelleSuperklasseUndInterfaces(UMLKlassifizierer klassifizierer, TypeDeclaration<?> typDeklaration) {
		typDeklaration.ifClassOrInterfaceDeclaration(clazz -> {
			if (klassifizierer.getSuperklasse() != null && !klassifizierer.getSuperklasse().isBlank()) {
				var superklasse = erzeugeSupertyp(klassifizierer.getSuperklasse());
				clazz.getExtendedTypes().add(superklasse);
			}
			for (var interf : klassifizierer.getInterfaces()) {
				var interfaceDeklaration = erzeugeSupertyp(interf);
				clazz.getImplementedTypes().add(interfaceDeklaration);
			}
		});
	}
	
	private ClassOrInterfaceType erzeugeSupertyp(String name) {
		var supertyp = new ClassOrInterfaceType();
		if (name.contains(":")) {
			supertyp.setName(name.substring(name.indexOf(":") + 1));
		} else {
			supertyp.setName(name);
		}
		return supertyp;
	}
	
	private void fuelleAttribute(List<Attribut> attribute, TypeDeclaration<?> typDeklaration, Comment kommentar) {
		for (var attribut : attribute) {
			Type typ = konvertiereDatentyp(attribut.getDatentyp());
			String name = attribut.getName();
			Keyword[] modifizierer = getAlleModifizierer(attribut);
			
			if (attribut.getInitialwert() != null && !attribut.getInitialwert().isBlank()) {
				var expr = new NameExpr(attribut.getInitialwert());
				var attr = typDeklaration.addFieldWithInitializer(typ, name, expr, modifizierer);
				if (kommentar != null) {
					attr.setComment(kommentar);
					kommentar = null;
				}
			} else {
				var attr = typDeklaration.addField(typ, attribut.getName(), getAlleModifizierer(attribut));
				if (kommentar != null) {
					attr.setComment(kommentar);
					kommentar = null;
				}
			}
		}
	}
	
	private void fuelleKonstruktoren(List<Konstruktor> konstruktoren, TypeDeclaration<?> typDeklaration,
			Comment kommentar) {
		for (Konstruktor konstruktor : konstruktoren) {
			var konstruktorDeklaration = typDeklaration.addConstructor(getAlleModifizierer(konstruktor));
			konstruktorDeklaration.setParameters(konvertiereParameter(konstruktor.parameterListeProperty()));
			if (kommentar != null) {
				konstruktorDeklaration.setComment(kommentar);
				kommentar = null;
			}
			konstruktorDeklaration.createBody().addOrphanComment(new LineComment(TODO));
		}
	}
	
	private void fuelleMethoden(List<Methode> methoden, TypeDeclaration<?> typDeklaration, boolean istInterface,
			Comment kommentar) {
		for (Methode methode : methoden) {
			var modifizierer = getAlleModifizierer(methode, istInterface);
			var methodenDeklaration = typDeklaration.addMethod(methode.getName(), modifizierer);
			methodenDeklaration.setParameters(konvertiereParameter(methode.parameterListeProperty()));
			var rueckgabeTyp = konvertiereDatentyp(methode.getRueckgabeTyp());
			methodenDeklaration.setType(rueckgabeTyp);
			if (kommentar != null) {
				methodenDeklaration.setComment(kommentar);
				kommentar = null;
			}
			
			if (methode.istAbstrakt()) {
				methodenDeklaration.setAbstract(true);
				methodenDeklaration.removeBody();
			} else if (methode.istGetter()) {
				String prefix = methode.istStatisch() ? typDeklaration.getNameAsString() : "this";
				var anweisung = prefix + "." + methode.getAttribut().getName();
				var rueckgabe = new ReturnStmt(anweisung);
				methodenDeklaration.createBody().addStatement(rueckgabe);
			} else if (methode.istSetter()) {
				try {
					var attribut = methode.getAttribut().getName();
					String prefix = methode.istStatisch() ? typDeklaration.getNameAsString() : "this";
					var param = methode.parameterListeProperty().get(0).getName();
					var anweisung = prefix + "." + attribut + " = " + param;
					var expr = new NameExpr(anweisung);
					methodenDeklaration.createBody().addStatement(new ExpressionStmt(expr));
				} catch (Exception e) {
					log.log(Level.WARNING, e, () -> "Setter %s konnte nicht erzeugt werden".formatted(methode));
				}
			} else if (!rueckgabeTyp.isVoidType()) {
				var rueckgabe = new ReturnStmt(getDefaultWert(rueckgabeTyp));
				rueckgabe.setLineComment(TODO);
				methodenDeklaration.createBody().addStatement(rueckgabe);
			} else {
				methodenDeklaration.createBody().addOrphanComment(new LineComment(TODO));
			}
		}
	}
	
	private String getDefaultWert(Type typ) {
		if (typ.isPrimitiveType()) {
			return switch (((PrimitiveType) typ).getType()) {
				case BOOLEAN -> "false";
				case BYTE, CHAR, SHORT, INT -> "0";
				case LONG -> "0L";
				case DOUBLE -> "0.0";
				case FLOAT -> "0.0f";
				default -> "null";
			};
		} else if (typ.isVoidType()) {
			return "";
		} else {
			return "null";
		}
	}
	
	private NodeList<com.github.javaparser.ast.body.Parameter> konvertiereParameter(List<Parameter> parameterListe) {
		NodeList<com.github.javaparser.ast.body.Parameter> params = new NodeList<>();
		
		for (Parameter parameter : parameterListe) {
			var datentyp = konvertiereDatentyp(parameter.getDatentyp());
			params.add(new com.github.javaparser.ast.body.Parameter(datentyp, parameter.getName()));
		}
		
		return params;
	}
	
	private Keyword[] getAlleModifizierer(Attribut attribut) {
		return getAlleModifizierer(attribut.getSichtbarkeit(), attribut.istStatisch(), attribut.istFinal(), false,
				false);
	}
	
	private Keyword[] getAlleModifizierer(Konstruktor konstruktor) {
		return getAlleModifizierer(konstruktor.getSichtbarkeit(), false, false, false, false);
	}
	
	private Keyword[] getAlleModifizierer(Methode methode, boolean istInterface) {
		return getAlleModifizierer(methode.getSichtbarkeit(), methode.istStatisch(), methode.istFinal(),
				methode.istAbstrakt(), istInterface);
	}
	
	private Keyword[] getAlleModifizierer(Modifizierer sichtbarkeit, boolean istStatisch, boolean istFinal,
			boolean istAbstrakt, boolean istInterfaceMethode) {
		ArrayList<Keyword> modifizierer = new ArrayList<>();
		var zugriff = konvertiereModifizierer(sichtbarkeit);
		
		if (zugriff != null) {
			modifizierer.add(zugriff);
		}
		
		if (istStatisch) {
			modifizierer.add(Keyword.STATIC);
		}
		if (istFinal) {
			modifizierer.add(Keyword.FINAL);
		}
		if (istAbstrakt) {
			modifizierer.add(Keyword.ABSTRACT);
		}
		if (istInterfaceMethode && !istAbstrakt && !istStatisch) {
			modifizierer.add(Keyword.DEFAULT);
		}
		
		return modifizierer.toArray(new Keyword[modifizierer.size()]);
	}
	
	private Type konvertiereDatentyp(Datentyp datentyp) {
		return konvertiereDatentyp(datentyp.getTypName());
	}
	
	private Type konvertiereDatentyp(String datentyp) {
		if (Java.getInstanz().istVoid(datentyp)) {
			return new VoidType();
		} else if (Java.getInstanz().getPrimitiveDatentypen().contains(datentyp)) {
			var typ = new PrimitiveType();
			switch (datentyp) {
				case "boolean" -> typ.setType(Primitive.BOOLEAN);
				case "byte" -> typ.setType(Primitive.BYTE);
				case "short" -> typ.setType(Primitive.SHORT);
				case "char" -> typ.setType(Primitive.CHAR);
				case "int" -> typ.setType(Primitive.INT);
				case "long" -> typ.setType(Primitive.LONG);
				case "float" -> typ.setType(Primitive.FLOAT);
				case "double" -> typ.setType(Primitive.DOUBLE);
				default -> throw new IllegalArgumentException("Unexpected value: " + datentyp);
			}
			return typ;
		} else if (datentyp.endsWith("[]")) {
			return new ArrayType(konvertiereDatentyp(datentyp.substring(0, datentyp.lastIndexOf("[]"))));
		} else {
			var typ = new ClassOrInterfaceType();
			typ.setName(datentyp);
			return typ;
		}
	}
	
	private Modifier.Keyword konvertiereModifizierer(Modifizierer mod) {
		return switch (mod) {
			case PACKAGE -> null;
			case PRIVATE -> Modifier.Keyword.PRIVATE;
			case PROTECTED -> Modifier.Keyword.PROTECTED;
			case PUBLIC -> Modifier.Keyword.PUBLIC;
		};
	}
	
}