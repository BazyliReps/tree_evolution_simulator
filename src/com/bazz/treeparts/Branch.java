package com.bazz.treeparts;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

import com.bazz.textures.Texture;
import com.bazz.window.GameLoop;

public class Branch extends TreeObject implements Serializable {

    private float delta;
    private float maxSize;
    private double angle;
    private float ratio;
    private float distanceBetween;
    private Dna dna_ref;

    private float width, height;

    private float size;

    private float h, w;

    private boolean havemini = false;

    LinkedList<MiniBranch> miniList;

    transient Texture tex = GameLoop.getInstance();

    private boolean toDie = false;
    private boolean toRemove = false;
    private boolean isEmpty = false;

    public Branch(float x, float y, TreePartId id, Dna dna, int i, double angle) {
        super(x, y);
        this.delta = dna.getDeltaBranch();
        this.maxSize = dna.getBranchSize();
        this.ratio = dna.getBranchRatio();
        this.distanceBetween = dna.getDistanceBetweenMiniBranches();
        this.dna_ref = dna;
        double deltaAngle;
        Random rand = new Random(System.currentTimeMillis());
        if (rand.nextBoolean() == true) {
            deltaAngle = -0.3 * angle * rand.nextDouble();
        } else {
            deltaAngle = 0.3 * angle * rand.nextDouble();
        }
        this.angle = angle + deltaAngle;
        miniList = new LinkedList<>();
        havemini = true;

        this.size = 1;
        this.height = (float) Math.sqrt(size / ratio);
        this.width = height * ratio;
        miniList.add(new MiniBranch(x, y - height, TreePartId.MiniBranch, dna, Math.toRadians(dna_ref.getAngleMinibranch()), 0));

    }

    int i = 1;
    int j;

    public boolean tick(Tree tree, float deltaIncreaseRatio) {

        if (height > -maxSize) {
            size += delta * deltaIncreaseRatio;
            height = -(float) Math.sqrt(size / ratio);
            width = (height * ratio);
        }
        tree.incrementSize(size);

        w = (float) Math.sin(angle) * height;
        h = (float) Math.cos(angle) * height;

        if (height < -i * 4 && i < 1 / distanceBetween) {
            int n = i;
            for (int j = 0; j < 2; j++) {
                miniList.add(new MiniBranch((float) (x + (w - w * distanceBetween * i)),
                        (float) (y - (h - h * distanceBetween * i)), TreePartId.Branch, dna_ref,
                        angle + (i % 2 == 0 ? 1 : -1) * Math.toRadians(dna_ref.getAngleMinibranch()), i));
                havemini = true;
                tree.incrementNumberOfMinibranches();
                i++;
            }

        }
        if (havemini == true) {
            j = 0;
            for (int i = 0; i < miniList.size(); i++) {
                MiniBranch mb = miniList.get(i);
                mb.setY((float) (y + h - h * distanceBetween * j));
                mb.setX((float) (x + (-facing) * w - w * (-facing) * distanceBetween * j));
                if (mb.tick(tree, deltaIncreaseRatio)) {
                    if (mb.toRemove()) {
                        miniList.remove(mb);
                    }
                    j++;
                }
            }
        }
        if (toDie) {
            size -= delta * 20;
            if (size < 1) {
                toRemove = true;
            }
        }
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

        g2d.drawImage(tex.branch[0], (int) (x), (int) y, (int) width, (int) height, null);
        g2d.translate(width / 2, 0);
        g2d.setTransform(saveAT);

        for (int i = 0; i < miniList.size(); i++) {
            MiniBranch tempMiniBranch = miniList.get(i);
            if (tempMiniBranch != null) {
                tempMiniBranch.render(g2d);
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

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public void remove() {
        removeMiniBranch();
    }

    public void removeMiniBranch() {
        for (MiniBranch mb : miniList) {
            mb = null;
        }
        miniList = null;
    }

    public boolean killOnBranch() {
        if (!miniList.isEmpty()) {
            miniList.getLast().setToDie(true);
            return true;
        } else {
            return false;
        }
    }

    public void setToDie(boolean toDie) {
        this.toDie = toDie;
    }

    public boolean getToDie() {
        return toDie;
    }

    public boolean toRemove() {
        return toRemove;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean getIsEmpty() {
        return isEmpty;
    }
}
