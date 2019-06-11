package com.bazz.treeparts;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

import com.bazz.textures.Texture;
import com.bazz.window.GameLoop;
import com.bazz.window.MainWindow;

public class MiniBranch extends TreeObject implements Serializable, AngleObserver{

    public float delta;
    private float maxSize;
    private double angle;
    private double deltaangle;
    private double ratio;
    private boolean toDie = false;
    private boolean toRemove = false;
    
    private double sunAngle = 45;

    public float size = 1;

    private float width, height;

    private int facing = 1;

    transient Texture tex = GameLoop.getInstance();

    public MiniBranch(float x, float y, TreePartId id, Dna dna, double angle, int i) {
        super(x, y);
        this.delta = dna.getDeltaMiniBranch();
        this.maxSize = dna.getMiniBranchSize();
        this.deltaangle = dna.getAngleMinibranch();
        this.angle = angle;
        this.ratio = dna.getMiniBranchRatio();
        this.facing = i % 2 == 0 ? 1 : -1;
        GameLoop.minis++;
        MainWindow.observers.add(this);

    }

    public boolean tick(Tree tree, float deltaIncreaseRatio) {

        if (width < maxSize && width > -maxSize) {
            size += delta * deltaIncreaseRatio;
            width = (float) (-facing * (size / Math.sqrt(2 * size)));
            height = (float) (-facing * (ratio * width));
        }
        if (toDie) {
            size -= delta * 20;
            if (size < 1) {
                toRemove = true;
            }

        }
        tree.incrementSize(size * 0.5f);
        if (tree.incrementSun(evaluateSun(tree.getAverageAreaHeight()))) {
            tree.addToSumOfHeights(y);
            // System.out.println("minibranch 54: y: " + y );
        }
        return true;

    }

    @Override
    public void render(Graphics2D g2d) {
        if (tex == null) {
            tex = GameLoop.getInstance();
        }
        AffineTransform saveAT = g2d.getTransform();
        g2d.rotate(angle, x, y);

        g2d.drawImage(tex.minibranch[0], (int) (x), (int) y, (int) width, (int) height, null);
        g2d.setTransform(saveAT);

    }

    private float evaluateSun(int avgHeight) {
        float heightBonus = 0;// (avgHeight - y) * 0.002f;
        // System.out.println("Minibranch 71: height: " + avgHeight);
        return (float) (size * 0.008f + heightBonus * Math.abs(Math.sin(Math.toRadians(sunAngle - angle))));
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, (int) width, (int) height);
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setToDie(boolean toDie) {
        this.toDie = toDie;
        MainWindow.observers.remove(this);
    }

    public boolean toRemove() {
        return toRemove;
    }

    @Override
    public void update(double angle) {
            this.sunAngle = angle;
    }

}
