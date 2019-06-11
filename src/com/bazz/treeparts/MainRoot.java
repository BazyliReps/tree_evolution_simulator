package com.bazz.treeparts;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.LinkedList;

import com.bazz.containers.TreeHandler;
import com.bazz.textures.Texture;
import com.bazz.window.GameLoop;

public class MainRoot extends TreeObject implements Serializable {

    public float delta, maxSize, ratio, width, height;
    private final float position;
    private float size = 1;

    LinkedList<Root> rootList;

    public TreeHandler handler;

    transient Texture tex = GameLoop.getInstance();

    public MainRoot(float x, float y, TreePartId id, Dna dna) {
        super(x, y);
        this.position = x;
        this.maxSize = dna.getMainRootSize();
        this.delta = dna.getDeltaMainRoot();
        this.ratio = dna.getMainRootRatio();
        this.size = 1;// 100;
        this.height = (float) Math.sqrt(size / ratio);
        this.width = height * ratio;
        rootList = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            rootList.add(new Root(x, y - height, TreePartId.Root, dna, 0));
        }

        rootList.get(0).setAngle(Math.toRadians(dna.getAngleRootStart()));
        rootList.get(1).setAngle(Math.toRadians(-dna.getAngleRootStart()));
        rootList.get(2).setAngle(Math.toRadians(180));
    }

    int i = 1;
    int j;
    float szer;
    float deltaSzer;

    public boolean tick(Tree tree, float deltaIncreaseRatio) {
        if (height < maxSize) {
            size += delta * deltaIncreaseRatio;
            height = (float) Math.sqrt(size / ratio);
        }
        tree.incrementSize(size);
        x = position - 0.7f * width;// - 0.5f*width;

        j = 0;
        for (Root r : rootList) {
            r.setY((float) (y + 0.06 * j * height));
            r.setX(x + width / 2);
            if (r.tick(tree, deltaIncreaseRatio)) {
                j++;
            }
        }
        tree.incrementWater(evaluateWater());
        return true;

    }

    @Override
    public void render(Graphics2D g2d) {
        if (tex == null) {
            tex = GameLoop.getInstance();
        }
        g2d.drawImage(tex.root[0], (int) (x), (int) y, (int) width, (int) height, null);
        j = 0;
        for (Root r : rootList) {
            r.render(g2d);
        }
    }

    private float evaluateWater() {
        return size * 0.05f;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, (int) width, (int) height);
    }

    public void setSize(float size) {
        this.size = size;
    }

    public void setWidth(float width) {
        this.width = width;
    }

}
