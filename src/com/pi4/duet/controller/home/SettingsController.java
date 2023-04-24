package com.pi4.duet.controller.home;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import com.pi4.duet.Scale;
import com.pi4.duet.model.game.Direction;
import com.pi4.duet.model.home.Commands;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.game.GameWindow;
import com.pi4.duet.view.home.CommandsView;
import com.pi4.duet.view.home.SettingsView;
import com.pi4.duet.view.home.Transition;

public class SettingsController {
	
	private SettingsView sv;
	private GameWindow gw;

	private HomePageViewController hpvC;
	private Settings model;
	
	private Commands commandsModel;
	private CommandsView commandsView;
	private CommandsController commandsController;
	
	private Scale scale;

	public SettingsController(HomePageViewController hpvC, GameWindow gw, Scale scale){
		this.hpvC = hpvC;

		this.gw = gw;
		this.scale = scale;
	}

	public void setModel(Settings model) { this.model = model; }
	public void setView(SettingsView sv) { this.sv = sv; }
	
	public void initCommands() {
		this.commandsModel = Commands.read();
		this.commandsController = new CommandsController();
		this.commandsController.setModel(commandsModel);
		this.commandsView = new CommandsView(sv, scale, commandsController);
	}
	
	public HomePageViewController getHpvC() {
		return hpvC;
	}

	public void setMusic(boolean val) {
		model.setMusic(val);
		if (val) { hpvC.runMusic(); }
		else { hpvC.stopMusic(); }
	}
	
	public void showCommands(GameWindow gw) {
		this.gw = gw;
		sv.setVisible(false);
		commandsView.setVisible(true);
		Transition t = new Transition(sv, commandsView, hpvC.getSize().width, hpvC.getSize().height, Direction.LEFT);

		gw.setMainContainer(t);
		sv.ButtonsOff();
		commandsView.ButtonsOff();
		t.transition();
		
		Timer temp = new Timer();
		temp.schedule(new TimerTask(){
			@Override
			public void run() {
				if(!t.getTransition()) {
					sv.ButtonsOn();
					commandsView.ButtonsOn();
					temp.cancel();
				}
			}
		}, 0, 10);
		

	}



	public void setEffects(boolean val) {
		model.setEffects(val);
	}

	public void setBackground(boolean val) {
		model.setBackground(val);
	}

	public void setInertie(boolean val) {
		model.setInertie(val);
	}

	public boolean getInertie() {
		return model.getInertie();
	}

	public boolean getMusic() { return model.getMusic(); }

	public boolean getEffects() { return model.getEffects(); }
 
	public boolean getBackground() {
		// TODO Auto-generated method stub
		return model.getBackground();
	}

	public void back() {
		sv.setVisible(false);
		commandsView.setVisible(true);
		Transition t = new Transition(hpvC.getView(), sv, hpvC.getSize().width, hpvC.getSize().height, Direction.RIGHT);
		JPanel container = new JPanel(new GridLayout(1, 3));

		gw.setMainContainer(t);
		hpvC.getView().ButtonsOff();
		sv.ButtonsOff();
		t.transition();
		
		Timer temp = new Timer();
		temp.schedule(new TimerTask(){
			@Override
			public void run() {
				if(!t.getTransition()) {
					hpvC.getView().ButtonsOn();
					sv.ButtonsOn();
					temp.cancel();
				}
			}
		}, 0, 10);
	}

	public void save() {
		model.save();
	}
	
	public GameWindow getGw() {
		return gw;
	}

	public Commands getCommandsModel() {
		// TODO Auto-generated method stub
		return commandsModel;
	}

	public void setGw(GameWindow gw) {
		this.gw = gw;
		
	}

}
