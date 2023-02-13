package com.pi4.duet.view;
import java.awt.Color;  
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pi4.duet.controller.GameController;
import com.pi4.duet.model.Point;

public class GameView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -306594423077754361L;
	
	private ArrayList<ObstacleView> obstacles;
	
	private JLabel ballRed, ballBlue;
	private GameController controller;
	//private ImageIcon imageR, imageB;
	
	public GameView(Dimension size, GameController controller) {
		this.controller = controller;
		obstacles = new ArrayList<ObstacleView>();
		Dimension dim = new Dimension(size.width / 3, size.height);
		this.setPreferredSize(dim);
		
		//imageR = new ImageIcon("BallRed.png");
        ballRed = new JLabel();
        int ballRadius = controller.getBallRadius();
        ballRed.setBounds((int) (controller.getCenterBall1().getX()) - ballRadius, (int) controller.getCenterBall1().getY(), 2 * ballRadius, 2 * ballRadius);
        ballRed.setBackground(Color.red);
        ballRed.setOpaque(true);
        //ballRed.setIcon(imageR);

        //imageB = new ImageIcon("BallBlue.png");
        ballBlue = new JLabel();
        ballBlue.setBounds((int) (controller.getCenterBall2().getX() - ballRadius), (int) controller.getCenterBall2().getY(), 2 * ballRadius, 2 * ballRadius);
        ballBlue.setBackground(Color.blue);
        ballBlue.setOpaque(true);
        //ballBlue.setIcon(imageB);
        
        this.addKeyListener(controller);
        this.add(ballBlue);
        this.add(ballRed);
        
		this.setLayout(null);
	}
	
	public void setBallsPosition(Point blue, Point red) {
		this.ballBlue.setLocation((int) blue.getX() - controller.getBallRadius(), (int) blue.getY());
		this.ballRed.setLocation((int) red.getX() - controller.getBallRadius(), (int) red.getY());
	}
			
	public void refresh() {
		revalidate();
		repaint();
	}
		
	public void addObstacle(ObstacleView ov) {
		obstacles.add(ov);
	}
	
	
	public void removeObstacle(ObstacleView ov) {
		obstacles.remove(ov);
	}
	
	
	public void paintComponent(Graphics g, ObstacleView ov) {
		g.setColor(Color.white);
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
