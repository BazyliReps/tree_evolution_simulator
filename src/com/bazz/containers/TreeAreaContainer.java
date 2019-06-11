/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bazz.containers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author bazyli
 */
public class TreeAreaContainer implements Serializable {

	private final int width, numberOfAreas, areaLength;
	private int index;
	private int tempVal;

	private ArrayList<Integer> treesInArea = new ArrayList<>();
	private ArrayList<Area> areas = new ArrayList<>();

	public TreeAreaContainer(int width, int numberOfAreas) {
		this.width = width;
		this.numberOfAreas = numberOfAreas;
		this.areaLength = (int) (width / numberOfAreas);
		for (int i = 0; i < numberOfAreas; i++) {
			treesInArea.add(new Integer(0));
			areas.add(new Area());
		}
	}

	public void changeNumberOfTreesInArea(float x, boolean isBorn) {
		index = getIndex(x);
		tempVal = treesInArea.get(index);
		if (isBorn) {
			treesInArea.set(index, ++tempVal);
			areas.get(index).notifyListeners(tempVal, "neighbours", tempVal, this.tempVal);
		} else {
			treesInArea.set(index, --tempVal);
			areas.get(index).notifyListeners(tempVal, "neighbours", tempVal + 1, this.tempVal);
		}
	}

	public int getIndex(float x) {
		return (int) (x / areaLength);
	}

	public int checkHowManyTreesInArea(float x) {
		index = getIndex(x);
		return treesInArea.get(index);
	}

	public void addListener(TreeAreaListener listener, int x) {
		areas.get(getIndex(x)).addListenerToArea(listener);
	}

	public void resetAreaContainer() {
		for (int i = 0; i < numberOfAreas; i++) {
			treesInArea.set(i, 0);
			areas.get(i).resetArea();
		}
	}

}

class Area implements Serializable {

	private final ArrayList<PropertyChangeListener> listeners = new ArrayList<>();

	public void notifyListeners(Integer I, String property, Integer oldVal, Integer newVal) {
		for (PropertyChangeListener tree : listeners) {
			tree.propertyChange(new PropertyChangeEvent(this, property, oldVal, newVal));
		}
	}

	public void addListenerToArea(PropertyChangeListener newListener) {
		listeners.add(newListener);
	}

	public void removeListener(PropertyChangeListener deadListener) {
		listeners.remove(deadListener);
	}

	public void resetArea() {
		listeners.removeAll(listeners);
	}

}
