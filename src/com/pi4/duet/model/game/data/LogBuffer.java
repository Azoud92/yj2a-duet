package com.pi4.duet.model.game.data;

import java.io.*;

public class LogBuffer {
	
	/*
	 * Usage : Le LogBuffer est une classe permettant d'écrire des résultats dans un fichier texte, avec n'importe-quelles chaines de caractères
	 * vers un fichier texte, avec quelques options de mise en forme. En tant que buffer, c'est une interface de relativement bas niveau, nécessitant
	 * un peu de babysitting.
	 * Il faut d'abord importer LogBuffer dans la classe souhaitée et en mettre un en tant qu'attribut de cette classe, déjà construit.
	 * Le fichier associé est crée à l'appel du constructeur
	 * Dans un premier temps, LogBuffer accumule un chaine de caractères, concaténation de toutes les opérations append.
	 * Quand le buffer est prêt, on peut écrire content vers le fichier donné à la construction avec write. 
	 * CETTE OPERATION NE RESET PAS CONTENT
	 * Pour cela, on appelle flush
	 */
	
	private String content = "";
	public final String writePath;
	public final String fileName;
	
	public LogBuffer(String path, String name) {
		writePath = path;
		fileName = name;
		write();
	}
	
	public void append(String s) {
		content += s;
	}
	
	public void appendLn(String s) {
		content += s + "\n";
	}
	
	public void appendSeparator(char c, int len) {
		String res = "";
		for (int i = 0 ; i < len ; i++) res += c;
		appendLn(res);
	}
	
	public void appendTitle(String title, char c, int len) {
		String res = "";
		for (int i = 0 ; i < len ; i++) res += c;
		res += title;
		for (int i = 0 ; i < len ; i++) res += c;
		appendLn(res);
	}
	
	public void flush() {
		content = "";
	}
	
	public void write() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(writePath + "duet_log_" + fileName + ".txt", true));
			writer.append(content);
			writer.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		LogBuffer log = new LogBuffer("", "test");
		log.appendTitle("Test log", '#', 10);
		log.appendLn("Hello world");
		log.appendSeparator('-', 20);
		log.append("Programmed to work and not to feel");
		log.write();
	}
	
}
