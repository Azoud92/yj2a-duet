package com.pi4.duet.view.game;

import java.awt.Dimension; 
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.pi4.duet.controller.HomePageViewController;
import com.pi4.duet.view.Scale;
import com.pi4.duet.view.home.HomePageView;

public class GameWindow extends JFrame { // fenêtre principale

	private static final long serialVersionUID = 8560843128781641255L;
	
	private Dimension size;
	private JFrame frame;
	private JPanel container;
	
	public GameWindow() {
		// Taille de l'écran en soustrayant celle de la barre des tâches et du haut de la fenêtre
		size = new Dimension();		
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle maximumWindowBounds = graphicsEnvironment.getMaximumWindowBounds();
		size.width = (int) maximumWindowBounds.getWidth();
		size.height = (int) maximumWindowBounds.getHeight();
		
		Scale scale = new Scale(size.width, size.height);
		
		EventQueue.invokeLater(() -> { // on programme de façon thread-safe
			frame = new JFrame();		
			
			frame.setPreferredSize(size);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle("Duet");			
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			
			frame.setMinimumSize(size);
			frame.setMaximumSize(size);
			
			container = new JPanel();
			container.setLayout(new GridLayout(1, 3));
			container.add(new JPanel());
						
			HomePageViewController hpc = new HomePageViewController(size, scale);
			HomePageView hpv = new HomePageView(size, frame, this, hpc, scale);
			hpc.setView(hpv);
			container.add(hpv);
			
			container.add(new JPanel());
			
			frame.add(container);
			frame.setVisible(true);		
		});
	}
	
	public void setMainContainer(JPanel container){
		this.container = container;
		frame.repaint();
		frame.add(container,0);
		frame.setVisible(true);
	}
	
	public JPanel getMainContainer() {
		return container;
	}

	public JFrame getFrame() {
		return frame;
	}

	public static void main(String[] args) {

		new GameWindow();

	}
	
}
