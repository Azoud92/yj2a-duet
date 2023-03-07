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
		
		public boolean isInCollision1(Obstacle o){
			for(int i=0;i<o.getCoords().length;i++){
				
				int j = (i + 1) % o.getCoords().length;
				
				double d1= distance(o.getCoords()[i],centerBall);
				double d2= distance(o.getCoords()[j],centerBall);
				double d3= distance(o.getCoords()[i],o.getCoords()[j]);
				//System.out.println("d1 : " + (int) d1 + "; d2 : " + (int) d2 + ", d3 : " + (int) d3 + "; d1 + d2 = " + (int) (d1 + d2));
				if(d1+d2<=d3 + 0.01) return true;
			}
			
			return false;
		}
		
		public boolean isInCollision(Obstacle o) {			
			// Vérifier d'abord si le cercle est dans le polygone
	        if (isInside(centerBall.getX(), centerBall.getY(), o)) {
	            return true;
	        }

	        // Projet des points sur les axes perpendiculaires aux côtés du polygone
	        double[] polygonNormalsX = new double[o.getCoords().length];
	        double[] polygonNormalsY = new double[o.getCoords().length];
	        for (int i = 0; i < o.getCoords().length; i++) {
	            double dx = o.getCoords()[(i + 1) % o.getCoords().length].getX() - o.getCoords()[i].getX();
	            double dy = o.getCoords()[(i + 1) % o.getCoords().length].getY() - o.getCoords()[i].getY();
	            double len = Math.sqrt(dx*dx + dy*dy);
	            polygonNormalsX[i] = dy / len;
	            polygonNormalsY[i] = -dx / len;
	        }

	        // Projeter le cercle sur chaque axe et vérifier s'il y a une collision
	        double[] circleProjection = project(centerBall.getX(), centerBall.getY(), polygonNormalsX[0], polygonNormalsY[0]);
	        double minOverlap = circleProjection[1] - circleProjection[0];
	        for (int i = 1; i < o.getCoords().length; i++) {
	            circleProjection = project(centerBall.getX(), centerBall.getY(), polygonNormalsX[i], polygonNormalsY[i]);
	            double overlap = circleProjection[1] - circleProjection[0];
	            if (overlap < 0) {
	                return false;  // Aucune collision possible
	            } else if (overlap < minOverlap) {
	                minOverlap = overlap;
	            }
	        }

	        // Projeter le polygone sur chaque axe et vérifier s'il y a une collision
	        for (int i = 0; i < o.getCoords().length; i++) {
	            double[] polygonProjection = project(o.getCoords()[i].getX(), o.getCoords()[i].getY(), polygonNormalsX[i], polygonNormalsY[i]);
	            double overlap = getOverlap(circleProjection, polygonProjection);
	            if (overlap < 0) {
	                return false;  // Aucune collision possible
	            } else if (overlap < minOverlap) {
	                minOverlap = overlap;
	            }
	        }

	        return true;  // Collision détectée
	    
		}
		
		// Vérifier si un point est à l'intérieur d'un polygone
	    private boolean isInside(double x, double y, Obstacle o) {
	    	int intersections = 0;
	        for (int i = 0; i < o.getCoords().length; i++) {
	            int j = (i + 1) % o.getCoords().length;
	            if ((o.getCoords()[i].getY() > centerBall.getY()) != (o.getCoords()[j].getY() > centerBall.getY())) {
	                double slope = (o.getCoords()[j].getX() - o.getCoords()[i].getX()) / (o.getCoords()[j].getY() - o.getCoords()[i].getY());
	                double intersectionX = (centerBall.getY() - o.getCoords()[i].getY()) * slope + o.getCoords()[i].getX();
	                if (centerBall.getX() < intersectionX) {
	                    intersections++;
	                }
	            }
	        }
	        return intersections % 2 == 1;
	    }
	    
	    // Projet d'un point sur un axe
	    private static double[] project(double x, double y, double axisX, double axisY) {
	    	double dotProduct = x * axisX + y * axisY;
	        double projectionX = dotProduct * axisX;
	        double projectionY = dotProduct * axisY;
	        return new double[] { projectionX, projectionY };
	    }


	    // Calculer le chevauchement entre deux projections
	    private static double getOverlap(double[] projection1, double[] projection2) {
	        return Math.min(projection1[1], projection2[1]) - Math.max(projection1[0], projection2[0]);
	    }

		
		
	}
}
