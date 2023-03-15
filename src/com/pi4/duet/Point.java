package com.pi4.duet;

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
			
	public Point clone() {
		return new Point(x, y);
	}
	
	public String toString() {
		return "(" + x + "," + y +")";
	}

}
