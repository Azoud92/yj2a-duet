package com.pi4.duet.model.game;

import java.util.ArrayList;

import com.pi4.duet.Point;
import com.pi4.duet.controller.game.GameDuoController;
import com.pi4.duet.model.Direction;


public class GamePlaneDuo { // Représente le modèle du jeu : coordonnées du volant, de la balle, état de la partie, liste des obstacles...

	public final int width, height;
	
	private GameDuoController controller;
	private Wheel wheelB, wheelH;
	private ArrayList<Obstacle> obstaclesH, obstaclesB;
	
	private State gameState = State.READY;
	
	private Direction wheelRotatingH = null,  wheelRotatingB = null;
	private Direction lastRotationH = null, lastRotationB = null;
	private boolean wheelBreakingH = false, wheelBreakingB = false;
		
	public GamePlaneDuo(int width, int height, GameDuoController controller) {		
		this.width = width;
		this.height = height;		
        this.wheelB = new Wheel(new Point(width / 2, height - 135));
        wheelB.setBallRadius(wheelB.radius*9/10);
        this.wheelH = new Wheel(new Point(width / 2, 135));
        wheelH.setBallRadius(wheelH.radius*9/10);
        this.controller = controller;

        this.obstaclesH = new ArrayList<Obstacle>();
        this.obstaclesB = new ArrayList<Obstacle>();
	}

	public void startWheelRotation(Side side, Direction dir) {
		if(side == Side.HIGH) wheelRotatingH = dir;
		else if(side == Side.LOW) wheelRotatingB = dir;
	}
	
	/*public void resetObstacles() { // on remplace tous les obstacles à leur position initiale
		for (Obstacle o : obstacles) {
			o.update(0, - (o.getCoords()[0].getY() / o.getVelocity()));
			controller.refreshView();
		}		
	}*/
	
	public void stopWheelRotation(Side side) {
		if(side == Side.HIGH) {
			if (wheelRotatingH != null) lastRotationH = wheelRotatingH;
			wheelRotatingH = null;
		}
		else if(side == Side.LOW) {
			if (wheelRotatingB != null) lastRotationB = wheelRotatingB;
			wheelRotatingB = null;
		}
	}

	public void startWheelBreaking(Side side) {
		if(side == Side.HIGH) wheelBreakingH = true;
		else if(side == Side.LOW) wheelBreakingB = true;
	}
	
	public void stopWheelBreaking(Side side) {
		if(side == Side.HIGH) wheelBreakingH = false; 
		else if(side == Side.LOW) wheelBreakingB = false; 
		}
	
	public void addObstacle(Side side, Obstacle o) {
		if(side == Side.HIGH) this.obstaclesH.add(o);
		else if(side == Side.LOW) this.obstaclesB.add(o);
	}
	
	public void removeObstacle(Side side, Obstacle o) {
		if(side == Side.HIGH) this.obstaclesH.remove(o);
		else if(side == Side.LOW) this.obstaclesB.remove(o);
	}
		
	public Wheel getWheel(Side side) {
		if(side == Side.HIGH)return wheelH; 
		return wheelB;
	}
	
	public State getState() { return gameState; }
	
	public void setState(State s) {
		this.gameState = s;
	}
	
	public Direction getWheelRotating(Side side) {
		if(side == Side.HIGH)return wheelRotatingH;
		return wheelRotatingB;
	}
	
	public void setWheelRotating(Side side, Direction wheelRotating) {
		if(side == Side.HIGH) wheelRotatingH = wheelRotating;
		else if(side == Side.LOW) wheelRotatingB = wheelRotating;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Obstacle> getObstacles(Side side) {
		if(side == Side.HIGH) return (ArrayList<Obstacle>) obstaclesH.clone();
		return (ArrayList<Obstacle>) obstaclesB.clone();
	}

	public boolean getWheelBreaking(Side side) {
		if(side == Side.HIGH)return wheelBreakingH;
		return wheelBreakingB;
	}
	
	public Direction getLastRotation(Side side) {
		if(side == Side.HIGH) return lastRotationH;
		return lastRotationB;
		}

	public void setLastRotation(Side side, Direction dir) {
		if(side == Side.HIGH) lastRotationH = dir;
		else if(side == Side.LOW) lastRotationB = dir;
	}
		
}
