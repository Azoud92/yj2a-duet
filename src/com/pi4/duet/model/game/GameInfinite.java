package com.pi4.duet.model.game;

import java.util.ArrayList;

import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.controller.game.GameInfiniteController;
import com.pi4.duet.model.game.data.InfiniteObstacleQueue;
import com.pi4.duet.model.game.data.ObstacleQueueStatus;
import com.pi4.duet.model.game.data.PatternData;

public class GameInfinite extends Game {

	public GameInfinite(int width, int height, Point coordsWheel, Scale scale, GameInfiniteController controller) {
		super(width, height, coordsWheel, scale, controller);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void gameStart() {
		if (gameState != GameState.READY) return;
		gameState = GameState.ON_GAME;
		gameTimer.setStatus(ObstacleQueueStatus.WAITING);

		controller.start();		

		gameTimer = new InfiniteObstacleQueue(this, scale);
	}

	@Override
	protected void updateObstacles() {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		ArrayList<Obstacle> copy = (ArrayList<Obstacle>) obstacles.clone();
		if (copy.size() > 0) {
			for (Obstacle o : copy) { // animation des obstacles pour les faire "tomber"
				o.updatePosition(gameTimer.getAdd());
				verifyCollision(o);
				obstacleReached(o);
				controller.refreshView();
			}
		}					
		else controller.refreshView();
	}
	
	@Override
	public void addPattern(PatternData d) { 
		gameTimer = new InfiniteObstacleQueue(this, scale, "levelDuo", d);
	}

}
