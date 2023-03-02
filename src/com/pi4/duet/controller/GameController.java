package com.pi4.duet.controller;

import java.awt.Dimension;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



import com.pi4.duet.model.Direction;
import com.pi4.duet.model.GamePlane;
import com.pi4.duet.model.Obstacle;
import com.pi4.duet.model.Point;
import com.pi4.duet.view.GameView;
import com.pi4.duet.view.ObstacleView;

public class GameController implements KeyListener {

	private GamePlane model;
	private GameView view;

	private HomePageViewController hpvC;

	private boolean pause=false;
	
	public GameController(HomePageViewController hpvC){
		this.hpvC = hpvC;
	}
		
	public void setModel(GamePlane model) { this.model = model; }
	
	public void setView(GameView view) { this.view = view; }
	
	public void refreshView() {
		view.refresh();
	}
	
	public void verifyCollision(Obstacle o) {
		if (model.wheel.isInCollision(o)) {
			model.gameStop();
			view.lostGame();
		}
	}
	
	public void testObstacles() {
		Point[] rect = new Point[4];
		rect[0] = new Point(model.width / 3, 100);
		rect[1] = new Point(model.width / 3 + 100, 100);
		rect[2] = new Point(model.width / 3 + 100, 120);
		rect[3] = new Point(model.width / 3, 120);
		
		Point centerRect = new Point(model.width / 3 + 50, 110);
				
		ObstacleController oc = new ObstacleController();
		Obstacle o = new Obstacle(rect, centerRect, oc);
		oc.setModel(o);
		ObstacleView ov = new ObstacleView(oc, model.width, model.height);
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
		view.MvtBlueRotate(dir, angle ); 
		view.MvtRedRotate(dir, angle);
	}
	
	public void resetAngleMvt() {
		view.resetAngleMvt();		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (!pause){
			// TODO Auto-generated method stub
			if (!model.isPaused()){
				switch(e.getKeyCode()) {
					case KeyEvent.VK_RIGHT: model.stopWheelRotation(); resetAngleMvt(); break;
					case KeyEvent.VK_LEFT: model.stopWheelRotation(); resetAngleMvt(); break;
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
		hpvC.runParty(new Dimension(view.getSize().width * 3 , view.getSize().height), hpvC.getWindow());
		view.setVisible(false);
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
