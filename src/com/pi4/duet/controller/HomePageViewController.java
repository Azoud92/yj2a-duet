package com.pi4.duet.controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;

import com.pi4.duet.model.GamePlane;
import com.pi4.duet.view.GameView;
import com.pi4.duet.view.GameWindow;
import com.pi4.duet.view.HomePageView;

public class HomePageViewController {
	
	private GameView gv;
	private GameController gc;
	private GamePlane gp;
	private HomePageView view;
	private GameWindow window;
		
	public void runParty(Dimension size, GameWindow window) {
		this.window = window;
		gc = new GameController(this);
		gp = new GamePlane(size.width / 3, size.height, gc);
		gc.setModel(gp);
		gv = new GameView(size, gc);
		gc.setView(gv);
		gv.addKeyListener(gc);	
				
		JPanel container = new JPanel(new GridLayout(1, 3));
		
		container.add(new JPanel());	
		container.add(gv);
		container.add(new JPanel());
		window.setMainContainer(container);
		
		gv.requestFocus();
		gv.setFocusable(true);
						
		gc.testObstacles();
		gp.gameStart();
	}
	
	public void runHomePage(Dimension dimension, GameWindow window2) {
		view.setVisible(true);
	}
	
	
	public void setView(HomePageView view) { 
		this.view = view;
	}	
	
	public HomePageView getView() {
		return view;
	}
	
	public GameWindow getWindow() {
		return window;
	}


	
}
