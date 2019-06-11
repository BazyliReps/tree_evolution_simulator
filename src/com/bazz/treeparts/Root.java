package com.bazz.treeparts;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

import com.bazz.textures.Texture;
import com.bazz.window.GameLoop;

public class Root extends TreeObject implements Serializable {

	private float delta;
	private float maxSize;
	private double deltaangle;
	private double angle;
	private float ratio;
	private int recursionLevel;
	private int currentRecursionLevel;

	Dna d;

	private float width, height; // Wysokość ujemna!!!!

	private float size = 1;

	private float h, w;

	private boolean diverse = false;

	Root[] child = new Root[2];

	transient Texture tex = GameLoop.getInstance();

	public Root(float x, float y, TreePartId id, Dna dna, int currentRecursionLevel) {
		super(x, y);

		this.delta = dna.getDeltaRoot();
		this.size = 10;
		this.deltaangle = Math.toRadians(dna.getAngleRootDelta());
		this.ratio = dna.getRootRatio();
		this.recursionLevel = dna.getRootRecursionLevel();
		this.currentRecursionLevel = currentRecursionLevel;
		this.maxSize = dna.getRootSize();
		d = dna;

		this.height = (float) Math.sqrt(size / ratio);
		this.width = height * ratio;
		// this.facing = currentRecursionLevel % 2 == 0 ? 1 : -1;
	}

	private boolean underground = true;

	public boolean tick(Tree tree, float deltaIncreaseRatio) {
		if (height > -maxSize && underground) {
			height = -(float) Math.sqrt(size / ratio);
			width = height * ratio;
			size += delta * deltaIncreaseRatio;
			w = (float) Math.sin(angle) * height;
			h = (float) Math.cos(angle) * height;
			if (y + h < 0 && currentRecursionLevel > 2)
				underground = false;

		} else if (diverse == false && currentRecursionLevel < recursionLevel && underground) {
			currentRecursionLevel++;
			child[0] = new Root((float) (x + w), (float) (y - h), TreePartId.Root, d, currentRecursionLevel);
			child[1] = new Root((float) (x + w), (float) (y - h), TreePartId.Root, d, currentRecursionLevel);

			child[0].setAngle(angle + deltaangle);
			child[1].setAngle(angle - deltaangle);
			diverse = true;
			tree.incrementNumberOfRoots();
			tree.incrementNumberOfRoots();
		}

		if (diverse == true) {
			for (int j = 0; j < 2; j++) {
				child[j].setY((float) (y + h));
				child[j].setX((float) (x - w));
				if(child[j].tick(tree, deltaIncreaseRatio)) {
                                    continue;
                                }
			}
		}

		tree.incrementWater(evaluateWater());
		tree.incrementSize(size);
                return true;

	}

	@Override
	public void render(Graphics2D g2d) {
		if (tex == null) {
			tex = GameLoop.getInstance();
		}
		AffineTransform saveAT = g2d.getTransform();
		g2d.translate(-width / 2, 0);
		g2d.rotate(angle, x + width / 2, y);

		g2d.drawImage(tex.root[0], (int) (x), (int) y, (int) width, (int) height, null);
		g2d.translate(width / 2, 0);
		g2d.setTransform(saveAT);

		if (diverse == true) {
			for (int j = 0; j < 2; j++) {
				child[j].render(g2d);
			}
		}

	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, (int) width, (int) height);
	}

	public void setSize(float size) {
		this.size = size;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public void removeRoot(Root r) {
		if (diverse == true) {
			for (int j = 0; j < 2; j++) {
				child[j] = null;
			}
		}

	}

	public void remove() {

	}

	private float evaluateWater() {
		float depthBonus = 0.01f * y;
		return size * 0.12f + depthBonus;
	}

}
