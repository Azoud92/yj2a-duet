package com.pi4.duet.view;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;

import com.pi4.duet.controller.GameController;
import com.pi4.duet.controller.HomePageViewController;
import com.pi4.duet.model.Point;

public class GameView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -306594423077754361L;
	
	private GameController controller;
	private HomePageViewController homePageViewController;

	private BallView ballRed, ballBlue;
	private Dimension size;

	
	private Image background = new ImageIcon(this.getClass().getResource("/resources/background.png")).getImage();



	public GameView(Dimension size, GameController controller) {

		this.size = size;
		this.controller = controller;
		Dimension dim = new Dimension(size.width / 3, size.height);
		this.setPreferredSize(dim);
		
		int ballRadius = controller.getBallRadius();
		ballRed = new BallView((int) (controller.getCenterBall1().getX()) - ballRadius, (int) controller.getCenterBall1().getY(), 2 * ballRadius, 2 * ballRadius, Color.red);
		this.add(ballRed);
		
		ballBlue = new BallView((int) (controller.getCenterBall2().getX() - ballRadius), (int) controller.getCenterBall2().getY(), 2 * ballRadius, 2 * ballRadius, Color.blue);
		this.add(ballBlue);
		        
		this.setLayout(null);
        
        this.addKeyListener(controller);


        this.add(ballBlue);
        this.add(ballRed);

		this.setLayout(null);
	}
	public void affichePause(){
		String[] option={"Reprendre le jeu", "Quitter le jeu"};
		JOptionPane jboite=new JOptionPane();
		int indice=jboite.showOptionDialog(this,
				"Le jeu est en pause, veuillez choisir une option",
				"Jeu en pause",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				new ImageIcon(getClass().getResource("/resources/pause.png")),
				option,
				option[0]);
		switch(indice){
			case 0: {
				decompte();
				controller.setPause(controller.isPause());
				controller.getModel().gamePausedOrResumed();
				break;
			}
			case 1: System.exit(0);


		}
	}


	public void decompte(){
		String []resume={"Reprendre maintenant"};
		JOptionPane jboite2 = new JOptionPane("Le jeu va reprendre automatiquement dans moins de 3 secondes",
				JOptionPane.INFORMATION_MESSAGE,-1,
				new ImageIcon(getClass().getResource("/resources/resume.jpg")));
		jboite2.setOptions(resume);
		final Timer timer = new Timer(3000, e -> {
			jboite2.setValue(JOptionPane.CLOSED_OPTION);
		});
		timer.setRepeats(false);
		timer.start();
		jboite2.createDialog(this, "TENEZ VOUS PRÊT").setVisible(true);

	}


	
	public void setBallsPosition(Point blue, Point red) {
		this.ballBlue.setLocation((int) blue.getX() - controller.getBallRadius(), (int) blue.getY());
		this.ballRed.setLocation((int) red.getX() - controller.getBallRadius(), (int) red.getY());
	}
	
	private class BallView extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8381550184021712931L;
		private int x, y, width, height;
		private Color color;
		
		public BallView(int x, int y, int width, int height, Color color) {
			this.x = x;
			this.y = y;
			this.width  = width;
			this.height = height;
			this.color = color;			
		}
		
		public void setLocation(int x, int y) {
			this.x = x;
			this.y = y;			
		}		
	}
	
	public void refresh() {
		revalidate();
		repaint();
	}
		
	public void addObstacle(ObstacleView ov) {
		this.add(ov);
	}
	
	
	public void removeObstacle(ObstacleView ov) {
		this.remove(ov);
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(background, 0, 0, size.width, size.height, this);
		g.setColor(ballBlue.color);
		g.fillOval(ballBlue.x, ballBlue.y, ballBlue.width, ballBlue.height);
		
		g.setColor(ballRed.color);
		g.fillOval(ballRed.x,ballRed.y, ballRed.width, ballRed.height);
	}

}
