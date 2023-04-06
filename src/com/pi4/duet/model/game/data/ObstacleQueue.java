package com.pi4.duet.model.game.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.controller.game.GameController;
import com.pi4.duet.model.game.GameState;
import com.pi4.duet.model.game.Obstacle;

public class ObstacleQueue extends Timer { // représente la liste avec les délais d'apparition des obstacles

	// Lien vers un GamePlane pour faire apparaitre les obstacles
	private final GameController controller;
	private static ObstacleQueueStatus status = ObstacleQueueStatus.WAITING;
	private Scale scale;
	private int time = 0;

	public ObstacleQueue(GameController gameController, Scale scale) {
		controller = gameController;
		this.scale = scale;
	}

	public ObstacleQueue(GameController c, Scale scale, PatternData data) {
		this(c, scale);
		this.schedule(new TimerTask() {
			@Override
			public void run() {
				putObs(data, time);
				time++;
			}
		}, 0, 1);
	}


	public ObstacleQueue(GameController c, Scale scale, String path) throws IOException, ClassNotFoundException {
		this(c, scale, PatternData.read(path));
	}
	
	protected void putObs(PatternData data, int time) {
		int i = 0;
		List<Entry<Obstacle, Long>> sortedEntries = new ArrayList<>(data.entrySet());
		Collections.sort(sortedEntries, Entry.comparingByValue());
		
		for (Entry<Obstacle, Long> entry : sortedEntries) {
			if (sortedEntries.size() == 1 || i == sortedEntries.size() - 1) {
				if(time == entry.getValue()) {
					addObstacle(entry.getKey());
					ObstacleQueue.status = ObstacleQueueStatus.FINISHED;
					
				}
			}
			else {
				if(time == entry.getValue()) {
					addObstacle(entry.getKey());
					ObstacleQueue.status = ObstacleQueueStatus.DELIVERY_IN_PROGRESS;
					}	
			}
			i++;
		}
		
	}

	

	private void addObstacle(Obstacle o) {
		// On met l'obstacle aux normes quand à l'échelle d'affichage
		for (Point p : o.getPoints()) {
			p.setX(p.getX() * scale.getScaleX());
			p.setY(p.getY() * scale.getScaleY());
		}
		o.getCenter().setX(o.getCenter().getX() * scale.getScaleX());
		o.getCenter().setY(o.getCenter().getY() * scale.getScaleY());
		controller.addObstacle(o);
	}



	public void putPattern(String path) throws IOException, ClassNotFoundException {
		new ObstacleQueue(controller, scale, PatternData.read(path));
	}

	public void setStatus(ObstacleQueueStatus status) { ObstacleQueue.status = status; }
	public ObstacleQueueStatus getStatus() { return status; }

}
