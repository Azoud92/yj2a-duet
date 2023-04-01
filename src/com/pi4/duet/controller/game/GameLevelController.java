package com.pi4.duet.controller.game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.TimerTask;

import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.model.game.GameLevel;
import com.pi4.duet.model.game.GameState;
import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.model.game.data.ObstacleQueue;
import com.pi4.duet.model.game.data.ObstacleQueueStatus;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.game.ObstacleView;
import com.pi4.duet.view.home.CommandsView;

public class GameLevelController extends GameController {

	public GameLevelController(HomePageViewController hpvC, Settings settings, Scale scale) {
		super(hpvC, settings, scale);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void gameStart() {
		if (model.getState() != GameState.READY) return;
		model.setState(GameState.ON_GAME);
		gameTimer.setStatus(ObstacleQueueStatus.WAITING);

		wheelController.setWheelRotating(null);

		if (settings.getMusic()) music.play();

		gameTimer = new ObstacleQueue(this, scale);
		gameTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (model.getState() == GameState.ON_GAME){
					hasWin();
					if (model.getObstacles().size() > 0) {
						for (Obstacle o : model.getObstacles()) { // animation des obstacles pour les faire "tomber"
							o.updatePosition();
							verifyCollision(o);
							verifyObstacleReached(o);
							refreshView();
						}
					}
					else refreshView();
					wheelController.animateWheel();
				}
			}
		}, 0, 1);
	}

	@Override
	public void verifyCollision(Obstacle o) {
		if (model.getState() == GameState.FINISHED) return;
		int res = model.getWheel().isInCollision(o);
		ObstacleController oc = o.getController();

		/*int oX = (int) o.getPoints()[0].getX();
		int oY = (int) o.getPoints()[0].getY();*/
		
		int oX = (int) o.getCenter().getX();
		int oY = (int) o.getCenter().getY();

		
		if (res > 0) {
			gameStop();
			gameTimer.setStatus(ObstacleQueueStatus.FINISHED);
			model.setState(GameState.FINISHED);
			if (res == 1) oc.addCollisionView(new Point(wheelController.getCenterBall_1().getX() - oX,
					wheelController.getCenterBall_1().getY() - oY), Color.RED);
			else if (res == 2) oc.addCollisionView(new Point(wheelController.getCenterBall_2().getX() - oX,
					wheelController.getCenterBall_2().getY() - oY), Color.BLUE);
			else if (res == 3) {
				oc.addCollisionView(new Point(wheelController.getCenterBall_1().getX() - oX,
						wheelController.getCenterBall_1().getY() - oY), Color.RED);
				oc.addCollisionView(new Point(wheelController.getCenterBall_2().getX() - oX,
						wheelController.getCenterBall_2().getY() - oY), Color.BLUE);
			}
			if (settings.getEffects()) defeatSound.play();
			view.lostGame();
			this.setBackgroundMovement(true);
		}
	}

	@Override
	public void verifyObstacleReached(Obstacle o) {
		if (!o.getReached()) {
			boolean reach = false;
			for (Point p : o.getPoints()) {
				if (p.getY() > model.getWheel().getCenter().getY() + model.getWheel().getRadius()) {
					reach = true;
				}
				else {
					return;
				}
			}
			if (reach) {
				if (settings.getEffects()) reachedSound.play();
				o.setReachedTrue();
			}
		}
		else {
			boolean visible = true;
			for (Point p : o.getPoints()) {
				if (p.getY() > model.height) {
					visible = false;
				}
				else {
					return;
				}
			}
			if (!visible) {
				model.removeObstacle(o);
			}
		}
	}

	@Override
	public void addObstacle(Obstacle o) {
		ObstacleController oc = new ObstacleController();
		ObstacleView ov = new ObstacleView(view.getWidth(), view.getHeight(), (int) wheelController.getBallRadius(), oc);

		if (hpvC.getObstaclesViews() != null && hpvC.getObstaclesViews().size() > 0) {
			ov.setCollisionsMap(hpvC.getObstaclesViews().get(0).getCollisionsMap());
			ov.resetCollisions();
			hpvC.getObstaclesViews().remove(0);
		}
		oc.setView(ov);
		o.setController(oc);
		oc.setModel(o);
		model.addObstacle(o);
		view.addObstacle(ov);
	}

	@Override
	public void hasWin() {
		super.hasWin();
		if (!hpvC.getLevelsAvailable().contains(((GameLevel) model).numLevel + 1)) { // on ajoute seulement le niveau suivant à la liste des niveaux disponibles si ce dernier n'y figure pas
			hpvC.addLevel(((GameLevel) model).numLevel + 1);
			hpvC.save();
		}
	}

	@Override
	public void replay() {
		// TODO Auto-generated method stub
		hpvC.runLevel(hpvC.getWindow(), hpvC.getView(), ((GameLevel) model).numLevel, true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()== CommandsView.keyButton[5]){
			for(int i=0;i<model.getObstacles().size();i++){
				model.getObstacles().get(i).setVelocity(1);
			}
		}


	}

}
