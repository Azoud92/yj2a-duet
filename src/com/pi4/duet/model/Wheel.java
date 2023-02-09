package com.pi4.duet.model;
import java.lang.Math;

public class Wheel {



	private int radius;
	private Point center;
	
	public  Ball ball_1, ball_2;
	
	private double angle;
	private double rotationSpeed;

	public Wheel(Point center) {
		this.center= center;
		this.radius=50;
		ball_2=new Ball(new Point(center.getX()+radius, center.getY()));
		ball_1=new Ball(new Point(center.getX()-radius, center.getY()));
		this.angle=0;
		this.rotationSpeed=1;
	}
	
	public int getRadius() { return radius; }
	public void setRadius(int r) { radius = r; }
	
	public Point getCenter() { return center; }
	public void setCenter(Point c) { center = c; }

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getAngle() { return angle; }
	
	public double getRotationSpeed() { return rotationSpeed; }
	public void setRotationSpeed(double rs) { rotationSpeed = rs; }
	/*
	L'angle thêta correspond à l'angle entre l'axe des abcisses et le vecteur du part du point
	central du volant vers la balle 2.
	Par ex si on  rotateContreHoraire() l'angle est est positif, alors que si rotateHoraire() l'angle est positif
	 */
	
	public void rotateHoraire() {
		setAngle(getAngle()-0.017453292519943295*10);
		//Changement coordonées Ball 2;
		ball_2.getCenterBall().setX(radius*Math.cos(angle)+center.getX());
		ball_2.getCenterBall().setY(radius*Math.sin(angle)+center.getY());
		//Changement coordonées Ball 1;
		double dist1=ball_2.getCenterBall().getX()-center.getX();
		double dist2=ball_2.getCenterBall().getY()-center.getY();
		ball_1.getCenterBall().setX(center.getX()-dist1);
		ball_1.getCenterBall().setY(center.getY()-dist2);
	}
	public void rotateContreHoraire(){
		setAngle(getAngle()+0.017453292519943295*10);
		//Changement coordonées Ball 2;
		ball_2.getCenterBall().setX(radius*Math.cos(angle)+center.getX());
		ball_2.getCenterBall().setY(radius*Math.sin(angle)+center.getY());
		//Changement coordonées Ball 1;
		double dist1=ball_2.getCenterBall().getX()-center.getX();
		double dist2=ball_2.getCenterBall().getY()-center.getY();
		ball_1.getCenterBall().setX(center.getX()-dist1);
		ball_1.getCenterBall().setY(center.getY()-dist2);

	}
	public boolean isLose(Obstacle o){
		//Méthode à appeler chaque milliseconde pour vérifier si une des balle est en contact avec un des segments du quadrilatère

		if(ball_1.isInCollision(o)|| ball_2.isInCollision(o)) return true;
		if(ball_1.getCenterBall().getX()==370&&ball_1.getCenterBall().getY()==965) System.out.println("CHIEN");
		if(ball_2.getCenterBall().getX()==370&&ball_2.getCenterBall().getY()==965) System.out.println("CHIEN");
		return false;
	}
	public Ball getBall_1() {
		return ball_1;
	}

	public void setBall_1(Ball ball_1) {
		this.ball_1 = ball_1;
	}

	public Ball getBall_2() {
		return ball_2;
	}

	public void setBall_2(Ball ball_2) {
		this.ball_2 = ball_2;
	}
	
	public class Ball {
		
		int radius;
		Point centerBall;
		
		public Ball(Point centerBall) {
			this.radius=5;
			this.centerBall=centerBall;
		}
		
		int getRadius() { return radius; }
		void setRadius(int r) { radius = r; }
		
		public Point getCenterBall() { return centerBall; }
		void setCenter(Point c) { center = c; }

		double distance(Point a,Point b){
			return Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY()));
		}

		//Méthode intersecte
		public boolean isInCollision(Obstacle o){
			if(distance(o.getCoords()[0],centerBall)+distance(o.getCoords()[1],centerBall)<=distance(o.getCoords()[0],o.getCoords()[1])+5){
				return true;
			}
			if(distance(o.getCoords()[0],centerBall)+distance(o.getCoords()[2],centerBall)<=distance(o.getCoords()[0],o.getCoords()[2])+5){
				return true;
			}
			if(distance(o.getCoords()[1],centerBall)+distance(o.getCoords()[3],centerBall)<=distance(o.getCoords()[1],o.getCoords()[3])+5){
				return true;
			}
			if(distance(o.getCoords()[2],centerBall)+distance(o.getCoords()[3],centerBall)<=distance(o.getCoords()[2],o.getCoords()[3])+5){
				return true;
			}
			return false;

		}
	}


	
}
