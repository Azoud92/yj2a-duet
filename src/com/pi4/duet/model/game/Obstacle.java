package com.pi4.duet.model.game;

import java.io.Serializable;

import com.pi4.duet.Point;
import com.pi4.duet.controller.game.ObstacleController;

public class Obstacle implements Serializable, Cloneable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6766058339472244614L;

	private Point[] points; // les coordonnées de l'obstacle
	private Point center;
	private transient boolean reached = false; // détermine si un obstacle a franchi l'écran (et est invisible)

	private transient ObstacleController controller;

	private double velocity = 0.1, rotationSpeed = 0.1; // la vitesse d'animation, et la vitesse de rotation (s'il y a lieu)
	private double angle = 0; // l'angle actuel de l'obstacle (en radians)

	public Obstacle(Point points[], Point center, ObstacleController controller) {
		this.points = new Point[points.length];
		for (int i = 0; i < points.length; i++) {
			this.points[i] = points[i].clone();
		}
		this.center = center.clone();
		this.controller = controller;
	}

	public Obstacle(Point points[], Point center, double velocity, double rotationSpeed, double angle, ObstacleController controller) {
		this(points, center, controller);
		this.velocity = velocity;
		this.rotationSpeed = rotationSpeed;
		this.angle = angle;
	}

	public void setController(ObstacleController controller) { this.controller = controller; }

	@Override
	public Obstacle clone() {
		ObstacleController oc = new ObstacleController();
		Obstacle o = new Obstacle(this.points, this.center, this.velocity, this.rotationSpeed, this.angle, this.controller);
		oc.setModel(o);
		return o;
	}

	public void updatePosition(Direction dir) {
		for (Point p : points) { // mise à jour de chaque point de l'obstacle
			switch(dir) {
			case BOTTOM:
				p.setY(p.getY() + velocity);
				break;
			case TOP:
				p.setY(p.getY() - velocity);
				break;
			case LEFT:
				p.setX(p.getX() - velocity);
				break;
			case RIGHT:
				p.setX(p.getX() + velocity);
				break;
			}
		}

		switch(dir) { // mise à jour du centre de l'obstacle
		case BOTTOM:
			center.setY(center.getY() + velocity);
			break;
		case TOP:
			center.setY(center.getY() - velocity);
			break;
		case LEFT:
			center.setX(center.getX() - velocity);
			break;
		case RIGHT:
			center.setX(center.getX() + velocity);
			break;
		}

		if (rotationSpeed != 0) {
			rotate();
		}

		controller.update();
	}

	private void rotate() {
		angle += Math.toRadians(rotationSpeed); // ce paramètre ne nous sert pas dans le calcul des nouvelles coordonnées car sinon les rotations seraient exponentielles

		for (Point p : points) {
			double newX = center.getX() + (p.getX() - center.getX()) * Math.cos(Math.toRadians(rotationSpeed)) - (p.getY() - center.getY()) * Math.sin(Math.toRadians(rotationSpeed));
			double newY = center.getY() + (p.getX() - center.getX()) * Math.sin(Math.toRadians(rotationSpeed)) + (p.getY() - center.getY()) * Math.cos(Math.toRadians(rotationSpeed));

			p.setX(newX);
			p.setY(newY);
		}
	}

	public double getAngle() {
		// TODO Auto-generated method stub
		return angle;
	}

	public Point[] getPoints() {
		// TODO Auto-generated method stub
		return points;
	}

	public Point getCenter() {
		// TODO Auto-generated method stub
		return center;
	}

	public boolean getReached() {
		// TODO Auto-generated method stub
		return reached;
	}

	public void setReachedTrue() {
		// TODO Auto-generated method stub
		this.reached = true;
	}

	public ObstacleController getController() {
		// TODO Auto-generated method stub
		return controller;
	}

	public double getVelocity() {
		// TODO Auto-generated method stub
		return velocity;
	}

	/*
	@Override
	public String toString() {
		String res = "[";
		for (int i = 0 ; i < coords.length ; i++) {
			res += coords[i];
			if (i < coords.length - 1) res += " ";
		}
		res += "];" + center + ";" + velocity + ";"
				+ rotationSpeed + ";" + angle;
		return res;
	}*/

}
