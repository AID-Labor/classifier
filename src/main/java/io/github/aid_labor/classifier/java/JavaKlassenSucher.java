/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.module.ModuleReader;
import java.lang.module.ResolvedModule;
import java.lang.reflect.Modifier;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.io.Ressourcen;


public class JavaKlassenSucher {
	
	private static final Logger log = Logger.getLogger(JavaKlassenSucher.class.getName());
	
	public static void main(String[] args) {
		
		SortedMap<String, SortedMap<String, SortedSet<String>>> module = findeKlassenUndInterfacesNachModul();
//		schreibeModuleInDatei(module, "pakete_und_klassen");
		
		SortedMap<String, SortedMap<String, SortedSet<String>>> interfaces = extrahiereInterfaces(module);
		SortedMap<String, SortedMap<String, SortedSet<String>>> klassen = extrahiereKlassen(module);
		SortedMap<String, SortedMap<String, SortedSet<String>>> enumerationen = extrahiereEnumerationen(module);
//		schreibeModuleInDatei(interfaces, "module_interfaces");
//		schreibeModuleInDatei(klassen, "module_klassen");
//		schreibeModuleInDatei(enumerationen, "module_enumerationen");
		
		schreibePaketeUndKlassenInDatei(klassen, BEKANNTE_KLASSEN);
		schreibePaketeUndKlassenInDatei(interfaces, BEKANNTE_INTERFACES);
		schreibePaketeUndKlassenInDatei(enumerationen, BEKANNTE_ENUMERATIONEN);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private static final String RESSOURCE_ORDNER = "src/main/resources/ressourcen/java/";
	private static final String BEKANNTE_KLASSEN = RESSOURCE_ORDNER + "bekannte_klassen";
	private static final String BEKANNTE_INTERFACES = RESSOURCE_ORDNER + "bekannte_interfaces";
	private static final String BEKANNTE_ENUMERATIONEN = RESSOURCE_ORDNER + "bekannte_enumerationen";
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	static SortedSet<String> ladeKlassen() {
		return ladeDatei(Ressourcen.get().JAVA_BEKANNTE_KLASSEN.alsPath().toFile());
	}
	
	static SortedSet<String> ladeEnumerationen() {
		return ladeDatei(Ressourcen.get().JAVA_BEKANNTE_ENUMERATIONEN.alsPath().toFile());
	}
	
	static SortedSet<String> ladeInterfaces() {
		return ladeDatei(Ressourcen.get().JAVA_BEKANNTE_INTERFACES.alsPath().toFile());
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private static SortedSet<String> ladeDatei(File datei) {
		SortedSet<String> inhalt = new TreeSet<>();
		try (BufferedReader read = new BufferedReader(new FileReader(datei))) {
			inhalt.addAll(read.lines().toList());
		} catch (IOException e) {
			log.log(Level.WARNING, e, () -> "Fehler beim Lesen der Datei " + datei);
		}
		
		return inhalt;
	}
	
	private static SortedMap<String, SortedMap<String, SortedSet<String>>> findeKlassenUndInterfacesNachModul() {
		SortedMap<String, SortedMap<String, SortedSet<String>>> module = new TreeMap<>();
		
		ModuleLayer.boot().configuration().modules().stream().filter(mod -> mod.name().startsWith("java"))
				.map(ResolvedModule::reference).forEach(modRef -> {
					try (ModuleReader reader = modRef.open()) {
						reader.list().filter(inhalt -> inhalt.endsWith(".class") && !inhalt.contains("-info"))
								.forEach(modulInhalt -> {
									var mod = ModuleLayer.boot().findModule(modRef.descriptor().name());
									if (mod.isEmpty()) {
										return;
									}
									werteModulEintragAus(modulInhalt, mod.get(), module);
								});
					} catch (Exception ioe) {
						ioe.printStackTrace();
					}
				});
		return module;
	}
	
	private static void werteModulEintragAus(String modulInhalt, Module mod,
			SortedMap<String, SortedMap<String, SortedSet<String>>> module) {
		int indexPaketEnde = modulInhalt.lastIndexOf("/");
		int indexKlassenende = modulInhalt.lastIndexOf('.');
		if (indexPaketEnde < 0 || indexKlassenende < 0) {
			return;
		}
		
		String paketname = modulInhalt.substring(0, indexPaketEnde).replace('/', '.');
		String klassenname = modulInhalt.substring(indexPaketEnde + 1, indexKlassenende);
		
		if (!mod.isExported(paketname) || !istKlasseOeffentlich(paketname, klassenname)) {
			return;	// nur exportierte Pakete und oeffentliche Klassen nutzen
		}
		
		SortedMap<String, SortedSet<String>> pakete = module.computeIfAbsent(mod.getName(), key -> new TreeMap<>());
		
		var klassenliste = pakete.computeIfAbsent(paketname, key -> new TreeSet<>());
		klassenliste.add(klassenname);
	}
	
	private static boolean istKlasseOeffentlich(String paket, String klasse) {
		try {
			Class<?> clazz = ClassLoader.getPlatformClassLoader().loadClass(paket + "." + klasse);
			return Modifier.isPublic(clazz.getModifiers());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static void schreibeModuleInDatei(SortedMap<String, SortedMap<String, SortedSet<String>>> module,
			String dateiname) {
		try (var pw = new PrintWriter(dateiname)) {
			module.forEach((modul, pakete) -> pakete.forEach((paket, klassen) -> {
				pw.println("%s/%s: [%d] %s".formatted(modul, paket, klassen.size(), klassen));
			}));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void schreibePaketeUndKlassenInDatei(SortedMap<String, SortedMap<String, SortedSet<String>>> module,
			String dateiname) {
		SortedSet<String> klassen = new TreeSet<>();
		
		module.values().forEach(pakete -> {
			pakete.forEach((paketname, klassenListe) -> klassenListe
					.forEach(klassenname -> klassen.add(paketname + ":" + klassenname.replace('$', '.'))));
		});
		
		try (var pw = new PrintWriter(dateiname)) {
			klassen.forEach(pw::println);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	private static SortedMap<String, SortedMap<String, SortedSet<String>>> extrahiereInterfaces(
			SortedMap<String, SortedMap<String, SortedSet<String>>> module) {
		return filterKlassenInModulen(module, clazz -> clazz.isInterface());
	}
	
	private static SortedMap<String, SortedMap<String, SortedSet<String>>> extrahiereKlassen(
			SortedMap<String, SortedMap<String, SortedSet<String>>> module) {
		return filterKlassenInModulen(module,
				clazz -> !clazz.isInterface() && !clazz.isAnnotation() && !clazz.isEnum());
	}
	
	private static SortedMap<String, SortedMap<String, SortedSet<String>>> extrahiereEnumerationen(
			SortedMap<String, SortedMap<String, SortedSet<String>>> module) {
		return filterKlassenInModulen(module, clazz -> clazz.isEnum());
	}
	
	private static SortedMap<String, SortedMap<String, SortedSet<String>>> filterKlassenInModulen(
			SortedMap<String, SortedMap<String, SortedSet<String>>> module, Predicate<Class<?>> bedingung) {
		SortedMap<String, SortedMap<String, SortedSet<String>>> moduleGefiltert = new TreeMap<>();
		
		module.forEach((modulname, paket) -> {
			var paketeGefiltert = filterKlassen(paket, bedingung);
			if (!paketeGefiltert.isEmpty()) {
				moduleGefiltert.put(modulname, paketeGefiltert);
			}
		});
		
		return moduleGefiltert;
	}
	
	private static SortedMap<String, SortedSet<String>> filterKlassen(SortedMap<String, SortedSet<String>> pakete,
			Predicate<Class<?>> bedingung) {
		SortedMap<String, SortedSet<String>> paketeGefiltert = new TreeMap<>();
		
		pakete.forEach((paketname, klassenliste) -> {
			klassenliste.forEach(klassenname -> {
				try {
					Class<?> clazz = ClassLoader.getPlatformClassLoader().loadClass(paketname + "." + klassenname);
					if (bedingung.test(clazz)) {
						SortedSet<String> klassenGefiltert = paketeGefiltert.computeIfAbsent(paketname,
								key -> new TreeSet<>());
						klassenGefiltert.add(klassenname);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			});
		});
		
		return paketeGefiltert;
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
	
	private JavaKlassenSucher() {
		// Hilfsklasse, nicht instanziierbar!
		throw new UnsupportedOperationException("Hilfsklasse darf nicht instanziiert werden!");
	}
	
}