package com.pi4.duet.model;
import java.lang.Math;

public class Wheel { // représente le volant du jeu
		
	private Point center;	
	private Ball ball_1, ball_2; // resp. balle rouge & balle bleue
	
	public int radius = 100; // rayon du volant	
	public final double rotationSpeed = 0.25;
	public final int ballRadius = 10; // rayon de la balle
	
	private double angle = 0; // angle des balles
	private double inertia = 0; // inertie pour donner l'illusion d'accélération / freinage
	
	public Wheel(Point center) {
		this.center = center;
		ball_1 = new Ball(new Point(center.getX() - radius, center.getY()));
		ball_2 = new Ball(new Point(center.getX() + radius, center.getY()));
	}	
		
	public void rotate(Direction dir) {
		if (dir == Direction.HORAIRE) {
			angle -= Math.toRadians(inertia);
		}
		else if (dir == Direction.ANTI_HORAIRE){
			angle += Math.toRadians(inertia);
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
		
	// on remet les balles à leur position initiale (droites)
	public void resetBallPosition() {
		ball_2.setCenterBall(new Point(center.getX() + radius, center.getY()));
		ball_1.setCenterBall(new Point(center.getX() - radius, center.getY()));
	}

	// méthode auxiliaire servant à déterminer une collision et renvoyant un entier servant à l'effet "tâche"
	public int isInCollision(Obstacle o) {
		if (ball_1.isInCollision(o) && ball_2.isInCollision(o)) return 3;
		if (ball_1.isInCollision(o)) return 1;
		if (ball_2.isInCollision(o)) return 2;
		return 0;
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
	public void setRadius(int radius) {
		this.radius = radius;
	}
		
	public Point getCenter() {
		// TODO Auto-generated method stub
		return center;
	}
	
	public void setInertia(double inertia) {
		this.inertia = inertia;
	}
	
	public double getInertia() { return inertia; }
	
	private class Ball { // représente une balle rattachée au volant
		
		Point centerBall;
		
		public Ball(Point centerBall) {
			this.centerBall = centerBall;
		}
		
		public void setCenterBall(Point centerBall) {
			this.centerBall = centerBall;			
		}

		// Méthode principale servant à déterminer si il y a collision entre le volant et l'obstacle par le biais de calculs
		public boolean isInCollision(Obstacle o) {
	        int n = o.getCoords().length;
	        double[] distances = new double[n];

	        // calcul de la distance entre le centre du cercle et chaque sommet du polygone
	        for (int i = 0; i < n; i++) {
	            double dx = o.getCoords()[i].getX() - centerBall.getX();
	            double dy = o.getCoords()[i].getY() - centerBall.getY();
	            distances[i] = Math.sqrt(dx * dx + dy * dy);
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
	            if (Math.sqrt(dx * dx + dy * dy) < ballRadius) {
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
