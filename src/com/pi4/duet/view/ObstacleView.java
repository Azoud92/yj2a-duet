package com.pi4.duet.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
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
		
	public ObstacleView(ObstacleController controller, int width, int height, int x, int y, GameController gc) {
		listCol = new ArrayList<CollisionView>();
		this.setOpaque(false);

		this.setSize(new Dimension(width, height));
		this.setLocation(x,y);
		this.gvC = gc;
		this.setVisible(true);
		this.setLayout(null);
	}
		
	public void setPolygon(Polygon polygon) {
		// TODO Auto-generated method stub
		
		this.polygon = polygon;
	}
	
	public Polygon getPolygon() { return polygon; }
	
	public void addCollision(CollisionView collision) {
		this.add(collision);
		listCol.add(collision);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);		
		g.setColor(Color.white);
		g.drawPolygon(polygon);
		g.fillPolygon(polygon);

	}
	
	public class CollisionView extends JLabel{

		private static final long serialVersionUID = -9103682417105599832L;
		

		public CollisionView(double x, double y, Color color) {
			this.setOpaque(false);
			this.setBounds((int) x - (gvC.getBallRadius()*3/2), (int) y - (gvC.getBallRadius()*3/2), gvC.getBallRadius()*3, gvC.getBallRadius()*3);

			if(color == Color.blue) this.setIcon(Auxiliaire.resizeImage(new ImageIcon(this.getClass().getResource("/resources/collision_blue.png")), gvC.getBallRadius()*3, gvC.getBallRadius()*3));
			if(color == Color.red) this.setIcon(Auxiliaire.resizeImage(new ImageIcon(this.getClass().getResource("/resources/collision_red.png")), gvC.getBallRadius()*3, gvC.getBallRadius()*3));
			
			
		}
	}
	
}
