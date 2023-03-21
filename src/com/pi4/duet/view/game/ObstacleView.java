package com.pi4.duet.view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.pi4.duet.Auxiliaire;

public class ObstacleView extends JPanel { // dessin d'un obstacle
	/**
	 * 
	 */
	private static final long serialVersionUID = -5265129255893879964L;
	
	private Polygon polygon;
	private ArrayList<CollisionView> listCol;
	private int widthCol, heightCol;
	private Image collisionBlue;
	private Image collisionRed;
		
	public ObstacleView(int width, int height, int ballRadius) {
		listCol = new ArrayList<CollisionView>();
		this.setOpaque(false);
		this.setSize(new Dimension(width, height));
		this.setLocation(0, 0);
		this.widthCol =ballRadius * 7;
		this.heightCol = ballRadius * 7;
		this.setVisible(true);
		this.setLayout(null);
		
		collisionBlue = Auxiliaire.resizeImage(new ImageIcon(this.getClass().getResource("/resources/img/collision_blue.png")),widthCol,heightCol).getImage();
		collisionRed = Auxiliaire.resizeImage(new ImageIcon(this.getClass().getResource("/resources/img/collision_red.png")), widthCol, heightCol).getImage();	
	}

	public void setPolygon(Polygon polygon) {
		// TODO Auto-generated method stub		
		this.polygon = polygon;
	}
	
	public Polygon getPolygon() { return polygon; }
	
	public void addCollision(CollisionView collision) {
		listCol.add(collision);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<CollisionView> getCollisions() {
		return (ArrayList<CollisionView>) listCol.clone();
	}
	
	// Dessin de l'obstacle
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (polygon != null) {
			g.setColor(Color.white);
			g.drawPolygon(polygon);
			g.fillPolygon(polygon);
			
			Graphics2D g2d = (Graphics2D) g.create();
		    g2d.clip(polygon);
		    for(CollisionView cv : listCol) {
		    	g2d.drawImage(cv.icon, cv.x + polygon.xpoints[0], cv.y + polygon.ypoints[0], cv.width, cv.height, null);
		    }
		    g2d.dispose();
		}		
	}
	
	public class CollisionView { // représente l'effet de tâche sur l'obstacle

		Image icon;
		int x, y, width, height;

		public CollisionView (double x, double y, Color color) {
			this.width = widthCol;
			this.height =  heightCol;
			
			this.x = (int) x - width / 2;
			this.y = (int) y - height / 2;			
			
			if(color == Color.blue) this.icon = collisionBlue;
			if(color == Color.red) this.icon = collisionRed;
		}
	}

	@SuppressWarnings("unchecked")
	public void setColList(ArrayList<CollisionView> alcv) {
		// TODO Auto-generated method stub
		this.listCol = (ArrayList<CollisionView>) alcv.clone();
	}
	
}
