package com.pi4.duet.controller.home;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.Sound;
import com.pi4.duet.controller.game.GameDuoController;
import com.pi4.duet.controller.game.GameLevelController;
import com.pi4.duet.model.game.GameDuo;
import com.pi4.duet.model.game.GameLevel;
import com.pi4.duet.model.game.GameState;
import com.pi4.duet.model.game.data.PatternData;
import com.pi4.duet.model.home.HomePage;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.game.GameDuoView;
import com.pi4.duet.view.game.GameLevelView;
import com.pi4.duet.view.game.GameWindow;
import com.pi4.duet.view.game.ObstacleView;
import com.pi4.duet.view.home.HomePageView;
import com.pi4.duet.view.home.SettingsView;

public class HomePageViewController {

	private GameLevelView gv;
	private GameLevelController gc;
	private GameLevel gp;

	private HomePage model;

	private HomePageView view;
	private GameWindow window;

	private Settings sm;
	private SettingsView sv;
	private SettingsController sc;

	private GameDuoView gdv;
	private GameDuoController gdc;
	private GameDuo gpd;

	private Scale scale;
	private Dimension size;

	private ArrayList<ObstacleView> obstaclesViews;

	private Sound homeMusic = new Sound("homeMusic.wav", true);

	public HomePageViewController(Dimension size, Scale scale) {
		this.size = size;
		this.scale = scale;
		homeMusic.stop();
		sm = Settings.read();
		sc = new SettingsController(this);
		sc.setModel(sm);
		sv = new SettingsView(size, sc, scale);
		sc.setView(sv);
		if (sm.getMusic()) homeMusic.play();
	}

	public void runLevel(GameWindow window, HomePageView view, int numLevel, boolean replay) {
		this.view = view;
		this.window = window;

		if (replay) {
			obstaclesViews = gv.getObstacles();
		}

		gc = new GameLevelController(this, sm, scale);
		gp = new GameLevel(size.width / 3, size.height, new Point(size.width / 6, size.height - 150), numLevel);
		gc.getWheelController().setModel(gp.getWheel());
		gc.setModel(gp);
		gv = new GameLevelView(size, gc);
		gc.getWheelController().setView(gv.getWheel());
		gc.setView(gv);
		gv.addKeyListener(gc);

		JPanel container = new JPanel(new GridLayout(1, 3));

		container.add(new JPanel());
		container.add(gv);
		container.add(new JPanel());
		window.setMainContainer(container);

		gv.requestFocus();
		gv.setFocusable(true);

		try {
			gc.addPattern(PatternData.read("src/resources/levels/level" + numLevel + ".ser"));
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

	public void runLvlDuo(GameWindow window, HomePageView view, boolean replay) {
		this.view = view;
		this.window = window;

		if (replay) {
			obstaclesViews = gdv.getObstacles();
		}

		gdc = new GameDuoController(this, sm, scale);
		gpd = new GameDuo(size.width / 3, size.height, new Point(size.width / 6, size.height - 150), new Point(size.width / 6, 150));
		gdc.getWheelController().setModel(gpd.getWheel());
		gdc.getWheelTopController().setModel(gpd.getTopWheel());
		gdc.setModel(gpd);
		gdv = new GameDuoView(size, gdc);
		gdc.getWheelController().setView(gdv.getWheel());
		gdc.getWheelTopController().setView(gdv.getWheelTopView());
		gdc.setView(gdv);
		gdv.addKeyListener(gdc);


		JPanel container = new JPanel(new GridLayout(1, 3));

		container.add(new JPanel());
		container.add(gdv);
		container.add(new JPanel());
		window.setMainContainer(container);

		gdv.requestFocus();
		gdv.setFocusable(true);

		try {
			gdc.addPattern(PatternData.read("2022-yj2-g2-duet/src/resources/levels/levelDuo.ser"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gdc.gameStart();
		homeMusic.stop();
	}


	public void continueParty(){
		this.gv.setVisible(true);
		this.gc.getModel().setState(GameState.ON_GAME);
		this.gv.requestFocus();
		this.gv.setFocusable(true);
		homeMusic.stop();
		if(sc.getMusic())gc.playMusic();
		this.gc.setBackgroundMovement(false);
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

	public ArrayList<ObstacleView> getObstaclesViews() { return obstaclesViews; }

}
