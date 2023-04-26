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
import java.util.HashSet;

public class Commands implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4586835493215068372L;
	
	private int turnLeft = 37, turnRight = 39, moveLeft = 17, moveRight = 16, pause = 32, fallObs = 40,
			turnLeftDuo = 81, turnRightDuo = 68, moveLeftDuo = 69, moveRightDuo = 84, effect = 38;
	
	public int getTurnLeft() {
		return turnLeft;
	}

	public void setTurnLeft(int turnLeft) {
		this.turnLeft = turnLeft;
	}

	public int getTurnRight() {
		return turnRight;
	}

	public void setTurnRight(int turnRight) {
		this.turnRight = turnRight;
	}

	public int getMoveLeft() {
		return moveLeft;
	}

	public void setMoveLeft(int moveLeft) {
		this.moveLeft = moveLeft;
	}

	public int getMoveRight() {
		return moveRight;
	}

	public void setMoveRight(int moveRight) {
		this.moveRight = moveRight;
	}

	public int getPause() {
		return pause;
	}

	public void setPause(int pause) {
		this.pause = pause;
	}

	public int getFallObs() {
		return fallObs;
	}

	public void setFallObs(int fallObs) {
		this.fallObs = fallObs;
	}
	
	public boolean areIdenticalCommands() {
		ArrayList<Integer> compare = new ArrayList<Integer>();
		compare.add(turnLeft);
		compare.add(turnRight);
		compare.add(moveLeft);
		compare.add(moveRight);
		compare.add(pause);
		compare.add(fallObs);
		compare.add(turnLeftDuo);
		compare.add(turnRightDuo);
		compare.add(moveLeftDuo);
		compare.add(moveRightDuo);

		HashSet<Integer> set = new HashSet<Integer>(compare);
        return set.size() != compare.size();
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

		File file = new File("data/controls.ser");
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

	public static Commands read() {
		File file = new File("data/controls.ser");
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Commands controls = (Commands) ois.readObject();
			ois.close();
			return controls;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return new Commands();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			return new Commands();
		}
	}

	public int getTurnLeftDuo() {
		return turnLeftDuo;
	}

	public void setTurnLeftDuo(int turnLeftDuo) {
		this.turnLeftDuo = turnLeftDuo;
	}

	public int getTurnRightDuo() {
		return turnRightDuo;
	}

	public void setTurnRightDuo(int turnRightDuo) {
		this.turnRightDuo = turnRightDuo;
	}

	public int getMoveLeftDuo() {
		return moveLeftDuo;
	}

	public void setMoveLeftDuo(int moveLeftDuo) {
		this.moveLeftDuo = moveLeftDuo;
	}

	public int getMoveRightDuo() {
		return moveRightDuo;
	}

	public void setMoveRightDuo(int moveRightDuo) {
		this.moveRightDuo = moveRightDuo;
	}

	public int getEffect() {
		return effect;
	}

	public void setEffect(int effect) {
		this.effect = effect;
	}

}
