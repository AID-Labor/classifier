/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis;

sealed class Unix extends OS permits Linux, MacOS {
	
	Unix() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	boolean useDarkTheme() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	String getKonfigurationsOrdner(ProgrammDetails programm) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
