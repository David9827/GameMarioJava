package entities;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Direction.*;

public class Crabby extends Enemy{
    private Rectangle2D.Float attachBox;
    private int attachBoxOffset;

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x,y, (int)(22* Game.SCALE), (int)(19*Game.SCALE));
        initAttachBox();
    }

    private void initAttachBox() {
        attachBox = new Rectangle2D.Float(x,y,(int) (82* Game.SCALE), (int) (19*Game.SCALE));
        attachBoxOffset = (int) (Game.SCALE * 30);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttachBox();
    }

    private void updateAttachBox() {
        attachBox.x = hitbox.x - attachBoxOffset;
        attachBox.y = hitbox.y;
    }

    public void drawAttachBox(Graphics g, int lvlOffset){
        g.setColor(Color.RED);
        g.drawRect((int ) (attachBox.x -lvlOffset), (int) attachBox.y, (int) attachBox.width, (int) attachBox.height);
    }

    private void updateBehavior(int[][] lvlData, Player player){
        if (firstUpdate)
            firstUpdatecheck(lvlData);
        if (inAir) {
            updateInAir(lvlData);
        }else {
            switch (enemyState){
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        moveTowardPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attachChecked = false;

                    if (aniIndex ==3 && !attachChecked)
                        checkPlayerHit(attachBox, player);
                    break;
                case HIT:
                    break;
            }
        }
    }

    public int flipX(){
        if (walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    public int flipW(){
        if (walkDir == RIGHT)
            return -1;
        else
            return 1;
    }

}
