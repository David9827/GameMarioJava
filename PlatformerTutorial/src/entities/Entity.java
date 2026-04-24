package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
	protected float x,y;
	protected int width, height;
	protected Rectangle2D.Float hitbox;
	
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

	}
	public void drawHitbox(Graphics g, int lvlOffset){
		g.setColor(new Color(200,60,100,80)); // set not color for hitbox
		g.drawRect((int)hitbox.x - lvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}
	protected void initHitbox(float x, float y, int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, width, height);
	}
//	public void updateHitbox(){
//		hitbox.x = (int) x;
//		hitbox.y = (int) y;
//	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
}
