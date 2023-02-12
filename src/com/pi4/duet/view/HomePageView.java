package com.pi4.duet.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


import com.pi4.duet.controller.HomePageViewController;


public class HomePageView extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5945161001415252238L;

	private HomePageViewController controller;
	private Dimension size;
	private double scaleX, scaleY;
	private JButton level1, level2, level3, level4, level5;
	private JLabel title, title1, title2;
	private JButton settings, quit;
	private Icon settings_i, quit_i;
	
	
	
	public HomePageView(Dimension size, double scaleX, double scaleY, JFrame frame, GameWindow window, HomePageViewController controller) {
		this.controller = controller;
		
		this.setBackground(Color.black);
		Dimension dim = new Dimension(size.width / 3, size.height);
		this.size = dim;
		this.setPreferredSize(dim);
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.setLayout(null);
		
		/*title = new JLabel("DUET");
		title.setBounds(this.size.width/5, this.size.height/24, this.size.width/5*3 , this.size.height/4);
		title.setForeground(Color.WHITE);
		title.setBackground(Color.BLACK);
		title.setFont(new Font("Arial", Font.BOLD, (int) (99 * scaleX)));
		this.add(title);*/
		
		title1 = new JLabel("DU");
		title1.setBounds(this.size.width/5, this.size.height/24, this.size.width/3 , this.size.height/4);
		title1.setForeground(Color.RED);
		title1.setBackground(Color.BLACK);
		title1.setFont(new Font("Arial", Font.BOLD, (int) (104 * scaleX)));
		this.add(title1);
		
		title2 = new JLabel("ET");
		title2.setBounds(title1.getLocation().x + title1.getSize().width, this.size.height/24, this.size.width/3 , this.size.height/4);
		title2.setForeground(Color.BLUE);
		title2.setBackground(Color.BLACK);
		title2.setFont(new Font("Arial", Font.BOLD, (int) (104 * scaleX)));
		this.add(title2);
		
		int tx1 = (this.size.width - (this.size.width/5*3) ) / 4; 
		int tx2 = (this.size.width - (this.size.width/5*2) ) / 3; 
		
		level1 = new JButton("1");
		level1.setBounds(tx1, this.size.height/12*4, this.size.width/5, this.size.width/5);
		level1.setBackground(Color.WHITE);
		level1.setFont(new Font("Arial", Font.BOLD, (int) (45 * scaleX)));
		this.add(level1);
		
		level1.addActionListener((ActionEvent e) ->{
			this.setVisible(false);
			controller.runParty(size, scaleX, scaleY, window);

		});
		
		level2 = new JButton("2");
		level2.setBounds(2 * tx1 + this.size.width/5, this.size.height/12*4, this.size.width/5, this.size.width/5);
		level2.setBackground(Color.WHITE);
		level2.setFont(new Font("Arial", Font.BOLD, (int) (45 * scaleX)));
		level2.setEnabled(false);
		this.add(level2);
		
		level3 = new JButton("3");
		level3.setBounds(3 * tx1 + 2 * this.size.width/5, this.size.height/12*4, this.size.width/5, this.size.width/5);
		level3.setBackground(Color.WHITE);
		level3.setFont(new Font("Arial", Font.BOLD, (int) (45 * scaleX)));
		level3.setEnabled(false);
		this.add(level3);
		
		level4 = new JButton("4");
		level4.setBounds(tx2, this.size.height/12*6 + this.size.width/10, this.size.width/5, this.size.width/5);
		level4.setBackground(Color.WHITE);
		level4.setFont(new Font("Arial", Font.BOLD, (int) (45 * scaleX)));
		level4.setEnabled(false);
		this.add(level4);
		
		level5 = new JButton("5");
		level5.setBounds(2 * tx2 + this.size.width/5, this.size.height/12*6 + this.size.width/10, this.size.width/5, this.size.width/5);
		level5.setBackground(Color.WHITE);
		level5.setFont(new Font("Arial", Font.BOLD, (int) (45 * scaleX)));
		level5.setEnabled(false);
		this.add(level5);
		
		settings_i = resizeImage(new ImageIcon("src/com/pi4/duet/resources/settings.png"), this.size.width/13, this.size.width/13);
		settings = new JButton(settings_i);
		settings.setBounds(this.size.width - this.size.width/20 - this.size.width/10, this.size.height - this.size.width/20 - this.size.width/10, this.size.width/10,this.size.width/10);
		settings.setBackground(Color.WHITE);
		//settings.setFont(new Font("Arial", Font.BOLD, (int) (45 * scaleX)));
		this.add(settings);
		
		quit_i = resizeImage(new ImageIcon("src/com/pi4/duet/resources/quit.png"), this.size.width/10, this.size.width/10);
		quit = new JButton(quit_i);
		quit.setBounds(this.size.width/20, this.size.height - this.size.width/20 - this.size.width/10, this.size.width/10,this.size.width/10);
		quit.setBackground(Color.WHITE);
		quit.setFont(new Font("Arial", Font.BOLD, (int) (30 * scaleX)));
		this.add(quit);
		
		quit.addActionListener(e -> {
			// action à effectuer pour fermer le programme
			frame.dispose();
		});
		
		
	}
	
	public static ImageIcon resizeImage(ImageIcon i, int width, int height) {
		return new ImageIcon(i.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
	}
	
	
}
