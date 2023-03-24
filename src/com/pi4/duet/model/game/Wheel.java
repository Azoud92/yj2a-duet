package com.pi4.duet.model.game;
import java.lang.Math;

import com.pi4.duet.Point;
import com.pi4.duet.model.Direction;

public class Wheel { // représente le volant du jeu
		
	private Point center;	
	private Ball ball_1, ball_2; // resp. balle rouge & balle bleue
	
	public int radius = 100; // rayon du volant	
	public final double rotationSpeed = 0.25;
	private final int ballRadius = 10; // rayon de la balle
	
	private double angle = 0; // angle des balles
	private double inertia = 0; // inertie pour donner l'illusion d'accélération / freinage
	
	private Direction wheelRotating = null;
	private Direction lastRotation = null;
	private Direction wheelMovement =null;

	private boolean stopMovement=false;
	private boolean wheelBreaking = false;
	
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

	public void move(Direction dir) {
		if(dir==Direction.RIGHT && ball_2.centerBall.getX()<420&& ball_1.centerBall.getX()<420) {
			ball_2.centerBall.setX(getCenterBall2().getX() + 0.5);
			ball_1.centerBall.setX(getCenterBall1().getX() + 0.5);
			center.setX(center.getX() + 0.5);
		}else if(dir==Direction.LEFT && ball_1.centerBall.getX()>0 && ball_2.centerBall.getX()>0){
			ball_2.centerBall.setX(getCenterBall2().getX()-0.5);
			ball_1.centerBall.setX(getCenterBall1().getX()-0.5);
			center.setX(center.getX()-0.5);
		}

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
	
	public Point getCenterBall1() { return ball_1.centerBall; }	
	public Point getCenterBall2() { return ball_2.centerBall; }
	
	public double getAngle() { return angle; }	
	public void setAngle(double angle) { this.angle = angle; }
		
	public Point getCenter() { return center; }
	
	public void setInertia(double inertia) { this.inertia = inertia; }	
	public double getInertia() { return inertia; }
	

	public int getBallRadius() { return ballRadius; }
	
	public Direction getWheelRotating() { return wheelRotating; }
	public void setWheelRotating(Direction wheelRotating) {	this.wheelRotating = wheelRotating;	}
	public void stopWheelRotation() {
		if (wheelRotating != null) lastRotation = wheelRotating;
		wheelRotating = null;
	}

	public Direction getLastRotation() { return lastRotation; }
	public void setLastRotation(Direction lastRotation) { this.lastRotation = lastRotation; }

	public boolean isWheelBreaking() { return wheelBreaking; }
	public void setWheelBreaking(boolean wheelBreaking) { this.wheelBreaking = wheelBreaking; }


	public Direction getWheelMovement() { return wheelMovement; }
	public void setWheelMovement(Direction wheelMovement) {	this.wheelMovement= wheelMovement;	}

	public void setStopMovement(boolean stopMovement){this.stopMovement= stopMovement;}
	public boolean getStopMovement(){return this.stopMovement;}


	private class Ball { // représente une balle rattachée au volant
		
		Point centerBall;
		
		Ball(Point centerBall) {
			this.centerBall = centerBall;
		}
		
		void setCenterBall(Point centerBall) {
			this.centerBall = centerBall;			
		}

		// Méthode principale servant à déterminer si il y a collision entre le volant et l'obstacle par le biais de calculs
		boolean isInCollision(Obstacle o) {
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
