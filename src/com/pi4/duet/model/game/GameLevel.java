package com.pi4.duet.model.game;

import com.pi4.duet.Point;
import com.pi4.duet.controller.game.GameLevelController;

// Modèle d'un niveau de jeu classique
public class GameLevel extends Game {

	public final int numLevel;
	public GameLevel(int width, int height, Point coordsWheel, int numLevel, GameLevelController controller) {
		super(width, height, coordsWheel, controller);
		this.numLevel = numLevel;
	}

}
