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

import com.pi4.duet.Point;
import com.pi4.duet.controller.game.GameDuoController;
import com.pi4.duet.model.Direction;
import com.pi4.duet.model.game.Side;
import com.pi4.duet.model.game.Wheel;


public class GameDuoView extends JPanel{
	 // représente la vue du jeu (graphismes, ...)
	
		private static final long serialVersionUID = -306594423077754361L;
			
		private GameDuoController controller;

		private BallView ballRedH, ballBlueH, ballRedB, ballBlueB;
		private BallMvt mvtRedH, mvtBlueH, mvtRedB, mvtBlueB;
		private JButton back, replay;
		private Dimension size;

		private Image background = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();
		
		public GameDuoView(Dimension size, GameDuoController controller) {
			
			this.size = new Dimension(size.width / 3, size.height);

			this.controller = controller;

			Dimension dim = new Dimension(size.width / 3, size.height);
			this.setPreferredSize(dim);
			
			int ballRadius = controller.getBallRadius();
			
			ballRedH = new BallView((int) (controller.getCenterBall1(Side.HIGH).getX()) - ballRadius, (int) controller.getCenterBall1(Side.HIGH).getY()  - ballRadius, 2 * ballRadius, 2 * ballRadius, Color.red);
			mvtRedH = new BallMvt(ballRedH, Color.red, controller.getWheel(Side.HIGH));
			this.add(mvtRedH);	
			
			ballBlueH = new BallView((int) (controller.getCenterBall2(Side.HIGH).getX() - ballRadius), (int) controller.getCenterBall2(Side.HIGH).getY()  - ballRadius, 2 * ballRadius, 2 * ballRadius, Color.blue);
			mvtBlueH = new BallMvt(ballBlueH, Color.blue, controller.getWheel(Side.HIGH));
			this.add(mvtBlueH);		
			
			
			ballRedB = new BallView((int) (controller.getCenterBall1(Side.LOW).getX()) - ballRadius, (int) controller.getCenterBall1(Side.LOW).getY()  - ballRadius, 2 * ballRadius, 2 * ballRadius, Color.red);
			mvtRedB = new BallMvt(ballRedB, Color.red, controller.getWheel(Side.LOW));
			this.add(mvtRedB);	
			
			ballBlueB = new BallView((int) (controller.getCenterBall2(Side.LOW).getX() - ballRadius), (int) controller.getCenterBall2(Side.LOW).getY()  - ballRadius, 2 * ballRadius, 2 * ballRadius, Color.blue);
			mvtBlueB = new BallMvt(ballBlueB, Color.blue, controller.getWheel(Side.LOW));
			this.add(mvtBlueB);	
	        
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
			if (side == Side.HIGH) {
				this.ballBlueH.setLocation((int) blue.getX() - controller.getBallRadius(), (int) blue.getY() - controller.getBallRadius());
				this.ballRedH.setLocation((int) red.getX() - controller.getBallRadius(), (int) red.getY() - controller.getBallRadius());
			}
			else if (side == Side.LOW) {
				this.ballBlueB.setLocation((int) blue.getX() - controller.getBallRadius(), (int) blue.getY() - controller.getBallRadius());
				this.ballRedB.setLocation((int) red.getX() - controller.getBallRadius(), (int) red.getY() - controller.getBallRadius());
			}
		}
		
		public Point getBallRedPos(Side side) {
			if(side == Side.HIGH) {
				return new Point(ballRedH.x, ballRedH.y);
			}
			else if(side == Side.LOW) {
				return new Point(ballRedB.x, ballRedB.y);
			}
			return null;
		}
		
