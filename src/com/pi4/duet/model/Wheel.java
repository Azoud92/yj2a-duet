package com.pi4.duet.model;
import java.lang.Math;

public class Wheel {
	
	public final int radius = 50;
	private Point center;
	
	private Ball ball_1, ball_2;
	
	private double angle = 0;
	public final double rotationSpeed = 0.25; // elle est fixe, vous pouvez lui donner une inertie (acceleration + frein)
	public final int ballRadius = 5;
	
	public Wheel(Point center) {
		this.center = center;
		ball_2 = new Ball(new Point(center.getX() + radius, center.getY()), ballRadius);
		ball_1 = new Ball(new Point(center.getX() - radius, center.getY()), ballRadius);
	}
	
	public Point getCenter() {
		// TODO Auto-generated method stub
		return center;
	}
			
	public void rotate(Direction dir) {
		if (dir == Direction.HORAIRE) {
			angle -= Math.toRadians(1 * rotationSpeed);
		}
		else if (dir == Direction.ANTI_HORAIRE){
			angle += Math.toRadians(1 * rotationSpeed);
		}
		
		// Changement coordonées Ball 2
		ball_2.centerBall.setX(radius * Math.cos(angle) + center.getX());
		ball_2.centerBall.setY(radius * Math.sin(angle) + center.getY());
		// Changement coordonées Ball 1
		
		double dist1 = ball_2.centerBall.getX() - center.getX();
		double dist2 = ball_2.centerBall.getY() - center.getY();
		ball_1.centerBall.setX(center.getX() - dist1);
		ball_1.centerBall.setY(center.getY() - dist2);
	}
	
	public Point getCenterBall1() {
		return ball_1.centerBall;
	}
	
	public Point getCenterBall2() {
		return ball_2.centerBall;
	}
	
	public boolean isInCollision(Obstacle o) {
		return ball_1.isInCollision(o) || ball_2.isInCollision(o);
	}
	
	private class Ball {
		
		Point centerBall;
		
		public Ball(Point centerBall, double radius) {
			this.centerBall = centerBall;
		}
		
		public boolean isInCollision(Obstacle o) {			
			boolean inside = false;
			for (int i = 0, j = o.getCoords().length - 1; i < o.getCoords().length - 1; j = i++) {
				if ((o.getCoords()[i].getY() > centerBall.getY() != o.getCoords()[j].getY() > centerBall.getY()) &&
						(centerBall.getX() < (o.getCoords()[j].getX() - o.getCoords()[i].getX()) * (centerBall.getY() - o.getCoords()[i].getY()) / (o.getCoords()[j].getY() - o.getCoords()[i].getY()) + o.getCoords()[i].getX())) {
					inside = !inside;
				}
			}
			if (inside) return true;
			return false;
		}
	}
}
