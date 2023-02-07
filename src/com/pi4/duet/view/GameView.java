package com.pi4.duet.view;



import java.awt.Color;  
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pi4.duet.controller.GameViewController;
import com.pi4.duet.model.Obstacle;
import com.pi4.duet.model.Point;
import com.pi4.duet.model.Wheel;

public class GameView extends JPanel implements KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -306594423077754361L;
	
	protected GameViewController controller;
	protected Dimension size;
	protected double scaleX, scaleY;
	public ArrayList<ObstacleView> obstacles;
	
	public JLabel ballRed,ballBlue,centre;
    public ImageIcon imageR,imageB;
    public static double angleR=180;
    public static double angleB=0;
    double radiasR;
    double radiasB;
    public Wheel w;


	
	public GameView(Dimension size, double scaleX, double scaleY) {
		this.setBackground(Color.black);
		obstacles = new ArrayList<ObstacleView>();
		Dimension dim = new Dimension(size.width/3,size.height);
		this.size = dim;
		this.setPreferredSize(dim);
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		
		//imageR = new ImageIcon("BallRed.png");
        ballRed= new JLabel();
        ballRed.setBounds(this.size.width/2 - 50, this.size.height - 100, 10, 10);
        ballRed.setBackground(Color.red);
        ballRed.setOpaque(true);
        //ballRed.setIcon(imageR);

        //imageB = new ImageIcon("BallBlue.png");
        ballBlue= new JLabel();
        ballBlue.setBounds(this.size.width/2 + 50, this.size.height - 100, 10, 10);
        ballBlue.setBackground(Color.blue);
        ballBlue.setOpaque(true);
        //ballBlue.setIcon(imageB);

        centre= new JLabel();
        centre.setBounds(this.size.width/2, this.size.height - 100, 0, 0);
        centre.setBackground(Color.black);
        centre.setOpaque(true);

        Point centreR=new Point(this.size.width/2 - 50, this.size.height - 100);
        Point centreB=new Point(this.size.width/2 + 50, this.size.height - 100);
        w=new Wheel(new Point(this.size.width/2, this.size.height - 100));
        w.ball_1 =w.new Ball(centreR);
        w.ball_2 =w.new Ball(centreB);
        
        this.addKeyListener(this);
        this.add(ballBlue);
        this.add(ballRed);
        this.add(centre);
        
		this.setLayout(null);
		
		
		/*
		 * CETTE PARTIE EST JUSTE POUR TESTER LA FONCTION ISLOSE QUI NE MARCHE PAS POUR L'INSTANT
		 * PLUS TARD IL FAUDRA SUPPRIMER CETTE PARTIE.
		 *
		Obstacle o1 =new Obstacle(this.size.width/3, 10, new Point(this.size.width/3,150), 1, 1, 0);
		ObstacleView ov1 = new ObstacleView(o1.getCoords());
		obstacles.add(ov1);
		this.add(ov1);
		
		
		
		Timer t = new Timer();
		
		TimerTask task = new TimerTask() {
	        public void run() {
	        	if(o1.getCoords()[3].getY()<=w.getCenter().getY()) {
	        		o1.update(5);
	    			setPositionObstacle(ov1, o1.getCoords());
	    			//System.out.print("task ");
	    			for(Point p : o1.getCoords()) {
	    				System.out.print("x " + p.getX()+" ");
	    				System.out.print("y " + p.getY()+" ");
	    				System.out.println();
	    			}
	    			System.out.println();
	    		}
	    		if(w.isLose(o1)) {
	    			System.out.println("PERDU");
	    			t.cancel();
	    		}
	        }
	    };
	    t.schedule(task, 10,10);
		
		*/

	}
	
	
	public void setPositionObstacle(ObstacleView ov, Point[] coord ) {
		ov.setPosition(coord);
		this.revalidate();
		this.repaint();
	}
	
	public void rotateObstacle(ObstacleView ov, Obstacle o, double rotation ) {
		ov.rotate(o, rotation);
		this.revalidate();
		this.repaint();
	}
	
	
	public void addObstacle(ObstacleView ov) {
		obstacles.add(ov);
	}
	
	
	public void rmObstacle(ObstacleView ov) {
		obstacles.remove(ov);
	}
	
	
	public void paintComponent(Graphics g, ObstacleView ov) {
		g.setColor(Color.white);
		g.drawPolygon(ov.getPolygon());
		g.fillPolygon(ov.getPolygon());
	}
	
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		for (ObstacleView ov : obstacles) {
			paintComponent(g, ov);
		}
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case 39 :
                w.rotateHoraire();
                
                angleR=angleR+10;
                angleB=angleB+10;
                radiasR=Math.toRadians(angleR);
                radiasB=Math.toRadians(angleB);
                ballRed.setLocation((int) (50*Math.cos(radiasR)+ this.size.width/2),(int) (50*Math.sin(radiasR) + this.size.height - 100) );
                ballBlue.setLocation((int) (50*Math.cos(radiasB)+ this.size.width/2),(int) (50*Math.sin(radiasB) + this.size.height - 100) );
                break;
                
            case 37 :
                w.rotateContreHoraire();
               
                angleR=angleR-10;
                angleB=angleB-10;
                radiasR=Math.toRadians(angleR);
                radiasB=Math.toRadians(angleB);
                ballRed.setLocation((int) (50*Math.cos(radiasR) + this.size.width/2),(int) (50*Math.sin(radiasR) + this.size.height - 100) );
                ballBlue.setLocation((int) (50*Math.cos(radiasB) + this.size.width/2),(int) (50*Math.sin(radiasB) + this.size.height - 100) );
                break;

        }

    }



	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
