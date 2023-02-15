package com.pi4.duet.controller;

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
		
	public void setModel(GamePlane model) { this.model = model; }
	
	public void setView(GameView view) { this.view = view; }
	
	public void refreshView() {
		view.refresh();
	}
	
	public void verifyCollision(Obstacle o) {
		if (model.wheel.isInCollision(o)) {
			model.gameStop();
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
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			model.stopWheelRotation();
			break;
		case KeyEvent.VK_LEFT:
			model.stopWheelRotation();
			break;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
        case KeyEvent.VK_RIGHT:
        	model.startWheelRotation(Direction.ANTI_HORAIRE);
        	break;
            
        case KeyEvent.VK_LEFT:
        	model.startWheelRotation(Direction.HORAIRE);
        	break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub		
	}
	
	public Point getCenterBall1() {
		return model.wheel.getCenterBall1();
	}
	
	public Point getCenterBall2() {
		return model.wheel.getCenterBall2();
	}

	public int getWheelRadius() {
		// TODO Auto-generated method stub
		return model.wheel.radius;
	}

	public int getBallRadius() {
		// TODO Auto-generated method stub
		return model.wheel.ballRadius;
	}
	
	public Point getWheelCenter() {
		return model.wheel.getCenter();
	}	
}
