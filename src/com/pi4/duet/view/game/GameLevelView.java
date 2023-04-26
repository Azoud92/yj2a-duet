package com.pi4.duet.view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.Timer;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.game.GameLevelController;
import com.pi4.duet.model.home.Commands;

public class GameLevelView extends GameView {

	/**
	 *
	 */
	private static final long serialVersionUID = 9189517439320799864L;

	public GameLevelView(Dimension size, Scale scale, Commands commands, GameLevelController controller) {
		super(size, scale, commands, controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void afficheWin(){
		JLabel win1 = new JLabel();
		win1.setText("VOUS AVEZ GAGNÉ !");
		win1.setBounds(size.width/6, this.size.height/5*2 , this.size.width/6 * 4, this.size.height/6);
		win1.setFont(new Font("Arial", Font.BOLD, (int) (43 * scale.getScaleY())));
		win1.setForeground(Color.WHITE);
		win1.setVisible(true);
		this.add(win1);

		JLabel win2 = new JLabel();
		win2.setText("BRAVO");
		win2.setBounds((this.size.width/6)+(int) (140 * scale.getScaleX()), (this.size.height/5)*2+ (int) (50 * scale.getScaleY()), this.size.width/6 * 4, this.size.height/6);
		win2.setFont(new Font("Arial", Font.BOLD, (int) (43 * scale.getScaleY())));
		win2.setForeground(Color.WHITE);
		win2.setVisible(true);
		this.add(win2);
		
		ConfettiView cv = new ConfettiView(this.getWidth(), this.getHeight(),controller.getWheelController().getWheelCenter());
		this.add(cv);

		
		Timer timer = new Timer(3000, e->timerFinish(cv));
		timer.setRepeats(false);
		timer.start();
	}
}
