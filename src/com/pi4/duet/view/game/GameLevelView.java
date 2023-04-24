package com.pi4.duet.view.game;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.game.GameLevelController;
import com.pi4.duet.model.home.Commands;

public class GameLevelView extends GameView {

	/**
	 *
	 */
	private static final long serialVersionUID = 9189517439320799864L;

	public GameLevelView(Dimension size, Scale scale, Commands commands, GameLevelController controller) {
		super(size, scale, commands, controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == commands.getFallObs()){
			controller.fall();			
		}
	}

}
