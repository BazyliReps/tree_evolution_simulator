/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bazz.treeparts;

/**
 *
 * @author bazyli
 */
public class DnaSingleGeneChange {

    int index;
    double newValue;
    double oldValue;
    
    public DnaSingleGeneChange(int index, double oldValue, double newValue) {
        this.index = index;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
}
