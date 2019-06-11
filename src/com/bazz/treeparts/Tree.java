package com.bazz.treeparts;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.bazz.containers.TreeAreaContainer;
import com.bazz.containers.TreeAreaListener;
import com.bazz.containers.TreeHandler;
import com.bazz.window.GameLoop;

enum TREESTATE {
    DYING(), LIVING();
}

public class Tree extends TreeObject implements Serializable {

    private int generation;
    
    private TREESTATE treeState = TREESTATE.LIVING;
    private boolean toRemove = false;

    private Trunk trunk;
    private MainRoot mainroot;
    TreeHandler handler;
    private Dna dna;
    private Dna newDna = new Dna();
    private double treeWidth;

    private float water = 0;
    private float sun = 0;
    private float points = 0;
    private float sumOfDeltas = 0;
    private float size = 0;
    private float resources = 0;
    private float deltaPoints = 0;
    private float deltaIncreaseRatio = 1;

    private float distanceOfReproduction;
    private float costOfChild;
    private int seasons = -1;

    transient Rectangle r;

    private int numberOfTrunks = 0;
    private int numberOfBranches = 0;
    private int numberOfMinibranches = 0;
    private int numberOfRoots = 0;
    private int numberOfMainroots = 0;

    private float oldpoints = 0;
    private int difficulty = 0;
    private int averageHeight = 0;
    private float maximumHeight = 0;
    private float maximumHeightDisplay = 0;
    private float myHeight = 0;
    private float averageWidth = 0;

    private Random rand;
    private int months = 0;

    private boolean reproduced = false;
    private boolean willReproduce = false;
    private boolean isHighest = false;

    private int neighbours = -1;
    private TreeAreaContainer neighboursContainer;
    private TreeAreaListener neighboursListener;
    transient private GameLoop game;

    public Tree(float x, float y, TreeHandler treeHandler, Dna dna/* , MainWindow mw */,
            TreeAreaContainer neighboursContainer, GameLoop game) {
        super(x, y);
        this.neighboursContainer = neighboursContainer;
        this.neighboursListener = new TreeAreaListener(neighboursContainer, this, (int) x);
        // this.mw = mw;
        this.game = game;
        this.seasons = -1;
        this.dna = dna;
        this.handler = treeHandler;
        rand = new Random(System.nanoTime());
        if (rand.nextInt(100) > 10) { // TODO kopiowac dna przez wartosc, nie przez referencje
            dna.mutateDna(dna, GameLoop.generations);
        }
        this.distanceOfReproduction = dna.getDistanceOfReproduction();
        this.costOfChild = dna.getCostOfChild();
        trunk = new Trunk(x, y, TreePartId.Trunk, dna, 0, (Math.toRadians(0)));
        treeWidth = trunk.getWidth();
        trunk.setAngle(Math.toRadians(0));
        mainroot = new MainRoot(x, y, TreePartId.MainRoot, dna);
        incrementNumberOfTrunks();
        incrementNumberOfMainRoots();
        neighboursContainer.changeNumberOfTreesInArea(x, true);
        this.difficulty = game.getDifficulty();
        this.points = dna.getCostOfChild();
        this.generation = GameLoop.generations;
    }

    private int lastMonth = 0;

    synchronized public /*void*/boolean /*boolean*/ tick() {
        minis = 0;
        size = 0;
        water = 0;
        sun = 0;
        checkIfIsHighest();
        defineReproductionStatus();
        mainroot.tick(this, deltaIncreaseRatio);
        trunk.tick(this, deltaIncreaseRatio);
        //if (trunk.tick(this, deltaIncreaseRatio) && mainroot.tick(this, deltaIncreaseRatio)) { //wersja synchronizowana
            treeWidth = trunk.getWidth();
            mainroot.setWidth((float) treeWidth);
            evaluateResources();
            evaluatePoints();
            deltaIncreaseRatio = evaluateDeltaIncreaseRatio();
            if (game.months != lastMonth) {
                defineState();
                lastMonth = game.months;
                reproduced = false;
            }
            if (game.reproductionFlag == true && reproduced == false) {
                //reproduce();
            }
       // }
        return true;

    }

    @Override
    public void render(Graphics2D g2d) {
        if (trunk != null) {
            trunk.render(g2d);
        }
        mainroot.render(g2d);
    }

    private void defineReproductionStatus() {
        if (points > dna.getCostOfChild()) {
            willReproduce = true;
        } else {
            willReproduce = false;
        }
    }

    public void incrementWater(float singleWater) {
        water += singleWater;
    }

    public boolean incrementSun(float singleSun) {
        sun += singleSun;
        return true;
    }

    public void incrementSize(float singleSize) {
        size += 0.01f * singleSize;
    }

    public void incrementNumberOfTrunks() {
        numberOfTrunks++;
        sumOfDeltas += dna.getDeltaTrunk();
    }

    public void incrementNumberOfBranches() {
        numberOfBranches++;
        sumOfDeltas += dna.getDeltaBranch();
    }

    public void incrementNumberOfMinibranches() {
        numberOfMinibranches++;
        sumOfDeltas += dna.getDeltaMiniBranch();
    }

    public void incrementNumberOfRoots() {
        numberOfRoots++;
        sumOfDeltas += dna.getDeltaRoot();
    }

