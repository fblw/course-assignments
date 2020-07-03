package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Hält eine Liste der Kategorien vor und persistiert diese in einer Datei.
 * <p>
 * Mit dem Aufruf des Konstruktors {@link #KategorienListe()} werden die gespeicherten
 * Kategorien aus der Datei <code>src/data/categories.txt</code> gelesen und
 * als Strings in eine Liste gepackt. Die Liste kann anschließend über {@link #getCategoryData()}
 * erreicht werden.
 * <br>
 * Werden durch Aufrufen der Methoden {@link #insert(String)}, {@link #delete(String)},
 * und/oder {@link #update(String, String)} Änderungen vorgenommen, so muss anschließend
 * {@link #apply()} aufgerufen werden um die Änderungen in der Datei zu persistieren.
 * 
 * @author Gruppe 8
 *
 */
public class KategorienListe {
	
	private ArrayList<String> categoryData;
	private boolean testMode; // true, falls getestet wird
	
    /**
     * Erzeugt eine Liste aller Kategorien, gefüllt mit den Einträgen der persistierten Daten.
     * <p>
     * Aus der Textdatei <code>src/data/categories.txt</code> werden die Kategorien herausgelesen.
     * Wenn die Datei nicht vorhanden ist, oder nicht gelesen werden kann, gibt
     * {@link #getCategoryData()} möglicherweise eine leere oder unvollständige Liste
     * an Kategorien zurück. 
     */
	public KategorienListe(boolean testMode) {
		this.categoryData = new ArrayList<String>();
		this.testMode = testMode;
        this.initialize(testMode);
    }
	
	/**
	 * Liest die gespeicherten Kategorien aus der Datei aus
	 * <p>
	 * Die Datei muss im Verzeichnis <code>src/data/categories.txt</code> liegen und
     * korrekt benannt sein. Jede Zeile der Datei entspricht einer Kategorie.
	 */
	private void initialize(boolean testMode) {
        
        String line;
        
        if (testMode) {
        	
        	try (BufferedReader reader = new BufferedReader(new FileReader("src/data/test_categories.txt"))) {
        		
	            while ((line = reader.readLine()) != null) {
	
	                this.insert(line);
	            }
	        } 
	        
	        catch (FileNotFoundException ex) {
	            System.out.println("Warning: No data in src/data/test_categories.txt");
	        } 
	        
	        catch (IOException ex) {
	            ex.printStackTrace();
	        }
        	
        } 
        else {
        	
        	try (BufferedReader reader = new BufferedReader(new FileReader("src/data/categories.txt"))) {
        		
	            while ((line = reader.readLine()) != null) {
	
	                this.insert(line);
	            }
	        } 
	        
	        catch (FileNotFoundException ex) {
	            System.out.println("Warning: No data in src/data/categories.txt");
	        } 
	        
	        catch (IOException ex) {
	            ex.printStackTrace();
	        }
        } 
    }

    /**
     * Gibt eine Liste mit allen Kategorien zurück.
     * @return Liste aller Kategorien
     */
    public ArrayList<String> getCategoryData() {
        return this.categoryData;
    }
	
    /**
     * Fügt eine Kategorie zur Liste hinzu.
     * @param k Kategorie die hinzugefügt werden soll
     */
	public void insert(String k) {
		categoryData.add(k);
	};
	
	/**
	 * Löscht eine Kategorie aus der Liste.
	 * @param k Kategorie, die gelöscht werden soll
	 */
	public void delete(String k) {
		categoryData.remove(k);
	};
	
    /**
     * Löscht alle Artikel aus der Liste und der zugehörigen Datenbank.
     */
    public void removeAll() {
    	this.categoryData = new ArrayList<String>();
    	apply();
    }
	
	/**
	 * Tauscht die eine gegen eine andere Kategorie aus.
	 * @param old 	Kategorie die entfernt wird
	 * @param k		Kategorie die hinzugefügt wird
	 */
	public void update(String old, String k) {
		categoryData.remove(old);
		categoryData.add(k);
	};
	
	/**
	 * Speichert die Liste der Kategorien ab.
     * <p>
     * Die Datei wird im Verzeichnis <code>src/data/categories.txt</code> mit entsprechendem
     * Namen angelegt oder überschrieben. Jede Kategorie wird in eine neue Zeile geschrieben.
	 */
	public void apply() {
		
		if (testMode) {
			
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/test_categories.txt"))) {
	            
	            for (String k: categoryData) {
	                writer.write(k + "\n");
	            }
	        }
	        catch (IOException ex) {
	            ex.printStackTrace();
	        }
			
		}
		
		else {
			
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/categories.txt"))) {
	            
	            for (String k: categoryData) {
	                writer.write(k + "\n");
	            }
	        }
	        catch (IOException ex) {
	            ex.printStackTrace();
	        }
			
		}
		
	}
    
	// Gibt aus, ob das System sich im Testmodus befindet
	public boolean getTestMode() {
    	return this.testMode;
    }

}
