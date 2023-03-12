package com.pi4.duet.view.home;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.pi4.duet.Auxiliaire;
import com.pi4.duet.controller.SettingsController;
import com.pi4.duet.view.Scale;

public class SettingsView extends JPanel{

	private static final long serialVersionUID = 8809186306938504775L;
	
	private Dimension size;
	private JButton back;
	private JButton son_on, son_off, music_on, music_off, inertie_on, inertie_off, fond_on, fond_off;
	private JLabel son, music, inertie, fond;
	private Icon back_img;
	private Image background = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();
	
	private SettingsController controller;
	
	public SettingsView(Dimension size, SettingsController controller, Scale scale) {
		this.controller = controller;
		this.setBackground(Color.black);
		Dimension dim = new Dimension(size.width / 3, size.height);
		this.size = dim;
		this.setPreferredSize(this.size);
		this.setLayout(null);
		
		back_img = Auxiliaire.resizeImage(new ImageIcon(this.getClass().getResource("/resources/img/back.png")), this.size.width/10, this.size.width/10);
		back = new JButton(back_img);
		back.setBounds(this.size.width/20, this.size.width/20 ,this.size.width/10, this.size.width/10);
		back.setBackground(Color.BLACK);
		this.add(back);
		
		back.addActionListener(e -> {
			controller.back();
		});
		
		int count = 0;
		
		son = new JLabel("Activer / Désactiver les effets sonores :", SwingConstants.CENTER);
		son.setBounds(0, (int) (this.size.width/20 + this.size.width/10 * 1.5), this.size.width, this.size.height/9);
		son.setFont(new Font("Arial", Font.BOLD, (int) (30 * scale.getScaleY())));
		son.setForeground(Color.LIGHT_GRAY);
		this.add(son);
		
		son_on = new JButton("ON");
		son_on.setBounds(this.size.width/9, this.size.width/6 + this.size.height/9, this.size.width/3, this.size.height/12);
		son_on.setBackground(Color.BLACK);
		son_on.setForeground(Color.GREEN);
		son_on.setFont(new Font("Arial", Font.BOLD, (int) (30 * scale.getScaleY())));
		son_on.setEnabled(false);
		this.add(son_on);
		son_on.addActionListener(e -> {
			son_on.setEnabled(false);
			son_off.setEnabled(true);
			controller.setEffects(true);
		});
		
		son_off = new JButton("OFF");
		son_off.setBounds(this.size.width/9 * 2 +  this.size.width/3, this.size.width/6 + this.size.height/9, this.size.width/3, this.size.height/12);
		son_off.setBackground(Color.BLACK);
		son_off.setForeground(Color.RED);
		son_off.setFont(new Font("Arial", Font.BOLD, (int) (30 * scale.getScaleY())));
		this.add(son_off);
		son_off.addActionListener(e -> {
			son_off.setEnabled(false);
			son_on.setEnabled(true);
			controller.setEffects(false);
		});
		
		count++;
		
		music = new JLabel("Activer / Désactiver la musique :", SwingConstants.CENTER);
		music.setBounds(0, (int) (this.size.width/20 + this.size.width/10 * 1.5) + count * this.size.height/5, this.size.width, this.size.height/9);
		music.setFont(new Font("Arial", Font.BOLD, (int) (30 * scale.getScaleY())));
		music.setForeground(Color.LIGHT_GRAY);
		this.add(music);
		
		music_on = new JButton("ON");
		music_on.setBounds(this.size.width/9, this.size.width/6 + this.size.height/9 + count * this.size.height/5, this.size.width/3, this.size.height/12);
		music_on.setBackground(Color.BLACK);
		music_on.setForeground(Color.GREEN);
		music_on.setFont(new Font("Arial", Font.BOLD, (int) (30 * scale.getScaleY())));
		music_on.setEnabled(false);
		this.add(music_on);
		music_on.addActionListener(e -> {
			music_on.setEnabled(false);
			music_off.setEnabled(true);
			controller.setMusic(true);
		});
		
		music_off = new JButton("OFF");
		music_off.setBounds(this.size.width/9 * 2 +  this.size.width/3, this.size.width/6 + this.size.height/9 + count * this.size.height/5, this.size.width/3, this.size.height/12);
		music_off.setBackground(Color.BLACK);
		music_off.setForeground(Color.RED);
		music_off.setFont(new Font("Arial", Font.BOLD, (int) (30 * scale.getScaleY())));
		this.add(music_off);
		music_off.addActionListener(e -> {
			music_off.setEnabled(false);
			music_on.setEnabled(true);
			controller.setMusic(false);
		});

		count++;
		
		inertie = new JLabel("Activer / Désactiver l'inertie du volant :", SwingConstants.CENTER);
		inertie.setBounds(0, (int) (this.size.width/20 + this.size.width/10 * 1.5) + count * this.size.height/5, this.size.width, this.size.height/9);
		inertie.setFont(new Font("Arial", Font.BOLD, (int) (30 * scale.getScaleY())));
		inertie.setForeground(Color.LIGHT_GRAY);
		this.add(inertie);
		
		inertie_on = new JButton("ON");
		inertie_on.setBounds(this.size.width/9, this.size.width/6 + this.size.height/9 + count * this.size.height/5, this.size.width/3, this.size.height/12);
		inertie_on.setBackground(Color.BLACK);
		inertie_on.setForeground(Color.GREEN);
		inertie_on.setFont(new Font("Arial", Font.BOLD, (int) (30 * scale.getScaleY())));
		inertie_on.setEnabled(false);
		this.add(inertie_on);
		inertie_on.addActionListener(e -> {
			inertie_on.setEnabled(false);
			controller.setInertie(true);
			inertie_off.setEnabled(true);
		});
		
		inertie_off = new JButton("OFF");
		inertie_off.setBounds(this.size.width/9 * 2 +  this.size.width/3, this.size.width/6 + this.size.height/9 + count * this.size.height/5, this.size.width/3, this.size.height/12);
		inertie_off.setBackground(Color.BLACK);
		inertie_off.setForeground(Color.RED);
		inertie_off.setFont(new Font("Arial", Font.BOLD, (int) (30 * scale.getScaleY())));
		this.add(inertie_off);
		inertie_off.addActionListener(e -> {
			inertie_off.setEnabled(false);
			controller.setInertie(false);
			inertie_on.setEnabled(true);
		});

		count++;
		
		fond = new JLabel("Activer / Désactiver l'arrière plan du jeu :", SwingConstants.CENTER);
		fond.setBounds(0, (int) (this.size.width/20 + this.size.width/10 * 1.5) + count * this.size.height/5, this.size.width, this.size.height/9);
		fond.setFont(new Font("Arial", Font.BOLD, (int) (30 * scale.getScaleY())));
		fond.setForeground(Color.LIGHT_GRAY);
		this.add(fond);
		
		fond_on = new JButton("ON");
		fond_on.setBounds(this.size.width/9, this.size.width/6 + this.size.height/9 + count * this.size.height/5, this.size.width/3, this.size.height/12);
		fond_on.setBackground(Color.BLACK);
		fond_on.setForeground(Color.GREEN);
		fond_on.setFont(new Font("Arial", Font.BOLD, (int) (30 * scale.getScaleY())));
		fond_on.setEnabled(false);
		this.add(fond_on);
		fond_on.addActionListener(e -> {
			fond_on.setEnabled(false);
			fond_off.setEnabled(true);
			controller.setBackground(true);
			repaint();
		});
		
		fond_off = new JButton("OFF");
		fond_off.setBounds(this.size.width/9 * 2 +  this.size.width/3, this.size.width/6 + this.size.height/9 + count * this.size.height/5, this.size.width/3, this.size.height/12);
		fond_off.setBackground(Color.BLACK);
		fond_off.setForeground(Color.RED);
		fond_off.setFont(new Font("Arial", Font.BOLD, (int) (30 * scale.getScaleY())));
		this.add(fond_off);
		fond_off.addActionListener(e -> {
			fond_off.setEnabled(false);
			fond_on.setEnabled(true);
			controller.setBackground(false);
			repaint();
		});		
	}	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (controller.getBackground()) g.drawImage(background, 0, 0, size.width, size.height, this);
	}
	
}
