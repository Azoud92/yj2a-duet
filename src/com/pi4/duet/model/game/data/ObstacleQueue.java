package com.pi4.duet.model.game.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
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
	private static int time = 0;
	private static int add = 1;
	private PatternData data;

	private List<Entry<Obstacle, Long>> sortedEntries;


	public ObstacleQueue(GameController gameController, Scale scale) {
		controller = gameController;
		this.scale = scale;
		time = 0;
		add = 1;		
	}

	public ObstacleQueue(GameController c, Scale scale, PatternData data) {
		this(c, scale);
		this.data = data;

		sortedEntries = new ArrayList<>(data.entrySet());
		Collections.sort(sortedEntries, Entry.comparingByValue());

		this.schedule(new TimerTask() {
			@Override
			public void run() {
				if (status == ObstacleQueueStatus.FINISHED) this.cancel();
				putObs(time);
				time += add;
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
		add =10;
	}

	public void stopFall() {add = 1;}

	protected void putObs(int time) {
		int i = 0;

		Iterator<Entry<Obstacle, Long>> iter = sortedEntries.iterator();

		while (iter.hasNext()) {
			Entry<Obstacle, Long> entry = iter.next();
			if(entry.getValue()>time) return;

			if (sortedEntries.size() == 1 || i == sortedEntries.size() - 1) {
				if(time >= entry.getValue()) {
					addObstacle(entry.getKey());
					data.remove(entry.getKey());
					iter.remove();
					ObstacleQueue.status = ObstacleQueueStatus.FINISHED;
					System.out.println(time);
				}
			}

			else {
				if(time >= entry.getValue()) {
					addObstacle(entry.getKey());
					data.remove(entry.getKey());
					iter.remove();
					ObstacleQueue.status = ObstacleQueueStatus.DELIVERY_IN_PROGRESS;
					System.out.println(time);
				}

			}
			i++;
		}
	}

	private void addObstacle(Obstacle o) {
		Thread obstacleCreation = new Thread() {
			@Override
			public void run() {
				// On met l'obstacle aux normes quand à l'échelle d'affichage
				for (Point p : o.getPoints()) {
					p.setX(p.getX() * scale.getScaleX());
					p.setY(p.getY() * scale.getScaleY());
					System.out.println(p.getX()+","+p.getY()+" ");
				}
				System.out.println();
				o.getCenter().setX(o.getCenter().getX() * scale.getScaleX());
				o.getCenter().setY(o.getCenter().getY() * scale.getScaleY());
				controller.addObstacle(o);
			}
		};
		obstacleCreation.start();
	}


	public void setStatus(ObstacleQueueStatus status) { ObstacleQueue.status = status; }
	public ObstacleQueueStatus getStatus() { return status; }

}
