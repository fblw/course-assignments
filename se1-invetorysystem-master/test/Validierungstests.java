import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.ArticleController;
import controller.CategoryController;
import controller.SearchController;
import model.Artikel;
import model.ArtikelListe;
import model.KategorienListe;
import model.RegalListe;
import view.AddCategoryView;
import view.ArticleView;
import view.SearchView;


public class Validierungstests {
	
	
	ArtikelListe inventory;
	KategorienListe categories;
	RegalListe shelves;
	ArticleView articleView;
	AddCategoryView categoryView;
	SearchView searchView;
	
	
	@Before
	public void setup() {
		
		// Mit Parameter true, um Daten in einer Testdatenbank zu speichern
		inventory = new ArtikelListe(true);
		categories = new KategorienListe(true);
		shelves = new RegalListe(true);
		
	}
	
	@Test
	public void assertAddNewArticle() {
		
		System.out.println("\nTeste 'Artikel hinzufügen' Funktion...\n");
		
		// Teste, ob das Inventar tatsächlich leer ist
		assertTrue(inventory.getArticleData().isEmpty());	
		
		// Bevor ein neuer Artikel gespeichert werden kann,
		// muss die zugehörige Kategorie erstellt werden
		categoryView = new AddCategoryView();
		new CategoryController(categories, categoryView, null);
		addCategory("Notizzettel");
		
		// Testartikel Nr.1
		articleView = new ArticleView();
		new ArticleController(inventory, categories, shelves, articleView,  null);
		Artikel a1 = new Artikel("111000", "Post-it Block gelb", "Notizzettel", 500, 0.3, 0.20);
		addArticle(a1);
		
		// Initialisiere ein neues Inventar, um zu überprüfen, ob der Artikel permanent hinzugefügt wurde
		ArtikelListe newInventory = new ArtikelListe(true);
		
		// Definiere den zuletzt hinzugefügten Artikel des neuen Inventars
		Artikel lastArticle;
		
		if (!newInventory.getArticleData().isEmpty()) {
			
			lastArticle = newInventory.getArticleData().get(newInventory.getArticleData().size() - 1);
		
		} else {
			
			lastArticle = null;
		}
		
		// Teste, ob der letzte Artikel des neuen Inventars dem soeben hinzugefügten Testartikel entspricht
		assertEquals(a1, lastArticle);
		
		System.out.println("\tSuccess");
	}
	
	@Test
	public void assertDBKeys() {
		
		System.out.println("\nTeste Eindeutigkeit der Datenbankschlüssel...\n");
		
		// Teste Eindeutigkeit der Kategorienschlüssel
		String kValidate = "Notizzettel";
		
		categoryView = new AddCategoryView();
		new CategoryController(categories, categoryView, null);
		
		// Versuche die Kategorie mehrfach zum Inventar hinzuzufügen und validiere, dass nur der erste Versuch funktioniert
		addCategory(kValidate);
		assertTrue(categories.getCategoryData().size() == 1);
		addCategory(kValidate); // kValidate wird nicht nochmal zur Datenbank hinzugefügt
		assertTrue(categories.getCategoryData().size() == 1);
		
		// Teste Eindeutigkeit der Artikelschlüssel
		Artikel aValidate = new Artikel("111000", "Post-it Block gelb", "Notizzettel", 500, 0.3, 0.20);
		
		// Artikel mit gleichem Namen wie aValidate
		Artikel a1 = new Artikel("111001", "Post-it Block gelb", "Notizzettel", 500, 0.3, 0.20);
		
		// Artikel mit gleicher Platznummer wie aValidate
		Artikel a2 = new Artikel("111000", "Post-it Block grün", "Notizzettel", 500, 0.3, 0.20);
		
		articleView = new ArticleView();
		ArticleController addArticleController = new ArticleController(inventory, categories, shelves, articleView,  null);
		
		// Versuche alle drei Artikel zum Inventar hinzuzufügen und validiere, dass nur der erste Artikel hinzugefügt wird
		addArticle(aValidate);
		assertTrue(inventory.getArticleData().size() == 1);
		addArticle(a1); // a1 wird nicht zum Inventar hinzugefügt
		assertTrue(inventory.getArticleData().size() == 1);
		addArticle(a2); // a2 wird nicht zum Inventar hinzugefügt
		assertTrue(inventory.getArticleData().size() == 1);
		addArticleController = null;
		
		// Überprüfe als nächstes, ob die Eindeutigkeitsbedingungen der Schlüssel auch beim Verändern eines Artikels gelten
		
		Artikel aTest = new Artikel("111001", "Post-it Block grün", "Notizzettel", 500, 0.3, 0.20); // Testartikel, um zu überprüfen, ob Artikel unzulässig verändert werden können
		addArticle(aTest);
		
		ArticleController editArticleController = new ArticleController(inventory, categories, shelves, articleView,  null, aValidate);
		
		articleView.setValuePlatz(111000); // Verändere die Platznummer von aTest zur Platznummer von aValidate
		articleView.getSubmitButton().doClick(); // Veränderter Artikel wird nicht hinzugefügt
		assertEquals("111001", inventory.getArticleData().get(0).getPlatznummer());
		
		articleView.setTxtName("Post-it Block gelb"); // Verändere den Artikelnamen von aTest zum Artikelnamen von aValidate
		articleView.getSubmitButton().doClick(); // Veränderter Artikel wird nicht hinzugefügt
		assertEquals("Post-it Block grün", inventory.getArticleData().get(0).getName());
		editArticleController = null;
		
		System.out.println("\tSuccess");
	}
	
