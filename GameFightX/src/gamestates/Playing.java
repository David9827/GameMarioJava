package gamestates;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.GameOverLay;
import ui.LevelCompletedOverLay;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods{
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private boolean paused = false;
    private PauseOverlay pauseOverlay;
    private GameOverLay gameOverOverLay;
    private LevelCompletedOverLay levelCompletedOverLay;

    private int xLvlOffset;
    private int leftBorder = (int) (0.2*Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8*Game.GAME_WIDTH);
    private int maxLvlOffsetX; // chuyển phần lệch k nhìn được thành pixel

    private BufferedImage backgrondImg, bigClouds, smallClouds;
    private int[] smallCloudsPos;
    private Random rnd = new Random();
    private boolean gameOver;
    private boolean lvlCompleted;

    public Playing(Game game) {
        super(game);
        initClasses();

        backgrondImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigClouds = LoadSave.GetSpriteAtlas(LoadSave.THE_BIG_CLOUD);
        smallClouds = LoadSave.GetSpriteAtlas(LoadSave.THE_SMALL_CLOUD);
        smallCloudsPos = new int[8];
        for (int i=0; i < smallCloudsPos.length; i++)
            smallCloudsPos[i] =(int)(90 * Game.SCALE) + rnd.nextInt((int) (100* Game.SCALE));

        calcOffset();
        loadStartLevel();
    }
    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
    }

    private void calcOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player(200,200, (int) (64*Game.SCALE), (int) (40*Game.SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverLay = new GameOverLay(this);
        levelCompletedOverLay = new LevelCompletedOverLay(this);
    }
    @Override
    public void update() {
        if (paused)
            pauseOverlay.update();
        else if (lvlCompleted) {
            levelCompletedOverLay.update();
        } else if (!gameOver) {
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLvlData(), player);
            checkCloseToBorder();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;

        if (xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;
    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        player.resetAll();
        enemyManager.resetALlEnemies();
    }

    public void checkEnemyHit(Rectangle2D.Float attachBox) {
        enemyManager.checkEnemyHit((attachBox));
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgrondImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawBigClouds(g);
        drawSmallClouds(g);

        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver)
            gameOverOverLay.draw(g);
        else if (lvlCompleted)
            levelCompletedOverLay.draw(g);

    }

    private void drawBigClouds(Graphics g) {
        for (int i=0; i< 3; i++)
            g.drawImage(bigClouds, i*BIG_CLOUD_WIDTH - (int)(xLvlOffset * 0.3), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);

    }

    private void drawSmallClouds(Graphics g) {
        for (int i =0; i< smallCloudsPos.length; i++)
            g.drawImage(smallClouds, SMALL_CLOUD_WIDTH *3*i - (int)(xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
    }

    public void mouseDragged(MouseEvent e){
        if (!gameOver)
            if (paused)
                pauseOverlay.Dragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver)
            if (e.getButton() == MouseEvent.BUTTON1){
                player.setAttacking(true);
            }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if (lvlCompleted) {
                levelCompletedOverLay.mousePressed(e);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseReleased(e);
            else if (lvlCompleted) {
                levelCompletedOverLay.mouseReleased(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if (lvlCompleted) {
                levelCompletedOverLay.mouseMoved(e);
            }
        }
    }
    public void unpauseGame(){
        paused = false;
    }
    @Override //: gặp lỗi delay
    public void keyPressed(KeyEvent e) {
        if (gameOver)
            gameOverOverLay.keyPressed(e);
        else
            switch(e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
            }
    }
    @Override //: gặp lỗi delay
    public void keyReleased(KeyEvent e) {
        if (!gameOver)
            switch(e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
    }
    public void windowFocusLoss(){
        player.resetDirBoolen();
    }
    public Player getPlayer() {
        return player;
    }
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }
    public void setMaxLvlOffset(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }
}
