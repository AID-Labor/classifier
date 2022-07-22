/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.java;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.ast.CompilationUnit;

import io.github.aid_labor.classifier.basis.io.system.OS;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindung;
import io.github.aid_labor.classifier.uml.programmierung.ExportImportVerarbeitung;
import io.github.aid_labor.classifier.uml.programmierung.ImportException;
import javafx.stage.FileChooser.ExtensionFilter;


public class JavaExportImportVerarbeitung implements ExportImportVerarbeitung {
//	private static final Logger log = Logger.getLogger(JavaExportImportVerarbeitung.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static final List<ExtensionFilter> importDateierweiterungen = List
			.of(new ExtensionFilter("Java", "*.java"));
	
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
			String[] paket = klassifizierer.getPaket().split(".");
			return OS.getDefault().pfadAus(OS.getDefault().pfadAus(paket), klassifizierer.getName()).toString();
		} else {
			return klassifizierer.getName();
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
	public void exportiere(UMLKlassifizierer klassifizierer, Writer ausgabe) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<UMLKlassifizierer> importiere(File quelle, List<UMLVerbindung> verbindungen)
			throws ImportException, IOException {
		
		ParserConfiguration konfiguration = new ParserConfiguration();
		konfiguration.setAttributeComments(false);
		konfiguration.setLanguageLevel(LanguageLevel.BLEEDING_EDGE);
		JavaParser parser = new JavaParser();
		ParseResult<CompilationUnit> ergebnis = parser.parse(quelle);
		
		if (!ergebnis.isSuccessful()) {
			Queue<Throwable> exceptions = new LinkedList<>();
			StringBuilder message = new StringBuilder("Aufgetrene Probleme: [Anzahl: ");
			message.append(ergebnis.getProblems().size());
			message.append("]\n");
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
			throw exc;
		}
		
		if (ergebnis.getResult().isPresent()) {
			return ergebnis.getResult().get().accept(new KlassifiziererBesucher(), verbindungen);
		} else {
			throw new ImportException("Kein Ergebnis!");
		}
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@SuppressWarnings("unused")
	private UMLKlassifizierer importiereMitJavaCompilerAPI(File quelle, List<UMLVerbindung> verbindungen)
			throws ImportException, IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		
		try (StandardJavaFileManager dateiManager = compiler.getStandardFileManager(null , null, null);
				StringWriter out = new StringWriter()){
			Iterable<? extends JavaFileObject> quelldateien = dateiManager.getJavaFileObjects(quelle);
			var task = compiler.getTask(out, dateiManager, null, null, null, quelldateien);
			
			boolean erfolg = task.call();
			if (!erfolg) {
				throw new ImportException(out.toString());
			}
		}
		
		return null;
	}
	
}