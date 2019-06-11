package com.bazz.window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bazz.containers.DnaShuffle;
import com.bazz.containers.TreeAreaContainer;
import com.bazz.containers.TreeHandler;
import com.bazz.textures.BufferedImageLoader;
import com.bazz.framework.GameSaver;
import com.bazz.inputs.KeyInput;
import com.bazz.framework.LevelCreator;
import com.bazz.inputs.MouseInput;
import com.bazz.framework.STATE;
import com.bazz.textures.Texture;
import com.bazz.treeparts.Dna;
import com.bazz.treeparts.Tree;
import java.awt.BasicStroke;

public class GameLoop extends Canvas implements Runnable {

    /**
     *
     */
    // private static final long serialVersionUID = -3024851680776623574L;
    public static int minis = 0;

    Tree tempTree;
    private File file;

    private boolean running = false;
    private Thread thread;
    private int width, height;
    public static int numberOfTrees = 0;
    public static int numberOfDyingTrees = 0;
    private int difficulty = 50;

    private TreeHandler handler;
    private Camera cam;
    private MouseInput mouse;
    private KeyInput keys;
    private LevelCreator levelCreator;

    public static Texture tex;
    private BufferedImage level = null;
    private BufferedImage soil = null;

    private int tx = 0;
    private int ty = 0;

    transient public static MainWindow mainWindow;
    private TreeAreaContainer areaContainer;
    transient private DnaShuffle dnaShuffle;
    private GameSaver saver;

    private STATE state = STATE.MENU;
    public static Graphics g;
    MainWindow mw;
    public static int months = 0;
    public static double angle = 45;
    public static boolean reproductionFlag = false;
    private int gameSize = 40000;
    private int numberOfAreas = 80;
    private boolean loading = false;
    private boolean firstStart = true;
    private int ticks = 0;
    private int year = 0;
    private Font fnt0;
    private int tickTime = 15;
    private boolean turboMode = false;
    
    public static int generations;
    
    public GameLoop(MainWindow mw) {
        this.mw = mw;
    }

    private void init() throws ClassNotFoundException {

        width = mw.getSize().width;
        height = mw.getSize().height;
        dnaShuffle = new DnaShuffle();
        saver = new GameSaver();
        tex = new Texture();
        BufferedImageLoader loader = new BufferedImageLoader();
        level = loader.loadImage("/level.png");
        soil = loader.loadImage("/soilSheet.png");
        levelCreator = new LevelCreator(gameSize, 0, soil);
        areaContainer = new TreeAreaContainer(gameSize, numberOfAreas);
        cam = new Camera(0f, 350f, height, width, levelCreator);
        if (loading) {
            handler = saver.loadGame(handler, this, cam, areaContainer, file);
        } else {
            handler = new TreeHandler(cam, this);
        }
        mouse = new MouseInput(this);
        keys = new KeyInput(handler, this, mw, cam);
        this.addMouseListener(mouse);
        this.addKeyListener(keys);
        fnt0 = new Font("TeX Gyre Schola Math", Font.PLAIN, 20);
    }

