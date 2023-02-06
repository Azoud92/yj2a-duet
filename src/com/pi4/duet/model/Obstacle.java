package com.pi4.duet.model;

public class Obstacle {
	
	private int width, height; 
	private Point[] coord; // représ. les 4 points (haut gauche, haut droit, bas droit, bas gauche) du rectangle formé
	private double velocity, rotationSpeed, angle = 0;
	
	public Obstacle(int width, int height, Point pos, double velocity, double rotationSpeed, double angle) {
		this.width = width;
		this.height = height;
		coord[0] = pos; // position du Rectangle (en haut à gauche)
		coord[1] = new Point(coord[0].getX() + width, coord[0].getY());
		coord[2] = new Point(coord[0].getX() + width, coord[0].getY() + height);
		coord[3] = new Point(coord[0].getX(), coord[0].getY() + height);
		
		this.velocity = velocity;
		this.rotationSpeed = rotationSpeed;
		this.angle = angle;
		rotate(angle);
	}
	
	public void update(int a) { // chaque appel de cette fonction par le Timer fera descendre l'obstacle et lui appliquera une rotation selon les vitesses définies
		setPosition(0, velocity * 1);
		if (a != 0) rotate(a * rotationSpeed);
	}
	
	private void setPosition(double xN, double yN) { // sert à faire bouger le rectangle du nombre de pixels souhaité par rapport à sa position actuelle
		for (Point p : coord) {
			p.setX(p.getX() + xN);
			p.setY(p.getY() + yN);
		}
	}
	
	private void rotate(double a) { // la rotation se fait par rapport au centre de la figure
		angle = a;
		double angleRadians = Math.toRadians(a);
		
		for (Point p : coord) {
			p.setX((p.getX() * Math.cos(angleRadians) - p.getY() * Math.sin(angleRadians)));
			p.setY((p.getX() * Math.sin(angleRadians) + p.getY() * Math.cos(angleRadians)));
		}
	}
	
	public int getWidth() { return width; }
	public void setWidth(int w) { width = w; }
	
	public int getHeight() { return height; }
	public void setHeight(int h) { height = h; }
	
	public Point[] getCoords() { return coord; }
		
	public double getVelocity() { return velocity; }
	public void setVelocity(double v) { velocity = v; }
	
	public double getRotationSpeed() { return rotationSpeed; }
	public void setRotationSpeed(double rs) { rotationSpeed = rs; }
	
	public double getAngle() { return angle; }
	public void setAngle(double a) { angle = a; }
	
}
