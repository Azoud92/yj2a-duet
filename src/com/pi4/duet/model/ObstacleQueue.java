package model;

import java.util.Timer;
import java.util.TimerTask;
import java.io.Serializable;
import java.util.HashMap;

public class ObstacleQueue extends Timer {
	
	//Lien vers un GamePlane pour faire apparaitre les obstacles
	private GamePlane plane;
	
	public ObstacleQueue(GamePlane p) {
		plane = p;
	}
	
	

	public void putObstacle(Obstacle o, long delay) {	//delay est en millisecondes
		this.schedule(new TimerTask() {
			public void run() {
				plane.testApparitionObstacle();	//Le but est de faire apparaitre l'obstacle o dans le jeu ; placeholder temporaire
			}
		}, delay);
	}
	
}
