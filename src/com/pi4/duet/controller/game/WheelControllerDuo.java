package com.pi4.duet.controller.game;

import java.awt.event.KeyEvent;

import com.pi4.duet.model.game.Direction;
import com.pi4.duet.model.game.GameState;
import com.pi4.duet.model.game.RotationType;
import com.pi4.duet.model.home.Commands;
import com.pi4.duet.model.home.Settings;

public class WheelControllerDuo extends WheelController {

	private final int idWheel;
	public WheelControllerDuo(Settings settings, Commands commands, GameController gameController, int idWheel) {
		super(settings, commands, gameController);
		this.idWheel = idWheel;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (game.getState() != GameState.ON_GAME) return;
		
		if(e.getKeyCode() == commands.getTurnRight()){
			if (idWheel != 1) return;
			if (!model.isWheelBreaking()) model.setWheelRotating(RotationType.ANTI_HORAIRE);
			return;
		}

		if(e.getKeyCode() == commands.getTurnLeft()){
			if (idWheel != 1) return;
			if (!model.isWheelBreaking()) model.setWheelRotating(RotationType.HORAIRE);
			return;
		}
		
		if(e.getKeyCode() == commands.getTurnLeftDuo()){
			if (idWheel != 2) return;
			if (!model.isWheelBreaking()) model.setWheelRotating(RotationType.HORAIRE);
			return;
		}
		if(e.getKeyCode() == commands.getTurnRightDuo()){
			if (idWheel != 2) return;
			if (!model.isWheelBreaking()) model.setWheelRotating(RotationType.ANTI_HORAIRE);
			return;
		}

		if(e.getKeyCode() == commands.getMoveRight()){				
			if (idWheel != 1) return;
			model.setMoving(true);
			model.setWheelMovement(Direction.RIGHT);
			return;
		}

		if(e.getKeyCode() == commands.getMoveLeft()){
			if (idWheel != 1) return;
			model.setMoving(true);
			model.setWheelMovement(Direction.LEFT);
			return;
		}
		
		if(e.getKeyCode() == commands.getMoveRightDuo()){
			if (idWheel != 2) return;
			model.setMoving(true);
			model.setWheelMovement(Direction.RIGHT);
			return;
		}

		if(e.getKeyCode() == commands.getMoveLeftDuo()){
			if (idWheel != 2) return;
			model.setMoving(true);
			model.setWheelMovement(Direction.LEFT);
			return;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub		
		if (game.getState() != GameState.ON_GAME) return;		
		if (game.getState() == GameState.ON_GAME) {

			if (e.getKeyCode() == commands.getTurnLeft()) {
				if (idWheel != 1) return;
				model.stopWheelRotation();
				if (settings.getInertie()) model.setWheelBreaking(true);
			}

			if (e.getKeyCode() == commands.getTurnRight()) {

				if (idWheel != 1) return;
				model.stopWheelRotation();
				if (settings.getInertie()) model.setWheelBreaking(true);
			}
			if (e.getKeyCode() == commands.getTurnLeftDuo()) {
				if (idWheel != 2) return;
				model.stopWheelRotation();
				if (settings.getInertie()) model.setWheelBreaking(true);
			}
			if (e.getKeyCode() == commands.getTurnRightDuo()) {
				if (idWheel != 2) return;
				model.stopWheelRotation();
				if (settings.getInertie()) model.setWheelBreaking(true);
			}
			
			if (e.getKeyCode() == commands.getMoveRightDuo()) {
				if (idWheel != 2) return;
				model.setMoving(false);
			}

			if (e.getKeyCode() == commands.getMoveLeftDuo()) {
				if (idWheel != 2) return;
				model.setMoving(false);
				return;
			}

			if (e.getKeyCode() == commands.getMoveLeft()) {
				if (idWheel != 1) return;
				model.setMoving(false);
			}
			if (e.getKeyCode() == commands.getMoveRight()) {
				if (idWheel != 1) return;
				model.setMoving(false);
			}

		}
	}

}
