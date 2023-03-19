package com.pi4.duet.controller.home;

import java.awt.Dimension; 
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.Sound;
import com.pi4.duet.controller.game.GameController;
import com.pi4.duet.controller.game.GameDuoController;
import com.pi4.duet.model.game.GamePlane;
import com.pi4.duet.model.game.GamePlaneDuo;
import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.model.game.ObstacleQueueStatus;
import com.pi4.duet.model.game.PatternData;
import com.pi4.duet.model.game.State;
import com.pi4.duet.model.home.HomePage;
import com.pi4.duet.model.home.Settings;
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
	
	private Scale scale;
		
	private Sound homeMusic = new Sound("homeMusic.wav", true);
		
	public HomePageViewController(Dimension size, Scale scale) {
		this.scale = scale;
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
		gc = new GameController(this, sm, scale);
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

		/*Point[] rect = new Point[4];
		rect[0] = new Point(gp.width / 2, 100);
		rect[1] = new Point(gp.width / 2 + 100, 100);
		rect[2] = new Point(gp.width / 2 + 100, 120);
		rect[3] = new Point(gp.width / 2, 120);
		
		Obstacle test = new Obstacle(rect, rect[0], null);*/
		try {
			gc.addPattern(PatternData.read(this.getClass().getResource("/resources/levels/level1.ser").getPath()));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
						
		Point[] points = new Point[4];
		points[0] = new Point(gpd.width / 3 + 50, gpd.height/2 - 10);
		points[1] = new Point(gpd.width / 3 + 200, gpd.height/2 - 10);
		points[2] = new Point(gpd.width / 3 + 200, gpd.height/2 + 10);
		points[3] = new Point(gpd.width / 3 + 50, gpd.height/2 + 10);
	
		Obstacle test = new Obstacle(points, points[0], null);
		gdc.addObstacle(test);
		
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
		gc = new GameController(this, sm, scale);
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
						
		
		
		Point[] points1 = new Point[4];
		points1[0] = new Point(gp.width / 3 + 50, 0);
		points1[1] = new Point(gp.width / 3 + 250, 0);
		points1[2] = new Point(gp.width / 3 + 250, 400);
		points1[3] = new Point(gp.width / 3 + 50, 400);
	
		Obstacle test1 = new Obstacle(points1, points1[0], null);
		gc.addObstacleTestDelay(test1, 1, ObstacleQueueStatus.FINISHED);
		
		gc.gameStart();
		homeMusic.stop();
	}
	
	public void runLvl3(Dimension size, GameWindow window, HomePageView view) {
		this.view=view;
		this.window = window;
		gc = new GameController(this, new Settings(), scale);
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
		
		Point[] points1 = new Point[4];
		points1[0] = new Point(gp.width / 3 + 50, 100);
		points1[1] = new Point(gp.width / 3 + 250, 100);
		points1[2] = new Point(gp.width / 3 + 250, 200);
		points1[3] = new Point(gp.width / 3 + 50, 200);
		Obstacle test1 = new Obstacle(points1, points1[0], null);
				
		gc.addObstacleTestDelay(test, 4000, ObstacleQueueStatus.DELIVERY_IN_PROGRESS);
		gc.addObstacleTestDelay(test1, 8000, ObstacleQueueStatus.FINISHED);
		
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
