package com.pi4.duet.model;

public class Settings {
	
	private boolean music = true, effects = true, background = true, inertie = true;
	
	public void setMusic(boolean val) {
		// TODO Auto-generated method stub
		this.music = val;
	}

	public void setEffects(boolean val) {
		// TODO Auto-generated method stub
		this.effects = val;
	}
	
	public void setBackground(boolean val) {
		// TODO Auto-generated method stub
		this.background = val;
	}
	
	public boolean getMusic() {
		return music;
	}
	
	public boolean getEffects() {
		return effects;
	}

	public boolean getBackground() {
		return background;
	}

	public boolean getInertie() {
		return inertie;
	}

	public void setInertie(boolean inertie) {
		this.inertie = inertie;
	}
	
}
