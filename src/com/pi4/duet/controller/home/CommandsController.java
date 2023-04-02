package com.pi4.duet.controller.home;

import com.pi4.duet.model.home.Commands;
import com.pi4.duet.view.home.CommandsView;

public class CommandsController {
	
	private Commands model;
    private SettingsController controller;

    public void setModel(Commands model) { this.model = model; }
    
    public void setCv(CommandsView view) { }

    public SettingsController getController() {
        return controller;
    }

    public void setController(SettingsController controller) {
        this.controller = controller;
    }
    
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
		}
	}
}
