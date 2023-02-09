package com.pi4.duet.controller;

import java.awt.Polygon;

import com.pi4.duet.model.Obstacle;
import com.pi4.duet.model.Point;

public class ObstacleController {

	private Obstacle model;
	
	public ObstacleController(Obstacle model) {
		this.model = model;
	}
	
	public Polygon getPolygon() {
		// TODO Auto-generated method stub
		Point[] coord = model.getCoords();
		
		int[] x = new int[coord.length];
		int[] y = new int [coord.length];
		for (int i = 0; i< coord.length;i++) {
			x[i] = (int) coord[i].getX();
			y[i] = (int) coord[i].getY();
			
		}
		return new Polygon(x, y,coord.length);
	}
	
	public void rotate(double a) {
		model.rotate(90);
		getPolygon();	
	}
	
	
	
}
