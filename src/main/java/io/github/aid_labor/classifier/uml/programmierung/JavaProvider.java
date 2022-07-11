/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.uml.programmierung;

public class JavaProvider implements Programmiersprache {
	
	private static JavaProvider instanz;
	
	public static JavaProvider provider() {
		if (instanz == null) {
			instanz = new JavaProvider();
		}
		return instanz;
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private JavaProvider() {
		
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

	@Override
	public String getName() {
		return "Java";
	}
	
	@Override
	public ProgrammierEigenschaften getEigenschaften() {
		return Java.getInstanz();
	}

	@Override
	public ExportImportVerarbeitung getVerarbeitung() {
		return null;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
}