package com.pi4.duet.view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.model.home.HomePage;
import com.pi4.duet.view.home.HomePageView;

public class Cinematic extends JPanel {
	private Dimension size;
	private int progression;
	private Scale scale;
	private GameWindow window;
	private HomePageViewController hpc;
	private Image background = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();
	private Timer timer;
	
	public Cinematic(Dimension size, JFrame frame, GameWindow window, HomePageViewController hpc, Scale scale) {
        progression = 0;
		this.scale = scale;
		this.window = window;
		this.setBackground(Color.black);
		Dimension dim = new Dimension(size.width / 3, size.height);
		this.size = dim;
		this.setPreferredSize(dim);
		this.setLayout(null);
		this.hpc = hpc;
		
		this.timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				progression++;
				repaint();
				
				if(progression == 100) {
					setVisible(false);
					HomePage hp = HomePage.read();
					hpc.setModel(hp);
					HomePageView hpv = new HomePageView(size, frame, window, hpc, scale);
					hpc.setView(hpv);
					hpc.setWindow(window);
					hpc.runHomePage();
					
				}
			}
		}, 0, 20);
    }
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (hpc.getSettings().getBackground()) g.drawImage(background, 0, 0, size.width, size.height, this);
		
		if(progression < 101 && progression > -1) {
			int placeW = size.width /5;
			int placeH = size.height/7;
		    g.setColor(Color.BLUE);
		    g.fillRect(placeW, placeH*3, placeW*3 * progression/100, placeH);
		    g.setColor(Color.BLACK);
		    g.drawRect(placeW, placeH*3, placeW*3 - 1, placeH - 1);
	    }
	}
	
    
	
}
