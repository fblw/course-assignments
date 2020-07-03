package controller;

import model.Artikel;
import model.ArtikelListe;
import model.KategorienListe;
import model.RegalListe;
import view.AddCategoryView;
import view.ArticleView;
import view.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;


/**
 * Steuert den {@link ArticleView} (Detailansicht eines Artikels) an.
 * <p>
 * Es sind zwei Konstruktoren möglich:
 * <br>
 * 1. {@link #ArticleController(ArtikelListe, KategorienListe, RegalListe, ArticleView, MainController)}
 * bei dem ein neuer Artikel angelegt wird und alle Textfelder leer sind.
 * <br>
 * 2. {@link #ArticleController(ArtikelListe, KategorienListe, RegalListe, ArticleView, MainController, Artikel)}
 * hier wird ein Artikel übergeben, der bearbeitet wird.
 * <p>
 * Die Klasse enthält zwei Listener ({@link SubmitListener} und {@link NewCategoryListener}),
 * um auf das Drücken von Buttons zu reagieren. 
 * 
 * 
 *  @author Gruppe 8
 *
 */
public class ArticleController {

    private Artikel selectedArticle;
    private ArtikelListe artikelListe;
    private KategorienListe kategorienListe;
    private RegalListe regalListe;
    private ArticleView articleView;
    private MainController mainController;

    // "Hinzufügen" Konstruktor
    /**
     * Erstellt einen Controller für einen neuen Artikel.
     * 
     * 
     * @param artikelListe		Liste aller Artikel
     * @param kategorienListe	Liste aller Kategorien
     * @param regalListe		Liste aller Regale
     * @param articleView		View, das welches dieser Controller ansteuert 
     * @param mainController	Controller, der das {@link MainView} ansteuert
     */
    public ArticleController(ArtikelListe artikelListe, KategorienListe kategorienListe, RegalListe regalListe,
            ArticleView articleView, MainController mainController)

    {

        this.artikelListe = artikelListe;
        this.kategorienListe = kategorienListe;
        this.regalListe = regalListe;
        this.articleView = articleView;
        this.mainController = mainController;
        this.populateKategorie(kategorienListe);

        this.articleView.addSubmitListener(new SubmitListener());
        this.articleView.addNewCategoryListener(new NewCategoryListener());
    }

    // "Bearbeiten" Konstruktor
    /**
     * Erstellt einen Controller zum bearbeiten eines bestehenden Artikels.
     * 
     * 
     * @param artikelListe		Liste aller Artikel
     * @param kategorienListe	Liste aller Kategorien
     * @param regalListe		Liste aller Regale
     * @param articleView		View, das welches dieser Controller ansteuert 
     * @param mainController	Controller, der das {@link MainView} ansteuert
     * @param selectedArticle	Artikel, der bearbeitet wird
     */
    public ArticleController(ArtikelListe artikelListe, KategorienListe kategorienListe, RegalListe regalListe,
            ArticleView articleView, MainController mainController, Artikel selectedArticle)

    {

        this(artikelListe, kategorienListe, regalListe, articleView, mainController);
        this.selectedArticle = selectedArticle;

        articleView.setTxtPlatz(selectedArticle.getPlatznummer());
        articleView.setTxtName(selectedArticle.getName());
        articleView.setCbxKategorie(selectedArticle.getKategorie());
        articleView.setTxtAnzahl(String.valueOf(selectedArticle.getAnzahl()));
        articleView.setTxtGewicht(String.valueOf(selectedArticle.getGewicht()));
        articleView.setTxtPreis(String.valueOf(selectedArticle.getPreis()));
    }
    
    /**
     * Listener, der auf das Betätigen des Submitbuttons lauscht.
     * <p>
     * Die Eingaben werden auf Plausibilität getestet und ein Fehlerdialog
     * angezeigt, falls etwas nicht stimmt.
     * <br> 
     * Andernfalls wird die Änderung des {@linkplain Artikel Artikels} bzw. 
     * der neue Artikel übernommen und das {@link ArticleView} geschlossen.
     * 
     */
    public class SubmitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            int newCount = Integer.parseInt(articleView.getTxtAnzahl());
            Double newWeight = Double.parseDouble(articleView.getTxtGewicht().replace(",", "."));
            String newLocation = articleView.getTxtPlatz();
            String newName = articleView.getTxtName().trim();

