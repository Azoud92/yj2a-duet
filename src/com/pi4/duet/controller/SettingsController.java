package com.pi4.duet.controller;

import com.pi4.duet.model.Settings;
import com.pi4.duet.view.home.SettingsView;

public class SettingsController {
	private SettingsView sv;
	private HomePageViewController hpvC;
	private Settings model;
	
	public SettingsController(HomePageViewController hpvC){
		this.hpvC = hpvC;
	}
	
	public void setModel(Settings model) { this.model = model; }
	public void setView(SettingsView sv) { this.sv = sv; }
	
	public void setMusic(boolean val) {
		model.setMusic(val);
		if(val) { hpvC.runMusic(); }
		else { hpvC.stopMusic(); }
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
		hpvC.getWindow().remove(sv);
		hpvC.runHomePage();		
	}
	
	public void save() {
		model.save();
	}
	
}
