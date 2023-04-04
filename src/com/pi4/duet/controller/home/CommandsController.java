package com.pi4.duet.controller.home;

import com.pi4.duet.view.home.CommandsView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.io.File;


public class CommandsController implements KeyListener, Serializable {
    private static final long serialVersionUID = 8809186306938504775L;
    private CommandsView cv;
    public static int[]keyButton={37,39,17,16,32,40};
    public CommandsController(CommandsView cv){
        this.cv=cv;
        cv.addKeyListener(this);
        File keyButtonTab=new File("data/buttonTab.ser");
        if(!keyButtonTab.exists()){
            try {
                //Création du fichier .ser si le fichier n'existe pas
                ObjectOutputStream tabOutput = new ObjectOutputStream(new FileOutputStream("data/buttonTab.ser"));
                tabOutput.writeObject(keyButton);
                tabOutput.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                //Ouverture du fichier .ser pour recup le tableau des keyButton
                ObjectInputStream tab=new ObjectInputStream(new FileInputStream("data/buttonTab.ser"));
                keyButton=(int[]) tab.readObject();
                tab.close();
            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        for(int i=0;i<keyButton.length;i++){
            cv.getAllJButton()[i].setText(KeyEvent.getKeyText(keyButton[i]));
        }
        for(int i=0;i<cv.getAllJButton().length-1;i++){
            for(int j=i+1;j<cv.getAllJButton().length;j++){
                if(cv.getAllJButton()[i]!=null&&cv.getAllJButton()[j]!=null){
                    if(keyButton[i]==keyButton[j]){
                        cv.getDifferentButton().setText("Il y a des touches identiques");
                        cv.getDifferentButton().setForeground(Color.red);
                        return;
                    }else{
                        cv.getDifferentButton().setText("");
                    }
                }
            }
        }
    }

    public CommandsView getCv() {
        return cv;
    }

    public void setCv(CommandsView cv) {
        this.cv = cv;
    }
    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        for(int i=0;i<cv.getAllJButton().length;++i){
            if(cv.getAllJButton()[i]!=null){
                if(cv.getAllJButton()[i].getText().equals("Veuillez chosir une touche")){
                    int keyCode=keyEvent.getKeyCode();
                    cv.getAllJButton()[i].setText(KeyEvent.getKeyText(keyCode));
                    cv.getAllJButton()[i].setFont(new Font("Arial",Font.BOLD,30));
                    keyButton[i]=keyCode;
                    cv.getAllJButton()[i].setForeground(Color.GREEN);
                    try {
                        //Mise à jour du tableau enregistré dans le fichier .ser
                        ObjectOutputStream tabOutput = new ObjectOutputStream(new FileOutputStream("data/buttonTab.ser"));
                        tabOutput.writeObject(keyButton);
                        tabOutput.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        for(int i=0;i<cv.getAllJButton().length-1;i++){
            for(int j=i+1;j<cv.getAllJButton().length;j++){
                if(cv.getAllJButton()[i]!=null&&cv.getAllJButton()[j]!=null){
                    if(keyButton[i]==keyButton[j]){
                        cv.getDifferentButton().setText("Il y a des touches identiques");
                        cv.getDifferentButton().setForeground(Color.red);
                        return;
                    }else{
                        cv.getDifferentButton().setText("");
                    }
                }
            }
        }
    }


}
