package com.pi4.duet.model.game;

import java.awt.Color;
import java.util.ArrayList;

import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.controller.game.GameDuoController;
import com.pi4.duet.controller.game.ObstacleController;
import com.pi4.duet.model.game.data.ObstacleQueueStatus;

public class GameDuo extends Game {

	private Wheel topWheel;
	private ArrayList<Obstacle> topObstacles = new ArrayList<>();
	
	public GameDuo(int width, int height, Point coordsWheel, Point coordsTopWheel, Scale scale, GameDuoController controller) {
		super(width, height, coordsWheel, scale, controller);
		// TODO Auto-generated constructor stub
		this.wheel = new Wheel(coordsWheel, width, controller.getWheelController(), controller.getSettings());
		wheel.setRadius(wheel.getRadius() * 9 / 10);
		
		topWheel = new Wheel(coordsTopWheel, width, controller.getTopWheelController(), controller.getSettings());
		topWheel.setRadius(topWheel.getRadius() * 9 / 10);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void updateObstacles() {
		// TODO Auto-generated method stub
		ArrayList<Obstacle> copyBottom = (ArrayList<Obstacle>) obstacles.clone();
		ArrayList<Obstacle> copyTop = (ArrayList<Obstacle>) topObstacles.clone();
		if (copyBottom.size() > 0 && copyTop.size() > 0) {
			for (Obstacle o : copyBottom) { // animation des obstacles pour les faire "tomber"
				o.getController().paint();
				o.updatePosition(gameTimer.getAdd());
				o.getController().paint();
				verifyCollision(o);
				obstacleReached(o);
				controller.refreshView();
			}
			for (Obstacle o : copyTop) {
				o.getController().paint();
				o.updatePosition(gameTimer.getAdd());	
				o.getController().paint();
				verifyCollision(o);
				obstacleReached(o);
				controller.refreshView();
			}
		}					
		else controller.refreshView();
	}
	
	@Override
	public void updateGame() {
		if (gameState != GameState.ON_GAME) return;		
		updateObstacles();
		wheel.animateWheel();
		topWheel.animateWheel();
	}
		
	@Override
	public void verifyCollision(Obstacle o) {
		if (gameState == GameState.FINISHED) return;
		int resBottom = wheel.isInCollision(o);
		ObstacleController oc = o.getController();
		
		int oX = (int) o.getCenter().getX();
		int oY = (int) o.getCenter().getY();
		
		int resTop = topWheel.isInCollision(o);

		
		if (resBottom > 0 || resTop > 0) {
			gameStop();
			gameTimer.setStatus(ObstacleQueueStatus.FINISHED);
			gameState = GameState.FINISHED;
			if (resBottom == 1) oc.addCollisionView(new Point(wheel.getCenterBall1().getX() - oX,
					wheel.getCenterBall1().getY() - oY), Color.RED);
			else if (resBottom == 2) oc.addCollisionView(new Point(wheel.getCenterBall2().getX() - oX,
					wheel.getCenterBall2().getY() - oY), Color.BLUE);
			else if (resBottom == 3) {
				oc.addCollisionView(new Point(wheel.getCenterBall1().getX() - oX,
						wheel.getCenterBall1().getY() - oY), Color.RED);
				oc.addCollisionView(new Point(wheel.getCenterBall2().getX() - oX,
						wheel.getCenterBall2().getY() - oY), Color.BLUE);
			}
			
			if (resTop == 1) oc.addCollisionView(new Point(topWheel.getCenterBall1().getX() - oX,
					topWheel.getCenterBall1().getY() - oY), Color.RED);
			else if (resTop == 2) oc.addCollisionView(new Point(topWheel.getCenterBall2().getX() - oX,
					topWheel.getCenterBall2().getY() - oY), Color.BLUE);
			else if (resTop == 3) {
				oc.addCollisionView(new Point(topWheel.getCenterBall1().getX() - oX,
						topWheel.getCenterBall1().getY() - oY), Color.RED);
				oc.addCollisionView(new Point(topWheel.getCenterBall2().getX() - oX,
						topWheel.getCenterBall2().getY() - oY), Color.BLUE);
			}
			
			controller.lost();
		}
	}
	
	@Override
	protected void obstacleReached(Obstacle o) {
		if (!o.getReached()) {
			boolean reach = false;
			for (Point p : o.getPoints()) {
				if (p.getY() > wheel.getCenter().getY() + wheel.getRadius() ||
						p.getY() > topWheel.getCenter().getY() - topWheel.getRadius()) {
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
				if (p.getY() > height || p.getY() < 0) {
					visible = false;
				}
				else {
					return;
				}
			}
			if (!visible) {
				removeObstacle(o);
				removeTopObstacle(o);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Obstacle> getTopObstacles() {
		return (ArrayList<Obstacle>) topObstacles.clone();
	}
	
	public Wheel getTopWheel() {
		return topWheel;
	}
		
	public void addTopObstacle(Obstacle o) {
		o.setDirection(Direction.TOP);
		topObstacles.add(o);
	}
	
	public void removeTopObstacle(Obstacle o) {
		topObstacles.remove(o);
	}

}
