/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bazz.containers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import com.bazz.treeparts.Tree;

/**
 *
 * @author bazyli
 */
public class TreeAreaListener implements PropertyChangeListener, Serializable {

	private Tree tree;
	private Area myArea;
	int x;

	public TreeAreaListener(TreeAreaContainer container, Tree tree, int x) {
		container.addListener(this, x);
		this.tree = tree;
		this.x = x;
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		tree.setNeighbours((int) e.getNewValue());
	}

}
