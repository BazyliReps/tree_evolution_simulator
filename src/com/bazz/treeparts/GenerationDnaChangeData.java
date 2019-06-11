/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bazz.treeparts;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bazyli
 */
public class GenerationDnaChangeData {

    public int generation;
    public List<DnaSingleGeneChange> changedGenes = new ArrayList<>();

    public GenerationDnaChangeData(int generation) {
        this.generation = generation;
    }

    public void addGenes(int index, double oldVal, double newVal) {
        changedGenes.add(new DnaSingleGeneChange(index, oldVal, newVal));
    }

    public void displayHistory() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("\n\n\ngeneration: " + generation);
        for (DnaSingleGeneChange d : changedGenes) {
            System.out.println("\n\n" + getGeneName(d.index));
            System.out.println("old value: " + d.oldValue);
            System.out.println("new value: " + d.newValue);
        }
        System.out.println("------------------------------------------------------------------");

    }

    public String getGeneName(int index) {
        switch (index) {
            case 0:
                return "branch angle";
            case 1:
                return "branch delta angle";
            case 2:
                return "angle mini branch";
            case 3:
                return "start root angle";
            case 4:
                return "root delta angle";
            case 5:
                return "start number of roots";
            case 6:
                return "trunk size";
            case 7:
                return "branch size";
            case 8:
                return "minibranch size";
            case 9:
                return "main root size";
            case 10:
                return "root size";
            case 11:
                return "trunk ratio";
            case 12:
                return "branch  ratio";
            case 13:
                return "minibranch ratio";
            case 14:
                return "main root ratio";
            case 15:
                return "root ratio";
            case 16:
                return "delta growth main root";
            case 17:
                return "delta growth root";
            case 18:
                return "delta growth trunk";
            case 19:
                return "delta growth branch";
            case 20:
                return "delta growth minibranch";
            case 21:
                return "distance between branches";
            case 22:
                return "distance between minibranches";
            case 23:
                return "trunk angle delta";
            case 24:
                return "root recursion level";
            case 25:
                return "trunk recursion level";
            case 26:
                return "distance of reproduction";
            case 27:
                return "cost of child";
            default:
                return "error";
        }
    }

}
