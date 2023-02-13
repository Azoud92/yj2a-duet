package com.pi4.duet.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class PatternData extends HashMap<Long, Obstacle> {

	private static final long serialVersionUID = -8250741712843302226L;
	
	public void write(String path) throws IOException {	//Attrapez l'exception dans l'interface graphique pour faire un message d'erreur
		File destination = new File(path);
		if (!destination.exists()) destination.createNewFile();	//S'il y a une IOException, une erreur s'est produite lors de la communication
		FileOutputStream output = new FileOutputStream(destination);
		ObjectOutputStream objectStream = new ObjectOutputStream(output);
		objectStream.writeObject(this);
		objectStream.close();
	}
	
	public static PatternData read(String path) throws IOException, ClassNotFoundException {	//Pareil ici
		FileInputStream fileInput = new FileInputStream(path);
		ObjectInputStream inputStream = new ObjectInputStream(fileInput);
		PatternData res = (PatternData) inputStream.readObject();	//Cette instruction peut lever plusieurs exception, voir la documentation java
		inputStream.close();
		return res;
	}

}
