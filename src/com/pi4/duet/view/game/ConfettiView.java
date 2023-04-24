package com.pi4.duet.view.game;

import java.awt.Color; 
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

import com.pi4.duet.Point;

public class ConfettiView extends JPanel implements Runnable {

	private static final long serialVersionUID = 6251420393206375207L;
	private static int widthC;
    private static int heightC;

    private Thread animate;
    private boolean running;
    private static Point depart;

    private ArrayList<Confetti> confettis;

    public ConfettiView(int widthC, int heightC, Point depart) {
    	this.setVisible(true);
    	this.setOpaque(false);
    	
    	ConfettiView.depart = depart;
    	ConfettiView.widthC = widthC;
    	ConfettiView.heightC = heightC;
        this.setBounds(0, 0, widthC, heightC);

        confettis = new ArrayList<>();
        for (int i = 0; i < 1500; i++) {
            Confetti confetti = new Confetti();
            confettis.add(confetti);
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Confetti confetti : confettis) {
            g.setColor(confetti.getColor());
            Graphics2D g2d = (Graphics2D) g;
            g2d.fill(confetti.getShape());
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        animate = new Thread(this);
        animate.start();
    }
        
    @Override
    public void run() {
        running = true;

        while (running) {
            for (Confetti confetti : confettis) {
                confetti.update();
            }
            revalidate();
            repaint();
        }
    }

    private static class Confetti {
        private static final Random RANDOM = new Random();
        private int x;
        private int y;
        
        private int vx;
        private int vy;
        
        private int sizeC;
        
        private int colorType;
        private int shapeType;

        public Confetti() {
            x = (int) depart.getX() ;
            y = (int) depart.getY();
            
            sizeC = RANDOM.nextInt(20) + 10;
            colorType = RANDOM.nextInt(2);
            
            while(vx ==0|| vy ==0) {
            	vx = RANDOM.nextInt(11) - 5;
            	vy = RANDOM.nextInt(11) - 5;
            	}
            shapeType = RANDOM.nextInt(3);
        }

        public Color getColor() {
        	switch (colorType) {
	            case 0:
	                return Color.red;
	            case 1:
	                return Color.blue;
	            default:
	                return Color.red;
        	}
		}

		public void update() {
			vx = RANDOM.nextInt(11) - 5;
        	vy = RANDOM.nextInt(11) - 5;
        	
            x += vx;
            y += vy;

            if (x < 0 || x + sizeC > widthC) vx = -vx;
            if (y < 0 || y + sizeC > heightC) vy = -vy;
        }

        public Shape getShape() {
            switch (shapeType) {
                case 0:
                    return new Rectangle(x, y, sizeC, sizeC);
                case 1:
                    return new Ellipse2D.Double(x, y, sizeC, sizeC / 2.0);
                case 2:
                    return createTriangle();
                default:
                    return createTriangle();
            }
        }
        
        private Shape createTriangle() {
            int[] xP = {x + sizeC / 2, x + sizeC, x};
            int[] yP = {y, y + sizeC, y + sizeC};
            return new Polygon(xP, yP, 3);
        }
    }

	public void finish() {
		running = false;		
	} 
 
}
