package com.pi4.duet.model.game.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import com.pi4.duet.Point;
import com.pi4.duet.model.game.Direction;
import com.pi4.duet.model.game.Obstacle;

public class PatternData extends LinkedHashMap<Obstacle, Long> {

	private static final long serialVersionUID = -8250741712843302226L;

	public void write(String path) throws IOException {	// Attrapez l'exception dans l'interface graphique pour faire un message d'erreur
		File destination = new File(path);
		if (!destination.exists()) destination.createNewFile();	// S'il y a une IOException, une erreur s'est produite lors de la communication
		FileOutputStream output = new FileOutputStream(destination);
		ObjectOutputStream objectStream = new ObjectOutputStream(output);
		objectStream.writeObject(this);
		objectStream.close();
	}

	public static PatternData read(URL url) throws IOException, ClassNotFoundException { // Pareil ici
		InputStream fileInput = url.openStream();
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

	private static PatternData parse(String input) {
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
				Direction dir = Direction.valueOf(lineSc.next());

				//Conversion des points (Un cast d'arrays ne marche pas)
				Object[] uncastedPoints = points.toArray();
				Point[] castedPoints = new Point[uncastedPoints.length];
				for (int i = 0 ; i < castedPoints.length ; i++) {
					castedPoints[i] = (Point) uncastedPoints[i];
				}

				//Placement du résultat
				res.put(new Obstacle(castedPoints, centre, velocity, rotationSpeed, angle, dir, null), key);
			} catch (Exception e) {
				//Arrêter de parser si l'on rencontre une ligne non conforme
				break;
			}
		}
		globalSc.close();

		return res;
	}
	
	@Override
	public String toString() {
		String res = "";
		int i = 0;
		for (Obstacle o : keySet()) {
			res += get(o) + ":" + o;
			if (i < keySet().size() -1) res += "\n";
			i++;
		}
		return res;
	}

	public String toHTMLString() {
		String res = "";
		int i = 0;
		for (Obstacle o : keySet()) {
			res += get(o) + ":" + o;
			if (i < keySet().size() -1) res += "<br>";
			i++;
		}
		return res;
	}

	public static PatternData read(String path) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		FileInputStream fileInput = new FileInputStream(path);
		ObjectInputStream inputStream = new ObjectInputStream(fileInput);
		PatternData res = (PatternData) inputStream.readObject(); // Cette instruction peut lever plusieurs exceptions, voir la documentation java
		inputStream.close();
		return res;
	}

}