package com.pi4.duet.view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.pi4.duet.controller.GameController;
import com.pi4.duet.model.Point;

public class GameView extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -306594423077754361L;

	private GameController controller;
	private BallView ballRed, ballBlue;
	private Dimension size;

	private Image background = new ImageIcon(this.getClass().getResource("/resources/background.png")).getImage();

	public GameView(Dimension size, GameController controller) {
		this.size = size;
		this.controller = controller;
		Dimension dim = new Dimension(size.width / 3, size.height);
		this.setPreferredSize(dim);

		int ballRadius = controller.getBallRadius();
		ballRed = new BallView((int) (controller.getCenterBall1().getX()) - ballRadius, (int) controller.getCenterBall1().getY(), 2 * ballRadius, 2 * ballRadius, Color.red);
		this.add(ballRed);

		ballBlue = new BallView((int) (controller.getCenterBall2().getX() - ballRadius), (int) controller.getCenterBall2().getY(), 2 * ballRadius, 2 * ballRadius, Color.blue);
		this.add(ballBlue);

		this.setLayout(null);

		this.addKeyListener(controller);
		this.add(ballBlue);
		this.add(ballRed);

		this.setLayout(null);
	}

	public void setBallsPosition(Point blue, Point red) {
		this.ballBlue.setLocation((int) blue.getX() - controller.getBallRadius(), (int) blue.getY());
		this.ballRed.setLocation((int) red.getX() - controller.getBallRadius(), (int) red.getY());
	}

	private class BallView extends JPanel {
		/**
		 *
		 */
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
	}

}
