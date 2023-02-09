package com.pi4.duet.controller;

import com.pi4.duet.model.GamePlane;
import com.pi4.duet.model.Wheel;
import com.pi4.duet.view.GameView;

public class GameController {

	private GamePlane model;
	private GameView view;
		
	public void setModel(GamePlane model) { this.model = model; }
	
	public void setView(GameView view) { this.view = view; }
	
	public Wheel getWheel() {
		return model.wheel;
	}
		
}
