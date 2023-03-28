package com.pi4.duet.model.game;

import java.util.ArrayList;

import com.pi4.duet.Point;

// Modèle du jeu en Duo
public class GameDuo extends Game {

	private Wheel topWheel;
	private ArrayList<Obstacle> topObstacles = new ArrayList<>();

	public GameDuo(int width, int height, Point coordsBottomWheel, Point coordsTopWheel) {
		super(width, height, coordsBottomWheel);
		wheel.setRadius(wheel.getRadius() * 9 / 10);
		topWheel = new Wheel(coordsTopWheel);
		topWheel.setRadius(topWheel.getRadius() * 9 / 10);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Obstacle> getTopObstacles() { return (ArrayList<Obstacle>) topObstacles.clone(); }

	public void addTopObstacle(Obstacle o) {
		this.topObstacles.add(o);
	}

	public void removeTopObstacle(Obstacle o) {
		this.topObstacles.remove(o);
	}

	public Wheel getTopWheel() { return topWheel; }

}
