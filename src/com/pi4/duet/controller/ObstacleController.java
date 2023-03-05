package com.pi4.duet.controller;

import java.io.Serializable;

import java.awt.Polygon;

import com.pi4.duet.model.Obstacle;
import com.pi4.duet.model.Point;
import com.pi4.duet.view.ObstacleView;

public class ObstacleController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7126796665402586980L;
	
	private Obstacle model;
	private ObstacleView view;
		
	public void setModel(Obstacle model) { this.model = model; }
	public Obstacle getModel() {
		return model;
	}
	
	public void setView(ObstacleView view) { this.view = view; }
	public ObstacleView getView() {
		return view;
	}
	
	public void update() {
		// TODO Auto-generated method stub
		
		Point[] coords = model.getCoords();
		
		int[] x = new int[coords.length];
		int[] y = new int [coords.length];
		for (int i = 0; i < coords.length; i++) {
			x[i] = (int) (coords[i].getX() - coords[0].getX());
			y[i] = (int) (coords[i].getY() - coords[0].getY());
			
		}
		
		view.setPolygon(new Polygon(x, y, coords.length));
		view.setLocation((int)coords[0].getX(), (int)coords[0].getY());
	}
			
}
