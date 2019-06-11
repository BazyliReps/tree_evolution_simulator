package com.bazz.containers;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import com.bazz.treeparts.Tree;
import com.bazz.window.Camera;
import com.bazz.window.GameLoop;

public class TreeHandler implements Serializable {

    private static final long serialVersionUID = -3024851680776623574L;

    transient public Rectangle r;

    private int year, numberOfTrees, numberOfDyingTrees;

    private final LinkedList<Tree> treeList = new LinkedList<>();

    transient private Tree tempTree;
    transient private Camera cam;
    transient private GameLoop game;
    private AreaHeightsContainer areaHeights;

    public TreeHandler(Camera cam, GameLoop game) {
        this.cam = cam;
        this.game = game;
        this.areaHeights = new AreaHeightsContainer(game.getGameSize(), game.getNumberOfAreas());
    }

    public boolean tick() {
        for (int i = 0; i < treeList.size(); i++) {
            tempTree = treeList.get(i);
            if (tempTree.getToRemove()) {
                treeList.remove(tempTree);
                game.decrementNumberOfTrees();
            } else {
                if (tempTree.tick()) {
                    if (tempTree.getSeasons() > 0) {
                        areaHeights.addNewSumOfHeights(tempTree.getTreeHeight(), tempTree.getNumberOfMinis(),
                                tempTree.getX());
                        tempTree.setAverageHeight(areaHeights.getAverageHeight(tempTree.getX()));
                        tempTree.setMaximumHeight(areaHeights.getMaximumHeight(tempTree.getX()));
                        tempTree.setMaximumHeightDisplay(areaHeights.getMaximumHeightDisplay(tempTree.getX()));
                    }
                }
            }
            
        }
        areaHeights.setAverageHeights();
        areaHeights.resetAreaHeightsContainer();
        return true;
    }

    public void render(Graphics2D g2d) {

        for (int i = 0; i < treeList.size(); i++) {
            tempTree = treeList.get(i);
            if (tempTree != null) {
                r = tempTree.getBounds();
                if (tempTree.getX() >= -cam.getX() && tempTree.getX() <= -cam.getX() + game.getWidth()) {
                    tempTree.render(g2d);

                }
            }
        }

    }

    public void addTree(Tree tree) {
        this.treeList.add(tree);
    }

    public void removeObject(Tree tree) {
        this.treeList.remove(tree);
    }

    public void loadTreeHandler(GameLoop game, Camera cam, TreeAreaContainer areaContainer) {
        setGame(game);
        setCam(cam);
        game.setYear(year);
        game.setNumberOfDyingTrees(numberOfDyingTrees);
        game.setNumberOfTrees(numberOfTrees);
        if (!treeList.isEmpty()) {
            try {
                areaContainer = treeList.getFirst().getAreaContainer();
            } catch (NullPointerException np) {
                System.out.println("pusty handler");
            }
        }
    }

    public void resetHandler() {
        Iterator<Tree> iterator = treeList.iterator();
        while (iterator.hasNext()) {
            tempTree = iterator.next();
            // tempTree.remove();
            iterator.remove();
        }

    }

    public Tree collision(float x, float y) {
        for (int i = 0; i < treeList.size(); i++) {
            tempTree = treeList.get(i);
            r = tempTree.getBounds();
            if (r.contains(x, y)) {
                return tempTree;
            }
        }
        return null;
    }

    public void setDifficulty(int difficulty) {
        for (Tree t : treeList) {
            t.setDifficulty(difficulty);
        }
    }

    public LinkedList getTreeListReference() {
        return treeList;
    }

    public void setCam(Camera cam) {
        this.cam = cam;
    }

    public void setGame(GameLoop game) {
        this.game = game;
        for (Tree t : treeList) {
            t.setGame(game);
        }
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setTrees(int trees) {
        this.numberOfTrees = trees;
    }

    public void setDyingTrees(int trees) {
        this.numberOfDyingTrees = trees;
    }
    
    public boolean reproduce() {
        for(int i = 0; i < treeList.size();i++) {
            Tree t = treeList.get(i);
            //if(t.reproduce());
            t.reproduce();
        }
        return true;
        
    }
    
    

}
