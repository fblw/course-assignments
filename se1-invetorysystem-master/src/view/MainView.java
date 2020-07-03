package view;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.JButton;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import controller.MainController;
import model.Artikel;
import model.KategorienListe;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Hauptfenster, das die Artikelübersicht anzeigt, wird durch den {@link MainController} gesteuert.
 * <p>
 * Dieses Fenster wird beim Starten der Applikation zuerst dargestellt. Es enthält neben einer Tabelle,
 * die die {@link Artikel} mit ihren Attributen darstellt auch Buttons, die weitere Fenster öffnen.
 * <p>
 * Die Tabelle lässt sich nach allen Attributen sowohl aufsteigend, als auch absteigend sortieren. Nach
 * einer Suche enthält sie nur jene Artikel, die Ergebnis der Suche sind, sonst enthält sie alle Artikel.
 * <p>
 * Die Buttons umfassen:
 * <ul>
 * <li><i>Artikel hinzufügen</i><br>
 * öffnet ein {@link ArticleView} zum hinzufügen eines Artikels</li>
 * <li><i>Artikel bearbeiten</i><br>
 * öffnet ein {@link ArticleView} zum bearbeiten des in der Tabelle ausgewählten Artikels</li>
 * <li><i>Artikel löschen</i><br>
 * löscht den in der Tabelle ausgewählten Artikel</li>
 * <li><i>Kategorie hinzufügen</i><br>
 * öffnet ein {@link AddCategoryView} zum hinzufügen einer Kategorie</li>
 * <li><i>Kategorie entfernen</i><br>
 * öffnet ein {@link DeleteCategoryView} zum entfernen einer Kategorie</li>
 * <li><i>Suche</i><br>
 * öffnet ein {@link AddCategoryView} zum spezifizieren einer Suche</li>
 * <li><i>Reset</i><br>
 * setzt die Suche zurück, dh. zeigt wieder alle Artikel in der Tabelle an</li>
 * </ul>
 * 
 * 
 * @author Gruppe 8
 *
 */
@SuppressWarnings("serial")
public class MainView extends JFrame{
	
	// Center
	private String[] columnNames = { "Platznummer", "Produktbezeichnung", "Kategorie", "Anzahl", "Gewicht (g/Stk.)", "Preis (€/Stk.)" };
	private DefaultTableModel tableModel;
	private JTable table;
	private JScrollPane scroll;
	private TableRowSorter<DefaultTableModel> sorter;
	private List<RowSorter.SortKey> sortKeys;
	
	// South
	private JPanel buttonPanel = new JPanel();
	private JButton addArticle = new JButton("Artikel hinzufügen"), editArticle = new JButton("Artikel bearbeiten"), delArticle = new JButton("Artikel löschen"), addCategory = new JButton("Kategorie hinzufügen"), delCategory = new JButton("Kategorie entfernen");
	
	// North
	private JPanel searchPanel = new JPanel(), container = new JPanel(new FlowLayout());
	private JButton search = new JButton("Suche"), reset = new JButton("Reset");

	/**
	 * Mit Aufruf des Konstruktors wird das Fenster erzeugt. 
	 * 
	 */
	public MainView(){

		this.setTitle("Lagerverwaltungssystem");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000,300);
		setLayout(new BorderLayout());
		
