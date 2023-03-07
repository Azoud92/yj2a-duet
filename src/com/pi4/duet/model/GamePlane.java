package com.pi4.duet.model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.pi4.duet.controller.GameController;

public class GamePlane {

	public final int width, height;
	
	private GameController controller;
	private ObstacleQueue gameTimer = new ObstacleQueue(this);
	private Timer wheelTimer;
	
	public final Wheel wheel;
	private ArrayList<Obstacle> obstacles;

	public boolean isPaused() {
		return paused;
	}

	private boolean paused = false;
	
	private boolean wheelRotatingAH = false; // rotation anti-horaire du volant en cours
	private boolean wheelRotatingH = false; // rotation horaire du volant en cours
		
	public GamePlane(int width, int height, GameController controller) {		
		this.width = width;
		this.height = height;		
        this.wheel = new Wheel(new Point(width / 2, height - 150));
        this.controller = controller;
        this.obstacles = new ArrayList<Obstacle>();
       
        
	}
	
	public void gameStart() {
		gameTimer = new ObstacleQueue(this);
		gameTimer.schedule(new TimerTask() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (!paused){
					for (Obstacle o : obstacles) {
						o.update(0, 1);
						controller.verifyCollision(o);
						controller.refreshView();
					}
				}
			}			
		}, 0, 1);
		
		wheelTimer = new Timer();
		wheelTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (wheelRotatingAH && !wheelRotatingH) {
					wheel.rotate(Direction.ANTI_HORAIRE);
					controller.updateWheel(wheel.getCenterBall2(), wheel.getCenterBall1());
					controller.updateMvt(Direction.ANTI_HORAIRE);
				}
				else if (!wheelRotatingAH && wheelRotatingH) {
					wheel.rotate(Direction.HORAIRE);
					controller.updateWheel(wheel.getCenterBall2(), wheel.getCenterBall1());
					controller.updateMvt(Direction.HORAIRE);
				}
				else {
					controller.stopMvt();
				}
			}
			
		}, 0, 1);
	}
	
	public void gameStop() {
		wheelTimer.cancel();
		gameTimer.cancel();
		
	}
	
	public void startWheelRotation(Direction dir) { 
		switch(dir) {
		case HORAIRE:
			wheelRotatingAH = false;
			wheelRotatingH = true;
			break;
		case ANTI_HORAIRE:
			wheelRotatingH = false;
			wheelRotatingAH = true;
			break;
		}
	}
	
	public void resetObstacle() {
		for (Obstacle o : obstacles) {
			o.update(0, - (o.getCoords()[0].getY() / o.getVelocity()));
			controller.refreshView();
		}
		
	}
	
	public void stopWheelRotation() { wheelRotatingAH = false; wheelRotatingH = false; }
	
	public void addObstacle(Obstacle o) {
		this.obstacles.add(o);
	}
	
	public void addObstacleTestDelay(Obstacle o, long delay) {
		this.gameTimer.putObstacle(o, delay);
	}
	
	public void addPattern(PatternData d) {
		gameTimer.putPattern(d);
	}
	
	public void removeObstacle(Obstacle o) {
		this.obstacles.remove(o);
	}
	public void gamePausedOrResumed(){
		paused=!paused;
	}
	
	public GameController getController() { return this.controller; }
	

	
		
}
