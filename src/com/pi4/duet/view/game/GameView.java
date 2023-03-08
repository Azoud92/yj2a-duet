package com.pi4.duet.view.game;

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
import com.pi4.duet.model.State;

public class GameView extends JPanel { // représente la vue du jeu (graphismes, ...)
	
	private static final long serialVersionUID = -306594423077754361L;
		
	private GameController controller;

	private BallView ballRed, ballBlue;
	private BallMvt mvtRed, mvtBlue;
	
	private Dimension size;

	private Image background = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();
	
	public GameView(Dimension size, GameController controller) {
		this.size = new Dimension(size.width / 3, size.height);

		this.controller = controller;

		Dimension dim = new Dimension(size.width / 3, size.height);
		this.setPreferredSize(dim);
		
		int ballRadius = controller.getBallRadius();
		
		ballRed = new BallView((int) (controller.getCenterBall1().getX()) - ballRadius, (int) controller.getCenterBall1().getY()  - ballRadius, 2 * ballRadius, 2 * ballRadius, Color.red);
		mvtRed = new BallMvt(ballRed, Color.red);
		this.add(mvtRed);	
		
		ballBlue = new BallView((int) (controller.getCenterBall2().getX() - ballRadius), (int) controller.getCenterBall2().getY()  - ballRadius, 2 * ballRadius, 2 * ballRadius, Color.blue);
		mvtBlue = new BallMvt(ballBlue, Color.blue);
		this.add(mvtBlue);		        
        
        this.addKeyListener(controller);

		this.setLayout(null);
	}
	
	public void affichePause() {
		String[] option = {"Reprendre le jeu", "Revenir au menu", "Quitter le jeu"};
		int indice = JOptionPane.showOptionDialog(this,
				"Le jeu est en pause, veuillez choisir une option",
				"Jeu en pause",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				new ImageIcon(getClass().getResource("/resources/img/pause.png")),
				option,
				option[0]);
		switch (indice) {
			case 0:
				decompte();
				controller.getModel().setState(State.ON_GAME);
				break;			
			case 1:
				this.setVisible(false);
				controller.affMenu();
				break;			
			case 2: System.exit(0);
		}
	}

	public void decompte(){
		String[] resume = {"Reprendre maintenant"};
		JOptionPane jboite2 = new JOptionPane("Le jeu va reprendre automatiquement dans moins de 3 secondes",
				JOptionPane.INFORMATION_MESSAGE, -1,
				new ImageIcon(getClass().getResource("/resources/img/resume.jpg")));
		jboite2.setOptions(resume);
		final Timer timer = new Timer(3000, e -> {
			jboite2.setValue(JOptionPane.CLOSED_OPTION);
		});
		timer.setRepeats(false);
		timer.start();
		jboite2.createDialog(this, "TENEZ VOUS PRÊT").setVisible(true);
	}
		
	public void setBallsPosition(Point blue, Point red) {
		this.ballBlue.setLocation((int) blue.getX() - controller.getBallRadius(), (int) blue.getY() - controller.getBallRadius());
		this.ballRed.setLocation((int) red.getX() - controller.getBallRadius(), (int) red.getY() - controller.getBallRadius());
	}
	
	public Point getBallRedPos() {
		return new Point(ballRed.x, ballRed.y);
	}
	
	public Point getBallBluePos() {
		return new Point(ballBlue.x, ballBlue.y);
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
		if (controller.isBackgroundEnabled()) g.drawImage(background, 0, 0, size.width, size.height, this);
		else g.fillRect(0, 0, size.width, size.height);
		
		g.setColor(ballBlue.color);
		g.fillOval(ballBlue.x, ballBlue.y, ballBlue.width, ballBlue.height);
		
		g.setColor(ballRed.color);
		g.fillOval(ballRed.x,ballRed.y, ballRed.width, ballRed.height);
		
		mvtRed.paintComponent(mvtRed.getGraphics());
		mvtBlue.paintComponent(mvtBlue.getGraphics());
	}
	
	// Fonctions d'animation de l'effet de traînée
	public void MvtRedRotate(Direction dir, double angle) {
		mvtRed.rotate(dir, angle);	
	}
	
	public void MvtBlueRotate(Direction dir, double angle) {
		mvtBlue.rotate(dir, angle);
	}
	
	public void stopMvt() {
		mvtRed.resetAngle();
		mvtBlue.resetAngle();		
	}
	
