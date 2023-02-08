package model;

import java.util.ArrayList;

import javax.swing.Timer;

public class GamePlane {

	public final Point p1, p2; // on peut aussi créer une classe Rectangle directement si on le souhaite
	public final Wheel wheel;
	private ArrayList<Obstacle> obstacles;
	private Timer timer;
	
	public GamePlane() {
		// A FAIRE : Initialiser les différents composants ci-dessus
		// ...
	}
	
	public void update() { // ajouter les param. nécessaires
		// A FAIRE : implémenter la méthode
		// ...
	}
	
	public void testApparitionObstacle() {
		// A FAIRE : implémenter la méthode
		// ...
	}
	
	public ArrayList<Obstacle> getObstacles() { return obstacles; }
	
}
