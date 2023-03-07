package com.pi4.duet;

import java.awt.Image;
import javax.swing.ImageIcon;

public final class Auxiliaire {
		
	public static ImageIcon resizeImage(ImageIcon i, int width, int height) {
		return new ImageIcon(i.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
	}
	
}
