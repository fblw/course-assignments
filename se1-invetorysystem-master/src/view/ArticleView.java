package view;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

import controller.ArticleController;
import controller.CategoryController;
import model.Artikel;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.JLabel;

/**
 * Fenster eines {@linkplain Artikel Artikels} zum bearbeiten und neu anlegen,
 * gesteuert durch den {@link ArticleController}. 
 * <p>
 * Das Fenster verfügt über Textfelder für die Attribute eines Artikels (inklusive
 * Eingabehinweisen), sowie eine Auswahlmöglichkeit für die Kategorie. 
 * Desweiteren über einen Submitbutton und einen Button, mit dem eine neue 
 * Kategorie angelegt werden kann.
 *  
 * 
 * @author Gruppe 8
 *
 */
@SuppressWarnings("serial")
public class ArticleView extends JFrame {

	private JTextField name = new JTextField("");
	private JFormattedTextField location = new JFormattedTextField(new DecimalFormat("000000")), count = new JFormattedTextField(new DecimalFormat("0")), weight = new JFormattedTextField(new DecimalFormat("0.######")), price = new JFormattedTextField(new DecimalFormat("0.00"));
	private JButton submit = new JButton("Bestätigen"), newCategory = new JButton("Neue Kategorie hinzufügen");
	private String comboBoxListe[] = { "Kategorie wählen" }, selectedCategory;
	private JComboBox<String> category = new JComboBox<String>(comboBoxListe);
	private JLabel locationInfo = new JLabel("Sechstellige Zahlenfolge (0-9)*"), countInfo = new JLabel("Ganzzahl*"), weightInfo = new JLabel("Dezimalzahl (Minimum=0.1)*"), priceInfo = new JLabel("Dezimalzahl (Minimum=0.01)*");

	/**
	 * Mit Aufruf des Konstruktors wird das Fenster erzeugt. 
	 * 
	 */
	public ArticleView() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(900, 200);
		setResizable(false);
		setLayout(new GridLayout(7, 3));

		add(new JLabel("Bezeichnung : "));
		add(name);
		add(new JLabel());
		add(new JLabel("Kategorie : "));
		add(category);
		
