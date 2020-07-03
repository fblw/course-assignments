import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.ArticleController;
import controller.CategoryController;
import model.Artikel;
import model.ArtikelListe;
import model.KategorienListe;
import model.RegalListe;
import view.AddCategoryView;
import view.ArticleView;

/*
 * Teststrategie der Fehlertests
 *
 * Partitioniert die zulässigen Eingaben nach folgendem Muster:
 * 
 * Produktbezeichnung: 	name.length() = 1, name.length() = 256
 * Kategorie: 			Auswahl einer beliebigen Kategorie
 * Platznummer:			0, 999999
 * Anzahl:				0, 1, 1e+6
 * Gewicht:				0.1, 1, 1e+6
 * Preis:				0.01, 1, Double.MAX_VALUE
 *
 * Die Länge des Produktnames ist begrenzt auf 256 Zeichen. Keine Eingaben sind nicht zulässig.
 * 
 * Für Anzahl und Gewicht muss geprüft werden, ob (Anzahl*Gewicht - 0.000001) < 1e+6 [Gramm] gilt. Falls
 * dies nicht zutrifft, wurde das maximale Regalgewicht überschritten. In diesem Fall soll eine Exception mit
 * zugehöriger Warnmeldung ausgelöst werden, die den Benutzer und das System über die Fehleingabe informiert.
 * 
 * Der Preis unterliegt einer Untergrenzen von 0.01 und keiner Obergrenze, daher wird der maximale Doublewert als oberer Grenzfall untersucht.
 *
 * Für die folgenden Fehlertests werden einzelne Kombinationen aus dem Kartesischen Produkt aller möglichen Eingaben gewählt.
 * Insgesamt sind 108 Partitionen möglich, die den gesamten sechs-dimensionalen Testraum abdecken. Aus Effizienzgründen werden 
 * lediglich die Teilmengen aller minimalen (testMinVals), aller maximalen (testMaxVals) und die der verbleibenden Eingabewerte 
 * (testRemainingVals) untersucht.
 * 
 */
public class Fehlertests {
	
	
	ArtikelListe inventory;
	KategorienListe categories;
	RegalListe shelves;
	ArticleView articleView;
	AddCategoryView categoryView;
	
	
	@Before
	public void setup() {
		
		// Mit Parameter true, um Daten in einer Testdatenbank zu speichern
		inventory = new ArtikelListe(true);
		categories = new KategorienListe(true);
		shelves = new RegalListe(true);
		
		// Fügt Dummydaten zur Datenbank der Kategorien hinzu
		categoryView = new AddCategoryView();
		new CategoryController(categories, categoryView, null);
			
		categoryView.setTxt("Kategorie_0");
		categoryView.getSubmitButton().doClick();
		categoryView.setTxt("Kategorie_1");
		categoryView.getSubmitButton().doClick();
		categoryView.setTxt("Kategorie_2");
		categoryView.getSubmitButton().doClick();
		categoryView.setTxt("Kategorie_3");
		categoryView.getSubmitButton().doClick();
		
		articleView = new ArticleView();
		new ArticleController(inventory, categories, shelves, articleView,  null);
	}
	
	// Deckt den folgenden Fall ab:
	//
	// Produktbezeichnung: 	name.length() = 1
	// Kategorie: 			Auswahl einer beliebigen Kategorie
	// Platznummer:			0
	// Anzahl:				0
	// Gewicht:				0.1
	// Preis:				0.01
	@Test
	public void testMinVals() {
			
		// Simuliert die Eingabe eines neuen Artikels
		Artikel aMin = new Artikel("0", "N", "Kategorie_0", 0, 0.1, 0.01);
		articleView.setTxtName(aMin.getName());
		articleView.setValuePlatz(Integer.valueOf(aMin.getPlatznummer()));
		articleView.setCbxKategorie(aMin.getKategorie()); // Muss bereits in src/data/test_categories.txt enthalten sein
		articleView.setValueAnzahl(aMin.getAnzahl());
		articleView.setValueGewicht(aMin.getGewicht());
		articleView.setValuePreis(aMin.getPreis());
		
		System.out.println("\nTeste Eingabe für Artikel: " + aMin.toString() + "\n");
		
		// Testet, ob die Eingabe den erwarteten Ausgabewerten entspricht
		assertEquals("N", articleView.getTxtName());
		assertEquals("Kategorie_0", articleView.getSelectedCategory());
		assertEquals("000000", articleView.getTxtPlatz());
		assertEquals("0", articleView.getTxtAnzahl());
		assertEquals("0.1", articleView.getTxtGewicht());
		assertEquals("0.01", articleView.getTxtPreis());	
		
		System.out.println("\tSuccess");
	}
	
