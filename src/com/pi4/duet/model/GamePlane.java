package com.pi4.duet.model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.pi4.duet.controller.GameController;

public class GamePlane {

	public final int width, height;
	
	private GameController controller;
	private Timer timer;
	
	public final Wheel wheel;
	private ArrayList<Obstacle> obstacles;
	
	private boolean wheelRotatingAH = false; // rotation anti-horaire du volant en cours
	private boolean wheelRotatingH = false; // rotation anti-horaire du volant en cours
		
	public GamePlane(int width, int height, GameController controller) {		
		this.width = width;
		this.height = height;		
        this.wheel = new Wheel(new Point(width / 2, height - 100));
        this.controller = controller;
        this.obstacles = new ArrayList<Obstacle>();
        timer = new Timer();
	}
	
	public void gameStart() {
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (wheelRotatingAH && !wheelRotatingH) {
					wheel.rotate(Direction.ANTI_HORAIRE);
					controller.updateWheel(wheel.getCenterBall2(), wheel.getCenterBall1());
				}
				else if (!wheelRotatingAH && wheelRotatingH) {
					wheel.rotate(Direction.HORAIRE);
					controller.updateWheel(wheel.getCenterBall2(), wheel.getCenterBall1());
				}
				else {
					stopWheelRotation();
				}
				
				for (Obstacle o : obstacles) {
					o.update(0, 1);
					controller.verifyCollision(o);
					controller.refreshView();
				}
			}
			
		}, 0, 1);
	}
	
	public void gameStop() {
		timer.cancel();
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
	
	public void stopWheelRotation() { wheelRotatingAH = false; wheelRotatingH = false; }
	
	public void addObstacle(Obstacle o) {
		this.obstacles.add(o);
	}
	
	public void removeObstacle(Obstacle o) {
		this.obstacles.remove(o);
	}
		
}