	// Affichage lorsqu'un joueur perd la partie
	public void lostGame() {
		background =  new ImageIcon(this.getClass().getResource("/resources/img/background_grey.png")).getImage();
		ballRed.color = Color.gray;
		ballBlue.color = Color.gray;
		mvtRed.color = Color.gray;
		mvtBlue.color = Color.gray;
		
		JButton back = new JButton("RETOUR");
		back.setBounds(size.width/5, this.size.height/6 , this.size.width/5 * 3, this.size.height/6);
		back.setForeground(Color.RED);
		back.setBackground(Color.BLACK);
		back.setFont(new Font("Arial", Font.BOLD, 50));
		back.setVisible(true);
		this.add(back);	
		
		JButton replay = new JButton("REJOUER");
		replay.setBounds(this.size.width/5, this.size.height/6 * 3, this.size.width/5 * 3, this.size.height/6);
		replay.setBackground(Color.BLACK);
		replay.setForeground(Color.BLUE);
		replay.setFont(new Font("Arial", Font.BOLD, 50));
		replay.setVisible(true);
		add(replay);
		
		back.addActionListener(e -> {
			reset(back, replay);
			controller.affMenu();
		});
		
		replay.addActionListener(e -> {
			// rejouer
			reset(back, replay);
			controller.replay();
		});
		this.setVisible(true);
		this.revalidate();
		this.repaint();		
	}

	// On réaffiche la partie
	private void reset(JButton back, JButton replay) {
		this.remove(back);
		this.remove(replay);
		this.repaint();
		
		background = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();
		ballRed.color = Color.red;
		ballBlue.color = Color.blue;
		mvtRed.color = Color.red;
		mvtBlue.color = Color.blue;		
	}
	
	public Dimension getSize() {
		return size;
	}
	
	private class BallView { // représente la vue d'une balle du volant
		
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
	
	private class BallMvt extends JPanel { // représente l'effet "traînée" des balles du volant

		private static final long serialVersionUID = -5296039916894329970L;
		
		private Color color;
		private BallView ballV;
		private double[] coordX, coordY;
		
		public BallMvt(BallView ballV, Color color) {
			this.setOpaque(false);			
			this.setSize(new Dimension(size.width, size.height));			
			this.setVisible(true);
			
			this.ballV = ballV;
			this.coordX = initCoordX(ballV);
			this.coordY = initCoordY(ballV);
			this.color = color;
		}
		
		public double[] initCoordX(BallView b) {
			double[] res = new double[62];
			for (int i = 0; i < 62; i++) {
				res[i] = b.x;
			}
			return res;
		}
		
		public double[] initCoordY(BallView b) {
			double[] res = new double[62];
			res[61] = b.y;
			for (int i = 60; i >= 0; i--) {
				res[i] = res[i + 1] + 0.5;
			}
			return res;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);		
			g.setColor(color);
			for (int i = 0; i < coordX.length; i++) { // On suppose que coordX ET coordY ont toujours la meme taille
				g.drawOval((int) coordX[i],(int) coordY[i], (ballV.width / (coordX.length/3)) * (i/3), (ballV.height / (coordX.length/3)) * (i/3));
				g.fillOval((int) coordX[i], (int) coordY[i], (ballV.width / (coordX.length/3)) * (i/3), (ballV.height / (coordX.length/3)) * (i/3));				
			}			
		}
		
		public void rotate(Direction dir, double angle) {			
			if (color == Color.blue) {
				double angleTmp = angle;

				for (int i = coordX.length - 1; i >= 0; i--) {					
					coordX[i] = (int) (controller.getWheelRadius() * Math.cos(angleTmp) + controller.getWheelCenter().getX()) - controller.getBallRadius();
					coordY[i] = (int) (controller.getWheelRadius() * Math.sin(angleTmp) + controller.getWheelCenter().getY()) - controller.getBallRadius();
					if (dir == Direction.HORAIRE) {
						angleTmp += Math.toRadians(1 * controller.getWheelSpeed());
					}
					else if (dir == Direction.ANTI_HORAIRE){
						angleTmp -= Math.toRadians(1 * controller.getWheelSpeed());
					}
				}
			}
			else {
				double angleTmp = angle + Math.PI;
			
				for (int i = coordX.length - 1; i>=0; i--) {					
					coordX[i] = (int) (controller.getWheelRadius() * Math.cos(angleTmp) + controller.getWheelCenter().getX()) - controller.getBallRadius();
					coordY[i] = (int) (controller.getWheelRadius() * Math.sin(angleTmp) + controller.getWheelCenter().getY()) - controller.getBallRadius();
					if (dir == Direction.HORAIRE) {
						angleTmp += Math.toRadians(1 * controller.getWheelSpeed());
					}
					else if (dir == Direction.ANTI_HORAIRE) {
						angleTmp -= Math.toRadians(1 * controller.getWheelSpeed());
					}
				}
			}
		}
		
		public void resetAngle() {
			for (int i = 0; i<coordX.length; i++) {
				coordX[i] = ballV.x;
			}
			coordY[coordY.length-1] = ballV.y;
			for (int i = 60; i>=0; i--) {
				coordY[i] = coordY[i+1] + 0.5;
			}		
		}
	}
}
