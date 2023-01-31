package com.pi4.duet.model;

import java.util.HashMap;

import javax.swing.Timer;

public class Pattern extends HashMap<Timer, Obstacle> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6372438323967897881L;
	
	public Obstacle getCurrentObstacle(Timer t) {
		return null;
		// A FAIRE : implémenter la méthode
		// ...
	}
	
	public void setObstacle(Timer t, Obstacle o) {
		this.put(t, o);
	}

}