		// initialisiere Tabelle:
		tableModel = new DefaultTableModel(columnNames,0) {
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    case 3:
                    	return Integer.class;
                    case 4:
                    	return Double.class;
                    case 5:
                    	return Double.class;
                    default:
                        return String.class;
                }
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
            	return false;	//Immer false im editieren der Tabelle zu verhindern
            }
        };
		
    	table = new JTable(tableModel);
    	scroll = new JScrollPane(table);
    	sorter = new TableRowSorter<DefaultTableModel>(tableModel);
    	sortKeys = new ArrayList<RowSorter.SortKey>();
		table.setAutoCreateRowSorter(true);
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		table.setRowSorter(sorter);
		add("Center", scroll);

		buttonPanel.setLayout(new GridLayout(1,7));
		buttonPanel.add(addArticle);
		buttonPanel.add(editArticle);
		buttonPanel.add(delArticle);
		buttonPanel.add(addCategory);
		buttonPanel.add(delCategory);
		add("South", buttonPanel);

		container.add(search);
		container.add(reset);
		searchPanel.add(container, BorderLayout.SOUTH);
		add("North", searchPanel);
	}

	/**
	 * Gibt das {@link DefaultTableModel} der Artikeltabelle zurück.
	 * @return Modell der Artikeltabelle
	 */
	public DefaultTableModel getDefaultTableModel() {
		return tableModel;
	}
	
	/**
	 * Gibt die Artikeltabelle zurück.
	 * @return Referenz auf das UI-Element
	 */
	public JTable getArticleTable() {
		return table;
	}

	/**
	 * Gibt den 'Artikel löschen'-Button zurück. 
	 * @return Referenz auf das UI-Element
	 */
	public JButton getDeleteArticleButton() {
		return delArticle;
	}

	/**
	 * Gibt den 'Artikel bearbeiten'-Button zurück. 
	 * @return Referenz auf das UI-Element
	 */
	public JButton getEditArticleButton() {
		return editArticle;
	}

	/**
	 * Gibt den 'Kategorie entfernen'-Button zurück.
	 * @return Referenz auf das UI-Element
	 */
	public JButton getDeleteCategoryButton() {
		return delCategory;
	}

	/**
	 * Fügt einen Listener für das Selektieren eines Artikels aus der Tabelle hinzu.
	 * @param listenForSelection Listener, der beim Selektieren aufgerufen wird
	 */
	public void addSelectArticleListener(ListSelectionListener listenForSelection) {
		table.getSelectionModel().addListSelectionListener(listenForSelection);;
	}
	
	/**
	 * Fügt einen Listener zum 'Artikel hinzufügen'-Button hinzu.
	 * @param listenForButtonAddArt Listener, der beim klicken des Buttons aufgerufen wird
	 */
	public void addNewArticleListener(ActionListener listenForButtonAddArt) {
        addArticle.addActionListener(listenForButtonAddArt);
	}

	/**
	 * Fügt einen Listener zum 'Artikel bearbeiten'-Button hinzu.
	 * @param listenForButtonEditArt Listener, der beim klicken des Buttons aufgerufen wird
	 */
	public void addEditArticleListener(ActionListener listenForButtonEditArt) {
		editArticle.addActionListener(listenForButtonEditArt);
	}

	/**
	 * Fügt einen Listener zum 'Artikel löschen'-Button hinzu.
	 * @param listenForButtonDelArt Listener, der beim klicken des Buttons aufgerufen wird
	 */
	public void addDeleteArticleListener(ActionListener listenForButtonDelArt) {
		delArticle.addActionListener(listenForButtonDelArt);
	}

	/**
	 * Fügt einen Listener zum 'Kategorie hinzufügen'-Button hinzu.
	 * @param listenForButtonAddCat Listener, der beim klicken des Buttons aufgerufen wird
	 */
	public void addNewCategoryListener(ActionListener listenForButtonAddCat) {
        addCategory.addActionListener(listenForButtonAddCat);
	}

	/**
	 * Fügt einen Listener zum 'Kategorie entfernen'-Button hinzu.
	 * @param listenForButtonDelCat Listener, der beim klicken des Buttons aufgerufen wird
	 */
	public void addDeleteCategoryListener(ActionListener listenForButtonDelCat) {
		delCategory.addActionListener(listenForButtonDelCat);
	}

	/**
	 * Fügt einen Listener zum 'Suchen'-Button hinzu.
	 * @param listenForButtonSearch Listener, der beim klicken des Buttons aufgerufen wird
	 */
	public void addSearchListener(ActionListener listenForButtonSearch) {
		search.addActionListener(listenForButtonSearch);
	}

	/**
	 * Fügt einen Listener zum 'Reset'-Button hinzu.
	 * @param listenForButtonReset Listener, der beim klicken des Buttons aufgerufen wird
	 */
	public void addResetListener(ActionListener listenForButtonReset) {
		reset.addActionListener(listenForButtonReset);
	}
	
	/**
	 * Entfernt alle Listener für den spezifizierten Button
	 * @param btn Button, von welchem die Listener entfernt werden sollen
	 * @see ActionListener
	 */
	public void removeAllButtonListeners(JButton btn) {
        for(ActionListener al: btn.getActionListeners()) {
            btn.removeActionListener(al);
        }
    }
}
