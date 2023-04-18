package com.pi4.duet.model.game;

import com.pi4.duet.Point;

import java.util.ArrayList;

public class GameInfini extends Game{


    private ArrayList<Obstacle> topObstacles = new ArrayList<>();
    public GameInfini(int width,int height, Point coorWheel){
        super(width,height,coorWheel);
    }
    public ArrayList<Obstacle> getTopObstacles() { return (ArrayList<Obstacle>) topObstacles.clone(); }

    public void addTopObstacle(Obstacle o) {
        this.topObstacles.add(o);
    }

    public void removeTopObstacle(Obstacle o) {
        this.topObstacles.remove(o);
    }
}
