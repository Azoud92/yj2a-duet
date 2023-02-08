package model;

public class Wheel {
	
	private int radius;
	private Point center;
	
	public final Ball ball_1, ball_2;
	
	private double angle = 0;
	private double rotationSpeed;
	
	public Wheel() {
		// A FAIRE : Initialiser les différents composants ci-dessus
		// ...
	}
	
	public int getRadius() { return radius; }
	public void setRadius(int r) { radius = r; }
	
	public Point getCenter() { return center; }
	public void setCenter(Point c) { center = c; }
	
	public double getAngle() { return angle; }
	
	public double getRotationSpeed() { return rotationSpeed; }
	public void setRotationSpeed(double rs) { rotationSpeed = rs; }
	
	public void rotate() {
		// A FAIRE : implémenter la méthode
		// ...
	}		
	
	private class Ball {
		
		int radius;
		Point center;
		
		public Ball() {
			// A FAIRE : Initialiser les différents composants ci-dessus
			// ...
		}
		
		int getRadius() { return radius; }
		void setRadius(int r) { radius = r; }
		
		Point getCenter() { return center; }
		void setCenter(Point c) { center = c; }
		
		boolean isInCollision(Obstacle o) {
			return false;
			// A FAIRE : implémenter la méthode
			// ...
		}
		
	}
	
	
}
