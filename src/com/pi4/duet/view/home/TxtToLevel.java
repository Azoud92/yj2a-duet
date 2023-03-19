package com.pi4.duet.view.home;

import java.awt.*;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.pi4.duet.model.game.PatternData;

/*
 * Pour l'instant, cette classe est écrite pour pouvoir être éxécutée pour afficher une interface graphique permettant de pointer
 * un fichier texte et une destination où placer un fichier .ser en sortie. Le but à l'avenir est de pouvoir l'implémenter à l'interface
 * graphique du jeu lui-même.
 */
public class TxtToLevel extends JPanel {

	private static final long serialVersionUID = -2274391003013979174L;
	
	private final JTextField txtFileInputField = new JTextField("LOREM IPSUM", 100);
	private final JTextField serFileOutputField = new JTextField("DOLOR SIT AMET", 100);
	
	private final JButton selectTxtSource = new JButton("Parcourir...");
	private final JButton selectSerDestination = new JButton("Parcourir...");
	private final JButton parseButton = new JButton("Parse");
	
	public TxtToLevel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		selectTxtSource.addActionListener((ev) -> {
			txtFileInputField.setText(selectPath());
		});
		
		selectSerDestination.addActionListener((ev) -> {
			serFileOutputField.setText(selectPath());
		});
		
		parseButton.addActionListener((ev) -> {
			String sourcePath = txtFileInputField.getText();
			String destPath = serFileOutputField.getText();
			JButton okButton = new JButton("OK");
			
			JFrame dialogBox = new JFrame("Conversion...");
			JPanel contentPane = (JPanel) dialogBox.getContentPane();
			
			okButton.addActionListener((e) -> {
				dialogBox.dispatchEvent(new WindowEvent(dialogBox, WindowEvent.WINDOW_CLOSING));
			}); 
			
			contentPane.setLayout(new GridLayout(3, 1));
			contentPane.add(new JLabel("Conversion de " + sourcePath + " vers " + destPath + "..."));
			dialogBox.setSize(600, 200);
			dialogBox.setVisible(true);
			try {
				PatternData writtenPattern = PatternData.readTxt(sourcePath);
				writtenPattern.write(destPath);
				contentPane.add(new JLabel("<html>Converti : <br>" + writtenPattern.toHTMLString() + "</html>"));
				contentPane.add(okButton);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
				dialogBox.dispatchEvent(new WindowEvent(dialogBox, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		add(new JLabel("Parseur de niveaux"), c);
		
		c.gridy = 1;
		c.gridwidth = 1;
		add(new JLabel("Fichier source : "), c);
		
		c.gridx = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		add(txtFileInputField, c);
		
		c.gridx = 2;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		add(selectTxtSource, c);
		
		c.gridy = 2;
		c.gridx = 0;
		add(new JLabel("Destination : "), c);
		
		c.gridx = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		add(serFileOutputField, c);
		
		c.gridx = 2;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		add(selectSerDestination, c);
		
		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.BOTH;
		add(parseButton, c);
	}
	
	private String selectPath() {
		JFileChooser explorer = new JFileChooser();
		if (explorer.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) return explorer.getSelectedFile().getAbsolutePath();
		return null;
	}
	
	public static void main(String[] args) {
		JFrame main = new JFrame("Level parser");
		TxtToLevel panel = new TxtToLevel();
		
		main.getContentPane().add(panel);
		main.setSize(500, 500);
		main.setVisible(true);
	}
	
	
}
