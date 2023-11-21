package com.cremitoStudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.cremitoStudios.main.Game;
import com.cremitoStudios.main.Sound;
import com.cremitoStudios.main.Spritesheet;
import com.cremitoStudios.world.Camera;
import com.cremitoStudios.world.World;

public class Player extends Entity{  

	public boolean right,up,down,left;  //variaveis para movimentação do jogador
	public int right_dir = 0,left_dir = 1;
	public int up_dir = 2,down_dir = 3;
	public int dir = right_dir;
	public double speed = 0.7;
	
	public double life = 100, maxlife = 100; //vida do jogador
	
						//variaveis para definir os frames da animação do jogador
	private int frames = 0,maxFrames = 10, index = 0,maxIndex = 3;
	private boolean moved = false;
	
	//conjunto de animações do jogador para se movimentar, retirados do spritesheet
	private BufferedImage[] rightPlayer;	
	private BufferedImage[] leftPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage[] upPlayer;
	
	private BufferedImage playerDamage; //animação para tomar dano
	
	public boolean hasGun = false; //variavies para a arma	
	public int ammo = 0;
	public boolean shoot = false;
	
	public boolean isDamaged = false; //variaveis para tomar dano
	private int damageFrames = 0;
	
	//metodo principal do jogador onde define sua imagem e animação
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 64, 16, 16);
		
		for(int i =0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(0+ (i*16) , 0, 16, 16);

		}
		for(int i=0; i<4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(0 + (i*16), 16, 16, 16);

		}
		for(int i=0; i<4; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(0 + (i*16), 32, 16, 16);

		}
		for(int i=0; i<4; i++) {
			upPlayer[i] = Game.spritesheet.getSprite(0 + (i*16), 48, 16, 16);

		}
	}

	public void tick() { //metodo para redefinir ações do jogador 
		
						//movimentação
		moved = false;
		if(right && World.isFree((int)(x+speed),this.getY())) {
			moved = true;
			dir = right_dir;
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed),this.getY())) {
			moved = true;
			dir = left_dir;
			x-=speed; 
		}
		if(up && World.isFree(this.getX(),(int)(y-speed))) {
			moved = true;
			dir = up_dir;
			y-=speed;
		} 
		else if(down && World.isFree(this.getX(),(int)(y+speed))) {
			moved = true;
			dir = down_dir;
			y+=speed;
		} 
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
		
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
					Sound.walk.play();
				}
			}
		} 
		
									//colidir com objetos
		checkCollisionAmmo();
		checkCollisionLife();
		checkCollisionGun();
					
									//receber dano
		if(isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
									//atirar com sua arma
		if(shoot) {
			shoot = false;
			if(hasGun && ammo > 0) {
			ammo--;
			shoot = false;
			int dx = 0;
			int dy = 0;
			int px = 0;
			int py = 0;
			if(dir == right_dir) {
				px = 11;
				py = 7;
				dx = 1;
			}else if (dir == left_dir){
				px = -6;
				py = 6;
				dx = -1;
			}else if (dir == up_dir) {
				px = 10;
				py = 6;
				dy = -1;
			}else {
				px = 3;
				py = 6;
				dy = 1;
			}
			
			BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py, 3, 3, null, dx, dy);
			Game.bullets.add(bullet);
			}
		}
						//morrer
		if(life < 1) {
			life = 0;
			//Game over!
			Sound.morreu.play();
			Game.gameState = "GAME_OVER";
			Game.player.hasGun = false;
		}
					//definir sua camera centrada no personagem
		updateCamera();
	}
	
	public void updateCamera() { //metodo para centralizar a camera do jogo no personagem
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
		
	}
	  
	public void checkCollisionGun() {  //colidir com a arma, para pega-la
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColidding(this, atual)) {
					hasGun = true;
					ammo+=15;
					Sound.ammo.play();
					Game.entities.remove(i);
					return;
				}
			}
		}
	}
	
	public void checkCollisionAmmo() { //colidir com munição, para aumenta-la
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColidding(this, atual)) {
					ammo+=15;
					//System.out.println("Munição atual: " + ammo );
					Sound.ammo.play();
					Game.entities.remove(i);
					return;
				}
			}
		}
	}
	
	public void checkCollisionLife() { //colidir com kit medico para aumentar sua vida
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Life) {
				if(Entity.isColidding(this, atual)) {
					Sound.life.play();
					life+=25;
					if(life >=100)
						life = 100;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}
	
	public void render(Graphics g) { //renderizar os graficos do jogador
		if(!isDamaged) {
			if(dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.GUN_RIGHT,this.getX()+3- Camera.x,this.getY()+3-Camera.y,null);
				}
			}else if(dir == left_dir){
				g.drawImage(leftPlayer[index], this.getX()- Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.GUN_LEFT,this.getX()-10 - Camera.x,this.getY()+3 -Camera.y,null);
				}
			}else if(dir == up_dir) {
				g.drawImage(upPlayer[index], this.getX()- Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					if(hasGun) {
						g.drawImage(Entity.GUN_UP,this.getX()+5- Camera.x,this.getY()+3-Camera.y,null);
					}
				}
			}else if(dir == down_dir){
				g.drawImage(downPlayer[index], this.getX()- Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.GUN_DOWN,this.getX()-4- Camera.x,this.getY()+3-Camera.y,null);
				}
			}
		} else {
			g.drawImage(playerDamage, this.getX()-Camera.x, this.getY()-Camera.y,null);
		}
	}
}
