package com.pi4.duet.model.game;

import java.awt.Color;
import java.util.ArrayList;

import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.controller.game.GameController;
import com.pi4.duet.controller.game.ObstacleController;
import com.pi4.duet.model.game.data.ObstacleQueue;
import com.pi4.duet.model.game.data.ObstacleQueueStatus;
import com.pi4.duet.model.game.data.PatternData;

// Modèle général abstrait du jeu
public abstract class Game {

	public final int width, height;
	
	protected GameController controller;
	protected ObstacleQueue gameTimer;
	protected Scale scale;

	protected Wheel wheel;
	protected ArrayList<Obstacle> obstacles = new ArrayList<>();
	protected GameState gameState = GameState.READY;
	
	private double effectDelaySpeed = 0.01;
	private double progressionEffect = 0;
	private boolean canUseEffect = false;

	public Game (int width, int height, Point coordsWheel, Scale scale, GameController controller) {
		this.controller = controller;
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.wheel = new Wheel(coordsWheel, width, controller.getWheelController(), controller.getSettings());
	}
	
	public void gameStart() {
		if (gameState != GameState.READY) return;
		gameState = GameState.ON_GAME;
		controller.start();		

		gameTimer = new ObstacleQueue(this, scale);
	}
	
	public void updateGame() {
		if (gameState != GameState.ON_GAME) return;		
		updateObstacles();
		incrProgressionEffect();
		if (getCanUseEffect()) controller.effectCanBeUsed();
		wheel.animateWheel();
	}
	
	protected abstract void updateObstacles();
	
	protected void verifyCollision(Obstacle o) {
		if (gameState == GameState.FINISHED) return;
		int res = wheel.isInCollision(o);
		ObstacleController oc = o.getController();
		
		int oX = (int) o.getCenter().getX();
		int oY = (int) o.getCenter().getY();

		
		if (res > 0) {
			gameStop();
			gameTimer.setStatus(ObstacleQueueStatus.FINISHED);
			gameState = GameState.FINISHED;
			if (res == 1) oc.addCollisionView(new Point(wheel.getCenterBall1().getX() - oX,
					wheel.getCenterBall1().getY() - oY), Color.RED);
			else if (res == 2) oc.addCollisionView(new Point(wheel.getCenterBall2().getX() - oX,
					wheel.getCenterBall2().getY() - oY), Color.BLUE);
			else if (res == 3) {
				oc.addCollisionView(new Point(wheel.getCenterBall1().getX() - oX,
						wheel.getCenterBall1().getY() - oY), Color.RED);
				oc.addCollisionView(new Point(wheel.getCenterBall2().getX() - oX,
						wheel.getCenterBall2().getY() - oY), Color.BLUE);
			}
			controller.lost();
		}
	}
	
	protected void obstacleReached(Obstacle o) {
		if (!o.getReached()) {
			boolean reach = false;
			for (Point p : o.getPoints()) {
				if (p.getY() > wheel.getCenter().getY() + wheel.getRadius()) {
					reach = true;
				}
				else {
					return;
				}
			}
			if (reach) {
				controller.obstacleReached();
				o.setReachedTrue();
			}
		}
		else {
			boolean visible = true;
			for (Point p : o.getPoints()) {
				if (p.getY() > height) {
					visible = false;
				}
				else {
					return;
				}
			}
			if (!visible) {
				removeObstacle(o);
			}
		}
	}
	
	public void gameStop() {
		if (gameState == GameState.ON_GAME) {
			gameTimer.cancel();
			gameTimer.purge();
			controller.stop();
		}
	}
	
	public void addPattern(PatternData d) { 
		gameTimer = new ObstacleQueue(this, scale, d);
	}

	public void addObstacle(Obstacle o) {
		this.obstacles.add(o);
	}

	public void removeObstacle(Obstacle o) {
		this.obstacles.remove(o);
		controller.removeObstacle(o.getController().getView());
	}

	@SuppressWarnings("unchecked")
	public final ArrayList<Obstacle> getObstacles() {
		return (ArrayList<Obstacle>) obstacles.clone();
	}

	public final Wheel getWheel() { return wheel; }

	public final GameState getState() { return gameState; }

	public final void setState(GameState s) {
		this.gameState = s;
	}

	public final boolean getCanUseEffect() {
		return canUseEffect;
	}

	public final void setCanUseEffect(boolean canUseEffect) {
		this.canUseEffect = canUseEffect;
	}
	
	public final double getProgressionEffect() { return progressionEffect; }
	
	private void incrProgressionEffect() {		
		if (this.progressionEffect >= 100) {
			this.canUseEffect = true;
		}
		else this.progressionEffect += effectDelaySpeed;
	}
	
	public void useEffect() {
		this.progressionEffect = 0;
		this.canUseEffect = false;
	}
	
	public final void fall() {
		gameTimer.fall();
	}
	
	public final void stopFall() { gameTimer.stopFall(); }

	public final void initObstacle(Obstacle o, int id) {
		controller.addObstacle(o, id);
	}
	
	public void gameResume() {
		controller.setBackgroundMovement(false);
		gameState = GameState.ON_GAME;
		gameTimer.setStatus(ObstacleQueueStatus.WAITING);
	}

}