		category.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				selectedCategory = e.getItem().toString();
			}
		});

		add(newCategory);
		add(new JLabel("Platznummer : "));
		add(location);
		add(locationInfo);

		location.addPropertyChangeListener("value", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent e) {
			
				if (e.getNewValue() != null) {
					int val = ((Number)location.getValue()).intValue();
					if (val < 0) {
						location.setValue(e.getOldValue());
					}
				}
			}
		});

		add(new JLabel("Anzahl : "));
		add(count);
		add(countInfo);
		
		count.addPropertyChangeListener("value", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent e) {
			
				if (e.getNewValue() != null) {
					int val = ((Number)count.getValue()).intValue();
					if (val < 0) {
						count.setValue(e.getOldValue());
					}
				}
			}
		});

		add(new JLabel("Gewicht in g : "));
		add(weight);
		add(weightInfo);

		weight.addPropertyChangeListener("value", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent e) {
			
				if (e.getNewValue() != null) {
					Double val = ((Number)weight.getValue()).doubleValue();
					if (val < 0.1) {
						weight.setValue(e.getOldValue());
					}
				}
			}
			
		});

		add(new JLabel("Preis in €/Stück : "));
		add(price);
		add(priceInfo);

		price.addPropertyChangeListener("value", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent e) {
			
				if (e.getNewValue() != null) {
					Double val = ((Number)price.getValue()).doubleValue();
					if (val < 0.01) {
						price.setValue(e.getOldValue());
					}
				}
			}
			
		});

		add(submit);
		add(new JLabel());
		add(new JLabel("*nicht negativ"));

	}
	
	/**
	 * Gibt den eingegebenen Regalplatz zurück.
	 * @return String Regalplatz
	 */
	public String getTxtPlatz() {
        return this.location.getText();
	}
	
	/**
	 * Schreibt den Regalplatz ins entsprechende Textfeld.
	 * @param platz Regalplatz, soll aus sechs Ziffern bestehen 
	 */
	public void setTxtPlatz(String platz) {
        this.location.setText(platz);
    }
	
	/**
	 * Legt den formatierten Regalplatz fest
	 * @param platz<int>
	 */
	public void setValuePlatz(int platz) {
		this.location.setValue(platz);
	}

	/**
	 * Gibt den eingegebenen Artikelnamen zurück.
	 * @return String Artikelname
	 */
    public String getTxtName() {
        return this.name.getText();
	}
	
    /**
	 * Schreibt den Artikelnamen ins entsprechende Textfeld.
	 * @param name Artikelname 
	 */
	public void setTxtName(String name) {
        this.name.setText(name);
	}
	
	/**
	 * Gibt die Kategorieauswahlbox zurück.
	 * @return Kategorieauswahlbox, Referenz auf das UI-Element
	 * @see JComboBox
	 */
	public JComboBox<String> getCbxKategorie() {
		return this.category;
	}
	
	/**
	 * Selektiert die entsprechende Kategorie in der Kategorieauswahlbox
	 * @param kategorie eine Kategorie
	 */
	public void setCbxKategorie(String kategorie) {
		this.category.setSelectedItem(kategorie);
	}

	/**
	 * Gibt die eingegebene Anzahl zurück.
	 * @return String Anzahl, möglicherweise keine Ganzzahl
	 */
    public String getTxtAnzahl() {
        return count.getText();
	}
	
    /**
	 * Schreibt die Anzahl ins entsprechende Textfeld.
	 * @param anzahl String, soll eine Ganzzahl sein 
	 */
	public void setTxtAnzahl(String anzahl) {
		this.count.setText(anzahl);
	}
	
	/**
	 * Legt die formatierte Artikelanzahl fest
	 * @param anzahl<int>
	 */
	public void setValueAnzahl(int anzahl) {
		this.count.setValue(anzahl);
	}

	/**
	 * Gibt das eingegebene Gewicht zurück.
	 * @return String Gewicht, möglicherweise keine Dezimalzahl
	 */
    public String getTxtGewicht() {
        return weight.getText();
	}
	
    /**
     * Schreibt das Gewicht ins entsprechende Textfeld.
     * @param gewicht String, soll eine Dezimalzahl sein
     */
	public void setTxtGewicht(String gewicht) {
		this.weight.setText(gewicht);
	}
	
	/**
	 * Legt das formatierte Artikelgewicht fest
	 * @param gewicht<double>
	 */
	public void setValueGewicht(double gewicht) {
		this.weight.setValue(gewicht);
	}

	/**
	 * Gibt den eingegebenen Preis zurück.
	 * @return String Preis, möglicherweise keine Dezimalzahl
	 */
    public String getTxtPreis() {
        return this.price.getText();
	}
	
    /**
     * Schreibt den Preis ins entsprechende Textfeld.
     * @param preis String, soll eine Dezimalzahl sein
     */
	public void setTxtPreis(String preis) {
		this.price.setText(preis);
	}
	
	/**
	 * Legt den formatierten Artikelpreis fest
	 * @param preis<double>
	 */
	public void setValuePreis(double preis) {
		this.price.setValue(preis);
	}
	
	/**
	 * Gibt den submit Button für Validierungstests zurück.
	 * @return JButton submit
	 */
	public JButton getSubmitButton() {
		return submit;
	}
    
	/**
	 * Fügt den Listener zum Submitbutton hinzu.
	 * @param listenForButtonSubmit Listener, der beim klicken des Buttons aufgerufen wird
	 */
    public void addSubmitListener(ActionListener listenForButtonSubmit) {
        submit.addActionListener(listenForButtonSubmit);
	}

    /**
	 * Fügt den Listener zum 'Kategorie hinzufügen'-Button hinzu.
	 * @param listenForButtonNewCat Listener, der beim klicken des Buttons aufgerufen wird
	 * @see CategoryController
	 */
	public void addNewCategoryListener(ActionListener listenForButtonNewCat) {
        newCategory.addActionListener(listenForButtonNewCat);
	}

	/**
	 * Gibt die ausgewählte Kategorie zurück.
	 * @return String gewählte Kategorie
	 */
	public String getSelectedCategory() {
		return selectedCategory;
	}
}

