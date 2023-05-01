package com.pi4.duet.model.game;
import com.pi4.duet.Point;
import com.pi4.duet.controller.game.WheelController;
import com.pi4.duet.model.home.Settings;

public class Wheel { // représente le volant du jeu

	private WheelController controller;
	private Point center;
	private Ball ball_1, ball_2; // resp. balle rouge & balle bleue
	private Settings settings;

	private int radius = 100;
	private double rotationSpeed = 0.15;
	private double movementSpeed = 0.5;
	private int ballRadius = 10;
	private double inertiaSpeed = 0.0004;

	private double angle = 0;
	private double inertia = 0; // inertie pour donner l'illusion d'accélération / freinage
	
	private int gameWidth; // pour déterminer le déplacement max. du volant

	private RotationType wheelRotating = null;
	private RotationType lastRotation = null;
	private Direction wheelMovement = null;

	private boolean isMoving = false;
	private boolean wheelBreaking = false;

	public Wheel(Point center, int gameWidth, WheelController controller, Settings settings) {
		this.center = center;
		this.gameWidth = gameWidth;
		this.controller = controller;
		this.settings = settings;
		ball_1 = new Ball(new Point(center.getX() - getRadius(), center.getY()));
		ball_2 = new Ball(new Point(center.getX() + getRadius(), center.getY()));
	}
	
	// animation du volant selon la direction souhaitée
	public void animateWheel() {		
		//if (wheelRotating == null) controller.stopMvt(); // on arrête l'effet visuel lorsque le volant ne tourne pas

		if (wheelBreaking) { // freinage en cours du volant
			if (inertia > 0) {
				if(!settings.getInertie()) inertia = rotationSpeed;
				else inertia -= inertiaSpeed;
				rotate(lastRotation);
				controller.updateBallsView(new Point(ball_1.centerBall.getX() - ballRadius, ball_1.centerBall.getY() - ballRadius),
						new Point(ball_2.centerBall.getX() - ballRadius, ball_2.centerBall.getY() - ballRadius));
				controller.updateMvt(lastRotation, angle);
			}
			else {
				wheelBreaking = false;
				inertia = 0;
				lastRotation = null;
			}
		}

		if (wheelRotating != null) {
			if (!wheelBreaking) {
				if (inertia < rotationSpeed) {
					if(!settings.getInertie()) inertia = rotationSpeed;
					else inertia += inertiaSpeed;
				}
			}
		}
		
		move();
		rotate(wheelRotating);
		controller.updateBallsView(new Point(ball_1.centerBall.getX() - ballRadius, ball_1.centerBall.getY() - ballRadius),
				new Point(ball_2.centerBall.getX() - ballRadius, ball_2.centerBall.getY() - ballRadius));
		controller.updateMvt(wheelRotating, angle);
	}

	private void rotate(RotationType dir) {
		if (dir == RotationType.HORAIRE) {
			angle -= Math.toRadians(inertia);
		}
		else if (dir == RotationType.ANTI_HORAIRE){
			angle += Math.toRadians(inertia);
		}

		// Changement coordonées Ball 2
		ball_2.centerBall.setX(getRadius() * Math.cos(angle) + center.getX());
		ball_2.centerBall.setY(getRadius() * Math.sin(angle) + center.getY());

		// Changement coordonées Ball 1
		double dist1 = ball_2.centerBall.getX() - center.getX();
		double dist2 = ball_2.centerBall.getY() - center.getY();
		ball_1.centerBall.setX(center.getX() - dist1);
		ball_1.centerBall.setY(center.getY() - dist2);
	}

	private void move() {
		if (wheelMovement == null || isMoving == false) return;
		switch(wheelMovement) {
		case RIGHT:
			if (center.getX() < gameWidth - radius - ballRadius) {
				ball_2.centerBall.setX(getCenterBall2().getX() + movementSpeed);
				ball_1.centerBall.setX(getCenterBall1().getX() + movementSpeed);
				center.setX(center.getX() + movementSpeed);
			}
			break;
		case LEFT:
			if (center.getX() > radius + ballRadius) {
				ball_2.centerBall.setX(getCenterBall2().getX() - movementSpeed);
				ball_1.centerBall.setX(getCenterBall1().getX() - movementSpeed);
				center.setX(center.getX() - movementSpeed);
			}
			break;
		default:
			break;
		}
		
		controller.updateBallsView(new Point(ball_1.centerBall.getX() - ballRadius, ball_1.centerBall.getY() - ballRadius),
				new Point(ball_2.centerBall.getX() - ballRadius,
						ball_2.centerBall.getY() - ballRadius));
	}

	// on remet les balles à leur position initiale (droites)
	private void resetBallPosition() {
		ball_2.setCenterBall(new Point(center.getX() + getRadius(), center.getY()));
		ball_1.setCenterBall(new Point(center.getX() - getRadius(), center.getY()));
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

	public Point getCenter() { return center; }

	public int getBallRadius() { return ballRadius; }

	public void setWheelRotating(RotationType wheelRotating) {	this.wheelRotating = wheelRotating;	}
	public void stopWheelRotation() {
		if (wheelRotating != null) lastRotation = wheelRotating;
		wheelRotating = null;
	}

	public boolean isWheelBreaking() { return wheelBreaking; }
	public void setWheelBreaking(boolean wheelBreaking) { this.wheelBreaking = wheelBreaking; }

	public Direction getWheelMovement() { return wheelMovement; }
	public void setWheelMovement(Direction wheelMovement) {	this.wheelMovement= wheelMovement; }

	public void setMoving(boolean isMoving){this.isMoving = isMoving; }

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
		this.resetBallPosition();
	}

	public double getRotationSpeed() {
		return rotationSpeed;
	}
	
	public void setInertia(int i) {
		// TODO Auto-generated method stub
		this.inertia = i;
	}

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
	        int n = o.getPoints().length;
	        double[] distances = new double[n];

	        // calcul de la distance entre le centre du cercle et chaque sommet du polygone
	        for (int i = 0; i < n; i++) {
	            double dx = o.getPoints()[i].getX() - centerBall.getX();
	            double dy = o.getPoints()[i].getY() - centerBall.getY();
	            distances[i] = Math.sqrt(dx * dx + dy * dy);
	        }

	        // vérification de la collision en comparant la distance minimale à la rayon du cercle
	        for (int i = 0; i < n; i++) {
	            int j = (i + 1) % n;
	            double edgeLength = Math.sqrt(Math.pow(o.getPoints()[j].getX() - o.getPoints()[i].getX(), 2) + Math.pow(o.getPoints()[j].getY() - o.getPoints()[i].getY(), 2));
	            double u = ((centerBall.getX() - o.getPoints()[i].getX()) * (o.getPoints()[j].getX() - o.getPoints()[i].getX()) + (centerBall.getY() - o.getPoints()[i].getY()) * (o.getPoints()[j].getY() - o.getPoints()[i].getY())) / Math.pow(edgeLength, 2);
	            if (u < 0 || u > 1) {
	                continue;
	            }
	            Point intersection = new Point(o.getPoints()[i].getX() + u * (o.getPoints()[j].getX() - o.getPoints()[i].getX()), o.getPoints()[i].getY() + u * (o.getPoints()[j].getY() - o.getPoints()[i].getY()));
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

	public void setSpeeds(double d) {
		// TODO Auto-generated method stub
		this.rotationSpeed *= d;
		this.movementSpeed *= d;
		this.inertiaSpeed *= d;
	}	
}
