package com.pi4.duet.model.game.data;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.controller.game.GameController;
import com.pi4.duet.model.game.Obstacle;

public class ObstacleQueue extends Timer { // représente la liste avec les délais d'apparition des obstacles

	// Lien vers un GamePlane pour faire apparaitre les obstacles
	private final GameController controller;
	private static ObstacleQueueStatus status = ObstacleQueueStatus.WAITING;
	private Scale scale;

	public ObstacleQueue(GameController gameController, Scale scale) {
		controller = gameController;
		this.scale = scale;
	}

	public ObstacleQueue(GameController c, Scale scale, PatternData data) {
		this(c, scale);
		putPattern(data);
	}

	public ObstacleQueue(GameController c, Scale scale, String path) throws IOException, ClassNotFoundException {
		this(c, scale, PatternData.read(path));
	}

	public void putObstacle(Obstacle o, long delay) { // delay est en millisecondes
		this.schedule(new TimerTask() {
			@Override
			public void run() {
				addObstacle(o);
			}
		}, delay);
	}

	public void putObstacle(Obstacle o, long delay, ObstacleQueueStatus s) { // delay est en millisecondes
		this.schedule(new TimerTask() {
			@Override
			public void run() {
				addObstacle(o);
				status = s;
			}
		}, delay);
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

	public void putPattern(PatternData data) {
		int i = 0;
		
		for (Long delay : data.keySet()) {
			if (data.keySet().size() == 1 || i == data.keySet().size() - 1)	putObstacle(data.get(delay), delay, ObstacleQueueStatus.FINISHED);
			else putObstacle(data.get(delay), delay, ObstacleQueueStatus.DELIVERY_IN_PROGRESS);		
			i++;
		}
	}

	public void putPattern(String path) throws IOException, ClassNotFoundException {
		putPattern(PatternData.read(path));
	}

	public void setStatus(ObstacleQueueStatus status) { ObstacleQueue.status = status; }
	public ObstacleQueueStatus getStatus() { return status; }

}
