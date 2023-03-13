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
import com.pi4.duet.controller.GameDuoController;
import com.pi4.duet.model.Direction;
import com.pi4.duet.model.Point;
import com.pi4.duet.model.Side;
import com.pi4.duet.model.Wheel;


public class GameDuoView extends JPanel{
	 // représente la vue du jeu (graphismes, ...)
	
		private static final long serialVersionUID = -306594423077754361L;
			
		private GameDuoController controller;

		private BallView ballRedG, ballBlueG, ballRedD, ballBlueD;
		private BallMvt mvtRedG, mvtBlueG, mvtRedD, mvtBlueD;
		private JButton back, replay;
		private Dimension size;

		private Image background = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();
		
		public GameDuoView(Dimension size, GameDuoController controller) {
			
			this.size = new Dimension(size.width / 3, size.height);

			this.controller = controller;

			Dimension dim = new Dimension(size.width / 3, size.height);
			this.setPreferredSize(dim);
			
			int ballRadius = controller.getBallRadius();
			
			ballRedG = new BallView((int) (controller.getCenterBall1(Side.LEFT).getX()) - ballRadius, (int) controller.getCenterBall1(Side.LEFT).getY()  - ballRadius, 2 * ballRadius, 2 * ballRadius, Color.red);
			mvtRedG = new BallMvt(ballRedG, Color.red);
			this.add(mvtRedG);	
			
			ballBlueG = new BallView((int) (controller.getCenterBall2(Side.LEFT).getX() - ballRadius), (int) controller.getCenterBall2(Side.LEFT).getY()  - ballRadius, 2 * ballRadius, 2 * ballRadius, Color.blue);
			mvtBlueG = new BallMvt(ballBlueG, Color.blue);
			this.add(mvtBlueG);		
			
			
			ballRedD = new BallView((int) (controller.getCenterBall1(Side.RIGHT).getX()) - ballRadius, (int) controller.getCenterBall1(Side.RIGHT).getY()  - ballRadius, 2 * ballRadius, 2 * ballRadius, Color.red);
			mvtRedD = new BallMvt(ballRedD, Color.red);
			this.add(mvtRedD);	
			
			ballBlueD = new BallView((int) (controller.getCenterBall2(Side.RIGHT).getX() - ballRadius), (int) controller.getCenterBall2(Side.RIGHT).getY()  - ballRadius, 2 * ballRadius, 2 * ballRadius, Color.blue);
			mvtBlueD = new BallMvt(ballBlueD, Color.blue);
			this.add(mvtBlueD);	
	        
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
					break;			
				case 1:
					this.setVisible(false);
					controller.stopMusic();
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
			
		public void setBallsPosition(Side side, Point blue, Point red) {
			if (side == Side.LEFT) {
				this.ballBlueG.setLocation((int) blue.getX() - controller.getBallRadius(), (int) blue.getY() - controller.getBallRadius());
				this.ballRedG.setLocation((int) red.getX() - controller.getBallRadius(), (int) red.getY() - controller.getBallRadius());
			}
			else if (side == Side.RIGHT) {
				this.ballBlueD.setLocation((int) blue.getX() - controller.getBallRadius(), (int) blue.getY() - controller.getBallRadius());
				this.ballRedD.setLocation((int) red.getX() - controller.getBallRadius(), (int) red.getY() - controller.getBallRadius());
			}
		}
		
		public Point getBallRedPos(Side side) {
			if(side == Side.LEFT) {
				return new Point(ballRedG.x, ballRedG.y);
			}
			else if(side == Side.RIGHT) {
				return new Point(ballRedD.x, ballRedD.y);
			}
		}
		
		public Point getBallBluePos(Side side) {
			if(side == Side.LEFT) {
				return new Point(ballBlueG.x, ballBlueG.y);
			}
			else if(side == Side.RIGHT) {
				return new Point(ballBlueD.x, ballBlueD.y);
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
			if (controller.isBackgroundEnabled()) g.drawImage(background, 0, 0, size.width, size.height, this);
			else g.fillRect(0, 0, size.width, size.height);
			
			g.setColor(ballBlueG.color);
			g.fillOval(ballBlueG.x, ballBlueG.y, ballBlueG.width, ballBlueG.height);
			
			g.setColor(ballRedG.color);
			g.fillOval(ballRedG.x,ballRedG.y, ballRedG.width, ballRedG.height);
			
			mvtRedG.paintComponent(mvtRedG.getGraphics());
			mvtBlueG.paintComponent(mvtBlueG.getGraphics());
			
			
			g.setColor(ballBlueD.color);
			g.fillOval(ballBlueD.x, ballBlueD.y, ballBlueD.width, ballBlueD.height);
			
			g.setColor(ballRedD.color);
			g.fillOval(ballRedD.x,ballRedD.y, ballRedD.width, ballRedD.height);
			
			mvtRedD.paintComponent(mvtRedD.getGraphics());
			mvtBlueD.paintComponent(mvtBlueD.getGraphics());
		}
		
		// Fonctions d'animation de l'effet de traînée
		public void MvtRedRotate(Side side, Direction dir, double angle) {
			if(side == Side.LEFT) {
				mvtRedG.rotate(dir, angle);	
			}
			else if(side == Side.RIGHT) {
				mvtRedD.rotate(dir, angle);	
			}
		}
		
		public void MvtBlueRotate(Side side, Direction dir, double angle) {
			if(side == Side.LEFT) {
				mvtBlueG.rotate(dir, angle);	
			}
			else if(side == Side.RIGHT) {
				mvtBlueD.rotate(dir, angle);	
			}
		}
		
		public void stopMvt(Side side) {
			if(side == Side.LEFT) {
				mvtRedG.resetAngle();
				mvtBlueG.resetAngle();
			}
			else if(side == Side.RIGHT) {
				mvtRedD.resetAngle();
				mvtBlueD.resetAngle();
			}
		}
		
		// Affichage lorsqu'un joueur perd la partie
		public void lostGame() {
			background =  new ImageIcon(this.getClass().getResource("/resources/img/background_grey.png")).getImage();
			ballRedG.color = Color.gray;
			ballBlueG.color = Color.gray;
			mvtRedG.color = Color.gray;
			mvtBlueG.color = Color.gray;
			
			ballRedD.color = Color.gray;
			ballBlueD.color = Color.gray;
			mvtRedD.color = Color.gray;
			mvtBlueD.color = Color.gray;
			
			back = new JButton("RETOUR");
			back.setBounds(size.width/5, this.size.height/6 , this.size.width/5 * 3, this.size.height/6);
			back.setForeground(Color.RED);
			back.setBackground(Color.BLACK);
			back.setFont(new Font("Arial", Font.BOLD, 50));
			back.setVisible(true);
			this.add(back);	
			
			replay = new JButton("REJOUER");
			replay.setBounds(this.size.width/5, this.size.height/6 * 3, this.size.width/5 * 3, this.size.height/6);
			replay.setBackground(Color.BLACK);
			replay.setForeground(Color.BLUE);
			replay.setFont(new Font("Arial", Font.BOLD, 50));
			replay.setVisible(true);
			add(replay);
			
			back.addActionListener(e -> {
				reset();
				controller.affMenu();
			});
			
			replay.addActionListener(e -> {
				// rejouer
				reset();
				controller.replay();
			});
			this.setVisible(true);
			this.revalidate();
			this.repaint();		
		}

		// On réaffiche la partie
		private void reset() {
			this.remove(back);
			this.remove(replay);
			this.repaint();
			
			background = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();
			ballRedG.color = Color.red;
			ballBlueG.color = Color.blue;
			mvtRedG.color = Color.red;
			mvtBlueG.color = Color.blue;	
			
			ballRedD.color = Color.red;
			ballBlueD.color = Color.blue;
			mvtRedD.color = Color.red;
			mvtBlueD.color = Color.blue;	
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
			private Wheel wheel;
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
						coordX[i] = (int) (wheel.radius * Math.cos(angleTmp) + wheel.getCenter().getX()) - wheel.ballRadius;
						coordY[i] = (int) (wheel.radius * Math.sin(angleTmp) + wheel.getCenter().getY()) - wheel.ballRadius;
						if (dir == Direction.HORAIRE) {
							angleTmp += Math.toRadians(1 * wheel.rotationSpeed);
						}
						else if (dir == Direction.ANTI_HORAIRE){
							angleTmp -= Math.toRadians(1 * wheel.rotationSpeed);
						}
					}
				}
				else {
					double angleTmp = angle + Math.PI;
				
					for (int i = coordX.length - 1; i>=0; i--) {					
						coordX[i] = (int) (wheel.radius * Math.cos(angleTmp) +  wheel.getCenter().getX()) - wheel.ballRadius;
						coordY[i] = (int) (wheel.radius * Math.sin(angleTmp) +  wheel.getCenter().getY()) - wheel.ballRadius;
						if (dir == Direction.HORAIRE) {
							angleTmp += Math.toRadians(1 * wheel.rotationSpeed);
						}
						else if (dir == Direction.ANTI_HORAIRE) {
							angleTmp -= Math.toRadians(1 * wheel.rotationSpeed);
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
