package com.cremitoStudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.cremitoStudios.main.Game;
import com.cremitoStudios.main.Sound;
import com.cremitoStudios.world.Camera;
import com.cremitoStudios.world.World;

public class Enemy2 extends Entity{
	
	private double speed = 1.3;
	
	private int frames = 0,maxFrames = 10, index = 0,maxIndex = 3;

	private int maskx = 3,masky = 3, maskw = 4, maskh = 4;
	
	private BufferedImage[] sprites;
	
	private int life = 2;
	
	private boolean isDamaged = false; 
	
	private int damageFrames = 10, damageCurrent = 0;
	
	
	public Enemy2(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[4];
		sprites[0] = Game.spritesheet.getSprite(80, 48, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(80+16, 48, 16, 16);
		sprites[2] = Game.spritesheet.getSprite(80+32, 48, 16, 16);
		sprites[3] = Game.spritesheet.getSprite(80+48, 48, 16, 16);
	}

	public void tick() {
		/*maskx = 8;
		masky = 8;
		maskw = 10; 
		maskh = 10;*/
		if(isColiddingWithPlayer() == false) {
			if((int)x < Game.player.getX() && World.isFree((int)(x+speed),this.getY()) 
					&& !isColidding((int)(x+speed),this.getY())) {
				x+=speed;
			}else if((int)x > Game.player.getX() && World.isFree((int)(x-speed),this.getY())
					&& !isColidding((int)(x-speed),this.getY())) {
				x-=speed;
			}
			
			if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed)) &&
					!isColidding(this.getX(),(int)(y+speed))) {
				y+=speed;
			}else if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed)) &&
					!isColidding(this.getX(),(int)(y-speed))) {
				y-=speed;
			}
			}else {
				//estamos colidindo
				if(Game.rand.nextInt(100) < 10){
					Sound.hurtEffect.play();
					Game.player.life-=Game.rand.nextInt(60);
					Game.player.isDamaged = true;
	
				}
			}
				
			
				frames++;
				if(frames == maxFrames) {
					frames = 0;
					index++;
					if(index > maxIndex) {
						index = 0;
					}
				}
				
				collidingBullet();
				
				if(life <= 0) {
					destroySelf();
					return;
				}
				if(isDamaged) {
					this.damageCurrent++;
					if(this.damageCurrent == this.damageFrames) {
						this.damageCurrent = 0;
						this.isDamaged = false;
					}
				}
				
	}
		
	public void destroySelf() {
		Sound.enemyDie.play();
		Game.enemies2.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingBullet() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);						
				if(Entity.isColidding(this, e)) {	
					isDamaged = true;
					life --;
					Game.bullets.remove(i);
					return;									
			}
		}
		
	}
	
	
	
	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),16,16);
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColidding(int xnext,int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		for(int i = 0; i<Game.enemies2.size(); i++) {
			Enemy2 e = Game.enemies2.get(i);
			if(e == this)
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx,e.getY() + masky, maskw, maskh);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			} 
		}
		return false;
	}
	
	public void render(Graphics g) {
		if(!isDamaged)
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		else
			g.drawImage(Entity.ENEMY2_FEEDBACK, this.getX() - Camera.x,this.getY() - Camera.y,null);
		//g.setColor(Color.BLUE);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	}
	
}

