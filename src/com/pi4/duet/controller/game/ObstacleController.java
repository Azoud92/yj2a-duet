package com.pi4.duet.controller.game;

import java.awt.Color;

import com.pi4.duet.Point;
import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.view.game.ObstacleView;

public class ObstacleController {

	private Obstacle model;
	private ObstacleView view;
	
	public ObstacleController() {}
	

	public Obstacle getModel() {
		return model;
	}
	
	public ObstacleView getView() {
		return view;
	}
	
	public void paint() {
		view.paintComponents(view.getGraphics());
	}

	public void setModel(Obstacle model) { this.model = model; }
	public void setView(ObstacleView view) { this.view = view; }

	public void updateView(Point[] points) {
		view.updatePosition(points);
	}

	public void addCollisionView(Point point, Color color) {		
		view.addCollision(model.getAngle(), point, color);
	}

	public double getAngle() {
		// TODO Auto-generated method stub
		return model.getAngle();
	}
	
	public Point getCenter() {
		// TODO Auto-generated method stub
		return model.getCenter().clone();
	}

}
