package com.cremitoStudios.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.cremitoStudios.main.Game;

public class Tile {

	public static BufferedImage TILE_Grama = Game.spritesheet.getSprite(144,144,16,16);
	public static BufferedImage TILE_GramaContaminada = Game.spritesheet.getSprite(144,96,16,16);
	public static BufferedImage TILE_Planta = Game.spritesheet.getSprite(144,128,16,16);
	public static BufferedImage TILE_Planta2 = Game.spritesheet.getSprite(144,112,16,16);
	public static BufferedImage TILE_Areia = Game.spritesheet.getSprite(96,144,16,16);
	public static BufferedImage TILE_AreiaAgua = Game.spritesheet.getSprite(80,144,16,16);
	public static BufferedImage TILE_AguaPerto = Game.spritesheet.getSprite(64,144,16,16);
	public static BufferedImage TILE_AguaPertoAguaLonge = Game.spritesheet.getSprite(48,144,16,16);
	public static BufferedImage TILE_AguaLonge = Game.spritesheet.getSprite(32,144,16,16);
	public static BufferedImage TILE_Oceano = Game.spritesheet.getSprite(16,144,16,16);
	public static BufferedImage TILE_Fragmento = Game.spritesheet.getSprite(128,144,16,16);
	public static BufferedImage TILE_Barril = Game.spritesheet.getSprite(4*16, 32, 16, 16);
	public static BufferedImage TILE_Caveira = Game.spritesheet.getSprite(4*16, 48, 16, 16);
	
	public static BufferedImage TILE_ChaoBarco = Game.spritesheet.getSprite(112,144,16,16);
	public static BufferedImage TILE_QuebradoBaixoDireita = Game.spritesheet.getSprite(96,112,16,16);
	public static BufferedImage TILE_QuebradoBaixoEsquerda = Game.spritesheet.getSprite(80,112,16,16);
	public static BufferedImage TILE_QuebradoCimaEsquerda = Game.spritesheet.getSprite(80,128,16,16);
	public static BufferedImage TILE_QuebradoCimaDireita = Game.spritesheet.getSprite(96,128,16,16);
	public static BufferedImage TILE_LateralEsquerda = Game.spritesheet.getSprite(112,112,16,16);
	public static BufferedImage TILE_LateralDireita = Game.spritesheet.getSprite(128,96,16,16);
	public static BufferedImage TILE_LateralAcima = Game.spritesheet.getSprite(112,128,16,16);
	public static BufferedImage TILE_LateralAbaixo = Game.spritesheet.getSprite(112,96,16,16);
	public static BufferedImage TILE_CurvaDireitaBaixo = Game.spritesheet.getSprite(128,128,16,16);
	
	private BufferedImage sprite;
	private int x,y;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
		
	}
}
