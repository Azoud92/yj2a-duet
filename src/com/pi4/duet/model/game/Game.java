package com.pi4.duet.model.game;

import java.util.ArrayList;

import com.pi4.duet.Point;

// Modèle général abstrait du jeu
public abstract class Game {

	public final int width, height;

	protected Wheel wheel;
	private ArrayList<Obstacle> obstacles = new ArrayList<>();
	private GameState gameState = GameState.READY;
	
	private double effectDelaySpeed = 0.01;
	private double progressionEffect = 0;
	private boolean canUseEffect = false;

	public Game(int width, int height, Point coordsWheel) {
		this.width = width;
		this.height = height;
		this.wheel = new Wheel(coordsWheel);
	}

	public void setVelocityTo01(){
		for(int i = 0;i <obstacles.size(); i++){
			obstacles.get(i).setVelocity(0.1);
		}
	}



	public int indice(){
		int indice=0;
		for(int i=0;i<obstacles.size();i++){
			if(obstacles.get(i).getCenter().getY()<wheel.getCenter().getY()){
				break;
			}else{
				indice++;
			}
		}
		return indice;
	}

	public final void addObstacle(Obstacle o) {
		this.obstacles.add(o);
	}

	public final void removeObstacle(Obstacle o) {
		this.obstacles.remove(o);
	}

	@SuppressWarnings("unchecked")
	public final ArrayList<Obstacle> getObstacles() {
		return (ArrayList<Obstacle>) obstacles.clone();
	}

	public final Wheel getWheel() { return wheel; }

	public final GameState getState() { return gameState; }

	public final void setState(GameState s) {
		this.gameState = s;
	}

	public boolean getCanUseEffect() {
		return canUseEffect;
	}

	public void setCanUseEffect(boolean canUseEffect) {
		this.canUseEffect = canUseEffect;
	}
	
	public double getProgressionEffect() { return progressionEffect; }
	
	public void incrProgressionEffect() {		
		if (this.progressionEffect >= 100) {
			this.canUseEffect = true;
		}
		else this.progressionEffect += effectDelaySpeed;
	}
	
	public void useEffect() {
		this.progressionEffect = 0;
		this.canUseEffect = false;
	}

}
