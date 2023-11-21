package com.cremitoStudios.main;

import java.io.*;
import javax.sound.sampled.*;

public class Sound {

	public static class Clips{
		public Clip[] clips;
		private int p;
		private int count;
		
		public Clips(byte[] buffer, int count) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
			if(buffer == null)
				return;
			
			clips = new Clip[count];
			this.count = count;
			
			for(int i = 0; i < count; i++) {
				clips[i] = AudioSystem.getClip();
				clips[i].open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer)));
				
			}
		}
		
		public void play() {
			if(clips == null) return;
			clips[p].stop();
			clips[p].setFramePosition(0);
			clips[p].start();
			p++;
			if(p>=count) p = 0;
		}
		
		public void loop() {
			if(clips == null) return;
			clips[p].loop(300);
		}
		public void stop() {
			clips[p].stop();
		}
	}
	
	public static Clips music = load("/music.wav",1);
	public static Clips hurtEffect = load("/hurt.wav",1);
	public static Clips enemyDie = load("/enemy.wav",1);
	public static Clips walk = load("/feet.wav",1);
	public static Clips ammo = load("/ammo.wav",1);
	public static Clips life = load("/life.wav",1);
	public static Clips bossDie = load("/bossdie.wav",1);
	public static Clips bossMusic = load("/bossmusic.wav",1);
	public static Clips shoot = load("/shoot.wav",1);
	public static Clips morreu = load("/bruh.wav",1);
	public static Clips fim = load("/finalmusic.wav",1);
	/*public static final Sound walk = new Sound("/feet.wav");
	public static final Sound ammo = new Sound("/ammo.wav");
	public static final Sound life = new Sound("/life.wav");
	public static final Sound bossDie = new Sound("/bossdie.wav");
	public static final Sound bossMusic = new Sound("/bossmusic.wav");
	public static final Sound shoot = new Sound("/shoot.wav");
	public static final Sound morreu = new Sound("/bruh.wav");
	public static final Sound fim = new Sound("/finalmusic.wav");*/
	
	private static Clips load(String name, int count) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataInputStream dis = new DataInputStream(Sound.class.getResourceAsStream(name));
			
			byte[] buffer = new byte[1024];
			int read = 0;
			while((read = dis.read(buffer)) >= 0) {
				baos.write(buffer,0,read);
			}
			dis.close();
			byte[] data = baos.toByteArray();
			return new Clips(data,count);
		}catch(Exception e) {
			try {
				return new Clips(null,0);
			}catch(Exception ee) {
				return null;
			}
		}
	}
}

