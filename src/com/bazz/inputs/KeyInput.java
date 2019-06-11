package com.bazz.inputs;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.bazz.containers.TreeHandler;
import com.bazz.framework.STATE;
import com.bazz.window.Camera;
import com.bazz.window.GameLoop;
import com.bazz.window.MainWindow;

public class KeyInput extends KeyAdapter {

	private TreeHandler handler;
	private GameLoop game;
	private MainWindow mw;
	private Camera cam;

	int key;

	public KeyInput(TreeHandler handler, GameLoop game, MainWindow mw, Camera cam) {
		this.handler = handler;
		this.game = game;
		this.mw = mw;
		this.cam = cam;
	}

	public void keyPressed(KeyEvent e) {
		key = e.getKeyCode();
		if (game.getGameState() == STATE.GAME || game.getGameState() == STATE.ADDTREE) {

			if (key == KeyEvent.VK_D) {
				cam.setVelX(-40);
			}
			if (key == KeyEvent.VK_A) {
				cam.setVelX(40);
			}
			if (key == KeyEvent.VK_W) {
				cam.setVelY(40);
			}
			if (key == KeyEvent.VK_S) {
				cam.setVelY(-40);
			}

			if (key == KeyEvent.VK_P) {
				game.setGameState(STATE.ADDTREE);
			}
			if (game.getGameState() == STATE.ADDTREE && key == KeyEvent.VK_Q) {
				game.setGameState(STATE.GAME);
			}
			if (key == KeyEvent.VK_ESCAPE) {
				game.setGameState(STATE.MENU);
				mw.setMenuVisible(true);

			}
                        if(key == KeyEvent.VK_T) {
                            game.setTurboMode(!game.getTurboMode());
                        }
		}
	}

	public void keyReleased(KeyEvent e) {

		int key = e.getKeyCode();

		if (key == KeyEvent.VK_D) {
			cam.setVelX(0);
		}
		if (key == KeyEvent.VK_A) {
			cam.setVelX(0);
		}
		if (key == KeyEvent.VK_W) {
			cam.setVelY(0);
		}
		if (key == KeyEvent.VK_S) {
			cam.setVelY(0);
		}

	}

}
