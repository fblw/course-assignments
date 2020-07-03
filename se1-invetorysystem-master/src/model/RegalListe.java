package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Hält eine Liste der Regale vor und persistiert diese in einer Datei.
 * <p>
 * Mit dem Aufruf des Konstruktors {@link #RegalListe()} werden die gespeicherten
 * Regaldaten aus der Datei <code>src/data/shelves.txt</code> gelesen und
 * in zu einer HashMap hinzugefügt, auf die mit {@link #getShelfData()}
 * zugegriffen werden kann.
 * <br>
 * Werden durch Aufrufen der Methoden {@link #insert(String, Double)}, {@link #delete(String)},
 * {@link #addWeight(String, Double)} und/oder {@link #reduceWeight(String, Double)} Änderungen
 * vorgenommen, so muss anschließend {@link #apply()} aufgerufen werden um die Änderungen in der
 * Datei zu persistieren.
 * <p>
 * Ein "Regal" besteht aus zwei Werten: Einer Regalnummer und einem Gewicht. Die Regalnummer wird
 * durch einen aus drei Ziffern bestehenden String modelliert, diese ist gleichzeitig Key der HashMap.
 * Das Gewicht ist das Gewicht alle Artikel in diesem Regal, Einheit ist Gramm.
 * 
 * 
 * @author Gruppe 8
 *
 */
public class RegalListe {
    
    private HashMap<String, Double> shelfData;
    private boolean testMode; // true, falls getestet wird

    /**
     * Erzeugt eine Regalliste, gefüllt mit den Einträgen der persistierten Daten.
     * <p>
     * Die Textdatei <code>src/data/shelves.txt</code> wird eingelesen und zu
     * {@link Artikel}-Objekten geparst. Wenn die Datei nicht vorhanden ist, oder nicht
     * gelesen werden kann, gibt {@link #getShelfData()} möglicherweise eine leere
     * oder unvollständige HashMap zurück. 
     */
    public RegalListe(boolean testMode) {
        this.shelfData = new HashMap<String, Double>();
        this.testMode = testMode;
        this.initialize(testMode);
    }
    
    /**
     * Parst die Datei mit den gespeicherten Regaldaten
     * <p>
     * Die Datei muss im Verzeichnis <code>src/data/shelves.txt</code> liegen und
     * korrekt benannt sein. Jede Zeile der Datei entspricht einem Regal. Jede Zeile
     * enthält die Regalnummer (3 Ziffern) gefolgt von einem Tabulator und dem Gewicht,
     * mit dem das Regal belastet wird.
     */
    private void initialize(boolean testMode) {
        
        String line;
        
        if (testMode) {
        	
        	try (BufferedReader reader = new BufferedReader(new FileReader("src/data/test_shelves.txt"))) {

                while ((line = reader.readLine()) != null) {
                    
                    // Spalte jede Zeil in Tokens (Strings) auf
                    String[] tokens = line.split("\t");
                    
                    // Füge jedes Regal zu shelfData hinzu
                    this.insert(tokens[0], Double.valueOf(tokens[1]));
                }
            } 
            
            catch (FileNotFoundException ex) {
                System.out.println("Warning: No data in src/data/test_shelves.txt");
            } 
            
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        else {
        	
        	try (BufferedReader reader = new BufferedReader(new FileReader("src/data/shelves.txt"))) {

                while ((line = reader.readLine()) != null) {
                    
                    // Spalte jede Zeil in Tokens (Strings) auf
                    String[] tokens = line.split("\t");
                    
                    // Füge jedes Regal zu shelfData hinzu
                    this.insert(tokens[0], Double.valueOf(tokens[1]));
                }
            } 
            
            catch (FileNotFoundException ex) {
                System.out.println("Warning: No data in src/data/shelves.txt");
            } 
            
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Gibt die Regal-{@link HashMap} zurück.
     * @return HashMap mit der Zuordnung Regalnummer=>Gesamtgewicht aller in diesem
     * Regal gelagerten Artikel
     */
    public HashMap<String, Double> getShelfData() {
        return this.shelfData;
    }
    
    /**
     * Fügt ein neues Regal hinzu.
     * @param id		Regalnummer (3 Ziffern)
     * @param weight	Gesamtgewicht der in diesem Regal eingelagerten Artikel
     */
    public void insert(String id, Double weight) {
        this.shelfData.put(id, weight);
    }

    /**
     * Entfernt ein Regal.
     * @param id Regalnummer (3 Ziffern)
     */
    public void delete(String id) {
        this.shelfData.remove(id);
    }
    
    /**
     * Löscht alle Regale aus der Liste und der zugehörigen Datenbank.
     */
    public void removeAll() {
    	this.shelfData = new HashMap<String, Double>();
    	apply();
    }

    /**
     * Erhöht das Gewicht eines Regals um <code>weight</code>
     * <p>
     * Falls das Regal bereits in der {@link HashMap} existiert, wird das
     * <code>weight</code> hinzuaddiert, sonst wird ein neues Regal mit
     * dem Gesamtgewicht <code>weight</code> angelegt.
     * 
     * 
     * @param id		Regalnummer (3 Ziffern)
     * @param weight	Gewicht, um das erhöht werden soll (Gramm)
     */
    public void addWeight(String id, Double weight) {
        if (!this.shelfData.containsKey(id)) this.insert(id,weight);
        else this.shelfData.put(id, this.shelfData.get(id) + weight);
    }

    /**
     * Verringert das Gewicht eines Regals um <code>weight</code>
     * <p>
     * <code>weight</code> wird vom Gewicht des Regals abgezogen, wenn dabei
     * das neue Gesamtgewicht auf oder unter Null sinkt, wird das Regal gelöscht.
     * 
     * 
     * @param id		Regalnummer (3 Ziffern)
     * @param weight	Gewicht, das abgezogen werden soll (Gramm)
     */
    public void reduceWeight(String id, Double weight) {
        if (this.shelfData.get(id) - weight <= 0.0) delete(id);
        else this.shelfData.put(id, this.shelfData.get(id) - weight);
    }

    /**
     * Speichert die Regalliste ab.
     * <p>
     * Die Datei wird im Verzeichnis <code>src/data/shelves.txt</code> mit entsprechendem
     * Namen angelegt oder überschrieben. Jeder Eintrag der {@link HashMap} wird in eine 
     * neue Zeile geschrieben: auf die Regalnummer (3 Ziffern) folgt ein Tabulator und
     * das entsprechende Gewicht.
     */
    public void apply() {
        
    	if (testMode) {
    		 
    		try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/test_shelves.txt"))) {
                 
    	            for (String s: shelfData.keySet()) {
    	                writer.write(s.toString() + "\t" + shelfData.get(s).toString() + "\n");
    	            }
    	        }
    	        catch (IOException ex) {
    	            ex.printStackTrace();
    	        }
    	}
    	
    	else {
    		
    		try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/shelves.txt"))) {
    			
    			for (String s: shelfData.keySet()) {
    	                writer.write(s.toString() + "\t" + shelfData.get(s).toString() + "\n");
    	            }
    	        }
    	        catch (IOException ex) {
    	            ex.printStackTrace();
    	        }	
    	}
    }
}
