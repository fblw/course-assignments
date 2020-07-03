package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import controller.CategoryController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JComboBox;

/**
 * Fenster zum löschen von Kategorien, wird durch den {@link CategoryController} gesteuert.
 * <p>
 * Das Fenster verfügt über ein Auswahlfeld zum wählen der Kategorie, sowie
 * über einen Bestätigungs- und einen Zurückbutton.
 * 
 * @author Gruppe 8
 *
 */
@SuppressWarnings("serial")
public class DeleteCategoryView extends JFrame {

	private JPanel contentPane;
	private JButton btnSubmit;
	private JComboBox deleteComboBox;
	private String selectedCategory;

	/**
	 * Mit Aufruf des Konstruktors wird das Fenster erzeugt. 
	 * 
	 */
	public DeleteCategoryView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout springPane = new SpringLayout();
		contentPane.setLayout(springPane);

		JButton btnCancle = new JButton("Zurück");
		springPane.putConstraint(SpringLayout.NORTH, btnCancle, 10, SpringLayout.NORTH, contentPane);
		springPane.putConstraint(SpringLayout.WEST, btnCancle, 10, SpringLayout.WEST, contentPane);
		contentPane.add(btnCancle);
		btnCancle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DeleteCategoryView.this.dispose();
			}
			
		});
		
		JLabel lblKategorie = new JLabel("Bezeichnung der zu löschenden Kategorie:");
		springPane.putConstraint(SpringLayout.NORTH, lblKategorie, 6, SpringLayout.SOUTH, btnCancle);
		springPane.putConstraint(SpringLayout.WEST, lblKategorie, 10, SpringLayout.WEST, btnCancle);
		contentPane.add(lblKategorie);
		
		btnSubmit = new JButton("Bestätigen");
		springPane.putConstraint(SpringLayout.SOUTH, btnSubmit, -10, SpringLayout.SOUTH, contentPane);
		springPane.putConstraint(SpringLayout.EAST, btnSubmit, -10, SpringLayout.EAST, contentPane);
		contentPane.add(btnSubmit);
		
		deleteComboBox = new JComboBox();
		springPane.putConstraint(SpringLayout.NORTH, deleteComboBox, -4, SpringLayout.NORTH, lblKategorie);
		springPane.putConstraint(SpringLayout.WEST, deleteComboBox, 6, SpringLayout.EAST, lblKategorie);
		contentPane.add(deleteComboBox);
		deleteComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				selectedCategory = e.getItem().toString();
			}	
		});
	}
	
	/**
	 * Gibt die ausgewählte Kategorie zurück.
	 * @return String gewählte Kategorie
	 */
	public String getSelectedCategory() {
		return selectedCategory;
	}

	/**
	 * Gibt die Kategorieauswahlbox zurück.
	 * @return Kategorieauswahlbox, Referenz auf das UI-Element
	 * @see JComboBox
	 */
	public JComboBox getDeleteComboBox() {
		return this.deleteComboBox;
	}
	
	/**
	 * Fügt den Listener zum Submitbutton hinzu.
	 * @param listenForBtnSubmit Listener, der beim klicken des Buttons aufgerufen wird
	 */
	public void addSubmitListener(ActionListener listenForBtnSubmit) {
        btnSubmit.addActionListener(listenForBtnSubmit);
    }
}