	// Deckt den folgenden Fall ab:
	//
	// Produktbezeichnung: 	name.length() = 256
	// Kategorie: 			Auswahl einer beliebigen Kategorie
	// Platznummer:			999999
	// Anzahl:				1
	// Gewicht:				1000000
	// Preis:				Double.MAX_VALUE
	@Test
	public void testMaxVals() {
		
		// Erzeugt einen beliebigen String der Länge 256
		char[] Max_Length = new char[256];
		Arrays.fill(Max_Length, '*');
		
		Artikel aMax = new Artikel("999999", new String(Max_Length), "Kategorie_1", 1, 1000000, Double.MAX_VALUE);
		articleView.setTxtName(aMax.getName());
		articleView.setValuePlatz(Integer.valueOf(aMax.getPlatznummer()));
		articleView.setCbxKategorie(aMax.getKategorie()); // Muss bereits in src/data/test_categories.txt enthalten sein
		articleView.setValueAnzahl(aMax.getAnzahl());
		articleView.setValueGewicht(aMax.getGewicht());
		articleView.setValuePreis(aMax.getPreis());
		
		System.out.println("\nTeste Eingabe für Artikel: " + aMax.toString() + "\n");
		
		// Testet, ob die Eingabe den erwarteten Ausgabewerten entspricht
		assertEquals(new String(Max_Length), articleView.getTxtName());
		assertEquals("Kategorie_1", articleView.getSelectedCategory());
		assertEquals("999999", articleView.getTxtPlatz());
		assertEquals("1", articleView.getTxtAnzahl());
		assertEquals("1000000", articleView.getTxtGewicht());
		assertEquals(String.format("%.2f", Double.MAX_VALUE), articleView.getTxtPreis());
		
		System.out.println("\tSuccess");
	}
	
	// Deckt den folgenden Fall ab:
	//
	// Produktbezeichnung: 	name.length() = 1
	// Kategorie: 			Auswahl einer beliebigen Kategorie
	// Platznummer:			0
	// Anzahl:				1000000
	// Gewicht:				1
	// Preis:				1
	@Test
	public void testRemainingVals() {
		
		// Simuliert die Eingabe eines neuen Artikels
		Artikel aRem = new Artikel("0", "N", "Kategorie_2", 1000000, 1, 1);
		articleView.setTxtName(aRem.getName());
		articleView.setValuePlatz(Integer.valueOf(aRem.getPlatznummer()));
		articleView.setCbxKategorie(aRem.getKategorie()); // Muss bereits in src/data/test_categories.txt enthalten sein
		articleView.setValueAnzahl(aRem.getAnzahl());
		articleView.setValueGewicht(aRem.getGewicht());
		articleView.setValuePreis(aRem.getPreis());
		
		System.out.println("\nTeste Eingabe für Artikel: " + aRem.toString() + "\n");
		
		// Testet, ob die Eingabe den erwarteten Ausgabewerten entspricht
		assertEquals("N", articleView.getTxtName());
		assertEquals("Kategorie_2", articleView.getSelectedCategory());
		assertEquals("000000", articleView.getTxtPlatz());
		assertEquals("1000000", articleView.getTxtAnzahl());
		assertEquals("1", articleView.getTxtGewicht());
		assertEquals("1.00", articleView.getTxtPreis());
		
		System.out.println("\tSuccess");
		
	}
	
	// Testet, ob beim Hinzufügen eines neuen Artikels, dessen Gewicht*Anzahl
	// das maximale Regalgewicht überschreitet, eine entsprechende Ausnahme ausgelöst wird
	@Test(expected = IllegalArgumentException.class)
	public void testNewArticleException() {
		
		System.out.println("\nTeste IllegalArgumentException Handling...\n");
		
		Artikel a = new Artikel("0", "N", "Kategorie_3", 1, 1000000.1, Double.MAX_VALUE);
		articleView.setTxtName(a.getName());
		articleView.setValuePlatz(Integer.valueOf(a.getPlatznummer()));
		articleView.setCbxKategorie(a.getKategorie()); // Muss bereits in src/data/test_categories.txt enthalten sein
		articleView.setValueAnzahl(a.getAnzahl());
		articleView.setValueGewicht(a.getGewicht());
		articleView.setValuePreis(a.getPreis());
		articleView.getSubmitButton().doClick(); // Sollte eine IllegalArgumentException werfen, da das Regalgewicht überschritten wird
		
		System.out.println("\tFail");
		
	}
	
	// Entfernt alle vorhandenen Daten aus der Datenbank
	@After
	public void clear() {
		
		this.categories.removeAll();
		this.inventory.removeAll();
		this.shelves.removeAll();
	}
}
