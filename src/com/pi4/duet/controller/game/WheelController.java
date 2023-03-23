package com.pi4.duet.controller.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.pi4.duet.Point;
import com.pi4.duet.model.Direction;
import com.pi4.duet.model.game.State;
import com.pi4.duet.model.game.Wheel;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.game.WheelView;

public class WheelController implements KeyListener {

	private Wheel model;
	private WheelView view;
	
	private GameController game;
	
	private Settings settings;
	
	public WheelController(Settings settings, GameController game) {
		this.settings = settings;
		this.game = game;
	}
	
	public void setModel(Wheel model) { this.model = model; }
	public void setView(WheelView view) { this.view = view; }
	
	public Point getCenterBall_1() { return model.getCenterBall1(); }
	public Point getCenterBall_2() { return model.getCenterBall2(); }
	
	public double getBallRadius() { return model.getBallRadius(); }	
	public double getWheelRadius() { return model.radius; }	
	public Point getWheelCenter() { return model.getCenter(); }
	public double getWheelSpeed() { return model.rotationSpeed; }
	
	public void animateWheel() {
		// animation du volant selon la direction souhaitée
		if (model.getWheelRotating() == null) view.stopMvt();
		double in = model.getInertia();
		
		if (model.isWheelBreaking()) {
			if (in > 0) {
				if(!settings.getInertie()) model.setInertia(model.rotationSpeed);
				else model.setInertia(in - 0.0004);
				model.rotate(model.getLastRotation());
				view.updateBall_1(new Point(model.getCenterBall1().getX() - model.getBallRadius(),
						model.getCenterBall1().getY() - model.getBallRadius()));
				view.updateBall_2(new Point(model.getCenterBall2().getX() - model.getBallRadius(),
						model.getCenterBall2().getY() - model.getBallRadius()));
				updateMvt(model.getLastRotation());
			}
			else {
				model.setWheelBreaking(false);
				model.setInertia(0);
				model.setLastRotation(null);
			}						
		}
		
		if (model.getWheelRotating() != null) {
			if (model.isWheelBreaking() == false) {
				if (in < model.rotationSpeed) {
					if(!settings.getInertie()) model.setInertia(model.rotationSpeed);
					else model.setInertia(in + 0.0004);
				}
			}
		}
			
		if (model.getWheelRotating() == Direction.ANTI_HORAIRE) {					
			model.rotate(Direction.ANTI_HORAIRE);
			view.updateBall_1(new Point(model.getCenterBall1().getX() - model.getBallRadius(),
					model.getCenterBall1().getY() - model.getBallRadius()));
			view.updateBall_2(new Point(model.getCenterBall2().getX() - model.getBallRadius(),
					model.getCenterBall2().getY() - model.getBallRadius()));
			updateMvt(Direction.ANTI_HORAIRE);
		}
		else if (model.getWheelRotating() == Direction.HORAIRE) {					
			model.rotate(Direction.HORAIRE);
			view.updateBall_1(new Point(model.getCenterBall1().getX() - model.getBallRadius(),
					model.getCenterBall1().getY() - model.getBallRadius()));
			view.updateBall_2(new Point(model.getCenterBall2().getX() - model.getBallRadius(),
					model.getCenterBall2().getY() - model.getBallRadius()));
			updateMvt(Direction.HORAIRE);
		}	
	}
	
	public void updateMvt(Direction dir) { 
		double angle = model.getAngle();
		view.mvt_1_Rotate(dir, angle); 
		view.mvt_2_Rotate(dir, angle);
	}
	
	public void stopWheelRotation() { model.stopWheelRotation(); }
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (game.getState() == State.ON_GAME){
			switch (e.getKeyCode()){
				case KeyEvent.VK_RIGHT: 
					if (!model.isWheelBreaking()) model.setWheelRotating(Direction.ANTI_HORAIRE);
					else if (model.getLastRotation() != null) {
						switch (model.getLastRotation()) {
						case HORAIRE:
							model.setInertia(model.getInertia() * 0.9);
							break;
						case ANTI_HORAIRE:
							if (model.getInertia() <= model.rotationSpeed) model.setInertia(model.getInertia() + 0.004);
							break;						
						}
					}
					break;
				case KeyEvent.VK_LEFT:
					if (!model.isWheelBreaking()) model.setWheelRotating(Direction.HORAIRE);
					else if (model.getLastRotation() != null) {
						switch (model.getLastRotation()) {
						case HORAIRE:
							if (model.getInertia() <= model.rotationSpeed) model.setInertia(model.getInertia() + 0.004);
							break;
						case ANTI_HORAIRE:
							model.setInertia(model.getInertia() * 0.9);
							break;					
						}
					}
					break;
				case KeyEvent.VK_CONTROL:
					System.out.println("A");
					System.out.println(model.getCenterBall1().getX());
					model.moveLeft();
					System.out.println(model.getCenterBall1().getX());
					System.out.println("B");
					break;				
				case KeyEvent.VK_ALT:
					model.moveRight();
					break;
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (game.getState() == State.ON_GAME) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_RIGHT: 
				model.stopWheelRotation();
				if (settings.getInertie()) model.setWheelBreaking(true);
				break;
			case KeyEvent.VK_LEFT:
				model.stopWheelRotation();
				if (settings.getInertie()) model.setWheelBreaking(true);
				break;
			}
		}		
	}
	public void setWheelRotating(Direction dir) {
		// TODO Auto-generated method stub
		model.setWheelRotating(dir);
	}
	
	
}
