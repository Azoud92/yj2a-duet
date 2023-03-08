package com.pi4.duet.model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.pi4.duet.controller.GameController;

public class GamePlane {

	public final int width, height;
	
	private GameController controller;
	private Timer gameTimer;
	
	public final Wheel wheel;
	private ArrayList<Obstacle> obstacles;

	public boolean isPaused() {
		return paused;
	}

	private boolean paused = false;

	
	private boolean wheelRotatingAH = false; // rotation anti-horaire du volant en cours
	private boolean wheelRotatingH = false; // rotation horaire du volant en cours

	private boolean wheelFreinageAH = false;
	private static double i = 1;
	private boolean wheelFreinageH = false;
		
	public GamePlane(int width, int height, GameController controller) {		
		this.width = width;
		this.height = height;		
        this.wheel = new Wheel(new Point(width / 2, height - 150));
        this.controller = controller;
        this.obstacles = new ArrayList<Obstacle>();
       
        
	}
	
	public void gameStart() {
		gameTimer = new Timer();
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

					if (wheelRotatingAH && !wheelRotatingH && !wheelFreinageAH && !wheelFreinageH) {
						i=i+0.05;
						wheel.rotate(Direction.ANTI_HORAIRE,i);
						controller.updateWheel(wheel.getCenterBall2(), wheel.getCenterBall1());
						controller.updateMvt(Direction.ANTI_HORAIRE);
					}
					else if (!wheelRotatingAH && wheelRotatingH && !wheelFreinageAH && !wheelFreinageH) {
						i=i+0.05;
						wheel.rotate(Direction.HORAIRE,i);
						controller.updateWheel(wheel.getCenterBall2(), wheel.getCenterBall1());
						controller.updateMvt(Direction.HORAIRE);
					}else if(wheelFreinageAH && !wheelFreinageH){
						if(i>0){
							wheel.rotate(Direction.ANTI_HORAIRE,i);
							controller.updateWheel(wheel.getCenterBall2(), wheel.getCenterBall1());
							controller.updateMvt(Direction.ANTI_HORAIRE);
							i=i-0.2;
						}else{
							stopWheelFreinage();
							i=1;
						}

					}else if(!wheelFreinageAH && wheelFreinageH){
						if(i>0){
							wheel.rotate(Direction.HORAIRE,i);
							controller.updateWheel(wheel.getCenterBall2(), wheel.getCenterBall1());
							controller.updateMvt(Direction.HORAIRE);
							i=i-0.2;
						}else{
							stopWheelFreinage();
							i=1;
						}

					}
					else {
						controller.stopMvt();
					}

				}
			}			
		}, 0, 2);
	}
	
	public void gameStop() {
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


	public void startWheelFreinage(Direction dir) {
		switch(dir) {
			case HORAIRE:
				wheelFreinageAH = false;
				wheelFreinageH = true;
				break;
			case ANTI_HORAIRE:
				wheelFreinageH = false;
				wheelFreinageAH = true;
				break;
		}
	}
	public void stopWheelFreinage(){ wheelFreinageAH = false;  wheelFreinageH = false;}
	
	public void addObstacle(Obstacle o) {
		this.obstacles.add(o);
	}
	
	public void removeObstacle(Obstacle o) {
		this.obstacles.remove(o);
	}
	public void gamePausedOrResumed(){
		paused=!paused;
	}
	

	
		
}
