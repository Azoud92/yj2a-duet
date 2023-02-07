package com.pi4.duet.view;

import java.awt.Polygon;


import javax.swing.JPanel;

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
	
	public void rotate(double a) { // la rotation se fait par rapport au centre de la figure
		double angleRadians = Math.toRadians(a);
		
		for (Point po : coord) {
			System.out.print("pos en x avant: " + po.getX()+"   ");
			System.out.print("pos en y avant: " + po.getY());
			System.out.println();
			po.setX((po.getX() * Math.cos(angleRadians) - po.getY() * Math.sin(angleRadians)));
			po.setY((po.getX() * Math.sin(angleRadians) + po.getY() * Math.cos(angleRadians)));
			System.out.print("pos en x apres: " + po.getX()+"   ");
			System.out.print("pos en y apres: " + po.getY());
			System.out.println();
			System.out.println();
		}
		setPosition(coord);
	}
	
	public Polygon getPolygon() {
		return p;
	}



	
}
