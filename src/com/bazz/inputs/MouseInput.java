package com.bazz.inputs;

import com.bazz.framework.STATE;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import com.bazz.window.GameLoop;

public class MouseInput implements MouseListener {

	public static int mouseX;
	public static int mouseY;
	int mx, my;
	private GameLoop game;

	public MouseInput(GameLoop game) {
		this.game = game;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

		mx = e.getX();
		my = e.getY();
		mouseX = mx;
		mouseY = my;

		if (game.getGameState() == STATE.ADDTREE && SwingUtilities.isLeftMouseButton(e)) {
			game.addTree(mx, game);
		} else if (game.getGameState() == STATE.GAME) {
			game.setTreeData(SwingUtilities.isLeftMouseButton(e));
		} 

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public int getMouseX() {
		return mx;
	}

	public int getMouseY() {
		return my;
	}

}
