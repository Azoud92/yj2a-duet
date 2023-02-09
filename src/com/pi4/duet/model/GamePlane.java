package model;

import java.util.ArrayList; 

import javax.swing.Timer;

import com.pi4.duet.controller.GameController;

public class GamePlane {

	public final Wheel wheel;
	private ArrayList<Obstacle> obstacles;
	private Timer timer;
	private GameController controller;
	
	public GamePlane(int width, int height, GameController controller) {
		// A FAIRE : Initialiser les différents composants ci-dessus
		// ...
		this.controller = controller;
		Point centreR = new Point(width / 2 - 50, height - 100);
        Point centreB = new Point(width / 2 + 50, height - 100);
        wheel = new Wheel(new Point(width / 2, height - 100));
        wheel.ball_1 = wheel.new Ball(centreR);
        wheel.ball_2 = wheel.new Ball(centreB);
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
