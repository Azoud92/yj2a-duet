package com.pi4.duet.controller.home;

import com.pi4.duet.model.home.EditorModel;

import java.io.IOException;

import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.model.game.data.PatternData;

public class EditorController {
	
	private final EditorModel model;
	
	private int selection = 0;
	
	public EditorController(EditorModel m) {
		model = m;
	}
	
	public void incrementSelection(int n) {selection += n;}
	
	public void transferData(Obstacle o, Long l) {
		model.getObstacles().add(o);
		model.getDelays().add(l);
	}
	
	public void transferData(Obstacle o, Long l, int i) {
		model.getObstacles().set(i, o);
		model.getDelays().set(i, l);
	}
	
	public void writeSelection(Obstacle o, Long l) {
		model.getObstacles().set(selection, o);
		model.getDelays().set(selection, l);
	}
	
	public void deleteSelection() {
		model.getObstacles().remove(selection);
		model.getDelays().remove(selection);
	}
	
	public int getSelection() {return selection;}
	
	public Obstacle getSelectedObstacle() {return model.getObstacles().get(selection);}
	public long getSelectedDelay() {return (long) model.getDelays().get(selection);}
	
	public int getSize() {return model.getObstacles().size();}
	
	public void transmitSaveRequest(String path) throws IOException {
		model.save(path);
	}
	
	public PatternData transmitCompileRequest() {
		return model.compile();
	}
	
}
