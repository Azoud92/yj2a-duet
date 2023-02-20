package com.pi4.duet.view;

import java.awt.Color;   
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.pi4.duet.controller.GameController;
import com.pi4.duet.model.Direction;

import com.pi4.duet.model.Point;

public class GameView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -306594423077754361L;
	
	private GameController controller;

	private BallView ballRed, ballBlue;
	private BallMvt mvtRed, mvtBlue;
	private Dimension size;

	private Image background;
	
	public GameView(Dimension size, GameController controller) {
		this.size = new Dimension(size.width/3, size.height);

		this.controller = controller;
		background = new ImageIcon(this.getClass().getResource("/resources/background.png")).getImage();
		Dimension dim = new Dimension(size.width / 3, size.height);
		this.setPreferredSize(dim);
		
		int ballRadius = controller.getBallRadius();
		
		ballRed = new BallView((int) (controller.getCenterBall1().getX()) - ballRadius, (int) controller.getCenterBall1().getY(), 2 * ballRadius, 2 * ballRadius, Color.red);
		this.add(ballRed);
		mvtRed = new BallMvt(ballRed, initCoordX(ballRed), initCoordY(ballRed), Color.red);
		this.add(mvtRed);	
		
		ballBlue = new BallView((int) (controller.getCenterBall2().getX() - ballRadius), (int) controller.getCenterBall2().getY(), 2 * ballRadius, 2 * ballRadius, Color.blue);
		this.add(ballBlue);
		mvtBlue = new BallMvt(ballBlue, initCoordX(ballBlue), initCoordY(ballBlue), Color.blue);
		this.add(mvtBlue);		        
        
        this.addKeyListener(controller);

        this.add(ballBlue);
        this.add(ballRed);

		this.setLayout(null);
	}
	
	public void affichePause(){
		String[] option={"Reprendre le jeu", "Quitter le jeu"};
		int indice=JOptionPane.showOptionDialog(this,
				"Le jeu est en pause, veuillez choisir une option",
				"Jeu en pause",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				new ImageIcon(getClass().getResource("/resources/pause.png")),
				option,
				option[0]);
		switch(indice){
			case 0: {
				decompte();
				controller.setPause(controller.isPause());
				controller.getModel().gamePausedOrResumed();
				break;
			}
			case 1: System.exit(0);
		}
	}

	public void decompte(){
		String []resume={"Reprendre maintenant"};
		JOptionPane jboite2 = new JOptionPane("Le jeu va reprendre automatiquement dans moins de 3 secondes",
				JOptionPane.INFORMATION_MESSAGE,-1,
				new ImageIcon(getClass().getResource("/resources/resume.jpg")));
		jboite2.setOptions(resume);
		final Timer timer = new Timer(3000, e -> {
			jboite2.setValue(JOptionPane.CLOSED_OPTION);
		});
		timer.setRepeats(false);
		timer.start();
		jboite2.createDialog(this, "TENEZ VOUS PRÊT").setVisible(true);

	}
	
	public int[] initCoordX(BallView b) {
		int[] res = new int[62];
		for (int i = 0; i < 62; i++) {
			res[i] = b.x;
		}
		return res;
	}
		
	public int[] initCoordY(BallView b) {
		int[] res = new int[62];
		res[61] = b.y;
		for (int i = 60; i >= 0; i--) {
			res[i] = res[i + 1] + 1;
		}
		return res;
	}
	
	public void setBallsPosition(Point blue, Point red) {
		this.ballBlue.setLocation((int) blue.getX() - controller.getBallRadius(), (int) blue.getY());
		this.ballRed.setLocation((int) red.getX() - controller.getBallRadius(), (int) red.getY());
	}
	
	private class BallView extends JPanel {
		
		private static final long serialVersionUID = -8381550184021712931L;
		private int x, y, width, height;
		private Color color;
		
		public BallView(int x, int y, int width, int height, Color color) {
			this.x = x;
			this.y = y;
			this.width  = width;
			this.height = height;
			this.color = color;			
		}
		
		public void setLocation(int x, int y) {
			this.x = x;
			this.y = y;			
		}		
	}
	
	private class BallMvt extends JPanel {	

		private static final long serialVersionUID = -5296039916894329970L;
		
		private Color color;
		private BallView ballV;

		private int[] coordX, coordY;
		
		public BallMvt(BallView ballV, int[] coordX, int[] coordY, Color color) {
			this.setOpaque(false);			
			this.setSize(new Dimension(size.width, size.height));			
			this.setVisible(true);
			this.ballV = ballV;
			this.coordX = coordX;
			this.coordY = coordY;
			this.color = color;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);		
			g.setColor(color);
			for (int i = 0; i < coordX.length; i++) { // On suppose que coordX ET coordY on toujours la meme taille.
				g.drawOval(coordX[i], coordY[i], (ballV.width / (coordX.length/3)) * (i/3), (ballV.height / (coordX.length/3)) * (i/3));
				g.fillOval(coordX[i], coordY[i], (ballV.width / (coordX.length/3)) * (i/3), (ballV.height / (coordX.length/3)) * (i/3));
				//System.out.println("x : "+ coordX[i] +" y : " + coordY[i] + " width : " + (ballV.width / (coordX.length/3)) * (i/3) +" height : " +  (ballV.height / (coordX.length/3)) * (i/3));
			}			
		}
		
		public void rotate(Direction dir) {
			double r = Math.sqrt(Math.pow(ballV.x - controller.getWheelCenter().getX(), 2) + Math.pow(ballV.y - controller.getWheelCenter().getY(), 2));
			double t0 = Math.atan2(ballV.y - controller.getWheelCenter().getY(), ballV.x - controller.getWheelCenter().getX());
			double angle = Math.atan((ballV.y - controller.getWheelCenter().getY()) / (ballV.x - controller.getWheelCenter().getX())) + t0;
			
			for (int i = 0; i < coordX.length; i++) {
				if (dir == Direction.HORAIRE) {
					angle -= Math.toRadians(controller.getWheelangle() * controller.getWheelSpeed());
				}
				else if (dir == Direction.ANTI_HORAIRE){
					angle += Math.toRadians(controller.getWheelangle() * controller.getWheelSpeed());
				}
				System.out.println("wheeel angle : "+controller.getWheelangle()+" vitesse wheel : "+controller.getWheelSpeed());
				coordX[i] = (int) (controller.getWheelCenter().getX() + r * Math.cos(angle));
				coordY[i] = (int) (controller.getWheelCenter().getY() + r * Math.sin(angle));			
				
				System.out.println(coordX[i] + "; " + coordY[i]);
			}
		}
		
		public void resetAngle() {
			double r = Math.sqrt(Math.pow(ballV.x - ballV.x, 2) + Math.pow(ballV.y - ballV.y, 2));
			double t0 = Math.atan2(ballV.y - ballV.y, ballV.x - ballV.x);
			double angle = t0;
			
			for (int i = 0; i < coordX.length; i++) {
				angle += Math.toRadians(controller.getWheelangle() * controller.getWheelSpeed());
				coordX[i] = (int) (ballV.x + r * Math.cos(angle));
				coordY[i] = (int) (ballV.y + r * Math.sin(angle));			
			}
		}	
	}
	
	
	public void refresh() {
		revalidate();
		repaint();
	}
		
	public void addObstacle(ObstacleView ov) {
		this.add(ov);
	}
	
	
	public void removeObstacle(ObstacleView ov) {
		this.remove(ov);
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(background, 0, 0, size.width, size.height, this);
		g.setColor(ballBlue.color);
		g.fillOval(ballBlue.x, ballBlue.y, ballBlue.width, ballBlue.height);
		
		g.setColor(ballRed.color);
		g.fillOval(ballRed.x,ballRed.y, ballRed.width, ballRed.height);
		
		mvtRed.paintComponent(mvtRed.getGraphics());
		mvtBlue.paintComponent(mvtBlue.getGraphics());
	}
	
	public void mvtRedRotate(Direction dir) {
		mvtRed.rotate(dir);		
	}
	
	public void mvtBlueRotate(Direction dir) {
		mvtBlue.rotate(dir);
	}


	public void resetAngleMvt() {
		mvtRed.resetAngle();
		mvtBlue.resetAngle();		
	}
		
	public void lostGame() {
		background =  new ImageIcon(this.getClass().getResource("/resources/background_grey.png")).getImage();
		ballRed.color = Color.gray;
		ballBlue.color = Color.gray;
		mvtRed.color = Color.gray;
		mvtBlue.color = Color.gray;
		
		JButton quit = new JButton("BACK");
		quit.setBounds(size.width/5, this.size.height/5 , this.size.width/5 * 3, this.size.height/5);
		quit.setForeground(Color.red);
		quit.setBackground(Color.gray);
		quit.setFont(new Font("Arial", Font.BOLD, 50));
		quit.setVisible(true);
		quit.setOpaque(false);
		this.add(quit);
		
		quit.addActionListener(e -> {
			controller.affMenu();
		});
		
		JButton replay = new JButton("REJOUER");
		replay.setBounds(this.size.width/5, this.size.height/5 * 3, this.size.width/5 * 3, this.size.height/5);
		replay.setBackground(Color.gray);
		replay.setForeground(Color.blue);
		replay.setFont(new Font("Arial", Font.BOLD, 50));
		replay.setVisible(true);
		replay.setOpaque(false);
		add(replay);
		
		replay.addActionListener(e -> {
			//rejouer
			controller.replay();
		});
		this.setVisible(true);
		this.revalidate();
		this.repaint();		
	}

}
