package com.pi4.duet.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Settings implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3702397720992732515L;
	
	private boolean music = true, effects = true, background = true;
	
	public void setMusic(boolean val) {
		// TODO Auto-generated method stub
		this.music = val;
	}

	public void setEffects(boolean val) {
		// TODO Auto-generated method stub
		this.effects = val;
	}
	
	public void setBackground(boolean val) {
		// TODO Auto-generated method stub
		this.background = val;
	}
	
	public boolean getMusic() {
		return music;
	}
	
	public boolean getEffects() {
		return effects;
	}

	public boolean getBackground() {
		return background;
	}
	
	// Sauvegarde des paramètres
	public boolean save() {
		Path dataFolder = Paths.get("data");
		try {
			Files.createDirectories(dataFolder);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		
		File file = new File("data/settings.ser");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(this);
			oos.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public static Settings read() {
		File file = new File("data/settings.ser");
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Settings settings = (Settings) ois.readObject();
			ois.close();
			return settings;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return new Settings();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			return new Settings();
		}
	}
	
	
}
