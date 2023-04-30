package com.pi4.duet;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	private AudioInputStream audioStream;
	private Clip clip;
	
	public Sound(String filename, boolean loop) {
		try {
			audioStream = AudioSystem.getAudioInputStream(this.getClass().getResource("/resources/snd/" + filename));
			clip = AudioSystem.getClip();
			clip.open(audioStream);
			if (loop) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				clip.stop();
			}
		}
		catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void play() {
		clip.setMicrosecondPosition(0);
		clip.start();
	}

	public void stop() {
		clip.stop();
	}

}
