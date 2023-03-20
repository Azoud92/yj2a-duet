 package com.pi4.duet.controller.game;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;

import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.Sound;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.model.Direction;
import com.pi4.duet.model.game.GamePlane;
import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.model.game.ObstacleQueue;
import com.pi4.duet.model.game.ObstacleQueueStatus;
import com.pi4.duet.model.game.PatternData;
import com.pi4.duet.model.game.State;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.game.GameView;
import com.pi4.duet.view.game.ObstacleView;

public class GameController implements KeyListener {

	private GamePlane model;
	private GameView view;
	private Sound defeatSound = new Sound("defeat.wav", false);
	private Sound reachedSound = new Sound("reached.wav", false);
	private Settings settings;
	private Scale scale;
	
	private Sound music = new Sound("music.wav", true);
	
	private ObstacleQueue gameTimer;
	
	private HomePageViewController hpvC;
	
	public GameController(HomePageViewController hpvC, Settings settings, Scale scale){
		this.hpvC = hpvC;
		this.settings = settings;
		music.stop();
		this.scale = scale;
		gameTimer = new ObstacleQueue(this, scale);
	}
		
	public void setModel(GamePlane model) { this.model = model; }
	
	public void setView(GameView view) { this.view = view; }
	
	public GameView getView() {
		return view;
	}
	
	public void refreshView() {
		view.refresh();
	}
	
