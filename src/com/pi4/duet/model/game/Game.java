package com.pi4.duet.model.game;

import java.util.ArrayList;

import com.pi4.duet.Point;
import com.pi4.duet.controller.game.GameController;

// Modèle général abstrait du jeu
public abstract class Game {

	public final int width, height;
	
	protected GameController controller;

	protected Wheel wheel;
	private ArrayList<Obstacle> obstacles = new ArrayList<>();
	private GameState gameState = GameState.READY;
	
	private double effectDelaySpeed = 0.01;
	private double progressionEffect = 0;
	private boolean canUseEffect = false;

	public Game(int width, int height, Point coordsWheel, GameController controller) {
		this.controller = controller;
		this.width = width;
		this.height = height;
		
		this.wheel = new Wheel(coordsWheel, width, controller.getWheelController(), controller.getSettings());
	}

	public final void addObstacle(Obstacle o) {
		this.obstacles.add(o);
	}

	public final void removeObstacle(Obstacle o) {
		this.obstacles.remove(o);
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

	public boolean getCanUseEffect() {
		return canUseEffect;
	}

	public void setCanUseEffect(boolean canUseEffect) {
		this.canUseEffect = canUseEffect;
	}
	
	public double getProgressionEffect() { return progressionEffect; }
	
	public void incrProgressionEffect() {		
		if (this.progressionEffect >= 100) {
			this.canUseEffect = true;
		}
		else this.progressionEffect += effectDelaySpeed;
	}
	
	public void useEffect() {
		this.progressionEffect = 0;
		this.canUseEffect = false;
	}

}
