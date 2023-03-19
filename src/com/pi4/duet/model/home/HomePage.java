package com.pi4.duet.model.home;

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
import java.util.ArrayList;

public class HomePage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6683590500436200055L;

	private ArrayList<Integer> levelsAvailable;
	
	public HomePage() {
		levelsAvailable = new ArrayList<Integer>();
		levelsAvailable.add(1); // le niveau 1 est toujours accompli comme c'est le niveau de départ
	}
	
	public void addLevel(int i) {
		this.levelsAvailable.add(i);
	}
	
	public boolean save() {
		Path dataFolder = Paths.get("data");
		try {
			Files.createDirectories(dataFolder);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		
		File file = new File("data/progression.ser");
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
	
	public static HomePage read() {
		File file = new File("data/progression.ser");
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			HomePage homePage = (HomePage) ois.readObject();
			ois.close();
			return homePage;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return new HomePage();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			return new HomePage();
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Integer> getLevelsAvailable() {
		return (ArrayList<Integer>) levelsAvailable.clone();
	}
	
}
