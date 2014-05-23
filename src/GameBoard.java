import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameBoard {

	public static final int ROWS = 4;
	public static final int COLS = 4;

	private final int startingTiles = 2;
	private Tile[][] board;
	private boolean dead;
	private boolean win;
	private BufferedImage gameBoard;
	private BufferedImage finalBoard;
	private int x;
	private int y;

	private static int SPACING = 10;
	public static int BOARD_WIDTH = (COLS + 1) * SPACING + COLS * Tile.WIDTH;
	public static int BOARD_HEIGHT = (ROWS + 1) * SPACING + ROWS * Tile.HEIGHT;

	private boolean hasStarted;

	public GameBoard(int x, int y) {
		this.x = x;
		this.y = y;
		board = new Tile[ROWS][COLS];
		gameBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		finalBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT,
				BufferedImage.TYPE_INT_RGB);

		Graphics g = gameBoard.getGraphics();
		// paint(g);
		createBoardImage(g);
		start();

	}

	// @Override
	// public void paint(Graphics g) {
	// super.paint(g);
	//
	// g.setColor(Color.darkGray);
	// g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
	// g.setColor(Color.lightGray);
	//
	// for (int row = 0; row < ROWS; row++) {
	// System.out.println("Check");
	// for (int col = 0; col < COLS; col++) {
	// int x = SPACING * (col + 1) + Tile.WIDTH * col;
	// int y = SPACING * (row + 1) + Tile.HEIGHT * row;
	// g.fillRoundRect(x, y, Tile.WIDTH, Tile.HEIGHT, Tile.ARC_WIDTH,
	// Tile.ARC_HEIGHT);
	//
	// }
	// }
	// }

	private void createBoardImage(Graphics g) {

		System.out.println("check1");
		// Graphics2D g = (Graphics2D) gameBoard.getGraphics();
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		g.setColor(Color.lightGray);

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				System.out.println("check2");
				int x = SPACING * (col + 1) + Tile.WIDTH * col;
				int y = SPACING * (row + 1) + Tile.HEIGHT * row;
				g.fillRoundRect(x, y, Tile.WIDTH, Tile.HEIGHT, Tile.ARC_WIDTH,
						Tile.ARC_HEIGHT);

			}
		}
	}

	private void start() {
		for (int i = 0; i < startingTiles; i++) {
			spawnRandom();
		}
	}

	private void spawnRandom() {
		Random random = new Random();
		boolean notValid = true;

		while (notValid) {
			int location = random.nextInt(COLS * ROWS);
			int row = (int) (location / ROWS);
			int col = location % COLS;
			Tile current = board[row][col];
			if (current == null) {
				int value = random.nextInt(10) < 9 ? 2 : 4;
				Tile tile = new Tile(value, getTileX(col), getTileY(row));
				board[row][col] = tile;
				notValid = false;
			}
		}

	}

	public int getTileY(int row) {
		return SPACING * (1 + row) + row * Tile.HEIGHT;
	}

	public int getTileX(int col) {
		return SPACING * (1 + col) + col * Tile.WIDTH;
	}

	public void render(Graphics2D g) {
		Graphics2D g2d = (Graphics2D) finalBoard.getGraphics();

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Tile current = board[row][col];
				if (current == null)
					continue;
				current.render(g2d);
			}
		}
		g2d.drawImage(gameBoard, 0, 0, null);
		g.drawImage(finalBoard, x, y, null);
		g2d.dispose();

	}

	void update() {
		checkKeys();

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Tile current = board[row][col];
				if (current == null)
					continue;
				current.update();
				// reset position
				if (current.getValue() == 2048) {
					win = true;
				}
			}
		}
	}

	private void checkKeys() {
		if (Keyboard.typed(KeyEvent.VK_LEFT)) {
			moveTiles(Direction.LEFT);
			if (!hasStarted)
				hasStarted = true;
		}
		if (Keyboard.typed(KeyEvent.VK_RIGHT)) {
			moveTiles(Direction.RIGHT);
			if (!hasStarted)
				hasStarted = true;
		}
		if (Keyboard.typed(KeyEvent.VK_UP)) {
			moveTiles(Direction.UP);
			if (!hasStarted)
				hasStarted = true;
		}
		if (Keyboard.typed(KeyEvent.VK_DOWN)) {
			moveTiles(Direction.DOWN);
			if (!hasStarted)
				hasStarted = true;
		}
	}

	private void moveTiles(Direction direction) {
		boolean canMove = false;
		int horizontalDirection = 0;
		int verticalDirection = 0;

		if (direction == Direction.LEFT) {
			horizontalDirection = -1;
			for (int row = 0; row < ROWS; row++) {
				for (int col = 0; col < COLS; col++) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection,
								verticalDirection, direction);
					} else
						move(row, col, horizontalDirection, verticalDirection,
								direction);
				}
			}
		} else if (direction == Direction.RIGHT) {
			horizontalDirection = 1;
			for (int row = 0; row < ROWS; row++) {
				for (int col = COLS - 1; col >= 0; col--) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection,
								verticalDirection, direction);
					} else
						move(row, col, horizontalDirection, verticalDirection,
								direction);
				}
			}
		}
		// 2 2 4 8 ---> 0 4 4 8
		// 2 2 4 8 ---> 0 0 0 16
		// 16 0 0 0 <--- 2 2 4 8

		else if (direction == Direction.UP) {
			verticalDirection = 1;
			for (int row = 0; row < ROWS; row++) {
				for (int col = 0; col < COLS; col++) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection,
								verticalDirection, direction);
					} else
						move(row, col, horizontalDirection, verticalDirection,
								direction);
				}
			}
		} else if (direction == Direction.DOWN) {
			verticalDirection = -1;
			for (int row = ROWS - 1; row >= 0; row--) {
				for (int col = 0; col < COLS; col++) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection,
								verticalDirection, direction);
					} else
						move(row, col, horizontalDirection, verticalDirection,
								direction);
				}
			}
		}

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Tile current = board[col][row];
				if (current == null)
					current.setCanCombine(true);
			}
		}

		if (canMove) {
			spawnRandom();
			// check dead
		}
	}

	private boolean move(int row, int col, int horizontalDir, int verticalDir,
			Direction dir) {
		// TODO Auto-generated method stub
		return false;
	}
}
