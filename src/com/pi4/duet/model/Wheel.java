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
	
		
	public void rotate(Direction dir,double i) {
		if (dir == Direction.HORAIRE) {
			angle -= Math.toRadians(i * rotationSpeed);
		}
		else if (dir == Direction.ANTI_HORAIRE){
			angle += Math.toRadians(i * rotationSpeed);
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
	
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public void resetBallPosition() {
		ball_2.setCenterBall(new Point(center.getX() + radius, center.getY()));
		ball_1.setCenterBall(new Point(center.getX() - radius, center.getY()));
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
		
		public void setCenterBall(Point centerBall) {
			this.centerBall = centerBall;
			
		}

		public boolean isInCollision(Obstacle o) {
	        int n = o.getCoords().length;
	        double[] distances = new double[n];

	        // calcul de la distance entre le centre du cercle et chaque sommet du polygone
	        for (int i = 0; i < n; i++) {
	            double dx = o.getCoords()[i].getX() - centerBall.getX();
	            double dy = o.getCoords()[i].getY() - centerBall.getY();
	            distances[i] = Math.sqrt(dx*dx + dy*dy);
	        }

	        // vérification de la collision en comparant la distance minimale à la rayon du cercle
	        for (int i = 0; i < n; i++) {
	            int j = (i + 1) % n;
	            double edgeLength = Math.sqrt(Math.pow(o.getCoords()[j].getX() - o.getCoords()[i].getX(), 2) + Math.pow(o.getCoords()[j].getY() - o.getCoords()[i].getY(), 2));
	            double u = ((centerBall.getX() - o.getCoords()[i].getX()) * (o.getCoords()[j].getX() - o.getCoords()[i].getX()) + (centerBall.getY() - o.getCoords()[i].getY()) * (o.getCoords()[j].getY() - o.getCoords()[i].getY())) / Math.pow(edgeLength, 2);
	            if (u < 0 || u > 1) {
	                continue;
	            }
	            Point intersection = new Point(o.getCoords()[i].getX() + u * (o.getCoords()[j].getX() - o.getCoords()[i].getX()), o.getCoords()[i].getY() + u * (o.getCoords()[j].getY() - o.getCoords()[i].getY()));
	            double dx = intersection.getX() - centerBall.getX();
	            double dy = intersection.getY() - centerBall.getY();
	            if (Math.sqrt(dx*dx + dy*dy) < ballRadius) {
	                return true;
	            }
	        }

	        for (double distance : distances) {
	            if (distance < ballRadius) {
	                return true;
	            }
	        }

	        return false;
	    }
	
	}
}
