package com.pi4.duet.controller.game;

import java.util.Iterator;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.model.home.Commands;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.game.ObstacleView;

public class GameDuoController extends GameController {

	private WheelControllerDuo topWheelController;
	public GameDuoController(HomePageViewController hpvC, Settings settings, Commands commands, Scale scale) {
		super(hpvC, settings, commands, scale);
		wheelController = new WheelControllerDuo(settings, commands, this, 1);
		topWheelController = new WheelControllerDuo(settings, commands, this, 2);
		// TODO Auto-generated constructor stub
	}
	
	public WheelControllerDuo getTopWheelController() {
		return topWheelController;
	}

	@Override
	public void replay() {
		// TODO Auto-generated method stub
		hpvC.runLvlDuo(hpvC.getWindow(), hpvC.getView(), true);
	}
	
	@Override
	public void addObstacle(Obstacle o, int idObs) {
		Obstacle oTop = o.clone();
		
		ObstacleController ocTop = new ObstacleController();
		ObstacleController ocBottom = new ObstacleController();
		
		ObstacleView ovTop = new ObstacleView(view.getWidth(), view.getHeight(), (int) wheelController.getBallRadius(), ocTop, idObs + 1);
		ObstacleView ovBottom = new ObstacleView(view.getWidth(), view.getHeight(), (int) wheelController.getBallRadius(), ocBottom, idObs);

		
		if (hpvC.getObstaclesViews() != null && hpvC.getObstaclesViews().size() > 1) {
			Iterator<ObstacleView> iter = hpvC.getObstaclesViews().iterator();
			while (iter.hasNext()) {
				ObstacleView ovH = iter.next();
				if (ovH.id == idObs) {
					ovBottom.setCollisionsMap(ovH.getCollisionsMap());
					break;
				}
				
				if (iter.hasNext()) {
					ovH = iter.next();
					if (ovH.id == idObs + 1) {
						ovTop.setCollisionsMap(ovH.getCollisionsMap());
						break;
					}
				}
			}			
		}
		ocBottom.setView(ovBottom);
		ocTop.setView(ovTop);
		o.setController(ocBottom);
		oTop.setController(ocTop);
		ocBottom.setModel(o);
		ocTop.setModel(oTop);
		model.addObstacle(o);
		model.addObstacle(oTop);
		view.addObstacle(ovBottom);
		view.addObstacle(ovTop);
	}

}
