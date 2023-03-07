package com.pi4.duet.controller;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.pi4.duet.Sound;
import com.pi4.duet.model.Direction;
import com.pi4.duet.model.GamePlane;
import com.pi4.duet.model.Obstacle;
import com.pi4.duet.model.Point;
import com.pi4.duet.view.GameView;
import com.pi4.duet.view.ObstacleView;

public class GameController implements KeyListener {

	private GamePlane model;
	private GameView view;
	private Sound defeatSound = new Sound("defeat.wav");
	
	
	private HomePageViewController hpvC;

	private boolean pause=false;
	
	public GameController(HomePageViewController hpvC){
		this.hpvC = hpvC;
	}
		
	public void setModel(GamePlane model) { this.model = model; }
	
	public void setView(GameView view) { this.view = view; }
	public GameView getView() {
		return view;
	}
	
	public void refreshView() {
		view.refresh();
	}
	
	public void verifyCollision(Obstacle o) {
		int res = model.wheel.isInCollision(o);
		ObstacleView ov = o.getController().getView();
		double oX =  o.getCoords()[0].getX();
		double oY = o.getCoords()[0].getY();

		if (res == 1) {
			
			model.gameStop();
			
			System.out.println(getCenterBall1().getX()+" "+ getCenterBall1().getY());
			ov.addCollision(ov.new CollisionView(getCenterBall1().getX() - oX, getCenterBall1().getY() - oY, Color.red));
			defeatSound.play();
			view.lostGame();
			
		}
		else if (res == 2) {
			
			model.gameStop();
			System.out.println(getCenterBall1().getX()+" "+ getCenterBall1().getY());
			ov.addCollision(ov.new CollisionView(getCenterBall2().getX()- oX,getCenterBall2().getY() - oY, Color.blue));
			defeatSound.play();
			view.lostGame();
			
		}
		else if (res == 3) {
			System.out.println(getCenterBall1().getX()+" "+ getCenterBall1().getY());
			model.gameStop();
			ov.addCollision(ov.new CollisionView(getCenterBall1().getX()- oX,getCenterBall1().getY() - oY, Color.red));
			ov.addCollision(ov.new CollisionView(getCenterBall2().getX()- oX,getCenterBall2().getY() - oY, Color.blue));	
			defeatSound.play();
			view.lostGame();
			
		}
		
	}
	
	
	public void testObstacles() {
		Point[] rect = new Point[4];
		rect[0] = new Point(model.width / 2, 100);
		rect[1] = new Point(model.width / 2 + 100, 100);
		rect[2] = new Point(model.width / 2 + 100, 120);
		rect[3] = new Point(model.width / 2, 120);
		
		Point centerRect = new Point(model.width / 2 + 50, 110);
				
		ObstacleController oc = new ObstacleController();
		Obstacle o = new Obstacle(rect, centerRect, oc);
		oc.setModel(o);
		ObstacleView ov = new ObstacleView(oc, (int)(rect[1].getX() - rect[0].getX()),(int)(rect[3].getY() - rect[0].getY()),(int) rect[0].getX(), (int) rect[0].getY(),this);
		//ObstacleView ov = new ObstacleView(oc, model.width, model.height, 0, 0, this);
		
		oc.setView(ov);	
		model.addObstacle(o);
		view.addObstacle(ov);		
	}

	
	public void putTestObstacle(Obstacle o) {
		ObstacleController oc = new ObstacleController();
		o.setController(oc);
		oc.setModel(o);
		ObstacleView ov = new ObstacleView(oc, (int) o.getWidth(), (int) o.getHeight(), (int) o.getPos().getX(), (int) o.getPos().getY(), this);
		oc.setView(ov);
		model.addObstacle(o);
		view.addObstacle(ov);		
	}

		
	public void updateWheel(Point blue, Point red) {
		// TODO Auto-generated method stub	
		view.setBallsPosition(blue, red);
	}

	
	public void updateMvt(Direction dir) { 
		double angle = getWheelangle();
		view.MvtBlueRotate(dir, angle); 
		view.MvtRedRotate(dir, angle);
	}
	

	
	@Override
	public void keyReleased(KeyEvent e) {
		if (!pause){
			// TODO Auto-generated method stub
			if (!model.isPaused()){
				switch(e.getKeyCode()) {
					case KeyEvent.VK_RIGHT: model.stopWheelRotation(); break;
					case KeyEvent.VK_LEFT: model.stopWheelRotation(); break;
					case KeyEvent.VK_SPACE:{
						model.stopWheelRotation();
						model.gamePausedOrResumed();
						view.affichePause();
						break;
					}
				}
			}
			else {
				if (KeyEvent.VK_SPACE == e.getKeyCode()) {
					model.gamePausedOrResumed();
				}
			}
		}
		pause = !pause;
	}




	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (!model.isPaused()){
			switch (e.getKeyCode()){
				case KeyEvent.VK_RIGHT: model.startWheelRotation(Direction.ANTI_HORAIRE); break;
				case KeyEvent.VK_LEFT: model.startWheelRotation(Direction.HORAIRE); break;
			}
		}
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub		
	}
	
	
	public void affMenu() {
		hpvC.runHomePage();
		view.setVisible(false);
	}
	
	
	public void replay() {
		hpvC.runNewParty(hpvC.getWindow());
	}
	
	public void stopMvt() {
		view.stopMvt();
		
	}
	
	public Dimension getSize() {
		return view.getSize();

	}

	
	public Point getCenterBall1() {
		return model.wheel.getCenterBall1();
	}
	
	public Point getCenterBall2() {
		return model.wheel.getCenterBall2();
	}
	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public int getWheelRadius() {
		// TODO Auto-generated method stub
		return model.wheel.radius;
	}

	public boolean isPause() {
		return pause;
	}

	public GamePlane getModel() {
		return model;
	}

	public int getBallRadius() {
		// TODO Auto-generated method stub
		return model.wheel.ballRadius;
	}
	public double getWheelSpeed() {
		return model.wheel.rotationSpeed;
	}
	
	public double getWheelangle() {
		return model.wheel.getAngle();
	}
	
	public Point getWheelCenter() {
		return model.wheel.getCenter();
	}
		
}
