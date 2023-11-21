package com.cremitoStudios.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.cremitoStudios.entities.Boss;
import com.cremitoStudios.entities.Bullet;
import com.cremitoStudios.entities.Enemy;
import com.cremitoStudios.entities.Enemy2;
import com.cremitoStudios.entities.Enemy3;
import com.cremitoStudios.entities.Entity;
import com.cremitoStudios.entities.Life;
import com.cremitoStudios.entities.Player;
import com.cremitoStudios.entities.Weapon;
import com.cremitoStudios.main.Game;
import com.cremitoStudios.main.Sound;
import com.cremitoStudios.main.Spritesheet;

public class World {

	private static Tile[] tiles; 			//renderizar o mundo em pixels de 16
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {	//relacionar todo o mapa atraves de uma imagem em pixels
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					
					int pixelAtual = pixels[xx + (yy*map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_Grama);
					if(pixelAtual == 0xFF0026FF) {
						//Player
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_ChaoBarco);
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
						
					}else if(pixelAtual == 0xFF3D107C) {
						//Player nivel 2 e 3
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_Grama);
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					
					}else if(pixelAtual == 0xFF697C42) {
						//Barril
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_Barril);
	
					
					}else if(pixelAtual == 0xFF5F635D) {
						//Caveira mapa
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_Caveira);
	
					
					}else if(pixelAtual == 0xFF267F00) {
						//grama
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_Grama);
						
					}else if(pixelAtual == 0xFF3A543A) {
						//grama contaminada
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_GramaContaminada);
						
					}else if (pixelAtual == 0xFFFFE97F) {
						//Areia
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_Areia);
						
					}else if (pixelAtual == 0xFF003E00) {
						//planta
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_Planta);
						
					}else if (pixelAtual == 0xFF3F7A54) {
						//planta2
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_Planta2);
						
					}else if (pixelAtual == 0xFFFFD800) {
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_AreiaAgua);
						
						//Areia/AguaPerto
					}else if (pixelAtual == 0xFF514400) {
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_ChaoBarco);
						
						//Chao do barco
					}else if (pixelAtual == 0xFF00FFFF) {
						//AguaPerto
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_AguaPerto);
						
					}else if (pixelAtual == 0xFF0094FF) {
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_AguaPertoAguaLonge);
						//AguaPerto/AguaLonge
					}else if (pixelAtual == 0XFF004A7F) {
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_AguaLonge);
						//AguaLonge
					}else if (pixelAtual == 0xFF00137F) {
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_Oceano);
						//Oceano
					}else if(pixelAtual == 0xFF000000) {
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_Fragmento);
						//Fragmentos do barco
					}else if(pixelAtual == 0xFF370000){
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_QuebradoCimaEsquerda);
						//Quebrado para a esquerda em cima
					}else if(pixelAtual == 0xFF7F6A00){
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_QuebradoCimaDireita);
						//Quebrado para a direita em cima
					}else if(pixelAtual == 0xFFAD846B){
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_LateralEsquerda);
						//Lateral esquerda
					}else if(pixelAtual == 0xFFFFC49E){
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_LateralDireita);
						//Lateral direita
					}else if(pixelAtual == 0xFFFF6A00){
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_LateralAcima);
						//Lateral acima
					}else if(pixelAtual == 0xFF7F0000){
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_LateralAbaixo);
						//Lateral abaixo
					}else if(pixelAtual == 0xFF4800FF) {
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_QuebradoBaixoEsquerda);
						//Quebrado para a esquerda em baixo
					}else if(pixelAtual == 0xFF5B7F00) {
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_QuebradoBaixoDireita);
						//Quebrado para a direita em baixo
					}else if(pixelAtual == 0xFFFF0000) {
						//inimigo tentaculos
						Enemy en = new Enemy(xx*16,yy*16,16,16,Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					}else if(pixelAtual == 0xFFFF5B5B) {
						//Caveira inimigo
						Enemy2 en = new Enemy2(xx*16,yy*16,16,16,Entity.ENEMY2_EN);
						Game.entities.add(en);
						Game.enemies2.add(en);
					
					}else if(pixelAtual == 0xFF808080) {
						//Caveira tentaculos inimigo
						Enemy3 en = new Enemy3(xx*16,yy*16,16,16,Entity.ENEMY3_EN);
						Game.entities.add(en);
						Game.enemies3.add(en);
					
					}else if(pixelAtual == 0xFF70FFD4) {
						//Boss
						Boss B = new Boss(xx*16,yy*16,16,16,Entity.BOSS_EN);
						Game.entities.add(B);
						Game.boss.add(B);
					
					}else if(pixelAtual == 0xFF480000) {
						//Lateral Curta baixo direita
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_CurvaDireitaBaixo);
					}else if(pixelAtual == 0xFF7F006E) {
						//Weapon
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_ChaoBarco);
						
						Game.entities.add(new Weapon(xx*16,yy*16,16,16,Entity.WEAPON_EN));
					}else if(pixelAtual == 0xFFFF006E) {
						//Life
						Life life = new Life(xx*16,yy*16,16,16,Entity.LIFE_EN);
						Game.entities.add(life);
					}else if(pixelAtual == 0xFFEAAC00) {
						Game.entities.add(new Bullet(xx*16,yy*16,16,16,Entity.BULLET_EN));
						//Ammo
					}
				}
			}
		}catch (IOException e) {
			
			e.printStackTrace();
		}
	}
								//quadrados que são colidíveis(paredes
	public static boolean isFree(int xnext,int ynext){ 

		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		if(x1 < 0 || y1 < 0 || x1 >= WIDTH || y1 >= HEIGHT) return false;     //<<<<<
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		if(x2 < 0 || y2 < 0 || x2 >= WIDTH || y2 >= HEIGHT) return false;     //<<<<<
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;

		if(x3 < 0 || y3 < 0 || x3 >= WIDTH || y3 >= HEIGHT) return false;     //<<<<<
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;

		if(x4 < 0 || y4 < 0 || x4 >= WIDTH || y4 >= HEIGHT) return false;     //<<<<<
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	}
	
		//método para reiniciar o jogo em caso de morte ou passar de nivel
	public static void restartGame(String level) {
		Sound.music.loop();
		Game.entities.clear();
		Game.enemies.clear();
		Game.enemies2.clear();
		Game.enemies3.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0,0,16,16, Game.spritesheet.getSprite(0, 16, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/" +level);  
		
		if(Game.CUR_LEVEL != 1) {
			Game.player.hasGun = true;
			Game.player.ammo = 15;
			Game.saveGame = true;
		}
		if(Game.CUR_LEVEL == 3) {
			Boss.morte = false;
			Boss.life = 50;
			Sound.music.stop();
			Sound.bossMusic.loop();	
		}
		return;
	}
	
	private static int parseint(String level) {
		// TODO Auto-generated method stub
		return 0;
	}
	public void render(Graphics g) { //metodo para renderizar o mapa
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}
