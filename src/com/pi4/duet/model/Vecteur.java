package com.pi4.duet.model;

public class Vecteur {
    private Point p1;
    private Point p2;
    private double x;
    private double y;
    public Vecteur(Point p1,Point p2){
        this.p1=p1;
        this.p2=p2;
        x=p2.getX()-p1.getX();
        y=p2.getY()-p1.getY();
    }

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
