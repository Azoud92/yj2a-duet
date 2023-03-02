package com.pi4.duet.controller;


import com.pi4.duet.view.SettingsView;

public class SettingsController {
	private SettingsView sv;
	private HomePageViewController hpvC;
	
	public SettingsController(HomePageViewController hpvC){
		this.hpvC = hpvC;
	}
	
	public void setView(SettingsView sv) { this.sv = sv; }

	public void back() {
		sv.setVisible(false);
		hpvC.getWindow().remove(sv);
		hpvC.runHomePage();
		
	}
}
