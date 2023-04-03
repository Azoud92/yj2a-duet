package com.pi4.duet.view.home;

import com.pi4.duet.Auxiliaire;
import com.pi4.duet.Scale;
import com.pi4.duet.controller.home.CommandsController;
import com.pi4.duet.controller.home.SettingsController;
import com.pi4.duet.view.game.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CommandsView extends JPanel implements KeyListener {
    private static final long serialVersionUID = 8809186306938504775L;
    private Dimension size;
    private CommandsController controller;
    private GameWindow window;
    private SettingsView sv;

    private JLabel turnLeft,turnRight,moveLeft,moveRight,pause,fallObs;
    private JButton[] allJButton;
    private JButton back;
    private JLabel differentButton;
    public static int[]keyButton={37,39,17,16,32,40};
    // Dans l'ordre : bouton pour
    // {tourner sens horaire,toucher sens anti-horaire,volant va à gauche,volant va à droite,pause,faire tomber l'obstacle}
    public CommandsView(SettingsView sv){
        this.addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        this.setLayout(new GridLayout(1,3));
        this.add(new JPanel());
        JPanel milieu=new JPanel(new GridLayout(7,2));
        milieu.setBackground(Color.black);

        ImageIcon backIcon=new ImageIcon(this.getClass().getResource("/resources/img/back.png"));
        back=new JButton(backIcon);
        back.setIcon(backIcon);
        back.setBackground(Color.BLACK);
        back.addActionListener(e->{
            this.setVisible(false);
            sv.setVisible(true);
        });
        milieu.add(back);

        differentButton = new JLabel("");
        differentButton.setFont(new Font("Arial",Font.BOLD,15));
        differentButton.setForeground(Color.LIGHT_GRAY);
        differentButton.setHorizontalAlignment(SwingConstants.CENTER);
        milieu.add(differentButton);

        turnLeft=new JLabel("Turn left");
        turnLeft.setFont(new Font("Arial",Font.BOLD,30));
        turnLeft.setForeground(Color.LIGHT_GRAY);
        turnLeft.setHorizontalAlignment(SwingConstants.CENTER);

        turnRight=new JLabel("Turn right");
        turnRight.setFont(new Font("Arial",Font.BOLD,30));
        turnRight.setForeground(Color.LIGHT_GRAY);
        turnRight.setHorizontalAlignment(SwingConstants.CENTER);

        moveLeft=new JLabel("Move left");
        moveLeft.setFont(new Font("Arial",Font.BOLD,30));
        moveLeft.setForeground(Color.LIGHT_GRAY);
        moveLeft.setHorizontalAlignment(SwingConstants.CENTER);

        moveRight=new JLabel("Move right");
        moveRight.setFont(new Font("Arial",Font.BOLD,30));
        moveRight.setForeground(Color.LIGHT_GRAY);
        moveRight.setHorizontalAlignment(SwingConstants.CENTER);

        pause=new JLabel("Pause");
        pause.setFont(new Font("Arial",Font.BOLD,30));
        pause.setForeground(Color.LIGHT_GRAY);
        pause.setHorizontalAlignment(SwingConstants.CENTER);

        fallObs=new JLabel("Tomber l'obstacle");
        fallObs.setFont(new Font("Arial",Font.BOLD,30));
        fallObs.setForeground(Color.LIGHT_GRAY);
        fallObs.setHorizontalAlignment(SwingConstants.CENTER);

        allJButton=new JButton[6];

        allJButton[0]=new JButton("Gauche");
        allJButton[0].setBackground(Color.BLACK);
        allJButton[0].setForeground(Color.GREEN);
        allJButton[0].setPreferredSize(allJButton[0].getSize());
        allJButton[0].setFont(new Font("Arial",Font.BOLD,30));
        allJButton[0].addActionListener(e->{
            allJButton[0].setText("Veuillez chosir une touche");
            allJButton[0].setFont(new Font("Arial",Font.BOLD,15));
            allJButton[0].setForeground(Color.blue);
            requestFocusInWindow();
        });


        allJButton[1]=new JButton("Droite");
        allJButton[1].setBackground(Color.BLACK);
        allJButton[1].setForeground(Color.GREEN);
        allJButton[1].setPreferredSize(allJButton[1].getSize());
        allJButton[1].setFont(new Font("Arial",Font.BOLD,30));
        allJButton[1].addActionListener(e->{
            allJButton[1].setText("Veuillez chosir une touche");
            allJButton[1].setFont(new Font("Arial",Font.BOLD,15));
            allJButton[1].setForeground(Color.blue);
            requestFocusInWindow();
        });

        allJButton[2]=new JButton("Ctrl");
        allJButton[2].setBackground(Color.BLACK);
        allJButton[2].setForeground(Color.GREEN);
        allJButton[2].setPreferredSize(allJButton[2].getSize());
        allJButton[2].setFont(new Font("Arial",Font.BOLD,30));
        allJButton[2].addActionListener(e->{
            allJButton[2].setText("Veuillez chosir une touche");
            allJButton[2].setFont(new Font("Arial",Font.BOLD,15));
            allJButton[2].setForeground(Color.blue);
            requestFocusInWindow();
        });

        allJButton[3]=new JButton("Maj");
        allJButton[3].setBackground(Color.BLACK);
        allJButton[3].setForeground(Color.GREEN);
        allJButton[3].setPreferredSize(allJButton[3].getSize());
        allJButton[3].setFont(new Font("Arial",Font.BOLD,30));
        allJButton[3].addActionListener(e->{
            allJButton[3].setText("Veuillez chosir une touche");
            allJButton[3].setFont(new Font("Arial",Font.BOLD,15));
            allJButton[3].setForeground(Color.blue);
            requestFocusInWindow();
        });

        allJButton[4]=new JButton("Espace");
        allJButton[4].setBackground(Color.BLACK);
        allJButton[4].setForeground(Color.GREEN);
        allJButton[4].setPreferredSize(allJButton[4].getSize());
        allJButton[4].setFont(new Font("Arial",Font.BOLD,30));
        allJButton[4].addActionListener(e->{
            allJButton[4].setText("Veuillez chosir une touche");
            allJButton[4].setFont(new Font("Arial",Font.BOLD,15));
            allJButton[4].setForeground(Color.blue);
            requestFocusInWindow();
        });

        allJButton[5]=new JButton("Bas");
        allJButton[5].setBackground(Color.BLACK);
        allJButton[5].setForeground(Color.GREEN);
        allJButton[5].setPreferredSize(allJButton[5].getSize());
        allJButton[5].setFont(new Font("Arial",Font.BOLD,30));
        allJButton[5].addActionListener(e->{
            allJButton[5].setText("Veuillez chosir une touche");
            allJButton[5].setFont(new Font("Arial",Font.BOLD,15));
            allJButton[5].setForeground(Color.blue);
            requestFocusInWindow();
        });
        milieu.add(turnLeft);
        milieu.add(allJButton[0]);

        milieu.add(turnRight);
        milieu.add(allJButton[1]);

        milieu.add(moveLeft);
        milieu.add(allJButton[2]);

        milieu.add(moveRight);
        milieu.add(allJButton[3]);
        milieu.add(pause);
        milieu.add(allJButton[4]);
        milieu.add(fallObs);
        milieu.add(allJButton[5]);
        this.add(milieu);
        this.add(new JPanel());


    }


    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        for(int i=0;i<allJButton.length;++i){
            if(allJButton[i]!=null){
                if(allJButton[i].getText().equals("Veuillez chosir une touche")){
                    int keyCode=keyEvent.getKeyCode();
                    allJButton[i].setText(KeyEvent.getKeyText(keyCode));
                    allJButton[i].setFont(new Font("Arial",Font.BOLD,30));
                    keyButton[i]=keyCode;
                    allJButton[i].setForeground(Color.GREEN);
                    System.out.println(keyButton[i]);
                }
            }

        }

    }
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        for(int i=0;i<keyButton.length-1;i++){
            for(int j=i+1;j<keyButton.length;j++){
                if(allJButton[i]!=null&&allJButton[j]!=null){
                    if(keyButton[i]==keyButton[j]){
                        differentButton.setText("Il y a des touches identiques");
                        differentButton.setForeground(Color.red);
                        return;
                    }else{
                        differentButton.setText("");
                    }
                }
            }
        }
    }
}
