package com.pi4.duet.view.game;

import java.awt.Color;    
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.*;

import com.pi4.duet.controller.game.GameController;

public class GameView extends JPanel { // représente la vue du jeu (graphismes, ...)
		
	private static final long serialVersionUID = -306594423077754361L;
		
	private GameController controller;

	private WheelView wheel;
	private JButton back, replay;
	private Dimension size;
	
	private double y_background = 0;
	private double background_speed = 0.5;
	
	private ArrayList<ObstacleView> obstacles = new ArrayList<ObstacleView>();

	private Image background = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();
	
	public GameView(Dimension size, GameController controller) {
		
		this.size = new Dimension(size.width / 3, size.height);

		this.controller = controller;

		Dimension dim = new Dimension(size.width / 3, size.height);
		this.setPreferredSize(dim);
		       		
		wheel = new WheelView(size, controller.getWheelController());
		this.add(wheel);
		
		this.addKeyListener(controller);
		this.addKeyListener(wheel.getController());
		this.setLayout(null);
	}
	
	public void affichePause() {
		String[] option = {"Reprendre le jeu", "Revenir au menu", "Quitter le jeu"};
		int indice = JOptionPane.showOptionDialog(this,
				"Le jeu est en pause, veuillez choisir une option",
				"Jeu en pause",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				new ImageIcon(getClass().getResource("/resources/img/pause.png")),
				option,
				option[0]);
		switch (indice) {
			case 0:
				decompte();
				break;			
			case 1:
				this.setVisible(false);
				controller.gameStop();
				controller.stopMusic();
				controller.affMenu();
				break;			
			case 2: System.exit(0);
		}
	}

	public void decompte(){
		String[] resume = {"Reprendre maintenant"};
		JOptionPane jboite2 = new JOptionPane("Le jeu va reprendre automatiquement dans moins de 3 secondes",
				JOptionPane.INFORMATION_MESSAGE, -1,
				new ImageIcon(getClass().getResource("/resources/img/resume.jpg")));
		jboite2.setOptions(resume);
		final Timer timer = new Timer(3000, e -> {
			jboite2.setValue(JOptionPane.CLOSED_OPTION);
		});
		timer.setRepeats(false);
		timer.start();
		jboite2.createDialog(this, "TENEZ VOUS PRÊT").setVisible(true);
		controller.setBackgroundMovement(false);
	}

	public void afficheWin(){
		JLabel win1 = new JLabel();
		win1.setText("VOUS AVEZ GAGNÉ");
		win1.setBounds(size.width/5, this.size.height/6 , this.size.width/5 * 3, this.size.height/6);
		win1.setFont(new Font("Arial", Font.BOLD, 25));
		win1.setForeground(Color.RED);
		win1.setVisible(true);
		this.add(win1);

		JLabel win2 = new JLabel();
		win2.setText("BRAVO");
		win2.setBounds((this.size.width/5)+80, (this.size.height/6)+30, this.size.width/5 * 3, this.size.height/6);
		win2.setFont(new Font("Arial", Font.BOLD, 25));
		win2.setForeground(Color.BLUE);
		win2.setVisible(true);
		this.add(win2);

		Timer timer = new Timer(3000, e->controller.affMenu());
		timer.setRepeats(false);
		timer.start();
	}
		
	public void refresh() {
		revalidate();
		repaint();
	}
		
	public void addObstacle(ObstacleView ov) {
		this.add(ov);
		obstacles.add(ov);
	}
	
	
	public void removeObstacle(ObstacleView ov) {
		this.remove(ov);
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		if (controller.isBackgroundEnabled()){
			for(int i = 0; i < 100; i++) {
				g.drawImage(background, 0, (int) (y_background + (i * 630)), size.width, size.height, this);
			}
		}
		else g.fillRect(0, 0, size.width, size.height);
		
		if(controller.getBackgroundMouvement() == false) {
			y_background -= background_speed;
			repaint();
		}
	}
	
	// Affichage lorsqu'un joueur perd la partie
	public void lostGame() {
		background =  new ImageIcon(this.getClass().getResource("/resources/img/background_grey.png")).getImage();
		wheel.greyWheel();
		
		back = new JButton("RETOUR");
		back.setBounds(size.width/5, this.size.height/6 , this.size.width/5 * 3, this.size.height/6);
		back.setForeground(Color.RED);
		back.setBackground(Color.BLACK);
		back.setFont(new Font("Arial", Font.BOLD, 50));
		back.setVisible(true);
		this.add(back);	
		setComponentZOrder(back, 1);
		
		replay = new JButton("REJOUER");
		replay.setBounds(this.size.width/5, this.size.height/6 * 3, this.size.width/5 * 3, this.size.height/6);
		replay.setBackground(Color.BLACK);
		replay.setForeground(Color.BLUE);
		replay.setFont(new Font("Arial", Font.BOLD, 50));
		replay.setVisible(true);
		add(replay);
		setComponentZOrder(replay, 0);
		
		back.addActionListener(e -> {
			reset();
			controller.affMenu();
		});
		
		replay.addActionListener(e -> {
			// rejouer
			reset();
			controller.replay();
		});
		this.setVisible(true);
		this.revalidate();
		this.repaint();		
	}

	// On réaffiche la partie
	private void reset() {
		this.remove(back);
		this.remove(replay);
		this.repaint();
		
		background = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();
		wheel.resetWheelColor();	
	}
	
	public Dimension getSize() {
		return size;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ObstacleView> getObstacles() { return (ArrayList<ObstacleView>) obstacles.clone(); }

	public WheelView getWheel() {
		// TODO Auto-generated method stub
		return wheel;
	}	
}
