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

	private Wheel model;
	private WheelView view;

	private GameController game;

	private Settings settings;
	private Commands commands;

	public final int numWheel; // sert à enregistrer le numéro de volant dans le cas multijoueur, pour gérer les évén. claviers

	public WheelController(Settings settings, Commands commands, GameController gameController, int numWheel) {
		this.settings = settings;
		this.commands = commands;
		this.game = gameController;
		this.numWheel = numWheel;
	}

	public void setModel(Wheel model) { this.model = model; }
	public void setView(WheelView view) { this.view = view; }

	public Point getCenterBall_1() { return model.getCenterBall1(); }
	public Point getCenterBall_2() { return model.getCenterBall2(); }

	public double getBallRadius() { return model.getBallRadius(); }
	public double getWheelRadius() { return model.getRadius(); }
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
			if (!model.isWheelBreaking()) {
				if (in < model.rotationSpeed) {
					if(!settings.getInertie()) model.setInertia(model.rotationSpeed);
					else model.setInertia(in + 0.0004);
				}
			}
		}

		if (model.getWheelRotating() == RotationType.ANTI_HORAIRE) {
			model.rotate(RotationType.ANTI_HORAIRE);
			view.updateBall_1(new Point(model.getCenterBall1().getX() - model.getBallRadius(),
					model.getCenterBall1().getY() - model.getBallRadius()));
			view.updateBall_2(new Point(model.getCenterBall2().getX() - model.getBallRadius(),
					model.getCenterBall2().getY() - model.getBallRadius()));
			updateMvt(RotationType.ANTI_HORAIRE);
		}
		else if (model.getWheelRotating() == RotationType.HORAIRE) {
			model.rotate(RotationType.HORAIRE);
			view.updateBall_1(new Point(model.getCenterBall1().getX() - model.getBallRadius(),
					model.getCenterBall1().getY() - model.getBallRadius()));
			view.updateBall_2(new Point(model.getCenterBall2().getX() - model.getBallRadius(),
					model.getCenterBall2().getY() - model.getBallRadius()));
			updateMvt(RotationType.HORAIRE);
		}

		if(model.getStopMovement()) {
			if (model.getWheelMovement() == Direction.LEFT) {
				model.move(Direction.LEFT,(int)view.getSize().getWidth()/3);
				view.updateBall_1(new Point(model.getCenterBall1().getX() - model.getBallRadius(),
						model.getCenterBall1().getY() - model.getBallRadius()));
				view.updateBall_2(new Point(model.getCenterBall2().getX() - model.getBallRadius(),
						model.getCenterBall2().getY() - model.getBallRadius()));


			} else if (model.getWheelMovement() == Direction.RIGHT) {
				model.move(Direction.RIGHT,(int)view.getSize().getWidth());
				view.updateBall_1(new Point(model.getCenterBall1().getX() - model.getBallRadius(),
						model.getCenterBall1().getY() - model.getBallRadius()));
				view.updateBall_2(new Point(model.getCenterBall2().getX() - model.getBallRadius(),
						model.getCenterBall2().getY() - model.getBallRadius()));


			}
		}
	}

	public void updateMvt(RotationType horaire) {
		double angle = model.getAngle();
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
		if (game.getState() == GameState.ON_GAME){
			if(e.getKeyCode()== commands.getTurnRight()){
				if (numWheel != 1) return;
				if (!model.isWheelBreaking()) model.setWheelRotating(RotationType.ANTI_HORAIRE);
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
				return;
			}

			if(e.getKeyCode() == commands.getTurnLeft()){
				if (numWheel != 1) return;
				if (!model.isWheelBreaking()) model.setWheelRotating(RotationType.HORAIRE);
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
				return;
			}
			if(e.getKeyCode() == commands.getTurnLeftDuo()){
				if (numWheel != 2) return;
				if (!model.isWheelBreaking()) model.setWheelRotating(RotationType.HORAIRE);
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
				return;
			}
			if(e.getKeyCode() == commands.getTurnRightDuo()){
				if (numWheel != 2) return;
				if (!model.isWheelBreaking()) model.setWheelRotating(RotationType.ANTI_HORAIRE);
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
				return;
			}

			if(e.getKeyCode() == commands.getMoveRight()){
				if (numWheel != 1) return;
				model.setStopMovement(true);
				model.setWheelMovement(Direction.RIGHT);
				return;
			}

			if(e.getKeyCode() == commands.getMoveLeft()){
				if (numWheel != 1) return;
				model.setStopMovement(true);
				model.setWheelMovement(Direction.LEFT);
				return;
			}
			
			if(e.getKeyCode() == commands.getMoveRightDuo()){
				if (numWheel != 2) return;
				model.setStopMovement(true);
				model.setWheelMovement(Direction.RIGHT);
				return;
			}

			if(e.getKeyCode() == commands.getMoveLeftDuo()){
				if (numWheel != 2) return;
				model.setStopMovement(true);
				model.setWheelMovement(Direction.LEFT);
				return;
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (game.getState() == GameState.ON_GAME) {

			if(e.getKeyCode() == commands.getTurnLeft()){
				if (numWheel != 1) return;
				model.stopWheelRotation();
				if (settings.getInertie()) model.setWheelBreaking(true);
			}

			if(e.getKeyCode() == commands.getTurnRight()){

				if (numWheel != 1) return;
				model.stopWheelRotation();
				if (settings.getInertie()) model.setWheelBreaking(true);
			}
			if(e.getKeyCode() == commands.getTurnLeftDuo()){
				if (numWheel != 2) return;
				model.stopWheelRotation();
				if (settings.getInertie()) model.setWheelBreaking(true);
			}
			if(e.getKeyCode() == commands.getTurnRightDuo()){
				if (numWheel != 2) return;
				model.stopWheelRotation();
				if (settings.getInertie()) model.setWheelBreaking(true);
			}
			
			if(e.getKeyCode() == commands.getMoveRightDuo()){
				if (numWheel != 2) return;
				model.setStopMovement(false);
			}

			if(e.getKeyCode() == commands.getMoveLeftDuo()){
				if (numWheel != 2) return;
				model.setStopMovement(false);
				return;
			}

			if(e.getKeyCode() == commands.getMoveLeft()) {
				if (numWheel != 1) return;
				model.setStopMovement(false);
			}
			if(e.getKeyCode() == commands.getMoveRight()) {
				if (numWheel != 1) return;
				model.setStopMovement(false);
			}

		}
	}
	
	public void setWheelRotating(RotationType dir) {
		// TODO Auto-generated method stub
		model.setWheelRotating(dir);
	}


}
