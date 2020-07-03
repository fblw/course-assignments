import model.ArtikelListe;
import model.KategorienListe;
import model.RegalListe;
import view.MainView;
import controller.MainController;

/** 
 * Entrypoint für das Lagersystem. Diese Klasse ruft die MainView auf und initialisiert 
 * beim Starten der Software alle relevanten Daten (Artikel, Kategorien, Regale).
 *
 */
public class App {
	/**
	 * 
	 * @param args ungenutzt, soll <code>null</code> sein
	 */
	public static void main(String[] args) {

                /* INITIALIZE MODELS */
                ArtikelListe artikelListe = new ArtikelListe(false);
                KategorienListe kategorienListe = new KategorienListe(false);
                RegalListe regalListe = new RegalListe(false);

                /* INITIALIZE VIEW */
                MainView mainView = new MainView(); 

                /* INITIALIZE CONTROLLER */
                new MainController(artikelListe, kategorienListe, regalListe, mainView);
        
                mainView.setVisible(true);
	}
}
