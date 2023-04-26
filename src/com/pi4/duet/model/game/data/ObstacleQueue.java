package com.pi4.duet.model.game.data;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.model.game.Game;
import com.pi4.duet.model.game.GameState;
import com.pi4.duet.model.game.Obstacle;

// Classe chargée de gérer la livraison des obstacles et le Timer d'animation du jeu
public class ObstacleQueue extends Timer {

	// Lien vers un GamePlane pour faire apparaitre les obstacles
	// Les attributs statiques sont obligatoires en raison de la manipulation en temps réel de leur valeur
	private final Game model;
	private static ObstacleQueueStatus status = ObstacleQueueStatus.WAITING;
	private Scale scale;
	private static int time = 0; // le temps qui s'est écoulé
	private static int add = 1; // pour modéliser l'accélération du temps
	private PatternData data;

	private static int idsObstacles = 0; // pour calculer les ID. d'obstacles

	public ObstacleQueue(Game game, Scale scale) {
		this.model = game;
		this.scale = scale;
		status = ObstacleQueueStatus.WAITING;
		time = 0;
		add = 1;
		idsObstacles = 0;
	}

	public ObstacleQueue(Game game, Scale scale, PatternData data) {
		this(game, scale);
		this.data = data;
				
		this.schedule(new TimerTask() {
			@Override
			public void run() {
				if (model.getState() == GameState.FINISHED) this.cancel();
				if (model.getState() == GameState.ON_GAME) {					
					putObs();
					model.updateGame();
					time += add * 1;
				}				
			}
		}, 0, 1);
	}


	public ObstacleQueue(Game g, Scale scale, String path) throws IOException, ClassNotFoundException {
		this(g, scale, PatternData.read(path));
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
		Iterator<Map.Entry<Obstacle, Long>> iter = data.entrySet().iterator();

		while (iter.hasNext()) {
			Entry<Obstacle, Long> entry = iter.next();
			
			if (entry.getValue() > time) return;

			if (time >= entry.getValue()) {
				addObstacle(entry.getKey());
				iter.remove();
				if (data.size() == 0) {
					ObstacleQueue.status = ObstacleQueueStatus.FINISHED;
				}
				else ObstacleQueue.status = ObstacleQueueStatus.DELIVERY_IN_PROGRESS;
			}
		}
	}

	private void addObstacle(Obstacle o) {
		int id = idsObstacles;
		idsObstacles += 5;		
				
		for (Point p : o.getPoints()) {
			p.setX(p.getX() * scale.getScaleX());
			p.setY(p.getY() * scale.getScaleY());
		}
		o.getCenter().setX(o.getCenter().getX() * scale.getScaleX());
		o.getCenter().setY(o.getCenter().getY() * scale.getScaleY());
		model.initObstacle(o, id);
	}

	public void setStatus(ObstacleQueueStatus status) { ObstacleQueue.status = status; }
	public ObstacleQueueStatus getStatus() { return status; }

	public int getAdd() {
		// TODO Auto-generated method stub
		return add;
	}

}
