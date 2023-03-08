package com.pi4.duet.controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;

import com.pi4.duet.Sound;
import com.pi4.duet.model.GamePlane;
import com.pi4.duet.model.Obstacle;
import com.pi4.duet.model.Point;
import com.pi4.duet.model.Settings;
import com.pi4.duet.model.State;
import com.pi4.duet.view.Scale;
import com.pi4.duet.view.game.GameView;
import com.pi4.duet.view.game.GameWindow;
import com.pi4.duet.view.home.HomePageView;
import com.pi4.duet.view.home.SettingsView;

public class HomePageViewController {
	
	private GameView gv;
	private GameController gc;
	private GamePlane gp;
	private HomePageView view;
	private GameWindow window;
	private Settings sm;
	private SettingsView sv;
	private SettingsController sc;
	
	private Sound homeMusic = new Sound("homeMusic.wav", true);
	
	public HomePageViewController(Dimension size, Scale scale) {
		sm = new Settings();
		sc = new SettingsController(this);
		sv = new SettingsView(size, sc, scale);
		sc.setModel(sm);
		sc.setView(sv);
	}
			
	public void runParty(Dimension size, GameWindow window,HomePageView view) {
		this.view=view;
		this.window = window;
		gc = new GameController(this, sm);
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
		gc.gameStart();
		homeMusic.stop();
	}
	
	
	public void continueParty(){
		this.gv.setVisible(true);
		this.gc.getModel().setState(State.ON_GAME);
		this.gv.requestFocus();
		this.gv.setFocusable(true);
		homeMusic.stop();
	}
	
	public void runLvl2(Dimension size, GameWindow window, HomePageView view) {
		this.view=view;
		this.window = window;
		gc = new GameController(this, sm);
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
		points[0] = new Point(gp.width / 3 + 100, 100);
		points[1] = new Point(gp.width / 3 + 200, 100);
		points[2] = new Point(gp.width / 3 + 200, 200);
		points[3] = new Point(gp.width / 3 + 100, 200);
		
		Point center = new Point(gp.width / 3 + 150, 150);
		
		Obstacle test = new Obstacle(points, center, null);
		
		gc.testObstacles();
		gc.putTestObstacle(test);
		gc.gameStart();
		homeMusic.stop();
	}

	
	public void runNewParty(GameWindow window) {
		this.window = window;
		gp.getWheel().resetBallPosition();
		double radius = gp.getWheel().ballRadius;
		Point centerball2 = new Point(gp.getWheel().getCenterBall2().getX()+radius, gp.getWheel().getCenterBall2().getY()+radius);
		Point centerball1 = new Point(gp.getWheel().getCenterBall1().getX()+radius, gp.getWheel().getCenterBall1().getY()+radius);
		gv.setBallsPosition(centerball2, centerball1);
		gp.getWheel().setAngle(0);
		gp.resetObstacles();
		gp.setState(State.READY);
		gc.gameStart();		
		homeMusic.stop();
	}
	
	public void runHomePage() {
		if (sm.getMusic()) homeMusic.play();
		view.setVisible(true);
		
		JPanel container = new JPanel(new GridLayout(1, 3));
		container.add(new JPanel());	
		container.add(view);
		container.add(new JPanel());
		window.setMainContainer(container);		
	}
	
	public void runSettings(Dimension size, GameWindow window) {
		this.window = window;		
		sv.setVisible(true);
		
		JPanel container = new JPanel(new GridLayout(1, 3));
		container.add(new JPanel());	
		container.add(sv);
		container.add(new JPanel());
		window.setMainContainer(container);
		homeMusic.stop();
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
	
	public Settings getSettings() { return sm; }
	
}
