package com.pi4.duet.controller.home;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.Sound;
import com.pi4.duet.controller.game.GameDuoController;
import com.pi4.duet.controller.game.GameInfiniteController;
import com.pi4.duet.controller.game.GameLevelController;
import com.pi4.duet.model.game.Direction;
import com.pi4.duet.model.game.GameDuo;
import com.pi4.duet.model.game.GameInfinite;
import com.pi4.duet.model.game.GameLevel;
import com.pi4.duet.model.game.data.PatternData;
import com.pi4.duet.model.home.HomePage;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.MainWindow;
import com.pi4.duet.view.game.*;
import com.pi4.duet.view.home.HomePageView;
import com.pi4.duet.view.home.SettingsView;
import com.pi4.duet.view.home.EditorView;
import com.pi4.duet.model.home.EditorModel;
import com.pi4.duet.view.home.Transition;

public class HomePageViewController {

	private GameLevelView gv;
	private GameLevelController gc;
	private GameLevel gp;

	private HomePage model;

	private HomePageView view;
	private MainWindow window;

	private Settings sm;
	private SettingsView sv;
	private SettingsController sc;

	private GameDuoView gdv;
	private GameDuoController gdc;
	private GameDuo gpd;
	
	private EditorView edv;
	private EditorController edc;
	private EditorModel edm;

	private GameInfinite gi;
	private GameInfiniteView giv;
	private GameInfiniteController gic;

	private Scale scale;
	private Dimension size;

	private ArrayList<ObstacleView> obstaclesViews;

	private Sound homeMusic = new Sound("homeMusic.wav", true);

	public HomePageViewController(Dimension size, Scale scale) {
		this.size = size;
		this.scale = scale;
		homeMusic.stop();
		sm = Settings.read();
		sc = new SettingsController(this, window, scale);
		sc.setModel(sm);
		sv = new SettingsView(size, sc, scale, window);
		sc.setView(sv);
		sc.initCommands();
	}

	public void runLevel(MainWindow window, HomePageView view, int numLevel, boolean replay) {
		this.view = view;
		this.window = window;

		if (replay) {
			gv.setVisible(false);
			obstaclesViews = gv.getObstacles();
		}
		else obstaclesViews = null;

		gc = new GameLevelController(this, sm, sc.getCommandsModel(), scale);
		gp = new GameLevel(size.width, size.height, new Point(size.width / 2, size.height - 150), numLevel, scale, gc);
		gc.getWheelController().setModel(gp.getWheel());
		gc.setModel(gp);
		gv = new GameLevelView(size, scale, sc.getCommandsModel(), gc);
		gc.getWheelController().setView(gv.getWheel());
		gc.setView(gv);
		
		window.setMainContainer(gv);

		gv.requestFocus();
		gv.setFocusable(true);
		
		try {
			gp.addPattern(PatternData.read(this.getClass().getResource("/resources/levels/level" + numLevel + ".ser").getFile()));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gp.gameStart();
		homeMusic.stop();
	}

	public void runLvlDuo(MainWindow window, HomePageView view, boolean replay) {
		this.view = view;
		this.window = window;

		if (replay) {
			obstaclesViews = gdv.getObstacles();
		}
		else obstaclesViews = null;

		gdc = new GameDuoController(this, sm, sc.getCommandsModel(), scale);
		gpd = new GameDuo(size.width, size.height, new Point(size.width / 2, size.height - 150), new Point(size.width / 2, 150), scale, gdc);
		gdc.getWheelController().setModel(gpd.getWheel());
		gdc.getTopWheelController().setModel(gpd.getTopWheel());
		gdc.setModel(gpd);
		gdv = new GameDuoView(size, scale, sc.getCommandsModel(), gdc);
		gdc.getWheelController().setView(gdv.getWheel());
		gdc.getTopWheelController().setView(gdv.getWheelTopView());
		gdc.setView(gdv);

		window.setMainContainer(gdv);

		gdv.requestFocus();
		gdv.setFocusable(true);

		try {
			gpd.addPattern(PatternData.read(this.getClass().getResource("/resources/levels/levelDuo.ser").getFile()));
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gpd.gameStart();
		homeMusic.stop();
	}
	
	public void runInfinite(MainWindow window, HomePageView view, boolean replay) {
		this.view = view;
		this.window = window;

		if (replay) {
			giv.setVisible(false);
			obstaclesViews = giv.getObstacles();
		}
		else obstaclesViews = null;

		gic = new GameInfiniteController(this, sm, sc.getCommandsModel(), scale);
		gi = new GameInfinite(size.width, size.height, new Point(size.width / 2, size.height - 150), scale, gic);
		gic.getWheelController().setModel(gi.getWheel());
		gic.setModel(gi);
		giv = new GameInfiniteView(size, scale, sc.getCommandsModel(), gic);
		gic.getWheelController().setView(giv.getWheel());
		gic.setView(giv);
		
		window.setMainContainer(giv);

		giv.requestFocus();
		giv.setFocusable(true);
		
		try {
			gi.addPattern(PatternData.read(this.getClass().getResource("/resources/levels/levelInfinite.ser").getFile()));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gi.gameStart();
		homeMusic.stop();
	}
	
	public void runLvlEditor(MainWindow window, HomePageView view) {
		this.window = window;
		this.view = view;
		
		edm = new EditorModel();
		edc = new EditorController(edm);
		edv = new EditorView(edc, view);
		
		JPanel container = new JPanel(new GridLayout());
		
		container.add(edv);
		
		window.setMainContainer(container);
	}
	
	public void runHomePage() {
		view.setVisible(true);
		view.revalidate();
		view.repaint();
		view.paintComponents(view.getGraphics());

		window.setMainContainer(view);
	}

	public void runSettings(Dimension size, MainWindow window) {
		this.window = window;
		this.size = size;
		sv.setVisible(true);
		Transition t = new Transition(view, sv, size.width, size.height, Direction.LEFT);

		window.setMainContainer(t);
		sv.setGw(this.window);
		view.buttonsOff();
		sv.ButtonsOff();
		t.transition();
		
		Timer temp = new Timer();
		temp.schedule(new TimerTask(){
			@Override
			public void run() {
				if(!t.getTransition()) {
					view.buttonsOn();
					sv.ButtonsOn();
					temp.cancel();
				}
			}
		}, 0, 10);	
	}

	public void stopMusic() {
		homeMusic.stop();
	}

	public void setModel(HomePage model) { this.model = model; }

	public void runMusic() {
		if (sm.getMusic()) homeMusic.play();
	}

	public void setView(HomePageView view) {
		this.view = view;
	}

	public HomePageView getView() {
		return view;
	}

	public MainWindow getWindow() {
		return window;
	}
	
	public void setWindow(MainWindow gw) {
		this.window = gw;
		
	}

	public Settings getSettings() { return sm; }

	public void save() {
		// TODO Auto-generated method stub
		model.save();
	}
	
	public Dimension getSize() {
		return size;
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

	public void refreshLevelButton(int i) {
		// TODO Auto-generated method stub
		view.buttonsOn();
	}

}
