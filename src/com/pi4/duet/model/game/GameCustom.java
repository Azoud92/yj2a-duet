package com.pi4.duet.model.game;

import java.util.ArrayList;

import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.controller.game.GameController;
import com.pi4.duet.controller.game.GameLevelController;
import com.pi4.duet.model.game.data.ObstacleQueueStatus;

public class GameCustom extends Game {
	
	public final String path;
	
	public GameCustom(int width, int height, Point coordsWheel, Scale scale, GameController controller, String path) {
		super(width, height, coordsWheel, scale, controller);
		this.path = path;
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
	public void updateGame() {
		super.updateGame();
		hasWin();
	}
	
	private void hasWin() {
		if (obstacles.size() == 0 && gameTimer.getStatus() == ObstacleQueueStatus.FINISHED) {
			gameStop();
			gameTimer.setStatus(ObstacleQueueStatus.FINISHED);
			gameState = GameState.FINISHED;			
			((GameLevelController) controller).win();
		}
	}
	
}
