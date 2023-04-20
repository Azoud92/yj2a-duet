package com.pi4.duet.controller.game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.TimerTask;

import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.model.game.Direction;
import com.pi4.duet.model.game.GameDuo;
import com.pi4.duet.model.game.GameState;
import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.model.game.data.ObstacleQueue;
import com.pi4.duet.model.game.data.ObstacleQueueStatus;
import com.pi4.duet.model.home.Commands;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.game.ObstacleView;

public class GameDuoController extends GameController {

	private WheelController wheelTopController;

	public GameDuoController(HomePageViewController hpvC, Settings settings, Commands commands, Scale scale){
		super(hpvC, settings, commands, scale);
		this.wheelTopController = new WheelController(settings, commands, this, 2);
	}

	public WheelController getWheelTopController() { return wheelTopController; }

	@Override
	public void gameStart() {
		if (model.getState() != GameState.READY) return;
		model.setState(GameState.ON_GAME);
		gameTimer.setStatus(ObstacleQueueStatus.WAITING);

		wheelController.setWheelRotating(null);
		wheelTopController.setWheelRotating(null);

		if (settings.getMusic()) music.play();

		gameTimer = new ObstacleQueue(this, scale);
		gameTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (model.getState() == GameState.ON_GAME){
					hasWin();
					if (model.getObstacles().size() > 0 && ((GameDuo) model).getTopObstacles().size() > 0) {
						for (Obstacle o : model.getObstacles()) { // animation des obstacles pour les faire "tomber"
							o.updatePosition(Direction.BOTTOM);
							verifyCollision(o);
							verifyObstacleReached(o);
							refreshView();
						}
						for (Obstacle o : ((GameDuo) model).getTopObstacles()) {
							o.updatePosition(Direction.TOP);
							verifyCollision(o);
							verifyObstacleReached(o);
							refreshView();
						}
					}
					else refreshView();
					wheelController.animateWheel();
					wheelTopController.animateWheel();
				}
			}
		}, 0, 1);
	}

	@Override
	public void verifyCollision(Obstacle o) {
		if (model.getState() == GameState.FINISHED) return;
		int res = model.getWheel().isInCollision(o);
		int resTop = ((GameDuo) model).getTopWheel().isInCollision(o);
		ObstacleController oc = o.getController();
		
		int oX = (int) o.getCenter().getX();
		int oY = (int) o.getCenter().getY();

		if (res > 0 || resTop > 0) {
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

			if (resTop == 1) oc.addCollisionView(new Point(wheelTopController.getCenterBall_1().getX() - oX,
					wheelTopController.getCenterBall_1().getY() - oY), Color.RED);
			else if (resTop == 2) oc.addCollisionView(new Point(wheelTopController.getCenterBall_2().getX() - oX,
					wheelTopController.getCenterBall_2().getY() - oY), Color.BLUE);
			else if (resTop == 3) {
				oc.addCollisionView(new Point(wheelTopController.getCenterBall_1().getX() - oX,
						wheelTopController.getCenterBall_1().getY() - oY), Color.RED);
				oc.addCollisionView(new Point(wheelTopController.getCenterBall_2().getX() - oX,
						wheelTopController.getCenterBall_2().getY() - oY), Color.BLUE);
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
				if (p.getY() > model.getWheel().getCenter().getY() + model.getWheel().getRadius() || p.getY() < wheelTopController.getWheelCenter().getY() - wheelTopController.getWheelRadius()) {
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
				if (p.getY() > model.height || p.getY() < 0) {
					visible = false;
				}
				else {
					return;
				}
			}
			if (!visible) {
				model.removeObstacle(o);
				((GameDuo) model).removeTopObstacle(o);
			}
		}
	}

	@Override
	public void replay() {
		hpvC.runLvlDuo(hpvC.getWindow(), hpvC.getView(), true);
	}

	@Override
	public void addObstacle(Obstacle o, int id) {
		Obstacle omTop = o.clone();

		ObstacleController ocTop = new ObstacleController();
		ObstacleController ocBottom = new ObstacleController();

		ObstacleView ovBottom = new ObstacleView(view.getWidth(), view.getHeight(), (int) wheelController.getBallRadius(), ocBottom, id);
		ObstacleView ovTop = new ObstacleView(view.getWidth(), view.getHeight(), (int) wheelController.getBallRadius(), ocTop, id);


		if(hpvC.getObstaclesViews() != null && hpvC.getObstaclesViews().size() > 1) {
			ovBottom.setCollisionsMap(hpvC.getObstaclesViews().get(0).getCollisionsMap());
			ovBottom.resetCollisions();
			ovTop.setCollisionsMap(hpvC.getObstaclesViews().get(1).getCollisionsMap());
			ovTop.resetCollisions();
			hpvC.getObstaclesViews().remove(0);
			hpvC.getObstaclesViews().remove(0);
		}

		ocBottom.setView(ovBottom);
		omTop.setController(ocTop);
		ocTop.setView(ovTop);
		ocTop.setModel(omTop);
		o.setController(ocBottom);
		
		ocBottom.setModel(o);
		model.addObstacle(o);
		((GameDuo) model).addTopObstacle(omTop);
		view.addObstacle(ovBottom);
		view.addObstacle(ovTop);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (model.getState() == GameState.ON_GAME){
			switch(e.getKeyCode()) {
				case KeyEvent.VK_SPACE:
					this.setBackgroundMovement(true);
					wheelController.stopWheelRotation();
					wheelTopController.stopWheelRotation();
					model.setState(GameState.PAUSED);
					view.affichePause();
					break;
			}
		}
		else {
			if (KeyEvent.VK_SPACE == e.getKeyCode()) {
				model.setState(GameState.ON_GAME);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