	@Test
	public void assertSearchResults() {
		
		// Hinzufügen der Artikel aus Aufgabenstellung 4.1.3
		String[] neueKategorien = {
				"Schreibgeräte",
				"Korrekturflüssigkeit",
				"Büromaterial",
				"Notizzettel",
				"Hefter",
				"Papier",
				"Register"
		};
		
		categoryView = new AddCategoryView();
		new CategoryController(categories, categoryView, null);
		
		for (String k: neueKategorien) addCategory(k);
		
		Artikel[] neueArtikel = { 
			new Artikel("000012", "Stift_blau_0.05mm", "Schreibgeräte", 360, 0.1, 0.60),
			new Artikel("000013", "Stift_schwarz_0.03mm", "Schreibgeräte", 130, 0.1, 0.60),
			new Artikel("951167", "Tippex 3x", "Korrekturflüssigkeit", 97, 0.16, 4.95),
			new Artikel("461168", "Tippex 10x", "Korrekturflüssigkeit", 48, 0.55, 15.50), 
			new Artikel("332471", "Locher 25 Blatt rot", "Büromaterial", 37, 0.24, 6.25), 
			new Artikel("123483", "Post-it Block weiß", "Notizzettel", 460, 0.30, 2.15), 
			new Artikel("513513", "Hefter 100x bunt", "Hefter", 179, 4.50, 25.00), 
			new Artikel("986722", "Textmarker 3x neongrün", "Schreibgeräte", 255, 20.00, 3.50), 
			new Artikel("002001", "Kopierpapier 10x 500", "Papier", 513, 25.00, 35.95), 
			new Artikel("501993", "Trennstreifen", "Register", 83, 24.00, 25.00)
		};
		
		articleView = new ArticleView();
		new ArticleController(inventory, categories, shelves, articleView,  null);
		
		for (Artikel a: neueArtikel) addArticle(a);
		
		System.out.println("Teste Suchenfunktion...\n");
		
		
		//////// 1. Tippex ////////
		
		
		System.out.println("\tEingabe: Tippex");
		Object[] s1 = mockSearch("Tippex").toArray();
		
		// Erwartete Suchausgabe für s1
		Artikel[] s1Validate = { 
				new Artikel("951167", "Tippex 3x", "Korrekturflüssigkeit", 97, 0.16, 4.95),
				new Artikel("461168", "Tippex 10x", "Korrekturflüssigkeit", 48, 0.55, 15.50)
				};
		
		assertEquals(s1Validate.length, s1.length);
		
		for (int i = 0; i < s1Validate.length; i++) {
			assertEquals(s1Validate[i], s1[i]);
		}
		
		System.out.println("\tSuccess");
		
		
		////////2. Locher 25 Blatt rot ////////
		
		
		System.out.println("\tEingabe: Locher 25 Blatt rot");
		Object[] s2 = mockSearch("Locher 25 Blatt rot").toArray();
		
		// Erwartete Suchausgabe für s2
		Artikel[] s2Validate = { 
				new Artikel("332471", "Locher 25 Blatt rot", "Büromaterial", 37, 0.24, 6.25)
				};
		
		assertEquals(s2Validate.length, s2.length);
		
		for (int i = 0; i < s2Validate.length; i++) {
			assertEquals(s2Validate[i], s2[i]);
		}
		
		System.out.println("\tSuccess");
		
		
		////////3. Kopierpapier 10x 500 ////////
		
		
		System.out.println("\tEingabe: Kopierpapier 10x 500");
		Object[] s3 = mockSearch("Kopierpapier 10x 500").toArray();
		
		// Erwartete Suchausgabe für s3
		Artikel[] s3Validate = { 
				new Artikel("002001", "Kopierpapier 10x 500", "Papier", 513, 25.00, 35.95)
				};
		
		assertEquals(s3Validate.length, s3.length);
		
		for (int i = 0; i < s3Validate.length; i++) {
			assertEquals(s3Validate[i], s3[i]);
		}
		
		System.out.println("\tSuccess");
		
		
		////////4. Tuppex 10x ////////
		
		
		System.out.println("\tEingabe: Tuppex 10x");
		Object[] s4 = mockSearch("Tuppex 10x").toArray();
		
		// Erwartete Suchausgabe für s4
		Artikel[] s4Validate = {};
		
		assertEquals(s4Validate.length, s4.length);
		
		for (int i = 0; i < s4Validate.length; i++) {
			assertEquals(s4Validate[i], s4[i]);
		}
		
		System.out.println("\tSuccess");
		
		
		////////5. 3 ////////
		
		
		System.out.println("\tEingabe: 3");
		Object[] s5 = mockSearch("3").toArray();
		
		// Erwartete Suchausgabe für s5 (insgesamt 9 Artikel - alle Zahlen werden als Strings aufgefasst)
		Artikel[] s5Validate = { 
				new Artikel("000012", "Stift_blau_0.05mm", "Schreibgeräte", 360, 0.1, 0.60),
				new Artikel("000013", "Stift_schwarz_0.03mm", "Schreibgeräte", 130, 0.1, 0.60),
				new Artikel("951167", "Tippex 3x", "Korrekturflüssigkeit", 97, 0.16, 4.95),
				new Artikel("332471", "Locher 25 Blatt rot", "Büromaterial", 37, 0.24, 6.25), 
				new Artikel("123483", "Post-it Block weiß", "Notizzettel", 460, 0.30, 2.15), 
				new Artikel("513513", "Hefter 100x bunt", "Hefter", 179, 4.50, 25.00),
				new Artikel("986722", "Textmarker 3x neongrün", "Schreibgeräte", 255, 20.00, 3.50),
				new Artikel("002001", "Kopierpapier 10x 500", "Papier", 513, 25.00, 35.95),
				new Artikel("501993", "Trennstreifen", "Register", 83, 24.00, 25.00)
				};
		
		assertEquals(s5Validate.length, s5.length);
		
		for (int i = 0; i < s5Validate.length; i++) {
			assertEquals(s5Validate[i], s5[i]);
		}
		
		System.out.println("\tSuccess");
	}
	
