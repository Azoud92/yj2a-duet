package com.pi4.duet.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.pi4.duet.controller.GameViewController;

public class GameView extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -306594423077754361L;
	
	protected GameViewController controller;
	protected Dimension size;
	protected double scaleX, scaleY;

	
	public GameView(Dimension size, double scaleX, double scaleY) {
		this.setBackground(Color.black);
		Dimension dim = new Dimension(size.width/3,size.height);
		this.size = dim;
		this.setPreferredSize(dim);
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.setLayout(null);

		
	}
	
	
}
