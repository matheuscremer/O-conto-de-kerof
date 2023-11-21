package com.cremitoStudios.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.cremitoStudios.entities.Boss;
import com.cremitoStudios.entities.Player;
import com.cremitoStudios.main.Game;

public class UI {

	public void render(Graphics g) {
		if(Game.CUR_LEVEL == 3) {
			g.setColor(Color.red);
			g.setFont(new Font("arial",Font.BOLD,10));
			g.drawString("Belphegor", 100, 10);
			g.setColor(Color.red);
			g.fillRect(100, 15, 50, 5);
			g.setColor(Color.green);
			g.fillRect(100, 15,(int)((Boss.life/Boss.maxLife)*50), 5);
		}
		g.setColor(Color.red);
		g.fillRect(3, 150, 50, 7);
		g.setColor(Color.green);
		g.fillRect(3, 150,(int)((Game.player.life/Game.player.maxlife)*50), 7);
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,10));
		g.drawString((int)Game.player.life + "/" + (int)Game.player.maxlife,7,157);
		if(Boss.morte == false && Game.CUR_LEVEL == 3) {
			
		}
	}
}
