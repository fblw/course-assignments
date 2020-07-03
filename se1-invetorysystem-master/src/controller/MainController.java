package controller;

import model.Artikel;
import model.ArtikelListe;
import model.KategorienListe;
import model.RegalListe;
import view.AddCategoryView;
import view.ArticleView;
import view.DeleteCategoryView;
import view.MainView;
import view.SearchView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * Steuert das {@link MainView}
 * <p>
 * Diese Klasse händelt alle Aktionen, die vom MainView ausgehen.
 * Dafür ist für jeden Button ein eigener Listener vorgesehen.
 * <br>
 * Auch das Befüllen mit Daten übernimmt diese Klasse.
 * 
 * 
 * @author Gruppe 8
 * 
 */
public class MainController {
    
    private ArtikelListe artikelListe;
    private ArtikelListe searchState;
    private KategorienListe kategorienListe;
    private RegalListe regalListe;
    private MainView mainView;
    
    /**
     * Konstruktor, der das {@link MainView} initialisiert. 
     * 
     * 
     * @param artikelListe		Liste aller Artikel
     * @param kategorienListe	Liste aller Kategorien
     * @param regalListe		Liste der Regale
     * @param mainView			View, das durch den Controller angesteuert wird
     */
    public MainController(ArtikelListe artikelListe, KategorienListe kategorienListe, RegalListe regalListe, MainView mainView) {
        
        this.artikelListe = artikelListe;
        this.kategorienListe = kategorienListe;
        this.regalListe = regalListe;
        this.mainView = mainView;
        
        // Befülle die Tabelle beim Start mit Daten
        this.populateTable(artikelListe.getArticleData(), mainView.getDefaultTableModel());

        this.mainView.addNewCategoryListener(new NewCategoryListener());
        this.mainView.addDeleteCategoryListener(new DeleteCategoryListener());
        this.mainView.addNewArticleListener(new NewArticleListener());
        this.mainView.addSelectArticleListener(new SelectArticleListener());
        this.mainView.addSearchListener(new SearchListener());
        this.mainView.addResetListener(new ResetListener());
    }

