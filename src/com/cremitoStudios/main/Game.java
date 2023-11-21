package com.cremitoStudios.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JFrame;

import com.cremitoStudios.entities.Boss;
import com.cremitoStudios.entities.BulletShoot;
import com.cremitoStudios.entities.Enemy;
import com.cremitoStudios.entities.Enemy2;
import com.cremitoStudios.entities.Enemy3;
import com.cremitoStudios.entities.Entity;
import com.cremitoStudios.entities.Player;
import com.cremitoStudios.graficos.Menu;
import com.cremitoStudios.graficos.UI;
import com.cremitoStudios.world.World;

public class Game extends Canvas implements Runnable,KeyListener{

	
	private static final long serialVersionUID = 1L; //renderização do grafico da tela
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 4;
	
	public static int CUR_LEVEL = 1; //organização de niveis
	private BufferedImage image;
	
	public static List<Entity> entities;             //criando entidades do jogo
	public static List<Enemy> enemies;
	public static List<Enemy2> enemies2;
	public static List<Enemy3> enemies3;
	public static List<Boss> boss;
	public static Spritesheet spritesheet;
	public static Spritesheet spritesheetMenu;
	public static List<BulletShoot> bullets;    
	
	public static World world;					//Objetos do jogo
	
	public static Player player;
	 
	public static Random rand;
	
	public UI ui;
	
	public static String gameState = "MENU";    //estado do jogo
	
	private boolean showMessageGameOver = true;       //renderizar fim do jogo
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	public Menu menu;
	 
	public static boolean saveGame = false;
	
	
	public Game() {      			  //criação do mundo
		Sound.music.loop();
		rand = new Random();
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		
		//Inicializando objetos
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		enemies2 = new ArrayList<Enemy2>();
		enemies3 = new ArrayList<Enemy3>();
		boss = new ArrayList<Boss>();
		bullets = new ArrayList<BulletShoot>();
		menu = new Menu();
		//entidade jogador
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0,0,16,16, spritesheet.getSprite(0, 16, 16, 16));
		entities.add(player);
		world = new World("/level1.png");  
		
		menu = new Menu();
	}
	
	public void initFrame() {                  //metodo para frames
		frame = new JFrame("O conto de Kerof");  
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		
		//Icone da janela
		Image imagem = null;
		try {
		imagem = ImageIO.read(getClass().getResource("/Icon.png"));
		}catch (IOException e) {
		e.printStackTrace();
		}
		Toolkit tollkit = Toolkit.getDefaultToolkit();
		frame.setIconImage(imagem);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
		
	public synchronized void start() {		//metodo iniciar jogo
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {		// metodo de parar jogo
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {	//metodo main
		Game game = new Game();
		game.start();
	}
		
	public void tick() { //metodo instantaneo para redefinição de ações
		if(gameState == "NORMAL") {
			  if(this.saveGame) {
				  this.saveGame = false;
				  String[] opt1 = {"level"};
				  int[] opt2 = {this.CUR_LEVEL};
				  Menu.saveGame(opt1,opt2,10);
				  System.out.println("Jogo salvo!");
			  }
		  this.restartGame = false;
		  for(int i = 0 ; i< entities.size(); i++) {
			  Entity e = entities.get(i);
			  e.tick();
		  }
		  
		  for(int i = 0; i < bullets.size(); i++) {
			  bullets.get(i).tick();
		  }
		  
		  if(enemies.size() == 0 && enemies2.size() == 0 && enemies3.size() == 0 && Boss.morte == true) {
			  //Próximo level 
			  CUR_LEVEL++;	
			  if(CUR_LEVEL == 4) {
				  System.out.println(CUR_LEVEL);
				  Sound.bossMusic.stop();
			  	  Sound.fim.play();
				  gameState = "FIM";	
			  }else {
			  String newWorld = "level" +CUR_LEVEL+".png";
			  System.out.println(newWorld);
			  World.restartGame(newWorld);
			  }
		  }
	  }else if(gameState == "GAME_OVER" ||gameState == "FIM") {
		  this.framesGameOver++;
		  if(this.framesGameOver == 35) {
			  this.framesGameOver = 0;
			  if(this.showMessageGameOver)
				  this.showMessageGameOver = false;
				  else
					  this.showMessageGameOver = true;
		  }
		  
		  if(restartGame) {
			  this.restartGame = false;
			  this.gameState = "NORMAL";
			  player.hasGun = false;
			  CUR_LEVEL = 1;
			  Sound.bossMusic.stop();
			  Sound.music.stop();
			  String newWorld = "level" +CUR_LEVEL+".png";
			  System.out.println(newWorld);
			  World.restartGame(newWorld);
		  }
	  }else if(gameState == "MENU") {
		  //
		  menu.tick();
		  
	  }
	}
	
	public void render() {						//metodo de renderização de graficos
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,255,0));
		g.fillRect(0,0,WIDTH,HEIGHT);
		
		//Renderização do jogo
		
		world.render(g);
		for(int i = 0 ; i< entities.size(); i++) {
			  Entity e = entities.get(i);
			  e.render(g);
		}
		for(int i = 0; i < bullets.size(); i++) {
			  bullets.get(i).render(g);
		}
		ui.render(g);
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0 ,0, WIDTH*SCALE,HEIGHT*SCALE,null);
		g.setFont(new Font("arial", Font.BOLD,20));
		g.setColor(Color.white);
		g.drawString("Munição: " + player.ammo, 230, 620);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0,0,WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial", Font.BOLD,36));
			g.setColor(Color.white);
			g.drawString("E você morreu? Eu sabia que este bebado era louco...", 20, 330);
			g.setFont(new Font("arial", Font.BOLD,24));
			if(showMessageGameOver)
				g.drawString(">Pressione -Enter- para recomeçar seu conto<", 220, 380);
		} else if(gameState == "MENU") {
			menu.render(g);
		}else if(Game.gameState == "FIM") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0,0,WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial", Font.BOLD,36));
			g.setColor(Color.white);
			g.drawString("E então Kerof Salvou a ilha!...", 235, 100);
			g.setFont(new Font("arial", Font.BOLD,24));
			g.drawString("Mas ninguem tinha certeza se", 320, 150);
			g.drawString("aquela história realmente aconteceu... ", 270, 200);
			g.setFont(new Font("arial", Font.BOLD,24));
			if(showMessageGameOver)
				g.drawString(">Pressione -Enter- para recomeçar seu conto<", 220, 380);
		}
		bs.show();
	}
	
	@Override
	public void run() {		//metodo para definir fps e sistematizar "tempo" no jogo
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {				//metodo para pressionar tecla
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || 
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
			
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || 
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || 
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if(gameState == "MENU") {
				menu.up = true;
			}
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || 
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down= true;
			
			if(gameState == "MENU") {
				menu.down = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.shoot = true;
			if(player.ammo>0) {
			Sound.shoot.play();
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			this.restartGame = true;
			if(gameState == "MENU" || gameState == "FIM" ) 
				menu.enter = true;		
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			menu.pause = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {				//metodo para soltar tecla
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || 
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || 
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || 
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || 
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down= false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {       //metodo para apertar e soltar tecla
		
	}
}
