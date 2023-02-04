package com.pi4.duet.model;
import java.lang.Math;

public class Wheel {
	
	private int radius;
	private Point center;
	
	public final Ball ball_1, ball_2;
	
	private double angle;
	private double rotationSpeed;
	
	public Wheel() {
		this.center=new Point(0,0);
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
		setAngle(getAngle()-0.01745*5);
		//Changement coordonées Ball 2;
		ball_2.getCenterBall().setX(radius*Math.cos(angle)+center.getX());
		ball_2.getCenterBall().setY(radius*Math.sin(angle)+center.getY());
		//Changement coordonées Ball 1;
		ball_1.getCenterBall().setX(-ball_2.getCenterBall().getX());
		ball_1.getCenterBall().setY(-ball_2.getCenterBall().getY());
	}
	public void rotateContreHoraire(){
		setAngle(getAngle()+0.01745*5);
		//Changement coordonées Ball 2;
		ball_2.getCenterBall().setX(radius*Math.cos(angle)+center.getX());
		ball_2.getCenterBall().setY(radius*Math.sin(angle)+center.getY());
		//Changement coordonées Ball 1;
		ball_1.getCenterBall().setX(-ball_2.getCenterBall().getX());
		ball_1.getCenterBall().setY(-ball_2.getCenterBall().getY());

	}
	public boolean isLose(Obstacle o){
		if(ball_1.isInCollision(o)|| ball_2.isInCollision(o)) return true;
		return false;
	}
	
	private class Ball {
		
		int radius;
		Point centerBall;
		
		public Ball(Point centerBall) {
			this.radius=5;
			this.centerBall=centerBall;
		}
		
		int getRadius() { return radius; }
		void setRadius(int r) { radius = r; }
		
		Point getCenterBall() { return centerBall; }
		void setCenter(Point c) { center = c; }
		
		boolean isInCollision(Obstacle o) {
			return false;
		}
		
	}

	public static void main(String[] args) {
		Wheel w=new Wheel();
		System.out.println("Ball 2 "+w.ball_2.centerBall.getX()+" "+w.ball_2.centerBall.getY());
		System.out.println("Ball 1 "+w.ball_1.centerBall.getX()+" "+w.ball_1.centerBall.getY());


		/*System.out.println("Ball 2");
		System.out.println(50*Math.cos(w.angle)+" "+50*Math.sin(w.angle));
		w.setAngle(Math.PI/2);
		//System.out.println(w.angle);
		System.out.println(50*Math.cos(w.angle)+" "+50*Math.sin(w.angle));*/
		System.out.println("TEST");
		w.rotateHoraire();
		System.out.println(w.getAngle());
		System.out.println("Ball 2 "+w.ball_2.centerBall.getX()+" "+w.ball_2.centerBall.getY());
		System.out.println("Ball 1 "+w.ball_1.centerBall.getX()+" "+w.ball_1.centerBall.getY());
		w.rotateHoraire();
		System.out.println("Ball 2 "+w.ball_2.centerBall.getX()+" "+w.ball_2.centerBall.getY());
		System.out.println("Ball 1 "+w.ball_1.centerBall.getX()+" "+w.ball_1.centerBall.getY());




	}

	
}