    public void incrementNumberOfMainRoots() {
        numberOfMainroots++;
        sumOfDeltas += dna.getDeltaMainRoot();
    }

    ////////////////////////////////////////////////////
    // resources block
    private void evaluateResources() {

        float penalty = (1 / (1 + 0.04f * (seasons > 0 ? neighbours : 1)));
        if (water > sun) {
            resources = sun * penalty * (1 - 0.5f * ((float) difficulty / 100));
        } else {
            resources = water * penalty * (1 - 0.5f * ((float) difficulty / 100));
        }
        if (isHighest) {
            resources *= 2f;
        }
    }

    private float evaluateHunger() {
        return (0.6f * size * (seasons * 0.03f + 1));
    }

    private void evaluatePoints() {
        deltaPoints = resources - evaluateHunger();
        points += deltaPoints;
        myHeight = allHeights / minis;

        if (points > 0.8f * size) {
            if (seasons > 0) {
                points = (0.8f * size);
            } else {
                if (points > dna.getCostOfChild()) {
                    points = dna.getCostOfChild() * 0.1f;
                }
            }
        }
    }

    public float evaluateDeltaIncreaseRatio() {
        if (sumOfDeltas > 0.8 * points) {
            float ratio = 0.8f * points / sumOfDeltas;
            points *= 0.2f;
            return ratio > 0 ? ratio : 0;
        } else {
            points -= sumOfDeltas;

            return 1f;
        }
    }

    // end of resources block
    ///////////////////////////////////////////////////////
    private void defineState() {
        if (points < oldpoints && seasons > 0 || points < 0) {
            treeState = TREESTATE.DYING;
            if (points < 0 && !isDying) {
                game.incrementNumberOfDyingTrees();
                isDying = true;
            }
        } else {
            treeState = TREESTATE.LIVING;
            if (isDying && points > 0) {
                isDying = false;
                game.decrementNumberOfDyingTrees();
            }
        }
        if (treeState == TREESTATE.DYING) {
            if (!trunk.killOnTrunk() && trunk.toRemove()) {
                toRemove = true;
                neighboursContainer.changeNumberOfTreesInArea(x, !isDying);
                game.decrementNumberOfDyingTrees();
            }
        }
        oldpoints = points;
    }

    public boolean reproduce() {
        if (seasons > 1 && willReproduce) {
            int numberOfChilds = (int) (points / dna.getCostOfChild());
            for (int i = 0; i < numberOfChilds; i++) {
                rand = new Random(System.currentTimeMillis());
                int position = (int) (x + dna.getDistanceOfReproduction() / 2
                        - rand.nextInt((int) dna.getDistanceOfReproduction()));
                if (position > 0 && position < 40000) {
                    handler.addTree(new Tree(position, y, handler, dna.copyDna(dna), neighboursContainer, game));
                    
                    game.incrementNumberOfTrees();
                }
                points -= dna.getCostOfChild();
            }
        }
        seasons++;
        reproduced = true;
        return true;

    }

    ///////////////////////////////////////////////////////
    // getters setters
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) (x + trunk.getWidth() / 2), (int) (y + trunk.getHeight()),
                (int) (-trunk.getWidth() + 20), (int) (-trunk.getHeight()));
    }

    public float getWater() {
        return water;
    }

    public float getSun() {
        return sun;
    }

    public float getPoints() {
        return points;
    }

    public float getSize() {
        return size;
    }

    public String toString() {
        return ("tree x: " + x);
    }

    public boolean getToRemove() {
        return toRemove;
    }

    private boolean isDying = false;

    public void setMonths(int months) {
        this.months = months;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setNeighbours(int neighbours) {
        this.neighbours = neighbours;
    }

    public int getNeighbours() {
        return neighbours;
    }

    public Dna getDna() {
        return dna;
    }

    public void setNewDna(Dna newDna) {
        this.newDna = newDna;
    }

    public Dna getNewDna() {
        return newDna;
    }

    public void setAverageHeight(int height) {
        this.averageHeight = height;
    }

    public void setMaximumHeight(float height) {
        this.maximumHeight = height;
    }

    public void setAverageWidth(float width) {
        this.averageWidth = width;
    }

    float allHeights = 0;

    int minis = 0;

    public void addToSumOfHeights(float y) {
        allHeights += y;
        minis++;
    }

    public float getTreeHeight() {
        return allHeights;
    }

    public int getAverageAreaHeight() {
        return averageHeight;
    }

    public float getMyHeight() {
        return myHeight;
    }

    public float getMaximumHeightDisplay() {
        return maximumHeightDisplay;
    }

    public void setMaximumHeightDisplay(float height) {
        this.maximumHeightDisplay = height;
    }

    public int getSeasons() {
        return seasons + 1;
    }

    public boolean getWillReproduce() {
        return willReproduce;
    }

    public TreeAreaContainer getAreaContainer() {
        return neighboursContainer;
    }

    public void setGame(GameLoop game) {
        this.game = game;
    }

    public int getNumberOfMinis() {
        return minis;
    }

    private void checkIfIsHighest() {
        if (allHeights == maximumHeight) {
            isHighest = true;
        }
        allHeights = 0;
    }
    
    public void displayHistory() {
        dna.displayHistory();
    }
    
    

}
