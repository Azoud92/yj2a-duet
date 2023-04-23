package com.pi4.duet.model.game.data;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.controller.game.GameController;
import com.pi4.duet.model.game.GameState;
import com.pi4.duet.model.game.Obstacle;

// Classe chargée de gérer la livraison des obstacles et le Timer d'animation du jeu
public class ObstacleQueue extends Timer {

	// Lien vers un GamePlane pour faire apparaitre les obstacles
	private final GameController controller;
	private static ObstacleQueueStatus status = ObstacleQueueStatus.WAITING;
	private Scale scale;
	private static int time = 0;
	private static int add = 1;
	private PatternData data;

	private static int nbObstacles = 0;

	public ObstacleQueue(GameController gameController, Scale scale) {
		controller = gameController;
		this.scale = scale;
		time = 0;
		add = 1;
		nbObstacles = 0;
	}

	public ObstacleQueue(GameController c, Scale scale, PatternData data) {
		this(c, scale);
		this.data = data;
				
		this.schedule(new TimerTask() {
			@Override
			public void run() {
				//if (status == ObstacleQueueStatus.FINISHED) this.cancel();
				if (controller.getState() == GameState.ON_GAME) {
					putObs();
					controller.updateGame();
					time += add * 1;
				}
				
			}
		}, 0, 1);
	}


	public ObstacleQueue(GameController c, Scale scale, String path) throws IOException, ClassNotFoundException {
		this(c, scale, PatternData.read(path));
	}

	@Override
	public void cancel() {
		super.cancel();
		status = ObstacleQueueStatus.FINISHED;
	}

	public void fall() {
		add = 10;
	}

	public void stopFall() {
		add = 1;
	}

	protected void putObs() {
		int i = 0;

		Iterator<Map.Entry<Obstacle, Long>> iter = data.entrySet().iterator();

		while (iter.hasNext()) {
			Entry<Obstacle, Long> entry = iter.next();
			
			if(entry.getValue()>time) return;

			if (data.size() == 1 || i == data.size() - 1) {
				if(time >= entry.getValue()) {
					addObstacle(entry.getKey());
					iter.remove();
					ObstacleQueue.status = ObstacleQueueStatus.FINISHED;
				}
			}

			else {
				if(time >= entry.getValue()) {
					nbObstacles++;
					addObstacle(entry.getKey());
					iter.remove();
					ObstacleQueue.status = ObstacleQueueStatus.DELIVERY_IN_PROGRESS;
				}

			}
			i++;
		}
	}

	private void addObstacle(Obstacle o) {
		int id = nbObstacles;
		nbObstacles++;		
		
		Thread obstacleCreation = new Thread() {
			@Override
			public void run() {				
				// On met l'obstacle aux normes quand à l'échelle d'affichage
				for (Point p : o.getPoints()) {
					p.setX(p.getX() * scale.getScaleX());
					p.setY(p.getY() * scale.getScaleY());
				}
				o.getCenter().setX(o.getCenter().getX() * scale.getScaleX());
				o.getCenter().setY(o.getCenter().getY() * scale.getScaleY());
				controller.addObstacle(o, id);
			}
		};
		obstacleCreation.start();
	}

	public void setStatus(ObstacleQueueStatus status) { ObstacleQueue.status = status; }
	public ObstacleQueueStatus getStatus() { return status; }

	public int getAdd() {
		// TODO Auto-generated method stub
		return add;
	}

}
