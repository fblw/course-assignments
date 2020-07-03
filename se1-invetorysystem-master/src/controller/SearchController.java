package controller;

import model.Artikel;
import model.ArtikelListe;
import view.DeleteCategoryView;
import view.MainView;
import view.SearchView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Steuert das View für die Suche: {@link SearchView}.
 * 
 * Enthält den {@link SubmitListener} um auch das klicken des 'Suchen'-Buttons zu regieren.
 * <br>
 * Ist das geschehen, wird das SerchView geschlossen und in der {@link Artikel}-Tabelle des
 * {@linkplain MainView MainViews} werden die Suchergebnisse angezeigt.
 * <p>
 * Mithilfe des Konstruktor {@link #SearchController(ArtikelListe, MainView)} kann auch ohne
 * SearchView nach Artikeln einer bestimmten Kategorie gesucht werden.<br>
 * Hierfür nach dem Konstruktoraufruf {@link #search(String)} aufrufen.
 * 
 * 
 * @author Gruppe 8
 *
 */
public class SearchController {
    
    private ArtikelListe artikelListe;
    private SearchView searchView;
    private MainView mainView;
    private MainController mainController;
    private ArrayList<Artikel> searchResult;
    
    /**
     * Konstruktor, ohne {@link SearchView}.
     * 
     * 
     * @param artikelListe	Liste der Artikel, die durchsucht werden
     * @param mainView		MainView, von welchem aus das {@link SearchView} geöffnet wurde
     */
    public SearchController(ArtikelListe artikelListe, MainView mainView) {
        this.artikelListe = artikelListe;
        this.mainView = mainView;
    }

    /**
     * Konstruktor, mit {@link SearchView}.
     * 
     * Das SearchView wird initialisiert.
     * 
     * @param artikelListe		Liste der Artikel, die durchsucht werden
     * @param searchView		View, das durch den Controller angesteuert wird
     * @param mainView			MainView, von welchem aus das {@link SearchView} geöffnet wurde
     * @param mainController	Controller selbigen MainViews
     */
    public SearchController(
        ArtikelListe artikelListe, 
        SearchView searchView,
        MainView mainView,
        MainController mainController
        ) {
        
        this(artikelListe, mainView);
        this.searchView = searchView;
        this.mainController = mainController;
        

        searchView.addSubmitListener(new SubmitListener());
        searchView.getCancelButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				searchView.dispose();
			}
            
        });
    }

    /**
     * Listener, der auf das Klicken des 'Suche starten'-Buttons wartet.
     * 
     * Die Suche wird durchgeführt, im {@link MainController} die Ergebnisse angezeigt,
     * und das {@link SearchView} geschlossen.
     * 
     *
     */
    class SubmitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            // Suche den Artikel
            searchResult = search(searchView.getInputFieldText());
            // Zeige alle gefundenen Artikel in MainView an, falls mainController vorhanden ist
            if (mainController != null) {
            	mainController.setSearchState(new model.ArtikelListe(searchResult));
                mainController.populateTable(searchResult, mainView.getDefaultTableModel());
            }
            searchView.dispose();
        }
    }

    /**
     * Sucht nach {@link Artikel Artikeln}, die das Suchkriterium erfüllen.
     * <p>
     * Wenn ein {@link SearchView} beim Konstruktoraufruf übergeben wurde, werden die
     * durch den User angehakten Attribute der Artikel durchsucht.
     * <br>
     * Sonst wird nur die Kategorie eines Artikels durchsucht
     * 
     * 
     * @param searchString	String nach dem gesucht wird
     * @return				eine ArrayList mit zu der Suche passenden Artikeln, die
     * 						Liste kann leer sein
     */
    public ArrayList<Artikel> search(String searchString) {

        ArrayList<Artikel> model = artikelListe.getArticleData();
        ArrayList<Artikel> result = new ArrayList<Artikel>();
        
        // Falls die Suche durch DelCatActionListener ausgelöst
        if (this.searchView == null) {
            // ...suche lediglich nach Artikeln mit dieser Kategorie
            
            for (int i = 0; i < model.size(); i++) {
                Artikel article = model.get(i);
                if (article.getKategorie().equals(searchString)) {
                    result.add(article);
                }
            }
        
        } else {
            // ...ansonsten starte eine normale Suche
            boolean[] selectedOptions = searchView.getSelectedOptions();
            
            for (int i = 0; i < model.size(); i++) {
                Artikel article = model.get(i);

                for (int j = 0; j < 6; j++) {
                    if (selectedOptions[j]) {
                        switch(j) {
                            case 0: if (KMPSearch(searchString.trim(), String.valueOf(article.getName())) && !result.contains(article)) result.add(article);
                            break;
                            case 1: if (KMPSearch(searchString.trim(), String.valueOf(article.getPlatznummer())) && !result.contains(article)) result.add(article);
                            break;
                            case 2: if (KMPSearch(searchString.trim(), String.valueOf(article.getKategorie())) && !result.contains(article)) result.add(article);
                            break;
                            case 3: if (KMPSearch(searchString.trim(), String.valueOf(article.getAnzahl())) && !result.contains(article)) result.add(article);
                            break;
                            case 4: if (KMPSearch(searchString.trim(), String.valueOf(article.getGewicht())) && !result.contains(article)) result.add(article);
                            break;
                            case 5: if (KMPSearch(searchString.trim(), String.valueOf(article.getPreis())) && !result.contains(article)) result.add(article);
                            break;
                        }
                    }
                }
            }
        }

        return result;
    }
    
    
    /**
	 * Gibt die gespeicherten Suchergebnisse zurück.
	 * @return ArrayList<Artikel> searchResult
	 */
    public ArrayList<Artikel> getSearchResult() { 
    	return this.searchResult;
    }

    private boolean KMPSearch(String pat, String txt) { 
        
        int m = pat.length(); // Länge des Suchstrings
        int n = txt.length(); // Länge des Texts
        int i = 0;
        int j = 0; 
        int lps[] = new int[m]; // Array, um längste Präfixe zu bestimmen, die auch ein Suffix des Suchstrings sind
  
        this.computeLPSArray(pat, m, lps); // Erzeugt das LPS Array
  
        while (i < n) { 
            
            if (pat.charAt(j) == txt.charAt(i)) { 
                j++; 
                i++; 
            } 
            
            if (j == m) { 
                return true;
            } 
            
            else if (i < n && pat.charAt(j) != txt.charAt(i)) {
                if (j != 0) 
                    j = lps[j - 1]; 
                else
                    i = i + 1; 
            } 
        }
        return false;
    } 
  
    private void computeLPSArray(String pat, int m, int lps[]) { 
        

        lps[0] = 0; // lps[0] wird jedesmal mit 0 initialisiert 
        int i = 0; // Laufindex: Untere Schranke
        int j = 1; // Laufindex: Obere Schranke
    
        while (j < m) {
            if (pat.charAt(i) != pat.charAt(j)) {
                
                if (i != 0) {
                    
                    i = lps[i-1];
                }
                
                else {
                    
                    lps[j] = i;
                    j++;
                }
            }
            else {
                
                lps[j] = i + 1;
                i++;
                j++;
            }
        }

    } 
}
