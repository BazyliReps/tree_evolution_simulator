package com.bazz.treeparts;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

import com.bazz.containers.TreeHandler;
import com.bazz.textures.Texture;
import com.bazz.window.GameLoop;

public class Trunk extends TreeObject implements Serializable {

    public float delta;
    private final float maxSize;
    private float ratio;
    private final float distanceBetween;
    private final int maxRecursionLevel;
    private double angle;
    private final double deltaangle;
    private final Dna dna_ref;

    private float size;

    private boolean diverse = false;
    private float x;

    private boolean havebranch = true;
    LinkedList<Branch> branchChild;
    LinkedList<Trunk> trunkChild;
    LinkedList<MiniBranch> miniChild;

    transient Texture tex;

    public TreeHandler handler;

    int j;

    private float h, w;
    private int recursionLevel;
    private int currentRecursionLevel;

    private boolean haveTexture = false;

    private boolean toDie = false;
    private boolean toRemove = false;

    public Trunk(float x, float y, TreePartId id, Dna dna, int currentRecursionLevel, double angle) {
        super(x, y);
        this.delta = dna.getDeltaTrunk();
        this.maxSize = (float) (dna.getTrunkSize() * (1 - 0.1 * currentRecursionLevel));
        this.ratio = dna.getTrunkRatio();
        ratio = (float) (ratio - 0.2 * currentRecursionLevel * ratio);
        this.distanceBetween = dna.getDistanceBetweenBranches();
        this.maxRecursionLevel = dna.getTrunkRecursionLevel();
        dna_ref = dna;
        this.facing = currentRecursionLevel % 2 == 0 ? 1 : -1;
        double randomDeltaAngle;
        Random rand = new Random(System.currentTimeMillis());
        if (rand.nextBoolean() == true) {
            randomDeltaAngle = -0.5 * angle * rand.nextDouble();
        } else {
            randomDeltaAngle = 0.5 * angle * rand.nextDouble();
        }
        this.angle = angle + randomDeltaAngle;

        this.size = 1;
        this.height = -(float) Math.sqrt(size / ratio);
        this.width = height * ratio;
        this.x = x;
        this.currentRecursionLevel = currentRecursionLevel;
        this.deltaangle = Math.toRadians(dna.getAngleDeltaTrunk());

        branchChild = new LinkedList<>();
        trunkChild = new LinkedList<>();
        miniChild = new LinkedList<>();

        if (currentRecursionLevel == 0) {
            miniChild.add(new MiniBranch(x, y + height, TreePartId.MiniBranch, dna, Math.toRadians(115), i));
            miniChild.add(new MiniBranch(x, y + height, TreePartId.MiniBranch, dna, Math.toRadians(85), i));
        }

    }

    int i = 0;