            String[] verifyNulls = {
                newLocation,
                newName,
                articleView.getSelectedCategory(),
                articleView.getTxtAnzahl(),
                articleView.getTxtGewicht(),
                articleView.getTxtPreis()
            };
            
            Artikel a = new Artikel(
                    newLocation, 
                    newName,
                    articleView.getSelectedCategory(),
                    newCount,
                    newWeight,
                    Double.parseDouble(articleView.getTxtPreis().replace(",", ".")));

            // Prüfe, ob die Benutzereingaben für den Artikel vollständig sind
            if (Arrays.asList(verifyNulls).contains(null) || Arrays.asList(verifyNulls).contains("")) { 
                
                JOptionPane.showMessageDialog(articleView,
                    "Einige erforderliche Felder sind ohne Angaben.\nBitte füllen Sie das Formular vollständig aus, bevor Sie die Eingabe bestätigen.",
                    "Warnung", JOptionPane.WARNING_MESSAGE);
            
            } else {

                // Ordne den Teilstring der Regalnummer einem String "regalId" zu
                String regalId = a.getPlatznummer().substring(0, 3);
                
                // Variablenzuordnung, um das neue Gesamtgewicht zu errechnen
                Double totalWeight;
                Double shelfWeight = regalListe.getShelfData().get(regalId);
                
                // Falls ein Artikel ausgewählt wurde, wird der Artikel bearbeitet...
                if (selectedArticle != null) {
                    
                    int oldCount = selectedArticle.getAnzahl();
                    Double oldWeight = selectedArticle.getGewicht();
                    String oldLocation = selectedArticle.getPlatznummer();
                    String oldName = selectedArticle.getName();
                    String oldRegalId = selectedArticle.getPlatznummer().substring(0, 3);
                    Double oldShelfWeight = regalListe.getShelfData().get(oldRegalId);

                    // Prüfe, ob die Platznummer sich geändert hat und diese bereits vergeben ist
                    if (!oldLocation.equals(newLocation) && artikelListe.contains(newLocation, null)) {
                    	
                    	if (artikelListe.getTestMode()) return; // Zeige keine Warnmeldung im Testmodus, um die Automatisierung der Testfälle nicht zu unterbrechen
                    	
                        JOptionPane.showMessageDialog(articleView,
                            "Die eingegebene Platznummer ist bereits vergeben. \nÜberprüfen Sie die Angaben zu diesem Feld, bevor Sie die Eingabe bestätigen",
                            "Warnung", JOptionPane.WARNING_MESSAGE);
                        return;
                    } 
                    
                    // Prüfe, ob der Artikelname sich geändert hat und dieser bereits vergeben ist
                    if (!oldName.equals(newName) && artikelListe.contains(null, newName)) {
                    	
                    	if (artikelListe.getTestMode()) return; // Zeige keine Warnmeldung im Testmodus, um die Automatisierung der Testfälle nicht zu unterbrechen
                    	
                        JOptionPane.showMessageDialog(articleView,
                            "Die eingegebene Produktbezeichnung ist bereits vorhanden. \nÜberprüfen Sie die Angaben zu diesem Feld, bevor Sie die Eingabe bestätigen",
                            "Warnung", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    else {

                        // Falls sich das Regal nicht geändert hat
                        if (regalId.equals(oldRegalId)) {

                            // Berechne das Gesamtgewicht
                            totalWeight = oldShelfWeight - (oldWeight*oldCount) + (newWeight*newCount);
                        
                        // Ansonsten hat sich das Regal geändert
                        } else {
                            
                            // Gibt es bereits Artikel im neuen Regal ?
                            if (shelfWeight != null) {
                                
                                totalWeight = shelfWeight + (newWeight*newCount);
                            }
                            // Ansonsten entspricht das Gesamtgewicht des geänderten Artikels dem Gesamtgewicht des Regals
                            else totalWeight = (newWeight*newCount);
                           
                        }

                        // Falls das Gesamtgewicht der im Regal befindlichen Artikel ein Mikrogramm mehr
                        // als 10 Tonnen wiegt, kann der Artikel nicht zum Invetar hinzugefügt werden
                        if (totalWeight > 10000000.000000) {

                            JOptionPane.showMessageDialog(articleView,
                                    "Das Artikelgewicht überschreitet die maximale Regalbelastung von 10 Tonnen. Artikel wurde nicht hinzugefügt.",
                                    "Warnung", JOptionPane.WARNING_MESSAGE);

                        } else {

                            regalListe.reduceWeight(oldRegalId, oldWeight*oldCount);
                            regalListe.addWeight(regalId, newWeight*newCount);
                            regalListe.apply();

                            artikelListe.update(selectedArticle, a);
                            artikelListe.apply();

                            articleView.dispose();
                            
                            if (mainController != null) { // Überprüfe auf testMode
                            	// Updatet die View mit dem Zustand der Suche, falls eine Suche durchgeführt wurde
                                if (mainController.getSearchState() != null) {

                                    mainController.getSearchState().update(selectedArticle, a);
                                    mainController.refresh(mainController.getSearchState().getArticleData());
                                }
                                // Sonst Standardrefresh
                                else mainController.refresh(null);
                            }            
                        }
                    }

                // ...ansonsten wird der Artikel neu hinzugefügt
                } else {
                    
                    // Prüfe, ob diese Platznummer bereits vergeben oder der Name bereits im Invetar ist
                    if (artikelListe.contains(newLocation, newName)) {
                        
                    	if (artikelListe.getTestMode()) return; // Zeige keine Warnmeldung im Testmodus, um die Automatisierung der Testfälle nicht zu unterbrechen
                    	
                        JOptionPane.showMessageDialog(articleView,
                            "Die eingegebene Produktbezeichnung ist bereits vorhanden oder die Platznummer ist bereits vergeben. \nÜberprüfen Sie die Angaben zu diesen beiden Feldern, bevor Sie die Eingabe bestätigen",
                            "Warnung", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Falls das Regal noch keine Artikel enthält, entspricht das neue Gewicht dem
                    // Gesamtregalgewicht
                    if (shelfWeight != null) {

                        totalWeight = shelfWeight + newWeight*newCount;
                    } else {

                        totalWeight = newWeight*newCount;
                    }

                    // Falls das Gesamtgewicht der im Regal befindlichen Artikel ein Mikrogramm mehr
                    // als 10 Tonnen wiegt, kann der Artikel nicht zum Invetar hinzugefügt werden
                    if (totalWeight > 10000000.000000) {

                        JOptionPane.showMessageDialog(articleView,
                                "Das Artikelgewicht überschreitet die maximale Regalbelastung von 10 Tonnen. Artikel wurde nicht hinzugefügt.",
                                "Warnung", JOptionPane.WARNING_MESSAGE);

                    } else {

                        regalListe.addWeight(regalId, newWeight*newCount);
                        regalListe.apply();

                        artikelListe.insert(a);
                        artikelListe.apply();

                        articleView.dispose();
                        
                        if (mainController != null) mainController.refresh(null);
                    }
                }
            }
        }
    }

    /**
     * Listener für den Button 'Neue Kategorie hinzufügen'.
     *
     */
    class NewCategoryListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            AddCategoryView addCatView = new AddCategoryView();
            new CategoryController(kategorienListe, addCatView, ArticleController.this);
            addCatView.setVisible(true);
        }
    }
    
    /**
     * Befüllt das Kategorieauswahlfeld im {@link ArticleView} mit den aktuellen Kategorien.
     * 
     * 
     * @param kl Liste der Kategorien
     */
    public void populateKategorie(KategorienListe kl) {

        Object[] categoryData = kl.getCategoryData().toArray();
        String[] stringArray = Arrays.copyOf(categoryData, categoryData.length, String[].class);
        String[] placeholder = {""};
        String[] comboBoxList = new String[stringArray.length + 1];

        System.arraycopy(placeholder, 0, comboBoxList, 0, 1);
        System.arraycopy(stringArray, 0, comboBoxList, 1, stringArray.length);
        
        if (!(categoryData == null)) {
            articleView.getCbxKategorie().setModel(new DefaultComboBoxModel(comboBoxList));
        }
    }
}
