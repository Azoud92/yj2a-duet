package com.pi4.duet.view.game;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.model.home.HomePage;
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
		size.width = ((int) maximumWindowBounds.getWidth()) / 3;
		size.height = (int) maximumWindowBounds.getHeight();

		Scale scale = new Scale(size.width, size.height);

		EventQueue.invokeLater(() -> { // on programme de façon thread-safe
			frame = new JFrame();

			
			frame.setTitle("Duet");
			frame.setResizable(false);
			frame.setBounds(size.width, 0, size.width, size.height);


			

			HomePage hp = HomePage.read();
			HomePageViewController hpc = new HomePageViewController(new Dimension(size.width, size.height), scale);
			hpc.setModel(hp);
			HomePageView hpv = new HomePageView(new Dimension(size.width, size.height), frame, this, hpc, scale);
			hpc.setView(hpv);


			frame.add(hpv);
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


}
