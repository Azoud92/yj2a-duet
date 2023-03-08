package com.pi4.duet.model;

import java.util.ArrayList;
import com.pi4.duet.controller.GameController;

public class GamePlane { // Représente le modèle du jeu : coordonnées du volant, de la balle, état de la partie, liste des obstacles...

	public final int width, height;
	
	private GameController controller;
	private Wheel wheel;
	private ArrayList<Obstacle> obstacles;
	
	private State gameState = State.READY;
	
	private Direction wheelRotating = null;
	private boolean wheelBreaking = false;
		
	public GamePlane(int width, int height, GameController controller) {		
		this.width = width;
		this.height = height;		
        this.wheel = new Wheel(new Point(width / 2, height - 150));
        this.controller = controller;

        this.obstacles = new ArrayList<Obstacle>();
	}

	public void startWheelRotation(Direction dir) { 
		wheelRotating = dir;
	}
	
	public void resetObstacles() { // on remplace tous les obstacles à leur position initiale
		for (Obstacle o : obstacles) {
			o.update(0, - (o.getCoords()[0].getY() / o.getVelocity()));
			controller.refreshView();
		}		
	}
	
	public void stopWheelRotation() {
		wheelRotating = null;
	}

	public void startWheelBreaking() {
		wheelBreaking = true;
	}
	
	public void stopWheelBreaking() { wheelBreaking = false; }
	
	public void addObstacle(Obstacle o) {
		this.obstacles.add(o);
	}
	
	public void removeObstacle(Obstacle o) {
		this.obstacles.remove(o);
	}
		
	public Wheel getWheel() { return wheel; }
	
	public State getState() { return gameState; }
	
	public void setState(State s) {
		this.gameState = s;
	}
	
	public Direction getWheelRotating() {
		return wheelRotating;
	}
	
	public void setWheelRotating(Direction wheelRotating) {
		this.wheelRotating = wheelRotating;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Obstacle> getObstacles() {
		return (ArrayList<Obstacle>) obstacles.clone();
	}

	public boolean getWheelBreaking() {
		// TODO Auto-generated method stub
		return wheelBreaking;
	}
		
}
