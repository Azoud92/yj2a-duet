package com.pi4.duet.view;

import java.awt.Dimension; 
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.pi4.duet.controller.GameViewController;
import com.pi4.duet.model.Point;


public class GameWindow extends JFrame{

	private static final long serialVersionUID = 8560843128781641255L;
	
	private Dimension size;
	private double scaleX, scaleY, scaleXY; // repr�sente le coef multiplicateur � appliquer selon la r�solution de l'ecran
	private JFrame frame;
	private JPanel container;
	
	public GameWindow() {
		// Taille de l'�cran en soustrayant celle de la barre des taches et du haut de la fenetre
			size = new Dimension();
			size.width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
			size.height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration()).bottom - 15;

			scaleX = (double) size.width / 1360; // on suppose qu'on compare la r�solution du client �1360x768 pixels (soit 1360x705 sans la barre des taches)
			scaleY = (double) size.height / 705;
			scaleXY = (double) (size.width * size.height) / (1360.0 * 705.0);
			
			EventQueue.invokeLater(() -> { // on programme de façon thread-safe
				frame = new JFrame();		
				
				frame.setPreferredSize(size);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setTitle("Duet");			
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				
				frame.setMinimumSize(size);
				frame.setMaximumSize(size);			


				GameViewController gc = new GameViewController();
				container = new JPanel(new GridLayout(1,3));
				container.add(new JPanel());
				GameView gw = new GameView(size, scaleX, scaleY);
				container.add(gw);
				container.add(new JPanel());
				frame.add(container);
				frame.setVisible(true);
				
				
				Point[] tab = {new Point(100,100), new Point(200,100), new Point(200,200), new Point(100,200)};
				ObstacleView ov1 = new ObstacleView(tab);
				gw.addObstacle(ov1);
				
				Point[] tab1 = {new Point(300,300), new Point(400,300), new Point(400,400), new Point(300,400)};
				ObstacleView ov2 = new ObstacleView(tab1);
				gw.addObstacle(ov2);
				
				

			});	
			
			
	}
	
}
