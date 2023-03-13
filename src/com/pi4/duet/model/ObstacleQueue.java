package com.pi4.duet.model;

import java.util.Timer;
import java.util.TimerTask;

import com.pi4.duet.controller.ObstacleController;
import com.pi4.duet.controller.GameController;
import com.pi4.duet.view.game.ObstacleView;

import java.io.IOException;

public class ObstacleQueue extends Timer { // représente la liste avec les délais d'apparition des obstacles
	
	// Lien vers un GamePlane pour faire apparaitre les obstacles
	private final GameController controller;
	
	public ObstacleQueue(GameController c) {
		controller = c;
	}
	
	public ObstacleQueue(GameController c, PatternData data) {
		this(c);
		putPattern(data);
	}
	
	public ObstacleQueue(GameController c, String path) throws IOException, ClassNotFoundException {
		this(c, PatternData.read(path));
	}
	
	public void putObstacle(Obstacle o, long delay) { // delay est en millisecondes
		this.schedule(new TimerTask() {
			public void run() {
				ObstacleController oc = new ObstacleController();
				o.setController(oc);
				oc.setModel(o);
				ObstacleView ov = new ObstacleView(oc, (int) o.getWidth(), (int) o.getHeight(), (int) o.getPos().getX(), (int) o.getPos().getY(), controller);
				oc.setView(ov);
				controller.addObstacle(o);
			}
		}, delay);
	}
	
	public void putPattern(PatternData data) {
		for (Long delay : data.keySet()) putObstacle(data.get(delay), delay);
	}
	
	public void putPattern(String path) throws IOException, ClassNotFoundException {
		putPattern(PatternData.read(path));
	}
	
}
