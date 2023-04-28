package com.pi4.duet.view.game;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.game.GameController;
import com.pi4.duet.model.home.Commands;

public class GameInfiniteView extends GameView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5713770611209267541L;

	public GameInfiniteView(Dimension size, Scale scale, Commands commands, GameController controller) {
		super(size, scale, commands, controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
