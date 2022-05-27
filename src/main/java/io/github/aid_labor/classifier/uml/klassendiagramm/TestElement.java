/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.uml.klassendiagramm;

import java.util.Random;

public class TestElement implements UMLDiagrammElement {
	
	private String name = "testelement";
	private final int id;
	
	public TestElement() {
		id = new Random().nextInt(1, 100);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TestElement e) {
			return this.name.equals(e.name) && this.id == e.id;
		}
		return super.equals(obj);
	}
}