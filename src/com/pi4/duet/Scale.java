package com.pi4.duet;

public class Scale {

	// Classe servant à représenter un facteur d'échelle pour l'affichage, afin que le jeu s'adapte à toutes les résolutions d'écran

	private double scaleX, scaleY, scaleXY;
	// On stocke le facteur horizontal, vertical, et diamétral

	public Scale(double width, double height) {
		this.scaleX = width / 1920;
		this.scaleY = height / 1080;
		this.scaleXY = scaleX * scaleY;
	}

	public double getScaleX() { return scaleX; }
	public double getScaleY() { return scaleY; }
	public double getScaleXY() { return scaleXY; }

}
