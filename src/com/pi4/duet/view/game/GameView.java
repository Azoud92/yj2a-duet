package com.pi4.duet.view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.game.GameController;

public abstract class GameView extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -2419747303611397162L;

	protected GameController controller;

	private WheelView wheelView;
	private JButton back, replay, resume, menu, close;
	protected Dimension size;

	protected double y_background = 0;
	protected double background_speed = 0.5;

	private ArrayList<ObstacleView> obstacles = new ArrayList<ObstacleView>();
	private int firstIndexObstacleVisible = 0;

	protected Image bg = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();
	protected Image bg_grey = new ImageIcon(this.getClass().getResource("/resources/img/background_grey.png")).getImage();
	
	protected Image background = bg;
	
	private int progression = 0;

	private JLabel effectLabel = new JLabel();
	
	private JProgressBar bar;
	
	private Scale scale;
	
	public GameView(Dimension size, Scale scale, GameController controller) {
		this.size = new Dimension(size.width, size.height);
		this.controller = controller;
		this.scale = scale;
		
		Dimension dim = new Dimension(size.width, size.height);
		this.setPreferredSize(dim);

		wheelView = new WheelView(size, controller.getWheelController());
		this.add(wheelView);
		
		effectLabel.setText("Appuyez sur '↑' pour faire disparaître un obstacle");
		effectLabel.setBounds(125, 30, (int) (400 * scale.getScaleX()), 30);
		effectLabel.setForeground(Color.WHITE);
		effectLabel.setVisible(false);
		this.add(effectLabel);
		
		bar = new JProgressBar(0,100);
		bar.setBounds(30, 30, 90, 30);
		bar.setValue(progression);
		bar.setForeground(Color.MAGENTA);
		bar.setOpaque(false);
		
		
		this.add(bar);
		this.addKeyListener(controller);
		this.addKeyListener(wheelView.getController());
		this.setLayout(null);
		
		
		back = new JButton("RETOUR");
		back.setBounds(size.width/5, this.size.height/6 , this.size.width/5 * 3, this.size.height/6);
		back.setForeground(Color.RED);
		back.setFont(new Font("Arial", Font.BOLD, (int) (50 * scale.getScaleY())));
		back.setVisible(false);
		back.setBorderPainted(false);
		back.setContentAreaFilled(false);
		back.setFocusPainted(false);
		this.add(back);
		setComponentZOrder(back, 1);


		replay = new JButton("REJOUER");
		replay.setBounds(this.size.width/5, this.size.height/6 * 3, this.size.width/5 * 3, this.size.height/6);
		replay.setForeground(Color.BLUE);
		replay.setFont(new Font("Arial", Font.BOLD, (int) (50 * scale.getScaleY())));
		replay.setVisible(false);
		replay.setBorderPainted(false);
		replay.setContentAreaFilled(false);
		replay.setFocusPainted(false);
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
		
		
		
		resume = new JButton("REPRENDRE");
		resume.setBounds(size.width/5, this.size.height/7 , this.size.width/5 * 3, this.size.height/7);
		resume.setForeground(Color.RED);
		resume.setFont(new Font("Arial", Font.BOLD, (int) (50 * scale.getScaleY())));
		resume.setVisible(false);
		this.add(resume);
		resume.setBorderPainted(false);
		resume.setContentAreaFilled(false);
		resume.setFocusPainted(false);
		
		resume.addActionListener(e->{
			gameResume();
			controller.gameResume();
		});
		
		
		menu = new JButton("RETOURNER AU MENU");
		menu.setBounds(size.width/45, 3 * this.size.height/7 , this.size.width/45 * 44, this.size.height/7);
		menu.setForeground(Color.RED);
		menu.setFont(new Font("Arial", Font.BOLD, (int) (50 * scale.getScaleY())));
		menu.setVisible(false);
		this.add(menu);
		menu.setBorderPainted(false);
		menu.setContentAreaFilled(false);
		menu.setFocusPainted(false);
		
		menu.addActionListener(e->{
			this.setVisible(false);
			controller.gameStop();
			controller.stopMusic();
			controller.affMenu();
		});
		
		
		close = new JButton("QUITTER LE JEU");
		close.setBounds(size.width/10, 5 * this.size.height/7 , this.size.width/10 * 8, this.size.height/7);
		close.setForeground(Color.RED);
		close.setFont(new Font("Arial", Font.BOLD, (int) (50 * scale.getScaleY())));
		close.setVisible(false);
		this.add(close);
		close.setBorderPainted(false);
		close.setContentAreaFilled(false);
		close.setFocusPainted(false);
		
		close.addActionListener(e->{
			System.exit(0);
		});
		
		
	}

	
	
	private void gameResume() {
		background = bg;
		wheelView.resetWheelColor();
		
		close.setVisible(false);
		menu.setVisible(false);
		resume.setVisible(false);
		
		this.setVisible(true);
		this.revalidate();
		this.repaint();
		
	}

	
	public final void affichePause() {
		background =  bg_grey;
		wheelView.greyWheel();
		
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

	protected void timerFinish(ConfettiView cv) {
		controller.affMenu();
		cv.finish();		
	}


	public final void refresh() {
		revalidate();
		repaint();
	}

	public final void addObstacle(ObstacleView ov) {
		this.add(ov);
		obstacles.add(ov);
	}


	public final void removeObstacle(ObstacleView ov) {
		this.remove(ov);
		this.repaint();
		this.incrFirstIndexObstacleVisible();
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

		if(!controller.getBackgroundMouvement()) {
			y_background -= background_speed;
			repaint();
		}
		
		bar.setValue(progression);
	}

	
	public void lostGame() {
		background =  bg_grey;
		wheelView.greyWheel();

		back.setVisible(true);
		replay.setVisible(true);
		
		this.setVisible(true);
		this.revalidate();
		this.repaint();
	}

	// On réaffiche la partie
	protected void reset() {
		back.setVisible(false);
		replay.setVisible(false);;
		this.repaint();

		background = bg;
		wheelView.resetWheelColor();
	}

	
	@Override
	public final Dimension getSize() {
		return size;
	}

	@SuppressWarnings("unchecked")
	public final ArrayList<ObstacleView> getObstacles() { return (ArrayList<ObstacleView>) obstacles.clone(); }

	public final WheelView getWheel() {
		// TODO Auto-generated method stub
		return wheelView;
	}
	
	public JProgressBar getBar() {
		return bar;
	}
	
	public void effectCanBeUsed() {
		effectLabel.setVisible(true);
	}

	public void updateProgressionEffect() {
		// TODO Auto-generated method stub
		this.progression = (int) controller.getProgressionEffect();
	}

	public void useEffect() {
		// TODO Auto-generated method stub
		this.effectLabel.setVisible(false);
	}

	public int getFirstIndexObstacleVisible() {
		return firstIndexObstacleVisible;
	}

	public void incrFirstIndexObstacleVisible() {
		this.firstIndexObstacleVisible += 1;
	}

}