    private void reset() throws ClassNotFoundException {

        handler.resetHandler();
        areaContainer.resetAreaContainer();
        cam.resetCam();
        numberOfTrees = 0;
        numberOfDyingTrees = 0;
        year = 0;
        months = 0;
        if (loading) {
            handler = saver.loadGame(handler, this, cam, areaContainer, file);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("przerwano watek");
            }
        } else {
            handler = new TreeHandler(cam, this);
        }
        state = STATE.GAME;
    }

    public synchronized void start(boolean loading) {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        this.loading = loading;
        thread.start();
    }

    @Override
    public void run() {
        System.out.println("Niech rosną...");
        tempTree = null;
        try {
            if (firstStart) {
                init();
                firstStart = false;
            } else {
                reset();
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.requestFocus(); // to okno
        long lastTime = System.nanoTime();
        double amountOfTicks = 30;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        mw.resetTextFields();
        mw.setNumberOfTreesTextField(numberOfTrees);
        mw.setDyingTreesTextField(numberOfDyingTrees);
        mw.setMenuVisible(false);
        while (running) {

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (!turboMode) {
                while (delta >= 1) {
                    if (state == STATE.GAME || state == STATE.ADDTREE) {
                        tick();
                        ticks++;
                        if (ticks > 30) {
                            ticks = 0;
                            if (state == STATE.GAME || state == STATE.ADDTREE) {
                                mw.setMonthTextField(++months);
                                if (months == 12) {
                                    /*if (!dnaShuffle.makeShuffledDna(handler.getTreeListReference()))
									;*/
                                    reproductionFlag = true;
                                    if (months == 12) {
                                        months = 0;
                                        handler.reproduce();
                                        mw.setYearTextField(++year);
                                        handler.setYear(year);
                                        generations++;
                                    }
                                }
                            }
                        }
                    }
                    updates++;
                    delta--;

                }

                if (state == STATE.GAME || state == STATE.ADDTREE) {
                    render();
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(tickTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                frames++;

                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    // System.out.println("FPS: " + frames + " TICKS: " + updates);
                    frames = 0;
                    updates = 0;
                    reproductionFlag = false;
                }
            } else {
                if (tick()) {
                    ticks++;
                    if (ticks == 30) {
                        ticks = 0;
                        mw.setMonthTextField(++months);
                        if (months == 12) {
                            /*if (!dnaShuffle.makeShuffledDna(handler.getTreeListReference()))
									;*/
                            reproductionFlag = true;
                            if (months == 12) {
                                months = 0;
                                mw.setYearTextField(++year);
                                handler.setYear(year);
                                if(handler.reproduce());
                                render();
                                generations++;
                            } /*else if (months == 1) {
                                reproductionFlag = false;
                            }*/
                        }
                    }
                    try {
                        TimeUnit.MICROSECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        thread.interrupt();
    }

    private boolean tick() {
        if (state == STATE.GAME || state == STATE.ADDTREE) {
            if (handler.tick()) {
                cam.tick();
            }
        }
        updateResourcesInfo();
        return true;

    }

    private void render() {

        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {

            this.createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(new Color(141, 203, 246));
        g.fillRect(0, 0, getWidth(), getHeight());
        g2d.translate(cam.getX(), cam.getY());
        if (state == STATE.GAME || state == STATE.ADDTREE) {
            levelCreator.render(g);
            handler.render(g2d);
            if (state == STATE.ADDTREE) {
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
                g2d.setFont(fnt0);
                g2d.setColor(Color.BLACK);
                g2d.drawString("Planting Tree Mode", (-1) * (int) cam.getX() + width / 2 - 100,
                        (-1) * (int) cam.getY() + 50);
                g2d.drawString("click left mouse button to plant new tree or press \"q\" to exit this mode",
                        (-1) * (int) cam.getX() + width / 3 - 150, (-1) * (int) cam.getY() + 100);
            }
        }
        g2d.setColor(Color.yellow);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine((int)(-cam.getX() + width/2), (int)(-cam.getY()), (int)(width/2 -cam.getX() + 50 * Math.sin(Math.toRadians(angle))), (int)(-cam.getY() + 50 * Math.cos(Math.toRadians(angle))));
        g2d.translate(-cam.getX(), cam.getY());
        g.dispose();
        bs.show();
    }

    public static Texture getInstance() {
        return tex;
    }

    public STATE getGameState() {
        return state;
    }

    public void setGameState(STATE newState) {
        this.state = newState;
    }

    public void addTree(int x, GameLoop game) {
        Dna dna = new Dna();
        handler.addTree(new Tree((float) x + (-1) * cam.getX(), 0, handler, dna, /* mw, */ areaContainer, this));
        incrementNumberOfTrees();
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfTrees() {
        return numberOfTrees;
    }

    public void setNumberOfTrees(int trees) {
        this.numberOfTrees = trees;
    }

    public void setNumberOfDyingTrees(int trees) {
        this.numberOfDyingTrees = trees;
    }

    public void incrementNumberOfTrees() {
        mw.setNumberOfTreesTextField(++numberOfTrees);
    }

    public void decrementNumberOfTrees() {
        mw.setNumberOfTreesTextField(--numberOfTrees);
    }

    public void incrementNumberOfDyingTrees() {
        mw.setDyingTreesTextField(++numberOfDyingTrees);
    }

    public void decrementNumberOfDyingTrees() {
        mw.setDyingTreesTextField(--numberOfDyingTrees);
    }

    public void setGameHeight(int height) {
        this.height = height;
    }

    public int getGameHeight() {
        return height;
    }

    public void setGameWidth(int width) {
        this.width = width;
    }

    public int getGameWidth() {
        return width;
    }

    public int getGameSize() {
        return gameSize;
    }

    public int getNumberOfAreas() {
        return numberOfAreas;
    }

    public Camera getCamera() {
        return cam;
    }

    public GameLoop getGame() {
        return this;
    }

    private float water = 0, sun = 0, points = 0;

    public void setTreeData(boolean isLeftButton) {

        tempTree = handler.collision((-cam.getX() + mouse.getMouseX()), (-cam.getY() + mouse.getMouseY()));
        if (tempTree != null) {
            water = tempTree.getWater();
            sun = tempTree.getSun();
            points = tempTree.getPoints();
            if(!isLeftButton) {
                tempTree.displayHistory();
            }
        }

    }

    private void updateResourcesInfo() {
        if (tempTree != null) {
            try {
                mw.setWaterTextField(tempTree.getWater());
                mw.setSunTextField(tempTree.getSun());
                mw.setPointsTextField(tempTree.getPoints());
                mw.setSizeTextField(tempTree.getSize());
                mw.setNeighboursTextField(tempTree.getNeighbours());
                mw.setHeightTextField((int) tempTree.getMyHeight(), (int) tempTree.getAverageAreaHeight(),
                        tempTree.getMaximumHeightDisplay());
                mw.setAgeTextField(tempTree.getSeasons());
            } catch (NullPointerException np) {
                System.out.println("game focus gained");
            }
        }
    }

    public int getMonths() {
        return months;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDifficulty(int dif) {
        handler.setDifficulty(dif);
        difficulty = dif;
        System.out.println("difficulty: " + dif);
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void saveGame() {
        handler.setYear(year);
        handler.setTrees(numberOfTrees);
        handler.setDyingTrees(numberOfDyingTrees);
        saver.saveGame(handler, file);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean getRunning() {
        return running;
    }

    public void saveGame(File file) {
        handler.setYear(year);
        handler.setTrees(numberOfTrees);
        handler.setDyingTrees(numberOfDyingTrees);
        saver.saveGame(handler, file);
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setTickTime(int time) {
        tickTime = time;
    }

    public void setTurboMode(boolean turboMode) {
        this.turboMode = turboMode;
    }

    public boolean getTurboMode() {
        return turboMode;
    }
    
    

}
