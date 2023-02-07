package com.pi4.duet.view;

import java.awt.Polygon;


import javax.swing.JPanel;

import com.pi4.duet.model.Obstacle;
import com.pi4.duet.model.Point;

public class ObstacleView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5265129255893879964L;
	
	private Point[] coord; // représente les 4 points (haut gauche, haut droit, bas droit, bas gauche) du rectangle formé
	private Polygon p;

	
	public ObstacleView(Point[] coord){
		this.coord = coord;
		int[] x = new int[coord.length];
		int[] y = new int [coord.length];
		for (int i = 0; i< coord.length;i++) {
			x[i] = (int) coord[i].getX();
			y[i] = (int) coord[i].getY();
			
		}
		p = new Polygon(x, y,coord.length);
	}

	
	public void setPosition(Point[] coord) {
		this.coord = coord;
		int[] x = new int[coord.length];
		int[] y = new int [coord.length];
		for (int i = 0; i< coord.length;i++) {
			x[i] = (int) coord[i].getX();
			y[i] = (int) coord[i].getY();
			
		}
		p = new Polygon(x, y,coord.length);
		
	}
	
	public void rotate(Obstacle o,double a) { // la rotation se fait par rapport au centre de la figure
		o.rotate(90);
		setPosition(o.getCoords());
	}
	
	public Polygon getPolygon() {
		return p;
	}



	
}
