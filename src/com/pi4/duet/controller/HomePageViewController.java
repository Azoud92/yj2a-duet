package com.pi4.duet.controller;

import java.awt.Dimension; 
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.pi4.duet.Point;
import com.pi4.duet.Sound;
import com.pi4.duet.model.GamePlane;

import com.pi4.duet.model.HomePage;

import com.pi4.duet.model.GamePlaneDuo;

import com.pi4.duet.model.Obstacle;
import com.pi4.duet.model.Settings;
import com.pi4.duet.model.State;
import com.pi4.duet.view.Scale;
import com.pi4.duet.view.game.GameDuoView;
import com.pi4.duet.view.game.GameView;
import com.pi4.duet.view.game.GameWindow;
import com.pi4.duet.view.home.HomePageView;
import com.pi4.duet.view.home.SettingsView;

public class HomePageViewController {
	
	private GameView gv;
	private GameController gc;
	private GamePlane gp;

	private HomePage model;

	private HomePageView view;
	private GameWindow window;
	
	private Settings sm;
	private SettingsView sv;
	private SettingsController sc;
	
	private GameDuoView gdv;
	private GameDuoController gdc;
	private GamePlaneDuo gpd;
	
	private Sound homeMusic = new Sound("homeMusic.wav", true);
	
	public HomePageViewController(Dimension size, Scale scale) {
		homeMusic.stop();
		sm = Settings.read();
		sc = new SettingsController(this);
		sc.setModel(sm);
		sv = new SettingsView(size, sc, scale);		
		sc.setView(sv);
		if (sm.getMusic()) homeMusic.play();
	}
			
	public void runParty(Dimension size, GameWindow window,HomePageView view) {
		this.view=view;
		this.window = window;
		gc = new GameController(this, sm);
		gp = new GamePlane(size.width / 3, size.height, gc, 1);
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
	
	public void runLvlDuo(Dimension size, GameWindow window, HomePageView view) {
		this.view=view;
		this.window = window;
		gdc = new GameDuoController(this, sm);
		gpd = new GamePlaneDuo(size.width / 3, size.height, gdc);
		gdc.setModel(gpd);
		gdv = new GameDuoView(size, gdc);
		gdc.setView(gdv);
		gdv.addKeyListener(gdc);	
				
		JPanel container = new JPanel(new GridLayout(1, 3));
		
		container.add(new JPanel());	
		container.add(gdv);
		container.add(new JPanel());
		window.setMainContainer(container);
		
		gdv.requestFocus();
		gdv.setFocusable(true);
						
		//gdc.testObstacles();
		gdc.gameStart();
		homeMusic.stop();
	}
	
	
	public void continueParty(){
		this.gv.setVisible(true);
		this.gc.getModel().setState(State.ON_GAME);
		this.gv.requestFocus();
		this.gv.setFocusable(true);
		homeMusic.stop();
		if(sc.getMusic())gc.playMusic();
	}
	
	public void runLvl2(Dimension size, GameWindow window, HomePageView view) {
		this.view=view;
		this.window = window;
		gc = new GameController(this, sm);
		gp = new GamePlane(size.width / 3, size.height, gc, 2);
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
		gc.addObstacle(test);
		gc.gameStart();
		homeMusic.stop();
	}
	
	public void runLvl3(Dimension size, GameWindow window, HomePageView view) {
		this.view=view;
		this.window = window;
		gc = new GameController(this, new Settings());
		gp = new GamePlane(size.width / 3, size.height, gc, 3);
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
		
		gc.addObstacleTestDelay(test, 1);
		
		gc.gameStart();
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
	}
	
	public void stopMusic() {
		homeMusic.stop();
	}
	
	public void setModel(HomePage model) { this.model = model; }

	public void runMusic() {
		homeMusic.play();
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

	public void save() {
		// TODO Auto-generated method stub
		model.save();
	}

	public void addLevel(int i) {
		// TODO Auto-generated method stub
		model.addLevel(i);
	}
	
	public boolean isLevelAvailable(int i) {
		return model.getLevelsAvailable().contains(i);
	}

	public ArrayList<Integer> getLevelsAvailable() {
		// TODO Auto-generated method stub
		return model.getLevelsAvailable();
	}
	

	
}
