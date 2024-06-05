package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import entity.NPC;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	// SCREEN SETTINGS
	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3;

	public final int tileSize = originalTileSize * scale; // 48x48 tile
	public  int maxScreenCol = 7;
	public  int maxScreenRow = 7;
	public  int screenWidth = tileSize * maxScreenCol; // 768 pixels
	public  int screenHeight = tileSize * maxScreenRow; // 576 pixels;

	int FPS = 60;
	public int globalValue = 0;
	int windowNum = 0;

	public boolean isPlayerInWindow = false;
	public boolean isSpacePressed = false;

	TileManager tileM = new TileManager(this, windowNum);
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	public CollisionChecker cChecker = new CollisionChecker(this);
	Player player = new Player(this, keyH);
	NPC npc = new NPC(this);

	public GamePanel(int num, int col, int row) {
		this.maxScreenCol = col;
		this.maxScreenRow = row;
		this.screenWidth = tileSize * col;
		this.screenHeight = tileSize * row;
		
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		this.windowNum = num;
		tileM = new TileManager(this, windowNum);
	}

	public void startGameThread() {

		gameThread = new Thread(this);
		gameThread.start();
	}

	public void stopGameThread() {
		gameThread = null;
	}

	@Override
	public void run() {

		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		while (gameThread != null) {

			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}

			if (timer >= 1000000000) {
				// System.out.println("FPS:" + drawCount);
				// System.out.println(player.x + " " + player.y);
				drawCount = 0;
				timer = 0;
			}
		}
	}

	public void update() {

		if (isPlayerInWindow) {
			player.update();
		}

		if(windowNum == 6) {
			npc.update();
		}
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		tileM.draw(g2);

		
		if(windowNum == 6) {
			npc.draw(g2);
		}


		if (isPlayerInWindow) {
			player.draw(g2);
		}

		g2.dispose();
	}
}
