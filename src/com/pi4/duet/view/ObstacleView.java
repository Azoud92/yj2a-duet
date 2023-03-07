package com.pi4.duet.view;

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
import com.pi4.duet.controller.GameController;
import com.pi4.duet.controller.ObstacleController;

public class ObstacleView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5265129255893879964L;
	
	private Polygon polygon;
	private ArrayList<CollisionView> listCol;
	private GameController gvC;
	
	private Image collisionBlue;
	private Image collisionRed;
			
	public ObstacleView(ObstacleController controller, int width, int height, int x, int y, GameController gc) {
		listCol = new ArrayList<CollisionView>();
		this.setOpaque(false);

		this.setSize(new Dimension(width, height));
		this.setLocation(x,y);
		this.gvC = gc;
		this.setVisible(true);
		this.setLayout(null);
		
		collisionBlue = Auxiliaire.resizeImage(new ImageIcon(this.getClass().getResource("/resources/img/collision_blue.png")), gvC.getBallRadius()*7, gvC.getBallRadius()*7).getImage();
		collisionRed = Auxiliaire.resizeImage(new ImageIcon(this.getClass().getResource("/resources/img/collision_red.png")), gvC.getBallRadius()*7, gvC.getBallRadius()*7).getImage();
		
	}
		
	public void setPolygon(Polygon polygon) {
		// TODO Auto-generated method stub
		
		this.polygon = polygon;
	}
	
	public Polygon getPolygon() { return polygon; }
	
	public void addCollision(CollisionView collision) {
		listCol.add(collision);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);		
		g.setColor(Color.white);
		g.drawPolygon(polygon);
		g.fillPolygon(polygon);
		
		Graphics2D g2d = (Graphics2D) g.create();
	    g2d.clip(polygon);
	    for(CollisionView cv : listCol) {
	    	g2d.drawImage(cv.icon, cv.x, cv.y, cv.width, cv.height, null);
	    }
	    g2d.dispose();
	}
	
	public class CollisionView{

		Image icon;
		int x, y, width, height;

		public CollisionView(double x, double y, Color color) {
			this.width =  gvC.getBallRadius()*7;
			this.height =  gvC.getBallRadius()*7;
			this.x = (int) x - width/2;
			this.y = (int) y - height/2;
			
			
			if(color == Color.blue) this.icon = collisionBlue;
			if(color == Color.red) this.icon = collisionRed;
		}
	}
	
}
