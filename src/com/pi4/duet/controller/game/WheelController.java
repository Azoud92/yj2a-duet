package com.pi4.duet.controller.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.pi4.duet.Point;
import com.pi4.duet.model.game.Direction;
import com.pi4.duet.model.game.GameState;
import com.pi4.duet.model.game.RotationType;
import com.pi4.duet.model.game.Wheel;
import com.pi4.duet.model.home.Commands;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.game.WheelView;

public class WheelController implements KeyListener {

	protected Wheel model;
	private WheelView view;

	protected GameController game;

	protected Settings settings;
	protected Commands commands;
	
	public WheelController(Settings settings, Commands commands, GameController gameController) {
		this.settings = settings;
		this.commands = commands;
		this.game = gameController;
	}

	public void setModel(Wheel model) { this.model = model; }
	public void setView(WheelView view) { this.view = view; }

	public Point getCenterBall_1() { return model.getCenterBall1(); }
	public Point getCenterBall_2() { return model.getCenterBall2(); }

	public double getBallRadius() { return model.getBallRadius(); }
	public double getWheelRadius() { return model.getRadius(); }
	public Point getWheelCenter() { return model.getCenter(); }
	public double getWheelSpeed() { return model.getRotationSpeed(); }
	
	public void updateMvt(RotationType horaire, double angle) {
		view.mvt_1_Rotate(horaire, angle);
		view.mvt_2_Rotate(horaire, angle);
	}

	public void stopWheelRotation() { model.stopWheelRotation(); }

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (game.getState() != GameState.ON_GAME) return;
		
		if (e.getKeyCode() == commands.getTurnRight()) {
			if (!model.isWheelBreaking()) model.setWheelRotating(RotationType.ANTI_HORAIRE);
			return;
		}

		if (e.getKeyCode() == commands.getTurnLeft()) {
			if (!model.isWheelBreaking()) model.setWheelRotating(RotationType.HORAIRE);
			return;
		}

		if (e.getKeyCode() == commands.getMoveRight()) {	
			model.setMoving(true);
			model.setWheelMovement(Direction.RIGHT);
			return;
		}

		if (e.getKeyCode() == commands.getMoveLeft()) {
			model.setMoving(true);
			model.setWheelMovement(Direction.LEFT);
			return;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (game.getState() != GameState.ON_GAME) return;
		if (e.getKeyCode() == commands.getTurnLeft()) {
			model.stopWheelRotation();
			if (settings.getInertie()) model.setWheelBreaking(true);
		}

		if (e.getKeyCode() == commands.getTurnRight()) {
			model.stopWheelRotation();
			if (settings.getInertie()) model.setWheelBreaking(true);
		}			

		if (e.getKeyCode() == commands.getMoveLeft()) {
			model.setMoving(false);
		}
		if (e.getKeyCode() == commands.getMoveRight()) {
			model.setMoving(false);
		}
	}
		
	public void updateBallsView(Point ball_1, Point ball_2) {
		// TODO Auto-generated method stub
		view.updateBall_1(ball_1);
		view.updateBall_2(ball_2);		
	}
}
