package com.pi4.duet.view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.HashMap;
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
	private HashMap<CollisionView, Double> collisionsMap = new HashMap<>();
	private int widthCollision, heightCollision;
	private Image collisionRed, collisionBlue;

	private ObstacleController controller;

	public ObstacleView(int width, int height, int ballRadius, ObstacleController controller) {
		this.controller = controller;
		this.setOpaque(false);
		this.setSize(new Dimension(width, height));
		this.setLocation(0, 0);
		this.widthCollision = ballRadius * 7;
		this.heightCollision = ballRadius * 7;
		this.setVisible(true);
		this.setLayout(null);

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
			//g2d.clip(polygon);
			for(CollisionView cv : collisionsMap.keySet()) {
				Double angle = collisionsMap.get(cv);

				double cx = cv.point.getX() + polygon.xpoints[0] + cv.width / 2;
				double cy = cv.point.getY() + polygon.ypoints[0] + cv.height / 2;
				
		        g2d.rotate(angle + controller.getAngle(), cx, cy);
		        g2d.drawImage(cv.icon, (int) (cv.point.getX() + polygon.xpoints[0]), (int) (cv.point.getY() + polygon.ypoints[0]), null);
			}
			g2d.dispose();
		}
	}

	public void updatePosition() {
		// TODO Auto-generated method stub
		Point[] points = controller.getPoints();

		int[] x = new int[points.length];
		int[] y = new int [points.length];
		for (int i = 0; i < points.length; i++) {
			x[i] = (int) (points[i].getX());
			y[i] = (int) (points[i].getY());
		}

		this.polygon = new Polygon(x, y, points.length);
	}

	@SuppressWarnings("unchecked")
	public void setCollisionsMap(HashMap<CollisionView, Double> collisionsMap) {
		// TODO Auto-generated method stub
		this.collisionsMap = (HashMap<CollisionView, Double>) collisionsMap.clone();
	}

	@SuppressWarnings("unchecked")
	public HashMap<CollisionView, Double> getCollisionsMap() { return (HashMap<CollisionView, Double>) this.collisionsMap.clone(); }

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
		HashMap<CollisionView, Double> copy = (HashMap<CollisionView, Double>) collisionsMap.clone();
		for (CollisionView cv : copy.keySet()) {
			collisionsMap.put(cv, - collisionsMap.get(cv));
		}
	}

}
