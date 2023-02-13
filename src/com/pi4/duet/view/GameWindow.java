package com.pi4.duet.view;

import java.awt.Dimension; 
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.pi4.duet.controller.GameController;
import com.pi4.duet.model.GamePlane;

public class GameWindow extends JFrame{

	private static final long serialVersionUID = 8560843128781641255L;
	
	private Dimension size;
	private JFrame frame;
	private JPanel container;
	
	private Image background = new ImageIcon(this.getClass().getResource("/background.png")).getImage();
	
	public GameWindow() {
		// Taille de l'écran en soustrayant celle de la barre des taches et du haut de la fenetre
		size = new Dimension();		
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle maximumWindowBounds = graphicsEnvironment.getMaximumWindowBounds();
		size.width = (int) maximumWindowBounds.getWidth();
		size.height = (int) maximumWindowBounds.getHeight();
		
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
			container.setLayout(new GridLayout(1, 3));
			container.setOpaque(false);
			container.add(new JPanel());
			
			GameController gc = new GameController();
			GamePlane gp = new GamePlane(size.width / 3, size.height, gc);
			gc.setModel(gp);
			
			GameView gw = new GameView(size, gc);
			gc.setView(gw);
			gw.addKeyListener(gc);
			gw.setFocusable(true);
			gw.setOpaque(false);
			
			container.add(gw);
			container.add(new JPanel());
			
			frame.add(container);
			frame.setVisible(true);
			
			gc.testObstacles();
			gp.gameStart();
		});	
			
			
	}
	
}
