/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bazz.containers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import com.bazz.treeparts.Dna;
import com.bazz.treeparts.Tree;

/**
 *
 * @author bazyli
 */
public class DnaShuffle {

	private Dna firstDna, secondDna, newDna;
	private Tree tempTree1, tempTree2;
	private Random rand;

	public DnaShuffle() {
		newDna = new Dna();
	}

	private void shuffleList(LinkedList list) {
		Collections.shuffle(list);
	}

	public boolean makeShuffledDna(LinkedList handler) {
		shuffleList(handler);

		for (int i = 0; i < handler.size(); i++) {
			tempTree1 = (Tree) handler.get(i);
			if (!tempTree1.getWillReproduce()) {
				while (++i < handler.size()) {
					tempTree1 = (Tree) handler.get(i);
					if (tempTree1.getWillReproduce()) {
						break;
					}
				}
			} else if (++i < handler.size()) {
				tempTree2 = (Tree) handler.get(i);
				if (tempTree2.getWillReproduce()) {
					newDna.shuffleDna(tempTree1.getDna(), tempTree2.getDna(), tempTree1.getNewDna(),
							tempTree2.getNewDna());
				} else {
					while (++i < handler.size()) {
						tempTree2 = (Tree) handler.get(i);
						if (tempTree2.getWillReproduce()) {
							newDna.shuffleDna(tempTree1.getDna(), tempTree2.getDna(), tempTree1.getNewDna(),
									tempTree2.getNewDna());
						}
					}
				}
			} else {
				tempTree1.setNewDna(tempTree1.getDna());
			}
		}
		return true;
	}
}
