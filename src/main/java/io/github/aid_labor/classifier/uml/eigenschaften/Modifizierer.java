/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.eigenschaften;

public enum Modifizierer {
	
	PUBLIC, PROTECTED, PACKAGE, PRIVATE;

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
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
	
}