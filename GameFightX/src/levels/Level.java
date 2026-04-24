package levels;

import entities.Crabby;
import main.Game;
import utilz.LoadSave;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utilz.HelpMethods.GetLevelData;
import static utilz.HelpMethods.GetCrab;

public class Level {
    private BufferedImage img;
    private int[][] lvlData;
    ArrayList<Crabby> crabs;

    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;

    public Level(BufferedImage img){
        this.img = img;
        createLvlData();
        createEnemies();
        calcLvlOffsets();
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = GetCrab(img);
    }

    private void createLvlData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }
    public int[][] getLvlData(){
        return lvlData;
    }
    public ArrayList<Crabby> getCrabs(){
        return crabs;
    }
    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

}
