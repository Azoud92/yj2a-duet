package com.pi4.duet.view;



import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Graphics;

import java.util.ArrayList;

import javax.swing.JPanel;

import com.pi4.duet.controller.GameViewController;
import com.pi4.duet.model.Point;

public class GameView extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -306594423077754361L;
	
	protected GameViewController controller;
	protected Dimension size;
	protected double scaleX, scaleY;
	public ArrayList<ObstacleView> obstacles;

	
	public GameView(Dimension size, double scaleX, double scaleY) {
		this.setBackground(Color.black);
		obstacles = new ArrayList<ObstacleView>();
		Dimension dim = new Dimension(size.width/3,size.height);
		this.size = dim;
		this.setPreferredSize(dim);
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.setLayout(null);
		

	}
	
	
	public void setPositionObstacle(ObstacleView ov, Point[] coord ) {
		ov.setPosition(coord);
		this.revalidate();
		this.repaint();
	}
	
	
	public void addObstacle(ObstacleView ov) {
		obstacles.add(ov);
	}
	
	
	public void rmObstacle(ObstacleView ov) {
		obstacles.remove(ov);
	}
	
	
	public void paintComponent(Graphics g, ObstacleView ov) {
		g.setColor(Color.blue);
		g.drawPolygon(ov.getPolygon());
		g.fillPolygon(ov.getPolygon());
	}
	
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		for (ObstacleView ov : obstacles) {
			paintComponent(g, ov);
		}
	}
	
	
	
	
}
