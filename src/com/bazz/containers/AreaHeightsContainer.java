/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bazz.containers;

import java.io.Serializable;

/**
 *
 * @author bazyli
 */
public class AreaHeightsContainer implements Serializable {

	private float[] areaHeights, areaAverageHeights, maximumHeightInArea, maximumHeightDisplay;
	private int[] highTreesInArea;
	private int size, areas, index;
	private float lengthOfArea = 0;

	public AreaHeightsContainer(int size, int areas) {
		initAreaHeightContainer(areas);
		this.size = size;
		this.areas = areas;
		this.lengthOfArea = size / areas;
	}

	private void initAreaHeightContainer(int areas) {
		areaHeights = new float[areas];
		highTreesInArea = new int[areas];
		areaAverageHeights = new float[areas];
		maximumHeightInArea = new float[areas];
		maximumHeightDisplay = new float[areas];
		for (int i = 0; i < areaHeights.length; i++) {
			areaHeights[i] = 0;
			highTreesInArea[i] = 0;
			areaAverageHeights[i] = 0;
			maximumHeightInArea[i] = 0;
			maximumHeightDisplay[i] = 0;
		}
	}

	private int getIndex(float x) {
		if (x > 0 && x < areas)
			return (int) (x / lengthOfArea);
		else
			return 0;
	}

	public void addNewSumOfHeights(float height, int numberOfMinis, float x) {
		index = getIndex(x);
		areaHeights[index] += height / numberOfMinis;
		highTreesInArea[index]++;
		if (height / numberOfMinis < maximumHeightInArea[index]) {
			maximumHeightInArea[index] = height / numberOfMinis;
			maximumHeightDisplay[index] = height / numberOfMinis;
		}
	}

	public void setAverageHeights() {
		for (int i = 0; i < areaHeights.length; i++) {
			areaAverageHeights[i] = areaHeights[i] / highTreesInArea[i];
		}
	}

	public int getAverageHeight(float x) {
		index = getIndex(x);
		return (int) areaAverageHeights[index];
	}

	public void resetAreaHeightsContainer() {
		for (int i = 0; i < areaHeights.length; i++) {
			areaHeights[i] = 0;
			highTreesInArea[i] = 0;
			maximumHeightInArea[i] = 0;
			maximumHeightDisplay[i] = 0;
		}
	}

	public float getMaximumHeight(float x) {
		index = getIndex(x);
		return maximumHeightInArea[index];
	}

	public float getMaximumHeightDisplay(float x) {
		index = getIndex(x);
		return maximumHeightDisplay[index];
	}
}
