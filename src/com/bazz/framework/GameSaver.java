/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bazz.framework;

import com.bazz.containers.TreeAreaContainer;
import com.bazz.containers.TreeHandler;
import com.bazz.window.Camera;
import com.bazz.window.GameLoop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



/**
 *
 * @author bazyli
 */
public class GameSaver {

	public void saveGame(TreeHandler treeHandler, File file) {
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
			outputStream.writeObject(treeHandler);
			System.out.println("game saved");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TreeHandler loadGame(TreeHandler treeHandler, GameLoop game, Camera cam, TreeAreaContainer areaContainer,
			File file) throws ClassNotFoundException {
		treeHandler = null;
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
			treeHandler = (TreeHandler) inputStream.readObject();
			treeHandler.loadTreeHandler(game, cam, areaContainer);
			System.out.println("game loaded");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!treeHandler.equals(null)) {
			return treeHandler;
		} else {
			return null;
		}
	}

}
