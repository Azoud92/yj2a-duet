package com.pi4.duet.model.game.data;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

import com.pi4.duet.Point;
import com.pi4.duet.model.game.Obstacle;

import java.util.ArrayList;

public class PatternData extends HashMap<Long, Obstacle> {

	private static final long serialVersionUID = -8250741712843302226L;
	
	public void write(String path) throws IOException {	// Attrapez l'exception dans l'interface graphique pour faire un message d'erreur
		File destination = new File(path);
		if (!destination.exists()) destination.createNewFile();	// S'il y a une IOException, une erreur s'est produite lors de la communication
		FileOutputStream output = new FileOutputStream(destination);
		ObjectOutputStream objectStream = new ObjectOutputStream(output);
		objectStream.writeObject(this);
		objectStream.close();
	}
	
	public void writeTxt(String path) throws IOException {
		File destination = new File(path);
		if (!destination.exists()) destination.createNewFile();
		destination.setWritable(true);
		FileWriter destwriter = new FileWriter(path);
		BufferedWriter buffer = new BufferedWriter(destwriter);
		buffer.write(toString());
		buffer.close();
	}
	
	public static PatternData read(String path) throws IOException, ClassNotFoundException { // Pareil ici
		FileInputStream fileInput = new FileInputStream(path);
		ObjectInputStream inputStream = new ObjectInputStream(fileInput);
		PatternData res = (PatternData) inputStream.readObject(); // Cette instruction peut lever plusieurs exceptions, voir la documentation java
		inputStream.close();
		return res;
	}
	
	public static PatternData readTxt(String path) throws IOException, ClassNotFoundException {
		File fileInput = new File(path);
		FileReader reader = new FileReader(fileInput);
		BufferedReader buffer = new BufferedReader(reader);
		
		String txt = "";
		String line = "";
		while (line != null) {
			line = buffer.readLine();
			txt += line + "\n";
		}
		buffer.close();
		return parse(txt);
	}
	
	public static PatternData parse(String input) {
		PatternData res = new PatternData();
		Scanner globalSc = new Scanner(input);
		globalSc.useDelimiter("\n");
		/*
		 * Les lignes contiennent les données de cette façon ;
		 * 1000:[(3,2) (2,5) (-4,9)];(7,4);1.4;.8;3
		 * delay:[Points(x1,y1) (x2,y2) (x3,y3)];center(x,y);velocity;rotationSpeed;angle
		 * Selon le constructeur polygonal de Obstacle
		 */
		while (globalSc.hasNext()) {
			//initialisation
			Scanner lineSc = new Scanner(globalSc.next());
			lineSc.useDelimiter(":|;");	
			
			try {
				//Récupération du délai
				long key = lineSc.nextLong();
			
				//Récupération des points
				Scanner arraySc = new Scanner(lineSc.next());
				arraySc.useDelimiter(" ");
				ArrayList<Point> points = new ArrayList<>();
				while (arraySc.hasNext()) {
					Scanner pointSc = new Scanner(arraySc.next());
					pointSc.useDelimiter("\\[\\(|\\)\\]|\\(|\\)|,| ");
					String d1 = pointSc.next();
					String d2 = pointSc.next();
					points.add(new Point(Double.parseDouble(d1), Double.parseDouble(d2)));
				}
				
				//Récupération du centre de rotation
				Scanner centreSc = new Scanner(lineSc.next());
				centreSc.useDelimiter("\\(|,|\\)");
				String x = centreSc.next();
				String y = centreSc.next();
				Point centre = new Point(Double.parseDouble(x), Double.parseDouble(y));
				
				//Récupération des données de mouvement
				double velocity = Double.parseDouble(lineSc.next());
				double rotationSpeed = Double.parseDouble(lineSc.next());
				double angle = Double.parseDouble(lineSc.next());
				
				//Conversion des points (Un cast d'arrays ne marche pas)
				Object[] uncastedPoints = points.toArray();
				Point[] castedPoints = new Point[uncastedPoints.length];
				for (int i = 0 ; i < castedPoints.length ; i++) {
					castedPoints[i] = (Point) uncastedPoints[i];
				}
				
				//Placement du résultat
				res.put(key, new Obstacle(castedPoints, centre, velocity, rotationSpeed, angle, null));
			} catch (Exception e) {
				//Arrêter de parser si l'on rencontre une ligne non conforme
				break;
			}
		}
		globalSc.close();
		
		return res;
	}
	
	public String toString() {
		String res = "";
		int i = 0;
		for (Long l : keySet()) {
			res += l + ":" + get(l);
			if (i < keySet().size() -1) res += "\n";
			i++;
		}
		return res;
	}
	
	public String toHTMLString() {
		String res = "";
		int i = 0;
		for (Long l : keySet()) {
			res += l + ":" + get(l);
			if (i < keySet().size() -1) res += "<br>";
			i++;
		}
		return res;
	}
	
	public static void main(String[] args) {
		/*
		PatternData d = parse("1000:[(3,2) (2,5) (-3,9.2)];(.4,8);1.4;.8;0\n" +
							  "2000:[(.8,-.902) (3,-7)];(1,0);0;9;9");
		System.out.println("Original : \n" + d);
		
		try {
			d.writeTxt("test.txt");
			d.write("test.ser");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		*/
		try {
			PatternData d2 = readTxt("test2.txt");
			//System.out.println("Lecture texte : \n" + d2);
			d2 = read("test2.ser");
			System.out.println("Lecture serie : \n" + d2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
