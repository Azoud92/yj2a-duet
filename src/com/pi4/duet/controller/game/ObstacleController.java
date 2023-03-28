package com.pi4.duet.controller.game;

import java.awt.Color;

import com.pi4.duet.Point;
import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.view.game.ObstacleView;

public class ObstacleController {

	private Obstacle model;
	private ObstacleView view;

	public void setModel(Obstacle model) { this.model = model; }
	public void setView(ObstacleView view) { this.view = view; }

	public void update() {
		view.updatePosition();
	}

	public void addCollisionView(Point point, Color color) {		
		view.addCollision(model.getAngle(), point, color);
	}

	public double getAngle() {
		// TODO Auto-generated method stub
		return model.getAngle();
	}

	public Point[] getPoints() {
		// TODO Auto-generated method stub
		Point[] modelPoints = model.getPoints();
		Point[] res = new Point[modelPoints.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = modelPoints[i].clone();
		}
		return res;
	}

	public double getVelocity() {
		// TODO Auto-generated method stub
		return model.getVelocity();
	}
	
	public Point getCenter() {
		// TODO Auto-generated method stub
		return model.getCenter().clone();
	}

}
