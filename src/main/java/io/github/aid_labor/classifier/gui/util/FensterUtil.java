/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.json.JsonUtil;
import javafx.beans.Observable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;


/**
 * Hilfsklasse fuer JavaFX Fenster
 * 
 * @author Tim Muehle
 *
 */
public class FensterUtil {
	private static final Logger log = Logger.getLogger(FensterUtil.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * Speichert und laedt die Groesse und Position eines Fensters
	 * 
	 * @param fenster     Fenster, das observiert wird
	 * @param minHoehe    kleinste erlaubte Hoehe des Fensters
	 * @param minBreite   kleinste erlaubte Breite des Fensters
	 * @param speicherort Ordner, in dem die Einstellungen fuer das Fenster gespeichert werden
	 */
	public static void installiereFensterwiederherstellung(Stage fenster, int minHoehe, int minBreite,
			Path speicherort) {
		new Fensterwiederherstellung(fenster, minHoehe, minBreite, speicherort);
	}
	
	/**
	 * Setzt das Elternfenster fuer einen Dialog und Positioniert den Dialog mittig ueber dem
	 * Elternfenster. Beim Anzeigen des Dialogs wird das Elternfenster blockiert. Der Dialog
	 * bekommt alle Stylesheets des Elternfensters zugewiesen.
	 * 
	 * @param elternFenster Elternfenster, an das der Dialog gebunden wird
	 * @param dialog        Dialog, der initialisiert wird
	 */
	public static void initialisiereElternFenster(Window elternFenster, Dialog<?> dialog) {
		new DialogPositionierung(elternFenster, dialog);
	}
	
	private static class DialogPositionierung {
		
		private boolean positioniert = false;
		
		private DialogPositionierung(Window elternFenster, Dialog<?> dialog) {
			Bounds position = new BoundingBox(elternFenster.getX(), elternFenster.getY(), elternFenster.getWidth(),
					elternFenster.getHeight());
			
			dialog.widthProperty().addListener((breite, alt, neueBreite) -> {
				if (!positioniert) {
					dialog.setX(position.getCenterX() - neueBreite.doubleValue() / 2);
				}
				
				log.finest(() -> "Dialog %s: x=%f y=%f hoehe=%f breite=%f".formatted(dialog.getTitle(), dialog.getX(),
						dialog.getY(), dialog.getHeight(), dialog.getWidth()));
			});
			dialog.heightProperty().addListener((hoehe, alt, neueHoehe) -> {
				if (!positioniert) {
					dialog.setY(position.getCenterY() - neueHoehe.doubleValue() / 2);
				}
				
				log.finest(() -> "Dialog %s: x=%f y=%f hoehe=%f breite=%f".formatted(dialog.getTitle(), dialog.getX(),
						dialog.getY(), dialog.getHeight(), dialog.getWidth()));
			});
			
			dialog.setOnShown(e -> {
				positioniert = true;
			});
			
			dialog.initOwner(elternFenster);
			dialog.initModality(Modality.WINDOW_MODAL);
			
			dialog.setX(position.getCenterX() - dialog.getWidth() / 2);
			dialog.setY(position.getCenterY() - dialog.getHeight() / 2);
			
			dialog.getDialogPane().getStylesheets().addAll(elternFenster.getScene().getStylesheets());
		}
		
	}
	
	private static class Fensterwiederherstellung {
		
		private final Stage fenster;
		private final Path speicherort;
		
		private static record Fensterpositonierung(double positionX, double positionY, double hoehe, double breite) {
			
			public Rectangle2D alsRectangle2D() {
				return new Rectangle2D(positionX, positionY, breite, hoehe);
			}
			
			@Override
			public String toString() {
				return "x = %f, y = %f, hoehe = %f, breite = %f".formatted(this.positionX, this.positionY, this.hoehe,
						this.breite);
			}
		}
		
		Fensterwiederherstellung(Stage fenster, int minHoehe, int minBreite, Path speicherort) {
			this.fenster = fenster;
			this.speicherort = speicherort.resolve("fensterposition.json");
			
			this.fenster.setMinHeight(minHoehe);
			this.fenster.setMinWidth(minBreite);
			ladePostiton();
			erstelleListener();
		}
		
		private void ladePostiton() {
			try (var parser = JsonUtil.getUTF8JsonParser(this.speicherort)) {
				Fensterpositonierung position = parser.readValueAs(Fensterpositonierung.class);
				this.fenster.setHeight(position.hoehe);
				this.fenster.setWidth(position.breite);
				
				if (Screen.getScreensForRectangle(position.alsRectangle2D()).isEmpty()) {
					log.fine(() -> "Position konnte nicht hergestellt werden, da sich die "
							+ "Positon ausserhalb der Monitore befand");
					fenster.centerOnScreen();
				} else {
					this.fenster.setX(position.positionX);
					this.fenster.setY(position.positionY);
				}
			} catch (IOException e) {
				log.log(Level.FINER, e, () -> "Fensterposititon konnte nicht geladen werden");
				this.fenster.setHeight(this.fenster.getMinHeight());
				this.fenster.setWidth(this.fenster.getMinWidth());
				this.fenster.centerOnScreen();
			}
		}
		
		private void erstelleListener() {
			this.fenster.heightProperty().addListener(this::speicherFensterposition);
			this.fenster.widthProperty().addListener(this::speicherFensterposition);
			this.fenster.xProperty().addListener(this::speicherFensterposition);
			this.fenster.yProperty().addListener(this::speicherFensterposition);
		}
		
		private void speicherFensterposition(Observable beobachtbar) {
			var position = new Fensterpositonierung(this.fenster.getX(), this.fenster.getY(), this.fenster.getHeight(),
					this.fenster.getWidth());
			try (var generator = JsonUtil.getUTF8JsonGenerator(this.speicherort)) {
				generator.writePOJO(position);
				log.finest(() -> "Gespeicherte Fensterposition: " + position);
			} catch (IOException e) {
				log.log(Level.WARNING, e, () -> "Fensterposititon konnte nicht gespeichert werden");
			}
		}
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private FensterUtil() {
		// Hilfsklasse, nicht instanziierbar!
		throw new UnsupportedOperationException("Hilfsklasse darf nicht instanziiert werden!");
	}
	
}