package com.pi4.duet.controller.game;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.pi4.duet.Scale;
import com.pi4.duet.Sound;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.model.game.Game;
import com.pi4.duet.model.game.GameState;
import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.model.game.data.ObstacleQueue;
import com.pi4.duet.model.game.data.ObstacleQueueStatus;
import com.pi4.duet.model.game.data.PatternData;
import com.pi4.duet.model.home.Commands;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.game.GameView;

public abstract class GameController implements KeyListener {

	protected Game model;
	protected GameView view;
	protected Settings settings;
	protected Commands commands;
	protected Scale scale;

	protected Sound defeatSound = new Sound("defeat.wav", false);
	protected Sound reachedSound = new Sound("reached.wav", false);
	protected Sound music = new Sound("music.wav", true);

	protected ObstacleQueue gameTimer;
	protected HomePageViewController hpvC;
	protected WheelController wheelController;

	protected boolean backgroundMovement;

	public GameController(HomePageViewController hpvC, Settings settings, Commands commands, Scale scale) {
		this.hpvC = hpvC;
		this.settings = settings;
		this.commands = commands;
		music.stop();
		this.scale = scale;
		gameTimer = new ObstacleQueue(this, scale);
		this.wheelController = new WheelController(settings, commands, this, 1);
	}

	public final void setModel(Game model) { this.model = model; }
	public final Game getModel() { return model; }
	public final void setView(GameView view) { this.view = view; }
	public final GameView getView() { return view; }
	public final WheelController getWheelController() { return this.wheelController; }

	public final void refreshView() {
		view.refresh();
	}

	public abstract void gameStart();

	public final void gameStop() {
		gameTimer.cancel();
		music.stop();
	}

	public abstract void verifyCollision(Obstacle o);

	public abstract void verifyObstacleReached(Obstacle o);

	public void hasWin() {
		if (model.getObstacles().size() == 0 && gameTimer.getStatus() == ObstacleQueueStatus.FINISHED) {
			this.setBackgroundMovement(true);
			gameStop();
			view.afficheWin();
			view.refresh();
			gameTimer.setStatus(ObstacleQueueStatus.FINISHED);
			model.setState(GameState.FINISHED);
		}
	}

	public final void resetIntertie() {
		model.getWheel().setInertia(0);
	}

	public abstract void addObstacle(Obstacle o);

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

	public final void addPattern(PatternData d) { gameTimer.putPattern(d); }

	public final Boolean getBackgroundMouvement() { return this.backgroundMovement; }
	public final void setBackgroundMovement(boolean b) { this.backgroundMovement = b; }

	public final GameState getState() {
		// TODO Auto-generated method stub
		return model.getState();

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (model.getState() == GameState.ON_GAME){

			if(e.getKeyCode() == commands.getPause()){
				this.setBackgroundMovement(true);
				wheelController.stopWheelRotation();
				model.setState(GameState.PAUSED);
				view.affichePause();
				for(int i = 0; i < model.getObstacles().size(); i++){
					model.getObstacles().get(i).setVelocity(0.1);
				}
			}

			if(e.getKeyCode() == commands.getFallObs()){
				for(int i = 0;i <model.getObstacles().size(); i++){
					model.getObstacles().get(i).setVelocity(0.1);
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_UP){
				if(view.getProgression()>=165 && view.getAppuyer()==false ) {
					view.setAppuyer(true);
					view.setAppuyer(true);
					model.removeObstacle(model.getObstacles().get(model.indice()));
					view.removeObstacle(view.getObstacles().get(view.indice()));

				}
			}
		}
		else {
			if (commands.getPause() == e.getKeyCode()) {
				model.setState(GameState.ON_GAME);
			}
		}
	}
}