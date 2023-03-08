package com.pi4.duet.model;

import java.io.Serializable;

import com.pi4.duet.controller.ObstacleController;

// Représente n'importe quel polygone à représenter
public class Obstacle implements Serializable {

	private static final long serialVersionUID = -7132766134333288736L;
	
	private Point[] coords; // représente les différents points de l'obstacle
	private Point center; // les coordonnées du centre de l'obstacle
	
	private ObstacleController controller;
	
	private double velocity = 0.1, rotationSpeed = 0.1, angle = 0;
		
	public Obstacle(Point[] points, Point center, ObstacleController controller) {
		coords = new Point[points.length];
		for (int i = 0; i < points.length ; i++) {
			coords[i] = points[i].clone();
		}
		
		this.center = center.clone();
		this.controller = controller;
		rotate();
	}
	
	public Obstacle(Point[] points, Point center, double velocity, double rotationSpeed, double angle, ObstacleController controller) {
		this(points, center, controller);
		this.velocity = velocity;
		this.rotationSpeed = rotationSpeed;
		this.angle = angle;
	}

	
	// Met à jour la pos. de l'obstacle pour un deltaX et deltaY données
	public void update(double deltaX, double deltaY) {
		for (Point p : coords) {
			p.setX(p.getX() + deltaX * velocity);
			p.setY(p.getY() + deltaY * velocity);
		
		}
		center.setX(center.getX() + deltaX * velocity);
		center.setY(center.getY() + deltaY * velocity);
		
		if (angle != 0) rotate(); // on met une condition pour éviter de faire des calculs inutiles
		
		controller.update();

		
	}
	
	// Effectue une rotation de l'obstacle par rapport à l'angle en degrés en argument
	public void rotate() {
		double angleRadians = Math.toRadians(angle * rotationSpeed);
		
		for (Point p : coords) {
			double newX = center.getX() + (p.getX() - center.getX()) * Math.cos(angleRadians) - (p.getY() - center.getY()) * Math.sin(angleRadians);
			double newY = center.getY() + (p.getX() - center.getX()) * Math.sin(angleRadians) + (p.getY() - center.getY()) * Math.cos(angleRadians);

			p.setX(newX);
			p.setY(newY);
		}
	}
	
	public String toString() {
		String res = "[";
		for (int i = 0 ; i < coords.length ; i++) {
			res += coords[i];
			if (i < coords.length - 1) res += " ";
		}
		res += "];" + center + ";" + velocity + ";"
				+ rotationSpeed + ";" + angle;
		return res;
	}
	
	public Point[] getCoords() { return coords; }
	
	public Point getCenter() { return center; }
		
	public double getVelocity() { return velocity; }
	public void setVelocity(double v) { velocity = v; }
	
	public double getRotationSpeed() { return rotationSpeed; }
	public void setRotationSpeed(double rs) { rotationSpeed = rs; }
	
	public double getAngle() { return angle; }
	public void setAngle(double a) { angle = a; }
	
	public double getWidth() {
		return (coords[1].getX() - coords[0].getX());
	}
	
	public double getHeight() {
		return (coords[3].getY() - coords[0].getY());
	}
	
	public Point getPos() {
		return coords[0];
	}

	public ObstacleController getController() {
		return controller;
	}

	public void setController(ObstacleController c) { controller = c; }

	
}
