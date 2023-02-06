package com.pi4.duet.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pi4.duet.model.Point;
import com.pi4.duet.model.Wheel;
import com.pi4.duet.model.Wheel.Ball;

public class GameView extends JFrame implements KeyListener{
    public JLabel ballRed,ballBlue,centre;
    public ImageIcon imageR,imageB;
    public static double angleR=180;
    public static double angleB=0;
    double radiasR;
    double radiasB;
    public Wheel w;


    public GameView() {
     //   imageR = new ImageIcon("BallRed.png");
        ballRed= new JLabel();
        ballRed.setBounds(150, 150, 10, 10);
        ballRed.setBackground(Color.red);
        ballRed.setOpaque(true);
     //   ballRed.setIcon(imageR);

      //  imageB = new ImageIcon("BallBlue.png");
        ballBlue= new JLabel();
        ballBlue.setBounds(250, 150, 10, 10);
        ballBlue.setBackground(Color.blue);
        ballBlue.setOpaque(true);
      //  ballBlue.setIcon(imageB);

        centre= new JLabel();
        centre.setBounds((250+150)/2, 150, 0, 0);
        centre.setBackground(Color.black);
        centre.setOpaque(true);

        Point centreR=new Point(150,150);
        Point centreB=new Point(250,150);
        w=new Wheel();
        w.ball_1 =w.new Ball(centreR);
        w.ball_2 =w.new Ball(centreB);



        this.setTitle("Game");
        this.setSize(400, 400);
        this.setVisible(true);
        this.setResizable(true);
        this.setLayout(null);
        this.addKeyListener(this);

        this.add(ballBlue);
        this.add(ballRed);
        this.add(centre);



        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }







    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case 39 :
                w.rotateHoraire();
                if(ballRed.getY()<=centre.getY() && ballRed.getX()<centre.getX()  ) {
                    angleR=angleR+10;
                    angleB=angleB+10;
                    radiasR=Math.toRadians(angleR);
                    radiasB=Math.toRadians(angleB);
                    ballRed.setLocation((int) (50*Math.cos(radiasR)+200),(int) (50*Math.sin(radiasR)+150) );
                    ballBlue.setLocation((int) (50*Math.cos(radiasB)+200),(int) (50*Math.sin(radiasB)+150) );
                }else if(ballRed.getY()<centre.getY() && ballRed.getX()>=centre.getX()) {
                    angleR=angleR+10;
                    angleB=angleB+10;
                    radiasR=Math.toRadians(angleR);
                    radiasB=Math.toRadians(angleB);
                    ballRed.setLocation((int) (50*Math.cos(radiasR)+200),(int) (50*Math.sin(radiasR)+150) );
                    ballBlue.setLocation((int) (50*Math.cos(radiasB)+200),(int) (50*Math.sin(radiasB)+150) );
                }else if(ballRed.getY()>=centre.getY() && ballRed.getX()>centre.getX() ) {
                    angleR=angleR+10;
                    angleB=angleB+10;
                    radiasR=Math.toRadians(angleR);
                    radiasB=Math.toRadians(angleB);
                    ballRed.setLocation((int) (50*Math.cos(radiasR)+200),(int) (50*Math.sin(radiasR)+150) );
                    ballBlue.setLocation((int) (50*Math.cos(radiasB)+200),(int) (50*Math.sin(radiasB)+150) );
                }else if(ballRed.getY()>centre.getY() && ballRed.getX()<=centre.getX() ) {
                    angleR=angleR+10;
                    angleB=angleB+10;
                    radiasR=Math.toRadians(angleR);
                    radiasB=Math.toRadians(angleB);
                    ballRed.setLocation((int) (50*Math.cos(radiasR)+200),(int) (50*Math.sin(radiasR)+150) );
                    ballBlue.setLocation((int) (50*Math.cos(radiasB)+200),(int) (50*Math.sin(radiasB)+150) );
                }
                break;
            case 37 :
                w.rotateContreHoraire();
                if(ballBlue.getY()<=centre.getY() && ballBlue.getX()>centre.getX()) {
                    angleR=angleR-10;
                    angleB=angleB-10;
                    radiasR=Math.toRadians(angleR);
                    radiasB=Math.toRadians(angleB);
                    ballRed.setLocation((int) (50*Math.cos(radiasR)+200),(int) (50*Math.sin(radiasR)+150) );
                    ballBlue.setLocation((int) (50*Math.cos(radiasB)+200),(int) (50*Math.sin(radiasB)+150) );
                }else if(ballBlue.getY()<centre.getY() && ballBlue.getX()<=centre.getX()) {
                    angleR=angleR-10;
                    angleB=angleB-10;
                    radiasR=Math.toRadians(angleR);
                    radiasB=Math.toRadians(angleB);
                    ballRed.setLocation((int) (50*Math.cos(radiasR)+200),(int) (50*Math.sin(radiasR)+150) );
                    ballBlue.setLocation((int) (50*Math.cos(radiasB)+200),(int) (50*Math.sin(radiasB)+150) );
                }else if(ballBlue.getY()>=centre.getY() && ballBlue.getX()<centre.getX()) {
                    angleR=angleR-10;
                    angleB=angleB-10;
                    radiasR=Math.toRadians(angleR);
                    radiasB=Math.toRadians(angleB);
                    ballRed.setLocation((int) (50*Math.cos(radiasR)+200),(int) (50*Math.sin(radiasR)+150) );
                    ballBlue.setLocation((int) (50*Math.cos(radiasB)+200),(int) (50*Math.sin(radiasB)+150) );
                }else if(ballBlue.getY()>centre.getY() && ballBlue.getX()>=centre.getX()) {
                    angleR=angleR-10;
                    angleB=angleB-10;
                    radiasR=Math.toRadians(angleR);
                    radiasB=Math.toRadians(angleB);
                    ballRed.setLocation((int) (50*Math.cos(radiasR)+200),(int) (50*Math.sin(radiasR)+150) );
                    ballBlue.setLocation((int) (50*Math.cos(radiasB)+200),(int) (50*Math.sin(radiasB)+150) );
                }
                break;

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println(" sxcc: "+e.getKeyCode());

    }
}
