package com.cremitoStudios.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.cremitoStudios.main.Game;
import com.cremitoStudios.main.Sound;
import com.cremitoStudios.world.Camera;
import com.cremitoStudios.world.World;

public class Boss extends Entity{
	
	private double speed = 0.3;
	
	private int frames = 0,maxFrames = 10, index = 0,maxIndex = 3;
	private int deathframes = 0,deathmaxFrames = 10, deathindex = 0,deathmaxIndex = 3;

	private int maskx = 1,masky = 1, maskw = 14, maskh = 14;
	
	private BufferedImage[] sprites;
	private BufferedImage[] spritesDeath;
	
	public static double life = 50, maxLife = 50;
	
	private boolean isDamaged = false; 
	
	private int damageFrames = 10, damageCurrent = 0;
	
	public static boolean morte = true;

	
	public Boss(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[4];
		spritesDeath = new BufferedImage[4];
		sprites[0] = Game.spritesheet.getSprite(80, 64, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(80+16, 64, 16, 16);
		sprites[2] = Game.spritesheet.getSprite(80+32, 64, 16, 16);
		sprites[3] = Game.spritesheet.getSprite(80+48, 64, 16, 16);
		spritesDeath[0] = Game.spritesheet.getSprite(80, 80, 16, 16);
		spritesDeath[1] = Game.spritesheet.getSprite(80+16, 80, 16, 16);
		spritesDeath[2] = Game.spritesheet.getSprite(80+32, 80, 16, 16);
		spritesDeath[3] = Game.spritesheet.getSprite(80+48, 80, 16, 16);
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
				if(morte == false) {
					if(Game.rand.nextInt(100) < 10){
						Game.player.life-=100;
						Game.player.isDamaged = true;
		
					}
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
				
				if(life <= 0) {
					deathframes++;
					Sound.bossDie.play();
					if(deathframes == deathmaxFrames) {
						deathframes = 0;
						deathindex++;
						if(deathindex > deathmaxIndex) {
							deathindex = deathmaxIndex;
							speed = 0;
							morte = true;
							Game.boss.remove(this);
							Game.entities.remove(this);
							Sound.bossMusic.stop();
						}
					}
				}
				
				collidingBullet();
				
				
				if(isDamaged) {
					this.damageCurrent++;
					if(this.damageCurrent == this.damageFrames) {
						this.damageCurrent = 0;
						this.isDamaged = false;
					}
				}
				
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
		for(int i = 0; i<Game.boss.size(); i++) {
			Boss b = Game.boss.get(i);
			if(b == this)
				continue;
			Rectangle targetEnemy = new Rectangle(b.getX() + maskx,b.getY() + masky, maskw, maskh);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			} 
		}
		return false;
	}
	
	public void render(Graphics g) {
		if(!isDamaged && life > 0) {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else if(isDamaged && life > 0) {
		g.drawImage(Entity.BOSS_FEEDBACK, this.getX() - Camera.x,this.getY() - Camera.y,null);
		}else if(life <= 0) {
			g.drawImage(spritesDeath[deathindex], this.getX() - Camera.x, this.getY() - Camera.y, null);
			return;
		}
		
			
		//g.setColor(Color.BLUE);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	}
	
}

