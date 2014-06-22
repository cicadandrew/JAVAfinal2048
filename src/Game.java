import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener, Runnable {

	private static final long serialVersionUID = 1L;
	public static int WIDTH = 500;
	public static int HEIGHT = 500;
	public static final Font main = new Font("Bebas Neue Regular", Font.PLAIN,
			28);
	private int player;
	private Thread game;
	private boolean running;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
			BufferedImage.TYPE_INT_RGB);
	private GameBoardOne boardOne;
	private GameBoardTwo boardTwo;

	public Game(int player) {
		this.player = player;
		setFocusable(true);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addKeyListener(this);

		if (player == 1)
			boardOne = new GameBoardOne(WIDTH / 2 - GameBoardOne.BOARD_WIDTH
					/ 2, HEIGHT - GameBoardOne.BOARD_HEIGHT - 10);
		else
			boardTwo = new GameBoardTwo(WIDTH / 2 - GameBoardTwo.BOARD_WIDTH
					/ 2, HEIGHT - GameBoardTwo.BOARD_HEIGHT - 10);
	}

	private void update() {
		if (player == 2) {
			boardTwo.update();
			KeyboardTwo.update();

		} else {
			boardOne.update();
			KeyboardOne.update();
		}

	}

	private void render() {
		// Render Board*
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		if (player == 1)
			boardOne.render(g);
		else
			boardTwo.render(g);
		g.dispose();

		Graphics2D g2d = (Graphics2D) getGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

	}

	@Override
	public void run() {
		int fps = 0, updates = 0;
		long fpsTimer = System.currentTimeMillis();
		double nsPerUpdate = 1000000000.0 / 60;

		// last update time in nanosecond
		double then = System.nanoTime();
		double unprocessed = 0;

		while (running) {
			boolean shouldRender = false;
			double now = System.nanoTime();
			unprocessed += (now - then) / nsPerUpdate;
			then = now;

			// update queue
			while (unprocessed >= 1) {
				updates++;
				update();
				unprocessed--;
				shouldRender = true;
			}

			// render
			if (shouldRender) {
				fps++;
				render();
				shouldRender = false;
			} else {
				try {
					Thread.sleep(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// FPS Timer
		if (System.currentTimeMillis() - fpsTimer > 1000) {
			System.out.printf("%d fps %d updates", fps, updates);
			System.out.println();
			fps = 0;
			updates = 0;
			fpsTimer += 1000;
		}

	}

	public synchronized void start() {
		if (running)
			return;
		running = true;
		game = new Thread(this, "game");
		game.start();
	}

	public synchronized void stop() {
		if (!running)
			return;
		running = false;
		System.exit(0);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		KeyboardOne.keyPressed(e);

	}

	@Override
	public void keyReleased(KeyEvent e) {
		KeyboardOne.keyreleased(e);

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
