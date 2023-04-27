package com.pi4.duet.controller.game;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.model.home.Commands;
import com.pi4.duet.model.home.Settings;

public class GameInfiniteController extends GameController {

	public GameInfiniteController(HomePageViewController hpvC, Settings settings, Commands commands, Scale scale) {
		super(hpvC, settings, commands, scale);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void replay() {
		// TODO Auto-generated method stub
		hpvC.runInfinite(hpvC.getWindow(), hpvC.getView(), true);
	}

}