	public void gameStart() {
		if (model.getState() != State.READY) return;
		model.setState(State.ON_GAME);
		gameTimer.setStatus(ObstacleQueueStatus.WAITING);
		model.setWheelRotating(null);
		if (settings.getMusic()) music.play();
		
		gameTimer = new ObstacleQueue(this, scale);
		gameTimer.schedule(new TimerTask() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (model.getState() == State.ON_GAME){
					hasWin();
					if (model.getObstacles().size() > 0) {
						for (Obstacle o : model.getObstacles()) { // animation des obstacles pour les faire "tomber"
							o.update(0, 1);
							verifyCollision(o);
							verifyObstacleReached(o);
							refreshView();
						}
					}
					else refreshView();
					// animation du volant selon la direction souhaitée
					if (model.getWheelRotating() == null) stopMvt();
					double in = model.getWheel().getInertia();
					
					if (model.getWheelBreaking()) {
						if (in > 0) {
							if(!settings.getInertie()) model.getWheel().setInertia(model.getWheel().rotationSpeed);
							else model.getWheel().setInertia(in - 0.0004);
							model.getWheel().rotate(model.getLastRotation());
							updateWheel(model.getWheel().getCenterBall2(), model.getWheel().getCenterBall1());
							updateMvt(model.getLastRotation());
						}
						else {
							model.stopWheelBreaking();
							model.getWheel().setInertia(0);
							model.setLastRotation(null);
						}						
					}
					
					if (model.getWheelRotating() != null) {
						if (model.getWheelBreaking() == false) {
							if (in < model.getWheel().rotationSpeed) {
								if(!settings.getInertie()) model.getWheel().setInertia(model.getWheel().rotationSpeed);
								else model.getWheel().setInertia(in + 0.0004);
							}
						}
					}
						
					if (model.getWheelRotating() == Direction.ANTI_HORAIRE) {					
						model.getWheel().rotate(Direction.ANTI_HORAIRE);
						updateWheel(model.getWheel().getCenterBall2(), model.getWheel().getCenterBall1());
						updateMvt(Direction.ANTI_HORAIRE);
					}
					else if (model.getWheelRotating() == Direction.HORAIRE) {					
						model.getWheel().rotate(Direction.HORAIRE);
						updateWheel(model.getWheel().getCenterBall2(), model.getWheel().getCenterBall1());
						updateMvt(Direction.HORAIRE);
					}		
				}
			}			
		}, 0, 1);
	}
	
	public void gameStop() {
		gameTimer.cancel();
		music.stop();
	}
	
	public void verifyCollision(Obstacle o) {
		if (model.getState() == State.FINISHED) return;
		int res = model.getWheel().isInCollision(o);
		ObstacleView ov = o.getController().getView();
		double oX =  o.getCoords()[0].getX();
		double oY = o.getCoords()[0].getY();
		if (res == 1) {			
			gameStop();
			gameTimer.setStatus(ObstacleQueueStatus.FINISHED);
			model.setState(State.FINISHED);
			ov.addCollision(ov.new CollisionView(getCenterBall1().getX() - oX, getCenterBall1().getY() - oY, Color.red));
			if (settings.getEffects()) defeatSound.play();
			view.lostGame();
			
		}
		else if (res == 2) {			
			gameStop();
			gameTimer.setStatus(ObstacleQueueStatus.FINISHED);
			model.setState(State.FINISHED);
			ov.addCollision(ov.new CollisionView(getCenterBall2().getX()- oX,getCenterBall2().getY() - oY, Color.blue));
			if (settings.getEffects()) defeatSound.play();
			view.lostGame();
			
		}
		else if (res == 3) {
			gameStop();
			gameTimer.setStatus(ObstacleQueueStatus.FINISHED);
			model.setState(State.FINISHED);
			ov.addCollision(ov.new CollisionView(getCenterBall1().getX()- oX,getCenterBall1().getY() - oY, Color.red));
			ov.addCollision(ov.new CollisionView(getCenterBall2().getX()- oX,getCenterBall2().getY() - oY, Color.blue));	
			if (settings.getEffects()) defeatSound.play();
			view.lostGame();			
		}
		
	}
	
	public void resetIntertie() {
		model.getWheel().setInertia(0);
	}

	
	public void hasWin() {
		if (model.getObstacles().size() == 0 && gameTimer.getStatus() == ObstacleQueueStatus.FINISHED) {
			gameStop();
			view.afficheWin();
			view.refresh();
			gameTimer.setStatus(ObstacleQueueStatus.FINISHED);
			model.setState(State.FINISHED);
			
			if (!hpvC.getLevelsAvailable().contains(model.numLevel + 1)) { // on ajoute seulement le niveau suivant à la liste des niveaux disponibles si ce dernier n'y figure pas
				hpvC.addLevel(model.numLevel + 1);
				hpvC.save();
			}
		}
	}
		
	public void verifyObstacleReached(Obstacle o) {
		if (o.getReached() == false) {
			boolean reach = false;
			for (Point p : o.getCoords()) {
				if (p.getY() > model.getWheel().getCenter().getY() + model.getWheel().radius) {
					reach = true;
				}
				else {
					return;
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
				if (p.getY() > model.height) {
					visible = false;
				}
				else {
					return;
				}
			}
			if (!visible) {
				model.removeObstacle(o);
			}
		}
	}
			
	public void addObstacle(Obstacle o) {
		ObstacleView ov = new ObstacleView((int) o.getWidth(), (int) o.getHeight(), (int) o.getPos().getX(), (int) o.getPos().getY(), getBallRadius());
		ObstacleController oc = new ObstacleController();
		oc.setView(ov);
		o.setController(oc);
		oc.setModel(o);		
		
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
	
	public void playMusic() {
		music.play();
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (model.getState() == State.ON_GAME){
			switch(e.getKeyCode()) {
				case KeyEvent.VK_RIGHT: model.stopWheelRotation(); if(settings.getInertie()) model.startWheelBreaking(); break;
				case KeyEvent.VK_LEFT: model.stopWheelRotation(); if(settings.getInertie()) model.startWheelBreaking(); break;
				case KeyEvent.VK_SPACE:
					model.stopWheelRotation();
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

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (model.getState() == State.ON_GAME){
			switch (e.getKeyCode()){
				case KeyEvent.VK_RIGHT: model.startWheelRotation(Direction.ANTI_HORAIRE); break;
				case KeyEvent.VK_LEFT: model.startWheelRotation(Direction.HORAIRE); break;
				case KeyEvent.VK_CONTROL:{
					System.out.println("A");
					System.out.println(model.getWheel().getCenterBall1().getX());
					model.getWheel().moveLeft();
					System.out.println(model.getWheel().getCenterBall1().getX());
					System.out.println("B");
					break;
				}
				case KeyEvent.VK_ALT:model.getWheel().moveRight();break;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub		
	}	
	
	public void affMenu() {
		hpvC.runHomePage();
		if(settings.getMusic()) { hpvC.runMusic(); }
		view.setVisible(false);
	}	
	
	public void stopMusic() {
		music.stop();
		
	}
	
	public void replay() {
		hpvC.runLevel(hpvC.getWindow(), hpvC.getView(), model.numLevel);
	}
	
	public void stopMvt() {
		view.stopMvt();		
	}
	
	public Dimension getSize() {
		return view.getSize();
	}

	
	public Point getCenterBall1() {
		return model.getWheel().getCenterBall1();
	}
	
	public Point getCenterBall2() {
		return model.getWheel().getCenterBall2();
	}
	
	public int getWheelRadius() {
		// TODO Auto-generated method stub
		return model.getWheel().radius;
	}

	public GamePlane getModel() {
		return model;
	}

	public int getBallRadius() {
		// TODO Auto-generated method stub
		return model.getWheel().ballRadius;
	}
	public double getWheelSpeed() {
		return model.getWheel().rotationSpeed;
	}
	
	public double getWheelangle() {
		return model.getWheel().getAngle();
	}
	
	public Point getWheelCenter() {
		return model.getWheel().getCenter();
	}
	
	public boolean isBackgroundEnabled() { return settings.getBackground(); }
	
	public void addObstacleTestDelay(Obstacle o, long delay) {
		this.gameTimer.putObstacle(o, delay);
	}
	
	public void addObstacleTestDelay(Obstacle o, long delay, ObstacleQueueStatus status) {
		this.gameTimer.putObstacle(o, delay, status);
	}
	
	public void addPattern(PatternData d) {
		gameTimer.putPattern(d);
	}

}
