package com.pi4.duet.controller.home;

import com.pi4.duet.model.home.EditorModel;
import com.pi4.duet.model.home.Settings;

import java.io.IOException;

import com.pi4.duet.model.game.Obstacle;
import com.pi4.duet.model.game.data.PatternData;

public class EditorController {
	
	private final EditorModel model;
	private Settings settings;
	private int selection = 0;
	
	public EditorController(EditorModel m, Settings s) {
		model = m;
		settings = s;
	}
	
	public void incrementSelection(int n) {selection += n;}
	
	public void transferData(Obstacle o, Long l) {
		model.getObstacles().add(o);
		model.getDelays().add(l);
	}
	
	public void writeSelection(Obstacle o, Long l) {
		model.getObstacles().set(selection, o);
		model.getDelays().set(selection, l);
		Long delta = 0l;
		if (selection < model.getDelays().size()-1) delta = model.getDelays().get(selection+1) - model.getDelays().get(selection);
		if (delta < 0) for (int i = selection+1 ; i < model.getDelays().size() ; i++) {
			model.getDelays().set(i, model.getDelays().get(i) - delta);
		}
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

	public final boolean isBackgroundEnabled() { return settings.getBackground(); }
	
}
