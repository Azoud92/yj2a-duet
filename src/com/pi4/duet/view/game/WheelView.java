package com.pi4.duet.view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.pi4.duet.Point;
import com.pi4.duet.controller.game.WheelController;
import com.pi4.duet.model.game.RotationType;

public class WheelView extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -3333759469583551200L;

	private Dimension size;
	private Ball ball_1, ball_2;
	private BallMvt mvtBall_1, mvtBall_2;

	private WheelController controller;

	public WheelView(Dimension size, WheelController controller) {
		this.size = size;
		this.controller = controller;
		setSize(size);
		setOpaque(false);
		setLayout(null);

		Point ball_1_coords = new Point(controller.getCenterBall_1().getX() - controller.getBallRadius(),
				controller.getCenterBall_1().getY() - controller.getBallRadius());
		ball_1 = new Ball(ball_1_coords, Color.RED, controller.getBallRadius());

		Point ball_2_coords = new Point(controller.getCenterBall_2().getX() - controller.getBallRadius(),
				controller.getCenterBall_2().getY() - controller.getBallRadius());
		ball_2 = new Ball(ball_2_coords, Color.BLUE, controller.getBallRadius());

		mvtBall_1 = new BallMvt(ball_1, ball_1.color);
		mvtBall_2 = new BallMvt(ball_2, ball_2.color);

		add(mvtBall_1);
		add(mvtBall_2);
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(ball_1.color);
		g.fillOval((int) (ball_1.coords.getX()),
				(int) (ball_1.coords.getY()),
				(int) (2 * controller.getBallRadius()), (int) (2 * controller.getBallRadius()));

		g.setColor(ball_2.color);
		g.fillOval((int) (ball_2.coords.getX()),
				(int) (ball_2.coords.getY()),
				(int) (2 * controller.getBallRadius()), (int) (2 * controller.getBallRadius()));

		mvtBall_1.paintComponent(mvtBall_1.getGraphics());
		mvtBall_2.paintComponent(mvtBall_2.getGraphics());
	}

	// Fonctions d'animation de l'effet de traînée
	public void mvt_1_Rotate(RotationType dir, double angle) {
		mvtBall_1.rotate(dir, angle);
	}

	public void mvt_2_Rotate(RotationType dir, double angle) {
		mvtBall_2.rotate(dir, angle);
	}

	public void stopMvt() {
		mvtBall_1.resetAngle();
		mvtBall_2.resetAngle();
	}

	public void greyWheel() {
		ball_1.color = Color.gray;
		ball_2.color = Color.gray;
		mvtBall_1.color = Color.gray;
		mvtBall_2.color = Color.gray;
	}

	public void resetWheelColor() {
		ball_1.color = Color.red;
		ball_2.color = Color.blue;
		mvtBall_1.color = Color.red;
		mvtBall_2.color = Color.blue;
	}

	public void updateBall_1(Point coords) {
		ball_1.coords = coords;
	}

	public void updateBall_2(Point coords) {
		ball_2.coords = coords;
	}

	private class Ball {
		Point coords;
		Color color;
		double radius;

		Ball(Point coords, Color color, double radius) {
			this.coords = coords;
			this.color = color;
			this.radius = radius;
		}
	}

	private class BallMvt extends JPanel { // représente l'effet "traînée" des balles du volant

		private static final long serialVersionUID = -5296039916894329970L;

		Color color;
		Ball ball;
		double[] coordX, coordY;


		BallMvt(Ball ball, Color color) {
			this.setSize(size);
			this.setVisible(true);
			this.setOpaque(false);
			this.ball = ball;
			this.coordX = initCoordX(ball);
			this.coordY = initCoordY(ball);
			this.color = color;
		}

		double[] initCoordX(Ball ball) {
			double[] res = new double[62];
			for (int i = 0; i < 62; i++) {
				res[i] = ball.coords.getX();
			}
			return res;
		}

		double[] initCoordY(Ball ball) {
			double[] res = new double[62];
			res[61] = ball.coords.getY();
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
				g.drawOval((int) coordX[i], (int) coordY[i], ((int) (2 * ball.radius) / (coordX.length / 3)) * (i / 3), ((int) (2 * ball.radius) / (coordX.length / 3)) * (i / 3));
				g.fillOval((int) coordX[i], (int) coordY[i], ((int) (2 * ball.radius) / (coordX.length / 3)) * (i / 3), ((int) (2 * ball.radius) / (coordX.length / 3)) * (i / 3));
			}
		}

		void rotate(RotationType dir, double angle) {
			if (color == Color.blue) {
				double angleTmp = angle;

				for (int i = coordX.length - 1; i >= 0; i--) {
					coordX[i] = (int) (controller.getWheelRadius() * Math.cos(angleTmp) + controller.getWheelCenter().getX()) - controller.getBallRadius();
					coordY[i] = (int) (controller.getWheelRadius() * Math.sin(angleTmp) + controller.getWheelCenter().getY()) - controller.getBallRadius();
					if (dir == RotationType.HORAIRE) {
						angleTmp += Math.toRadians(1 * controller.getWheelSpeed());
					}
					else if (dir == RotationType.ANTI_HORAIRE){
						angleTmp -= Math.toRadians(1 * controller.getWheelSpeed());
					}
				}
			}
			else {
				double angleTmp = angle + Math.PI;

				for (int i = coordX.length - 1; i>=0; i--) {
					coordX[i] = (int) (controller.getWheelRadius() * Math.cos(angleTmp) + controller.getWheelCenter().getX()) - controller.getBallRadius();
					coordY[i] = (int) (controller.getWheelRadius() * Math.sin(angleTmp) + controller.getWheelCenter().getY()) - controller.getBallRadius();
					if (dir == RotationType.HORAIRE) {
						angleTmp += Math.toRadians(1 * controller.getWheelSpeed());
					}
					else if (dir == RotationType.ANTI_HORAIRE) {
						angleTmp -= Math.toRadians(1 * controller.getWheelSpeed());
					}
				}
			}
		}

		void resetAngle() {
			for (int i = 0; i < coordX.length; i++) {
				coordX[i] = ball.coords.getX();
			}
			coordY[coordY.length - 1] = ball.coords.getY();
			for (int i = 60; i >= 0; i--) {
				coordY[i] = coordY[i + 1] + 0.5;
			}
		}
	}

	public WheelController getController() {
		// TODO Auto-generated method stub
		return controller;
	}

}
