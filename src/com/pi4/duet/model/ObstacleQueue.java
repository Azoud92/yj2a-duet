package com.pi4.duet.model;

import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;

public class ObstacleQueue extends Timer {
	
	//Lien vers un GamePlane pour faire apparaitre les obstacles
	private final GamePlane plane;
	
	public ObstacleQueue(GamePlane p) {
		plane = p;
	}
	
	public ObstacleQueue(GamePlane p, PatternData data) {
		this(p);
		putPattern(data);
	}
	
	public ObstacleQueue(GamePlane p, String path) throws IOException, ClassNotFoundException {
		this(p, PatternData.read(path));
	}
	
	public void putObstacle(Obstacle o, long delay) {	//delay est en millisecondes
		this.schedule(new TimerTask() {
			public void run() {
				plane.testApparitionObstacle();	//Le but est de faire apparaitre l'obstacle o dans le jeu ; placeholder temporaire
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
