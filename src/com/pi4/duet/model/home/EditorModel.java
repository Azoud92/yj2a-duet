package com.pi4.duet.model.home;

import java.io.IOException;
import java.util.LinkedList;

import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.model.game.data.PatternData;
import com.pi4.duet.Point;
import com.pi4.duet.model.game.Direction;

public class EditorModel {
	
	private LinkedList<Obstacle> obstacles = new LinkedList<>();
	private LinkedList<Long> delays = new LinkedList<>();
	
	public EditorModel() {
		Point[] points = new Point[4];
		for (int i = 0 ; i < 4 ; i++) points[i] = new Point(0, 0);
		
		obstacles.add(new Obstacle(points, new Point(0, 0), 0, 0, 0, Direction.BOTTOM, null));
		delays.add(0l);
	}
	
	public LinkedList<Obstacle> getObstacles() {return obstacles;}
	public LinkedList<Long> getDelays() {return delays;}
	
	public PatternData compile() {
		PatternData res = new PatternData();
		for (int i = 0 ; i < obstacles.size() ; i++) res.put(obstacles.get(i), delays.get(i));
		return res;
	}
	
	public void save(String path) throws IOException {
		compile().write(path);
	}

}
