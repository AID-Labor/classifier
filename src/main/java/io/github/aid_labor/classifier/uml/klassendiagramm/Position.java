/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.uml.klassendiagramm;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.json.JsonDoubleProperty;

//@formatter:off
@JsonAutoDetect(
		getterVisibility = Visibility.NONE,
		isGetterVisibility = Visibility.NONE,
		setterVisibility = Visibility.NONE,
		fieldVisibility = Visibility.ANY
)
//@formatter:on
public class Position {

	private static final Logger log = Logger.getLogger(Position.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private JsonDoubleProperty x;
	private JsonDoubleProperty y;
	private JsonDoubleProperty hoehe;
	private JsonDoubleProperty breite;
	@JsonIgnore
	private final String id;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public Position(Object bean, String id) {
		this(0, 0, 0, 0, bean, id);
	}
	
	public Position(Position position, String id) {
		this(null, id, position);
	}
	
	public Position(Object bean, String id, Position position) {
		this(bean, id);
		setPosition(position);
	}
	
	@JsonCreator
	private Position(@JsonProperty("x") double x, @JsonProperty("y") double y, 
			@JsonProperty("hoehe") double hoehe, @JsonProperty("breite") double breite) {
		this.x = new JsonDoubleProperty(null, "x", x);
		this.y = new JsonDoubleProperty(null, "y", y);
		this.hoehe = new JsonDoubleProperty(null, "hoehe", hoehe);
		this.breite = new JsonDoubleProperty(null, "breite", breite);
		this.id = "json";
	}
	
	public Position(double x, double y, double hoehe, double breite, Object bean, String id) {
		this.x = new JsonDoubleProperty(bean, id + "-x", x);
		this.y = new JsonDoubleProperty(bean, id + "-y", y);
		this.hoehe = new JsonDoubleProperty(bean, id + "-hoehe", hoehe);
		this.breite = new JsonDoubleProperty(bean, id + "-breite", breite);
		this.id = id;
	}
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public double getX() {
		return x.get();
	}
	
	public void setX(double x) {
		this.x.set(x);
	}
	
	public double getY() {
		return y.get();
	}
	
	public void setY(double y) {
		this.y.set(y);
	}
	
	public double getHoehe() {
		return hoehe.get();
	}
	
	public void setHoehe(double hoehe) {
		this.hoehe.set(hoehe);
	}
	
	public double getBreite() {
		return breite.get();
	}
	
	public void setBreite(double breite) {
		this.breite.set(breite);
	}
	
	@JsonIgnore
	public double getMaxX() {
		return getX() + getBreite();
	}
	
	@JsonIgnore
	public double getMaxY() {
		return getY() + getHoehe();
	}
	
	@JsonIgnore
	public JsonDoubleProperty xProperty() {
		return x;
	}
	
	@JsonIgnore
	public JsonDoubleProperty yProperty() {
		return y;
	}
	
	@JsonIgnore
	public JsonDoubleProperty hoeheProperty() {
		return hoehe;
	}
	
	@JsonIgnore
	public JsonDoubleProperty breiteProperty() {
		return breite;
	}
	
	@JsonIgnore
	public boolean istRechtsVon(Position p) {
		return this.getX() > p.getMaxX();
	}
	
	@JsonIgnore
	public boolean istLinksVon(Position p) {
		return this.getMaxX() < p.getX();
	}
	
	@JsonIgnore
	public boolean istOberhalbVon(Position p) {
		return this.getMaxY() < p.getY();
	}
	
	@JsonIgnore
	public boolean istUnterhalbVon(Position p) {
		return this.getY() > p.getMaxY();
	}
	
	@JsonIgnore
	public boolean istRechtsVon(Position p, double deltaMin) {
		return this.getX() - deltaMin > p.getMaxX();
	}
	
	@JsonIgnore
	public boolean istLinksVon(Position p, double deltaMin) {
		return this.getMaxX() + deltaMin < p.getX();
	}
	
	@JsonIgnore
	public boolean istOberhalbVon(Position p, double deltaMin) {
		return this.getMaxY() + deltaMin < p.getY();
	}
	
	@JsonIgnore
	public boolean istUnterhalbVon(Position p, double deltaMin) {
		return this.getY() - deltaMin > p.getMaxY();
	}
	
	@JsonIgnore
	public void setPosition(Position position) {
		this.setX(position.getX());
		this.setY(position.getY());
		this.setHoehe(position.getHoehe());
		this.setBreite(position.getBreite());
	}
	
	@JsonIgnore
	public void set(Position position) {
		this.setX(position.getX());
		this.setY(position.getY());
		this.setBreite(position.getBreite());
		this.setHoehe(position.getHoehe());
	}
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@Override
	public int hashCode() {
		return ClassifierUtil.hashAlle(getX(), getY(), getBreite(), getHoehe());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Position other = (Position) obj;
		
		boolean xGleich = this.getX() == other.getX();
		boolean yGleich = this.getY() == other.getY();
		boolean breiteGleich = this.getBreite() == other.getBreite();
		boolean hoeheGleich = this.getHoehe() == other.getHoehe();
		
		boolean istGleich = xGleich && yGleich && breiteGleich && hoeheGleich;
		
		Level level = istGleich ? Level.FINEST : Level.FINE;
		log.log(level, () -> """
				istGleich: %s   [Position %s {%s}]
				   |-- xGleich: %s [%f =? %f]
				   |-- yGleich: %s [%f =? %f]
				   |-- breiteGleich: %s [%f =? %f]
				   ╰-- hoeheGleich: %s [%f =? %f]"""
				.formatted(istGleich, id, x.getBean(), xGleich, this.getX(), other.getX(), yGleich, this.getY(), 
						other.getY(), breiteGleich, this.getBreite(), other.getBreite(), hoeheGleich, this.getHoehe(), 
						other.getHoehe()));
		
		return istGleich;
	}
	
	@Override
	public String toString() {
		return "Position(x=%.1f; y=%.1f)[breite=%.1f; hoehe=%.1f]".formatted(getX(), getY(), getBreite(), getHoehe());
	}
	
}