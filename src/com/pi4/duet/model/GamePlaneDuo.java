package com.pi4.duet.model;

import java.util.ArrayList;

import com.pi4.duet.controller.GameDuoController;


public class GamePlaneDuo { // Représente le modèle du jeu : coordonnées du volant, de la balle, état de la partie, liste des obstacles...

	public final int width, height;
	
	private GameDuoController controller;
	private Wheel wheelD, wheelG;
	private ArrayList<Obstacle> obstacles;
	
	private State gameState = State.READY;
	
	private Direction wheelRotatingG = null,  wheelRotatingD = null;
	private Direction lastRotationG = null, lastRotationD = null;
	private boolean wheelBreakingG = false, wheelBreakingD = false;
		
	public GamePlaneDuo(int width, int height, GameDuoController controller) {		
		this.width = width;
		this.height = height;		
        this.wheelD = new Wheel(new Point(width / 4 * 3, height - 150));
        wheelD.setAngle(wheelD.getAngle()/2);
        this.wheelG = new Wheel(new Point(width / 4, height - 150));
        wheelG.setAngle(wheelG.getAngle()/2);
        this.controller = controller;

        this.obstacles = new ArrayList<Obstacle>();
	}

	public void startWheelRotation(Side side, Direction dir) {
		if(side == Side.LEFT) wheelRotatingG = dir;
		else if(side == Side.RIGHT) wheelRotatingD = dir;
	}
	
	/*public void resetObstacles() { // on remplace tous les obstacles à leur position initiale
		for (Obstacle o : obstacles) {
			o.update(0, - (o.getCoords()[0].getY() / o.getVelocity()));
			controller.refreshView();
		}		
	}*/
	
	public void stopWheelRotation(Side side) {
		if(side == Side.LEFT) {
			if (wheelRotatingG != null) lastRotationG = wheelRotatingG;
			wheelRotatingG = null;
		}
		else if(side == Side.RIGHT) {
			if (wheelRotatingD != null) lastRotationD = wheelRotatingD;
			wheelRotatingD = null;
		}
	}

	public void startWheelBreaking(Side side) {
		if(side == Side.LEFT) wheelBreakingG = true;
		else if(side == Side.RIGHT) wheelBreakingD = true;
	}
	
	public void stopWheelBreaking(Side side) {
		if(side == Side.LEFT) wheelBreakingG = false; 
		else if(side == Side.RIGHT) wheelBreakingD = false; 
		}
	
	public void addObstacle(Obstacle o) {
		this.obstacles.add(o);
	}
	
	public void removeObstacle(Obstacle o) {
		this.obstacles.remove(o);
	}
		
	public Wheel getWheel(Side side) {
		if(side == Side.LEFT)return wheelG; 
		return wheelD;
	}
	
	public State getState() { return gameState; }
	
	public void setState(State s) {
		this.gameState = s;
	}
	
	public Direction getWheelRotating(Side side) {
		if(side == Side.LEFT)return wheelRotatingG;
		return wheelRotatingD;
	}
	
	public void setWheelRotating(Side side, Direction wheelRotating) {
		if(side == Side.LEFT) wheelRotatingG = wheelRotating;
		else if(side == Side.RIGHT) wheelRotatingD = wheelRotating;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Obstacle> getObstacles() {
		return (ArrayList<Obstacle>) obstacles.clone();
	}

	public boolean getWheelBreaking(Side side) {
		if(side == Side.LEFT)return wheelBreakingG;
		return wheelBreakingD;
	}
	
	public Direction getLastRotation(Side side) {
		if(side == Side.LEFT) return lastRotationG;
		return lastRotationD;
		}

	public void setLastRotation(Side side, Direction dir) {
		if(side == Side.LEFT) lastRotationG = dir;
		else if(side == Side.RIGHT) lastRotationD = dir;
	}
		
}
