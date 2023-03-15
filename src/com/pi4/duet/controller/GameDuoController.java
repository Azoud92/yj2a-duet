package com.pi4.duet.controller;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import com.pi4.duet.Sound;
import com.pi4.duet.model.Direction;
import com.pi4.duet.model.GamePlaneDuo;
import com.pi4.duet.model.Obstacle;
import com.pi4.duet.model.Point;
import com.pi4.duet.model.Settings;
import com.pi4.duet.model.Side;
import com.pi4.duet.model.State;
import com.pi4.duet.model.Wheel;
import com.pi4.duet.view.game.GameDuoView;
import com.pi4.duet.view.game.ObstacleView;
import com.pi4.duet.view.game.ObstacleView.CollisionView;


public class GameDuoController implements KeyListener{
	
	private GamePlaneDuo model;
	private GameDuoView view;
	private Sound defeatSound = new Sound("defeat.wav", false);
	private Sound reachedSound = new Sound("reached.wav", false);
	private Settings settings;
	
	private Sound music = new Sound("music.wav", true);
	
	private Timer gameTimer;
	
	private HomePageViewController hpvC;
	
	public GameDuoController(HomePageViewController hpvC, Settings settings){
		this.hpvC = hpvC;
		this.settings = settings;
		music.stop();
	}
		
	public void setModel(GamePlaneDuo model) { this.model = model; }
	
	public void setView(GameDuoView view) { this.view = view; }
	
	public GameDuoView getView() {
		return view;
	}
	
	public void refreshView() {
		view.refresh();
	}
	
	public void gameStop() {
		gameTimer.cancel();
		music.stop();
	}
	