    public boolean tick(Tree tree, float deltaIncreaseRatio) {
        if (currentRecursionLevel == 0) {
        }
        w = (float) Math.sin(angle) * (-height);
        h = (float) Math.cos(angle) * (-height);
        if (height > -maxSize) {
            size += delta * deltaIncreaseRatio;//
            height = -(float) Math.sqrt(size / ratio);
            width = (float) (ratio * height);
        }
        if (toDie) {
            size -= delta * 20;
            if (size < 1) {
                toRemove = true;
            }
        } else {
            tree.incrementSize(size * 0.2f);

            if (height < -(i) * 15 && i < (int) 1 / distanceBetween && recursionLevel != 0) {

                branchChild.add(new Branch((float) (x + (w * (1 - distanceBetween * i))),
                        (float) (y - (h * (1 - distanceBetween * i))), TreePartId.Branch, dna_ref, i,
                        angle + (i % 2 == 0 ? 1 : -1) * Math.toRadians(dna_ref.getAngleDeltaBranch())));
                tree.incrementNumberOfBranches();
                i++;
            }
            if (havebranch == true) {
                j = 0;
                for (j = 0; j < branchChild.size(); j++) {
                    Branch tempBranch = branchChild.get(j);
                    tempBranch.setY((float) (y - h * (1 - distanceBetween * j))); // moze minus miedzy h
                    tempBranch.setX(x + (w * (1 - distanceBetween * j)));
                    if (tempBranch.tick(tree, deltaIncreaseRatio)) {
                        if (tempBranch.toRemove()) {
                            branchChild.remove(tempBranch);
                        }
                    }
                }
            }

            if (recursionLevel == 0) {
                for (MiniBranch m : miniChild) {
                    if (m != null) {
                        m.setX(x);
                        m.setY(y + height);
                        if (m.tick(tree, deltaIncreaseRatio)) {
                            ;
                        }
                    }
                }
            }

            if (diverse == false && (currentRecursionLevel < maxRecursionLevel)
                    && height < -20 * (currentRecursionLevel)) {
                currentRecursionLevel++;
                for (int i = 0; i < 2; i++) {
                    trunkChild.add(new Trunk((float) (x + w), -(float) (y - h), TreePartId.Trunk, dna_ref,
                            currentRecursionLevel + 1, i == 0 ? angle - deltaangle : angle + deltaangle));
                    trunkChild.getLast().setRecursionLevel(recursionLevel + 1);
                    tree.incrementNumberOfTrunks();
                }
                diverse = true;
            }

            if (diverse == true && !trunkChild.isEmpty()) {
                for (int i = 0; i < trunkChild.size(); i++) {
                    Trunk t = trunkChild.get(i);
                    t.setY((float) (y - h + 2));
                    t.setX((float) (x + w));
                    t.setWidth(width / 2);
                    if (t.tick(tree, deltaIncreaseRatio)) {
                        if (t.toRemove()) {
                            trunkChild.remove(t);
                        }
                    }
                }
            }
        }
        if (recursionLevel == 0 && trunkChild.isEmpty() && tree.getPoints() < 0) {
            toDie = true;
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
        g2d.rotate(angle, x /*- width / 2*/ + width / 2, y);

        g2d.drawImage(tex.trunk[0], (int) (x), (int) y, (int) width, (int) height, null);
        g2d.setTransform(saveAT);
        if (recursionLevel == 0) {
            for (MiniBranch m : miniChild) {
                if (m != null) {
                    m.render(g2d);
                }
            }
        }
        if (diverse == true) {
            for (Trunk t : trunkChild) {
                if (t != null) {
                    t.render(g2d);
                }
            }
        }
        if (havebranch == true) {
            for (j = 0; j < branchChild.size(); j++) {
                Branch b = branchChild.get(j);
                if (b != null) {
                    b.render(g2d);
                }
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

    @Override
    public float getX() {
        return x;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setRecursionLevel(int level) {
        this.recursionLevel = level;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getWidth() {
        return width;
    }

    public float getUpperBound() {
        return y - height;
    }

    public boolean killOnTrunk() {
        deleteMinis();
        Random rand = new Random(System.currentTimeMillis());
        if (!trunkChild.isEmpty()) {
            if (rand.nextBoolean() ? trunkChild.getLast().killOnTrunk() : trunkChild.getFirst().killOnTrunk()) {
                return true;
            } else if (!trunkChild.getLast().killOnTrunk()) {
                trunkChild.getLast().setToDie(true);
                return true;
            } else {
                trunkChild.getFirst().setToDie(true);
                return true;
            }
        }

        if (!branchChild.isEmpty()) {
            Branch b = branchChild.getLast();
            int i = 0;
            while (b.getIsEmpty() && i < branchChild.size()) {

                b = branchChild.get(i);
                i++;

                if (i == branchChild.size() && b.getIsEmpty()) {
                    for (int ii = 0; ii < branchChild.size(); ii++) {
                        b = branchChild.get(ii);
                        b.setToDie(true);
                    }
                }

                //return false;
            }
            /*if (b.toRemove() && !b.getToDie()) {
				branchChild.remove(b);
				return true;
			}*/

            if (!b.getIsEmpty()/*b != null*/ & b.killOnBranch()) {
                return true;
            } else {
                //b.setToDie(true);
                b.setIsEmpty(true);
                return true;
            }
        }
        return false;
    }

    private boolean deleteMinis() {
        if (!miniChild.isEmpty()) {
            miniChild.removeLast();
            return true;
        } else {
            return false;
        }
    }

    public void setToDie(boolean toDie) {
        this.toDie = toDie;
    }

    public boolean toRemove() {
        return toRemove;
    }

}
