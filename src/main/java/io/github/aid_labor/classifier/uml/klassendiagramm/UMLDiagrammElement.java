/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.uml.klassendiagramm;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import io.github.aid_labor.classifier.basis.projekt.Editierbar;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLBasisElement.Position;

// @formatter:off
@JsonTypeInfo(
		use = Id.NAME,
		include = As.PROPERTY,
		property = "klasse"
)
@JsonSubTypes({
	@JsonSubTypes.Type(value = UMLBasisElement.class)
})
@JsonAutoDetect(
		getterVisibility = Visibility.NONE,
		isGetterVisibility = Visibility.NONE,
		setterVisibility = Visibility.NONE,
		creatorVisibility = Visibility.NONE,
		fieldVisibility = Visibility.ANY
)
// @formatter:on
public interface UMLDiagrammElement extends Editierbar {
	
	String getName();
	
	Position getPosition();

	long getId();
	
	void setId(long id);

	UMLDiagrammElement erzeugeTiefeKopie();

	
}