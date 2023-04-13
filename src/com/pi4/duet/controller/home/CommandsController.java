package com.pi4.duet.controller.home;

import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import com.pi4.duet.model.game.Direction;
import com.pi4.duet.model.home.Commands;
import com.pi4.duet.view.home.CommandsView;
import com.pi4.duet.view.home.SettingsView;
import com.pi4.duet.view.home.Transition;

public class CommandsController {
	
	private Commands model;
	
    public void setModel(Commands model) { this.model = model; }
    
    public int getTurnLeft() {
		return model.getTurnLeft();
	}

	public int getTurnRight() {
		return model.getTurnRight();
	}

	public int getMoveLeft() {
		return model.getMoveLeft();
	}

	public int getMoveRight() {
		return model.getMoveRight();
	}

	public int getPause() {
		return model.getPause();
	}

	public int getFallObs() {
		return model.getFallObs();
	}
	
	public boolean areIdenticalCommands() {
		return model.areIdenticalCommands();
	}
	
	public void save() {
		model.save();
	}
	
	public void backSettings(CommandsView cv, SettingsView sv) {
		cv.setVisible(false);
		sv.setVisible(true);
		Transition t = new Transition(sv, cv, sv.getSize().width, sv.getSize().height, Direction.RIGHT);
		JPanel container = new JPanel(new GridLayout(1, 3));
		container.add(new JPanel());
		container.add(t);
		container.add(new JPanel());
		sv.getGw().setMainContainer(container);
		cv.ButtonsOff();
		sv.ButtonsOff();
		t.transition();
		
		Timer temp = new Timer();
		temp.schedule(new TimerTask(){
			@Override
			public void run() {
				if(!t.getTransition()) {
					cv.ButtonsOn();
					sv.ButtonsOn();
					temp.cancel();
				}
			}
		}, 0, 10);
	}
	
	public void updateCommands(int idCmd, int keyCode) {
		// TODO Auto-generated method stub
		switch(idCmd) {
		case 0:
			model.setTurnLeft(keyCode);
			break;
		case 1:
			model.setTurnRight(keyCode);
			break;
		case 2:
			model.setMoveLeft(keyCode);
			break;
		case 3:
			model.setMoveRight(keyCode);
			break;
		case 4:
			model.setPause(keyCode);
			break;
		case 5:
			model.setFallObs(keyCode);
			break;
		case 6:
			model.setTurnLeftDuo(keyCode);
			break;
		case 7:
			model.setTurnRightDuo(keyCode);
			break;
		case 8:
			model.setMoveLeftDuo(keyCode);
			break;
		case 9:
			model.setMoveRightDuo(keyCode);
			break;
		}
	}

	public int getTurnLeftDuo() {
		// TODO Auto-generated method stub
		return model.getTurnLeftDuo();
	}

	public int getTurnRightDuo() {
		// TODO Auto-generated method stub
		return model.getTurnRightDuo();
	}

	public int getMoveLeftDuo() {
		// TODO Auto-generated method stub
		return model.getMoveLeftDuo();
	}

	public int getMoveRightDuo() {
		// TODO Auto-generated method stub
		return model.getMoveRightDuo();
	}

}
