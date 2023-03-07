package com.pi4.duet.view;

import javax.swing.*;

public class BooleanButton extends JButton {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2171414454103860872L;
	
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
