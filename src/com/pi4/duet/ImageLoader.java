package com.pi4.duet;

import java.awt.Image;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

// Permet de charger une image sans bloquer le thread courant
public class ImageLoader extends Thread {

	private Image image;
	private int width, height;
    private ExecutorService executor;
    
    public ImageLoader(String imgPath, int width, int height) {
    	this.width = width;
    	this.height = height;
        executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> image = loadImage(imgPath));
    }
    
    private Image loadImage(String path) {
        Image image = null;
        try {
            image = ImageIO.read(this.getClass().getResource("/resources/img/" + path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    
    public Image getImage() {
        return image;
    	//return Auxiliaire.resizeImage(new ImageIcon(image), width, height).getImage();
    }
	
}
