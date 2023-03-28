package com.pi4.duet.model.game;

import java.util.ArrayList; 

import com.pi4.duet.Point;
import com.pi4.duet.controller.game.GameController;

public class GamePlane { // Représente le modèle du jeu : coordonnées du volant, de la balle, état de la partie, liste des obstacles...

	public final int width, height;
	
	private Wheel wheel;
	private ArrayList<Obstacle> obstacles;
	public final int numLevel;
	private State gameState = State.READY;
			
	public GamePlane(int width, int height, GameController controller, int numLevel) {		
		this.width = width;
		this.height = height;		
        this.wheel = new Wheel(new Point(width / 2, height - 150));

        this.obstacles = new ArrayList<Obstacle>();
		this.numLevel = numLevel;
	}
	
		
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
		
	@SuppressWarnings("unchecked")
	public ArrayList<Obstacle> getObstacles() {
		return (ArrayList<Obstacle>) obstacles.clone();
	}	
}
