package com.pi4.duet.view.game;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.game.GameController;
import com.pi4.duet.controller.game.GameInfiniController;

import java.awt.*;

public class GameInfiniView extends GameView{

    public GameInfiniView(Dimension size, Scale scale, GameInfiniController controller) {
        super(size, scale, controller);
    }
}
