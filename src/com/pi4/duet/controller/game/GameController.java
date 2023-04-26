package com.pi4.duet.controller.game;

import java.awt.Dimension;
import java.util.Iterator;

import com.pi4.duet.Scale;
import com.pi4.duet.Sound;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.model.game.Game;
import com.pi4.duet.model.game.GameState;
import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.model.home.Commands;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.game.GameView;
import com.pi4.duet.view.game.ObstacleView;

public abstract class GameController {

	protected Game model;
	protected GameView view;
	protected Settings settings;	
	protected Scale scale;

	protected Sound defeatSound = new Sound("defeat.wav", false);
	protected Sound reachedSound = new Sound("reached.wav", false);
	protected Sound music = new Sound("music.wav", true);

	protected HomePageViewController hpvC;
	protected WheelController wheelController;
	
	protected boolean backgroundMovement;	
	
	public GameController(HomePageViewController hpvC, Settings settings, Commands commands, Scale scale) {
		this.hpvC = hpvC;
		this.settings = settings;
		this.scale = scale;
		this.wheelController = new WheelController(settings, commands, this);
	}

	public final void setModel(Game model) { this.model = model; }
	public final Game getModel() { return model; }
	public final void setView(GameView view) { this.view = view; }
	public final GameView getView() { return view; }
	public final WheelController getWheelController() { return this.wheelController; }

	public final void playMusic() {
		music.play();
	}

	public final void stopMusic() { music.stop(); }

	public final void affMenu() {
		hpvC.runHomePage();
		
		if(settings.getMusic()) { hpvC.runMusic(); }
		view.setVisible(false);
		this.setBackgroundMovement(true);
	}

	public abstract void replay();

	public final Dimension getSize() { return view.getSize(); }

	public final boolean isBackgroundEnabled() { return settings.getBackground(); }

	public final Boolean getBackgroundMouvement() { return this.backgroundMovement; }
	public final void setBackgroundMovement(boolean b) { this.backgroundMovement = b; }
	
	public final GameState getState() {
		// TODO Auto-generated method stub
		return model.getState();

	}

	public double getProgressionEffect() {
		// TODO Auto-generated method stub
		return model.getProgressionEffect();
	}

	public Settings getSettings() {
		// TODO Auto-generated method stub
		return settings;
	}
	
	public final boolean canUseEffect() {
		return model.getCanUseEffect() && model.getObstacles().size() > 0;
	}
	
	public final boolean isOnGame() { return model.getState() == GameState.ON_GAME; }
		
	public final void startPause() { 
		wheelController.stopWheelRotation();
		model.setState(GameState.PAUSED);		
	}
	
	public final void stopPause() { model.setState(GameState.ON_GAME); }

	public final void useEffect() {
		// TODO Auto-generated method stub
		model.useEffect();
		model.removeObstacle(model.getObstacles().get(0));
	}

	public final void stop() {
		// TODO Auto-generated method stub
		music.stop();
	}

	public final void start() {
		// TODO Auto-generated method stub
		if (settings.getMusic()) music.play();
	}

	public final void effectCanBeUsed() {
		// TODO Auto-generated method stub
		view.effectCanBeUsed();
	}

	public final void refreshView() {
		// TODO Auto-generated method stub
		view.refresh();
	}

	public final void lost() {
		// TODO Auto-generated method stub
		if (settings.getEffects()) defeatSound.play();
		view.lostGame();
		this.setBackgroundMovement(true);
	}

	public final void obstacleReached() {
		// TODO Auto-generated method stub
		if (settings.getEffects()) reachedSound.play();
	}

	public final void gameStop() {
		// TODO Auto-generated method stub
		model.gameStop();
	}

	public final void stopFall() {
		// TODO Auto-generated method stub
		model.stopFall();
	}

	public void addObstacle(Obstacle o, int idObs) {		
		ObstacleController oc = new ObstacleController();
		ObstacleView ov = new ObstacleView(view.getWidth(), view.getHeight(), (int) wheelController.getBallRadius(), oc, idObs);

		if (hpvC.getObstaclesViews() != null && hpvC.getObstaclesViews().size() > 0) {
			Iterator<ObstacleView> iter = hpvC.getObstaclesViews().iterator();
			while (iter.hasNext()) {
				ObstacleView ovH = iter.next();
				if (ovH.id == idObs) {
					ov.setCollisionsMap(ovH.getCollisionsMap());
					break;
				}				
			}			
		}
		oc.setView(ov);
		o.setController(oc);
		oc.setModel(o);
		model.addObstacle(o);
		view.addObstacle(ov);
	}

	public void fall() {
		// TODO Auto-generated method stub
		model.fall();
	}

	public void removeObstacle(ObstacleView ov) {
		// TODO Auto-generated method stub
		view.removeObstacle(ov);
	}

	public void gameResume() {
		// TODO Auto-generated method stub
		model.gameResume();
	}
		
}