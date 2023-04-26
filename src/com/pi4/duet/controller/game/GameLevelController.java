package com.pi4.duet.controller.game;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.model.game.GameLevel;
import com.pi4.duet.model.home.Commands;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.game.GameLevelView;

public class GameLevelController extends GameController {
	
	public GameLevelController(HomePageViewController hpvC, Settings settings, Commands commands, Scale scale) {
		super(hpvC, settings, commands, scale);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void replay() {
		// TODO Auto-generated method stub
		hpvC.runLevel(hpvC.getWindow(), hpvC.getView(), ((GameLevel) model).numLevel, true);
	}
	
	public void win() {
		// TODO Auto-generated method stub
		setBackgroundMovement(true);
		((GameLevelView) view).afficheWin();
		view.refresh();
		
		if (!hpvC.getLevelsAvailable().contains(((GameLevel) model).numLevel + 1)) { // on ajoute seulement le niveau suivant Ã  la liste des niveaux disponibles si ce dernier n'y figure pas
			hpvC.addLevel(((GameLevel) model).numLevel + 1);
			hpvC.save();
		}
	}

}
