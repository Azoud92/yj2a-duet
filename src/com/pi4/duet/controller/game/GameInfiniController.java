package com.pi4.duet.controller.game;

import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.model.game.*;
import com.pi4.duet.model.game.data.ObstacleQueue;
import com.pi4.duet.model.game.data.ObstacleQueueStatus;
import com.pi4.duet.model.home.Commands;
import com.pi4.duet.model.home.Settings;
import com.pi4.duet.view.game.ObstacleView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.TimerTask;

public class GameInfiniController extends GameController {
    public GameInfiniController(HomePageViewController hpvC, Settings settings, Commands commands, Scale scale) {
        super(hpvC, settings, commands, scale);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void gameStart() {
        if (model.getState() != GameState.READY) return;
        model.setState(GameState.ON_GAME);
        this.gameTimer=new ObstacleQueue(this,scale);
        System.out.println("a");
        System.out.println(gameTimer);
        gameTimer.setStatus(ObstacleQueueStatus.WAITING);
        System.out.println("b");
        wheelController.setWheelRotating(null);

        if (settings.getMusic()) music.play();

        gameTimer = new ObstacleQueue(this, scale);
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (model.getState() == GameState.ON_GAME){
                    hasWin();
                    Point a=new Point(10,-100);
                    Point b=new Point(320,-100);
                    Point c=new Point(320,0);
                    Point d=new Point(10,0);
                    Point[]points={a,b,c,d};
                    Obstacle ob=new Obstacle(points,new Point(160,-50),0.1,0.0,0.0, Direction.BOTTOM,null);

                    ObstacleController oc=new ObstacleController(ob,new ObstacleView(100,100,10,null,0));
                    oc.getModel().setController(oc);
                    ob.setController(oc);
                    System.out.println(model.getObstacles().size());
                    model.getObstacles().add(ob);
                    model.addObstacle(ob);
                    System.out.println(model.getObstacles().size());
                    if (model.getObstacles().size() > 0) {
                        System.out.println(model.getObstacles());
                        for (Obstacle o : model.getObstacles()) { // animation des obstacles pour les faire "tomber"
                            o.updatePosition();
                            verifyCollision(o);
                            verifyObstacleReached(o);
                            if(fallAcceleration==false) model.setVelocityTo01();
                            refreshView();
                        }
                    }
                    else refreshView();
                    incrEffectDelaySpeed();
                    if (model.getCanUseEffect()) view.effectCanBeUsed();
                    wheelController.animateWheel();
                }
            }
        }, 0, 1);
    }

    @Override
    public void verifyCollision(Obstacle o) {
        if (model.getState() == GameState.FINISHED) return;
        int res = model.getWheel().isInCollision(o);
        ObstacleController oc = o.getController();

        int oX = (int) o.getCenter().getX();
        int oY = (int) o.getCenter().getY();


        if (res > 0) {
            gameStop();
            gameTimer.setStatus(ObstacleQueueStatus.FINISHED);
            model.setState(GameState.FINISHED);
            if (res == 1) oc.addCollisionView(new Point(wheelController.getCenterBall_1().getX() - oX,
                    wheelController.getCenterBall_1().getY() - oY), Color.RED);
            else if (res == 2) oc.addCollisionView(new Point(wheelController.getCenterBall_2().getX() - oX,
                    wheelController.getCenterBall_2().getY() - oY), Color.BLUE);
            else if (res == 3) {
                oc.addCollisionView(new Point(wheelController.getCenterBall_1().getX() - oX,
                        wheelController.getCenterBall_1().getY() - oY), Color.RED);
                oc.addCollisionView(new Point(wheelController.getCenterBall_2().getX() - oX,
                        wheelController.getCenterBall_2().getY() - oY), Color.BLUE);
            }
            if (settings.getEffects()) defeatSound.play();
            view.lostGame();
            this.setBackgroundMovement(true);
        }
    }

    @Override
    public void verifyObstacleReached(Obstacle o) {
        if (!o.getReached()) {
            boolean reach = false;
            for (Point p : o.getPoints()) {
                if (p.getY() > model.getWheel().getCenter().getY() + model.getWheel().getRadius()) {
                    reach = true;
                }
                else {
                    return;
                }
            }
            if (reach) {
                if (settings.getEffects()) reachedSound.play();
                o.setReachedTrue();
            }
        }
        else {
            boolean visible = true;
            for (Point p : o.getPoints()) {
                if (p.getY() > model.height) {
                    visible = false;
                }
                else {
                    return;
                }
            }
            if (!visible) {
                model.removeObstacle(o);
            }
        }
    }

    @Override
    public void addObstacle(Obstacle o, int id) {
        ObstacleController oc = new ObstacleController();
        ObstacleView ov = new ObstacleView(view.getWidth(), view.getHeight(), (int) wheelController.getBallRadius(), oc, id);

        if (hpvC.getObstaclesViews() != null && hpvC.getObstaclesViews().size() > 0) {
            ov.setCollisionsMap(hpvC.getObstaclesViews().get(0).getCollisionsMap());
            ov.resetCollisions();
            hpvC.getObstaclesViews().remove(0);
        }
        oc.setView(ov);
        o.setController(oc);
        oc.setModel(o);
        if (fallAcceleration) o.setVelocity(1);
        model.addObstacle(o);
        view.addObstacle(ov);
    }

    @Override
    public void replay() {
        hpvC.runLevel(hpvC.getWindow(), hpvC.getView(), ((GameLevel) model).numLevel, true);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == commands.getFallObs()){
            fallAcceleration = true;
            gameTimer.fall();
            for(int i = 0; i < model.getObstacles().size(); i++){
                model.getObstacles().get(i).setVelocity(1);
            }

        }
    }
}
