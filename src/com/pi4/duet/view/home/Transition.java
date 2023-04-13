package com.pi4.duet.view.home;

import java.awt.Dimension;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import com.pi4.duet.model.game.Direction;


public class Transition extends JPanel{

    private JPanel jp1;
    private JPanel jp2;
    private int width;
    private int height;
    private Direction dir;
    private boolean isTransition;
    

    public Transition(JPanel jp1, JPanel jp2, int width, int height, Direction dir) {
    	this.setPreferredSize(new Dimension(width, height));
    	this.setVisible(true);
    	this.jp1 = jp1;
    	this.jp2 = jp2;
    	this.dir = dir;
    	this.width = width;
    	this.height = height;
    	this.setLayout(null);

	    if(dir == Direction.LEFT) {
	        jp1.setBounds(0, 0, width, height);
	        jp2.setBounds(width, 0, width, height);
        }
	    else if(dir == Direction.RIGHT) {
	        jp2.setBounds(0, 0, width, height);
	        jp1.setBounds(-width, 0, width, height);
        }
	    jp1.setVisible(true);
	    jp2.setVisible(true);
    	this.add(jp1);
    	this.add(jp2);
	    	
    }
    
    public boolean getTransition() {
    	return isTransition;
    }

    public void transition() {
    	isTransition = true;
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = width; i >= 0; i--) {
                    int x1 = jp1.getLocation().x;
                    int x2 = jp2.getLocation().x;
                    
                    if(dir == Direction.LEFT) {
        	            jp1.setBounds(x1-1, 0, width, height);
        	            jp2.setBounds(x2-1, 0, width, height);
        	            Transition.this.repaint();
        	            if (x2 <= 0) {
        	            	Transition.this.remove(jp1);
        	            	jp1.setVisible(false);
        	            	Transition.this.repaint();
        	                }
        	           }
                    
                    else if(dir == Direction.RIGHT) {
        	            jp1.setBounds(x1+1, 0, width, height);
        	            jp2.setBounds(x2+1, 0, width, height);
        	            Transition.this.repaint();
        	            if (x1 >= 0) {
        	            	Transition.this.remove(jp2);
        	            	jp2.setVisible(false);
        	            	Transition.this.repaint();
        	                }
        	           }
                    Transition.this.repaint();
                    
                    Thread.sleep(1);
                }
            	isTransition = false;
                return null;
            }
            
            
            

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

}
