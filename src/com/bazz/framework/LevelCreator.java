/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bazz.framework;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author bazyli
 */
public class LevelCreator {

	private int width, height;
	private BufferedImage soil;

	public LevelCreator(int width, int height, BufferedImage soil) {
		this.width = width;
		this.height = height;
		this.soil = soil;
	}

	public void render(Graphics g) {
		for (int xx = 0; xx < width; xx += soil.getWidth() - 10) {
			g.drawImage(soil, xx, height, null);
		}
	}

	public int getLevelWidth() {
		return width;
	}

	public int getLevelHeight() {
		return height;
	}

	public int getLevelImageHeight() {
		return soil.getHeight();
	}

}
