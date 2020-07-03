package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import controller.CategoryController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Fenster zum hinzufügen von Kategorien, wird durch den {@link CategoryController} gesteuert.
 * <p>
 * Das Fenster verfügt über ein Textfeld zum Eintragen des neuen Kategorienamens, sowie
 * über einen Bestätigungs- und einen Zurückbutton.
 * 
 * @author Gruppe 8
 *
 */
@SuppressWarnings("serial")
public class AddCategoryView extends JFrame {

	private JPanel contentPane;
	private JTextField txtKategorie;
	private JButton btnSubmit;

	/**
	 * Mit Aufruf des Konstruktors wird das Fenster erzeugt. 
	 * 
	 */
	public AddCategoryView() {
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
				AddCategoryView.this.dispose();
			}
			
		});
		
		JLabel lblKategorie = new JLabel("Bezeichnung der neuen Kategorie:");
		springPane.putConstraint(SpringLayout.NORTH, lblKategorie, 6, SpringLayout.SOUTH, btnCancle);
		springPane.putConstraint(SpringLayout.WEST, lblKategorie, 10, SpringLayout.WEST, btnCancle);
		contentPane.add(lblKategorie);
		
		txtKategorie = new JTextField();
		springPane.putConstraint(SpringLayout.NORTH, txtKategorie, -5, SpringLayout.NORTH, lblKategorie);
		springPane.putConstraint(SpringLayout.WEST, txtKategorie, 24, SpringLayout.EAST, lblKategorie);
		contentPane.add(txtKategorie);
		txtKategorie.setColumns(10);
		
		btnSubmit = new JButton("Bestätigen");
		springPane.putConstraint(SpringLayout.SOUTH, btnSubmit, -10, SpringLayout.SOUTH, contentPane);
		springPane.putConstraint(SpringLayout.EAST, btnSubmit, -10, SpringLayout.EAST, contentPane);
		contentPane.add(btnSubmit);
	}

	/**
	 * Gibt den Text des Kategorietextfeldes zurück.
	 * @return String
	 */
	public String getTxt(){
		return this.txtKategorie.getText();
	}
	
	/**
	 * Legt den Text des Kategorietextfeldes fest.
	 * @return void
	 */
	public void setTxt(String k) {
		this.txtKategorie.setText(k);
	}
	
	/**
	 * Gibt den submit Button für Validierungstests zurück.
	 * @return JButton submit
	 */
	public JButton getSubmitButton() {
		return btnSubmit;
	}
	
	/**
	 * Fügt den Listener zum Submitbutton hinzu.
	 * @param listenForButtonSubmit Listener, der beim klicken des Buttons aufgerufen wird
	 */
	public void addSubmitListener(ActionListener listenForButtonSubmit) {
        btnSubmit.addActionListener(listenForButtonSubmit);
    }

}
