package com.pi4.duet.model.game;

import java.util.ArrayList;

import com.pi4.duet.Point;

// Modèle général abstrait du jeu
public abstract class Game {

	public final int width, height;

	protected Wheel wheel;
	private ArrayList<Obstacle> obstacles = new ArrayList<>();
	private GameState gameState = GameState.READY;

	public Game(int width, int height, Point coordsWheel) {
		this.width = width;
		this.height = height;
		this.wheel = new Wheel(coordsWheel);
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

}
