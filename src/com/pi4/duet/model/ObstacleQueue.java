package com.pi4.duet.model;

import java.util.Timer;
import java.util.TimerTask;

import com.pi4.duet.controller.ObstacleController;
import com.pi4.duet.controller.GameController;

import java.io.IOException;

public class ObstacleQueue extends Timer { // représente la liste avec les délais d'apparition des obstacles
	
	// Lien vers un GamePlane pour faire apparaitre les obstacles
	private final GameController controller;
	
	public ObstacleQueue(GameController c) {
		controller = c;
	}
	
	public ObstacleQueue(GamePlane p, PatternData data) {
		this(p);
		putPattern(data);
	}
	
	public ObstacleQueue(GamePlane p, String path) throws IOException, ClassNotFoundException {
		this(p, PatternData.read(path));
	}
	
	public void putObstacle(Obstacle o, long delay) { // delay est en millisecondes
		this.schedule(new TimerTask() {
			public void run() {
				ObstacleController oc = new ObstacleController();
				o.setController(oc);
				oc.setModel(o);
				ObstacleView ov = new ObstacleView(oc, (int) o.getWidth(), (int) o.getHeight(), (int) o.getPos().getX(), (int) o.getPos().getY(), plane.getController());
				oc.setView(ov);
				plane.addObstacle(o);
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
