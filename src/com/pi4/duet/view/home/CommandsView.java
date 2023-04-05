package com.pi4.duet.view.home;

import com.pi4.duet.Scale;
import com.pi4.duet.controller.home.CommandsController;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class CommandsView extends JPanel implements KeyListener {

    private static final long serialVersionUID = 8809186306938504775L;
    
    private CommandsController controller;
    private Scale scale;

    private LabelView turnLeft, turnRight, turnLeftDuo, turnRightDuo, moveLeft, moveRight, moveLeftDuo, moveRightDuo, pause, fallObs;
    private TouchButtonView[] allJButton;
    private JButton back;
    private LabelView differentButton;
    
    public CommandsView(SettingsView sv, Scale scale, CommandsController controller) {
    	this.scale = scale;
    	this.controller = controller;
    	
        this.addKeyListener(this);
        
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        this.setLayout(new GridLayout(1, 3));
        this.add(new JPanel());
        JPanel milieu = new JPanel(new GridLayout(11, 2));
        milieu.setBackground(Color.black);

        ImageIcon backIcon = new ImageIcon(this.getClass().getResource("/resources/img/back.png"));
        back = new JButton(backIcon);
        back.setIcon(backIcon);
        back.setBackground(Color.BLACK);
        back.addActionListener(e -> {
            this.setVisible(false);
            sv.setVisible(true);
        });
        milieu.add(back);

        differentButton = new LabelView("", 20);
        milieu.add(differentButton);

        turnLeft = new LabelView("Tourner à gauche");
        turnRight = new LabelView("Tourner à droite");
        moveLeft = new LabelView("Se déplacer à gauche");
        moveRight = new LabelView("Se déplacer à droite");
        pause = new LabelView("Pause");
        fallObs = new LabelView("Accélération de la desc.");

        turnLeftDuo = new LabelView("Tourner à gauche (1v1)");
        turnRightDuo = new LabelView("Tourner à droite (1v1)");
        moveLeftDuo = new LabelView("Se déplacer à gauche (1v1)");
        moveRightDuo = new LabelView("Se déplacer à droite (1v1)");

        allJButton = new TouchButtonView[10];

        allJButton[0] = new TouchButtonView(KeyEvent.getKeyText(controller.getTurnLeft()));
        allJButton[1] = new TouchButtonView(KeyEvent.getKeyText(controller.getTurnRight()));
        allJButton[2] = new TouchButtonView(KeyEvent.getKeyText(controller.getMoveLeft()));
        allJButton[3] = new TouchButtonView(KeyEvent.getKeyText(controller.getMoveRight()));
        allJButton[4] = new TouchButtonView(KeyEvent.getKeyText(controller.getPause()));
        allJButton[5] = new TouchButtonView(KeyEvent.getKeyText(controller.getFallObs()));
        allJButton[6] = new TouchButtonView(KeyEvent.getKeyText(controller.getTurnLeftDuo()));
        allJButton[7] = new TouchButtonView(KeyEvent.getKeyText(controller.getTurnRightDuo()));
        allJButton[8] = new TouchButtonView(KeyEvent.getKeyText(controller.getMoveLeftDuo()));
        allJButton[9] = new TouchButtonView(KeyEvent.getKeyText(controller.getMoveRightDuo()));
        
        milieu.add(turnLeft);
        milieu.add(allJButton[0]);

        milieu.add(turnRight);
        milieu.add(allJButton[1]);
        
        milieu.add(turnLeftDuo);
        milieu.add(allJButton[6]);
        
        milieu.add(turnRightDuo);
        milieu.add(allJButton[7]);

        milieu.add(moveLeft);
        milieu.add(allJButton[2]);

        milieu.add(moveRight);
        milieu.add(allJButton[3]);
        
        milieu.add(moveLeftDuo);
        milieu.add(allJButton[8]);

        milieu.add(moveRightDuo);
        milieu.add(allJButton[9]);
        
        milieu.add(pause);
        milieu.add(allJButton[4]);
        
        milieu.add(fallObs);
        milieu.add(allJButton[5]);
        
        this.add(milieu);
        this.add(new JPanel());
    } 
        
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        for (int i = 0; i < allJButton.length; i++) {
            if (allJButton[i] != null) {            	
                if(allJButton[i].isPressed){
                    int keyCode = keyEvent.getKeyCode();
                    allJButton[i].setText(KeyEvent.getKeyText(keyCode));
                    allJButton[i].setFont(new Font("Arial", Font.BOLD, (int) (45 * scale.getScaleY())));
                    allJButton[i].setForeground(Color.GREEN);
                    allJButton[i].isPressed = false;
                    controller.updateCommands(i, keyCode);
                }
            }
        }
    }
    
    @Override
    public void keyReleased(KeyEvent keyEvent) {
    	if (controller.areIdenticalCommands()) {
    		differentButton.setText("Il y a des touches identiques");
            differentButton.setForeground(Color.red);
            back.setEnabled(false);
    	}
    	else {
    		differentButton.setText("");
    		controller.save();
    		back.setEnabled(true);
    	}
    }
    
    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }
    
    private class LabelView extends JLabel {    	    	
    	/**
		 * 
		 */
		private static final long serialVersionUID = 9127636860113180127L;
		
		LabelView(String text, int sizeFont) {
    		super(text);
    		this.setFont(new Font("Arial", Font.BOLD, (int) (sizeFont * scale.getScaleY())));
            this.setForeground(Color.LIGHT_GRAY);
            this.setHorizontalAlignment(SwingConstants.CENTER);
    	}
		
		LabelView(String text) {
			this(text, 25);
    	}    	
    }
    
    private class TouchButtonView extends JButton {
    	    	
    	/**
		 * 
		 */
		private static final long serialVersionUID = -1192065793451561191L;

		boolean isPressed = false;
		
		TouchButtonView(String text) {
    		super(text);
    		this.setBackground(Color.BLACK);
    		this.setForeground(Color.GREEN);
    		this.setPreferredSize(this.getSize());
    		this.setFont(new Font("Arial", Font.BOLD, (int) (45 * scale.getScaleY())));
    		this.addActionListener(e -> {
    			this.setText("Appuyez sur une touche");
    			this.setFont(new Font("Arial", Font.BOLD, (int) (25 * scale.getScaleY())));
    			this.setForeground(Color.BLUE);
    			CommandsView.this.requestFocusInWindow();
    			this.isPressed = true;
    		});
    	}
    }
}
