/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.eigenschaften;

import io.github.aid_labor.classifier.basis.Einstellungen;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;


public enum Modifizierer {
	
	PUBLIC("+"), PROTECTED("#"), PACKAGE("~"), PRIVATE("-");

// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final ReadOnlyStringWrapper kurzformProperty;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private Modifizierer(String kurzform) {
		this.kurzformProperty = new ReadOnlyStringWrapper(kurzform);
		Einstellungen.getBenutzerdefiniert().zeigePackageModifier
				.addListener((property, alt, zeigePackage) -> update(zeigePackage, kurzform));
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * {@code true}, wenn dieser Modifizierer PUBLIC ist
	 * 
	 * @return {@code true}, wenn dieser Modifizierer PUBLIC ist
	 */
	public boolean istPublic() {
		return this.equals(PUBLIC);
	}
	
	/**
	 * {@code true}, wenn dieser Modifizierer PUBLIC oder PROTECTED ist
	 * 
	 * @return {@code true}, wenn dieser Modifizierer PUBLIC oder PROTECTED ist
	 */
	public boolean istProtected() {
		return this.equals(PUBLIC) || this.equals(PROTECTED);
	}
	
	/**
	 * {@code true}, wenn dieser Modifizierer PUBLIC, PROTECTED oder PACKAGE ist
	 * 
	 * @return {@code true}, wenn dieser Modifizierer PUBLIC, PROTECTED oder PACKAGE ist
	 */
	public boolean istPackage() {
		return this.equals(PUBLIC) || this.equals(PROTECTED) || this.equals(PACKAGE);
	}
	
	/**
	 * {@code true}, wenn dieser Modifizierer PUBLIC, PROTECTED, PACKAGE oder PRIVATE ist
	 * 
	 * @return {@code true}, wenn dieser Modifizierer PUBLIC, PROTECTED, PACKAGE oder PRIVATE
	 *         ist
	 */
	public boolean istPrivate() {
		return true;
	}
	
	public ReadOnlyStringProperty getKurzform() {
		return kurzformProperty.getReadOnlyProperty();
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
	
	private void update(boolean zeigePackage, String kurzform) {
		if(this.equals(PACKAGE)) {
			if (zeigePackage) {
				kurzformProperty.set(kurzform);
			} else {
				kurzformProperty.set("");
			}
		}
	}
	
}