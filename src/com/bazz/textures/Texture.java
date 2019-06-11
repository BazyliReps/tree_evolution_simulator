package com.bazz.textures;

import java.awt.image.BufferedImage;

public class Texture {

	SpriteSheet blocksheet, branchsheet, minibranchsheet, trunksheet, mainrootsheet, rootsheet, minirootsheet,
			pineminibranchsheet, soilsheet;
	private BufferedImage blockSheet = null;
	private BufferedImage branchSheet = null;
	private BufferedImage miniBranchSheet = null;
	private BufferedImage trunkSheet = null;
	private BufferedImage rootSheet = null;
	private BufferedImage miniRootSheet = null;
	private BufferedImage mainRootSheet = null;
	private BufferedImage pineMiniBranchSheet = null;
	private BufferedImage soilSheet = null;

	public BufferedImage[] block = new BufferedImage[2];
	public BufferedImage[] branch = new BufferedImage[1];
	public BufferedImage[] minibranch = new BufferedImage[2];
	public BufferedImage[] trunk = new BufferedImage[1];
	public BufferedImage[] root = new BufferedImage[1];
	public BufferedImage[] miniroot = new BufferedImage[1];
	public BufferedImage[] mainroot = new BufferedImage[1];
	public BufferedImage[] pineminibranch = new BufferedImage[1];
	public BufferedImage[] soil = new BufferedImage[1];

	public Texture() {

		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			blockSheet = loader.loadImage("/blockSheet.png");
			branchSheet = loader.loadImage("/branchSheet.png");
			miniBranchSheet = loader.loadImage("/miniBranchSheet.png");
			trunkSheet = loader.loadImage("/trunkSheet.png");
			mainRootSheet = loader.loadImage("/mainRootSheet.png");
			rootSheet = loader.loadImage("/rootSheet.png");
			miniRootSheet = loader.loadImage("/miniRootSheet.png");
			pineMiniBranchSheet = loader.loadImage("/pineMiniBranchSheet.png");
			soilSheet = loader.loadImage("/soilSheet.png");

		} catch (Exception e) {
			e.printStackTrace();
		}

		blocksheet = new SpriteSheet(blockSheet);
		branchsheet = new SpriteSheet(branchSheet);
		minibranchsheet = new SpriteSheet(miniBranchSheet);
		trunksheet = new SpriteSheet(trunkSheet);
		mainrootsheet = new SpriteSheet(mainRootSheet);
		rootsheet = new SpriteSheet(rootSheet);
		minirootsheet = new SpriteSheet(miniRootSheet);
		pineminibranchsheet = new SpriteSheet(pineMiniBranchSheet);

		getTextures();

	}

	private void getTextures() {

		block[0] = blocksheet.grabImage(1, 1, 32, 32); // ziemia
		block[1] = blocksheet.grabImage(2, 1, 32, 32); // trawa

		branch[0] = branchsheet.grabImage(1, 1, 32, 128);

		minibranch[0] = /* pineminibranchsheet.grabImage(1, 1, 256, 128); */ minibranchsheet.grabImage(1, 1, 64, 32);
		minibranch[1] = minibranchsheet.grabImage(1, 2, 64, 32);

		trunk[0] = trunksheet.grabImage(1, 1, 32, 320);
		mainroot[0] = mainrootsheet.grabImage(1, 1, 64, 320);
		root[0] = rootsheet.grabImage(1, 1, 64, 320);
		miniroot[0] = minirootsheet.grabImage(1, 1, 64, 32);
	}

}
