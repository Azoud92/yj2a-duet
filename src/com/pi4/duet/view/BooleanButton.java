package com.pi4.duet.view;

import javax.swing.*;

public class BooleanButton extends JButton {
    private boolean button =false;
    public BooleanButton(String text){
        super(text);
    }

    public boolean isButton() {
        return button;
    }

    public void setButton(boolean button) {
        this.button = button;
    }
}
