package com.pi4.duet.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;

import com.pi4.duet.Sound;
import com.pi4.duet.model.GamePlaneDuo;
import com.pi4.duet.model.Point;
import com.pi4.duet.model.Settings;
import com.pi4.duet.model.Side;
import com.pi4.duet.view.game.GameDuoView;


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

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
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
	
	public boolean isBackgroundEnabled() { return settings.getBackground(); }


}
