package model;

/**
 * Repräsentiert einen Artikel des Lagers.
 * <p>
 * Diese Klasse ist eine Abstraktion eines realen Artikel und stellt
 * alle notwendigen Attribute inklusive Get- und Set-Methoden bereit.
 * 
 * @author Gruppe 8
 *
 */
public class Artikel {
    private String platznummer;
    private String name;
    private String kategorie;
    private int anzahl;
    private double gewicht;
    private double preis;
    
    /**
     * Erzeugt einen Artikel mit den gegebenen Attributen.
     * 
     * 
     * @param platznummer 	String aus sechs Ziffern, die ersten drei bestimmen das Regal,
     * 						die letzten drei den Platz im Regal
     * @param name			Artikelname
     * @param kategorie		Kategorie des Artikels
     * @param anzahl		Menge vorhandener Artikel
     * @param gewicht		Gewicht des Artikels in Gramm
     * @param preis			Preis des Artikels in Euro
     */
    public Artikel(String platznummer, String name, String kategorie, int anzahl, double gewicht, double preis) {
        this.platznummer = platznummer;
        this.name = name;
        this.kategorie = kategorie;
        this.anzahl = anzahl;
        this.gewicht = gewicht;
        this.preis = preis;   
    }

    /**
     * Gibt die Platznummer zurück.
     * @return String aus sechs Ziffern
     */
    public String getPlatznummer() {
        return this.platznummer;
    }

    /**
     * Gibt den Artikelnamen zurück 
     * @return Artikelname
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gibt die Kategorie des Artikels zurück.
     * @return Kategorie
     */
    public String getKategorie() {
        return this.kategorie;
    }

    /**
     * Gibt zurück, wie oft der Artikel im Lager vorhanden ist.
     * @return Anzahl
     */
    public int getAnzahl() {
        return this.anzahl;
    }

    /**
     * Gibt das Artikelgewicht zurück.
     * @return Gewicht in Gramm
     */
    public double getGewicht() {
        return this.gewicht;
    }

    /**
     * Gibt den Preis des Artikel zurück.
     * @return Preis in Euro
     */
    public double getPreis() {
        return this.preis;
    }
    
    /**
     * Legt die Platznummer des Artikels fest.
     * <p>
     * Die Platznummer soll aus sechs Ziffern bestehen, die ersten drei
     * bestimmen das Regal, die letzten drei den Platz im Regal
     * 
     * 
     * @param platznummer String aus 6 Ziffern
     */
	public void setPlatznummer(String platznummer) {
        this.platznummer = platznummer;
    }
	
	/**
	 * Setzt den Namen des Artikels.
	 * @param name Artikelname
	 */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Legt die Kategorie des Artikels fest.
     * @param kategorie Artikelkategorie
     */
    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }
    
    /**
     * Setzt, wie oft der Artikel im Lager vorhanden ist.
     * @param anzahl Artikelanzahl
     */
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }
    
    /**
     * Legt das Gewicht des Artikels fest.
     * @param gewicht Artikelgewicht in Gramm.
     */
    public void setGewicht(double gewicht) {
        this.gewicht = gewicht;
    }
    
    /**
     * Legt den Preis des Artikels fest.
     * @param preis Artikelpreis in Euro
     */
    public void setPreis(double preis) {
        this.preis = preis;
    }
    
    /**
     * Konvertiert den Artikel zu einem String.
     * <p>
     * Der String enthält in dieser Reihenfolge die Attribute
     * <code>platznummer, name, kategorie, anzahl, gewicht, preis</code>
     * getrennt durch einen Tabulator.
     */
    @Override
    public String toString() {
        return (
            this.platznummer + "\t" 
            + this.name + "\t"
            + this.kategorie + "\t"
            + this.anzahl + "\t"
            + this.gewicht + "\t"
            + this.preis
            );
    }

    /**
     * Entscheidet, ob ein Artikel gleich einem anderen Objekt ist.
     * <p>
     * Als gleich werden Artikel betrachtet, deren Platznummer übereinstimmt.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Artikel) {
            Artikel other = (Artikel) object;

            return (
                this.platznummer.equals(other.platznummer) ||
                this.name.equals(other.name)
                );
        }
        return false;
    }

}
