package com.pi4.duet.controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;

import com.pi4.duet.model.GamePlane;
import com.pi4.duet.model.Obstacle;
import com.pi4.duet.model.Point;
import com.pi4.duet.view.GameView;
import com.pi4.duet.view.GameWindow;
import com.pi4.duet.view.HomePageView;
import com.pi4.duet.view.SettingsView;

public class HomePageViewController {
	
	private GameView gv;
	private GameController gc;
	private GamePlane gp;
	private HomePageView view;
	private GameWindow window;
	private SettingsView sv;
	private SettingsController sc;
		
	public void runParty(Dimension size, GameWindow window,HomePageView view) {
		this.view=view;
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
	
	
	public void continueParty(){
		this.gv.setVisible(true);
		this.gc.setPause(gc.isPause());
		this.gc.getModel().gamePausedOrResumed();
		this.gv.requestFocus();
		this.gv.setFocusable(true);

	}
	
	public void runLvl2(Dimension size, GameWindow window, HomePageView view) {
		this.view=view;
		this.window = window;
		gc = new GameController(this);
		gp = new GamePlane(size.width / 3, size.height, gc);
		gc.setModel(gp);
		gv = new GameView(size, gc);
		gv.addKeyListener(gc);	
		gc.setView(gv);
				
		JPanel container = new JPanel(new GridLayout(1, 3));
		
		container.add(new JPanel());	
		container.add(gv);
		container.add(new JPanel());
		window.setMainContainer(container);
		
		gv.requestFocus();
		gv.setFocusable(true);
						
		Point[] points = new Point[4];
		points[0] = new Point(gp.width / 3 + 50, 100);
		points[1] = new Point(gp.width / 3 + 250, 100);
		points[2] = new Point(gp.width / 3 + 250, 200);
		points[3] = new Point(gp.width / 3 + 50, 200);
	
		Obstacle test = new Obstacle(points, points[0], null);
		gc.putTestObstacle(test);
		gp.gameStart();
	}
	
	public void runLvl3(Dimension size, GameWindow window, HomePageView view) {
		this.view=view;
		this.window = window;
		gc = new GameController(this);
		gp = new GamePlane(size.width / 3, size.height, gc);
		gc.setModel(gp);
		gv = new GameView(size, gc);
		gv.addKeyListener(gc);	
		gc.setView(gv);
				
		JPanel container = new JPanel(new GridLayout(1, 3));
		
		container.add(new JPanel());	
		container.add(gv);
		container.add(new JPanel());
		window.setMainContainer(container);
		
		gv.requestFocus();
		gv.setFocusable(true);
						
		Point[] points = new Point[4];
		points[0] = new Point(gp.width / 3 + 50, 100);
		points[1] = new Point(gp.width / 3 + 250, 100);
		points[2] = new Point(gp.width / 3 + 250, 200);
		points[3] = new Point(gp.width / 3 + 50, 200);
		Obstacle test = new Obstacle(points, points[0], null);
		
		gp.addObstacleTestDelay(test, 1000);
		
		gp.gameStart();
	}	
	
	public void runNewParty(GameWindow window) {
		this.window = window;
		gp.wheel.resetBallPosition();
		double radius = gp.wheel.ballRadius;
		Point centerball2 = new Point(gp.wheel.getCenterBall2().getX()+radius, gp.wheel.getCenterBall2().getY()+radius);
		Point centerball1 = new Point(gp.wheel.getCenterBall1().getX()+radius, gp.wheel.getCenterBall1().getY()+radius);
		gv.setBallsPosition(centerball2, centerball1);
		gp.wheel.setAngle(0);
		gp.resetObstacle();
		gp.gameStart();
		
				
		
	}
	
	public void runHomePage() {

		view.setVisible(true);
		
		JPanel container = new JPanel(new GridLayout(1, 3));
		container.add(new JPanel());	
		container.add(view);
		container.add(new JPanel());
		window.setMainContainer(container);
		
	}
	
	public void runSettings(Dimension size, GameWindow window) {
		this.window = window;
		sc = new SettingsController(this);
		sv = new SettingsView(size, sc);
		sc.setView(sv);
		sv.setVisible(true);

		
		JPanel container = new JPanel(new GridLayout(1, 3));
		container.add(new JPanel());	
		container.add(sv);
		container.add(new JPanel());
		window.setMainContainer(container);
		
		
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
