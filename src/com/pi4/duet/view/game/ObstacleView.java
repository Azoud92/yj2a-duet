package com.pi4.duet.view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.LinkedHashMap;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.pi4.duet.Auxiliaire;
import com.pi4.duet.Point;
import com.pi4.duet.controller.game.ObstacleController;

public class ObstacleView extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 6211757738266989720L;

	private Polygon polygon;
	private LinkedHashMap<CollisionView, Double> collisionsMap = new LinkedHashMap<>();
	private int widthCollision, heightCollision;
	private Image collisionRed, collisionBlue;

	private ObstacleController controller;
	
	public final int id;

	public ObstacleView(int width, int height, int ballRadius, ObstacleController controller, int idObs) {
		this.controller = controller;
		this.setOpaque(false);
		this.setSize(new Dimension(width, height));
		this.setLocation(0, 0);
		this.widthCollision = ballRadius * 8;
		this.heightCollision = ballRadius * 8;
		this.setVisible(true);
		this.setLayout(null);
		this.id = idObs;

		collisionBlue = Auxiliaire.resizeImage(new ImageIcon(this.getClass().getResource("/resources/img/collision_blue.png")), widthCollision, heightCollision).getImage();
		collisionRed = Auxiliaire.resizeImage(new ImageIcon(this.getClass().getResource("/resources/img/collision_red.png")), widthCollision, heightCollision).getImage();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (polygon != null) {
			Graphics2D g2d = (Graphics2D) g.create();
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.setColor(Color.WHITE);
	        g2d.drawPolygon(polygon);
	        g2d.fillPolygon(polygon);			
	        @SuppressWarnings("unchecked")
			LinkedHashMap<CollisionView, Double> copy = (LinkedHashMap<CollisionView, Double>) collisionsMap.clone();
			for(CollisionView cv : copy.keySet()) {
				g2d = (Graphics2D) g.create();
				g2d.clip(polygon);
				Double angle = -collisionsMap.get(cv);
				
				// Translation pour centrer l'image
		        int x = (int) (cv.point.getX() + controller.getCenter().getX() + cv.width / 2);
		        int y = (int) (cv.point.getY() + controller.getCenter().getY() + cv.height / 2);

		        // Rotation autour du point central de l'obstacle
		        AffineTransform at = new AffineTransform();
		        if (angle != controller.getAngle()) {
		        	at.rotate(angle + controller.getAngle(), controller.getCenter().getX(), controller.getCenter().getY());
			        g2d.transform(at);
		        }		        

		        // Rotation sur elle-même
		        g2d.rotate(angle, x, y);		        
		        g2d.drawImage(cv.icon, (int) (cv.point.getX() + controller.getCenter().getX()), (int) (cv.point.getY() + controller.getCenter().getY()), null);
		       	        				
		        g2d.dispose();
			}
		}
	}

	public void updatePosition(Point[] points) {
		// TODO Auto-generated method stub

		int[] x = new int[points.length];
		int[] y = new int [points.length];
		for (int i = 0; i < points.length; i++) {
			x[i] = (int) (points[i].getX());
			y[i] = (int) (points[i].getY());
		}

		this.polygon = new Polygon(x, y, points.length);	
	}

	public void setController(ObstacleController controller) {
		this.controller = controller;
	}

	@SuppressWarnings("unchecked")
	public void setCollisionsMap(LinkedHashMap<CollisionView, Double> collisionsMap) {
		// TODO Auto-generated method stub
		this.collisionsMap = (LinkedHashMap<CollisionView, Double>) collisionsMap.clone();
	}

	@SuppressWarnings("unchecked")
	public LinkedHashMap<CollisionView, Double> getCollisionsMap() { return (LinkedHashMap<CollisionView, Double>) this.collisionsMap.clone(); }

	public void addCollision(double angle, Point point, Color color) {
		collisionsMap.put(new CollisionView(point.getX(), point.getY(), color), angle);
	}

	private class CollisionView { // représente l'effet de tâche sur l'obstacle
		Image icon;
		Point point;
		int width, height;
		
		public CollisionView (double x, double y, Color color) {
			this.width = widthCollision;
			this.height = heightCollision;
			this.point = new Point(x - width / 2, y - height / 2);
			if(color == Color.blue) this.icon = collisionBlue;
			if(color == Color.red) this.icon = collisionRed;
		}
	}

	public void resetCollisions() {
		@SuppressWarnings("unchecked")
		LinkedHashMap<CollisionView, Double> copy = (LinkedHashMap<CollisionView, Double>) collisionsMap.clone();
		for (CollisionView cv : copy.keySet()) {
			collisionsMap.remove(cv);
			collisionsMap.put(cv,Math.abs(copy.get(cv)));				
		}
	}
}
