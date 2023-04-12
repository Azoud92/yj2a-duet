package com.pi4.duet.controller.game;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.model.game.GameInfini;
import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.model.home.Commands;
import com.pi4.duet.model.home.Settings;

import java.awt.event.KeyEvent;

public class GameInfiniController extends GameController {
    public GameInfiniController(HomePageViewController hpvC, Settings settings, Commands commands, Scale scale) {
        super(hpvC, settings, commands, scale);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void gameStart() {

    }

    @Override
    public void verifyCollision(Obstacle o) {

    }

    @Override
    public void verifyObstacleReached(Obstacle o) {

    }

    @Override
    public void addObstacle(Obstacle o) {

    }

    @Override
    public void replay() {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }
}
