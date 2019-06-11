package com.bazz.treeparts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Dna implements Serializable {

    private float[] genes = new float[28];
    private Random rand = new Random(System.currentTimeMillis());
    List<GenerationDnaChangeData> historyOfChanges = new ArrayList<>();

    public Dna() {
        this.genes[0] = 145; // angle branch (w sumie nie uzywany, mozna zamienic na cos innego)
        this.genes[1] = 20; // angle delta branch
        this.genes[2] = 70; // 45 angle mini branch

        this.genes[3] = 160;// 160;//150, -150 //angle root start
        this.genes[4] = 30;// 30 //angle root delta
        this.genes[5] = 5;// 2 //start number of roots

        this.genes[6] = 80;// 150 //trunk size
        this.genes[7] = 80;// 80 //branch size
        this.genes[8] = 80;// 80 //minibranch size

        this.genes[9] = 200;// 200 //main root size
        this.genes[10] = 70;// 70;//-40 //root size

        this.genes[11] = (float) 0.2; // 0,2 trunk ratio
        this.genes[12] = (float) 0.3; // branch ratio
        this.genes[13] = (float) 0.4; // mini branch ratio

        this.genes[14] = (float) 0.25; // main root ratio
        this.genes[15] = (float) 0.2; // root ratio

        this.genes[16] = 0.8f;// 4 //delta growth main root
        this.genes[17] = 2;// 2f;//8 // delta growth root

        this.genes[18] = 0.8f;// 7 //delta growth trunk
        this.genes[19] = 0.8f;// 10 //delta growth branch
        this.genes[20] = 5f;// 5 // delta growth minibrancz

        this.genes[21] = (float) 0.33;// 0.2 distance between branches
        this.genes[22] = (float) 0.15;// 0.23 //distance between minibranches

        this.genes[23] = 13; // trunk angle delta
        this.genes[24] = 2;// 2 //root recursion level
        this.genes[25] = 2; // 2trunk recursion level
        this.genes[26] = 2000; // distance of reproduction
        this.genes[27] = 1300; // cost of child

    }

    public float getAngleBranch() {
        return genes[0];
    }

    public void setAngleBranch(float angleBranch) {
        this.genes[0] = angleBranch;
    }

    public float getAngleDeltaBranch() {
        return genes[1];
    }

    public void setAngleDeltaBranch(float angleDeltaBranch) {
        this.genes[1] = angleDeltaBranch;
    }

    public float getAngleMinibranch() {
        return genes[2];
    }

    public void setAngleMinibranch(float angleMinibranch) {
        this.genes[2] = angleMinibranch;
    }

    public float getAngleRootStart() {
        return genes[3];
    }

    public void setAngleRootStart(float angleRootStart) {
        this.genes[3] = angleRootStart;
    }

    public float getAngleRootDelta() {
        return genes[4];
    }

    public void setAngleRootDelta(float angleRootDelta) {
        this.genes[4] = angleRootDelta;
    }

    public float getStartNumberOfRoots() {
        return genes[5];
    }

    public void setStartNumberOfRoots(float startNumberOfRoots) {
        this.genes[5] = startNumberOfRoots;
    }

    public float getTrunkSize() {
        return genes[6];
    }

    public void setTrunkSize(float trunkSize) {
        this.genes[6] = trunkSize;
    }

    public float getBranchSize() {
        return genes[7];
    }

    public void setBranchSize(float branchSize) {
        this.genes[7] = branchSize;
    }

    public float getMiniBranchSize() {
        return genes[8];
    }

    public void setMiniBranchSize(float miniBranchSize) {
        this.genes[8] = miniBranchSize;
    }

    public float getMainRootSize() {
        return genes[9];
    }

    public void setMainRootSize(float mainRootSize) {
        this.genes[9] = mainRootSize;
    }

    public float getRootSize() {
        return genes[10];
    }

    public void setRootSize(float rootSize) {
        this.genes[10] = rootSize;
    }

    public float getTrunkRatio() {
        return genes[11];
    }

    public void setTrunkRatio(float trunkRatio) {
        this.genes[11] = trunkRatio;
    }

    public float getBranchRatio() {
        return genes[12];
    }

    public void setBranchRatio(float branchRatio) {
        this.genes[12] = branchRatio;
    }

    public float getMiniBranchRatio() {
        return genes[13];
    }

    public void setMiniBranchRatio(float miniBranchRatio) {
        this.genes[13] = miniBranchRatio;
    }

    public float getMainRootRatio() {
        return genes[14];
    }

    public void setMainRootRatio(float mainRootRatio) {
        this.genes[14] = mainRootRatio;
    }

    public float getRootRatio() {
        return genes[15];
    }

    public void setRootRatio(float rootRatio) {
        this.genes[15] = rootRatio;
    }

    public float getDeltaMainRoot() {
        return genes[16];
    }

    public void setDeltaMainRoot(float deltaMainRoot) {
        this.genes[16] = deltaMainRoot;
    }

    public float getDeltaRoot() {
        return genes[17];
    }

    public void setDeltaRoot(float deltaRoot) {
        this.genes[17] = deltaRoot;
    }

    public float getDeltaTrunk() {
        return genes[18];
    }

    public void setDeltaTrunk(float deltaTrunk) {
        this.genes[18] = deltaTrunk;
    }

    public float getDeltaBranch() {
        return genes[19];
    }

    public void setDeltaBranch(float deltaBranch) {
        this.genes[19] = deltaBranch;
    }

    public float getDeltaMiniBranch() {
        return genes[20];
    }

    public void setDeltaMiniBranch(float deltaMiniBranch) {
        this.genes[20] = deltaMiniBranch;
    }

    public float getDistanceBetweenBranches() {
        return genes[21];
    }

    public void setDistanceBetweenBranches(float distanceBetweenBranches) {
        this.genes[21] = distanceBetweenBranches;
    }

    public float getDistanceBetweenMiniBranches() {
        return genes[22];
    }

    public void setDistanceBetweenMiniBranches(float distanceBetweenMiniBranches) {
        this.genes[22] = distanceBetweenMiniBranches;
    }

    public double getAngleDeltaTrunk() {
        return genes[23];
    }

    public int getRootRecursionLevel() { // root recursion level
        return (int) genes[24];
    }

    public void setRootRecursionLevel(int rootRecursionLevel) {
        this.genes[24] = rootRecursionLevel;
    }

    public void setTrunkRecursionLevel(int trunkRecursionLevel) { // trunk recursion level
        this.genes[25] = trunkRecursionLevel;
    }

    public int getTrunkRecursionLevel() {
        return (int) genes[25];
    }

    public float getDistanceOfReproduction() {
        return genes[26];
    }

    public float getCostOfChild() {
        return genes[27];
    }

    public void mutateDna(Dna dna, int generation) {

        rand = new Random(System.nanoTime());

        GenerationDnaChangeData newGeneration = new GenerationDnaChangeData(generation);

        int j;

        for (int i = 0; i < dna.genes.length - 2; i++) {
            if (i == 3) {
                continue;
            }
            j = rand.nextInt(100);
            if (j > 70) {
                double val = 1.1 * dna.genes[i];
                newGeneration.addGenes(i, dna.genes[i], val);
                dna.genes[i] = (float) val;
            } else if (j < 30) {
                if (i == 22 && genes[i] < 0.1) {
                    continue;
                }
                double val = 0.9 * dna.genes[i];
                newGeneration.addGenes(i, dna.genes[i], val);
                dna.genes[i] = (float) val;

            }
        }
        j = rand.nextInt(100);
        if (j > 99) {
            newGeneration.addGenes(24, dna.genes[24], ++dna.genes[24]);
        }
        if (j < 2) {
            newGeneration.addGenes(24, dna.genes[24], --dna.genes[24]);
        }
        j = rand.nextInt(100);
        if (j > 99) {
            newGeneration.addGenes(25, dna.genes[25], ++dna.genes[25]);
        }
        if (j < 2) {
            newGeneration.addGenes(25, dna.genes[25], --dna.genes[25]);
        }

        for (int i = 0; i < 2; i++) {
            j = rand.nextInt(100);
            if (j > 80) {
                double val = 1.1 * dna.genes[26 + i];
                newGeneration.addGenes(26 + i, dna.genes[26 + i], val);
                dna.genes[26 + i] = (float) val;
            } else if (j < 20) {
                double val = 0.9 * dna.genes[26 + i];
                newGeneration.addGenes(26 + i, dna.genes[26 + i], val);
                dna.genes[26 + i] = (float) val;
            }
        }
        if(!newGeneration.changedGenes.isEmpty()) {
            historyOfChanges.add(newGeneration);
        }
    }

    public Dna copyDna(Dna dna) {
        Dna newDna = new Dna();
        for (int i = 0; i < dna.genes.length; i++) {
            newDna.genes[i] = dna.genes[i];
        }
        Iterator<GenerationDnaChangeData> it = dna.historyOfChanges.iterator();
        while (it.hasNext()) {
            GenerationDnaChangeData g = it.next();
            GenerationDnaChangeData thisChanges = new GenerationDnaChangeData(g.generation);
            Iterator<DnaSingleGeneChange> i = g.changedGenes.iterator();
            while (i.hasNext()) {
                DnaSingleGeneChange sg = i.next();
                //nad tym sie zastanow, 
                thisChanges.addGenes(sg.index, sg.oldValue, sg.newValue);
            }
            newDna.historyOfChanges.add(thisChanges);
        }
        return newDna;
    }

    public void shuffleDna(Dna dna1, Dna dna2, Dna newDna1, Dna newDna2) {
        rand = new Random(System.nanoTime());
        for (int i = 0; i < newDna1.genes.length; i++) {
            if (rand.nextBoolean()) {
                newDna1.genes[i] = dna1.genes[i];
            } else {
                newDna1.genes[i] = dna2.genes[i];
            }
            if (rand.nextBoolean()) {
                newDna2.genes[i] = dna1.genes[i];
            } else {
                newDna2.genes[i] = dna2.genes[i];
            }

        }

    }
    
    public void displayHistory() {
        for(GenerationDnaChangeData g : historyOfChanges) {
            g.displayHistory();
        }
    }

}