    /**
     * Listener, der auf das Klicken des 'Kategorie hinzufügen' Buttons wartet
     * und das {@link AddCategoryView} startet.
     *
     */
    class NewCategoryListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            AddCategoryView addCatView = new AddCategoryView(); 
            new CategoryController(kategorienListe, addCatView, null);
            addCatView.setVisible(true);
        }
        
    }

    /**
     * Listener, der auf das Klicken des 'Kategorie entfernen' Buttons wartet
     * und das {@link DeleteCategoryView} öffnet.
     * 
     */
    class DeleteCategoryListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            DeleteCategoryView deleteView = new DeleteCategoryView(); 
            new CategoryController(artikelListe, kategorienListe, deleteView, mainView, MainController.this);
            deleteView.setVisible(true);
        }
    }

    /**
     * Listener, der auf das Klicken des 'Artikel hinzufügen' Buttons lauscht
     * und das {@link ArticleView} startet.
     *
     */
    class NewArticleListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            ArticleView addEditArtView = new ArticleView(); 
            new ArticleController(artikelListe, kategorienListe, regalListe, addEditArtView, MainController.this);
            addEditArtView.setVisible(true);
        }
    }

    /**
     * Listener, der beim Wählen einer Zeile aus der Artikeltabelle aktiv wird.
     * 
     * Der gewählte {@link Artikel} wird zum Bearbeiten und Löschen über den {@link EditArticleListener} bzw. 
     * den {@link DeleteArticleListener} vorbereitet 
     *
     */
    class SelectArticleListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {

            mainView.removeAllButtonListeners(mainView.getDeleteArticleButton());
            mainView.removeAllButtonListeners(mainView.getEditArticleButton());
              
            if (mainView.getArticleTable().getSelectedRow() > -1 && !(e.getValueIsAdjusting())) {

                String vals[] = new String[6];

                for (int col = 0; col <= 5; col++) {
                    vals[col] = mainView.getArticleTable().getValueAt(mainView.getArticleTable().getSelectedRow(), col).toString();
                }

                Artikel a = new Artikel(
                    vals[0], 
                    vals[1], 
                    vals[2],  
                    Integer.parseInt(vals[3]), 
                    Double.parseDouble(vals[4]), 
                    Double.parseDouble(vals[5])
                );
                
                mainView.addDeleteArticleListener(new DeleteArticleListener(a));
                mainView.addEditArticleListener(new EditArticleListener(a));
            }
        }
        
    }

    /**
     * Listener, der auf das Klicken des 'Artikel löschen' Buttons wartet
     * und den {@link Artikel} aus der {@link ArtikelListe} löscht
     *
     */
    class DeleteArticleListener implements ActionListener {

        private Artikel a;

        /**
         * Konstruktor mit dem der {@link Artikel} gesetzt wird.
         * 
         * 
         * @param a Artikel, welcher gelöscht werden soll
         */
        DeleteArticleListener(Artikel a) {
            this.a = a;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            Object[] options = { "Abbrechen", "Löschen" };
            int pane = JOptionPane.showOptionDialog(mainView, "Möchten Sie den ausgewählten Artikel wirklich unwiderruflich löschen?", "Warnung",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

            if (pane == 1) {
                
                regalListe.reduceWeight(a.getPlatznummer().substring(0,3), a.getGewicht()*a.getAnzahl());
                regalListe.apply();
                artikelListe.delete(this.a);
                artikelListe.apply();

                // Updatet die View mit dem Zustand der Suche, falls eine Suche durchgeführt wurde
                if (getSearchState() != null) {

                    getSearchState().delete(this.a);
                    refresh(getSearchState().getArticleData());
                }
                // Sonst Standardrefresh
                else refresh(null);
            }
        }
    }

    /**
     * Listener, der auf das Klicken des 'Artikel bearbeiten' Buttons lauscht
     * und das {@link ArticleView} startet mit dem aus der Tabelle gewählten
     * {@link Artikel} öffnet.
     *
     */
    class EditArticleListener implements ActionListener {

        private Artikel a;

        /**
         * Konstruktor mit dem der {@link Artikel} gesetzt wird.
         * 
         * 
         * @param a Artikel, welcher bearbeitet werden soll
         */
        EditArticleListener(Artikel a) {
            this.a = a;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            
            ArticleView addEditView = new ArticleView(); 
            new ArticleController(artikelListe, kategorienListe, regalListe, addEditView, MainController.this, a);
            addEditView.setVisible(true);
        }
    }

    /**
     * Listener, der auf das Klicken des 'Suchen' Buttons lauscht
     * und das {@link SearchView} startet.
     *
     */
    class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            SearchView searchView = new SearchView(); 
            new SearchController(artikelListe, searchView, mainView, MainController.this);
            searchView.setVisible(true);
        }
    }

    /**
     * Listener, der auf das Klicken des 'Reset' Buttons wartet
     * und wieder alle Artikel in der Tabelle des {@linkplain MainView MainView} anzeigt
     *
     */
    class ResetListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            refresh(null);
        }
        
    }

    /**
     * Befüllt die Tabelle mit Artikeln.
     * 
     * 
     * @param al	ArrayList der hinzuzufügenden Artikel
     * @param dtm	TableModel der Tabelle
     * 
     * @see Artikel
     * @see DefaultTableModel
     */
    public void populateTable(ArrayList<Artikel> al, DefaultTableModel dtm) {

        // Entferne Tabelleninhalt vor der Aktualisierung
        dtm.setRowCount(0);

        for (Artikel a: al) {
            Object[] row = {
                a.getPlatznummer(),
                a.getName(),
                a.getKategorie(),
                a.getAnzahl(),
                a.getGewicht(),
                a.getPreis()
            };
            dtm.addRow(row);  
        }
    }

    /**
     * Befüllt die {@link Artikel}-Tabelle des {@linkplain MainView MainViews} mit Artikeln der {@link #artikelListe}
     * 
     */
    public void refresh(ArrayList<Artikel> tableData) {
        if (tableData == null) {
            this.populateTable(artikelListe.getArticleData(), mainView.getDefaultTableModel());
            setSearchState(null);
        } 
        else this.populateTable(tableData, mainView.getDefaultTableModel());
    }

    /**
     * Erzeugt einen {@link NewArticleListener}
     * 
     * 
     * @return einen neuen {@link NewCategoryListener}
     */
    public NewCategoryListener getNewCategoryListener() {
        return new NewCategoryListener();
    }

    /**
     * Legt den Zustand der Artikelliste nach der Suche fest.
     * @param searchState Die Liste der Artikel, nach denen gesucht wurde
     */
    public void setSearchState(ArtikelListe searchState) {
        this.searchState = searchState;
    }

    /**
     * Gibt den Zustand der Artikelliste nach der Suche aus.
     * @return searchState
     */
    public ArtikelListe getSearchState() {
        return this.searchState;
    }
}
