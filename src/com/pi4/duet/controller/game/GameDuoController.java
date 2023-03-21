package com.pi4.duet.controller.game;

import java.awt.Color;  
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import com.pi4.duet.Point;
import com.pi4.duet.Sound;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.model.Direction;
import com.pi4.duet.model.game.GamePlaneDuo;
import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.model.game.Side;
import com.pi4.duet.model.game.State;
import com.pi4.duet.model.game.Wheel;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.game.GameDuoView;
import com.pi4.duet.view.game.ObstacleView;


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
		model.setWheelRotating(Side.LOW, null);
		model.setWheelRotating(Side.HIGH, null);
		if (settings.getMusic()) music.play();
		
		gameTimer = new Timer();
		gameTimer.schedule(new TimerTask() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (model.getState() == State.ON_GAME){
					for (Obstacle o : model.getObstacles(Side.LOW)) { // animation des obstacles pour les faire "tomber"
						o.update(0, 1);
						verifyCollision(Side.HIGH, o);
						verifyCollision(Side.LOW, o);
						verifyObstacleReached(Side.LOW, o);
						refreshView();
					}
					for (Obstacle o : model.getObstacles(Side.HIGH)) { // animation des obstacles pour les faire "tomber"
						o.update(0, -1);
						verifyCollision(Side.HIGH, o);
						verifyCollision(Side.LOW, o);
						verifyObstacleReached(Side.HIGH, o);
						refreshView();
					}
					
					// animation du volant selon la direction souhaitée
					if (model.getWheelRotating(Side.HIGH) == null) stopMvt(Side.HIGH);
					if (model.getWheelRotating(Side.LOW) == null) stopMvt(Side.LOW);
					
					
					double inB = model.getWheel(Side.LOW).getInertia();
					
					if (model.getWheelBreaking(Side.LOW)) {
						if (inB > 0) {
							if(!settings.getInertie()) model.getWheel(Side.LOW).setInertia(model.getWheel(Side.LOW).rotationSpeed);
							else model.getWheel(Side.LOW).setInertia(inB - 0.0004);
							model.getWheel(Side.LOW).rotate(model.getLastRotation(Side.LOW));
							updateWheel(Side.LOW, model.getWheel(Side.LOW).getCenterBall2(), model.getWheel(Side.LOW).getCenterBall1());
							updateMvt(Side.LOW, model.getLastRotation(Side.LOW));
						}
						else {
							model.stopWheelBreaking(Side.LOW);
							model.getWheel(Side.LOW).setInertia(0);
							model.setLastRotation(Side.LOW, null);
						}						
					}
					
					if (model.getWheelRotating(Side.LOW) != null) {
						if (model.getWheelBreaking(Side.LOW) == false) {
							if (inB < model.getWheel(Side.LOW).rotationSpeed) {
								if(!settings.getInertie()) model.getWheel(Side.LOW).setInertia(model.getWheel(Side.LOW).rotationSpeed);
								else model.getWheel(Side.LOW).setInertia(inB + 0.0004);
							}
						}
					}
						
					if (model.getWheelRotating(Side.LOW) == Direction.ANTI_HORAIRE) {					
						model.getWheel(Side.LOW).rotate(Direction.ANTI_HORAIRE);
						updateWheel(Side.LOW, model.getWheel(Side.LOW).getCenterBall2(), model.getWheel(Side.LOW).getCenterBall1());
						updateMvt(Side.LOW, Direction.ANTI_HORAIRE);
					}
					else if (model.getWheelRotating(Side.LOW) == Direction.HORAIRE) {					
						model.getWheel(Side.LOW).rotate(Direction.HORAIRE);
						updateWheel(Side.LOW, model.getWheel(Side.LOW).getCenterBall2(), model.getWheel(Side.LOW).getCenterBall1());
						updateMvt(Side.LOW, Direction.HORAIRE);
					}	
					
					double inH = model.getWheel(Side.HIGH).getInertia();
					
					if (model.getWheelBreaking(Side.HIGH)) {
						if (inH > 0) {
							if(!settings.getInertie()) model.getWheel(Side.HIGH).setInertia(model.getWheel(Side.HIGH).rotationSpeed);
							else model.getWheel(Side.HIGH).setInertia(inH - 0.0004);
							model.getWheel(Side.HIGH).rotate(model.getLastRotation(Side.HIGH));
							updateWheel(Side.HIGH, model.getWheel(Side.HIGH).getCenterBall2(), model.getWheel(Side.HIGH).getCenterBall1());
							updateMvt(Side.HIGH, model.getLastRotation(Side.HIGH));
						}
						else {
							model.stopWheelBreaking(Side.HIGH);
							model.getWheel(Side.HIGH).setInertia(0);
							model.setLastRotation(Side.HIGH, null);
						}						
					}
					
					if (model.getWheelRotating(Side.HIGH) != null) {
						if (model.getWheelBreaking(Side.HIGH) == false) {
							if (inH < model.getWheel(Side.HIGH).rotationSpeed) {
								if(!settings.getInertie()) model.getWheel(Side.HIGH).setInertia(model.getWheel(Side.HIGH).rotationSpeed);
								else model.getWheel(Side.HIGH).setInertia(inH + 0.0004);
							}
						}
					}
						
					if (model.getWheelRotating(Side.HIGH) == Direction.ANTI_HORAIRE) {					
						model.getWheel(Side.HIGH).rotate(Direction.ANTI_HORAIRE);
						updateWheel(Side.HIGH, model.getWheel(Side.HIGH).getCenterBall2(), model.getWheel(Side.HIGH).getCenterBall1());
						updateMvt(Side.HIGH, Direction.ANTI_HORAIRE);
					}
					else if (model.getWheelRotating(Side.HIGH) == Direction.HORAIRE) {					
						model.getWheel(Side.HIGH).rotate(Direction.HORAIRE);
						updateWheel(Side.HIGH, model.getWheel(Side.HIGH).getCenterBall2(), model.getWheel(Side.HIGH).getCenterBall1());
						updateMvt(Side.HIGH, Direction.HORAIRE);
					}
					
				}
			}			
		}, 0, 1);
	}
	
	public void verifyCollision(Side side, Obstacle o) {
		int res = model.getWheel(side).isInCollision(o);
		ObstacleView ov = o.getController().getView();
		int oX = (int) o.getCoords()[0].getX();
		int oY = (int) o.getCoords()[0].getY();
		
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
			ov.addCollision(ov.new CollisionView(getCenterBall2(side).getX() - oX,getCenterBall2(side).getY() - oY, Color.blue));
			if (settings.getEffects()) defeatSound.play();
			view.lostGame();
			
		}
		else if (res == 3) {
			gameStop();
			model.setState(State.FINISHED);
			ov.addCollision(ov.new CollisionView(getCenterBall1(side).getX() - oX,getCenterBall1(side).getY() - oY, Color.red));
			ov.addCollision(ov.new CollisionView(getCenterBall2(side).getX() - oX,getCenterBall2(side).getY() - oY, Color.blue));	
			if (settings.getEffects()) defeatSound.play();
			view.lostGame();			
		}
		
	}
	
	public void verifyObstacleReached(Side side, Obstacle o) {
		if (o.getReached() == false) {
			boolean reach = false;
			for (Point p : o.getCoords()) {
				if (side == Side.LOW) {
					if (p.getY() > model.getWheel(side).getCenter().getY() + model.getWheel(side).radius) {
						reach = true;
					}
					else {
						return;
					}
				}
				else if (side == Side.HIGH) {
					if (p.getY() < model.getWheel(side).getCenter().getY() - model.getWheel(side).radius) {
						reach = true;
					}
					else {
						return;
					}
				}
			}
			if (reach) {
				if (settings.getEffects()) reachedSound.play();
				o.setReached();
			}
		}
		else {
			boolean visible = true;
			for (Point p : o.getCoords()) {
				if (p.getY() > model.height || p.getY() < 0) {
					visible = false;
				}
				else {
					return;
				}
			}
			if (!visible) {
				model.removeObstacle(side, o);
			}
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
				case KeyEvent.VK_RIGHT: model.startWheelRotation(Side.LOW, Direction.ANTI_HORAIRE); break;
				case KeyEvent.VK_LEFT: model.startWheelRotation(Side.LOW, Direction.HORAIRE); break;
				
				case KeyEvent.VK_D: model.startWheelRotation(Side.HIGH, Direction.ANTI_HORAIRE); break;
				case KeyEvent.VK_Q: model.startWheelRotation(Side.HIGH, Direction.HORAIRE); break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (model.getState() == State.ON_GAME){
			switch(e.getKeyCode()) {
				case KeyEvent.VK_RIGHT: model.stopWheelRotation(Side.LOW); if(settings.getInertie()) model.startWheelBreaking(Side.LOW); break;
				case KeyEvent.VK_LEFT: model.stopWheelRotation(Side.LOW); if(settings.getInertie()) model.startWheelBreaking(Side.LOW); break;
				
				case KeyEvent.VK_Q: model.stopWheelRotation(Side.HIGH); if(settings.getInertie()) model.startWheelBreaking(Side.HIGH); break;
				case KeyEvent.VK_D: model.stopWheelRotation(Side.HIGH); if(settings.getInertie()) model.startWheelBreaking(Side.HIGH); break;
				
				case KeyEvent.VK_SPACE:
					model.stopWheelRotation(Side.LOW);
					model.stopWheelRotation(Side.HIGH);
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
	
	public void addObstacle(Obstacle o) {
		ObstacleController oc = new ObstacleController();
		o.setController(oc);
		oc.setModel(o);
		ObstacleView ov = new ObstacleView(view.getWidth(), view.getHeight(),getBallRadius());
		oc.setView(ov);
		model.addObstacle(Side.HIGH, o);
		view.addObstacle(ov);
		
		Obstacle o1 = o.copy();
		ObstacleController oc1 = new ObstacleController();
		o1.setController(oc1);
		oc1.setModel(o1);
		ObstacleView ov1 = new ObstacleView(view.getWidth(), view.getHeight(), getBallRadius());
		oc1.setView(ov1);
		model.addObstacle(Side.LOW, o1);
		view.addObstacle(ov1);
	}
	
	public void stopMusic() {
		music.stop();
		
	}
	
	public int getWheelRadius() {
		// TODO Auto-generated method stub
		return model.getWheel(Side.LOW).radius;
	}
	
	public int getBallRadius() {
		// TODO Auto-generated method stub
		return model.getWheel(Side.LOW).ballRadius;
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
