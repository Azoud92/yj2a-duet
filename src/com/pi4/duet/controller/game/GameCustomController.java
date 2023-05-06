package com.pi4.duet.controller.game;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.model.game.GameCustom;
import com.pi4.duet.model.home.Commands;
import com.pi4.duet.model.home.Settings;

public class GameCustomController extends GameController {

	public GameCustomController(HomePageViewController hpvC, Settings settings, Commands commands, Scale scale) {
		super(hpvC, settings, commands, scale);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void replay() {
		// TODO Auto-generated method stub
		hpvC.runCustomLvl(hpvC.getWindow(), hpvC.getView(), ((GameCustom) model).path, true);
	}

}