		public Point getBallBluePos(Side side) {
			if(side == Side.HIGH) {
				return new Point(ballBlueH.x, ballBlueH.y);
			}
			else if(side == Side.LOW) {
				return new Point(ballBlueB.x, ballBlueB.y);
			}
			return null;
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
			
			g.setColor(ballBlueH.color);
			g.fillOval(ballBlueH.x, ballBlueH.y, ballBlueH.width, ballBlueH.height);
			
			g.setColor(ballRedH.color);
			g.fillOval(ballRedH.x,ballRedH.y, ballRedH.width, ballRedH.height);
			
			mvtRedH.paintComponent(mvtRedH.getGraphics());
			mvtBlueH.paintComponent(mvtBlueH.getGraphics());
			
			
			g.setColor(ballBlueB.color);
			g.fillOval(ballBlueB.x, ballBlueB.y, ballBlueB.width, ballBlueB.height);
			
			g.setColor(ballRedB.color);
			g.fillOval(ballRedB.x,ballRedB.y, ballRedB.width, ballRedB.height);
			
			mvtRedB.paintComponent(mvtRedB.getGraphics());
			mvtBlueB.paintComponent(mvtBlueB.getGraphics());
		}
		
		// Fonctions d'animation de l'effet de traînée
		public void MvtRedRotate(Side side, Direction dir, double angle) {
			if(side == Side.HIGH) {
				mvtRedH.rotate(dir, angle);	
			}
			else if(side == Side.LOW) {
				mvtRedB.rotate(dir, angle);	
			}
		}
		
		public void MvtBlueRotate(Side side, Direction dir, double angle) {
			if(side == Side.HIGH) {
				mvtBlueH.rotate(dir, angle);	
			}
			else if(side == Side.LOW) {
				mvtBlueB.rotate(dir, angle);	
			}
		}
		
		public void stopMvt(Side side) {
			if(side == Side.HIGH) {
				mvtRedH.resetAngle();
				mvtBlueH.resetAngle();
			}
			else if(side == Side.LOW) {
				mvtRedB.resetAngle();
				mvtBlueB.resetAngle();
			}
		}
		
		// Affichage lorsqu'un joueur perd la partie
		public void lostGame() {
			background =  new ImageIcon(this.getClass().getResource("/resources/img/background_grey.png")).getImage();
			ballRedH.color = Color.gray;
			ballBlueH.color = Color.gray;
			mvtRedH.color = Color.gray;
			mvtBlueH.color = Color.gray;
			
			ballRedB.color = Color.gray;
			ballBlueB.color = Color.gray;
			mvtRedB.color = Color.gray;
			mvtBlueB.color = Color.gray;
			
			back = new JButton("RETOUR");
			back.setBounds(size.width/5, this.size.height/6 , this.size.width/5 * 3, this.size.height/6);
			back.setForeground(Color.RED);
			back.setBackground(Color.BLACK);
			back.setFont(new Font("Arial", Font.BOLD, 50));
			back.setVisible(true);
			this.add(back);	
			this.setComponentZOrder(back, 0);
			
			replay = new JButton("REJOUER");
			replay.setBounds(this.size.width/5, this.size.height/6 * 3, this.size.width/5 * 3, this.size.height/6);
			replay.setBackground(Color.BLACK);
			replay.setForeground(Color.BLUE);
			replay.setFont(new Font("Arial", Font.BOLD, 50));
			replay.setVisible(true);
			add(replay);
			this.setComponentZOrder(replay, 1);
			
			back.addActionListener(e -> {
				reset();
				controller.affMenu();
			});
			
			replay.addActionListener(e -> {
				// rejouer
				reset();
				//controller.replay();
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
			ballRedH.color = Color.red;
			ballBlueH.color = Color.blue;
			mvtRedH.color = Color.red;
			mvtBlueH.color = Color.blue;	
			
			ballRedB.color = Color.red;
			ballBlueB.color = Color.blue;
			mvtRedB.color = Color.red;
			mvtBlueB.color = Color.blue;	
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
			
			public BallMvt(BallView ballV, Color color, Wheel wheel) {
				this.setOpaque(false);			
				this.setSize(new Dimension(size.width, size.height));			
				this.setVisible(true);
				
				this.ballV = ballV;
				this.coordX = initCoordX(ballV);
				this.coordY = initCoordY(ballV);
				this.color = color;
				this.wheel = wheel;
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
