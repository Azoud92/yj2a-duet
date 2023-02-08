package model;

import java.io.Serializable;

public class Obstacle implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1057503017254395944L;
	
	private int width, height;
	private Point position; // représ. la position du centre du rectangle
	private double velocity, rotationSpeed, angle = 0;
	
	public Obstacle() {
		// A FAIRE : Initialiser les différents composants ci-dessus
		// ...
	}
	
	public void update() {
		// A FAIRE : implémenter la méthode pour mettre à j. le pt. référence & l'angle selon vélocité & vit. rotation
		// ...
	}
	
	public int getWidth() { return width; }
	public void setWidth(int w) { width = w; }
	
	public int getHeight() { return height; }
	public void setHeight(int h) { height = h; }
	
	public Point getPosition() { return position; }
	public void setPosition(Point p) { position = p; }
	
	public double getVelocity() { return velocity; }
	public void setVelocity(double v) { velocity = v; }
	
	public double getRotationSpeed() { return rotationSpeed; }
	public void setRotationSpeed(double rs) { rotationSpeed = rs; }
	
	public double getAngle() { return angle; }
	public void setAngle(double a) { angle = a; }
	
	
}
