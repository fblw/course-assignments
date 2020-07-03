package controller;

import model.Artikel;
import model.ArtikelListe;
import model.KategorienListe;
import model.RegalListe;
import view.AddCategoryView;
import view.ArticleView;
import view.DeleteCategoryView;
import view.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import controller.ArticleController.NewCategoryListener;
import controller.ArticleController.SubmitListener;


/**
 * Steuert die beiden Kategorieviews {@link AddCategoryView} und {@link DeleteCategoryView}.
 * <p>
 * Jedes View hat einen eigenen Konstruktor:
 * <br>
 * 1. {@link #CategoryController(KategorienListe, AddCategoryView, ArticleController)}
 * zum Anlegen einer neuen Kategorie, und
 * <br>
 * 2. {@link #CategoryController(ArtikelListe, KategorienListe, DeleteCategoryView, MainView, MainController)}
 * für das Löschen einer Kategorie
 * <p>
 * Die Klasse einen Listener ({@link SubmitListener}),
 * um auf das Drücken des Submitbuttons zu reagieren. 
 *  
 *  
 * @see KategorienListe 
 * @author Gruppe 8
 *
 */
public class CategoryController {
    
    private ArtikelListe artikelListe;
    private KategorienListe kategorienListe;
    private AddCategoryView addCategoryView;
    private DeleteCategoryView deleteCategoryView;
    private MainView mainView;
    private MainController mainController;
    private ArticleController articleController;

    /**
     * Konstruktor für einen Controller zum Anlegen einer neuen Kategorie.
     * 
     * @param kategorienListe	Liste, in welcher die neue Kategorie gespeichert werden soll
     * @param addCategoryView	View, das durch den Controller angesteuert wird
     * @param articleController	der {@link ArticleController} von dem aus die Kategorie erstellt wird, 
     * kann <code>null</code> sein
     */
    public CategoryController(KategorienListe kategorienListe, AddCategoryView addCategoryView, ArticleController articleController) {
        
        this.kategorienListe = kategorienListe;
        this.addCategoryView = addCategoryView;
        this.articleController = articleController;
        this.addCategoryView.addSubmitListener(new SubmitListener(false));
    }

    /**
     * Konstruktor für einen Controller zum löschen einer bestehenden Kategorie.
     * 
     * @param artikelListe			Liste aller Artikel
     * @param kategorienListe		Liste, aus welcher die Kategorie gelöscht wird
     * @param deleteCategoryView	View, das durch den Controller angesteuert wird
     * @param mainView				MainView, von welchem aus das {@link DeleteCategoryView} geöffnet wurde
     * @param mainController		Controller selbigen MainViews
     */
    public CategoryController(
        ArtikelListe artikelListe, 
        KategorienListe kategorienListe, 
        DeleteCategoryView deleteCategoryView, 
        MainView mainView,
        MainController mainController
        ) 
        {
        
        this.artikelListe = artikelListe;
        this.kategorienListe = kategorienListe;
        this.deleteCategoryView = deleteCategoryView;
        this.mainView = mainView;
        this.mainController = mainController;
        this.populateComboBox(kategorienListe);
        this.deleteCategoryView.addSubmitListener(new SubmitListener(true));
    }

    /**
     * Listener, der auf das Betätigen des Submitbuttons lauscht.
     * 
     *
     */
    class SubmitListener implements ActionListener {

        private boolean isDelete;

        /**
         * Konstruktor, mit dem gesetzt wird, ob eine Kategorie gelöscht oder
         * hinzugefügt werden soll.
         * 
         * 
         * @param isDelete	<code>true</code> für das {@link DeleteCategoryView}
         * 					<code>false</code> für das {@link AddCategoryView}
         */
        SubmitListener(boolean isDelete) {
            this.isDelete = isDelete;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            // Kontrollfluss zum Entfernen einer Kategorie i.e. dieser Controller wurde über DeleteCategoryView aufgerufen
            if (isDelete) {
                
                // Gibt die zu löschende Kategorie zurück
                String deletedCategory = deleteCategoryView.getSelectedCategory();
                
                ArrayList<Artikel> searchResult = new SearchController(artikelListe, mainView).search(deletedCategory);
                
                // Falls es noch Artikel gibt, die die zu löschende Kategorie noch enthalten...
                if (searchResult.size() > 0) {
                    
                    // ...starte eine Suche nach Artikel mit dieser Kategorie und zeige sie im mainView an
                    mainController.populateTable(searchResult, mainView.getDefaultTableModel());
                    deleteCategoryView.dispose();
                    
                    JOptionPane.showMessageDialog(mainView, 
                    "Das Lagersystem enthält noch Artikel (" 
                    + String.valueOf(searchResult.size()) 
                    + "), die mit dieser Kategorie verbunden sind.\nLöschen Sie zunächst diese/n Artikel, um die Kategorie aus dem System zu entfernen.",
                    "Warnung",
                    JOptionPane.WARNING_MESSAGE
                    );
                
                } else {
                    
                    // ...ansonsten lösche die Kategorie
                    kategorienListe.delete(deleteCategoryView.getSelectedCategory());
                    deleteCategoryView.dispose();
                }
            
            // Kontrollfluss zum Hinzufügen einer Kategorie i.e. dieser Controller wurde über AddCategoryView aufgerufen
            } else {

                String newCategory = addCategoryView.getTxt().trim();

                // Prüfe, ob die Benutzereingaben für die Kategorie vollständig ist
                if (newCategory.equals("") || newCategory.equals(null)) { 
                
                    JOptionPane.showMessageDialog(addCategoryView,
                        "Einige erforderliche Felder sind ohne Angaben.\nBitte füllen Sie das Formular vollständig aus, bevor Sie die Eingabe bestätigen.",
                        "Warnung", JOptionPane.WARNING_MESSAGE);
            
                }
                // Falls die Kategorie bereits im System enhalten ist...
                else if (kategorienListe.getCategoryData().contains(newCategory)) {
                	
                	if (kategorienListe.getTestMode()) return; // Zeige keine Warnmeldung im Testmodus, um die Automatisierung der Testfälle nicht zu unterbrechen
                	
                    // ...zeige eine Fehlermeldung an
                    JOptionPane.showMessageDialog(addCategoryView,
                    "Diese Kategorie ist bereits im Lagersystem enthalten.",
                    "Warnung", JOptionPane.WARNING_MESSAGE);
                
                // ...füge den Artikel hinzu sonst
                } else {
                    
                    kategorienListe.insert(newCategory);
                    addCategoryView.dispose();
                }
            }

            // aktualisiere die Combobox in der Artikelansicht, falls dieser Controller über die Artikelansicht aufgerufen wurde
            if (articleController != null) articleController.populateKategorie(kategorienListe);
            
            kategorienListe.apply();
            
        }
    }

    /**
     * Befüllt das Kategorieauswahlfeld des Views mit den aktuellen Kategorien.
     * 
     * 
     * @param kl Liste der Kategorien
     */
    public void populateComboBox(KategorienListe kl) {

        Object[] categoryData = kl.getCategoryData().toArray();
        String[] stringArray = Arrays.copyOf(categoryData, categoryData.length, String[].class);
        String[] placeholder = {""};
        String[] comboBoxList = new String[stringArray.length + 1];

        System.arraycopy(placeholder, 0, comboBoxList, 0, 1);
        System.arraycopy(stringArray, 0, comboBoxList, 1, stringArray.length);
        
        if (!(categoryData == null)) {
            deleteCategoryView.getDeleteComboBox().setModel(new DefaultComboBoxModel(comboBoxList));
        }
    }
}
