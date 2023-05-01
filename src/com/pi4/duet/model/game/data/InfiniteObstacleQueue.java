package com.pi4.duet.model.game.data;

import java.io.IOException;
import java.util.TimerTask;

import com.pi4.duet.Scale;
import com.pi4.duet.model.game.Game;
import com.pi4.duet.model.game.GameState;

public class InfiniteObstacleQueue extends ObstacleQueue {

	public InfiniteObstacleQueue(Game game, Scale scale) {
		super(game, scale);
		// TODO Auto-generated constructor stub
	}
	
	public InfiniteObstacleQueue(Game game, Scale scale, PatternData data) {
		this(game, scale);		
		this.data = data;		
				
		this.schedule(new TimerTask() {
			@Override
			public void run() {
				if (model.getState() == GameState.FINISHED) this.cancel();
				if (model.getState() == GameState.ON_GAME) {					
					putObs();
					model.updateGame();
					time += add;
					if (time > 0 && time % 10000 == 0) { // implémentation de l'accélération progressive
						add += 1;
					}
					
					if (status == ObstacleQueueStatus.FINISHED) { // on recrée le niveau et on ajoute de l'accélération
						status = ObstacleQueueStatus.WAITING;
						time = -3000; // on laisse 5 secondes (de plus en plus petites avec l'accélération du temps) de pause entre le dernier obstacle et la nouvelle livraison
						
						try {
							InfiniteObstacleQueue.this.data = PatternData.read("src/resources/levels/levelInfinite.ser");
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}				
			}
		}, 0, 1);
	}

}
