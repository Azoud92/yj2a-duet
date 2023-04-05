package com.pi4.duet.view.game;

import java.awt.Dimension;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.game.GameDuoController;

public class GameDuoView extends GameView {

	private static final long serialVersionUID = -306594423077754361L;

	private WheelView wheelTopView;

	public GameDuoView(Dimension size, Scale scale, GameDuoController controller) {
		super(size, scale, controller);
		wheelTopView = new WheelView(size, controller.getWheelTopController());
		this.add(wheelTopView);
		this.addKeyListener(wheelTopView.getController());
	}

	@Override
	public void lostGame() {
		wheelTopView.greyWheel();
		super.lostGame();
	}

	@Override
	protected void reset() {
		super.reset();
		wheelTopView.resetWheelColor();
	}

	public WheelView getWheelTopView() {
		return wheelTopView;
	}

}
