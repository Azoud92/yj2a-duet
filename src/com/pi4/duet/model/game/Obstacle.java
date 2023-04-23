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
	private transient boolean reached = false; // détermine si un obstacle a franchi l'écran
	private Direction direction;

	private transient ObstacleController controller;

	private double velocity = 0.1, rotationSpeed = 0; // la vitesse d'animation, et la vitesse de rotation (s'il y a lieu)
	private double angle = 0; // l'angle de départ de l'obstacle (en radians)

	public Obstacle(Point points[], Point center, Direction dir, ObstacleController controller) {
		this.points = new Point[points.length];
		for (int i = 0; i < points.length; i++) {
			this.points[i] = points[i].clone();
		}
		this.center = center.clone();
		this.controller = controller;
		this.direction = dir;
	}

	public Obstacle(Point points[], Point center, double velocity, double rotationSpeed, double angle, Direction dir, ObstacleController controller) {
		this(points, center, dir, controller);
		this.velocity = velocity;
		this.rotationSpeed = rotationSpeed;
		this.angle = angle;
	}

	public void setController(ObstacleController controller) { this.controller = controller; }

	@Override
	public Obstacle clone() {
		ObstacleController oc = new ObstacleController();
		Obstacle o = new Obstacle(this.points, this.center, this.velocity, this.rotationSpeed, this.angle, this.direction, this.controller);
		oc.setModel(o);
		return o;
	}
	
	public void updatePosition(int add) {
		for (Point p : points) { // mise à jour de chaque point de l'obstacle
			switch(this.direction) {
			case BOTTOM:
				p.setY(p.getY() + velocity * add);
				break;
			case TOP:
				p.setY(p.getY() - velocity * add);
				break;
			case LEFT:
				p.setX(p.getX() - velocity * add);
				break;
			case RIGHT:
				p.setX(p.getX() + velocity * add);
				break;
			}
		}

		switch(this.direction) { // mise à jour du centre de l'obstacle
		case BOTTOM:
			center.setY(center.getY() + velocity * add);
			break;
		case TOP:
			center.setY(center.getY() - velocity * add);
			break;
		case LEFT:
			center.setX(center.getX() - velocity * add);
			break;
		case RIGHT:
			center.setX(center.getX() + velocity * add);
			break;
		}

		if (rotationSpeed != 0) {
			rotate();
		}

		controller.updateView(points);
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

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public double getRotationSpeed() {
		return rotationSpeed;
	}

	@Override
	public String toString() {
		String res = "[";
		for (int i = 0 ; i < points.length ; i++) {
			res += points[i];
			if (i < points.length - 1) res += " ";
		}
		res += "];" + center + ";" + velocity + ";"
				+ rotationSpeed + ";" + angle;
		return res;
	}

}
