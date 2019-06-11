package com.bazz.treeparts;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;

public abstract class TreeObject implements Serializable {

	protected float x, y, width, height;
	protected int facing = 1;

	public TreeObject(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public abstract void render(Graphics2D g2d);

	public abstract Rectangle getBounds();

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public void setFacing(int facing) {
		this.facing = facing;
	}

	public int getFacing() {
		return facing;
	}

	public boolean collision() {
		return false;
	}

}
