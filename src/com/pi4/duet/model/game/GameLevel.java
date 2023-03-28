package com.pi4.duet.model.game;

import com.pi4.duet.Point;

// Modèle d'un niveau de jeu classique
public class GameLevel extends Game {

	public final int numLevel;
	public GameLevel(int width, int height, Point coordsWheel, int numLevel) {
		super(width, height, coordsWheel);
		this.numLevel = numLevel;
	}

}
