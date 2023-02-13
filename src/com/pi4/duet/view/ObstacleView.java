package com.pi4.duet.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.JPanel;

import com.pi4.duet.controller.ObstacleController;

public class ObstacleView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5265129255893879964L;
	
	private Polygon polygon;
		
	public ObstacleView(ObstacleController controller, int width, int height) {		
		this.setOpaque(false);
		
		this.setSize(new Dimension(width, height));
		
		this.setVisible(true);
	}
		
	public void setPolygon(Polygon polygon) {
		// TODO Auto-generated method stub
		this.polygon = polygon;
	}
	
	public Polygon getPolygon() { return polygon; }
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);		
		g.setColor(Color.white);
		g.drawPolygon(polygon);
		g.fillPolygon(polygon);
	}
	
}
