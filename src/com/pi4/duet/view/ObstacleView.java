package com.pi4.duet.view;

import java.awt.Polygon;

import javax.swing.JPanel;

public class ObstacleView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5265129255893879964L;
	
	private Polygon polygon;
		
	public void setPolygon(Polygon polygon) {
		// TODO Auto-generated method stub
		this.polygon = polygon;
	}
	
	public Polygon getPolygon() { return polygon; }
	
	// Essayer d'implémenter paintComponent ici plutôt
}
