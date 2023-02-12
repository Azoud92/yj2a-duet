package com.pi4.duet.view;

import java.awt.Dimension; 
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.pi4.duet.controller.GameController;
import com.pi4.duet.model.GamePlane;

public class GameWindow extends JFrame{

	private static final long serialVersionUID = 8560843128781641255L;
	
	private Dimension size;
	private double scaleX, scaleY; // représente le coeff. multiplicateur à appliquer selon la résolution de l'écran
	private JFrame frame;
	private JPanel container;
	
	private Image background = new ImageIcon(this.getClass().getResource("/background.png")).getImage();
	
	public GameWindow() {
		// Taille de l'écran en soustrayant celle de la barre des taches et du haut de la fenetre
		size = new Dimension();
		size.width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		size.height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration()).bottom - 15;

		scaleX = (double) size.width / 1360; // on suppose qu'on compare la résolution du client à 1360x768 pixels (soit 1360x705 sans la barre des tâches)
		scaleY = (double) size.height / 705;
		
		EventQueue.invokeLater(() -> { // on programme de façon thread-safe
			frame = new JFrame();		
			
			frame.setPreferredSize(size);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle("Duet");			
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			
			frame.setMinimumSize(size);
			frame.setMaximumSize(size);
			
			// Pour redéfinir la méthode "paintComponent" du container, on procède par bloc d'initialisation :
			container = new JPanel() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;		
				
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(background, 0, 0, size.width, size.height, this);
				}
				
			};
			container.setLayout(new GridLayout(1,3));
			container.setOpaque(false);
			JPanel j1 = new JPanel();
			j1.setOpaque(false);
			container.add(j1);
			
			GameController gc = new GameController();
			GamePlane gp = new GamePlane(size.width/3, size.height, gc);
			gc.setModel(gp);
			GameView gw = new GameView(size, scaleX, scaleY, gc);
			gc.setView(gw);
			gw.addKeyListener(gw);
			gw.setFocusable(true);
			gw.setOpaque(false);
			container.add(gw);
			JPanel j3 = new JPanel();
			j3.setOpaque(false);
			container.add(j3);
			frame.add(container);
			frame.setVisible(true);
		});	
			
			
	}
	
}
