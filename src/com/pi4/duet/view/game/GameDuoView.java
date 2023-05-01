package com.pi4.duet.view.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.game.GameDuoController;
import com.pi4.duet.model.home.Commands;

public class GameDuoView extends GameView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6414643001608861521L;

	private WheelView wheelTopView;
	
	public GameDuoView(Dimension size, Scale scale, Commands commands, GameDuoController controller) {
		super(size, scale, commands, controller);
		this.remove(effectLabel);
		this.remove(bar);
		wheelTopView = new WheelView(size, controller.getTopWheelController());
		this.add(wheelTopView);
		
		this.addKeyListener(wheelTopView.getController());
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.superPaintComponent(g);
		if (controller.isBackgroundEnabled()){
			for(int i = 0; i < 100; i++) {
				g.drawImage(background, 0, (int) (y_background + (i * 630)), size.width, size.height, this);
			}
		}
		else g.fillRect(0, 0, size.width, size.height);
	}
	
	@Override
	public final void affichePause() {
		background =  bg_grey;
		wheelView.greyWheel();
		wheelTopView.greyWheel();
		
		close.setVisible(true);
		menu.setVisible(true);
		resume.setVisible(true);
		
		setComponentZOrder(resume, 2);
		setComponentZOrder(close, 1);
		setComponentZOrder(menu, 0);
		
		this.setVisible(true);
		this.revalidate();
		this.repaint();		
	
	}
	
	@Override
	protected void gameResume() {
		background = bg;
		wheelView.resetWheelColor();
		wheelTopView.resetWheelColor();
		
		close.setVisible(false);
		menu.setVisible(false);
		resume.setVisible(false);
		
		this.setVisible(true);
		this.revalidate();
		this.repaint();		
	}
	
	@Override
	public void lostGame() {
		wheelTopView.greyWheel();
		super.lostGame();
	}
	
	@Override
	protected void reset() {
		super.reset();
		wheelTopView.resetWheelColor();
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (controller.isOnGame()) {			
			if (e.getKeyCode() == commands.getPause()) {
				controller.setBackgroundMovement(true);				
				controller.startPause();
				affichePause();
			}
		}
		else {
			if (commands.getPause() == e.getKeyCode()) {
				gameResume();
				controller.gameResume();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public WheelView getWheelTopView() {
		// TODO Auto-generated method stub
		return wheelTopView;
	}

}
