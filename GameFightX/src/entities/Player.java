package entities;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import static main.Game.SCALE;
import static utilz.Constants.PlayerConstant.*;
import static utilz.HelpMethods.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Player extends Entity {
	private boolean moving = false, attacking = false;
	private int aniTick, aniIndex;
	private float aniSpeed = 25f;
	private long lastAnistick;
	private int playerAction = IDLE ;
	private boolean left, up, right, down, jump;
	private BufferedImage[][] animations;
	private float playerSpeed = 1.5f * SCALE;
	private int[][] lvlData;
	private float xDrawOffset = 21* SCALE;
	private float yDrawOffset = 4* SCALE;

	//Jumping and Gravity
	private boolean inAir = false;
	private float airSpeed = 0f;
	private float gravity = 0.04f * SCALE;
	private float jumpSpeed = -2.25f * SCALE;
	private float fallSpeedAfterCollision = 0.5f*SCALE;
	//StatusBar
	private BufferedImage statusBarImg;

	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);

	private int maxHealth = 100;
	private int currentHealth = maxHealth;
	private int healthWidth = healthBarWidth;
	//attackBox
	private Rectangle2D.Float attackBox;
	private boolean attachChecked;

	//flip player
	private int flipX;
	private int flipW = 1;

	private Playing playing;

	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		loadAnimations();
		initHitbox(x, y, (int) (20*SCALE), (int) (27*SCALE));
		initAttackBox();
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x,y,(int) (20* SCALE), (int) (20* SCALE));
	}

	public void update() {
		updateHealthBar();
		if (currentHealth <= 0){
			playing.setGameOver(true);
			return;
		}
		updateAttackBox();
		
		updatePos();
		if (attacking)
			checkAttach();
		setAnimations();
		updateAnimationTick();
	}

	private void checkAttach() {
		if (attachChecked || aniIndex != 1)
			return;
		attachChecked = true;
		playing.checkEnemyHit(attackBox);
	}

	private void updateAttackBox() {
		if (right){
			attackBox.x = hitbox.x + hitbox.width + (int)(SCALE*10);
		} else if (left) {
			attackBox.x = hitbox.x + hitbox.width - (int)(SCALE*50);
		}
		attackBox.y = hitbox.y + (SCALE*10);
	}

	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth/(float) maxHealth) *healthBarWidth);
	}

	public void render(Graphics g, int lvlOffset) {
		g.drawImage( animations[playerAction][aniIndex],
				(int) (hitbox.x - xDrawOffset) - lvlOffset + flipX,
				(int) (hitbox.y - yDrawOffset),
				width * flipW,
				height, null);
		//drawHitbox(g, lvlOffset);//ve hitbox
		//drawAttackBox(g,lvlOffset);
		drawUI(g);
	}

	private void drawAttackBox(Graphics g, int lvlOffsetX) {
		g.setColor(Color.RED);
		g.drawRect((int) (attackBox.x) - lvlOffsetX, (int) (attackBox.y),(int)(attackBox.width), (int)(attackBox.height));
	}

	private void drawUI(Graphics g) {
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.RED);
		g.fillRect(healthBarXStart +statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
	}

	private void updateAnimationTick() {
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(playerAction)){
				aniIndex=0;
				attacking = false;
				attachChecked =false;
			}
		}
	}
	private void setAnimations() {
		int startAni = playerAction;

		if (moving)
			playerAction = RUNNING;
		else
			playerAction = IDLE;

		if (inAir) {
			if (airSpeed < 0)
				playerAction = JUMP;
			else
				playerAction = FALLING;
		}

		if (attacking) {
			playerAction = ATTACK;
			if (startAni != ATTACK){
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		}

		if (startAni != playerAction) resetAniTick();
	}

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	public void updatePos() {
		moving = false;
		if (jump)
			jump();
//		if (!left && !right && !inAir)
//			return;
		if (!inAir)
			if ((!left && !right) || (left && right))
				return;

		float xSpeed = 0;
		if (right) {
			xSpeed += playerSpeed;
			flipX = 0;
			flipW = 1;
		}
		if (left) {
			xSpeed -= playerSpeed;
			flipX = width;
			flipW = -1;
		}
		if (!inAir){
			if (!IsEntityOnFloor(hitbox, lvlData)){
				inAir = true;
			}
		}

		if (inAir){
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			}else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0)
					resetAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}
		}else {
			updateXPos(xSpeed);
		}
		moving = true;
	}

	private void jump() {
		if (inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			hitbox.x += xSpeed;
		else
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
	}

	public void changeHealth(int value){
		currentHealth += value;
		if (currentHealth <= 0) {
			currentHealth = 0;
			//gameOver();
		}else if (currentHealth >= maxHealth) {
			currentHealth = maxHealth;
		}
	}

	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
		animations = new BufferedImage[7][8];
		for (int j =0; j < animations.length; j++)
			for(int i=0; i < animations[j].length; i++) {
				animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
			}
		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
	}
	public void loadLvlData(int[][] lvlData){
		this.lvlData = lvlData;
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}
	public void resetDirBoolen() {
		left = false;
		right = false;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}
	public void setJump(boolean jump){
		this.jump = jump;
	}

	public void resetAll() {
		resetDirBoolen();
		inAir = false;
		attacking = false;
		moving = false;
		playerAction = IDLE;
		currentHealth = maxHealth;

		hitbox.x = x;
		hitbox.y = y;
	}
}
