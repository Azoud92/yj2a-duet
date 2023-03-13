package com.pi4.duet.view.home;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.*;

import com.pi4.duet.Auxiliaire;
import com.pi4.duet.controller.HomePageViewController;
import com.pi4.duet.view.Scale;
import com.pi4.duet.view.game.GameWindow;

public class HomePageView extends JPanel {
 
	private static final long serialVersionUID = 5945161001415252238L;

	private Dimension size;
	private BooleanButton level1, level2, level3, level4, level5;
	private JLabel title1, title2;
	private JButton settings, quit, lvlDuo;
	private Icon settings_i, quit_i;
	private HomePageViewController controller;
	
	private Image background = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();
		
	public HomePageView(Dimension size, JFrame frame, GameWindow window, HomePageViewController controller, Scale scale) {
		this.controller = controller;
		this.setBackground(Color.black);
		Dimension dim = new Dimension(size.width / 3, size.height);
		this.size = dim;
		this.setPreferredSize(dim);
		this.setLayout(null);
		
		title1 = new JLabel("DU");
		title1.setBounds((int) (this.size.width/4 + 7 * scale.getScaleX()), this.size.height/24, this.size.width/4 , this.size.height/4);
		title1.setForeground(Color.RED);
		title1.setBackground(Color.BLACK);
		title1.setFont(new Font("Arial", Font.BOLD, (int) (115 * scale.getScaleY())));
		this.add(title1);
		
		title2 = new JLabel("ET");
		//title2.setBounds((int) (title1.getLocation().x + title1.getSize().width - 5 * scale.getScaleXY()), this.size.height/24, this.size.width/4 , this.size.height/4);
		title2.setBounds((int) (this.size.width / 2 + 7 * scale.getScaleX()), this.size.height/24, this.size.width/4 , this.size.height/4);
		title2.setForeground(Color.BLUE);
		title2.setBackground(Color.BLACK);
		title2.setFont(new Font("Arial", Font.BOLD, (int) (115 * scale.getScaleY())));
		this.add(title2);
		
		int tx1 = (this.size.width - (this.size.width/5*3) ) / 4; 
		int tx2 = (this.size.width - (this.size.width/5*2) ) / 3; 
		
		int ty = (this.size.height/3 - (2*this.size.width/5))/3;
		
		level1 = new BooleanButton("1");
		level1.setBounds(tx1, this.size.height/12*4 - ty, this.size.width/5, this.size.width/5);
		level1.setBackground(Color.BLACK);
		level1.setForeground(Color.white);
		level1.setFocusable(false);
		level1.setFont(new Font("Arial", Font.BOLD, (int) (70 * scale.getScaleY())));
		this.add(level1);
		level1.addActionListener((ActionEvent e) ->{

			if(!level1.isButton()){
				level1.setButton(true);
				this.setVisible(false);
				controller.runParty(size, window,this);
			}
			else{
				String[] option={"Reprendre votre progression","Relancer depuis le début"};
				int indice= JOptionPane.showOptionDialog(this,
						"Veuillez choisir",
						"Progression en cours",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						new ImageIcon(getClass().getResource("/resources/img/reset.png")),
						option,
						option[0]);
				switch(indice){
					case 0: {
						this.setVisible(false);
						controller.continueParty();
						break;
					}
					case 1:{
						this.setVisible(false);
						controller.runParty(size, window,this);
						break;
					}
				}
			}



		});
		
		level2 = new BooleanButton("2");
		level2.setBounds(2 * tx1 + this.size.width/5, this.size.height/12*4 - ty, this.size.width/5, this.size.width/5);
		level2.setBackground(Color.BLACK);
		level2.setForeground(Color.white);
		level2.setFocusable(false);
		level2.setFont(new Font("Arial", Font.BOLD, (int) (70 * scale.getScaleY())));
		level2.setEnabled(true);
		this.add(level2);
		
		level2.addActionListener((ActionEvent e) -> {
			if(!level2.isButton()){
				level2.setButton(true);
				this.setVisible(false);
				controller.runLvl2(size, window,this);
			}else{
				String[] option={"Reprendre votre progression","Relancer depuis le début"};
				int indice= JOptionPane.showOptionDialog(this,
						"Veuillez choisir",
						"Progression en cours",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						new ImageIcon(getClass().getResource("/resources/img/reset.png")),
						option,
						option[0]);
				switch(indice){
					case 0: {
						this.setVisible(false);
						controller.continueParty();
					}
					case 1:{
						this.setVisible(false);
						controller.runLvl2(size, window,this);
						break;
					}
				}
			}
		});
		
		level3 = new BooleanButton("3");
		level3.setBounds(3 * tx1 + 2 * this.size.width/5, this.size.height/12*4 - ty, this.size.width/5, this.size.width/5);
		level3.setBackground(Color.BLACK);
		level3.setForeground(Color.white);
		level3.setFocusable(false);
		level3.setFont(new Font("Arial", Font.BOLD, (int) (70 * scale.getScaleY())));
		level3.setEnabled(false);
		this.add(level3);
		
		level4 = new BooleanButton("4");
		level4.setBounds(tx2, this.size.height/12*6 + this.size.width/10 - ty, this.size.width/5, this.size.width/5);
		level4.setBackground(Color.BLACK);
		level4.setForeground(Color.white);
		level4.setFocusable(false);
		level4.setFont(new Font("Arial", Font.BOLD, (int) (70 * scale.getScaleY())));
		level4.setEnabled(false);
		this.add(level4);
		
		level5 = new BooleanButton("5");
		level5.setBounds(2 * tx2 + this.size.width/5, this.size.height/12*6 + this.size.width/10 - ty, this.size.width/5, this.size.width/5);
		level5.setBackground(Color.BLACK);
		level5.setForeground(Color.white);
		level5.setFocusable(false);
		level5.setFont(new Font("Arial", Font.BOLD, (int) (70 * scale.getScaleY())));
		level5.setEnabled(false);
		this.add(level5);
		
		
		settings_i = Auxiliaire.resizeImage(new ImageIcon(this.getClass().getResource("/resources/img/settings.png")), this.size.width/13, this.size.width/13);
		settings = new JButton(settings_i);
		settings.setBounds(this.size.width - this.size.width/20 - this.size.width/10, this.size.height - this.size.width/20 - this.size.width/10, this.size.width/10,this.size.width/10);
		settings.setBackground(Color.BLACK);
		this.add(settings);
		
		settings.addActionListener(e -> {
			this.setVisible(false);
			controller.runSettings(size, window);
		});
		
		quit_i = Auxiliaire.resizeImage(new ImageIcon(this.getClass().getResource("/resources/img/quit.png")), this.size.width/10, this.size.width/10);
		quit = new JButton(quit_i);
		quit.setBounds(this.size.width/20, this.size.height - this.size.width/20 - this.size.width/10, this.size.width/10,this.size.width/10);
		quit.setBackground(Color.BLACK);
		this.add(quit);
		
		quit.addActionListener(e -> {
			// action à effectuer pour fermer le programme
			frame.dispose();
		});		
		
		lvlDuo = new JButton("1 vs 1");
		lvlDuo.setBounds(level4.getX(), quit.getY() - this.size.width/5 - quit.getHeight(), level5.getX() + level5.getWidth() - level4.getX(), this.size.width/5);
		lvlDuo.setBackground(Color.BLACK);
		lvlDuo.setForeground(Color.white);
		lvlDuo.setFocusable(false);
		lvlDuo.setFont(new Font("Arial", Font.BOLD, (int) (70 * scale.getScaleY())));
		this.add(lvlDuo);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (controller.getSettings().getBackground()) g.drawImage(background, 0, 0, size.width, size.height, this);
	}	
}
