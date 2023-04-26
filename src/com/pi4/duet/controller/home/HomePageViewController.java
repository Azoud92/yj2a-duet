package com.pi4.duet.controller.home;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.Sound;
import com.pi4.duet.controller.game.GameDuoController;
import com.pi4.duet.controller.game.GameInfiniController;
import com.pi4.duet.controller.game.GameLevelController;
import com.pi4.duet.model.game.Direction;
import com.pi4.duet.model.game.GameDuo;
import com.pi4.duet.model.game.GameInfini;
import com.pi4.duet.model.game.GameLevel;
import com.pi4.duet.model.game.GameState;
import com.pi4.duet.model.game.data.PatternData;
import com.pi4.duet.model.home.HomePage;
import com.pi4.duet.model.home.Settings;
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
	private GameWindow window;

	private Settings sm;
	private SettingsView sv;
	private SettingsController sc;

	private GameDuoView gdv;
	private GameDuoController gdc;
	private GameDuo gpd;
	
	private EditorView edv;
	private EditorController edc;
	private EditorModel edm;

	private GameInfini gi;
	private GameInfiniView giv;
	private GameInfiniController gic;

	private Scale scale;
	private Dimension size;

	private ArrayList<ObstacleView> obstaclesViews;

	private Sound homeMusic = new Sound("homeMusic.wav", true);

	public HomePageViewController(Dimension size, Scale scale) {
		this.size = size;
		System.out.println(size);
		this.scale = scale;
		homeMusic.stop();
		sm = Settings.read();
		sc = new SettingsController(this, window, scale);
		sc.setModel(sm);
		sv = new SettingsView(size, sc, scale, window);
		sc.setView(sv);
		sc.initCommands();
	}

	public void runLevel(GameWindow window, HomePageView view, int numLevel, boolean replay) {
		this.view = view;
		this.window = window;

		if (replay) {
			gv.setVisible(false);
			obstaclesViews = gv.getObstacles();
		}

		gc = new GameLevelController(this, sm, sc.getCommandsModel(), scale);
		gp = new GameLevel(size.width, size.height, new Point(size.width / 2, size.height - 150), numLevel, scale, gc);
		gc.getWheelController().setModel(gp.getWheel());
		gc.setModel(gp);
		gv = new GameLevelView(size, scale, sc.getCommandsModel(), gc);
		gc.getWheelController().setView(gv.getWheel());
		gc.setView(gv);
		//gv.addKeyListener(gc);

		window.setMainContainer(gv);

		gv.requestFocus();
		gv.setFocusable(true);
		
		try {
			gp.addPattern(PatternData.read("src/resources/levels/level" + numLevel + ".ser"));
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

	public void runLvlDuo(GameWindow window, HomePageView view, boolean replay) {
		this.view = view;
		this.window = window;

		if (replay) {
			obstaclesViews = gdv.getObstacles();
		}

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
			gpd.addPattern(PatternData.read("src/resources/levels/levelDuo.ser"));
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
	
	public void runLvlEditor(GameWindow window, HomePageView view) {
		this.window = window;
		this.view = view;
		
		edm = new EditorModel();
		edc = new EditorController(edm);
		edv = new EditorView(edc, view);
		
		JPanel container = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx = .3;
		container.add(new JPanel(), c);
		
		c.gridx = 1;
		c.weightx = .4;
		container.add(edv, c);
		
		c.gridx = 2;
		c.weightx = .3;
		container.add(new JPanel(), c);
		window.setMainContainer(container);

	}
	public void runLevelInfini(GameWindow window,HomePageView view,boolean replay){
		this.view = view;
		this.window = window;

		if (replay) {
			giv.setVisible(false);
			obstaclesViews = giv.getObstacles();
		}

		gic = new GameInfiniController(this, sm, sc.getCommandsModel(), scale);
		gi= new GameInfini(size.width / 3, size.height, new Point(size.width / 6, size.height - 150));

		gic.getWheelController().setModel(gi.getWheel());
		gic.setModel(gi);
		giv = new GameInfiniView(size, scale, gic);
		gic.getWheelController().setView(giv.getWheel());
		gic.setView(giv);
		//giv.addKeyListener(gic);


		window.setMainContainer(giv);

		giv.requestFocus();
		giv.setFocusable(true);
		gic.gameStart();
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
		view.revalidate();
		view.repaint();
		view.paintComponents(view.getGraphics());


		window.setMainContainer(view);
	}

	public void runSettings(Dimension size, GameWindow window) {
		this.window = window;
		this.size = size;
		System.out.println(size);
		sv.setVisible(true);
		Transition t = new Transition(view, sv, size.width, size.height, Direction.LEFT);

		window.setMainContainer(t);
		sv.setGw(this.window);
		view.ButtonsOff();
		sv.ButtonsOff();
		t.transition();
		
		Timer temp = new Timer();
		temp.schedule(new TimerTask(){
			@Override
			public void run() {
				if(!t.getTransition()) {
					view.ButtonsOn();
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

	public GameWindow getWindow() {
		return window;
	}
	
	public void setWindow(GameWindow gw) {
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

}
