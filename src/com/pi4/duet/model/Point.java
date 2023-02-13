package com.pi4.duet.model;

import java.io.Serializable;

public class Point implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6268835503859790493L;
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() { return x; }
	public double getY() { return y; }

	public void setX(double x) { this.x = x; }
	public void setY(double y ) { this.y = y; }
	
	// Calcule la distance entre deux points
	public static double distance(Point p1, Point p2) {
		return Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
	}
		
	public Point clone() {
		return new Point(x, y);
	}

}
