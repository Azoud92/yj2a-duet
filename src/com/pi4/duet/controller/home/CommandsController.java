package com.pi4.duet.controller.home;

import com.pi4.duet.model.home.Commands;

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
