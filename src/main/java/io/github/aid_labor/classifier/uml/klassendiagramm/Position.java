/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.uml.klassendiagramm;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.json.JsonDoubleProperty;

public class Position {

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
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public Position(Object bean) {
		this(0, 0, 0, 0, bean);
	}
	
	public Position(Position position, Object bean) {
		this(bean);
		setPosition(position);
	}
	
	@JsonCreator
	private Position(@JsonProperty("x") double x, @JsonProperty("y") double y, 
			@JsonProperty("hoehe") double hoehe, @JsonProperty("breite") double breite) {
		this.x = new JsonDoubleProperty(null, "x", x);
		this.y = new JsonDoubleProperty(null, "y", y);
		this.hoehe = new JsonDoubleProperty(null, "hoehe", hoehe);
		this.breite = new JsonDoubleProperty(null, "breite", breite);
	}
	
	public Position(double x, double y, double hoehe, double breite, Object bean) {
		this.x = new JsonDoubleProperty(bean, "x", x);
		this.y = new JsonDoubleProperty(bean, "y", y);
		this.hoehe = new JsonDoubleProperty(bean, "hoehe", hoehe);
		this.breite = new JsonDoubleProperty(bean, "breite", breite);
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
		return Objects.equals(breite, other.breite) && Objects.equals(hoehe, other.hoehe)
				&& Objects.equals(x, other.x) && Objects.equals(y, other.y);
	}
	
	@Override
	public String toString() {
		return "Position(x=%.1f; y=%.1f)[breite=%.1f; hoehe=%.1f]".formatted(getX(), getY(), getBreite(), getHoehe());
	}
	
}