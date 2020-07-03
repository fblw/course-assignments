package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Hält eine Liste der {@link Artikel} vor und persistiert diese in einer Datei.
 * <p>
 * Mit dem Aufruf des Konstruktors {@link #ArtikelListe()} werden die gespeicherten
 * Artikel aus der Datei <code>src/data/inventory.txt</code> gelesen und
 * entsprechende Objekte erzeugt, die dann über eine ArrayList mittels {@link #getArticleData()}
 * referenziert werden können.
 * <br>
 * Werden durch Aufrufen der Methoden {@link #insert(Artikel)}, {@link #delete(Artikel)},
 * und/oder {@link #update(Artikel, Artikel)} Änderungen vorgenommen, so muss anschließend
 * {@link #apply()} aufgerufen werden um die Änderungen in der Datei zu persistieren.
 * 
 * @author Gruppe 8
 *
 */
public class ArtikelListe {
    
    private ArrayList<Artikel> articleData;
    private boolean testMode; // true, falls getestet wird

    /**
     * Erzeugt eine Artikelliste, gefüllt mit den Einträgen der persistierten Daten.
     * <p>
     * Die Textdatei <code>src/data/inventory.txt</code> wird eingelesen und zu
     * {@link Artikel}-Objekten geparst. Wenn die Datei nicht vorhanden ist, oder nicht
     * gelesen werden kann, gibt {@link #getArticleData()} möglicherweise eine leere
     * oder unvollständige Liste an Artikeln zurück. 
     */
    public ArtikelListe(boolean testMode) {
        this.articleData = new ArrayList<Artikel>();
        this.testMode = testMode;
        this.initialize(testMode);
    }

    /**
     * Erzeugt eine Artikelliste, gefüllt mit den Einträgen von articleData.
     * <p>
     * Wenn dieser Konstruktor aufgerufen wird, wird keine neue Textdatei erzeugt. 
     */
    public ArtikelListe(ArrayList<Artikel> articleData) {
        this.articleData = articleData;
    }
    
    /**
     * Parst die Datei mit den gespeicherten Artikeln
     * <p>
     * Die Datei muss im Verzeichnis <code>src/data/inventory.txt</code> liegen und
     * korrekt benannt sein. Jede Zeile der Datei entspricht einem {@link Artikel}, die
     * einzelnen Artikel-Attribute müssen durch Tabulatoren voneinander getrennt sein.
     * Reihenfolge der Attribute ist die folgende:
     * <br>
     * <code>platznummer, name, kategorie, anzahl, gewicht, preis</code>
     */
    private void initialize(boolean testMode) {
        
        String line;
        
        if (testMode) {
            
            try (BufferedReader reader = new BufferedReader(new FileReader("src/data/test_inventory.txt"))) {

                while ((line = reader.readLine()) != null) {
                    
                    // Spalte jede Zeil in Tokens (Strings) auf
                    String[] tokens = line.split("\t");
                    
                    // Füge jeden Artikel zu articleData hinzu
                    Artikel a = new Artikel(tokens[0], tokens[1], tokens[2], Integer.parseInt(tokens[3]), Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5]));

                    this.insert(a);
                }
            } 
            
            catch (FileNotFoundException ex) {
                System.out.println("Warning: No data in src/data/test_inventory.txt");
            } 
            
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        else {
        	
        	try (BufferedReader reader = new BufferedReader(new FileReader("src/data/inventory.txt"))) {

                while ((line = reader.readLine()) != null) {
                    
                    // Spalte jede Zeil in Tokens (Strings) auf
                    String[] tokens = line.split("\t");
                    
                    // Füge jeden Artikel zu articleData hinzu
                    Artikel a = new Artikel(tokens[0], tokens[1], tokens[2], Integer.parseInt(tokens[3]), Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5]));

                    this.insert(a);
                }
            } 
            
            catch (FileNotFoundException ex) {
                System.out.println("Warning: No data in src/data/inventory.txt");
            } 
            
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Gibt eine Liste mit allen {@linkplain Artikel Artikeln} zurück.
     * @return Liste aller Artikel
     */
    public ArrayList<Artikel> getArticleData() {
        return this.articleData;
    }
    
    /**
     * Fügt einen Artikel zur Liste hinzu.
     * @param a	Artikel, der hinzugefügt werden soll
     */
    public void insert(Artikel a) {
        this.articleData.add(a);
    }

    /**
     * Löscht einen Artikel aus der Liste.
     * @param a Artikel, der gelöscht werden soll
     */
    public void delete(Artikel a) {
        this.articleData.remove(a);
    }
    
    /**
     * Löscht alle Artikel aus der Liste und der zugehörigen Datenbank.
     */
    public void removeAll() {
    	this.articleData = new ArrayList<Artikel>();
    	apply();
    }

    /**
     * Tauscht den einen gegen den anderen Artikel aus.
     * @param old	Artikel der entfernt wird
     * @param a		Artikel der hinzugefügt wird
     */
    public void update(Artikel old, Artikel a) {
        this.articleData.remove(old);
        this.articleData.add(a);
    }

    /**
     * Speichert die Artikeliste ab.
     * <p>
     * Die Datei wird im Verzeichnis <code>src/data/inventory.txt</code> mit entsprechendem
     * Namen angelegt oder überschrieben. Jeder {@link Artikel} wird in eine 
     * neue Zeile geschrieben, und seine Attribute werden durch Tabulatoren getrennt.
     * Reihenfolge der Attribute ist die folgende:
     * <br>
     * <code>platznummer, name, kategorie, anzahl, gewicht, preis</code>
     */
    public void apply() {
        
    	if (testMode) {
    		
    		try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/test_inventory.txt"))) {
                
                for (Artikel a: articleData) {
                    writer.write(a.toString() + "\n");
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
    	} 
    	else {
    		
    		try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/inventory.txt"))) {
                
                for (Artikel a: articleData) {
                    writer.write(a.toString() + "\n");
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
    	}
    }

    /**
     * Iteriert über articleData, um zu überprüfen, ob location als Platznummer, name als Artikelname oder 
     * beide Werte schon in articleData enthalten sind
     * <br>
     * @param location		Platznummer des Artikels
     * @param name          Name des Artikels
     * @return              true, falls name oder location in articleData enthalten ist, ansonsten false
     */

    public boolean contains(String location, String name) {
        
        for (int i = 0; i < this.articleData.size(); i++) {
            
            if (location != null) {
                
                if (this.articleData.get(i).getPlatznummer().equals(location)) return true;
            }
            
            if (name != null) {
                
                if (this.articleData.get(i).getName().equals(name)) return true;
            }
        }
        
        return false;
    }
    
    // Gibt aus, ob das System sich im Testmodus befindet
    public boolean getTestMode() {
    	return this.testMode;
    }
}
