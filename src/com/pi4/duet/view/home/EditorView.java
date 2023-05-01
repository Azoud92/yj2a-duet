package com.pi4.duet.view.home;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import com.pi4.duet.Point;
import com.pi4.duet.model.game.Direction;
import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.model.game.data.PatternData;
import com.pi4.duet.model.home.EditorModel;
import com.pi4.duet.controller.home.EditorController;


public class EditorView extends JPanel {
	
	private static final long serialVersionUID = 416515678625343978L;
	
	private final Font labelFont = new Font("Arial", Font.BOLD, 14);
	private HomePageView hpv = null;
	
	private class PointSelector extends JPanel {

		private static final long serialVersionUID = -2235902615262010858L;
		
		private JSpinner xSpinner = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 1100.0, 1.0));
		private JSpinner ySpinner = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 1100.0, 1.0));
		
		public PointSelector() {
			setLayout(new GridLayout(1, 4));
			
			JLabel xLabel = new JLabel("X :");
			xLabel.setFont(labelFont);
			xLabel.setForeground(Color.WHITE);
			add(xLabel);
			xSpinner.getEditor().getComponent(0).setBackground(Color.BLACK);
			xSpinner.getEditor().getComponent(0).setForeground(Color.WHITE);
			add(xSpinner);
			JLabel yLabel = new JLabel("Y :");
			yLabel.setFont(labelFont);
			yLabel.setForeground(Color.WHITE);
			add(yLabel);
			ySpinner.getEditor().getComponent(0).setBackground(Color.BLACK);
			ySpinner.getEditor().getComponent(0).setForeground(Color.WHITE);
			add(ySpinner);
			setOpaque(false);
		}
		
		public Point getSelection() { return new Point((double) xSpinner.getValue(), (double) ySpinner.getValue());}
		
		public void setSelection(double x, double y) { xSpinner.setValue(x); ySpinner.setValue(y); }
		
	}
	
	private EditorController controller;
	
	private PointSelector[] pointSelect = new PointSelector[4];
	private PointSelector centerSelect = new PointSelector();
	private JSpinner dirSelect = new JSpinner(new SpinnerListModel(Direction.values()));
	private JSpinner speedSelect = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 1100.0, 1.0));
	private JSpinner rotationSpeedSelect = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 1100.0, 1.0));
	private JSpinner angleSelect = new JSpinner(new SpinnerNumberModel(0.0, -180.0, 180.0, 1.0)); 	//Sera à convertir en radians
	private JSpinner delaySelect = new JSpinner(new SpinnerNumberModel(0l, 0l, 180000l, 1000l));//En ms
	
	private JButton mainMenuButton = new JButton("Menu principal");
	private JButton saveButton = new JButton("Sauvegarder");
	
	private JButton previousObstacle = new JButton("X");
	private JButton currentObstacle = new JButton("Obstacle 1");
	private JButton nextObstacle = new JButton("+");
	private JButton deleteObstacle = new JButton("Supprimer");
	
	private JPanel pointGrid = new JPanel(new GridLayout(2,2));
	private JPanel obstacleSelect = new JPanel(new GridBagLayout());
	private JPanel menuButtons = new JPanel(new BorderLayout());
	
	
	private final Runnable addNewObstacle = new Runnable() {
		public void run() {
			Point[] points = new Point[4];
			for (int i = 0 ; i < 4 ; i++) points[i] = new Point(0, 0);
			
			controller.transferData(new Obstacle(points, new Point(0, 0), 0, 0, 0, Direction.BOTTOM, null), 0l);
			controller.incrementSelection(1);
			update();
		}
	};
	
	private final Runnable deleteCurrentObstacle = new Runnable() {
		public void run() {
			controller.deleteSelection();
			if (controller.getSelection() > 0) controller.incrementSelection(-1);
			update();
		}
	};
	
	private final Runnable writeCurrentObstacle = new Runnable() {
		public void run() {
			Point[] points = new Point[4];
			for (int i = 0 ; i < 4 ; i++) points[i] = pointSelect[i].getSelection();
			Point center = centerSelect.getSelection();
			
			Direction dir = (Direction) dirSelect.getValue();
			double velocity = (double) speedSelect.getValue();
			double rotationSpeed = (double) rotationSpeedSelect.getValue();
			double angle = (double) angleSelect.getValue();
			
			//Je sais que ce code pue, mais c'est la solution la plus consistante que j'aie trouvé
			long delay = 0l;
			try {
				delay = (long) delaySelect.getValue();
			} catch (ClassCastException cce) {
				delay = (long) (double) delaySelect.getValue();
			}
			
			Obstacle res = new Obstacle(points, center, velocity, rotationSpeed, angle, dir, null);
			
			controller.writeSelection(res, delay);
		}
	};
	
	private final Runnable selectNextObstacle = new Runnable() {
		public void run() {
			controller.incrementSelection(1);
			update();
		}
	};
	
	private final Runnable selectPreviousObstacle = new Runnable() {
		public void run() {
			controller.incrementSelection(-1);
			update();
		}
	};
	
	private final Runnable sendSaveRequest = new Runnable() {
		public void run() {
			try {
				PatternData data = controller.transmitCompileRequest();
				String[] options = {"Sauvegarder", "Annuler"};
				int answ = JOptionPane.showOptionDialog(new JFrame(), "Sauvegarder le niveau suivant ?\n" + data.toString(),
						"Sauvegarde", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				switch (answ) {
				case 0 :
					String path = selectPath();
					if (path != null) controller.transmitSaveRequest(path); 
					break;
				case 1 : break;
				}
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(new JFrame(), ioe.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}
	};
	
	private final Runnable back = () -> {
		this.setVisible(false);
		hpv.setVisible(true);
	};

	private Image background = new ImageIcon(this.getClass().getResource("/resources/img/background.png")).getImage();;
	
	public EditorView(EditorController cont, HomePageView home) {
		controller = cont;
		hpv = home;
		
		for (int i = 0 ; i < 4 ; i++) {
			pointSelect[i] = new PointSelector();
			pointGrid.add(pointSelect[i]);
		}
		
		previousObstacle.addActionListener((ev) -> {selectPreviousObstacle.run();});
		currentObstacle.addActionListener((ev) -> {writeCurrentObstacle.run();});
		nextObstacle.addActionListener((ev) -> {addNewObstacle.run();});
		deleteObstacle.addActionListener((ev) -> {deleteCurrentObstacle.run();});
		saveButton.addActionListener((ev) -> {sendSaveRequest.run();});
		mainMenuButton.addActionListener((ev) -> {back.run();});
		
		previousObstacle.setEnabled(false);
		deleteObstacle.setEnabled(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = .1;
		obstacleSelect.add(previousObstacle, c);
		
		c.gridx = 1;
		c.weightx = .8;
		obstacleSelect.add(currentObstacle, c);
		
		c.gridx = 2;
		c.weightx = .1;
		obstacleSelect.add(nextObstacle, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.gridwidth = 3;
		obstacleSelect.add(deleteObstacle, c);
		
		menuButtons.add(mainMenuButton, BorderLayout.WEST);
		menuButtons.add(saveButton, BorderLayout.EAST);
		menuButtons.setOpaque(false);
		obstacleSelect.setOpaque(false);
		setLayout(new GridBagLayout());
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(50, 10, 50, 10);
		add(menuButtons, c);
		
		c.gridy = 1;
		c.fill = GridBagConstraints.NONE;
		pointGrid.setOpaque(false);
		add(pointGrid, c);
		
		JPanel paramPanel = new JPanel(new GridLayout(0, 2));
		
		JLabel centerLabel = new JLabel("Centre de rotation :");
		centerLabel.setFont(labelFont);
		centerLabel.setForeground(Color.WHITE);
		paramPanel.add(centerLabel);
		paramPanel.add(centerSelect);
		
		JLabel dirLabel = new JLabel("Direction :");
		dirLabel.setFont(labelFont);
		dirLabel.setForeground(Color.WHITE);
		paramPanel.add(dirLabel);
		dirSelect.getEditor().getComponent(0).setBackground(Color.BLACK);
		dirSelect.getEditor().getComponent(0).setForeground(Color.WHITE);
		paramPanel.add(dirSelect);
		
		JLabel speedLabel = new JLabel("Vitesse :");
		speedLabel.setFont(labelFont);
		speedLabel.setForeground(Color.WHITE);
		paramPanel.add(speedLabel);
		speedSelect.getEditor().getComponent(0).setBackground(Color.BLACK);
		speedSelect.getEditor().getComponent(0).setForeground(Color.WHITE);
		paramPanel.add(speedSelect);
		
		JLabel rotationSpeedLabel = new JLabel("Vitesse de rotation :");
		rotationSpeedLabel.setFont(labelFont);
		rotationSpeedLabel.setForeground(Color.WHITE);
		paramPanel.add(rotationSpeedLabel);
		rotationSpeedSelect.getEditor().getComponent(0).setBackground(Color.BLACK);
		rotationSpeedSelect.getEditor().getComponent(0).setForeground(Color.WHITE);
		paramPanel.add(rotationSpeedSelect);
		
		JLabel angleLabel = new JLabel("Angle :");
		angleLabel.setFont(labelFont);
		angleLabel.setForeground(Color.WHITE);
		paramPanel.add(angleLabel);
		angleSelect.getEditor().getComponent(0);
		angleSelect.getEditor().getComponent(0).setBackground(Color.BLACK);
		angleSelect.getEditor().getComponent(0).setForeground(Color.WHITE);
		paramPanel.add(angleSelect);
		
		JLabel delayLabel = new JLabel("Délai (ms) :");
		delayLabel.setFont(labelFont);
		delayLabel.setForeground(Color.WHITE);
		paramPanel.add(delayLabel);
		delaySelect.getEditor().getComponent(0).setBackground(Color.BLACK);
		delaySelect.getEditor().getComponent(0).setForeground(Color.WHITE);
		paramPanel.add(delaySelect);
		
		c.gridy = 2;
		paramPanel.setOpaque(false);
		paramPanel.setForeground(Color.WHITE);
		add(paramPanel, c);
		
		c.gridy = 3;
		c.fill = GridBagConstraints.NONE;
		add(obstacleSelect, c);
		
	}
	
	private static void removeAllActionListeners(JButton b) {
		for (ActionListener a : b.getActionListeners()) b.removeActionListener(a);
	}
	
	private String selectPath() {
		JFileChooser explorer = new JFileChooser();
		if (explorer.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) return explorer.getSelectedFile().getAbsolutePath();
		return null;
	}
	
	private void update() {
		Obstacle currObs = controller.getSelectedObstacle();
		
		for (int i = 0 ; i < 4 ; i++) pointSelect[i].setSelection(currObs.getPoints()[i].getX(), currObs.getPoints()[i].getY());
		
		centerSelect.setSelection(currObs.getCenter().getX(), currObs.getCenter().getY());
		dirSelect.setValue(currObs.getDirection());
		speedSelect.setValue(currObs.getVelocity());
		rotationSpeedSelect.setValue(currObs.getRotationSpeed());
		angleSelect.setValue(currObs.getAngle());		//Conversion en degrés à faire
		delaySelect.setValue(controller.getSelectedDelay());
		
		if (controller.getSelection() == controller.getSize() - 1) {
			nextObstacle.setText("+");
			removeAllActionListeners(nextObstacle);
			nextObstacle.addActionListener((ev) -> {addNewObstacle.run();});
		} else {
			nextObstacle.setText(">");
			removeAllActionListeners(nextObstacle);
			nextObstacle.addActionListener((ev) -> {selectNextObstacle.run();});
		}
		
		if (controller.getSelection() == 0) {
			previousObstacle.setText("X");
			previousObstacle.setEnabled(false);
			deleteObstacle.setEnabled(false);
		} else {
			previousObstacle.setText("<");
			previousObstacle.setEnabled(true);
			deleteObstacle.setEnabled(true);
		}
		
		currentObstacle.setText("Obstacle " + (controller.getSelection()+1));
	}
	
	public void setController(EditorController c) {controller = c;}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	
	

}