	public void gameStart() {
		if (model.getState() != State.READY) return;
		model.setState(State.ON_GAME);
		model.setWheelRotating(Side.RIGHT, null);
		model.setWheelRotating(Side.LEFT, null);
		if (settings.getMusic()) music.play();
		
		gameTimer = new Timer();
		gameTimer.schedule(new TimerTask() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (model.getState() == State.ON_GAME){
					for (Obstacle o : model.getObstacles()) { // animation des obstacles pour les faire "tomber"
						o.update(0, 1);
						verifyCollision(Side.LEFT, o);
						verifyCollision(Side.RIGHT, o);
						//verifyObstacleReached(o);
						refreshView();
					}
					
					// animation du volant selon la direction souhaitée
					if (model.getWheelRotating(Side.LEFT) == null) stopMvt(Side.LEFT);
					if (model.getWheelRotating(Side.RIGHT) == null) stopMvt(Side.RIGHT);
					
					
					double inD = model.getWheel(Side.RIGHT).getInertia();
					
					if (model.getWheelBreaking(Side.RIGHT)) {
						if (inD > 0) {
							if(!settings.getInertie()) model.getWheel(Side.RIGHT).setInertia(model.getWheel(Side.RIGHT).rotationSpeed);
							else model.getWheel(Side.RIGHT).setInertia(inD - 0.0004);
							model.getWheel(Side.RIGHT).rotate(model.getLastRotation(Side.RIGHT));
							updateWheel(Side.RIGHT, model.getWheel(Side.RIGHT).getCenterBall2(), model.getWheel(Side.RIGHT).getCenterBall1());
							updateMvt(Side.RIGHT, model.getLastRotation(Side.RIGHT));
						}
						else {
							model.stopWheelBreaking(Side.RIGHT);
							model.getWheel(Side.RIGHT).setInertia(0);
							model.setLastRotation(Side.RIGHT, null);
						}						
					}
					
					if (model.getWheelRotating(Side.RIGHT) != null) {
						if (model.getWheelBreaking(Side.RIGHT) == false) {
							if (inD < model.getWheel(Side.RIGHT).rotationSpeed) {
								if(!settings.getInertie()) model.getWheel(Side.RIGHT).setInertia(model.getWheel(Side.RIGHT).rotationSpeed);
								else model.getWheel(Side.RIGHT).setInertia(inD + 0.0004);
							}
						}
					}
						
					if (model.getWheelRotating(Side.RIGHT) == Direction.ANTI_HORAIRE) {					
						model.getWheel(Side.RIGHT).rotate(Direction.ANTI_HORAIRE);
						updateWheel(Side.RIGHT, model.getWheel(Side.RIGHT).getCenterBall2(), model.getWheel(Side.RIGHT).getCenterBall1());
						updateMvt(Side.RIGHT, Direction.ANTI_HORAIRE);
					}
					else if (model.getWheelRotating(Side.RIGHT) == Direction.HORAIRE) {					
						model.getWheel(Side.RIGHT).rotate(Direction.HORAIRE);
						updateWheel(Side.RIGHT, model.getWheel(Side.RIGHT).getCenterBall2(), model.getWheel(Side.RIGHT).getCenterBall1());
						updateMvt(Side.RIGHT, Direction.HORAIRE);
					}	
					
					double inG = model.getWheel(Side.LEFT).getInertia();
					
					if (model.getWheelBreaking(Side.LEFT)) {
						if (inG > 0) {
							if(!settings.getInertie()) model.getWheel(Side.LEFT).setInertia(model.getWheel(Side.LEFT).rotationSpeed);
							else model.getWheel(Side.LEFT).setInertia(inG - 0.0004);
							model.getWheel(Side.LEFT).rotate(model.getLastRotation(Side.LEFT));
							updateWheel(Side.LEFT, model.getWheel(Side.LEFT).getCenterBall2(), model.getWheel(Side.LEFT).getCenterBall1());
							updateMvt(Side.LEFT, model.getLastRotation(Side.LEFT));
						}
						else {
							model.stopWheelBreaking(Side.LEFT);
							model.getWheel(Side.LEFT).setInertia(0);
							model.setLastRotation(Side.LEFT, null);
						}						
					}
					
					if (model.getWheelRotating(Side.LEFT) != null) {
						if (model.getWheelBreaking(Side.LEFT) == false) {
							if (inG < model.getWheel(Side.LEFT).rotationSpeed) {
								if(!settings.getInertie()) model.getWheel(Side.LEFT).setInertia(model.getWheel(Side.LEFT).rotationSpeed);
								else model.getWheel(Side.LEFT).setInertia(inG + 0.0004);
							}
						}
					}
						
					if (model.getWheelRotating(Side.LEFT) == Direction.ANTI_HORAIRE) {					
						model.getWheel(Side.LEFT).rotate(Direction.ANTI_HORAIRE);
						updateWheel(Side.LEFT, model.getWheel(Side.LEFT).getCenterBall2(), model.getWheel(Side.LEFT).getCenterBall1());
						updateMvt(Side.LEFT, Direction.ANTI_HORAIRE);
					}
					else if (model.getWheelRotating(Side.LEFT) == Direction.HORAIRE) {					
						model.getWheel(Side.LEFT).rotate(Direction.HORAIRE);
						updateWheel(Side.LEFT, model.getWheel(Side.LEFT).getCenterBall2(), model.getWheel(Side.LEFT).getCenterBall1());
						updateMvt(Side.LEFT, Direction.HORAIRE);
					}
					
				}
			}			
		}, 0, 1);
	}
	
	public void verifyCollision(Side side, Obstacle o) {
		int res = model.getWheel(side).isInCollision(o);
		ObstacleView ov = o.getController().getView();
		double oX =  o.getCoords()[0].getX();
		double oY = o.getCoords()[0].getY();
		if (res == 1) {			
			gameStop();
			model.setState(State.FINISHED);
			ov.addCollision(ov.new CollisionView(getCenterBall1(side).getX() - oX, getCenterBall1(side).getY() - oY, Color.red));
			if (settings.getEffects()) defeatSound.play();
			view.lostGame();
			
		}
		else if (res == 2) {			
			gameStop();
			model.setState(State.FINISHED);
			ov.addCollision(ov.new CollisionView(getCenterBall2(side).getX()- oX,getCenterBall2(side).getY() - oY, Color.blue));
			if (settings.getEffects()) defeatSound.play();
			view.lostGame();
			
		}
		else if (res == 3) {
			gameStop();
			model.setState(State.FINISHED);
			ov.addCollision(ov.new CollisionView(getCenterBall1(side).getX()- oX,getCenterBall1(side).getY() - oY, Color.red));
			ov.addCollision(ov.new CollisionView(getCenterBall2(side).getX()- oX,getCenterBall2(side).getY() - oY, Color.blue));	
			if (settings.getEffects()) defeatSound.play();
			view.lostGame();			
		}
		
	}
	
	public void updateWheel(Side side, Point blue, Point red) {
		// TODO Auto-generated method stub	
		view.setBallsPosition(side, blue, red);
		view.refresh();
	}

	
	public void updateMvt(Side side, Direction dir) { 
		double angle = getWheelangle(side);
		view.MvtBlueRotate(side, dir, angle); 
		view.MvtRedRotate(side, dir, angle);
		view.refresh();
	}
	
	public void playMusic() {
		music.play();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (model.getState() == State.ON_GAME){
			switch (e.getKeyCode()){
				case KeyEvent.VK_RIGHT: model.startWheelRotation(Side.RIGHT, Direction.ANTI_HORAIRE); break;
				case KeyEvent.VK_LEFT: model.startWheelRotation(Side.RIGHT, Direction.HORAIRE); break;
				
				case KeyEvent.VK_D: model.startWheelRotation(Side.LEFT, Direction.ANTI_HORAIRE); break;
				case KeyEvent.VK_Q: model.startWheelRotation(Side.LEFT, Direction.HORAIRE); break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (model.getState() == State.ON_GAME){
			switch(e.getKeyCode()) {
				case KeyEvent.VK_RIGHT: model.stopWheelRotation(Side.RIGHT); if(settings.getInertie()) model.startWheelBreaking(Side.RIGHT); break;
				case KeyEvent.VK_LEFT: model.stopWheelRotation(Side.RIGHT); if(settings.getInertie()) model.startWheelBreaking(Side.RIGHT); break;
				
				case KeyEvent.VK_Q: model.stopWheelRotation(Side.LEFT); if(settings.getInertie()) model.startWheelBreaking(Side.LEFT); break;
				case KeyEvent.VK_D: model.stopWheelRotation(Side.LEFT); if(settings.getInertie()) model.startWheelBreaking(Side.LEFT); break;
				
				case KeyEvent.VK_SPACE:
					model.stopWheelRotation(Side.RIGHT);
					model.stopWheelRotation(Side.LEFT);
					model.setState(State.PAUSED);
					view.affichePause();
					break;				
			}
		}
		else {
			if (KeyEvent.VK_SPACE == e.getKeyCode()) {
				model.setState(State.ON_GAME);
			}
		}
	}
	
	public void stopMvt(Side side) {
		view.stopMvt(side);		
	}
	
	public void affMenu() {
		hpvC.runHomePage();
		if(settings.getMusic()) { hpvC.runMusic(); }
		view.setVisible(false);
	}	
	
	public void stopMusic() {
		music.stop();
		
	}
	
	public int getWheelRadius() {
		// TODO Auto-generated method stub
		return model.getWheel(Side.RIGHT).radius;
	}
	
	public int getBallRadius() {
		// TODO Auto-generated method stub
		return model.getWheel(Side.RIGHT).ballRadius;
	}
	
	public Point getCenterBall1(Side side) {
		return model.getWheel(side).getCenterBall1();
	}
	
	public Point getCenterBall2(Side side) {
		return model.getWheel(side).getCenterBall2();
	}
	
	public double getWheelangle(Side side) {
		return model.getWheel(side).getAngle();
	}
	
	public Wheel getWheel(Side side) {
		return model.getWheel(side);
	}
	
	public boolean isBackgroundEnabled() { return settings.getBackground(); }


}
