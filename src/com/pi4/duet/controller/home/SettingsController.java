package com.pi4.duet.controller.home;

import com.pi4.duet.model.game.Game;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.game.GameWindow;
import com.pi4.duet.view.home.CommandsView;
import com.pi4.duet.view.home.SettingsView;

import javax.swing.*;
import java.awt.*;

public class SettingsController {
	private SettingsView sv;
	private GameWindow gw;


	public HomePageViewController getHpvC() {
		return hpvC;
	}

	private HomePageViewController hpvC;
	private Settings model;
	private CommandsView cv;
	private CommandsController cc;

	public SettingsController(HomePageViewController hpvC,GameWindow gw,CommandsView cv){
		this.hpvC = hpvC;
		this.gw=gw;
		cc=new CommandsController();
		this.cv=cv;
		cc.setCv(cv);
		this.cv=cv;
	}

	public void setModel(Settings model) { this.model = model; }
	public void setView(SettingsView sv) { this.sv = sv; }

	public void setMusic(boolean val) {
		model.setMusic(val);
		if(val) { hpvC.runMusic(); }
		else { hpvC.stopMusic(); }
	}
	public void showCommands(CommandsView cv,GameWindow gw) {
		this.gw=gw;
		sv.setVisible(false);
		cv.setVisible(true);
		gw.setMainContainer(cv);
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
	public void setCv(CommandsView cv) {
		this.cv = cv;
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
		hpvC.getWindow().remove(sv);
		hpvC.runHomePage();
	}

	public void save() {
		model.save();
	}
	public GameWindow getGw() {
		return gw;
	}

}
