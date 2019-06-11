package com.bazz.window;

//import static com.bazz.window.Game.cam;
import java.io.Serializable;

import com.bazz.framework.LevelCreator;

public class Camera implements Serializable {

	private float x = 0, y = 0;
	private float velX, velY;
	private final float yMax;
	private final float xMax;

	public Camera(float x, float y, int frameHeight, int frameWidth, LevelCreator creator) {
		this.x = x;
		this.y = y;
		this.velX = 0;
		this.velY = 0;
		this.yMax = creator.getLevelImageHeight() - frameHeight;
		this.xMax = creator.getLevelWidth() - frameWidth;
	}

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

	public float getVelY() {
		return velY;
	}

	public void setCamX(float x) {
		this.x = x;
	}

	public void setCamY(float y) {
		this.y = y;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public void resetCam() {
		setCamX(0);
		setCamY(300);
	}

	public void tick() {

		if (velX < 0) {
			if (x >= -xMax) {
				x += velX;
			}
		} else if (velX > 0) {
			if (x <= -10) {
				x += velX;
			}
		}
		if (y > -yMax || velY > 0) {
			y += velY;
		}
	}
}
