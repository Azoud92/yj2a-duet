package com.pi4.duet.model;
import java.lang.Math;

public class Wheel {
	
	public final int radius = 100;
	private Point center;
	
	private Ball ball_1, ball_2;
	
	private double angle = 0;
	public final double rotationSpeed = 0.25; // elle est fixe, vous pouvez lui donner une inertie (acceleration + frein)
	public final int ballRadius = 10;
	
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
	
	
	public static double distance(Point p1,Point p2) {
		return Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
	}
	public Point getCenterBall1() {
		return ball_1.centerBall;
	}
	
	public Point getCenterBall2() {
		return ball_2.centerBall;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public int isInCollision(Obstacle o) {
		if(ball_1.isInCollision(o) && ball_2.isInCollision(o))return 3;
		if(ball_1.isInCollision(o))return 1;
		if(ball_2.isInCollision(o))return 2;
		return 0;

	}
	
	private class Ball {
		
		Point centerBall;
		
		public Ball(Point centerBall, double radius) {
			this.centerBall = centerBall;
		}
		public boolean isInCollision(Obstacle o){
			for(int i=0;i<o.getCoords().length-1;i++){
				double d1=distance(o.getCoords()[i],centerBall);
				double d2=distance(o.getCoords()[i+1],centerBall);
				double d3=distance(o.getCoords()[i],o.getCoords()[i+1]);
				if(d1+d2<=d3+0.01) return true;
			}

			return false;
		}


		/*public boolean isInCollision2(Obstacle o) {
			
			for (int i = 0; i < o.getCoords().length; i++) {				
				double distance1 = Math.sqrt(Math.pow(o.getCoords()[i].getX() - centerBall.getX(), 2) + Math.pow(o.getCoords()[i].getY() - centerBall.getY(), 2));
				double distance2 = 0;
				
				if (i + 1 >= o.getCoords().length) {
					distance2 = Math.sqrt(Math.pow(o.getCoords()[0].getX() - centerBall.getX(), 2) + Math.pow(o.getCoords()[0].getY() - centerBall.getY(), 2));
				}
				else {
					distance2 = Math.sqrt(Math.pow(o.getCoords()[i + 1].getX() - centerBall.getX(), 2) + Math.pow(o.getCoords()[i + 1].getY() - centerBall.getY(), 2));
				}
				
				if (distance1 <= ballRadius || distance2 <= ballRadius) {
		            return true;
		        }				
			}
			
			return false;
		}*/
	}
}
