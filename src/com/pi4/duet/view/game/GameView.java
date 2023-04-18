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
import com.pi4.duet.model.game.data.LogBuffer;

public abstract class GameView extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -2419747303611397162L;

	protected GameController controller;

	private WheelView wheelView;
	private JButton back, replay;
	protected Dimension size;

	protected double y_background = 0;
	protected double background_speed = 0.5;
	private LogBuffer log = new LogBuffer("", "test");

	private ArrayList<ObstacleView> obstacles = new ArrayList<ObstacleView>();
	private int firstIndexObstacleVisible = 0;

	protected Image background = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();

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
	}

	public final void affichePause() {
		log.appendLn(System.currentTimeMillis() + " : Affichage du menu de pause");
		log.write();
		log.flush();
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

	public final void decompte(){
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
		jboite2.createDialog(this, "TENEZ VOUS PRÃŠT").setVisible(true);
		controller.setBackgroundMovement(false);
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

	// Affichage lorsqu'un joueur perd la partie
	public void lostGame() {
		background =  new ImageIcon(this.getClass().getResource("/resources/img/background_grey.png")).getImage();
		wheelView.greyWheel();

		back = new JButton("RETOUR");
		back.setBounds(size.width/5, this.size.height/6 , this.size.width/5 * 3, this.size.height/6);
		back.setForeground(Color.RED);
		back.setFont(new Font("Arial", Font.BOLD, (int) (50 * scale.getScaleY())));
		back.setVisible(true);
		back.setBorderPainted(false);
		back.setContentAreaFilled(false);
		back.setFocusPainted(false);
		this.add(back);
		setComponentZOrder(back, 1);

		

		replay = new JButton("REJOUER");
		replay.setBounds(this.size.width/5, this.size.height/6 * 3, this.size.width/5 * 3, this.size.height/6);
		replay.setForeground(Color.BLUE);
		replay.setFont(new Font("Arial", Font.BOLD, (int) (50 * scale.getScaleY())));
		replay.setVisible(true);
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
		this.setVisible(true);
		this.revalidate();
		this.repaint();
	}

	// On réaffiche la partie
	protected void reset() {
		this.remove(back);
		this.remove(replay);
		this.repaint();

		background = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();
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