	@After
	public void clear() {
		
		// Entferne alle Artikel und Kategorien aus dem Inventar nach jedem Testfall
		this.inventory.removeAll();
		this.categories.removeAll();
		this.shelves.removeAll();
	}
	
	// Legt die Werte des ArticleViews fest und bestätigt die Eingabe
	private void addArticle(Artikel a) {
		
		articleView.setTxtPlatz(a.getPlatznummer());
		articleView.setTxtName(a.getName());
		articleView.setCbxKategorie(a.getKategorie()); // Muss bereits in src/data/test_categories.txt enthalten sein
		articleView.setTxtAnzahl(String.valueOf(a.getAnzahl()));
		articleView.setTxtGewicht(String.valueOf(a.getGewicht()));
		articleView.setTxtPreis(String.valueOf(a.getPreis()));
		articleView.getSubmitButton().doClick();
		
	}
	
	// Legt die Werte des CategoryViews fest und bestätigt die Eingabe
	private void addCategory(String k) {
		
		categoryView.setTxt(k);
		categoryView.getSubmitButton().doClick();
		
	}
	
	private ArrayList<Artikel> mockSearch(String s) {
		
		searchView = new SearchView();
		searchView.setGeneralSearchOptions();
		searchView.setInputFieldText(s);
		SearchController searchController = new SearchController(inventory, searchView, null, null);
		searchView.getSubmitButton().doClick();
		
		return searchController.getSearchResult();
	}

}
