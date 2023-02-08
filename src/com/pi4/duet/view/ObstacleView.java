package com.pi4.duet.view;

import javax.swing.JPanel;

import com.pi4.duet.controller.ObstacleController;

public class ObstacleView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5265129255893879964L;
	
	private ObstacleController controller;
	
	public ObstacleView(ObstacleController controller){
		this.controller = controller;
	}	
	
	public ObstacleController getController() { return controller; }
}
