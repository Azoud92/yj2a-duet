package com.pi4.duet.controller;

import java.awt.Polygon;

import com.pi4.duet.model.Obstacle;
import com.pi4.duet.model.Point;
import com.pi4.duet.view.ObstacleView;

public class ObstacleController {

	private Obstacle model;
	private ObstacleView view;
		
	public void setModel(Obstacle model) { this.model = model; }
	
	public void setView(ObstacleView view) { this.view = view; }
	
	public void update() {
		// TODO Auto-generated method stub
		
		Point[] coords = model.getCoords();
		
		int[] x = new int[coords.length];
		int[] y = new int [coords.length];
		for (int i = 0; i < coords.length; i++) {
			x[i] = (int)coords[i].getX();
			y[i] = (int)coords[i].getY();
			
		}
		
		view.setPolygon(new Polygon(x, y, coords.length));
	}
			
}
