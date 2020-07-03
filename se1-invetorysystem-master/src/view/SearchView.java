package view;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.SearchController;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

/**
 * Fenster, das Suchschaltflächen bereitstell, gesteuert durch den {@link SearchController}.
 * <p>
 * Das Fenster enthält neben Textfeld für den Suchbegriff auch Checkboxen für die Attribute,
 * in denen gesucht werden kann, das umfasst:
 * <ul>
 * <li>Artikelname</li>
 * <li>Kategorie</li>
 * <li>Gewicht</li>
 * <li>Platznummer</li>
 * <li>Anzahl</li>
 * <li>Preis</li>
 * </ul>
 * <br>
 * Außerdem sind ein 'Suche starten'- und ein 'Abbrechen'-Button vorhanden.
 * 
 *  
 * @author Gruppe 8
 *
 */
@SuppressWarnings("serial")
public class SearchView extends JFrame {

    JCheckBox name = new JCheckBox("Artikelname"), location = new JCheckBox("Platznummer"), category = new JCheckBox("Kategorie"), count = new JCheckBox("Anzahl"), weight = new JCheckBox("Gewicht"), price = new JCheckBox("Preis"); 
    JTextField inputField = new JTextField(10);
    JButton submit = new JButton("Suche starten"), cancel = new JButton("Abbrechen");
    JPanel head = new JPanel(new FlowLayout(FlowLayout.CENTER)), searchOptions = new JPanel(new GridLayout(3,3)), footer = new JPanel(new FlowLayout(FlowLayout.CENTER));

	/**
	 * Mit Aufruf des Konstruktors wird das Fenster erzeugt. 
	 * 
	 */
    public SearchView() {
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(300,300);
		setLayout(new BorderLayout());
        
        head.add(new JLabel("Suchen nach : "));
        head.add(inputField);
        add("North", head);

        name.setSelected(true);        
        searchOptions.add(name);
        searchOptions.add(location);
        searchOptions.add(category);
        searchOptions.add(count);
        searchOptions.add(weight);
        searchOptions.add(price);
        add("Center", searchOptions);
        

        footer.add(submit);
        footer.add(cancel);
        add("South", footer);
    }

	/**
	 * Gibt den eingegebenen Suchstring zurück.
	 * @return Suchstring
	 */
    public String getInputFieldText() {
        return inputField.getText();
    }
    
	/**
	 * Legt einen neuen Suchstring fest.
	 * @param String t - Suchstring
	 * @return void
	 */
    public void setInputFieldText(String t) {
    	inputField.setText(t);
    }

    /**
     * Gibt ein die gewählten Suchattribute zurück.
     * <p>
     * Die Elemente sind in folgender Reihenfolge angeordnet:
     * <br>
     * <code>[Artikelname, Regalplatz, Kategorie, Anzahl, Gewicht, Preis]</code>
     * 
     * 
     * @return Array aus Booleans
     */
    public boolean[] getSelectedOptions() {
        boolean[] selectedOptions = {
            name.isSelected(),
            location.isSelected(),
            category.isSelected(),
            count.isSelected(),
            weight.isSelected(),
            price.isSelected()
        };
        return selectedOptions;
    }
    
    /**
	 * Hilfsfunktion. Wählt alle Attribute zur "Allgemeinen Suche" aus.
	 * @return void
	 */
    public void setGeneralSearchOptions() {
    	name.setSelected(true);
        location.setSelected(true);
        category.setSelected(true);
        count.setSelected(true);
        weight.setSelected(true);
        price.setSelected(true);
    }
    
    /**
	 * Gibt den submit Button für Validierungstests zurück.
	 * @return JButton submit
	 */
	public JButton getSubmitButton() {
		return submit;
	}

    /**
	 * Gibt den 'Abbrechen'-Button zurück.
	 * @return Referenz auf das UI-Element
	 */
    public JButton getCancelButton() {
        return cancel;
    }

    /**
     * Fügt einen Listener zum 'Suche starten'-Button hinzu.
     * @param listenForButtonSubmit Listener, der beim klicken des Buttons aufgerufen wird
     */
    public void addSubmitListener(ActionListener listenForButtonSubmit) {
        submit.addActionListener(listenForButtonSubmit);
    }
}