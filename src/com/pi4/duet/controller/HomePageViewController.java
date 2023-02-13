package com.pi4.duet.controller;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import com.pi4.duet.model.GamePlane;
import com.pi4.duet.view.GameView;
import com.pi4.duet.view.GameWindow;
import com.pi4.duet.view.HomePageView;

public class HomePageViewController {
	
	private HomePageView view;
	private GameView gv;
	private GameController gc;
	private GamePlane gp;
	
	
	
	public void runParty(Dimension size, double scaleX, double scaleY, GameWindow window) {
		gc = new GameController();
		gp = new GamePlane(size.width/3, size.height, gc);
		gc.setModel(gp);
		gv = new GameView(size, scaleX, scaleY, gc);
		gc.setView(gv);
		
		
		JPanel container = new JPanel(new GridLayout(1,3));
		container.add(new JPanel());
		container.add(gv);
		container.add(new JPanel());
		window.setMainContainer(container);
		
		gv.requestFocus();
		gv.setFocusable(true);
		gv.addKeyListener(gv);

	}
	
	public void setView(HomePageView view) { this.view = view; }
	

}
