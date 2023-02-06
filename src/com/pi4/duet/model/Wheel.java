package com.pi4.duet.model;
import java.lang.Math;


public class Wheel {
	
	private int radius;
	private Point center;
	
	public  Ball ball_1, ball_2;
	
	private double angle;
	private double rotationSpeed;
	
	public Wheel() {
		this.center=new Point(200,150);
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
		setAngle(getAngle()-0.01745*10);
		//Changement coordonées Ball 2;
		ball_2.getCenterBall().setX(radius*Math.cos(angle)+center.getX());
		ball_2.getCenterBall().setY(radius*Math.sin(angle)+center.getY());
		//Changement coordonées Ball 1;
		ball_1.getCenterBall().setX(-ball_2.getCenterBall().getX());
		ball_1.getCenterBall().setY(-ball_2.getCenterBall().getY());
	}
	public void rotateContreHoraire(){
		setAngle(getAngle()+0.01745*10);
		//Changement coordonées Ball 2;
		ball_2.getCenterBall().setX(radius*Math.cos(angle)+center.getX());
		ball_2.getCenterBall().setY(radius*Math.sin(angle)+center.getY());
		//Changement coordonées Ball 1;
		ball_1.getCenterBall().setX(-ball_2.getCenterBall().getX());
		ball_1.getCenterBall().setY(-ball_2.getCenterBall().getY());

	}
	public boolean isLose(Obstacle o){
		//Méthode à appeler chaque milliseconde pour vérifier si une des balle est en contact avec un des segments du quadrilatère
		if(ball_1.isInCollision(o)|| ball_2.isInCollision(o)) return true;
		return false;
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
		
		Point getCenterBall() { return centerBall; }
		void setCenter(Point c) { center = c; }
		double distance(Point a,Point b){
			return Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY()));
		}
		
		boolean isInCollision(Obstacle o) {
			/*Pour résumer : il y a quatres conditions qui verifient si une des balles touchent un des quatres segment
			du quadrilatère

			Pour cela, on prend deux points, du quadrilatère et  on créer la fonction affine associé et on regarde si le
			centre de la balle vérifie l'équation de la droite engendrée par le fonction, si c'est le cas c'est que la
			balle appartient à la droite.
			De plus il faut rajouter deux conditions qui vérifie que la distance entre la balle et les deux points est
			inférieur à la distance totale : cette conditions reflète le fait que la balle est dans la droite mais
			aussi dans le segment des deux points
			 */

			//Premier segment
			double dist1=distance(o.getCoords()[0],o.getCoords()[1]);
			if(o.getCoords()[0].getX()-o.getCoords()[1].getX()==0){
				if(o.getCoords()[0].getX()== centerBall.getX()&&distance(o.getCoords()[0],centerBall)<=dist1&&distance(o.getCoords()[1],centerBall)<=dist1){
					return true;
				}
			}else{
				double coefD1=(o.getCoords()[0].getY()-o.getCoords()[1].getY())/(o.getCoords()[0].getX()-o.getCoords()[1].getX());
				double ordonneO1=o.getCoords()[0].getY()-coefD1*o.getCoords()[0].getX();
				if(distance(o.getCoords()[0],centerBall)<=dist1&&distance(o.getCoords()[1],centerBall)<=dist1&&centerBall.getY()==coefD1*centerBall.getX()+ordonneO1) {
					return true;
				}
			}
			//Deuxième segment
			double dist2=distance(o.getCoords()[0],o.getCoords()[2]);
			if(o.getCoords()[0].getX()-o.getCoords()[2].getX()==0){
				if(o.getCoords()[0].getX()== centerBall.getX()&&distance(o.getCoords()[0],centerBall)<=dist2&&distance(o.getCoords()[2],centerBall)<=dist2){
					return true;
				}
			}else{
				double coefD2=(o.getCoords()[0].getY()-o.getCoords()[2].getY())/(o.getCoords()[0].getX()-o.getCoords()[2].getX());
				double ordonneO2=o.getCoords()[0].getY()-coefD2*o.getCoords()[0].getX();
				if(distance(o.getCoords()[0],centerBall)<=dist2&&distance(o.getCoords()[2],centerBall)<=dist2&&centerBall.getY()==coefD2*centerBall.getX()+ordonneO2){
					return true;
				}
			}
			//Troisième segment
			double dist3=distance(o.getCoords()[1],o.getCoords()[3]);
			if(o.getCoords()[1].getX()-o.getCoords()[3].getX()==0){
				if(o.getCoords()[1].getX()== centerBall.getX()&&distance(o.getCoords()[1],centerBall)<=dist3&&distance(o.getCoords()[3],centerBall)<=dist3){
					return true;
				}
			}else{
				double coefD3=(o.getCoords()[1].getY()-o.getCoords()[3].getY())/(o.getCoords()[1].getX()-o.getCoords()[3].getX());
				double ordonneO3=o.getCoords()[1].getY()-coefD3*o.getCoords()[1].getX();
				if(distance(o.getCoords()[1],centerBall)<=dist3&&distance(o.getCoords()[3],centerBall)<=dist3&&centerBall.getY()==coefD3*centerBall.getX()+ordonneO3){
					return true;
				}
			}

			//Quatrième segment
			double dist4=distance(o.getCoords()[2],o.getCoords()[3]);
			if(o.getCoords()[2].getX()-o.getCoords()[3].getX()==0){
				if(o.getCoords()[2].getX()== centerBall.getX()&&distance(o.getCoords()[2],centerBall)<=dist4&&distance(o.getCoords()[3],centerBall)<=dist4){
					return true;
				}
			}else{
				double coefD4=(o.getCoords()[2].getY()-o.getCoords()[3].getY())/(o.getCoords()[2].getX()-o.getCoords()[3].getX());
				double ordonneO4=o.getCoords()[2].getY()-coefD4*o.getCoords()[2].getX();
				if(distance(o.getCoords()[2],centerBall)<=dist4&&distance(o.getCoords()[3],centerBall)<=dist4&&centerBall.getY()==coefD4*centerBall.getX()+ordonneO4){
					return true;
				}
			}

			return false;
			//La méthode marche uniquement pour des quadrilatère
		}
		
	}

	public static void main(String[] args) {
		Wheel w=new Wheel();
		System.out.println("Ball 2 "+w.ball_2.centerBall.getX()+" "+w.ball_2.centerBall.getY());
		System.out.println("Ball 1 "+w.ball_1.centerBall.getX()+" "+w.ball_1.centerBall.getY());
		Point p=new Point(250,100);
		Obstacle o=new Obstacle(100,50,p,0,0,0);
		System.out.println(o.getCoords()[0].getX()+" "+o.getCoords()[0].getY());
		System.out.println(o.getCoords()[2].getX()+" "+o.getCoords()[2].getY());
		System.out.println(w.isLose(o));




	}

	
}
