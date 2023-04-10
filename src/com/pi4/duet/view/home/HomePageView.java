package com.pi4.duet.view.home;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pi4.duet.Auxiliaire;
import com.pi4.duet.Point;
import com.pi4.duet.Scale;
import com.pi4.duet.controller.home.HomePageViewController;
import com.pi4.duet.view.game.GameWindow;
import com.pi4.duet.model.game.data.LogBuffer;

public class HomePageView extends JPanel {

	private static final long serialVersionUID = 5945161001415252238L;

	private Dimension size;
	private LevelButton level1, level2, level3, level4, level5;
	private JLabel title1, title2;
	private JButton settings, quit, lvlDuo;
	private Icon settings_i, quit_i;
	private HomePageViewController controller;
	private Scale scale;
	private int progression;
	private Timer timer;

	private GameWindow window;

	private Image background = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();

	public HomePageView(Dimension size, JFrame frame, GameWindow window, HomePageViewController controller, Scale scale) {
		this.controller = controller;
		this.scale = scale;
		this.window = window;
		this.setBackground(Color.black);
		Dimension dim = new Dimension(size.width / 3, size.height);
		this.size = dim;
		this.setPreferredSize(dim);
		this.setLayout(null);
		progression = 0;
		
		

		title1 = new JLabel("DU");
		title1.setBounds(this.size.width/2 - this.size.width/7, this.size.height/24, this.size.width/4 , this.size.height/4);
		title1.setForeground(Color.RED);
		title1.setBackground(Color.BLACK);
		title1.setFont(new Font("Arial", Font.BOLD, (int) (110 * scale.getScaleY())));
		//this.add(title1);

		title2 = new JLabel("ET");
		title2.setBounds((int) (this.size.width / 2 + 6 * scale.getScaleX()), this.size.height/24, this.size.width/4 , this.size.height/4);
		title2.setForeground(Color.BLUE);
		title2.setBackground(Color.BLACK);
		title2.setFont(new Font("Arial", Font.BOLD, (int) (110 * scale.getScaleY())));
		//this.add(title2);
		
		
		this.timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				progression++;
				repaint();
				if(progression == 25) {
					add(title1);
					repaint();
				}
				if(progression == 75) {
					title1.setLocation((int) (dim.width/4 + 12 * scale.getScaleX()), dim.height/24);;
					add(title2);
				}
				if(progression == 100) {
					initHPV(frame);
					
				}
			}
		}, 0, 20);

		
	}
	
	public void initHPV(JFrame frame) {
		int tx1 = (this.size.width - (this.size.width/5*3) ) / 4;
		int tx2 = (this.size.width - (this.size.width/5*2) ) / 3;

		int ty = (this.size.height/3 - (2*this.size.width/5))/3;

		level1 = new LevelButton(1, new Point(tx1, this.size.height / 12 * 4 - ty), this.size.width / 5, this.size.width / 5);
		this.add(level1);

		level2 = new LevelButton(2, new Point(2 * tx1 + this.size.width / 5, this.size.height / 12 * 4 - ty), this.size.width / 5, this.size.width / 5);
		this.add(level2);

		level3 = new LevelButton(3, new Point(3 * tx1 + 2 * this.size.width / 5, this.size.height/12 * 4 - ty), this.size.width / 5, this.size.width / 5);
		this.add(level3);

		level4 = new LevelButton(4, new Point(tx2, this.size.height/12 * 6 + this.size.width / 10 - ty), this.size.width / 5, this.size.width / 5);
		this.add(level4);

		level5 = new LevelButton(5, new Point(2 * tx2 + this.size.width / 5, this.size.height/12 * 6 + this.size.width / 10 - ty), this.size.width / 5, this.size.width / 5);
		this.add(level5);

		settings_i = Auxiliaire.resizeImage(new ImageIcon(this.getClass().getResource("/resources/img/settings.png")), this.size.width/13, this.size.width/13);
		settings = new JButton(settings_i);
		settings.setBounds(this.size.width - this.size.width/10, this.size.height - this.size.width/20 - this.size.width/10, this.size.width/10,this.size.width/10);
		settings.setBorderPainted(false);
		settings.setContentAreaFilled(false);
		settings.setFocusPainted(false);
		this.add(settings);

		settings.addActionListener(e -> {
			this.setVisible(false);
			controller.runSettings(size, window);
		});

		quit_i = Auxiliaire.resizeImage(new ImageIcon(this.getClass().getResource("/resources/img/quit.png")), this.size.width/10, this.size.width/10);
		quit = new JButton(quit_i);
		quit.setBounds(0, this.size.height - this.size.width/20 - this.size.width/10, this.size.width/10,this.size.width/10);
		quit.setBorderPainted(false);
		quit.setContentAreaFilled(false);
		quit.setFocusPainted(false);
		this.add(quit);

		quit.addActionListener(e -> {
			// action à effectuer pour fermer le programme
			if(controller.getSettings().getMusic()) controller.stopMusic();
			frame.dispose();
		});

		lvlDuo = new JButton("1 vs 1");
		lvlDuo.setBounds(level4.getX(), quit.getY() - this.size.width/5 - quit.getHeight(), level5.getX() + level5.getWidth() - level4.getX(), this.size.width/5);
		lvlDuo.setBackground(Color.BLACK);
		lvlDuo.setForeground(Color.white);
		lvlDuo.setFocusable(false);
		lvlDuo.setFont(new Font("Arial", Font.BOLD, (int) (70 * scale.getScaleY())));
		this.add(lvlDuo);
		lvlDuo.addActionListener(e -> {
			this.setVisible(false);
			controller.runLvlDuo(window, this, false);
		});
		
		controller.runMusic();
	}

	private class LevelButton extends JButton {

		/**
		 *
		 */
		private static final long serialVersionUID = -2116619883053158714L;

		public LevelButton(int numLevel, Point coords, int width, int height) {
			super(String.valueOf(numLevel));

			setBounds((int) coords.getX(), (int) coords.getY(), width, height);
			setBackground(Color.BLACK);
			setForeground(Color.WHITE);
			setFocusable(false);
			setFont(new Font("Arial", Font.BOLD, (int) (70 * scale.getScaleY())));
			if (controller.isLevelAvailable(numLevel)) setEnabled(true);
			else setEnabled(false);

			addActionListener(e -> {
				HomePageView.this.setVisible(false);
				controller.runLevel(window, HomePageView.this, numLevel, false);
			});
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (controller.getSettings().getBackground()) g.drawImage(background, 0, 0, size.width, size.height, this);
		
		if(progression < 101 && progression > -1) {
			int placeW = size.width /5;
			int placeH = size.height/7;
		    g.setColor(Color.red);
		    g.fillRoundRect(placeW, placeH * 3, placeW * 3 * progression/100, placeH, 20, 20);
		    
		    g.setColor(Color.white);
		    g.drawRoundRect(placeW, placeH*3, placeW*3 , placeH, 20, 20);
	    }
	}
}
