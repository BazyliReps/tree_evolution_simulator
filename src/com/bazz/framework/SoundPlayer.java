/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bazz.framework;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import com.bazz.window.MainWindow;

/**
 *
 * @author bazyli
 */
public class SoundPlayer {

	Clip clip;
	AudioInputStream inputStream;

	public SoundPlayer(MainWindow mw) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// URL url =
					// mw.getClass().getClassLoader().getResource("/home/bazyli/wiatr.wav");
					inputStream = AudioSystem.getAudioInputStream(new File("/home/bazyli/las.wav"));
					DataLine.Info info = new DataLine.Info(Clip.class, inputStream.getFormat());
					clip = (Clip) AudioSystem.getLine(info);
					clip.open(inputStream);
					// clip.start();

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
			}

		});

	}

}
